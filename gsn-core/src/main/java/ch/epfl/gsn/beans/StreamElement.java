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
* File: src/ch/epfl/gsn/beans/StreamElement.java
*
* @author rhietala
* @author Timotee Maret
* @author Sofiane Sarni
* @author Ali Salehi
* @author Mehdi Riahi
* @author Julien Eberle
* @author Davide De Sclavis
* @author Manuel Buchauer
* @author Jan Beutel
*
*/

package ch.epfl.gsn.beans;

import play.libs.Json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import ch.epfl.gsn.beans.json.*;
import ch.epfl.gsn.delivery.StreamElement4Rest;
import ch.epfl.gsn.utils.CaseInsensitiveComparator;

import org.slf4j.Logger;

public final class StreamElement implements Serializable {

	private static final long serialVersionUID = 2000261462783698617L;

	private static final transient Logger logger = LoggerFactory.getLogger(StreamElement.class);

	private transient TreeMap<String, Integer> indexedFieldNames = null;

	private long timeStamp = -1;

	private String[] fieldNames;

	private Serializable[] fieldValues;

	private Byte[] fieldTypes;

	private transient long internalPrimayKey = -1;

	private static final String NULL_ENCODING = "NULL"; // null encoding for transmission over xml-rpc

	private boolean timestampProvided = false;
	private Long volume = null;

	public StreamElement(StreamElement other) {
		this.fieldNames = new String[other.fieldNames.length];
		this.fieldValues = new Serializable[other.fieldValues.length];
		this.fieldTypes = new Byte[other.fieldTypes.length];
		for (int i = 0; i < other.fieldNames.length; i++) {
			fieldNames[i] = other.fieldNames[i];
			fieldValues[i] = other.fieldValues[i];
			fieldTypes[i] = other.fieldTypes[i];
		}
		this.timeStamp = other.timeStamp;
		this.internalPrimayKey = other.internalPrimayKey;
		this.volume = other.volume;
	}

	public StreamElement() { // constructor for serialization

	}

	public StreamElement(DataField[] outputStructure, final Serializable[] data) {
		this(outputStructure, data, System.currentTimeMillis());
	}

	public StreamElement(DataField[] outputStructure, final Serializable[] data, final long timeStamp) {
		this.fieldNames = new String[outputStructure.length];
		this.fieldTypes = new Byte[outputStructure.length];
		this.timeStamp = timeStamp;
		for (int i = 0; i < this.fieldNames.length; i++) {
			this.fieldNames[i] = outputStructure[i].getName().toLowerCase();
			this.fieldTypes[i] = outputStructure[i].getDataTypeID();
		}
		if (this.fieldNames.length != data.length) {
			throw new IllegalArgumentException(
					"The length of dataFileNames and the actual data provided in the constructor of StreamElement doesn't match.");
		}
		this.verifyTypesCompatibility(this.fieldTypes, data);
		this.fieldValues = data;
		this.volume = null;
	}

	public StreamElement(final String[] dataFieldNames, final Byte[] dataFieldTypes, final Serializable[] data) {
		this(dataFieldNames, dataFieldTypes, data, System.currentTimeMillis());
	}

	public StreamElement(final String[] dataFieldNames, final Byte[] dataFieldTypes, final Serializable[] data,
			final long timeStamp) {
		if (dataFieldNames.length != dataFieldTypes.length) {
			throw new IllegalArgumentException(
					"The length of dataFileNames and dataFileTypes provided in the constructor of StreamElement doesn't match.");
		}
		if (dataFieldNames.length != data.length) {
			throw new IllegalArgumentException(
					"The length of dataFileNames and the actual data provided in the constructor of StreamElement doesn't match.");
		}
		this.timeStamp = timeStamp;
		this.timestampProvided = true;
		this.fieldTypes = dataFieldTypes;
		this.fieldNames = dataFieldNames;
		this.fieldValues = data;
		this.volume = null;
		this.verifyTypesCompatibility(dataFieldTypes, data);
	}

