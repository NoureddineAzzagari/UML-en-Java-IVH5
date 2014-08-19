/*
 */
package edu.avans.aei.ivh5.model.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import edu.avans.aei.ivh5.api.RemoteMemberAdminManagerIF;

/**
 * Creates the stub, which will be remote accessible by the client, and
 * registers it at the rmiregistry by a servicename. Clients can retrieve the
 * remote stub by this servicename.
 * 
 * @author Robin Schellius
 */
public class LibraryServer {

	// The following variables are initialized by reading properties from the property file.
	
	static private String hostname;				// Host/IP-address of the RMI registry
	static private String servicename;			// Name identifying this server in the RMI registry
	static private String logconfigfile;		// Logging configuration
	static public String daofactoryclassname;	// Implements the specific DAO functionality (MySQL, XML).
	static public String rmifactoryclassname;	// Implements the remote access DAO functionality.
	static private RemoteMemberAdminManagerIF stub;	// 

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(LibraryServer.class);

	/**
	 * Empty constructor
	 * 
	 * @throws RemoteException
	 */
	public LibraryServer() throws RemoteException {
		logger.debug("Constructor");
	}

	/**
	 * Initialize the server, register it in the RMI registry, and
	 * (automatically) start listening for incoming client calls.
	 * 
	 * @param args
	 *            Command line arguments indicating the servicename for this
	 *            server.
	 */
	public static void main(String args[]) {

		if (args.length == 2) {
			String propertiesfile = parseCommandLine(args);
			loadProperties(propertiesfile);
		} else {
			System.out.println("No properties file was found. Provide a properties file name on the command line.");
			System.out.println("Program is exiting.");
			return;
		}

		// Configure logging using the given log config file.
		PropertyConfigurator.configure(logconfigfile);
		logger.info("Starting application");

		// These properties must have been set correctly at this point. Just checking. 
		logger.debug("java.rmi.server.codebase = " + System.getProperty("java.rmi.server.codebase", "invalid!"));
		logger.debug("java.security.policy = " + System.getProperty("java.security.policy", "invalid!"));

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
			logger.debug("SecurityManager installed");
		}

		try {
			// ShutdownHook handles cleaning up the registry when this
			// application exits.
			ShutdownHook shutdownHook = new ShutdownHook();
			Runtime.getRuntime().addShutdownHook(shutdownHook);

			logger.debug("Creating stub");
			MemberAdminManagerImpl obj = new MemberAdminManagerImpl(servicename);
			stub = (RemoteMemberAdminManagerIF) UnicastRemoteObject.exportObject(obj, 0);

			logger.debug("Locating registry on '" + hostname + "'");
			Registry registry = LocateRegistry.getRegistry(hostname);
			logger.debug("Registering stub using name \"" + servicename + "\"");
			registry.rebind(servicename, stub);

			logger.info("Server ready");
		} catch (java.rmi.ConnectException e) {
			logger.fatal("Could not connect: " + e.getMessage());
		} catch (java.security.AccessControlException e) {
			logger.fatal("No access: " + e.getMessage());
			logger.fatal("(are the webserver and rmiregistry running?)");
		} catch (Exception e) {
			logger.fatal(e.toString());
		}
	}

	/**
	 * When the server is stopped, by Ctrl-C or closing the window that it is
	 * running in, we want to unregister ourselves from the registry and
	 * decouple the stub, so that clients cannot find the remote stub without a
	 * running server.
	 * 
	 * @throws RemoteException
	 */
	public static void exit() throws RemoteException {
		logger.info("Server is exiting, cleaning up registry.");
		try {
			logger.debug("Unbind servicename " + servicename);
			Naming.unbind(servicename);
		} catch (java.net.MalformedURLException e) {
			logger.error("Servicename not found in registry.");
		} catch (java.rmi.NoSuchObjectException e) {
			logger.error("Server not found in registry.");
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {
			logger.info("Bye.");
		}
	}

	/**
	 * Read the command line and parse the name of the properties file. The properties file
	 * contains all required properties for running this application.
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
			System.out.println("     -properties \"http://localhost/path/to/file.props\"");
			System.out.println("     -properties \"file:/C:/path/to/server.cnf\"");
		}
		return propertiesfilename;
	}

	/**
	 * Read the required system properties from the given properties file.
	 * 
	 * @param propertiesFileName The name of the file containing the system properties.
	 */
	private static void loadProperties(String propertiesFileName) {

		Properties props = new Properties(System.getProperties());
		InputStream inputFile = null;

		if (propertiesFileName != null) {
			try {
				inputFile = new FileInputStream(propertiesFileName);
				if(inputFile != null ) {
					props.load(inputFile);
					
					hostname = props.getProperty("hostname", "localhost");
					servicename = props.getProperty("servicename", "BibliotheekBreda");
					// policyfile = props.getProperty("policyfile", "server.policy");
					daofactoryclassname = props.getProperty("daoclassname", "library.datastorage.daofactory.xml.dom.XmlDOMDAOFactory");
					rmifactoryclassname = props.getProperty("rmiclassname", "library.datastorage.daofactory.xml.dom.XmlDOMDAOFactory");
					logconfigfile = props.getProperty("logconfigfile", "server.cnf");
					
					System.setProperty("java.rmi.server.codebase", props.getProperty("java.rmi.server.codebase"));
					System.setProperty("java.security.policy", props.getProperty("java.security.policy"));
					
					props.getProperty("mysql.username", "root");
					props.getProperty("mysql.password", "");
					props.getProperty("mysql.hostname", "localhost");
					props.getProperty("mysql.dbname", "library");
					
					System.setProperties(props);
				}
			} catch (IOException e) {
				System.out.println("Error reading file: " + e.getMessage());
			} finally {
				if (inputFile != null) {
					try {
						inputFile.close();
					} catch (IOException e) {
						System.out.println("Error closing file: " + e.getMessage());
					}
				}
			}
		}
	}
}

/**
 * ShutdownHook is a way to handle application cleanup in case the process is
 * stopped by an external event, such as the user stopping the program.
 * 
 * @see http
 *      ://docs.oracle.com/javase/7/docs/api/java/lang/Runtime.html#addShutdownHook
 *      (java.lang.Thread)
 * @see http://www.onjava.com/pub/a/onjava/2003/03/26/shutdownhook.html
 * 
 * @author Robin Schellius
 */
class ShutdownHook extends Thread {

	public void run() {
		try {
			LibraryServer.exit();
		} catch (RemoteException e) {
			System.out.println("Error exiting: " + e.getMessage());
		}
	}
}