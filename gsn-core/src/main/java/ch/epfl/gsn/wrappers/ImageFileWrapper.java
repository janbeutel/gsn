/**
* Global Sensor Networks (GSN) Source Code
* Copyright (c) 2006-2016, Ecole Polytechnique Federale de Lausanne (EPFL)
* Copyright (c) 2020-2023, University of Innsbruck
* 
* This file is part of GSN.
* 
* GSN is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* GSN is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with GSN.  If not, see <http://www.gnu.org/licenses/>.
* 
* File: src/ch/epfl/gsn/wrappers/ImageFileWrapper.java
*
* @author Sofiane Sarni
* @author Davide De Sclavis
* @author Manuel Buchauer
* @author Jan Beutel
*
*/

package ch.epfl.gsn.wrappers;

import java.io.*;
import java.util.Arrays;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.slf4j.LoggerFactory;

import ch.epfl.gsn.beans.AddressBean;
import ch.epfl.gsn.beans.DataField;
import ch.epfl.gsn.beans.StreamElement;

import org.slf4j.Logger;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat;

/*
* This wrapper searches in a local directory for images
* that match the file-mask (given as regular expression)
* with the defined rate (rate parameter).
* The timestamp for the image is created from the file name following the time-format parameter
* See ./virtual-sensors/samples/imagefileexample.xml for an example
* */
public class ImageFileWrapper extends AbstractWrapper {

    private static final transient Logger logger = LoggerFactory.getLogger(ImageFileWrapper.class);

    private int threadCounter;
    private String imagesDirectory;
    private String fileExtension;
    private String timeFormat;
    private String fileMask;
    private long rate;

    private long latestProcessedTimestamp;

    private static final String PARAM_DIRECTORY = "directory";
    private static final String PARAM_FILE_MASK = "file-mask";
    private static final String PARAM_TIME_FORMAT = "time-format";
    private static final String PARAM_EXTENSION = "extension";
    private static final String PARAM_RATE = "rate";

    public DataField[] getOutputFormat() {
        return new DataField[] {
                new DataField("image", "binary:image/" + fileExtension, fileExtension + " image") };
    }

    /**
     * Initializes the ImageFileWrapper by retrieving the necessary configuration
     * parameters from the active address bean.
     * 
     * @return true if the initialization is successful, false otherwise.
     */
    public boolean initialize() {
        AddressBean addressBean = getActiveAddressBean();

        fileExtension = addressBean.getPredicateValue(PARAM_EXTENSION);
        if (fileExtension == null) {
            logger.warn("The > " + PARAM_EXTENSION + " < parameter is missing from the wrapper for VS "
                    + this.getActiveAddressBean().getVirtualSensorName());
            return false;
        }

        timeFormat = addressBean.getPredicateValue(PARAM_TIME_FORMAT);
        if (timeFormat == null) {
            logger.warn("The > " + PARAM_TIME_FORMAT + " < parameter is missing from the wrapper for VS "
                    + this.getActiveAddressBean().getVirtualSensorName());
            return false;
        }

        fileMask = addressBean.getPredicateValue(PARAM_FILE_MASK);
        if (fileMask == null) {
            logger.warn("The > " + PARAM_FILE_MASK + " < parameter is missing from the wrapper for VS "
                    + this.getActiveAddressBean().getVirtualSensorName());
            return false;
        }

        imagesDirectory = addressBean.getPredicateValue(PARAM_DIRECTORY);
        if (imagesDirectory == null) {
            logger.warn("The > " + PARAM_DIRECTORY + " < parameter is missing from the wrapper for VS "
                    + this.getActiveAddressBean().getVirtualSensorName());
            return false;
        }

        String rateStr = addressBean.getPredicateValue(PARAM_RATE);
        if (rateStr == null) {
            logger.warn("The > " + PARAM_RATE + " < parameter is missing from the wrapper in VS "
                    + this.getActiveAddressBean().getVirtualSensorName());
            return false;
        } else {
            try {
                rate = Integer.parseInt(rateStr);
            } catch (NumberFormatException e) {
                logger.warn("The > " + PARAM_RATE + " < parameter is invalid for wrapper in VS "
                        + this.getActiveAddressBean().getVirtualSensorName());
                return false;
            }  
        }

        latestProcessedTimestamp = -1;

        return true;
    }

