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
* File: src/ch/epfl/gsn/wrappers/GPSGenerator.java
*
* @author Mehdi Riahi
* @author Ali Salehi
* @author Davide De Sclavis
* @author Manuel Buchauer
* @author Jan Beutel
*
*/

package ch.epfl.gsn.wrappers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.LoggerFactory;

import ch.epfl.gsn.beans.AddressBean;
import ch.epfl.gsn.beans.DataField;
import ch.epfl.gsn.beans.DataTypes;
import ch.epfl.gsn.beans.StreamElement;
import ch.epfl.gsn.utils.ParamParser;

import org.slf4j.Logger;

public class GPSGenerator extends AbstractWrapper {

   private static final int DEFAULT_SAMPLING_RATE = 1000;

   private static final String[] FIELD_NAMES = new String[] { "latitude", "longitude", "temperature", "light",
         "camera" };

   private static final Byte[] FIELD_TYPES = new Byte[] { DataTypes.DOUBLE, DataTypes.DOUBLE, DataTypes.DOUBLE,
         DataTypes.INTEGER, DataTypes.BINARY };

   private static final String[] FIELD_DESCRIPTION = new String[] { "Latitude Reading", "Longitude Reading",
         "Temperature Sensor", "Light Sensor", "Camera Picture" };

   private static final String[] FIELD_TYPES_STRING = new String[] { "double", "double", "double", "int",
         "binary:jpeg" };

   private int samplingRate = DEFAULT_SAMPLING_RATE;

   private final transient Logger logger = LoggerFactory.getLogger(GPSGenerator.class);

   private static int threadCounter = 0;

   private byte[] picture;

   private DataField[] outputStrcture;
   private static int step = 1;

   public DataField[] getOutputFormat() {
      return outputStrcture;
   }

   /**
    * Initializes the GPSGenerator.
    * This method retrieves the necessary configuration parameters from the address
    * bean,
    * such as the sampling rate and the picture file.
    * It also initializes the output structure with the appropriate field names,
    * types, and descriptions.
    * 
    * @return true if the initialization is successful, false otherwise.
    */
   public boolean initialize() {
      AddressBean addressBean = getActiveAddressBean();
      if (addressBean.getPredicateValue("rate") != null) {
         samplingRate = ParamParser.getInteger(addressBean.getPredicateValue("rate"), DEFAULT_SAMPLING_RATE);
         if (samplingRate <= 0) {
            logger.warn(
                  "The specified >sampling-rate< parameter for the >MemoryMonitoringWrapper< should be a positive number.\nGSN uses the default rate ("
                        + DEFAULT_SAMPLING_RATE + "ms ).");
            samplingRate = DEFAULT_SAMPLING_RATE;
         }
      }
      if (addressBean.getPredicateValue("picture") == null) {
         logger.warn("The >picture< parameter is missing from the GPSGenerator wrapper.");
         return false;
      } else {
         String picture = addressBean.getPredicateValue("picture");
         File pictureF = new File(picture);
         if (!pictureF.isFile() || !pictureF.canRead()) {
            logger.warn("The GPSGenerator can't access the specified picture file. Initialization failed.");
            return false;
         }
         try {
            BufferedInputStream fis = new BufferedInputStream(new FileInputStream(pictureF));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4 * 1024];
            while (fis.available() > 0) {
               outputStream.write(buffer, 0, fis.read(buffer));
            }
            fis.close();
            this.picture = outputStream.toByteArray();
            outputStream.close();
         } catch (FileNotFoundException e) {
            logger.warn(e.getMessage(), e);
            return false;
         } catch (IOException e) {
            logger.warn(e.getMessage(), e);
            return false;
         }
      }
      ArrayList<DataField> output = new ArrayList<DataField>();
      for (int i = 0; i < FIELD_NAMES.length; i++) {
         output.add(new DataField(FIELD_NAMES[i], FIELD_TYPES_STRING[i], FIELD_DESCRIPTION[i]));
      }
      outputStrcture = output.toArray(new DataField[] {});
      return true;
   }



   /**
    * Runs the GPSGenerator thread.
    * Generates GPS coordinates and posts them as StreamElements.
    */
   public void run() {
      while (isActive()) {
         double latitude = 37.4419 + .01 * (step++);
         double longitude = -122.1419;
         try {
            Thread.sleep(samplingRate);
         } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
         }
         StreamElement streamElement = new StreamElement(FIELD_NAMES, FIELD_TYPES,
               new Serializable[] { latitude, longitude, 25.5, 650, picture });
         postStreamElement(streamElement);
      }
   }

   public void dispose() {
      threadCounter--;
   }

   public String getWrapperName() {
      return "GPS Generator";
   }

}
