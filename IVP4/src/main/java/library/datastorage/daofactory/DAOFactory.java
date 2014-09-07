/**
 * 
 */
package library.datastorage.daofactory;

import org.apache.log4j.Logger;

import library.datastorage.daofactory.interfaces.LoanDAOInf;
import library.datastorage.daofactory.interfaces.MemberDAOInf;
import library.datastorage.daofactory.interfaces.ReservationDAOInf;

/**
 * The DAO Factory is an abstract class that provides the functionality for creating
 * data access objects for different data source implementations. A data source can be 
 * anything that can hold data, such as a relational database, XML file, flat file, 
 * serialized Java objects, or even remote servers. Using this factory, a client can 
 * ask to create a specific factory, which in turn can create specific data access objects
 * for the given data source implementation. The DAO's handle the persistent storage of 
 * the specific domain class, such as (in this case) a Member, Loan of Reservation.
 * 
 * Clients can ask this class to create a DAOFactory instance for a specific 
 * implementation, based on the classname that is provided. The implementation
 * for the specific data source has to extend from this DAOFactory class.
 * 
 * @author Robin Schellius
 * 
 */
public abstract class DAOFactory {

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(DAOFactory.class);

	/**
	 * This method creates an instance of the specified factory class
	 * and returns the instance to the caller. The class of the instance created has
	 * to extend DAOFactory, which forces it to implement the abstract methods to 
	 * get domain objects from this factory.
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

	/**
	 * Create and return a MemberDAO object for the specific datasource implementation.
	 * 
	 * @return MemberDAOInf The specific DAO instance that implements the MemberDAOInf interface.
	 */
	public abstract MemberDAOInf getMemberDAO();

	/**
	 * Create and return a LoanDAO object for the specific datasource implementation.
	 * 
	 * @return LoanDAOInf The specific DAO instance that implements the MemberDAOInf interface.
	 */
	public abstract LoanDAOInf getLoanDAO();

	/**
	 * Create and return a ReservationDAO object for the specific datasource implementation.
	 * 
	 * @return ReservationDAOInf The specific DAO instance that implements the MemberDAOInf interface.
	 */
	public abstract ReservationDAOInf getReservationDAO();

}
