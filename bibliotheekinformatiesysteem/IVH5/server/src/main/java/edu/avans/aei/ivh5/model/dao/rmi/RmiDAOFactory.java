/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.avans.aei.ivh5.model.dao.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import edu.avans.aei.ivh5.api.RemoteMemberInfo;
import edu.avans.aei.ivh5.model.dao.DAOFactory;
import edu.avans.aei.ivh5.model.dao.api.LoanDAOInf;
import edu.avans.aei.ivh5.model.dao.api.MemberDAOInf;
import edu.avans.aei.ivh5.model.dao.api.ReservationDAOInf;
import edu.avans.aei.ivh5.util.Settings;

/**
 * This factory provides the RMI implementations for the domain classes
 * in this system.
 *
 * @author Robin Schellius
 */
public class RmiDAOFactory extends DAOFactory {
    
	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(RmiDAOFactory.class);

	// Caching list of available services.
	// This list may not be accurate, since we cannot be shure that a service that is
	// found at some point, is acutally available at a later time, since the service
	// could be stopped or the host could be unreachable.
	ArrayList<String> availableServices;

	/**
	 * 
	 */
	public RmiDAOFactory() {
		logger.debug("Constructor");
		
		// Initialize list of available services
		availableServices = new ArrayList<String>();
    }
    
	public MemberDAOInf getMemberDAO() {
		return new RmiMemberDAO();
	}
	
	public LoanDAOInf getLoanDAO() {
		return new RmiLoanDAO();
	}
	
	public ReservationDAOInf getReservationDAO() {
		return new RmiReservationDAO();
	}
	
	/**
	 * Find all available services that can be contacted from this service. This includes
	 * all services that are listed in the RMI registry on the host that this service is 
	 * running on, and could include more remote registries and services if available.
	 * 
	 * @return
	 */
	public ArrayList<String> findAvailableServices() {
		
		logger.debug("findAvailableServices");

		String hostname = System.getProperty(Settings.propRmiHostName);
		// Make sure we start with a empty list.
		availableServices = new ArrayList<String>();

    	try {
			String[] list = RmiConnection.getRegistry(hostname).list();

			if(list != null) {
				for(String servicename: list) {
					availableServices.add(servicename);
				}
				logger.debug("Found " + availableServices.size() + " services: " + availableServices.toString());
	    	}
		} catch (RemoteException e) {
			logger.error("Exception: " + e.getMessage());
		}
    	
		return availableServices;
	}


}
