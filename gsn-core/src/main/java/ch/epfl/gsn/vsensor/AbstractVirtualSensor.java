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
* File: src/ch/epfl/gsn/vsensor/AbstractVirtualSensor.java
*
* @author gsn_devs
* @author Ali Salehi
* @author Mehdi Riahi
* @author Davide De Sclavis
* @author Manuel Buchauer
* @author Jan Beutel
*
*/

package ch.epfl.gsn.vsensor;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.lang.management.ThreadMXBean;

import org.slf4j.LoggerFactory;

import ch.epfl.gsn.ContainerImpl;
import ch.epfl.gsn.Main;
import ch.epfl.gsn.beans.DataField;
import ch.epfl.gsn.beans.DataTypes;
import ch.epfl.gsn.beans.InputInfo;
import ch.epfl.gsn.beans.InputStream;
import ch.epfl.gsn.beans.StreamElement;
import ch.epfl.gsn.beans.StreamSource;
import ch.epfl.gsn.beans.VSensorConfig;
import ch.epfl.gsn.monitoring.AnomalyDetector;
import ch.epfl.gsn.monitoring.Monitorable;

import org.slf4j.Logger;

public abstract class AbstractVirtualSensor implements Monitorable {

	private static final transient Logger logger = LoggerFactory.getLogger(AbstractVirtualSensor.class);

	private VSensorConfig virtualSensorConfiguration;

	private Long outputCount = 0L;
	private Long inputCount = 0L;

	private long lastOutputedTime = 0;
	private long lastInputTime = 0;

	/*
	 * Map threads would keep track of thread ID (and name) of each wrapper
	 * associated with this Virtual Sensor
	 * It is populated in start method of VirtualSensor class when each wrapper
	 * thread is started
	 */
	private Map<Long, String> threads = new HashMap<Long, String>();
	private AnomalyDetector anomalyDetector;

	/**
	 * Initializes the virtual sensor wrapper.
	 * Registers the virtual sensor with the anomaly detector and main GSN instance.
	 * Calls the virtual sensor's initialize() method.
	 * 
	 * @return True if the wrapper initialization succeeded.
	 */
	public final boolean initialize_wrapper() {

		anomalyDetector = new AnomalyDetector(this);
		Main.getInstance().getToMonitor().add(this);
		return initialize();
	}

	/**
	 * Called once while initializing an instance of the virtual sensor
	 * 
	 * @return True if the initialization is done successfully.
	 */
	public abstract boolean initialize();

	/**
	 * Validates that the given stream element is compatible with the
	 * output structure defined in the virtual sensor configuration.
	 * 
	 * If adjust is true, it will adjust the stream element to match the
	 * output structure by removing extra fields.
	 * 
	 * If adjust is false, it will throw an exception if the structures do not
	 * match exactly.
	 *
	 * @param streamElement The stream element to validate
	 * @param adjust        Whether to adjust the stream element if needed
	 * @throws RuntimeException if adjust is false and the structures do not match
	 */
	private void validateStreamElement(StreamElement streamElement, boolean adjust) {
		if (!compatibleStructure(streamElement, getVirtualSensorConfiguration().getOutputStructure(), adjust)) {
			StringBuilder exceptionMessage = new StringBuilder().append("The streamElement produced by :")
					.append(getVirtualSensorConfiguration().getName()).append(
							" Virtual Sensor is not compatible with the defined streamElement.\n");
			exceptionMessage.append("The expected stream element structure (specified in ")
					.append(getVirtualSensorConfiguration().getFileName()).append(" is [");
			for (DataField df : getVirtualSensorConfiguration().getOutputStructure()) {
				exceptionMessage.append(df.getName()).append(" (").append(DataTypes.TYPE_NAMES[df.getDataTypeID()])
						.append(") , ");
			}
			exceptionMessage.append(
					"] but the actual stream element received from the " + getVirtualSensorConfiguration().getName())
					.append(" has the [");
			for (int i = 0; i < streamElement.getFieldNames().length; i++) {
				exceptionMessage.append(streamElement.getFieldNames()[i]).append("(")
						.append(DataTypes.TYPE_NAMES[streamElement.getFieldTypes()[i]]).append("),");
			}
			exceptionMessage.append(" ] thus the stream element dropped !!!");
			throw new RuntimeException(exceptionMessage.toString());
		}
	}

