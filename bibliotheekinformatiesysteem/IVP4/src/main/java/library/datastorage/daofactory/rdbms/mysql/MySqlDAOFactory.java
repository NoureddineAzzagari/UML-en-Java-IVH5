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
 *
 * @author Robin Schellius
 */
public class MySqlDAOFactory extends DAOFactory {
    
    public MySqlDAOFactory() {
    	
    }
    
	public MemberDAOInf getMemberDAO() {
		return new MySqlMemberDAO();
	}
	
	public LoanDAOInf getLoanDAO() {
		return new MySqlLoanDAO();
	}
	
	public ReservationDAOInf getReservationDAO() {
		return new MySqlReservationDAO();
	}
    
}
