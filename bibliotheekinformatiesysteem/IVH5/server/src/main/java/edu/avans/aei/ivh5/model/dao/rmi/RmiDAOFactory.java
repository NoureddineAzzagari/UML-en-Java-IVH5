/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.avans.aei.ivh5.model.dao.rmi;

import org.apache.log4j.Logger;

import edu.avans.aei.ivh5.model.dao.DAOFactory;
import edu.avans.aei.ivh5.model.dao.api.LoanDAOInf;
import edu.avans.aei.ivh5.model.dao.api.MemberDAOInf;
import edu.avans.aei.ivh5.model.dao.api.ReservationDAOInf;

/**
 * This factory provides the RMI implementations for the domain classes
 * in this system.
 *
 * @author Robin Schellius
 */
public class RmiDAOFactory extends DAOFactory {
    
	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(RmiDAOFactory.class);

	public RmiDAOFactory() {
		logger.debug("Factory constructed.");
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
}
