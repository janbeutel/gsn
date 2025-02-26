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
* File: src/ch/epfl/gsn/vsensor/ScheduledStreamExporterVirtualSensor.java
*
* @author bgpearn
* @author Timotee Maret
* @author Davide De Sclavis
* @author Manuel Buchauer
* @author Jan Beutel
*
*/

package ch.epfl.gsn.vsensor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.TreeMap;
import java.util.TimerTask;

import org.slf4j.LoggerFactory;

import ch.epfl.gsn.ContainerImpl;
import ch.epfl.gsn.Main;
import ch.epfl.gsn.utils.GSNRuntimeException;

import org.slf4j.Logger;

/**
 * This virtual sensor exports streams on a scheduled basis to any JDBC data
 * source.
 * It extends AbstractScheduledVirtualSensor
 */
public class ScheduledStreamExporterVirtualSensor extends AbstractScheduledVirtualSensor {

	public static final String PARAM_USER = "user", 
							   PARAM_PASSWD = "password", 
							   PARAM_URL = "url", 
							   TABLE_NAME = "table",
							   PARAM_DRIVER = "driver";
							   
	public static final String[] OBLIGATORY_PARAMS = new String[] { PARAM_USER, PARAM_URL, PARAM_DRIVER };

	private Connection connection;
	private CharSequence table_name;
	private String password;
	private String user;
	private String url;

	private static final transient Logger logger = LoggerFactory.getLogger(ScheduledStreamExporterVirtualSensor.class);

	/**
	 * Initializes the ScheduledStreamExporterVirtualSensor.
	 * Gets the required parameters from the virtual sensor configuration.
	 * Loads the JDBC driver class.
	 * Establishes a database connection.
	 * Checks if the output table exists, creates it if needed.
	 * Schedules the timer task to run periodically.
	 * 
	 * @return true if initialization succeeded, false otherwise.
	 * @throws ClassNotFoundException If the JDBC driver class cannot be found.
	 * @throws SQLException           If there is an error connecting to the
	 *                                database.
	 */
	public boolean initialize() {
		// Get the StreamExporter parameters
		TreeMap<String, String> params = getVirtualSensorConfiguration()
				.getMainClassInitialParams();
		for (String param : OBLIGATORY_PARAMS) {
			if (params.get(param) == null || params.get(param).trim().length() == 0) {
				logger.warn("Initialization Failed, The " + param + " initialization parameter is missing");
				return false;
			}
		}
		table_name = params.get(TABLE_NAME);
		user = params.get(PARAM_USER);
		password = params.get(PARAM_PASSWD);
		url = params.get(PARAM_URL);
		try {
			Class.forName(params.get(PARAM_DRIVER));
			connection = getConnection();
			if(logger.isDebugEnabled()){
				logger.debug("jdbc connection established.");
			}
			if (!Main.getStorage(table_name.toString()).tableExists(table_name,
					getVirtualSensorConfiguration().getOutputStructure(), connection)) {
				Main.getStorage(table_name.toString()).executeCreateTable(table_name,
						getVirtualSensorConfiguration().getOutputStructure(), false, connection);
			}
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
			logger.error("Initialization of the Stream Exporter VS failed !");
			return false;
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			logger.error("Initialization of the Stream Exporter VS failed !");
			return false;
		} catch (GSNRuntimeException e) {
			logger.error(e.getMessage(), e);
			logger.error("Initialization failed. There is a table called " + TABLE_NAME
					+ " Inside the database but the structure is not compatible with what GSN expects.");
			return false;
		}
		try {
			connection.close();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
		super.initialize(); // get the timer settings

		TimerTask timerTask = new MyTimerTask();
		timer0.scheduleAtFixedRate(timerTask, new Date(startTime), clock_rate);
		return true;
	}

	/**
	 * TimerTask subclass that runs periodically.
	 * It retrieves the latest data item, sets the timestamp, logs a message,
	 * builds the insert query, gets a DB connection, executes the insert,
	 * closes the connection, publishes the data item, and catches any errors.
	 * This handles the core logic of exporting the data stream to the database
	 * on a scheduled interval.
	 */
	class MyTimerTask extends TimerTask {

		public void run() {

			if (dataItem == null) {
				return;
			}
			dataItem.setTimeStamp(System.currentTimeMillis());
			logger.warn(getVirtualSensorConfiguration().getName() + " Timer Event ");
			StringBuilder query = Main.getStorage(table_name.toString()).getStatementInsert(table_name,
					getVirtualSensorConfiguration().getOutputStructure());

			try {
				connection = getConnection();

				Main.getStorage(table_name.toString()).executeInsert(table_name,
						getVirtualSensorConfiguration().getOutputStructure(), dataItem, getConnection());
				logger.warn(getVirtualSensorConfiguration().getName() + " Wrote to database ");
				connection.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
				logger.error("Insertion failed! (" + query + ")");
			} finally {

				try {
					ContainerImpl.getInstance().publishData(ScheduledStreamExporterVirtualSensor.this, dataItem);
				} catch (SQLException e) {
					if (e.getMessage().toLowerCase().contains("duplicate entry")) {
						logger.info(e.getMessage(), e);
					} else {
						logger.error(e.getMessage(), e);
					}
				}
			}

		}
	}

	/**
	 * Gets a connection to the database.
	 * If the connection is null or closed, creates a new connection.
	 * Otherwise returns the existing open connection.
	 * Uses the url, user, and password fields to create the connection.
	 */
	public Connection getConnection() throws SQLException {
		if (this.connection == null || this.connection.isClosed()) {
			this.connection = DriverManager.getConnection(url, user, password);
		}
		return connection;
	}

	/**
	 * Cancels the timer, closes the database connection, and logs any errors.
	 */
	public void dispose() {
		timer0.cancel();
		try {
			this.connection.close();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
	}

}
