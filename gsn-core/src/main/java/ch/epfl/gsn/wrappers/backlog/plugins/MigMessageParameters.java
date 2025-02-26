package ch.epfl.gsn.wrappers.backlog.plugins;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import ch.epfl.gsn.beans.AddressBean;
import ch.epfl.gsn.beans.DataField;


/**
 * This class is used by the {@link MigMessagePlugin} to get the Mig message class 
 * information needed.
 */
public class MigMessageParameters {

	private static Hashtable<Class<?>,String> typesMapping = null;

	private final transient Logger logger = LoggerFactory.getLogger( MigMessageParameters.class );

	private ArrayList<Method> getters = null;
	private ArrayList<Method> setters = null;

	private DataField[] outputStructure = null;

	private Method timedFieldGetter = null;

	private static final Comparator<Method> orderMethodComparator = new Comparator<Method>() { 
		public int compare(Method m1, Method m2) { return m1.getName().compareTo(m2.getName()); }
	};
	
	private static final Comparator<Method> equalMethodComparator = new Comparator<Method>() { 
		public int compare(Method m1, Method m2) { 
			boolean sameName = (m1.getName().compareTo(m2.getName()) == 0);
			boolean sameReturnType = (m1.getReturnType().equals(m2.getReturnType()));
			if (sameName && sameReturnType) return 0;
			else return orderMethodComparator.compare(m1, m2);
		}
	};
	


	// Optional Parameters

	private static final String TINYOS_GETTER_PREFIX = "getter-prefix";
	private static final String TINYOS_GETTER_PREFIX_DEFAULT = "get_";
	private String tinyosGetterPrefix = null;

	private static final String TINYOS_SETTER_PREFIX = "setter-prefix";
	private static final String TINYOS_SETTER_PREFIX_DEFAULT = "set_";
	private String tinyosSetterPrefix = null;

	private static final String TINYOS_MESSAGE_LENGTH = "message-length";
	private int tinyOSMessageLength;

	private String tinyosSource = null;

	
	// mandatory parameter
	
	private static final String TINYOS_MESSAGE_NAME = "message-classname";
	private String tinyosMessageName = null;

	//private static final String TINYOS_VERSION = "tinyos-version";
	public static final byte TINYOS_VERSION_1 = 0x01;
	public static final byte TINYOS_VERSION_2 = 0x02;
	private byte tinyosVersion = 0;

	public void initParameters (AddressBean infos, int localSFPort) {

		// Mandatory parameters (may thow RuntimeException)

		tinyosSource = "sf@127.0.0.1:" + localSFPort;

		tinyosMessageName = infos.getPredicateValueWithException(TINYOS_MESSAGE_NAME) ;

		// Define TinyOS version from the superclasses

		try {
			Class<?> messageClass = Class.forName(tinyosMessageName);
			findTinyOSVersionFromClassHierarchy(messageClass);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Unable to find the >" + tinyosMessageName + "< class.");
		}

		// Optional parameters

		tinyosGetterPrefix = infos.getPredicateValueWithDefault(TINYOS_GETTER_PREFIX, TINYOS_GETTER_PREFIX_DEFAULT);
		tinyosSetterPrefix = infos.getPredicateValueWithDefault(TINYOS_SETTER_PREFIX, TINYOS_SETTER_PREFIX_DEFAULT);

		tinyOSMessageLength = Integer.parseInt(infos.getPredicateValueWithDefault(TINYOS_MESSAGE_LENGTH, "-1")) ;

	}

	public void initParameters (AddressBean infos) {

		// Mandatory parameters (may throw RuntimeException)

		//tinyosSource = infos.getPredicateValueWithException(TINYOS_SOURCE) ;

		tinyosMessageName = infos.getPredicateValueWithException(TINYOS_MESSAGE_NAME) ;

		// Define TinyOS version from the superclasses

		try {
			Class<?> messageClass = Class.forName(tinyosMessageName);
			findTinyOSVersionFromClassHierarchy(messageClass);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Unable to find the >" + tinyosMessageName + "< class.");
		}

		// Optional parameters

		tinyosGetterPrefix = infos.getPredicateValueWithDefault(TINYOS_GETTER_PREFIX, TINYOS_GETTER_PREFIX_DEFAULT);
		tinyosSetterPrefix = infos.getPredicateValueWithDefault(TINYOS_SETTER_PREFIX, TINYOS_SETTER_PREFIX_DEFAULT);
		
		tinyOSMessageLength = Integer.parseInt(infos.getPredicateValueWithDefault(TINYOS_MESSAGE_LENGTH, "-1")) ;

	}

