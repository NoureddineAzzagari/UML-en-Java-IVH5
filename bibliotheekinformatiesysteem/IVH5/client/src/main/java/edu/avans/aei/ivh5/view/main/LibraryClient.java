/*
 * 
 */
package edu.avans.aei.ivh5.view.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import edu.avans.aei.ivh5.api.RemoteMemberAdminManagerIF;
import edu.avans.aei.ivh5.control.Controller;
import edu.avans.aei.ivh5.util.ManagerNotFoundException;
import edu.avans.aei.ivh5.util.RmiConnection;
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

	/**
	 * Constructor.
	 * 
	 * @param hostname The host where the server is running.
	 */
	public LibraryClient(String hostname) {

		logger.debug("Constructor using " + hostname);

		String status;
		String service = Settings.props.getProperty(
				Settings.propRmiServiceGroup) + Settings.props.getProperty(Settings.propRmiServiceName);
		RemoteMemberAdminManagerIF manager = null;

		logger.debug("Connecting to " + service + " on host " + hostname);
		manager = (RemoteMemberAdminManagerIF) RmiConnection.getService(hostname, service);

		if(manager == null) {
			status = "Not connected";
			logger.debug("Manager not found: " + status);
		} else {
			status = "Connected to " + service + " on host " + hostname;
		}

		/**
		 *  Build the User interface. Note that, since the Controller handles all UI events, it
		 *  must be constructed and available before the UI gets created.
		 */
		Controller controller = new Controller(manager);
		UserInterface ui = new UserInterface(controller);
		ui.setStatusText(status);
		ui.frame.setVisible(true);
		
//		try {
//			HashMap<String, String> listOfServices = manager.findAvailableServices();
//			
//			if(listOfServices != null) {
//				logger.debug("Available services: " + listOfServices.toString());
//				for(Entry<String, String> entry : listOfServices.entrySet()) {
//				    String key = entry.getKey();
//				    String value = entry.getValue();
//	
//				    // do what you have to do here
//				}
//			}
//		} catch (RemoteException e) {
//			logger.fatal("RemoteException: " + e.getMessage());
//		} catch (NullPointerException e) {
//			logger.fatal("NullPointerException: " + e.getMessage());
//			// e.printStackTrace();
//		} catch (Exception e) {
//			logger.fatal("Exception: " + e.getMessage());
//			// e.printStackTrace();
//		}
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

		// Create the client.
		String service = System.getProperty(Settings.propRmiHostName);
		LibraryClient client = new LibraryClient(service);
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
	
}