/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.avans.aei.ivh5.util;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * Handles the connection to a remote service via Java Remote Method Invocation (RMI).
 * 
 * @author Robin Schellius
 */
public class RmiConnection {

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(RmiConnection.class);
	
	static Object remoteService = null;
	public static String hostname = null;
	public static String servicename = null;
	
	// A list of available registries, possibly on other remote hosts.
	private static HashMap<String, Registry> registries = 
			new HashMap<String, Registry>();
	
	/**
	 * 
	 * @param hostname
	 * @return
	 */
	private static Registry getRegistry(String hostname) {
		
		logger.debug("getRegistry on " + hostname);
		
		try {
			if (registries.containsKey(hostname) && (registries.get(hostname) != null)) {
				logger.debug("Found registry in cache.");
			} else {
				Registry registry = LocateRegistry.getRegistry(hostname);
				logger.debug("Found registry, storing it in cache.");
				registries.put(hostname, registry);
			}
		} catch (java.security.AccessControlException e) {
			logger.fatal("No access: " + e.getMessage());
		} catch (Exception e) {
			logger.fatal("Exception: " + e.getMessage());
			// e.printStackTrace();
		}
		return registries.get(hostname);
	}
	
    /**
     * Creates a RMI connection to the given service on the given hostname.
     * 
     * @param host Hostname or IP-address of remote server.
     * @param service The name of the service as it is registered in the registry.
     */
    public static Object getService(String host, String service) {

		logger.debug("Connecting to " + service + " on host "  + host);
		
		Object remoteService = null;

        try {
            Registry registry = getRegistry(host);
            remoteService = (Object) registry.lookup(service);
        } 
		catch (java.security.AccessControlException e) {
			logger.error("Could not connect to registry: " + e.getMessage());			
		}
        catch (java.rmi.NotBoundException e) {
            logger.error("Service " + service + " not found!");
        }
        catch (Exception e) {
            logger.error("Error: " + e.getMessage());
            logger.error("(Are the webserver, the registry and the server running?)");
        }

        return remoteService;
    }	
    

    public static HashMap<String, String> getAvailableServices(String hostname) {
    	
        String[] serviceNames;

        Thread t = new Thread(new RmiConnection.listServices("DUMMY DUMMY!"));
        t.start();

        serviceNames = new String[]{"Dummy!"};
        
		logger.debug("Contents of registry: " + Arrays.toString(serviceNames));
		return null;

    }

    
    /**
     * 
     */
//     public static RemoteMemberAdminManagerIF getRemoteService() {
//    public static Object getRemoteService() {
//    	return remoteService;
//    }
    
    /**
     * 
     * @author Robin Schellius
     *
     */
    public static class listServices implements Runnable {

    	
    	public listServices(String hostname) {
    		
    	}
    	
	    public void run() {
	        String importantInfo[] = {
	            "Mares eat oats",
	            "Does eat oats",
	            "Little lambs eat ivy",
	            "A kid will eat ivy too"
	        };
	        try {
	            for (int i = 0;
	                 i < importantInfo.length;
	                 i++) {
	                // Pause for 4 seconds
	                Thread.sleep(4000);
	                // Print a message
	                // threadMessage(importantInfo[i]);
	            }
	        } catch (InterruptedException e) {
	            // threadMessage("I wasn't done!");
	        }
	    }
	}

}
