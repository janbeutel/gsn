
__author__      = "Ben Buchli <bbuchli@ethz.ch>"
__copyright__   = "Copyright 2010, ETH Zurich, Switzerland"
__license__     = "GPL"
__version__     = "$Revision$"
__date__        = "$Date$"
__id__          = "$Id$"
__source__      = "$URL$"
'''
backlog imports
'''

import BackLogMessage
from AbstractPlugin import AbstractPluginClass

'''
stdlib imports
'''
import struct
#from time import gmtime, time, strftime
import time
from threading import Event, Thread
import GPSDriver
from SpecialAPI import PowerControl

from ScheduleHandler import SUBPROCESS_BUG_BYPASS
if SUBPROCESS_BUG_BYPASS:
    import SubprocessFake
    subprocess = SubprocessFake
else:
    import subprocess
    
from SpecialAPI import Statistics
'''
defines
'''

DEFAULT_BACKLOG = True

NAV_TYPE = 1
RAW_TYPE = 2
#Which conversion must be applied to this data at the GSN end? 0 -> NONE, 1 -> RAW->ASCII
RAW_DATA_VERSION = 1
NAV_DATA_VERSION = 1
#Reads raw GPS messages from a u-blox device and sends them to GSN

class GPSPluginClass(AbstractPluginClass):
    '''
    ##########################################################################################
    __init__
    ##########################################################################################
    '''
    
    def __init__(self, parent, config):
        Thread.__init__(self)
    
    	AbstractPluginClass.__init__(self, parent, config, DEFAULT_BACKLOG, needPowerControl=True)
        self.info('Init GPSPlugin...')
        self._stopped = False
        self._busy = True
        
        # The measurement interval in seconds
        self._interval = float(self.getOptionValue('poll_interval'))
    	# The measurement time in seconds
        self._measTime = int(self.getOptionValue('measurement_time'))
        # The device identifier
        self._deviceStr = self.getOptionValue('gps_device')
        # The GPS Mode (nav or raw)
        self._mode = self.getOptionValue('gps_mode')
        # The GPS loggin Mode (binary or ascii)
        self._logMode = self.getOptionValue('gps_log_mode')

        if (self._logMode == "ascii"):
            RAW_DATA_VERSION = 0
    	
        #For quality measurement
        self._measurementNo=0
        self._goodSatelliteCounter=0
        self._SatelliteCounter=0
           
        self._runEv = Event()
            
        #Wlan must only be cycled when in dc mode!!
        if (self.isDutyCycleMode()):    	
            self._WlanThread = WlanThread(self,10,10)
        
        #counter
        fp = open("/media/card/backlog/GPS_cnt.txt","rw")
        cnt = int(fp.readline())
        if (cnt >= (pow(2,32)-1) or cnt == ""):
            self._cnt = 0
        else:
            self._cnt = cnt
        fp.close()
        
        self._stats = Statistics()
        self.info("Starting with " + str(self._cnt))
        self._counterID = self._stats.createCounter(0,self._cnt)
        
        self.debug("Done init GPS Plugin")


    '''
    ##########################################################################################
    action():   This function will be fired by the schedule handler each time
                this plugin is scheduled. The function is started in a new
                thread.
        
                @param parameters:  The parameters as one string given in the
                                    schedule file.
    ##########################################################################################
    '''
    def action(self,parameters):
        self.info("Action called!")
        if (not self.isDutyCycleMode()):
            self.warning("GPSPlugin's action called even though we are not in duty-cycle mode!")
            return
        self.runPlugin(parameters)
        
    '''
    ##########################################################################################
    run():     This function gets called when the plugin is loaded. In duty-cycle mode the
               action() function takes care of running the plugin
    ##########################################################################################
    '''
    def run(self):
        if (self.isDutyCycleMode()):
            return
        else:
            self.runPlugin('')
 
    '''
    ##########################################################################################
    runPlugin():  This function is essentially the run function
    ##########################################################################################
    '''
    def runPlugin(self,param):  
        self.gps = GPSDriver.GPSDriver([self._deviceStr, self._interval,self._mode])
        # scheduling my death...
        self._endTime = time.time() + self._measTime
        
        if (not self.gps._isInitialized()):
            self.error('Initializing GPS Plugin failed!!')
            return
        
        self.info('GPSPlugin running...')
        if (self.isDutyCycleMode()):  
            self._WlanThread.start()
        # Prepare for precise timing
        now = time.time()
        while (time.time() <= self._endTime and not self._stopped):                    
            rawMsg = self.gps._read("")
            if (rawMsg):
                if (self._mode == "nav"):
                    #self.processMsg(self.getTimeStamp(), [NAV_TYPE, NAV_DATA_VERSION]+rawMsg[2])
                    pass
                elif (self._mode == "raw" and self._logMode == "binary"):
                    self._stats.counterAction(self._counterID)
                    cnt = self._stats.getCounterValue(self._counterID)
                    self.processMsg(self.getTimeStamp(), [RAW_TYPE, RAW_DATA_VERSION, cnt, ((rawMsg[4]-8)/24), bytearray(rawMsg[2])])
                elif (self._mode == "raw" and self._logMode == "ascii"):
                    self.processMsg(self.getTimeStamp(), [RAW_TYPE, RAW_DATA_VERSION, parseRawMsg(rawMsg[2])])
                self.info("GPS Sample Nr: " + str(cnt))
                self.writeToFile(cnt)
                self.info(str((rawMsg[4]-8)/24) + " Satellites (" + str(rawMsg[4]) + " Bytes)")
            self._runEv.wait(self._interval-1)

        # die...
        if (self.isDutyCycleMode()):  
            self._WlanThread.join()
        self.debug('GPSPlugin died...')
    
        end = time.time()
        '''
        self.info('Number of measurements: ' + str(self.gps.measurementNo))
        self.info('Number of satellites: '+ str(self.gps._SatelliteCounter))
        self.info('Number of satellites above thresholds: '+str(self.gps._goodSatelliteCounter))
        self.info("Killed " + str(self.gps._zombiesKilled))
        self.info("Serial disconnected " + str(self.gps._serialCount))
        '''
        self.stop()
    
    '''
    ##########################################################################################
    parseRawMsg()
    ##########################################################################################
    '''
    def _parseRawMsg(self, rawMsg):
    	if rawMsg!=0:
            dataPackage = False
            payload = rawMsg[2]
            # the first 8 bytes are: GPS Time (4B), GPS week (2B), Number of satellites following (1B), Reserved (1B)
            gps_time, gps_week, svs = struct.unpack('<ihB', payload[0:7])
            self.info('GPS Time: '+str(gps_time)+" - "+str(gps_week))
            self.info('Number of satellites following: ' +str(svs))
            # extract data for each SV 
            for i in range(0, svs):
                #Byte offset for SV data
                startIndex = 8+i*24
                #Payload format: Carrier Phase (8B - double), Pseudorange (8B - double), 
                #                Doppler (4B - int), SV nbr (1B - unsigned char), 
                #                Quality (1B - signed char), C/No (1B - signed char), 
                #                LLI (1B - unsigned char)
                
                carrier_phase, pseudorange, doppler, sv, quality, cno, lli = struct.unpack_from('<2diB2bB', payload, startIndex)
                
                dataPackage = [gps_time, gps_week, svs, carrier_phase, pseudorange, doppler, sv, quality, cno, lli]
                
                if quality>=int(self.getOptionValue('quality_threshold')) and cno>=int(self.getOptionValue('signal_threshold')):
                      self._goodSatelliteCounter=self._goodSatelliteCounter+1
                self._SatelliteCounter=self._SatelliteCounter+1
    			
    		if (dataPackage == False):
    			self.warning("parseMsg: WARNING! Function returned nothing!")
    			return dataPackage
    		self._measurementNo += 1
    		return dataPackage
        else:
            self.warning("parseMsg: WARNING! MSG packet was empty!")
            return False

    '''
    ##########################################################################################
    parseNavMsg()
    ##########################################################################################
    '''
    def _parseNavMsg(self, msg):
    	if (msg):
            self.info(str(msg[2]))
            #dataPackage = [NAV_TYPE, ]
    	else:
    		self.warning("WARNING: MSG packet was empty!")
        
    '''
    ##########################################################################################
    stop()
    ##########################################################################################
    '''
    def stop(self):
        self.info('GPSPlugin stopping...')
        self._runEv.set()
        if (self.isDutyCycleMode()):  
            self._WlanThread.stop()
        self._busy = False
        self._stopped = True
        self.info('GPSPlugin stopped')

    '''
    ##########################################################################################
    getMsgType()
    ##########################################################################################
    '''
    
    def getMsgType(self):
        return BackLogMessage.GPS_MESSAGE_TYPE
    
    '''
    ##########################################################################################
    isBusy(): This function is required to let the parent know we're still alive and busy.
    ##########################################################################################
    '''
    def isBusy(self):
        return self._busy
        
    
    def needsWLAN(self):
        # TODO: implement return value
        return False
    
    def writeToFile(self, val):
        fp = open("/media/card/backlog/GPS_cnt.txt","w")
        fp.write(str(val))
        fp.close()
    