	public StreamElement(TreeMap<String, Serializable> output, DataField[] fields) {
		int nbFields = output.keySet().size();
		if (output.containsKey("timed")) {
			nbFields--;
		}
		String fieldNames[] = new String[nbFields];
		Byte fieldTypes[] = new Byte[nbFields];
		Serializable fieldValues[] = new Serializable[nbFields];
		TreeMap<String, Integer> indexedFieldNames = new TreeMap<String, Integer>(new CaseInsensitiveComparator());
		int idx = 0;

		long timestamp = System.currentTimeMillis();
		for (String key : output.keySet()) {
			Serializable value = output.get(key);

			if (key.equalsIgnoreCase("timed")) {
				timestamp = (Long) value;
				timestampProvided = true;
			} else {
				fieldNames[idx] = key;
				fieldValues[idx] = value;
				for (int i = 0; i < fields.length; i++) {
					if (fields[i].getName().equalsIgnoreCase(key)) {
						fieldTypes[idx] = fields[i].getDataTypeID();
					}
				}
				indexedFieldNames.put(key, idx);
				idx++;
			}
		}
		this.fieldNames = fieldNames;
		this.fieldTypes = fieldTypes;
		this.fieldValues = fieldValues;
		this.indexedFieldNames = indexedFieldNames;
		this.timeStamp = timestamp;
		this.volume = null;
	}

	public StreamElement(StreamElement other, final DataField[] outputStructure, final Serializable[] data) {
		int len = other.fieldNames.length + outputStructure.length;
		this.fieldNames = new String[len];
		this.fieldValues = new Serializable[len];
		this.fieldTypes = new Byte[len];
		for (int i = 0; i < other.fieldNames.length; i++) {
			this.fieldNames[i] = other.fieldNames[i];
			this.fieldValues[i] = other.fieldValues[i];
			this.fieldTypes[i] = other.fieldTypes[i];
		}
		for (int i = 0; i < outputStructure.length; i++) {
			this.fieldNames[other.fieldNames.length + i] = outputStructure[i].getName();
			this.fieldValues[other.fieldNames.length + i] = data[i];
			this.fieldTypes[other.fieldNames.length + i] = outputStructure[i].getDataTypeID();
		}
		this.timeStamp = other.timeStamp;
		this.internalPrimayKey = other.internalPrimayKey;
		this.volume = null;
	}

	public StreamElement(StreamElement other, final String[] dataFieldNames, final Byte[] dataFieldTypes,
			final Serializable[] data) {
		int len = other.fieldNames.length + dataFieldNames.length;
		this.fieldNames = new String[len];
		this.fieldValues = new Serializable[len];
		this.fieldTypes = new Byte[len];
		for (int i = 0; i < other.fieldNames.length; i++) {
			this.fieldNames[i] = other.fieldNames[i];
			this.fieldValues[i] = other.fieldValues[i];
			this.fieldTypes[i] = other.fieldTypes[i];
		}
		for (int i = 0; i < dataFieldNames.length; i++) {
			this.fieldNames[other.fieldNames.length + i] = dataFieldNames[i];
			this.fieldValues[other.fieldNames.length + i] = data[i];
			this.fieldTypes[other.fieldNames.length + i] = dataFieldTypes[i];
		}
		this.timeStamp = other.timeStamp;
		this.internalPrimayKey = other.internalPrimayKey;
		this.volume = null;
	}