	/**
	 * <p>
	 * Build recursively the output structure. This implementation use the refelexivity to get the names of the fields.
	 * The fields which will compose the output structure must hold the following conditions:
	 * </p>
	 * <ul>
	 * <li>The method name must be suffixed either by the specified <code>tinyosGetterPrefix</code> or <code>get_</code> by default.</li>
	 * <li>The return type must be supported. See <code>buildMappings</code> method for a list of supported types.</li>
	 * </ul>
	 * @param tosmsgClass
	 * @param fields
	 */
	public void buildOutputStructure (Class<?> tosmsgClass, ArrayList<DataField> fields, ArrayList<Method> getters, ArrayList<Method> setters) throws RuntimeException {
		if (logger.isDebugEnabled())
			logger.debug("Building output structure for class: " + tosmsgClass.getCanonicalName() + " and getter prefix: " + tinyosGetterPrefix+" and setter prefix: " + tinyosSetterPrefix);

		if (typesMapping == null) buildMappings() ;

		boolean tinyos1xMessageClassReached = tosmsgClass == net.tinyos1x.message.Message.class;
		boolean tinyos2xMessageClassReached = tosmsgClass == net.tinyos.message.Message.class;
		boolean tinyosMessageClassNotFound = tosmsgClass == Object.class;

		if (tinyosMessageClassNotFound) {
			fields = null;
			getters = null;
			setters = null;
			throw new RuntimeException ("Neither TinyOS1x (net.tinyos1x.message.Message) nor TinyOS2x (net.tinyos.message.Message) message class where found in the >" + tinyosMessageName + "< class hierarchy") ;
		} 
		else if (tinyos1xMessageClassReached || tinyos2xMessageClassReached) {
			this.outputStructure = fields.toArray(new DataField[] {});
			this.getters = getters;
			this.setters = setters;
		}
		else {

			Method[] methods = tosmsgClass.getDeclaredMethods();
			Arrays.sort(methods, orderMethodComparator);

			Method method = null;
			Class<?> type = null;
			String map_type = null;
			DataField nextField = null;
			for (int i = 0 ; i < methods.length ; i++) {

				method = methods[i];

				// select getters
				if (method.getName().startsWith(tinyosGetterPrefix)) {
					if ( isInMethodList(getters, method) ) {
						if (logger.isDebugEnabled())
							logger.debug("The method >" + method.getName() + "< is already defined in a subclass. This getter is skipped.");
					}
					else if (method.getName().compareToIgnoreCase(tinyosGetterPrefix + "TIMED") == 0) {
						if (logger.isDebugEnabled())
							logger.debug("next data field is the TIMED field");
						timedFieldGetter = method;
					}
					else {
						type = method.getReturnType();
						map_type = typesMapping.get(type) ;
						String name = method.getName();
						
						if (map_type == null) {
							logger.warn("Not managed type: >" + method.getReturnType() + "< for getter >" + name + "<. This getter is skipped.");
						}
						else {
							if(type.isArray()) {
					            int j=0;
					            int size=0;
					            Method getArraySize = null;
								try {
									getArraySize = tosmsgClass.getDeclaredMethod("numElements_" + name.substring(tinyosGetterPrefix.length()));
								} catch (NoSuchMethodException e) {
					            	logger.error(e.getMessage(), e);
								}
					            try {
					            	size = (Integer) getArraySize.invoke(tosmsgClass);
					            } catch (InvocationTargetException e) {
					            	logger.error("Invocation of numElements_" + name.substring(tinyosGetterPrefix.length()) + " failed: %s%n", e);
					            } catch (IllegalAccessException e) {
					            	logger.error(e.getMessage(), e);
								}
					            for(j=0; j < size; j++) {
									nextField = new DataField (name.substring(tinyosGetterPrefix.length()).toUpperCase() + j , map_type) ;
									if (logger.isDebugEnabled())
										logger.debug("next data field: " + nextField);
									fields.add(nextField);
					            }
				            }
							else {
								nextField = new DataField (name.substring(tinyosGetterPrefix.length()).toUpperCase() , map_type) ;
								if (logger.isDebugEnabled())
									logger.debug("next data field: " + nextField);
								fields.add(nextField);
							}
							getters.add(method);
						}
					}
				}
				// select setters
				else if (method.getName().startsWith(tinyosSetterPrefix)) {
					if (logger.isDebugEnabled())
						logger.debug("setter: " + method.getName());
					if ( isInMethodList(setters, method) ) {
						if (logger.isDebugEnabled())
							logger.debug("The method >" + method.getName() + "< is already defined in a subclass. This setter is skipped.");
					}
					else {
						setters.add(method);
					}
				}
			}
			buildOutputStructure (tosmsgClass.getSuperclass(), fields, getters, setters) ;
		}
	}
	