    /**
     * Runs the image file wrapper.
     * This method continuously checks for new files in the specified directory and
     * performs actions accordingly.
     * It sleeps for 2 seconds initially and then checks for new files in a loop
     * until the wrapper is active.
     * The rate at which it checks for new files can be set using the 'rate'
     * variable.
     */
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }

        while (isActive()) {
            try {

                listOfNewFiles(imagesDirectory, fileMask);

                Thread.sleep(rate);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public void dispose() {
        threadCounter--;
    }

    public String getWrapperName() {
        return "ImageFileWrapper";
    }

    /*
     * converts time from string to long
     * returns -1 if not successful
     */
    private long strTime2Long(String s, String timeFormat) {

        long l = -1;
        try {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(timeFormat);
            l = fmt.parseDateTime(s).getMillis();
        } catch (java.lang.IllegalArgumentException e) {
            logger.warn(e.getMessage(), e);
        }
        return l;
    }

    /**
     * Extracts the timestamp from a given file name using a regular expression
     * mask.
     *
     * @param fileName  the name of the file
     * @param regexMask the regular expression mask to match against the file name
     * @return the extracted timestamp from the file name, or null if no match is
     *         found
     */
    private String getTimeStampFromFileName(String fileName, String regexMask) {

        Pattern pattern = Pattern.compile(regexMask);
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            if(logger.isDebugEnabled()){
                logger.debug("Date => " + matcher.group(1));
            }
            return matcher.group(1);
        } else {
            if(logger.isDebugEnabled()){
                logger.debug("Date => null");
            }
            return null;
        }
    }

    /*
     * posts new image files to database
     * returns a list of file names in a directory,
     * which match a fileMask (given as regular expression)
     */
    private Vector<String> listOfNewFiles(String dir, String regexFileMask) {

        File f = new File(dir);
        String[] files = f.list();

        Arrays.sort(files);

        Vector<String> v = new Vector<String>();
        if(logger.isDebugEnabled()){
            logger.debug("*** found " + files.length + " files ***");
        }
        for (int i = 0; i < files.length; i++) {
            String file = files[i];
            Pattern pattern = Pattern.compile(regexFileMask);
            Matcher matcher = pattern.matcher(file);
            if(logger.isDebugEnabled()){
                logger.debug("(" + i + ") Testing... " + file);
            }
            if (matcher.find()) {
                String date = getTimeStampFromFileName(file, regexFileMask);
                long epoch = strTime2Long(date, timeFormat);
                if(logger.isDebugEnabled()){
                    logger.debug("Matching => " + file + " date = " + date + " epoch = " + epoch);
                }
                if (epoch > latestProcessedTimestamp) {
                    if(logger.isDebugEnabled()){
                        logger.debug("New image => " + epoch);
                    }
                    latestProcessedTimestamp = epoch;
                    v.add(file);
                    postData(dir + "/" + file, epoch);
                }
            }
        }

        return v;
    }

    /*
     * Posting data to database
     */
    private boolean postData(String imagePath, long timed) {
        
        if(logger.isDebugEnabled()){
            logger.debug("trying to post... " + imagePath);
        }

        boolean success = true;

        Serializable[] stream = new Serializable[1];

        try {
            FileInputStream fileinputstream = new FileInputStream(imagePath);
            int numberBytes = fileinputstream.available();
            if(logger.isDebugEnabled()){
                logger.debug("Image file has size: " + numberBytes + " bytes");
            }
            byte bytearray[] = new byte[numberBytes];
            fileinputstream.read(bytearray);
            fileinputstream.close();
            stream[0] = bytearray;
        } catch (FileNotFoundException e) {
            logger.warn("Couldn't find image file: " + imagePath);
            logger.warn(e.getMessage(), e);
            success = false;
        } catch (IOException e) {
            logger.warn("Couldn't read image file: " + imagePath);
            logger.warn(e.getMessage(), e);
            success = false;
        }

        StreamElement se = new StreamElement(getOutputFormat(), stream, timed);

        if (success) {
            success = postStreamElement(se);
        }

        return success;
    }
}
