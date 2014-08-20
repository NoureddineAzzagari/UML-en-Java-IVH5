/*
 * 
 */
package edu.avans.aei.ivh5.view.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import edu.avans.aei.ivh5.api.RemoteMemberAdminManagerIF;
import edu.avans.aei.ivh5.control.Controller;
import edu.avans.aei.ivh5.util.Settings;
import edu.avans.aei.ivh5.view.ui.UserInterface;

/**
 * <p>
 * Client that connects to the given RMI service from the registry and opens a
 * GUI. The RMI registry must be running and the server class must be available
 * in order for this client to work properly.
 * </p>
 * <p>
 * This application requires the properties that were specified in the
 * accompanying properties file. Adjust the properties in the file to change the
 * settings and behavior of this client.
 * </p>
 * 
 * @author Robin Schellius
 */
public class LibraryClient {

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(LibraryClient.class);

//	// The name of our server in the RMI registry.
//	private static String servicename;
//	// Category combining services that offer identical services.
//	private static String servicegroup;
//	// Name of the logging configuration file.
//	private static String logconfigfile;

	/**
	 * Constructor.
	 * 
	 * @param hostname The host where the server is running.
	 */
	public LibraryClient(String hostname) {

		// Initial message, will change once we're connected.
		String status = "Could not connect to " + hostname;
		String service = null;
		RemoteMemberAdminManagerIF manager = null;

		// Try to retrieve the server connection. 
		try {
			logger.debug("Locating registry on " + hostname);
			Registry registry = LocateRegistry.getRegistry(hostname);

			// Service is a string like "Library/Breda". This enables us
			// to find all Libraries, and skip all other services.
			service = Settings.props.getProperty(Settings.propRmiServiceGroup) +
					Settings.props.getProperty(Settings.propRmiServiceName);
			
			logger.debug("Looking up \"" + service + "\" in registry.");
			manager = (RemoteMemberAdminManagerIF) registry.lookup(service);
			status = "Connected to " + service + " on host " + hostname;

		} catch (java.security.AccessControlException e) {
			logger.fatal("No access: " + e.getMessage());
		} catch (java.rmi.NotBoundException e) {
			logger.fatal("Servicename \"" + service + "\" not found.");
			logger.fatal("(Are the webserver, the registry and the server running?)");
		} catch (Exception e) {
			logger.fatal("Exception: " + e.getMessage());
			// e.printStackTrace();
		}

		// Build the User interface.
		UserInterface ui = new UserInterface();
		Controller controller = new Controller(ui, manager);
		ui.setController(controller);
		ui.setStatusText(status);
		ui.applicationFrame.setVisible(true);		
	}

	/**
	 * Main method setting up this client application.
	 * 
	 * @param args
	 *            Command line argument identifying the property file. <br/>
	 *            Format: -properties [filename].
	 */
	public static void main(String[] args) {

		// Get the properties file name from the command line, and load the
		// properties.
		if (args.length == 2) {
			String propertiesfile = parseCommandLine(args);
			Settings.loadProperties(propertiesfile);
		} else {
			System.out.println("No properties file was found. Provide a properties file name.");
			System.out.println("Program is exiting.");
			return;
		}
		
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		// Configure logging using the given log config file.
		PropertyConfigurator.configure(Settings.props.getProperty(Settings.propLogConfigFile));
		logger.info("Starting application");

		// Create the client. We do not need to reference it later, so a
		// variable name is not necessary.
		new LibraryClient(System.getProperty(Settings.propRmiHostName));
	}

	/**
	 * Read the command line and parse the name of the properties file. The
	 * properties file contains all required properties for running this
	 * application.
	 * <p>
	 * Options: -properties [filename]
	 * </p>
	 * 
	 * @param args
	 *            The string of options given to this application via the
	 *            command line.
	 */
	private static String parseCommandLine(String[] args) {
		boolean errorFound = false;
		String propertiesfilename = null;

		if (args.length != 2) {
			System.out.println("Error reading options; expected 2 but found "
					+ args.length + ".");
			errorFound = true;
		} else {
			if (args[0].equalsIgnoreCase("-properties")) {
				propertiesfilename = args[1];
			} else
				errorFound = true;
		}
		if (errorFound) {
			System.out.println("Use: -properties [filename or URL]");
			System.out.println("     -properties client.properties");
		}
		return propertiesfilename;
	}

//	/**
//	 * <p>Load the properties for this application from the given properties file.
//	 * The properties are used to initialize variables that are important in the
//	 * correct initialization of the application.</p>
//	 * 
//	 * <p>
//	 * The properties file allows the use of placeholders. A placeholder is a property
//	 * between double brackets ({{ and }}). Method loadProperties replaces these placeholders
//	 * with their correct value. This is not standard functionality and must be 
//	 * implemented per use.
//	 * </p>
//	 * 
//	 * @param propertiesFileName
//	 *            The name of the file containing the system properties.
//	 */
//	private static void loadProperties(String propertiesFileName) {
//
//		/**
//		 *  Define constants for property values, for ease 
//		 *  of use and adaptability.
//		 */
//		final String propRmiServiceName = "service.servicename";
//		final String propRmiServiceGroup = "service.servicegroup";
//		final String propLogConfigFile = "logconfigfile";
//		final String propRmiHostName = "java.rmi.server.hostname";
//		final String propRmiCodebase = "java.rmi.server.codebase";
//		final String propSecurityPolicy = "java.security.policy";
//		
//		Properties props = new Properties();
//		InputStream inputFile = null;
//
//		try {
//			inputFile = new FileInputStream(propertiesFileName);
//			if (inputFile != null) {
//				props.load(inputFile);
//
//				/**
//				 * Read all properties, and save important ones in the System properties.
//				 * Potential {{placeholders}} are replaced with the full value.
//				 */
//
//				// The name of our 'paired' service.
//				servicename = props.getProperty(propRmiServiceName);
//
//				/**
//				 *  The category identifies the subsection of services that	
//				 *  we can connect to. Services outside this category are 
//				 *  automatically not found.
//				 */
//				servicegroup = props.getProperty(propRmiServiceGroup);
//				// File containing settings for the Log4J plugin.
//				logconfigfile = props.getProperty(propLogConfigFile);
//
//				/**
//				 * Replace placeholders.
//				 */
//				String rmiHostName = props.getProperty(propRmiHostName);
//				
//				String rmiCodeBase = props.getProperty(propRmiCodebase)
//						.replace("{{"+propRmiHostName+"}}", rmiHostName);
//				String rmiSecurity = props.getProperty(propSecurityPolicy)		
//						.replace("{{"+propRmiHostName+"}}", rmiHostName);
//				
//				System.out.println(propRmiHostName + " = " + rmiHostName);
//				System.out.println(propRmiCodebase + " = " + rmiCodeBase);
//				System.out.println(propSecurityPolicy + " = " + rmiSecurity);
//				
//				// Reset in the System properties.
//				System.setProperty("java.rmi.server.hostname", rmiHostName);
//				System.setProperty("java.rmi.server.codebase", rmiCodeBase);
//				System.setProperty("java.security.policy", rmiSecurity);
//
//				// Adding to the set of system properties enables global use
//				// through the application.
//				System.setProperties(props);
//			}
//		} catch (IOException e) {
//			System.out.println("Error reading file: " + e.getMessage());
//		} catch (Error e) {
//			System.out.println(e.getMessage());
//		} finally {
//			if (inputFile != null) {
//				try {
//					inputFile.close();
//				} catch (IOException e) {
//					System.out.println("Error closing file: "
//							+ e.getMessage());
//				}
//			}
//		}
//	}
//
}