	/**
	 * Verify if the data corresponds to the fieldType
	 * 
	 * @param fieldType
	 * @param data
	 * @throws IllegalArgumentException
	 */
	private void verifyTypeCompatibility(Byte fieldType, Serializable data) throws IllegalArgumentException {
		if (data == null) {
			return;
		}

		switch (fieldType) {
			case DataTypes.TINYINT:
				if (!(data instanceof Byte)) {
					throw new IllegalArgumentException("The field is defined as " + DataTypes.TYPE_NAMES[fieldType]
							+ " while the actual data in the field is of type : *" + data.getClass().getCanonicalName()
							+ "*");
				}

				break;
			case DataTypes.SMALLINT:
				if (!(data instanceof Short)) {
					throw new IllegalArgumentException("The field is defined as " + DataTypes.TYPE_NAMES[fieldType]
							+ " while the actual data in the field is of type : *" + data.getClass().getCanonicalName()
							+ "*");
				}

				break;
			case DataTypes.BIGINT:
				if (!(data instanceof Long)) {
					throw new IllegalArgumentException("The field is defined as " + DataTypes.TYPE_NAMES[fieldType]
							+ " while the actual data in the field is of type : *" + data.getClass().getCanonicalName()
							+ "*");
				}

				break;
			case DataTypes.CHAR:
			case DataTypes.VARCHAR:
				if (!(data instanceof String)) {
					throw new IllegalArgumentException("The field is defined as " + DataTypes.TYPE_NAMES[fieldType]
							+ " while the actual data in the field is of type : *" + data.getClass().getCanonicalName()
							+ "*");
				}

				break;
			case DataTypes.INTEGER:
				if (!(data instanceof Integer)) {
					throw new IllegalArgumentException("The field is defined as " + DataTypes.TYPE_NAMES[fieldType]
							+ " while the actual data in the field is of type : *" + data.getClass().getCanonicalName()
							+ "*");
				}

				break;
			case DataTypes.DOUBLE:
				if (!(data instanceof Double || data instanceof Float)) {
					throw new IllegalArgumentException("The field is defined as " + DataTypes.TYPE_NAMES[fieldType]
							+ " while the actual data in the field is of type : *" + data.getClass().getCanonicalName()
							+ "*");
				}

				break;
			case DataTypes.FLOAT:
				if (!(data instanceof Float)) {
					throw new IllegalArgumentException("The field is defined as " + DataTypes.TYPE_NAMES[fieldType]
							+ " while the actual data in the field is of type : *" + data.getClass().getCanonicalName()
							+ "*");
				}

				break;
			case DataTypes.BINARY:
				// if ( data[ i ] instanceof String ) data[ i ] = ( ( String )
				// data[ i ] ).getBytes( );
				if (!(data instanceof byte[] || data instanceof String)) {
					throw new IllegalArgumentException("The field is defined as " + DataTypes.TYPE_NAMES[fieldType]
							+ " while the actual data in the field is of type : *" + data.getClass().getCanonicalName()
							+ "*");
				}

				break;
			default:
				break;
		}
	}

	/**
	 * Checks the type compatibility of all fields of the StreamElement
	 * 
	 * @param fieldTypes the array of all fields' type
	 * @param data       the array of data to check
	 * @throws IllegalArgumentException if a data field doesn't match the given type
	 */
	private void verifyTypesCompatibility(final Byte[] fieldTypes, final Serializable[] data)
			throws IllegalArgumentException {
		for (int i = 0; i < data.length; i++) {
			try {
				verifyTypeCompatibility(fieldTypes[i], data[i]);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(
						"The newly constructed Stream Element is not consistent for the " + (i + 1) + "th field.", e);
			}
		}
	}

	public String toString() {
		final StringBuffer output = new StringBuffer("timed = ");
		output.append(this.getTimeStamp()).append("\t");
		for (int i = 0; i < this.fieldNames.length; i++) {
			output.append(",").append(this.fieldNames[i]).append("/").append(this.fieldTypes[i]).append(" = ")
					.append(this.fieldValues[i]);
		}

		return output.toString();
	}

	public final String[] getFieldNames() {
		return this.fieldNames;
	}

	/*
	 * Returns the field types in GSN format. Checkout ch.epfl.gsn.beans.DataTypes
	 */
	public final Byte[] getFieldTypes() {
		return this.fieldTypes;
	}

	public final Serializable[] getData() {
		return this.fieldValues;
	}

	public void setData(int index, Serializable data) {
		this.fieldValues[index] = data;
	}

	public long getTimeStamp() {
		return this.timeStamp;
	}

	/**
	 * Returns a string representation of the field types in the StringBuilder.
	 *
	 * @return a StringBuilder object containing the field types as a string
	 */
	public StringBuilder getFieldTypesInString() {
		final StringBuilder stringBuilder = new StringBuilder();
		for (final byte i : this.getFieldTypes()) {
			stringBuilder.append(DataTypes.TYPE_NAMES[i]).append(" , ");
		}

		return stringBuilder;
	}

	/**
	 * Returns true if the timestamp is set. A timestamp is valid if it is
	 * set.
	 * 
	 * @return Whether the timestamp is set or not. If it is >0 it is assumed to be
	 *         set
	 */
	public boolean isTimestampSet() {
		return this.timeStamp > 0 || timestampProvided;
	}

	/**
	 * Sets the time stamp of this stream element.
	 * 
	 * @param timeStamp The time stamp value. If the timestamp is zero or
	 *                  negative, it is considered non valid and zero will be
	 *                  placed.
	 */
	public void setTimeStamp(long timeStamp) {
		if (timeStamp <= 0) {
			this.timeStamp = 0;
		} else {
			this.timeStamp = timeStamp;
		}

	}

