/*
 * 
 */
package library.main;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

import library.presentation.LibraryUI;
import nl.avans.aei.ivh5.library.api.RemoteMemberAdminManagerIF;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Example client class that retrieves a given RMI service from the registry and opens a GUI.
 * The RMI registry must be running and the server class must be available in order for this
 * client to work properly. <p> Hostname and servicename can be set via the command line. This enables the
 * running of multiple clients connecting to different servers or retrieving different services.</p>
 * 
 * @author Robin Schellius
 *
 */
public class LibraryClient {
	
	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(LibraryClient.class);
	// The host where the client will look for the registry. Can be set via command line option.
    static String hostname = "localhost";
    // The service to be retrieved from the registry. Can be set via command line option.
    static String servicename = "BibliotheekBreda";

    /**
     * 
     * @param args optional command line arguments.
     */
    public static void main(String[] args) {

		// Configure logging. 
		PropertyConfigurator.configure("./src/resources/clientlog.cnf");	     
		logger.debug("Starting application");

        if(args.length == 0) {
    		logger.debug("No command line options found; using defaults.");
        } else {
        	parseCommandLine(args);
        }
		logger.debug("Connecting to " + servicename + " on " + hostname);

        // System.setProperty("java.rmi.server.codebase", "file:/C:/dev/workspace/workspace/HelloServer/bin/-");
    	System.setProperty("java.rmi.server.codebase", "http://" + hostname + "/classes/");
    	
    	// System.setProperty("java.security.policy", "file:/C:/dev/workspace/workspace/HelloServer/bin/server.policy");
    	System.setProperty("java.security.policy", "http://" + hostname + "/classes/server.policy");
    	
    	if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
            logger.debug("SecurityManager installed");
        }
        
        try {
    		logger.debug("Locating registry on " + hostname);
            Registry registry = LocateRegistry.getRegistry(hostname);
            
            logger.info("Contents of registry: " + Arrays.toString(registry.list()));
            
            RemoteMemberAdminManagerIF stub = (RemoteMemberAdminManagerIF) registry.lookup(servicename);
    		logger.debug("Found '" + servicename + "' in registry");
            
            LibraryUI ui = new LibraryUI(stub);
    		ui.frmLibraryInformationSystem.setVisible(true);

        } 
		catch (java.security.AccessControlException e) {
			logger.error("Could not connect to registry: " + e.getMessage());			
		}
        catch (Exception e) {
            logger.error("Error: " + e.getMessage());
            logger.error("Make sure the registry is running by starting rmiregistry.");
        }        
    }
    
    /**
     * Read the command line options and, if they match the requirements, set the 
     * corresponding variables to their correct values. 
     * <p>Options: -hostname [hostname or IP address] -servicename [servicename]</p>
     * 
     * @param args The string of options given to this application via the command line.
     */
    private static void parseCommandLine(String[] args) {
    	boolean errorFound = false;
		if(args.length != 4) {
			logger.debug("Skipping command line options; expected 4 but found " + args.length);			
			errorFound = true;
		} else {
			if(args[0].equalsIgnoreCase("-hostname")) 
				hostname = args[1];
			else errorFound = true;
			if(args[2].equalsIgnoreCase("-servicename")) 
				servicename = args[3];
			else errorFound = true;
		}
		if(errorFound) {
			logger.debug("Use: -hostname [hostname or IP address] -servicename [servicename]");			
			logger.debug("     -hostname 145.48.6.147 -servicename BibliotheekBreda");			
		}

    }
}