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
* File: src/ch/epfl/gsn/beans/Modifications.java
*
* @author Mehdi Riahi
* @author gsn_devs
* @author Sofiane Sarni
* @author Ali Salehi
* @author Davide De Sclavis
* @author Manuel Buchauer
* @author Jan Beutel
*
*/

package ch.epfl.gsn.beans;

import ch.epfl.gsn.config.VsConf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.slf4j.LoggerFactory;

import ch.epfl.gsn.Main;
import ch.epfl.gsn.Mappings;
import ch.epfl.gsn.delivery.LocalDeliveryWrapper;
import ch.epfl.gsn.storage.SQLUtils;
import ch.epfl.gsn.utils.graph.Graph;
import ch.epfl.gsn.utils.graph.Node;
import ch.epfl.gsn.utils.graph.NodeNotExistsExeption;

import java.net.URI;
import java.net.URISyntaxException;
import org.slf4j.Logger;

/**
 * This class holds the files changed in the virtual-sensor directory and
 * adds/removes the virtual sensors based on the changes.
 *
 */
public final class Modifications {

	private final ArrayList<VSensorConfig> addVirtualSensorConf = new ArrayList<VSensorConfig>();

	private final ArrayList<VSensorConfig> removeVirtualSensorConf = new ArrayList<VSensorConfig>();

	private Graph<VSensorConfig> graph;

	private static transient Logger logger = LoggerFactory.getLogger(Modifications.class);

	/**
	 * The list of the virtual sensors, sorted by dependency relations between them,
	 * to be added to the GSN.
	 *
	 * @return Returns the add.
	 */
	public ArrayList<VSensorConfig> getAdd() {
		return addVirtualSensorConf;
	}

	/**
	 * @param add The add to set.
	 */
	public void setAdd(final Collection<String> add) {
		addVirtualSensorConf.clear();
		ArrayList<VSensorConfig> toAdd = new ArrayList<VSensorConfig>();
		loadVirtualSensors(add, toAdd);
		fillGraph(graph, toAdd.iterator());

		List<VSensorConfig> nodesByDFSSearch = graph.getNodesByDFSSearch();
		for (VSensorConfig config : nodesByDFSSearch) {
			int indexOf = toAdd.indexOf(config);
			if (indexOf != -1) {
				addVirtualSensorConf.add(toAdd.get(indexOf));
			}
		}
	}

	/**
	 * Note that the list parameter should be an empty arrayList;
	 *
	 * @param fileNames
	 * @param list
	 */
	private void loadVirtualSensors(Collection<String> fileNames, ArrayList<VSensorConfig> list) {
		if (fileNames == null || list == null) {
			throw new RuntimeException("Null pointer Exception (" + (fileNames == null) + "),(" + (list == null) + ")");
		}
		/*
		 * IBindingFactory bfact;
		 * IUnmarshallingContext uctx;
		 * try {
		 * bfact = BindingDirectory.getFactory( VSensorConfig.class );
		 * uctx = bfact.createUnmarshallingContext( );
		 * } catch ( JiBXException e1 ) {
		 * logger.fatal( e1.getMessage( ) , e1 );
		 * return;
		 * }
		 */
		VSensorConfig configuration;
		for (String file : fileNames) {
			try {
				// configuration = ( VSensorConfig ) uctx.unmarshalDocument( new
				// FileInputStream( file ) , null );
				VsConf vsConf = VsConf.load(file);
				configuration = BeansInitializer.vsensor(vsConf);
				configuration.setFileName(file);
				if (!configuration.validate()) {
					logger.error("Adding the virtual sensor specified in " + file
							+ " failed because of one or more problems in configuration file.");
					logger.info("Please check the file and try again");
					continue;
				}

				list.add(configuration);
				/*
				 * } catch ( JiBXException e ) {
				 * logger.error( e.getMessage( ) , e );
				 * logger.error( new StringBuilder( ).append(
				 * "Adding the virtual sensor specified in " ).append( file ).append(
				 * " failed because there is syntax error in the configuration file. Please check the configuration file and try again."
				 * ).toString( ) );
				 */
			} catch (Exception e) {
				logger.error("Adding the virtual sensor specified in " + file + " failed." + e.getMessage());
			}
		}
	}

	/**
	 * The list of the virtual sensors which should be removed.
	 *
	 * @return Returns the remove.
	 */
	public ArrayList<VSensorConfig> getRemove() {
		return removeVirtualSensorConf;
	}