	/**
	 * if Adjust is true then system checks the output structure of the virtual
	 * sensor and
	 * only publishes the fields defined in the output structure of the virtual
	 * sensor and
	 * ignores the rest. IF the adjust is set to false, the system will enforce
	 * strict
	 * compatibility of the output and the produced value.
	 * 
	 * @param streamElement
	 * @param adjust        Default is false.
	 */
	protected synchronized void dataProduced(StreamElement streamElement, boolean adjust) {
		try {
			validateStreamElement(streamElement, adjust);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return;
		}
		if (!streamElement.isTimestampSet()) {
			streamElement.setTimeStamp(System.currentTimeMillis());
		}

		final int outputStreamRate = getVirtualSensorConfiguration().getOutputStreamRate();
		final long currentTime = System.currentTimeMillis();
		if ((currentTime - lastOutputedTime) < outputStreamRate) {
			logger.info("Called by *discarded* b/c of the rate limit reached.");
			return;
		}
		lastOutputedTime = currentTime;
		try {
			ContainerImpl.getInstance().publishData(this, streamElement);
			outputCount = outputCount == Long.MAX_VALUE ? 0 : outputCount + 1;
		} catch (SQLException e) {
			if (e.getMessage().toLowerCase().contains("duplicate entry")) {
				logger.info(e.getMessage(), e);
			} else {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * Calls the dataProduced with adjust = false.
	 * 
	 * @param streamElement
	 */
	protected synchronized void dataProduced(StreamElement streamElement) {
		dataProduced(streamElement, true);
	}

	/**
	 * First checks compatibility of the data type of each output data item in the
	 * stream element with the
	 * defined output in the VSD file. (this check is done regardless of the value
	 * for adjust flag).
	 * <p>
	 * If the adjust flag is set to true, the method checks the newly generated
	 * stream element
	 * and returns true if and only if the number of data items is equal to the
	 * number of output
	 * data structure defined for this virtual sensor.
	 * If the adjust=true, then this test is not performed.
	 * 
	 * @param se
	 * @param outputStructure
	 * @param adjust          default is false.
	 * @return
	 */
	private static boolean compatibleStructure(StreamElement se, DataField[] outputStructure, boolean adjust) {
		if (!adjust && outputStructure.length != se.getFieldNames().length) {
			logger.warn(
					"Validation problem, the number of field doesn't match the number of output data strcture of the virtual sensor");
			return false;
		}
		int i = -1;
		for (DataField field : outputStructure) {
			Serializable value = se.getData(field.getName());
			i++;
			if (value == null) {
				continue;
			}
			if (((field.getDataTypeID() == DataTypes.BIGINT ||
					field.getDataTypeID() == DataTypes.DOUBLE ||
					field.getDataTypeID() == DataTypes.FLOAT ||
					field.getDataTypeID() == DataTypes.INTEGER ||
					field.getDataTypeID() == DataTypes.SMALLINT ||
					field.getDataTypeID() == DataTypes.TINYINT) && !(value instanceof Number))
					||
					((field.getDataTypeID() == DataTypes.VARCHAR || field.getDataTypeID() == DataTypes.CHAR)
							&& !(value instanceof String))
					||
					((field.getDataTypeID() == DataTypes.BINARY) && !(value instanceof byte[]))) {
				logger.warn("Validation problem for output field >" + field.getName() + ", The field type declared as >"
						+ field.getType() + "< while in VSD it is defined as >"
						+ DataTypes.TYPE_NAMES[outputStructure[i].getDataTypeID()]);
				return false;
			}
		}
		return true;
	}

	/**
	 * Disposes of this virtual sensor instance by removing it from the
	 * monitoring list and calling the dispose() method.
	 * Called when the container stops the pool and removes its resources.
	 */
	public final void dispose_decorated() {
		Main.getInstance().getToMonitor().remove(this);
		dispose();
	}

	/**
	 * Called when the container want to stop the pool and remove it's resources.
	 * The container will call this method once on each install of the virtual
	 * sensor in the pool. The programmer should release all the resources used by
	 * this virtual sensor instance in this method specially those resources
	 * acquired during the <code>initialize</code> call.
	 * <p/>
	 * Called once while
	 * finalizing an instance of the virtual sensor
	 */
	public abstract void dispose();

	public boolean dataFromWeb(String action, String[] paramNames, Serializable[] paramValues) {
		boolean ret = false;
		Iterator<InputStream> streams = getVirtualSensorConfiguration().getInputStreams().iterator();
		while (streams.hasNext()) {
			InputStream str = streams.next();
			StreamSource[] sources = str.getSources();
			for (int j=0; j<sources.length; j++) {
				try {
					ret = sources[j].getWrapper().sendToWrapper(action, paramNames, paramValues);
				} catch (OperationNotSupportedException e) {
					logger.error(e.getMessage());
					ret = false;
				}
			}
		}
		return ret;

	}

	/**
	 * @return the virtualSensorConfiguration
	 */
	public VSensorConfig getVirtualSensorConfiguration() {
		if (virtualSensorConfiguration == null) {
			throw new RuntimeException("The VirtualSensorParameter is not set !!!");
		}
		return virtualSensorConfiguration;
	}

	/**
	 * @param virtualSensorConfiguration the virtualSensorConfiguration to set
	 */
	public void setVirtualSensorConfiguration(VSensorConfig virtualSensorConfiguration) {
		this.virtualSensorConfiguration = virtualSensorConfiguration;
	}

	public Map<Long, String> getThreads() {
		return threads;
	}

	public void setThreads(Map<Long, String> threads) {
		this.threads = threads;
	}

	/**
	 * Gets statistics related to this virtual sensor instance.
	 * Adds output count and last output time statistics.
	 * Adds input count and last input time statistics.
	 * Sums the CPU times of the threads associated with this
	 * virtual sensor and adds the total CPU time statistic.
	 * 
	 * @return A map containing the statistics.
	 */
	public Hashtable<String, Object> getStatistics() {
		Hashtable<String, Object> stat = anomalyDetector.getStatistics();
		stat.put("vs." + virtualSensorConfiguration.getName().replaceAll("\\.", "_") + ".output.produced.counter",
				outputCount);
		stat.put("vs." + virtualSensorConfiguration.getName().replaceAll("\\.", "_") + ".outupt.lastOutputedTime",
				lastOutputedTime);
		stat.put("vs." + virtualSensorConfiguration.getName().replaceAll("\\.", "_") + ".input.produced.counter",
				inputCount);
		stat.put("vs." + virtualSensorConfiguration.getName().replaceAll("\\.", "_") + ".input.lastInputTime",
				lastInputTime);

		/*
		 * We know the IDs of threads associated with this VSensor
		 * Using the IDs, we would extract the CPU times, sum them up and put them in
		 * the stats map
		 */
		ThreadMXBean threadBean = Main.getThreadMXBean();
		if (!threadBean.isThreadCpuTimeEnabled()) {
			logger.info("ThreadCpuTime is disabled. Enabling it | Thread time measurement might not be accurate");
			threadBean.setThreadCpuTimeEnabled(true);
		}

		Iterator<Map.Entry<Long, String>> iter = threads.entrySet().iterator();
		long totalCpuTime = 0L;

		// TODO: Not using thread names | Should be used for logging in debug mode

		while (iter.hasNext()) {
			Map.Entry<Long, String> entry = iter.next();
			Long id = entry.getKey();

			long cpuTime = threadBean.getThreadCpuTime(id);

			if (cpuTime == -1) { // Thread is not alive anymore
				iter.remove();
				continue;
			}

			if (Long.MAX_VALUE - totalCpuTime > cpuTime) {
				totalCpuTime += cpuTime;
			} else {
				totalCpuTime = cpuTime - (Long.MAX_VALUE - totalCpuTime);
			}
		}

		stat.put("vs." + virtualSensorConfiguration.getName().replaceAll("\\.", "_") + ".cputime.totalCpuTime.counter",
				totalCpuTime);

		return stat;
	}

	/**
	 * Decorated version of {@link #dataAvailable(String, StreamElement)} that
	 * updates internal counters for number of inputs received and time of last
	 * input.
	 * 
	 * Increments {@code inputCount} counter and sets {@code lastInputTime} to
	 * current time whenever new data is received. Handles counter overflow.
	 * 
	 * This is called by the container when new data is available on one of
	 * the input streams.
	 */
	public final void dataAvailable_decorated(String inputStreamName, StreamElement streamElement) {
		dataAvailable(inputStreamName, streamElement);
		final long currentTime = System.currentTimeMillis();
		inputCount = inputCount == Long.MAX_VALUE ? 0 : inputCount + 1;
		lastInputTime = currentTime;
	}

	/**
	 * This method is going to be called by the container when one of the input
	 * streams has a data to be delivered to this virtual sensor. After receiving
	 * the data, the virtual sensor can do the processing on it and this
	 * processing could possibly result in producing a new stream element in this
	 * virtual sensor in which case the virtual sensor will notify the container
	 * by simply adding itself to the list of the virtual sensors which have
	 * produced data. (calling <code>container.publishData(this)</code>. For
	 * more information please check the <code>AbstractVirtalSensor</code>
	 * 
	 * @param inputStreamName is the name of the input stream as specified in the
	 *                        configuration file of the virtual sensor. @param
	 *                        inputDataFromInputStream
	 *                        is actually the real data which is produced by the
	 *                        input stream and should
	 *                        be delivered to the virtual sensor for possible
	 *                        processing.
	 */
	public abstract void dataAvailable(String inputStreamName, StreamElement streamElement);
}

