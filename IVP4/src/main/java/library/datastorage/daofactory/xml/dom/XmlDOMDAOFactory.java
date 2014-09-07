/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.datastorage.daofactory.xml.dom;

import org.apache.log4j.Logger;
import library.datastorage.daofactory.DAOFactory;
import library.datastorage.daofactory.interfaces.LoanDAOInf;
import library.datastorage.daofactory.interfaces.MemberDAOInf;
import library.datastorage.daofactory.interfaces.ReservationDAOInf;

/**
 * The XML Document Object Model (DOM) implementation of the DAOFactory.
 * This factory provides XML DOM implementations for the domain classes
 * in this system.
 *
 * @author Robin Schellius
 */
public class XmlDOMDAOFactory extends DAOFactory {
    
	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(XmlDOMDAOFactory.class);

	public XmlDOMDAOFactory() {
    }
    
	public MemberDAOInf getMemberDAO() {
		return new XmlDOMMemberDAO();
	}
	
	public LoanDAOInf getLoanDAO() {
		return new XmlDOMLoanDAO();
	}
	
	public ReservationDAOInf getReservationDAO() {
		return new XmlDOMReservationDAO();
	}
}
