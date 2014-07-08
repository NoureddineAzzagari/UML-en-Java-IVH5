/**
 * 
 */
package library.datastorage.daofactory;

import org.apache.log4j.Logger;

import library.datastorage.daofactory.interfaces.LoanDAOInf;
import library.datastorage.daofactory.interfaces.MemberDAOInf;
import library.datastorage.daofactory.interfaces.ReservationDAOInf;

/**
 * Abstract class DAO Factory
 * 
 * @author Robin Schellius
 * 
 */
public abstract class DAOFactory {

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(DAOFactory.class);

	/**
	 * 
	 * @param factoryClassName
	 * @return
	 */
	public static DAOFactory getDAOFactory(String factoryClassName) {

		DAOFactory theFactory = null;
		
		try {
			Class<?> theClass = Class.forName(factoryClassName);
			theFactory = (DAOFactory)theClass.newInstance();
		} catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
		} catch (InstantiationException e) {
            logger.error(e.getMessage());
		} catch (IllegalAccessException e) {
            logger.error(e.getMessage());
		}
		
		return theFactory;
	}

	public abstract MemberDAOInf getMemberDAO();
	public abstract LoanDAOInf getLoanDAO();
	public abstract ReservationDAOInf getReservationDAO();

}