	/**
	 * This method gets the attribute name as the input and returns the value
	 * corresponding to that tuple.
	 * 
	 * @param fieldName The name of the tuple.
	 * @return The value corresponding to the named tuple.
	 */
	public final Serializable getData(final String fieldName) {
		generateIndex();
		Integer index = indexedFieldNames.get(fieldName);
		if (index == null) {
			logger.warn("There is a request for field " + fieldName + " for StreamElement: " + this.toString()
					+ ". As the requested field doesn't exist, GSN returns Null to the callee.");
			return null;
		}
		return this.fieldValues[index];
	}

	/**
	 * This method gets the attribute name as the input and returns the type of the
	 * value
	 * corresponding to that tuple.
	 * 
	 * @param fieldName The name of the tuple.
	 * @return The type of the value corresponding to the named tuple.
	 */
	public final Byte getType(final String fieldName) {
		generateIndex();
		Integer index = indexedFieldNames.get(fieldName);
		if (index == null) {
			if (logger.isDebugEnabled()) {
				logger.warn("There is a request for type of field " + fieldName + " for StreamElement: "
						+ this.toString() + ". As the requested field doesn't exist, GSN returns Null to the callee.");
			}
			return null;
		}
		return this.fieldTypes[index];
	}

	public long getInternalPrimayKey() {
		return internalPrimayKey;
	}

	public void setInternalPrimayKey(long internalPrimayKey) {
		this.internalPrimayKey = internalPrimayKey;
	}

	/**
	 * This method compares this StreamElement with an other one,
	 * ignoring the specified field names and ignoring the TIMED value.
	 * Two StreamElements are seen to be the same, if all elements
	 * (except the ones to be ignored) are of equal type and content.
	 * 
	 * @param se:                    an other StreamElement.
	 * @param fieldNamesToBeIgnored: all field names to be ignored
	 * @return true, if both StreamElements are the same, otherwise false.
	 */
	public boolean equalsIgnoreTimedAndFields(StreamElement se, String[] fieldNamesToBeIgnored) {
		if (se == null) {
			return false;
		}

		if (this.getFieldNames().length != se.getFieldNames().length) {
			return false;
		}

		Serializable[] fieldValues = se.getData();
		String[] fieldNames = se.getFieldNames();
		Byte[] fieldTypes = se.getFieldTypes();

		for (int i = 0; i < this.getFieldNames().length; i++) {
			if (this.fieldTypes[i].compareTo(fieldTypes[i]) != 0) {
				return false;
			}

			if (this.fieldNames[i].compareToIgnoreCase(fieldNames[i]) != 0) {
				return false;
			}

			boolean cont = false;
			if (fieldNamesToBeIgnored != null) {
				for (int j = 0; j < fieldNamesToBeIgnored.length; j++) {
					if (this.fieldNames[i].compareToIgnoreCase(fieldNamesToBeIgnored[j]) == 0) {
						cont = true;
						break;
					}
				}
			}
			if (cont) {
				continue;
			}

			if (this.fieldValues[i] == null && fieldValues[i] != null) {
				return false;
			}

			if (this.fieldValues[i] != null && fieldValues[i] == null) {
				return false;
			}

			if (!(this.fieldValues[i] == null && fieldValues[i] == null)) {
				switch (fieldTypes[i]) {
					case DataTypes.DOUBLE:
						if (((Double) this.fieldValues[i]).compareTo((Double) fieldValues[i]) != 0) {
							return false;
						}
						break;
					case DataTypes.BIGINT:
						if (((Long) this.fieldValues[i]).compareTo((Long) fieldValues[i]) != 0) {
							return false;
						}
						break;
					case DataTypes.TINYINT:
						if (((Byte) this.fieldValues[i]).compareTo((Byte) fieldValues[i]) != 0) {
							return false;
						}

						break;
					case DataTypes.SMALLINT:
						if (((Short) this.fieldValues[i]).compareTo((Short) fieldValues[i]) != 0) {
							return false;
						}

						break;
					case DataTypes.INTEGER:
						if (((Integer) this.fieldValues[i]).compareTo((Integer) fieldValues[i]) != 0) {
							return false;
						}
						break;
					case DataTypes.CHAR:
					case DataTypes.VARCHAR:
						if (((String) this.fieldValues[i]).compareTo((String) fieldValues[i]) != 0) {
							return false;
						}

						break;
					case DataTypes.BINARY:
						if (!Arrays.equals((byte[]) this.fieldValues[i], (byte[]) fieldValues[i])) {
							return false;
						}
						break;
					default:
						logger.error("Type can't be converted : TypeID : " + fieldTypes[i]);
						return false;
				}
			}
		}
		return true;
	}

