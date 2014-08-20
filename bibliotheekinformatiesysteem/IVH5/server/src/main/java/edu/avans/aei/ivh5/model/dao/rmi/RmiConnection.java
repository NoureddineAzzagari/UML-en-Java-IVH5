/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.avans.aei.ivh5.model.dao.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.log4j.Logger;

import edu.avans.aei.ivh5.api.RemoteMemberAdminManagerIF;

/**
 * Handles the connection to a remote service via Java Remote Method Invocation (RMI).
 * 
 * @author Robin Schellius
 */
public class RmiConnection {

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(RmiConnection.class);
	
	static RemoteMemberAdminManagerIF remoteService = null;
	static String hostname = null;
	static String servicename = null;
	
	// A list of available registries, possibly on other remote hosts.
	private static HashMap<String, Registry> registries = new HashMap<String, Registry>();
	
	/**
	 * 
	 * @param hostname
	 * @return
	 */
	public static Registry getRegistry(String hostname) {
		
		logger.debug("getRegistry on " + hostname);
		
		try {
			if (!registries.containsKey(hostname) || (registries.get(hostname) == null)) 
			{
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
    public static void connectToService(String host, String service) {

		logger.debug("Connect to service " + service + " on host "  + host);

		// We need to save hostname and servicename for later lookup.
//		if(host != null) hostname = host;
//		if(service != null) servicename = service;
		
//    	if (System.getSecurityManager() == null) {
//            System.setSecurityManager(new SecurityManager());
//            logger.debug("SecurityManager installed");
//        }
        
        try {
            Registry registry = registries.get(host);
            remoteService = (RemoteMemberAdminManagerIF) registry.lookup(service);
    		logger.debug("Connected to remote server stub.");
        } 
		catch (java.security.AccessControlException e) {
			logger.error("Could not connect to registry: " + e.getMessage());			
		}
        catch (java.rmi.NotBoundException e) {
            logger.error("Servicename \"" + service + "\" not found.");
        }
        catch (Exception e) {
            logger.error("Error: " + e.getMessage());
            logger.error("(Are the webserver, the registry and the server running?)");
        }
    }	
    
    /*
    public static HashMap<String, String> getAvailableServices() {
    	
		String[] serviceNames = registry.list();
		logger.debug("Contents of registry: "
				+ Arrays.toString(serviceNames));

    }
    */
    
    /**
     * 
     */
    public static RemoteMemberAdminManagerIF getRemoteService() {
    	return remoteService;
    }
}
