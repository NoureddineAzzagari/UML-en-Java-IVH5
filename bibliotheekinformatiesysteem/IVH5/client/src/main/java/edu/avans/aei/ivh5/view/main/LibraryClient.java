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

	// The hostname where our serverside is running.
	private static String hostname;
	// The name of our server in the RMI registry.
	private static String servicename;
	// Category combining services that offer identical services.
	private static String servicegroup;
	// Array of possible remote hosts to connect to.
	private static String[] remotehosts;
	// Name of the logging configuration file.
	private static String logconfigfile;

	public LibraryClient() {

		// Retrieve the server connection and create the GUI.
		try {
			logger.debug("Locating registry on " + hostname);
			Registry registry = LocateRegistry.getRegistry(hostname);

			String[] serviceNames = registry.list();
			logger.debug("Contents of registry: "
					+ Arrays.toString(serviceNames));

			logger.debug("Looking up \"" + servicegroup + servicename + "\" in registry.");
			RemoteMemberAdminManagerIF stub = (RemoteMemberAdminManagerIF) registry
					.lookup(servicegroup + servicename);
			logger.debug("Connected to remote server stub.");

			UserInterface ui = new UserInterface(stub, hostname, serviceNames);
			ui.applicationFrame.setVisible(true);
		} catch (java.security.AccessControlException e) {
			logger.fatal("No access: " + e.getMessage());
			logger.fatal("(There's something wrong with the security policy ...)");
		} catch (java.rmi.NotBoundException e) {
			logger.fatal("Servicename \"" + servicegroup + servicename + "\" not found.");
			logger.fatal("(Are the webserver, the registry and the server running?)");
		} catch (Exception e) {
			logger.fatal("Exception: " + e.getMessage());
			e.printStackTrace();
		}
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
			loadProperties(propertiesfile);
		} else {
			System.out
					.println("No properties file was found. Provide a properties file name on the command line.");
			System.out.println("Program is exiting.");
			return;
		}

		// Configure logging using the given log config file.
		PropertyConfigurator.configure(logconfigfile);
		logger.info("Starting application");

		// These properties must have been set correctly at this point. Just
		// checking.
		logger.debug("java.rmi.server.codebase = "
				+ System.getProperty("java.rmi.server.codebase", "invalid!"));
		logger.debug("java.security.policy = "
				+ System.getProperty("java.security.policy", "invalid!"));

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
			logger.debug("SecurityManager installed");
		}

		LibraryClient client = new LibraryClient();
		
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

	/**
	 * Load the properties for this application from the given properties file.
	 * The properties are used to initialize variables that are important in the
	 * correct initialization of the application.
	 * 
	 * @param propertiesFileName
	 *            The name of the file containing the system properties.
	 */
	private static void loadProperties(String propertiesFileName) {

		Properties props = new Properties();
		InputStream inputFile = null;

		if (propertiesFileName != null) {
			try {
				inputFile = new FileInputStream(propertiesFileName);
				if (inputFile != null) {
					props.load(inputFile);

					// The client is 'paired' with a server-part. This hostname
					// indicates
					// the name of the host where the serverside can be
					// retrieved.
					hostname = props.getProperty("hosts.hostname", "undefined");
					// The name of our 'paired' service.
					String remote_hosts = props.getProperty(
							"hosts.remotehosts", "undefined");
					remotehosts = remote_hosts.split(",");
					// The name of our 'paired' service.
					servicename = props.getProperty("service.servicename",
							"undefined");
					// The category identifies the subsection of services that
					// we can connect to.
					// Services outside this category are left out.
					servicegroup = props.getProperty("service.servicegroup",
							"undefined");
					// File containing settings for the Log4J plugin.
					logconfigfile = props.getProperty("logconfigfile",
							"undefined");

					// Read the security properties, and set them in the System
					// properties.
					System.setProperty("java.rmi.server.codebase",
							props.getProperty("java.rmi.server.codebase"));
					System.setProperty("java.security.policy",
							props.getProperty("java.security.policy"));
				}
			} catch (IOException e) {
				System.out.println("Error reading file: " + e.getMessage());
			} finally {
				if (inputFile != null) {
					try {
						inputFile.close();
					} catch (IOException e) {
						System.out.println("Error closing file: "
								+ e.getMessage());
					}
				}
			}
		}
	}

}