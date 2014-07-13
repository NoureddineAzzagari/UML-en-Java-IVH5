/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.datastorage.daofactory.rdbms.mysql;

import library.datastorage.daofactory.DAOFactory;
import library.datastorage.daofactory.interfaces.LoanDAOInf;
import library.datastorage.daofactory.interfaces.MemberDAOInf;
import library.datastorage.daofactory.interfaces.ReservationDAOInf;

/**
 * This factory provides the MySQL data source implementation. Clients can
 * ask this factory to create specific MySQL data access objects for the 
 * domain classes in this system.
 *
 * @author Robin Schellius
 */
public class MySqlDAOFactory extends DAOFactory {
    
    public MySqlDAOFactory() {
    	// No implementation here.
    }
    
    /**
     * Return the MySQL implementation for the MemberDAO.
     */
	public MemberDAOInf getMemberDAO() {
		return new MySqlMemberDAO();
	}
	
    /**
     * Return the MySQL implementation for the LoanDAO.
     */
	public LoanDAOInf getLoanDAO() {
		return new MySqlLoanDAO();
	}
	
    /**
     * Return the MySQL implementation for the ReservationDAO.
     */
	public ReservationDAOInf getReservationDAO() {
		return new MySqlReservationDAO();
	}
    
}
