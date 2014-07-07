/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.datastorage.daofactory.xml;

import library.datastorage.daofactory.DAOFactory;
import library.datastorage.daofactory.interfaces.LoanDAOInf;
import library.datastorage.daofactory.interfaces.MemberDAOInf;
import library.datastorage.daofactory.interfaces.ReservationDAOInf;

/**
 *
 * @author Robin Schellius
 */
public class XmlDAOFactory extends DAOFactory {
    
    public XmlDAOFactory() {
    	
    }
    
	public MemberDAOInf getMemberDAO() {
		return new XmlMemberDAO();
	}
	
	public LoanDAOInf getLoanDAO() {
		return new XmlLoanDAO();
	}
	
	public ReservationDAOInf getReservationDAO() {
		return new XmlReservationDAO();
	}
}
