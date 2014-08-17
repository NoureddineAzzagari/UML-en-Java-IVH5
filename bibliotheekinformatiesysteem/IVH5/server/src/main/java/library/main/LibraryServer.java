/*
 */
package library.main;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import library.businesslogic.MemberAdminManagerImpl;
import nl.avans.aei.ivh5.library.api.RemoteMemberAdminManagerIF;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Creates the stub, which will be remote accessible by the client, and registers
 * it at the rmiregistry by a servicename. Clients can retrieve the remote stub by this 
 * servicename.
 * 
 * @author Robin Schellius
 */
public class LibraryServer {

	final static private String hostname = "localhost"; // "145.48.6.147";
	final static private String logconfigfile = "serverlog.cnf";
	static private String servicename = "BibliotheekBreda";
	static private RemoteMemberAdminManagerIF stub;

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
	 * Initialize the server, register it in the RMI registry, and (automatically) 
	 * start listening for incoming client calls. 
	 * 
	 * @param args Command line arguments indicating the servicename for this server. 
	 */
	public static void main(String args[]) {

		// Configure logging. 
		PropertyConfigurator.configure(logconfigfile);
	     
		logger.debug("Starting application");

        if(args.length == 0) {
    		logger.debug("No command line options found; using defaults.");
        } else {
        	parseCommandLine(args);
        }

		// System.setProperty("java.rmi.server.codebase", "file:/C:/dev/workspace/workspace/HelloServer/bin/-");
		// System.setProperty("java.rmi.server.codebase", "file:/C:/xampp/htdocs/classes/-");
		System.setProperty("java.rmi.server.codebase", "http://" + hostname + "/classes/");

		// System.setProperty("java.security.policy", "file:/C:/dev/workspace/workspace/HelloServer/bin/server.policy");
		// System.setProperty("java.security.policy", "file:/C:/xampp/htdocs/classes/server.policy");
		System.setProperty("java.security.policy", "http://" + hostname + "/classes/server.policy");

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
            logger.debug("SecurityManager installed");
		}

		// ShutdownHook handles cleaning up the registry when this application exits.
		ShutdownHook shutdownHook = new ShutdownHook();
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        
		try {
			logger.debug("Creating stub");
			MemberAdminManagerImpl obj = new MemberAdminManagerImpl(servicename);
			stub = (RemoteMemberAdminManagerIF) UnicastRemoteObject.exportObject(obj, 0);

			logger.debug("Locating registry on host '" + hostname + "'");
			Registry registry = LocateRegistry.getRegistry(hostname);
			logger.debug("Registering stub using name \"" + servicename + "\"");
			registry.rebind(servicename, stub);

			logger.info("Server ready");
		}
		catch (java.rmi.ConnectException e) {
			logger.error("Could not connect: " + e.getMessage());			
		}
		catch (java.security.AccessControlException e) {
			logger.error("Could not access registry: " + e.getMessage());			
			logger.error("(are the webserver and rmiregistry running?)");
		}
		catch (Exception e) {
			logger.error("Server exception: " + e.toString());
		}
	}
	
	/**
	 * When the server is shutdown, mostly by closing the window that it is running in, we
	 * want to unregister ourselves from the registry and decouple the stub, so that clients 
	 * cannot find the remote stub without a running server.
	 * 
	 * @throws RemoteException
	 */
	public static void exit() throws RemoteException
	{
		logger.debug("Server is exiting, cleaning up registry ...");
	    try{
			logger.debug("Unbind servicename " + servicename);
	        Naming.unbind(servicename);

			logger.debug("Unexport the server and decouple from the RMI runtime");
	        UnicastRemoteObject.unexportObject(stub, true);

			logger.info("Server is exiting.");
	    }
	    catch(java.net.MalformedURLException e) {
			logger.error("Servicename not found in registry.");	    	
	    }
	    catch( java.rmi.NoSuchObjectException e) {
			logger.error("Server not found in registry.");
	    }
	    catch(Exception e) {
			logger.error(e.toString());
	    }
	}
	
	/**
     * Read the command line options and, if they match the requirements, set the 
     * corresponding variables to their correct values. 
     * <p>Options: -servicename [servicename]</p>
     * 
     * @param args The string of options given to this application via the command line.
     */
    private static void parseCommandLine(String[] args) {
    	boolean errorFound = false;
		if(args.length != 2) {
			logger.debug("Skipping command line options; expected 2 options but found " + args.length + ".");			
			errorFound = true;
		} else {
			if(args[0].equalsIgnoreCase("-servicename")) {
				servicename = args[1];
				servicename = servicename.replaceAll(" ", "%20");
			}
			else errorFound = true;
		}
		if(errorFound) {
			logger.debug("Use: -servicename [servicename]");			
			logger.debug("     -servicename \"Bibliotheek Breda\"");			
		}
    }
}

/**
 * ShutdownHook is a way to handle application cleanup in case the process is stopped
 * by an external event, such as the user stopping the program.
 * 
 * @see http://docs.oracle.com/javase/7/docs/api/java/lang/Runtime.html#addShutdownHook(java.lang.Thread)
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