	/**
	 * @return
	 */
	public Object[] getDataInRPCFriendly() {
		Object[] toReturn = new Object[fieldValues.length];
		for (int i = 0; i < toReturn.length; i++) {
			// process null values
			if (fieldValues[i] == null) {
				toReturn[i] = NULL_ENCODING;
				continue;
			}
			switch (fieldTypes[i]) {
				case DataTypes.DOUBLE:
				case DataTypes.FLOAT:
					toReturn[i] = fieldValues[i];
					break;
				case DataTypes.BIGINT:
					toReturn[i] = Long.toString((Long) fieldValues[i]);
					break;
				// case DataTypes.TIME :
				// toReturn[ i ] = Long.toString( ( Long ) fieldValues[ i ] );
				// break;
				case DataTypes.TINYINT:
				case DataTypes.SMALLINT:
				case DataTypes.INTEGER:
					toReturn[i] = new Integer((Integer) fieldValues[i]);
					break;
				case DataTypes.CHAR:
				case DataTypes.VARCHAR:
				case DataTypes.BINARY:
					toReturn[i] = fieldValues[i];
					break;
				default:
					logger.error("Type can't be converted : TypeID : " + fieldTypes[i]);
					break;
			}
		}
		return toReturn;

	}

	/**
	 * Build stream elements from a JSON representation like this one:
	 * {"type":"Feature","properties":{"vs_name":"geo_oso3m","values":[[1464094800000,21,455.364922]],"fields":[{"name":"timestamp","type":"time","unit":"ms"},{"name":"station","type":"smallint","unit":null},{"name":"altitude","type":"float","unit":null}],"stats":{"start-datetime":1381953249010,"end-datetime":1464096133100},"geographical":"Lausanne,
	 * Switzerland","description":"OZ47
	 * Sensor"},"geometry":{"type":"Point","coordinates":[6.565356337141691,46.5608445136986,689.7967]},"total_size":0,"page_size":0}
	 * Expecting the first value to be the timestamp
	 * 
	 * @param s
	 * @return
	 */

	public static StreamElement[] fromJSON(String s) {
		JsonNode jn = Json.parse(s).get("properties");
		DataField[] df = new DataField[jn.get("fields").size() - 1];
		int i = 0;
		for (JsonNode f : jn.get("fields")) {
			if (f.get("name").asText().equals("timestamp")) {
				continue;
			}
			df[i] = new DataField(f.get("name").asText(), f.get("type").asText());
			i++;
		}
		StreamElement[] ret = new StreamElement[jn.get("values").size()];
		int k = 0;
		for (JsonNode v : jn.get("values")) {
			Serializable[] data = new Serializable[df.length];
			for (int j = 1; j < v.size(); j++) {
				switch (df[j - 1].getDataTypeID()) {
					case DataTypes.DOUBLE:
						data[j - 1] = v.get(j).asDouble();
						break;
					case DataTypes.FLOAT:
						data[j - 1] = (float) v.get(j).asDouble();
						break;
					case DataTypes.BIGINT:
						data[j - 1] = v.get(j).asLong();
						break;
					case DataTypes.TINYINT:
						data[j - 1] = (byte) v.get(j).asInt();
						break;
					case DataTypes.SMALLINT:
						data[j - 1] = (short) v.get(j).asInt();
						break;
					case DataTypes.INTEGER:
						data[j - 1] = v.get(j).asInt();
						break;
					case DataTypes.CHAR:
					case DataTypes.VARCHAR:
						data[j - 1] = v.get(j).asText();
						break;
					case DataTypes.BINARY:
						data[j - 1] = (byte[]) Base64.decodeBase64(v.get(j).asText());
						break;
					default:
						logger.error("The data type of the field cannot be parsed: " + df[j - 1].toString());
						break;
				}
			}
			ret[k] = new StreamElement(df, data, v.get(0).asLong());
			k++;
		}
		return ret;
	}

