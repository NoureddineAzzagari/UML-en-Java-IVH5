/*
 * Copyright (c) 2004, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * -Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 * Neither the name of Oracle nor the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR
 * ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that Software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
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
 * 
 * @author Robin Schellius
 *
 */
public class LibraryClient {
	
	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(LibraryClient.class);

    private LibraryClient() {
		logger.debug("Constructor");
    }

    public static void main(String[] args) {

        String remotehost = "localhost"; 
        // String remotehost = "145.48.6.147";
        String servicename = "BibliotheekBreda";

		// Configure logging. 
		PropertyConfigurator.configure("./src/resources/clientlog.cnf");
	     
		logger.debug("Starting ...");

        // System.setProperty("java.rmi.server.codebase", "file:/C:/dev/workspace/workspace/HelloServer/bin/-");
    	System.setProperty("java.rmi.server.codebase", "http://" + remotehost + "/classes/");
    	
    	// System.setProperty("java.security.policy", "file:/C:/dev/workspace/workspace/HelloServer/bin/server.policy");
    	System.setProperty("java.security.policy", "http://" + remotehost + "/classes/server.policy");
    	
    	if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
            logger.debug("SecurityManager installed");
        }
        
        try {
    		logger.debug("Locating registry on " + remotehost);
            Registry registry = LocateRegistry.getRegistry(remotehost);
            
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
        }
        
    }
}