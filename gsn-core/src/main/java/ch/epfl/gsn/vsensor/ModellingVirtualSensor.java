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
* File: src/ch/epfl/gsn/vsensor/ModellingVirtualSensor.java
*
* @author Julien Eberle
* @author Davide De Sclavis
* @author Manuel Buchauer
* @author Jan Beutel
*
*/

package ch.epfl.gsn.vsensor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeMap;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import ch.epfl.gsn.beans.StreamElement;
import ch.epfl.gsn.utils.models.AbstractModel;



/**
 * This class is linked to an array of AbstractModels and keeps them updated by
 * pushing every StreamElement to them.
 * The model classes are defined by their class names separated by "," as a
 * parameter of the VS.
 * If a model needs some parameters before initializing, they can be specified
 * in
 * the VS parameters as "model.i.param",
 * where i is the index of the model and param the parameter name.
 * 
 * @author jeberle
 *
 */
public class ModellingVirtualSensor extends AbstractVirtualSensor {

	private static final transient Logger logger = LoggerFactory.getLogger(ModellingVirtualSensor.class);

	private static final String PARAM_MODEL_CLASS = "model";
	private static final String PARAM_MODEL_PREFIX = "model";

	private String[] model;

	private AbstractModel[] am;

	/**
	 * Initializes the modeling virtual sensor by getting the model class names from
	 * the parameters, instantiating the models, setting their parameters, output
	 * structure and virtual sensor reference, and initializing them.
	 * 
	 * @return true if all models initialize successfully, false otherwise.
	 */
	@Override
	public boolean initialize() {

		TreeMap<String, String> params = getVirtualSensorConfiguration().getMainClassInitialParams();

		// get all the models
		String model_str = params.get(PARAM_MODEL_CLASS);

		if (model_str == null) {
			logger.warn("Parameter \"" + PARAM_MODEL_CLASS + "\" not provided in Virtual Sensor file");
			return false;
		}

		model = model_str.trim().split(",");

		am = new AbstractModel[model.length];

		for (int i = 0; i < model.length; i++) {
			try {
				// instantiate the models, ...
				Class<?> fc = Class.forName(model[i]);
				am[i] = (AbstractModel) fc.newInstance();
				// output structure of the models is the same as the one of the VS
				am[i].setOutputFields(getVirtualSensorConfiguration().getOutputStructure());
				// ...set their parameters...
				for (String k : params.navigableKeySet()) {
					String prefix = PARAM_MODEL_PREFIX + "." + i + ".";
					if (k.startsWith(prefix)) {
						am[i].setParam(k.substring(prefix.length()), params.get(k));
					}
				}
				am[i].setVirtualSensor(this);
				// ... and initialize them.
				if (!am[i].initialize()) {
					return false;
				}

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return false;
			}
		}
		return true;
	}

	@Override
	public void dispose() {

	}

	/**
	 * Handles new sensor data arriving from sensor.
	 *
	 * Pushes the data to each configured model, collects the output,
	 * sorts it by timestamp, and sends it out as the virtual sensor's
	 * output.
	 *
	 * @param inputStreamName Name of the input stream for the arriving data
	 * @param streamElement   The StreamElement data object containing the sensor
	 *                        reading
	 */
	@Override
	public void dataAvailable(String inputStreamName, StreamElement streamElement) {
		StreamElement[] out = new StreamElement[] { streamElement };
		if (am.length > 0) {
			out = am[0].pushData(streamElement, inputStreamName); // by default returns the result from the first model
		}
		for (int i = 1; i < am.length; i++) {
			if (am[i] != null) {
				am[i].pushData(streamElement, inputStreamName);// push the data to all other models too
			}
		}
		if (out != null) {
			Arrays.sort(out, new Comparator<StreamElement>() {
				@Override
				public int compare(StreamElement o1, StreamElement o2) {
					return Long.valueOf(o1.getTimeStamp()).compareTo(o2.getTimeStamp());
				}
			});
			for (int i = 0; i < out.length; i++) {
				if (out[i] != null) {
					dataProduced(out[i]);
				}
			}
		}
	}

	/**
	 * Return the model corresponding to the given index
	 * 
	 * @param index of the model
	 * @return the model if it exists or null if the index is out of bound
	 */
	public AbstractModel getModel(int index) {
		if (index >= 0 && index < am.length) {
			return am[index];
		} else {
			return null;
		}

	}

}