	public static StreamElement[] fromCSV(Path filePath, Map<String, String> namesTypesMap) {
		List<StreamElement> ret = new ArrayList<>();

		try {
            // Read all bytes from the file
            byte[] fileBytes = Files.readAllBytes(filePath);

            // Convert the byte array to a string
            String csvContent = new String(fileBytes);

			BufferedReader reader = new BufferedReader(new StringReader(csvContent));
			// Read the first line which contains column headers
			String firstLine = reader.readLine();
	
			// Split the first line using a CSV separator
			String[] columnNames = firstLine.split(",");
	
			// Trim whitespace from each column name
			for (int i = 0; i < columnNames.length; i++) {
				columnNames[i] = columnNames[i].trim().replace("\"", "");
			}
	
			// Read the remaining lines containing data
			String line;
			while ((line = reader.readLine()) != null) {
				
				// Split the first line using a CSV separator
				String[] values = line.split(",");
				List<Byte> byteList = new ArrayList<Byte>();
				List<Serializable> serializableList = new ArrayList<Serializable>();
				List<String> n = new ArrayList<String>();
				long timestamp=0L;
				for (int i = 0; i < values.length; i++) {
					values[i] = values[i].trim();
					if("timed".equals(columnNames[i])){
						timestamp = Long.parseLong(values[i]);
					}
					
					String key = columnNames[i];

					if(namesTypesMap.get(key) != null){
						String lowerCaseTypeStr = namesTypesMap.get(key);
						switch (lowerCaseTypeStr.split("\\(")[0]) {
							case "double":
								byteList.add(DataTypes.DOUBLE);
								if(values[i].equals("")){
									serializableList.add(null);
								}else{
									serializableList.add(Double.parseDouble(values[i]));
								}
								n.add(key);
								break;
							case "float":
								byteList.add(DataTypes.FLOAT);
								if(values[i].equals("")){
									serializableList.add(null);
								}else{
									serializableList.add((float) Double.parseDouble(values[i]));
								}
								n.add(key);
								break;
							case "bigint":
								byteList.add(DataTypes.BIGINT);
								if(values[i].equals("")){
									serializableList.add(null);
								}else{
									serializableList.add(Long.parseLong(values[i]));
								}
								n.add(key);
								break;
							case "tinyint":
								byteList.add(DataTypes.TINYINT);
								if(values[i].equals("")){
									serializableList.add(null);
								}else{
									serializableList.add((byte) Integer.parseInt(values[i]));
								}
								n.add(key);
								break;
							case "smallint":
								byteList.add(DataTypes.SMALLINT);
								if(values[i].equals("")){
									serializableList.add(null);
								}else{
									serializableList.add((short) Integer.parseInt(values[i]));
								}
								n.add(key);
								break;
							case "integer":
								byteList.add(DataTypes.INTEGER);
								if(values[i].equals("")){
									serializableList.add(null);
								}else{
									serializableList.add(Integer.parseInt(values[i]));
								}
								n.add(key);
								break;
							case "char":
							case "varchar":
								byteList.add(DataTypes.VARCHAR);
								if(values[i].equals("")){
									serializableList.add(null);
								}else{
									serializableList.add(values[i].toString());
								}
								n.add(key);
								break;
							case "binary":
								byteList.add(DataTypes.BINARY);
								if(values[i].equals("")){
									serializableList.add(null);
								}else{
									serializableList.add(Base64.decodeBase64(values[i]));
								}
								n.add(key);
								break;
							default:
								throw new IllegalArgumentException("Unknown data type: " + lowerCaseTypeStr);
						}
					}
				}
				
				String[] stringArray = n.toArray(new String[0]);
				Byte[] byteArray = byteList.toArray(new Byte[0]);
				Serializable[] serializableArray = serializableList.toArray(new Serializable[0]);
				StreamElement se = new StreamElement(stringArray, byteArray, serializableArray,timestamp);

				ret.add(se);
			} 

        } catch (IOException e) {
            e.printStackTrace();
        }

		return ret.toArray(new StreamElement[0]);
	}