	/**
	 * @param listOfTheRemovedVirtualSensorsFileName The remove to set.
	 */
	public void setRemove(final Collection<String> listOfTheRemovedVirtualSensorsFileName) {
		removeVirtualSensorConf.clear();
		// file has been removed, the virtual sensor and dependent virtual sensors
		// should be deleted
		for (String fileName : listOfTheRemovedVirtualSensorsFileName) {
			VSensorConfig vSensorConfig = Mappings.getConfigurationObject(fileName);
			if (vSensorConfig != null) {
				Node<VSensorConfig> node = graph.findNode(vSensorConfig);
				if (node != null && !removeVirtualSensorConf.contains(vSensorConfig)) {
					// adding to removed list the removed vs and all virtual sensors that depend on
					// it
					List<Node<VSensorConfig>> nodesAffectedByRemoval = graph.nodesAffectedByRemoval(node);
					for (Node<VSensorConfig> toRemoveNode : nodesAffectedByRemoval) {
						VSensorConfig config = toRemoveNode.getObject();
						if (!removeVirtualSensorConf.contains(config)) {
							removeVirtualSensorConf.add(config);
						}

					}
				}
				try {
					graph.removeNode(vSensorConfig);
				} catch (NodeNotExistsExeption e) {
					// This shouldn't happen
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * Construct a new Modifications object.
	 *
	 * @param add    : The list of the virtual sensor descriptor files added.
	 * @param remove : The list of the virtual sensor descriptor files removed.
	 */
	public Modifications(final Collection<String> add, final Collection<String> remove) {
		buildDependencyGraph();
		// the order of the following two methods is important
		setRemove(remove);
		setAdd(add);
	}

	public Graph<VSensorConfig> getGraph() {
		return graph;
	}

	private void buildDependencyGraph() {
		graph = new Graph<VSensorConfig>();
		Iterator<VSensorConfig> allVSensorConfigs = Mappings.getAllVSensorConfigs();
		fillGraph(graph, allVSensorConfigs);
	}

	/**
	 * Note: There my be multiple valid addressing element for each stream source
	 * and for each unique addressing,
	 * an edge is added to the graph
	 * 
	 * @param graph
	 * @param allVSensorConfigs
	 */
	private static void fillGraph(Graph<VSensorConfig> graph, Iterator<VSensorConfig> allVSensorConfigs) {
		HashMap<String, VSensorConfig> vsNameTOVSConfig = new HashMap<String, VSensorConfig>();
		while (allVSensorConfigs.hasNext()) {
			VSensorConfig config = allVSensorConfigs.next();
			if (config != null && config.getName() != null) {
				vsNameTOVSConfig.put(config.getName().toLowerCase().trim(), config);
				graph.addNode(config);
			}
		}

		outFor: for (VSensorConfig config : vsNameTOVSConfig.values()) {
			Collection<InputStream> inputStreams = config.getInputStreams();
			for (InputStream stream : inputStreams) {
				StreamSource[] sources = stream.getSources();
				for (int sourceIndex = 0; sourceIndex < sources.length; sourceIndex++) {
					AddressBean[] addressing = sources[sourceIndex].getAddressing();
					boolean hasValidAddressing = false;
					for (int addressingIndex = 0; addressingIndex < addressing.length; addressingIndex++) {
						String wrapper = addressing[addressingIndex].getWrapper();
						boolean isLocalZeroMQ = false;
						if (wrapper.equals("zeromq-sync")) {
							String address = addressing[addressingIndex].getPredicateValue("address").toLowerCase();
							try {
								isLocalZeroMQ = new URI(address).getScheme().equals("inproc");
							} catch (URISyntaxException e) {
								throw new IllegalArgumentException(e);
							}
						}
						String vsensorName = addressing[addressingIndex].getPredicateValue("query");
						
						if (vsensorName!=null) {
							vsensorName = SQLUtils.getTableName(vsensorName);
							if(vsensorName ==null)
								vsensorName=Double.toString(Math.random()*1000000000.0);
						}else if(isLocalZeroMQ){
							vsensorName = addressing[addressingIndex].getPredicateValue("vsensor");
							if(vsensorName ==null)
								vsensorName=Double.toString(Math.random()*1000000000.0);
						}else {
							vsensorName = addressing[addressingIndex].getPredicateValueWithDefault("name",Double.toString(Math.random()*10000000.0));
						}
						vsensorName = vsensorName.toLowerCase();

						Class<?> wrapperClass = Main.getWrapperClass(wrapper);
						if (wrapperClass == null) {
							// If this addressing element is the last one, remove VS from the graph
							logger.warn("The specified wrapper >" + addressing[addressingIndex].getWrapper()
									+ "< does not exist");
							if (addressingIndex == addressing.length && !hasValidAddressing) {
								try {
									graph.removeNode(config);
								} catch (NodeNotExistsExeption e) {
									logger.error(e.getMessage(), e);
									// This shouldn't happen, as we first add all virtual sensors to the graph
								}
								continue outFor;
							}
							continue;
						}

						if ((wrapperClass.isAssignableFrom(LocalDeliveryWrapper.class))
								|| (wrapper.equals("zeromq-sync") && isLocalZeroMQ)) {
							String vsName = vsensorName.toLowerCase().trim();
							VSensorConfig sensorConfig = vsNameTOVSConfig.get(vsName);
							if (sensorConfig == null) {
								sensorConfig = Mappings.getVSensorConfig(vsName);
							}
							if (sensorConfig == null) {
								if(logger.isDebugEnabled()){
									logger.debug("There is no virtual sensor with name >" + vsName + "< in the >"
										+ config.getName() + "< virtual sensor");
								}
								
								// If this addressing element is the last one, remove VS from the graph
								if (addressingIndex == addressing.length - 1 && !hasValidAddressing) {
									try {
										graph.removeNode(config);
									} catch (NodeNotExistsExeption e) {
										logger.error(e.getMessage(), e);
										// This shouldn't happen, as we first add all virtual sensors to the graph
									}
									continue outFor;
								}
								continue;
							}
							try {
								if (graph.findNode(sensorConfig) != null) {
									graph.addEdge(config, sensorConfig);
									if (graph.hasCycle()) {
										logger.warn("A dependency cycle was found when adding >" + config.getName()
												+ "< virtual sensor. The cycle will be removed");
										graph.removeNode(sensorConfig);
										continue outFor;
									}
									hasValidAddressing = true;
								} else
								// If this addressing element is the last one, remove VS from the graph
								if (addressingIndex == addressing.length - 1 && !hasValidAddressing) {
									try {
										graph.removeNode(config);
									} catch (NodeNotExistsExeption e) {
										logger.error(e.getMessage(), e);
										// This shouldn't happen, as we first add all virtual sensors to the graph
									}
									continue outFor;
								}
							} catch (NodeNotExistsExeption e) {
								logger.error(e.getMessage(), e);
								// This shouldn't happen, as we first add all virtual sensors to the graph
							}

						}
					}
				}
			}
		}
	}

	/*
	 * let's keep it if we want to play with zmq dependency one day
	 * private static boolean isInTheSameGSNInstance(AddressBean addressBean) {
	 * String urlStr = addressBean.getPredicateValue ( "remote-contact-point" );
	 * String host;
	 * int port;
	 * 
	 * if(urlStr != null){
	 * try {
	 * URL url = new URL(urlStr);
	 * host = url.getHost();
	 * port = url.getPort() != -1 ? url.getPort() :
	 * ContainerConfig.DEFAULT_GSN_PORT;
	 * } catch (MalformedURLException e) {
	 * logger.warn("Malformed URL : " + e.getMessage(), e);
	 * return false;
	 * }
	 * }else{
	 * host = addressBean.getPredicateValue ( "host" );
	 * if ( host == null || host.trim ( ).length ( ) == 0 ) {
	 * logger.warn (
	 * "The >host< parameter is missing from the RemoteWrapper wrapper." );
	 * return false;
	 * }
	 * port = addressBean.getPredicateValueAsInt("port"
	 * ,ContainerConfig.DEFAULT_GSN_PORT);
	 * if ( port > 65000 || port <= 0 ) {
	 * logger.warn("Remote wrapper initialization failed, bad port number:"+port);
	 * return false;
	 * }
	 * }
	 * boolean toReturn = (ValidityTools.isLocalhost(host) &&
	 * Main.getContainerConfig().getContainerPort() == port);
	 * return toReturn;
	 * }
	 */
	public static Graph<VSensorConfig> buildDependencyGraphFromIterator(Iterator<VSensorConfig> vsensorIterator) {
		Graph<VSensorConfig> graph = new Graph<VSensorConfig>();
		fillGraph(graph, vsensorIterator);
		return graph;
	}

}
