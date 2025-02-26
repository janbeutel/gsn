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
* File: src/ch/epfl/gsn/wrappers/ReplayWrapper.java
*
* @author Ali Salehi
* @author Mehdi Riahi
* @author Timotee Maret
* @author Davide De Sclavis
* @author Manuel Buchauer
* @author Jan Beutel
*
*/

package ch.epfl.gsn.wrappers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.LoggerFactory;

import ch.epfl.gsn.Main;
import ch.epfl.gsn.beans.AddressBean;
import ch.epfl.gsn.beans.DataField;
import ch.epfl.gsn.beans.StreamElement;
import ch.epfl.gsn.storage.DataEnumerator;

import org.slf4j.Logger;

/**
 * This wrapper enables one to reply the existing stream from a database.
 * parameters: (dbname,speed [integer >=1, default is 1], for instance speed=2
 * means play 2 times faster).
 */
public class ReplayWrapper extends AbstractWrapper {

  private transient Logger logger = LoggerFactory.getLogger(this.getClass());

  private int threadCounter = 0;

  private DataField[] output;

  private String dbname;

  private int speed;

  private final Timer timer = new Timer(true);

  private DelayedDataEnumerator dt;

  public void dispose() {
    threadCounter--;
  }

  public DataField[] getOutputFormat() {
    return output;
  }

  public String getWrapperName() {
    return "ReplayWrapper";
  }

  public boolean initialize() {
    AddressBean addressBean = getActiveAddressBean();
    dbname = addressBean.getPredicateValue("dbname");
    speed = addressBean.getPredicateValueAsInt("speed", 1);
    if (speed <= 0) {
      logger.warn("Invalid speed, speed is set to 1.");
      speed = 1;
    }
    Connection connection = null;

    try {
      logger.info("Initializing the ReplayWrapper with : " + dbname + ". Loading the table structure ...");
      connection = Main.getStorage(dbname).getConnection();
      output = Main.getStorage(dbname).tableToStructure(dbname, connection);
    } catch (SQLException e) {
      logger.error(e.getMessage(), e);
      return false;
    } finally {
      Main.getStorage(dbname).close(connection);
    }

    dt = new DelayedDataEnumerator(dbname, speed);

    timer.schedule(new TimerTask() {
      public void run() {
        start_publishing();
      }
    }, 1000);// 1000ms is the initial delay to have everything initialized.
    return true;
  }

  /**
   * Starts publishing the stream elements.
   * This method schedules a TimerTask to execute the publishing of each stream
   * element
   * with a delay based on the execution time of the stream element.
   * If there are no more stream elements to publish, the method returns
   * immediately.
   */
  public void start_publishing() {
    if (!dt.hasMoreElements()) {
      return;
    }
    final ScheduledStreamElement item = dt.nextElement();
    final long delay = item.getExecutionTime();
    timer.schedule(new TimerTask() {
      public void run() {
        ReplayWrapper.this.postStreamElement(item.getStreamElement());
        start_publishing();
      }
    }, delay);
  }

}

class DelayedDataEnumerator implements Enumeration<ScheduledStreamElement> {
  private int speed;
  private DataEnumerator data;
  private StreamElement previousElement = null;
  private transient Logger logger = LoggerFactory.getLogger(this.getClass());

  public DelayedDataEnumerator(String dbName, int speed) {
    this.speed = speed;
    StringBuilder query = new StringBuilder("select * from ").append(dbName).append(" order by TIMED asc");
    try {
      data = Main.getStorage(dbName).executeQuery(query, false);

    } catch (SQLException e) {
      logger.error(e.getMessage(), e);
    }
  }

  public boolean hasMoreElements() {
    return data.hasMoreElements();
  }

  public ScheduledStreamElement nextElement() {
    StreamElement currentSe = data.nextElement();
    long delay = 500;// First time execution is delayed for 500ms.
    if (previousElement != null) {
      delay = (currentSe.getTimeStamp() - previousElement.getTimeStamp()) / speed;
    }
    previousElement = currentSe;
    return new ScheduledStreamElement(currentSe, delay);
  }

}

class ScheduledStreamElement {
  private StreamElement se;
  private long executionTime;

  public StreamElement getStreamElement() {
    return se;
  }

  public long getExecutionTime() {
    return executionTime;
  }

  public ScheduledStreamElement(StreamElement se, long executionTime) {
    this.se = se;
    this.executionTime = executionTime;
  }
}
