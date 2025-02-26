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
* File: src/ch/epfl/gsn/wrappers/DiskSpaceWrapper.java
*
* @author Mehdi Riahi
* @author Ali Salehi
* @author Mehdi Riahi
* @author Davide De Sclavis
* @author Manuel Buchauer
* @author Jan Beutel
*
*/

package ch.epfl.gsn.wrappers;

import java.io.File;
import java.io.Serializable;

import org.slf4j.LoggerFactory;

import ch.epfl.gsn.beans.DataField;
import ch.epfl.gsn.beans.DataTypes;
import ch.epfl.gsn.beans.StreamElement;

import org.slf4j.Logger;

public class DiskSpaceWrapper extends AbstractWrapper {

    private static final int DEFAULT_SAMPLING_RATE = 1000;

    private int samplingRate = DEFAULT_SAMPLING_RATE;

    private final transient Logger logger = LoggerFactory.getLogger(DiskSpaceWrapper.class);

    private static int threadCounter = 0;

    private transient DataField[] outputStructureCache = new DataField[] {
            new DataField("FREE_SPACE", "bigint", "Free Disk Space") };

    private File[] roots;

    public boolean initialize() {
        logger.info("Initializing DiskSpaceWrapper Class");
        String javaVersion = System.getProperty("java.version");
        if (!javaVersion.startsWith("1.6")) {
            logger.error("Error in initializing DiskSpaceWrapper because of incompatible jdk version: " + javaVersion
                    + " (should be 1.6.x)");
            return false;
        }
        return true;
    }

    /**
     * Runs the disk space monitoring process in a separate thread.
     * This method continuously checks the available free space on the disk and
     * posts the information as a stream element.
     * The monitoring process runs until the wrapper is active.
     */
    public void run() {
        while (isActive()) {
            try {
                Thread.sleep(samplingRate);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
            roots = File.listRoots();
            long totalFreeSpace = 0;
            for (int i = 0; i < roots.length; i++) {
                totalFreeSpace += roots[i].getFreeSpace();
            }

            // convert to MB
            totalFreeSpace = totalFreeSpace / (1024 * 1024);
            StreamElement streamElement = new StreamElement(new String[] { "FREE_SPACE" },
                    new Byte[] { DataTypes.BIGINT }, new Serializable[] { totalFreeSpace
                    }, System.currentTimeMillis());
            postStreamElement(streamElement);
        }
    }

    public void dispose() {
        threadCounter--;
    }

    public String getWrapperName() {
        return "Free Disk Space";
    }

    public DataField[] getOutputFormat() {
        return outputStructureCache;
    }

}
