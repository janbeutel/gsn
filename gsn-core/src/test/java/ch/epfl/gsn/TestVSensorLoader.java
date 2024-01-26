/**
* Global Sensor Networks (GSN) Source Code
* Copyright (c) 2006-2016, Ecole Polytechnique Federale de Lausanne (EPFL)
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
* File: src/ch/epfl/gsn/TestVSensorLoader.java
*
* @author Ali Salehi
* @author Timotee Maret
*
*/

package ch.epfl.gsn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.epfl.gsn.Main;
import ch.epfl.gsn.VSensorLoader;
import ch.epfl.gsn.VirtualSensor;
import ch.epfl.gsn.beans.AddressBean;
import ch.epfl.gsn.beans.InputStream;
import ch.epfl.gsn.beans.StreamSource;
import ch.epfl.gsn.beans.VSensorConfig;
import ch.epfl.gsn.storage.StorageManager;
import ch.epfl.gsn.storage.StorageManagerFactory;
import ch.epfl.gsn.wrappers.MockWrapper;
import java.io.IOException;

import org.junit.Ignore;

public class TestVSensorLoader {

    private static StorageManager sm = null;
	
	private AddressBean[] addressing= new AddressBean[] {new AddressBean("mock-test")};
	private VSensorConfig testVensorConfig;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		// Setup current working directory
        String currentWorkingDir = System.getProperty("user.dir");
		if (!currentWorkingDir.endsWith("/gsn-core/")) {
			String newDirectory = currentWorkingDir + "/gsn-core/";
        	System.setProperty("user.dir", newDirectory);
		}

		DriverManager.registerDriver( new org.h2.Driver( ) );
		sm = StorageManagerFactory.getInstance( "org.h2.Driver","sa","" ,"jdbc:h2:mem:test", Main.DEFAULT_MAX_DB_CONNECTIONS);

		Main.setDefaultGsnConf("/gsn_test.xml");
	  	Main.getInstance();
	
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}

	@Before
	public void setUp() throws Exception {
		Properties p = new Properties();
		p.put("mock-test", "ch.epfl.gsn.wrappers.MockWrapper");
		p.put("system-time", "ch.epfl.gsn.wrappers.SystemTime");
		
		testVensorConfig = new VSensorConfig();
		testVensorConfig.setName("test");
		File someFile = File.createTempFile("bla", ".xml");
		testVensorConfig.setMainClass("ch.epfl.gsn.vsensor.BridgeVirtualSensor");
		testVensorConfig.setFileName(someFile.getAbsolutePath());
	
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testCreateInputStreams() {

	}

	@Test
	public void testPrepareWrapper() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		VSensorLoader loader = new VSensorLoader();
		MockWrapper wrapper = (MockWrapper) loader.createWrapper(addressing[0]);
		assertNotNull(wrapper);
	}

	@Test
	public void testPrepareStreamSource() {

	}

	@Test
	public void testStopLoading() throws IOException {
		VirtualSensor pool = new VirtualSensor(testVensorConfig);

		InputStream is = new InputStream();
		is.setInputStreamName("t1");
		is.setQuery("select * from my-stream1");
		StreamSource ss1 = new StreamSource().setAlias("my-stream1").setAddressing(new AddressBean[] {new AddressBean("mock-test")}).setSqlQuery("select * from wrapper").setRawHistorySize("2").setInputStream(is);	

		ss1.setSamplingRate(1);
		assertTrue(ss1.validate());
		is.setSources(ss1);
		assertTrue(is.validate());
		testVensorConfig.setInputStreams(is);
		assertTrue(testVensorConfig.validate());
	}
	@Test
	public void testOneInputStreamUsingTwoStreamSources() throws InstantiationException, IllegalAccessException, SQLException {
		VSensorLoader loader = new VSensorLoader();
		InputStream is = new InputStream();
		MockWrapper wrapper = (MockWrapper) loader.createWrapper(addressing[0]);
		
		StreamSource ss1 = new StreamSource().setAlias("my-stream1").setAddressing(new AddressBean[] {new AddressBean("mock-test")}).setSqlQuery("select * from wrapper").setRawHistorySize("2").setInputStream(is);		
		ss1.setWrapper(wrapper);
		ss1.setSamplingRate(1);
		assertTrue(ss1.validate());
		assertTrue(loader.prepareStreamSource(testVensorConfig, is, ss1));

		StreamSource ss2 = new StreamSource().setAlias("my-stream2").setAddressing(new AddressBean[] {new AddressBean("mock-test")}).setSqlQuery("select * from wrapper").setRawHistorySize("20").setInputStream(is);		
		ss2.setSamplingRate(1);
		assertTrue(ss2.validate());
		assertTrue(loader.prepareStreamSource(testVensorConfig, is, ss2));
		ss1.getWrapper().releaseResources();
		assertFalse(sm.tableExists(ss1.getWrapper().getDBAliasInStr()));
	}
	
	@Test
	public void testReloadingVirtualSensor() throws InstantiationException, IllegalAccessException, SQLException {
		VSensorLoader loader = new VSensorLoader();
		InputStream is = new InputStream();
		
		StreamSource ss = new StreamSource().setAlias("my-stream1").setAddressing(addressing).setSqlQuery("select * from wrapper").setRawHistorySize("2").setInputStream(is);		
		MockWrapper wrapper = (MockWrapper) loader.createWrapper(addressing[0]);
		ss.setWrapper(wrapper);
		ss.setSamplingRate(1);

		assertTrue(ss.validate());
		assertTrue(loader.prepareStreamSource(testVensorConfig, is, ss));
		assertTrue(sm.tableExists(ss.getWrapper().getDBAliasInStr()));
		assertTrue(sm.tableExists(ss.getUIDStr()));
		assertFalse(is.getRenamingMapping().isEmpty());
		loader.releaseStreamSource(ss);
		assertTrue(is.getRenamingMapping().isEmpty());
		assertFalse(sm.tableExists(ss.getUIDStr()));
		assertFalse(ss.getWrapper().isActive());
		assertFalse(sm.tableExists(ss.getWrapper().getDBAliasInStr()));
		assertTrue(is.getRenamingMapping().isEmpty());
		ss = new StreamSource().setAlias("my-stream1").setAddressing(addressing).setSqlQuery("select * from wrapper").setRawHistorySize("2").setInputStream(is);		
		ss.setSamplingRate(1);
		assertTrue(loader.prepareStreamSource(testVensorConfig, is, ss));	
	}

}