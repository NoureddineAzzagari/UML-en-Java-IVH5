/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.avans.aei.ivh5.model.dao.rmi;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import edu.avans.aei.ivh5.model.dao.api.LoanDAOInf;
import edu.avans.aei.ivh5.model.domain.Loan;
import edu.avans.aei.ivh5.model.domain.Member;

/**
 * The RMI implementation for the LoanDAO class. This class provides CRUD 
 * methods to work with Loan domain object on remote RMI servers.
 * 
 * @author Robin Schellius
 */
public class RmiLoanDAO implements LoanDAOInf {
	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(RmiLoanDAO.class);

	public RmiLoanDAO() {
	}

	/**
	 * Tries to find the loans on a given remote RMI server.
	 * In this POC, the lend copies of the books are
	 * not loaded - it is out of scope for now.
	 * 
	 * @param member
	 *            identifies the member whose loans are to be loaded from the
	 *            database
	 * 
	 * @return an ArrayList object containing the Loan objects that were found.
	 *         In case no loan could be found, still a valid ArrayList object is
	 *         returned. It does not contain any objects.
	 */
	public ArrayList<Loan> findLoans(Member member) {
		logger.debug("findLoans - not implemented yet.");

		return null;
	}
}