	private static boolean isInMethodList (ArrayList<Method> methods, Method amethod) {
		Iterator<Method> iter = methods.iterator();
		Method nextMethod = null;
		while (iter.hasNext()) {
			nextMethod = iter.next();
			if (equalMethodComparator.compare(nextMethod, amethod) == 0) return true;
		}
		return false;
	}

	private static void buildMappings () {
		typesMapping = new Hashtable<Class<?>, String> () ;
		typesMapping.put(byte.class, "TINYINT") ;
		typesMapping.put(short.class, "SMALLINT") ;
		typesMapping.put(int.class, "INTEGER") ;
		typesMapping.put(long.class, "BIGINT") ;
		typesMapping.put(float.class, "DOUBLE");
		typesMapping.put(double.class, "DOUBLE");
		typesMapping.put(byte[].class, "TINYINT") ;
		typesMapping.put(short[].class, "SMALLINT") ;
		typesMapping.put(int[].class, "INTEGER") ;
		typesMapping.put(long[].class, "BIGINT") ;
		typesMapping.put(float[].class, "DOUBLE");
		typesMapping.put(double[].class, "DOUBLE");
		typesMapping.put(Byte.class, "TINYINT") ;
		typesMapping.put(Short.class, "SMALLINT") ;
		typesMapping.put(Integer.class, "INTEGER") ;
		typesMapping.put(Long.class, "BIGINT") ;
		typesMapping.put(Float.class, "DOUBLE");
		typesMapping.put(Double.class, "DOUBLE");
		typesMapping.put(Byte[].class, "TINYINT") ;
		typesMapping.put(Short[].class, "SMALLINT") ;
		typesMapping.put(Integer[].class, "INTEGER") ;
		typesMapping.put(Long[].class, "BIGINT") ;
		typesMapping.put(Float[].class, "DOUBLE");
		typesMapping.put(Double[].class, "DOUBLE");
	}

	private void findTinyOSVersionFromClassHierarchy (Class<?> messageClass) {
		Class<?> currentMessageClass = messageClass;
		Class<?> messageSuperClass;
		boolean found = false;
		while ( ! found ) {
			messageSuperClass = currentMessageClass.getSuperclass();
			if (logger.isDebugEnabled())
				logger.debug("message super class: " + messageSuperClass.getCanonicalName()) ;
			if (messageSuperClass == Object.class) break;
			else if (messageSuperClass == net.tinyos1x.message.Message.class) {
				if (logger.isDebugEnabled())
					logger.debug("> TinyOS v1.x message") ;
				tinyosVersion = TINYOS_VERSION_1 ;
				found = true;
			}
			else if (messageSuperClass == net.tinyos.message.Message.class) {
				tinyosVersion = TINYOS_VERSION_2 ; 
				if (logger.isDebugEnabled())
					logger.debug("> TinyOS v2.x message") ;
				found = true;
			}
			currentMessageClass = messageSuperClass;
		}
		if (! found) throw new RuntimeException ("Neither TinyOS1x (net.tinyos1x.message.Message) nor TinyOS2x (net.tinyos.message.Message) message class where found in the >" + tinyosMessageName + "< class hierarchy") ;
	}

	public String getTinyosSource() {
		return tinyosSource;
	}

	public String getTinyosMessageName() {
		return tinyosMessageName;
	}

	public ArrayList<Method> getGetters() {
		return getters;
	}
	
	public ArrayList<Method> getSetters() {
		return setters;
	}

	public DataField[] getOutputStructure() {
		return outputStructure;
	}

	public String getTinyosGetterPrefix() {
		return tinyosGetterPrefix;
	}

	public String getTinyosSetterPrefix() {
		return tinyosSetterPrefix;
	}

	public byte getTinyosVersion() {
		return tinyosVersion;
	}

	public int getTinyOSMessageLength() {
		return tinyOSMessageLength;
	}

	public Method getTimedFieldGetter() {
		return timedFieldGetter;
	}
}