#############################################################
# Class WlanThread
#############################################################
class WlanThread(Thread):

    #*********************************************************
    # init()
    #*********************************************************
    def __init__(self, parent,uptime=10,downtime=40):
        Thread.__init__(self)
        self._uptime=uptime*60
        self._downtime=downtime*60
        self._parent = parent
        self._work = Event()
        self._stopped = False
        self._online = parent.getPowerControlObject().getWlanStatus()
    
    #*********************************************************
    # run()
    #*********************************************************
    def run(self):
        self._parent._logger.info('WlanThread: started')
        while not self._stopped: 
            self._parent._logger.info('WlanThread: Waiting for ' + str(self._uptime) + ' secs before cycling WLAN')
            self._work.wait(self._uptime)
            if (self._parent.getPowerControlObject().getWlanStatus()):
                p = subprocess.Popen('/usr/bin/who')
                p.wait()
                user = p.communicate()[0]
                self._parent._logger.info(str(user))
                #self._parent._logger.info(str("HOST\nroot" in user))
                start = time.time()
                while ((("HOST\nroot" in user) or (self._parent.isResendingDB())) and not self._stopped):
                    self._parent._logger.info("Flushing: " + str(self._parent.isResendingDB()))
                    self._parent._logger.info('Someone is logged in or we are flushing DB... NOT power cycling WLAN!')
                    self._parent._logger.info('Waiting for 10 sec')
                    self._work.wait(10)
                    p = subprocess.Popen('/usr/bin/who')
                    p.wait()
                    user = p.communicate()[0]
                    #user = commands.getoutput('/usr/bin/who')
                    #self._parent._logger.info(str(user))
                if (not self._stopped):
                    duration = time.time() - start
                    self._parent.getPowerControlObject().wlanOff()
                    self._parent._logger.info('Waiting for ' + str(self._downtime-duration) + ' secs')
                    self._work.wait(self._downtime - duration)
            #Todo: Turn Wlan on
            if (not self._stopped):
                self._parent._logger.info("We are not online, so turn on wlan")
                self._parent.getPowerControlObject().wlanOn()  
        self._parent._logger.info('WlanThread: died')

    #*********************************************************
    # stop()
    #*********************************************************
    def stop(self):
        self._stopped = True
        self._work.set()
        self._parent._logger.info('WlanThread: stopped')
        
