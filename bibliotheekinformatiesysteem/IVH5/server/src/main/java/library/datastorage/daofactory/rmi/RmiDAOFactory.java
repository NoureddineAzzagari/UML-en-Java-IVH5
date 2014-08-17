/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.datastorage.daofactory.rmi;

import org.apache.log4j.Logger;
import library.datastorage.daofactory.DAOFactory;
import library.datastorage.daofactory.interfaces.LoanDAOInf;
import library.datastorage.daofactory.interfaces.MemberDAOInf;
import library.datastorage.daofactory.interfaces.ReservationDAOInf;

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