	/**
	 * Returns the type of the field in the output format or -1 if the field doesn't
	 * exit.
	 * 
	 * @param outputFormat
	 * @param fieldName
	 * @return
	 */
	private static byte findIndexInDataField(DataField[] outputFormat, String fieldName) {
		for (int i = 0; i < outputFormat.length; i++) {
			if (outputFormat[i].getName().equalsIgnoreCase(fieldName)) {
				return outputFormat[i].getDataTypeID();
			}
		}

		return -1;
	}

	/***
	 * Used with the new JRuby/Mongrel/Rest interface
	 */

	public static StreamElement fromREST(DataField[] outputFormat, String[] fieldNames, String[] fieldValues,
			String timestamp) {
		Serializable[] values = new Serializable[outputFormat.length];
		for (int i = 0; i < fieldNames.length; i++) {
			switch (findIndexInDataField(outputFormat, (String) fieldNames[i])) {
				case DataTypes.DOUBLE:
					values[i] = Double.parseDouble(fieldValues[i]);
					break;
				case DataTypes.FLOAT:
					values[i] = Float.parseFloat((String) fieldValues[i]);
					break;
				case DataTypes.BIGINT:
					// case DataTypes.TIME :
					values[i] = Long.parseLong((String) fieldValues[i]);
					break;
				case DataTypes.TINYINT:
					values[i] = Byte.parseByte((String) fieldValues[i]);
					break;
				case DataTypes.SMALLINT:
				case DataTypes.INTEGER:
					values[i] = Integer.parseInt(fieldValues[i]);
					break;
				case DataTypes.CHAR:
				case DataTypes.VARCHAR:
					values[i] = new String(Base64.decodeBase64(fieldValues[i].getBytes()));
					break;
				case DataTypes.BINARY:
					values[i] = (byte[]) Base64.decodeBase64(fieldValues[i].getBytes());
					break;
				case -1:
				default:
					logger.error("The field name doesn't exit in the output structure : FieldName : "
							+ (String) fieldNames[i]);
					break;
			}
		}
		return new StreamElement(outputFormat, values, Long.parseLong(timestamp));
	}

