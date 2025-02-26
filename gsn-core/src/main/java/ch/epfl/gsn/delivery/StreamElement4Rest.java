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
* File: src/ch/epfl/gsn/http/rest/StreamElement4Rest.java
*
* @author Ali Salehi
* @author Davide De Sclavis
* @author Manuel Buchauer
* @author Jan Beutel
*
*/

package ch.epfl.gsn.delivery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.XStream;

import ch.epfl.gsn.beans.DataField;
import ch.epfl.gsn.beans.StreamElement;

/**
 * Represents a StreamElement for REST communication.
 */
public class StreamElement4Rest {

	private List<Field4Rest> fields = new ArrayList<Field4Rest>();
	private Date timestamp;
	/**
	 * Default constructor.
	 */
	public StreamElement4Rest() {

	}

	/**
	 * Constructs a StreamElement4Rest object from a StreamElement.
	 *
	 * @param se The StreamElement object to convert.
	 */
	public StreamElement4Rest(StreamElement se) {
		this.timestamp = new Date(se.getTimeStamp());
		for (int i = 0; i < se.getFieldNames().length; i++) {
			fields.add(new Field4Rest(se.getFieldNames()[i], se.getFieldTypes()[i], se.getData()[i]));
		}
	}

	/**
	 * Returns a string representation of the StreamElement4Rest object.
	 *
	 * @return The string representation of the object.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder("StreamElement4Rest: (timestamp:");
		sb.append(timestamp.toString());
		sb.append("Fields =>{ ");
		for (Field4Rest field : fields) {
			sb.append(field.toString()).append(", ");
		}

		sb.append("})");
		return sb.toString();
	}



	/**
	 * Converts the StreamElement4Rest object to a StreamElement object.
	 *
	 * @return The converted StreamElement object.
	 */
	public StreamElement toStreamElement() {
		String[] names = new String[fields.size()];
		Serializable[] values = new Serializable[fields.size()];
		Byte[] types = new Byte[fields.size()];
		int idx = 0;
		for (Field4Rest field : fields) {
			names[idx] = field.getName();
			values[idx] = field.getValue();
			types[idx] = field.getType();
			idx++;
		}

		return new StreamElement(names, types, values, timestamp.getTime());

	}

	/**
	 * Returns an XStream object configured for StreamElement4Rest serialization.
	 *
	 * @return The configured XStream object.
	 */
	public static XStream getXstream() {
		XStream xstream = new XStream();
		xstream.alias("stream-element", StreamElement4Rest.class);
		xstream.alias("field", Field4Rest.class);
		xstream.useAttributeFor(StreamElement4Rest.class, "timestamp");
		xstream.addImplicitCollection(StreamElement4Rest.class, "fields");
		xstream.registerConverter(new Field4RestConverter());
		xstream.alias("strcture", DataField.class);

		return xstream;
	}

	public static XStream getXstream4Structure() {
		XStream xstream = new XStream();
		// xstream.alias("stream-element", StreamElement4Rest.class);
		// xstream.alias("field", Field4Rest.class);
		// xstream.useAttributeFor(StreamElement4Rest.class,"timestamp");
		// xstream.addImplicitCollection(StreamElement4Rest.class, "fields");
		// xstream.registerConverter(new Field4RestConverter());
		return xstream;
	}

	/**
	 * Returns the timestamp of the StreamElement4Rest object.
	 *
	 * @return The timestamp.
	 */
	public Date getTimestamp() {
		return timestamp;
	}

}
