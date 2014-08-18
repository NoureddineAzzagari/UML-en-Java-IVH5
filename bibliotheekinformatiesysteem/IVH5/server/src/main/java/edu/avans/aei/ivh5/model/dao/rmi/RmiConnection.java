/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.avans.aei.ivh5.model.dao.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

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

    /**
     * Creates a RMI connection to the given service on the given hostname.
     * 
     * @param host Hostname or IP-address of remote server.
     * @param service The name of the service as it is registered in the registry.
     */
    public static void connectToService(String host, String service) {

		logger.debug("Connect to service " + service + " on host "  + host);

		// We need to save hostname and servicename for later lookup.
		if(host != null) hostname = host;
		if(service != null) servicename = service;
		
    	System.setProperty("java.rmi.server.codebase", "http://" + host + "/classes/");
    	System.setProperty("java.security.policy", "http://" + host + "/classes/server.policy");
    	
    	if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
            logger.debug("SecurityManager installed");
        }
        
        try {
    		logger.debug("Locating registry on " + host);
            Registry registry = LocateRegistry.getRegistry(host);
            
            logger.info("Looking up \"" + service + "\" in registry.");
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
    
    /**
     * 
     */
    public static RemoteMemberAdminManagerIF getRemoteService() {
    	return remoteService;
    }
}