	/**
	 * Creates a {@link StreamElement} instance from REST API data represented by
	 * field names and values.
	 *
	 * @param outputFormat The array of {@link DataField} representing the expected
	 *                     output format.
	 * @param fieldNames   The array of field names corresponding to the REST API
	 *                     data.
	 * @param fieldValues  The array of field values corresponding to the REST API
	 *                     data.
	 * @return A {@link StreamElement} instance constructed from the provided field
	 *         names and values.
	 */
	public static StreamElement createElementFromREST(DataField[] outputFormat, String[] fieldNames,
			Object[] fieldValues) {
		ArrayList<Serializable> values = new ArrayList<Serializable>();
		// ArrayList<String> fields = new ArrayList<String>();

		long timestamp = -1;
		for (int i = 0; i < fieldNames.length; i++) {
			if (fieldNames[i].equalsIgnoreCase("TIMED")) {
				timestamp = Long.parseLong((String) fieldValues[i]);
				continue;
			}
			boolean found = false;
			for (DataField f : outputFormat) {
				if (f.getName().equalsIgnoreCase(fieldNames[i])) {
					// fields.add(fieldNames[i]);
					found = true;
					break;
				}
			}
			if (!found) {
				continue;
			}

			switch (findIndexInDataField(outputFormat, fieldNames[i])) {
				case DataTypes.DOUBLE:
					values.add(Double.parseDouble((String) fieldValues[i]));
					break;
				case DataTypes.FLOAT:
					values.add(Float.parseFloat((String) fieldValues[i]));
					break;
				case DataTypes.BIGINT:
					values.add(Long.parseLong((String) fieldValues[i]));
					break;
				case DataTypes.TINYINT:
					values.add(Byte.parseByte((String) fieldValues[i]));
					break;
				case DataTypes.SMALLINT:
					values.add(Short.parseShort((String) fieldValues[i]));
					break;
				case DataTypes.INTEGER:
					values.add(Integer.parseInt((String) fieldValues[i]));
					break;
				case DataTypes.CHAR:
				case DataTypes.VARCHAR:
					values.add(new String((byte[]) fieldValues[i]));
					break;
				case DataTypes.BINARY:
					try {
						// StreamElementTest.md5Digest(fieldValues[ i ]);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
					values.add((byte[]) fieldValues[i]);
					break;
				case -1:
				default:
					logger.error("The field name doesn't exit in the output structure : FieldName : "
							+ (String) fieldNames[i]);
					break;
			}

		}
		if (timestamp == -1) {
			timestamp = System.currentTimeMillis();
		}

		return new StreamElement(outputFormat, values.toArray(new Serializable[] {}), timestamp);
	}

	public StreamElement4Rest toRest() {
		return new StreamElement4Rest(this);
	}

	/**
	 * Build the index for mapping field name to their positions in the array if it
	 * is not yet built
	 * This assumes that StreamElements cannot change their structure
	 */
	private void generateIndex() {
		if (indexedFieldNames == null) {
			indexedFieldNames = new TreeMap<String, Integer>(new CaseInsensitiveComparator());
			for (int i = 0; i < this.fieldNames.length; i++) {
				this.indexedFieldNames.put(fieldNames[i], i);
			}

		}
	}

	/**
	 * set the data in the corresponding field, throws an exception if the data type
	 * doesn't match
	 * 
	 * @param fieldName
	 * @param data
	 * @throws IllegalArgumentException
	 */
	public void setData(String fieldName, Serializable data) throws IllegalArgumentException {
		generateIndex();
		Integer index = indexedFieldNames.get(fieldName);
		if (index == null) {
			logger.warn("There is a request for setting field " + fieldName + " for StreamElement: " + this.toString()
					+ ". But the requested field doesn't exist.");
		}
		verifyTypeCompatibility(fieldTypes[index], data);
		setData(index, data);
	}

	/**
	 * Generates a JSON representation of the stream element in GeoJSON format with
	 * additional properties.
	 *
	 * @param vs_name The name of the virtual sensor.
	 * @return A JSON string representing the stream element in GeoJSON format.
	 */
	public String toJSON(String vs_name) {

		GeoJsonField[] fields = new GeoJsonField[getFieldNames().length + 1];
		fields[0] = new GeoJsonField();
		fields[0].setName("timestamp");
		fields[0].setType("time");
		fields[0].setUnit("ms");
		for (int i = 1; i < fields.length; i++) {
			fields[i] = new GeoJsonField();
			fields[i].setName(getFieldNames()[i - 1]);
			fields[i].setType(DataTypes.TYPE_NAMES[getFieldTypes()[i - 1]]);
		}
		Serializable[] values = new Serializable[fields.length];
		values[0] = getTimeStamp();
		for (int j = 1; j < fields.length; j++) {
			values[j] = fieldValues[j - 1];
		}
		GeoJsonProperties prop = new GeoJsonProperties();
		prop.setVs_name(vs_name);
		prop.setFields(fields);
		prop.setValues(new Serializable[][] { values });
		GeoJsonFeature feature = new GeoJsonFeature();
		feature.setPage_size(1);
		feature.setTotal_size(1);
		feature.setType("Feature");
		feature.setProperties(prop);

		return Json.toJson(feature).toString();
	}

	/**
	 * Calculates and returns the volume of the stream element. The volume
	 * represents the data size in bytes.
	 * The calculation is based on the size of individual fields and their
	 * corresponding data types.
	 *
	 * @return The volume of the stream element in bytes.
	 */
	public long getVolume() {
		if (volume == null) {
			volume = (long) 0;
			for (int i = 0; i < fieldNames.length; i++) {
				if (fieldValues[i] == null) {
					continue;
				}
				switch (fieldTypes[i]) {
					case DataTypes.TINYINT:
						volume += 1;
						break;
					case DataTypes.SMALLINT:
						volume += 2;
						break;
					case DataTypes.BIGINT:
						volume += 8;
						break;
					case DataTypes.CHAR:
						volume += 2;
						break;
					case DataTypes.VARCHAR:
						volume += ((String) fieldValues[i]).getBytes().length;
						break;
					case DataTypes.INTEGER:
						volume += 4;
						break;
					case DataTypes.DOUBLE:
						if (fieldValues[i] instanceof Float) {
							volume += 4;
						} else if (fieldValues[i] instanceof Double) {
							volume += 8;
						}

						break;
					case DataTypes.BINARY:
						if (fieldValues[i] instanceof byte[]) {
							volume += ((byte[]) fieldValues[i]).length;
						} else if (fieldValues[i] instanceof String) {
							volume += ((String) fieldValues[i]).getBytes().length;
						}

						break;
					default:
						break;
				}
			}
		}
		return volume;
	}
}
