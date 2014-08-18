/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.avans.aei.ivh5.model.dao.xml.dom;

import org.apache.log4j.Logger;

import edu.avans.aei.ivh5.model.dao.DAOFactory;
import edu.avans.aei.ivh5.model.dao.api.LoanDAOInf;
import edu.avans.aei.ivh5.model.dao.api.MemberDAOInf;
import edu.avans.aei.ivh5.model.dao.api.ReservationDAOInf;

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
		logger.debug("Factory constructed.");
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
