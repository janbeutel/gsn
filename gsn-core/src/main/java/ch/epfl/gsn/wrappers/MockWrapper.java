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
* File: src/ch/epfl/gsn/wrappers/MockWrapper.java
*
* @author Ali Salehi
* @author Mehdi Riahi
* @author Davide De Sclavis
* @author Manuel Buchauer
* @author Jan Beutel
*
*/

package ch.epfl.gsn.wrappers;

import ch.epfl.gsn.beans.DataField;
import ch.epfl.gsn.beans.StreamElement;

public class MockWrapper extends AbstractWrapper {
	int threadCounter;

	private DataField[] outputFormat = new DataField[] { new DataField("data", "int") };

	public boolean initialize() {
		return true;
	}

	public void run() {

	}

	public DataField[] getOutputFormat() {
		return outputFormat;
	}

	public boolean publishStreamElement(StreamElement se) {
		return postStreamElement(se);
	}

	public void dispose() {
		threadCounter--;
	}

	public String getWrapperName() {
		return "TestWrapperMock";
	}

}
