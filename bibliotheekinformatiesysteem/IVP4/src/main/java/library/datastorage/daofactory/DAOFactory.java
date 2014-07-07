/**
 * 
 */
package library.datastorage.daofactory;

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
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return theFactory;
	}

	public abstract MemberDAOInf getMemberDAO();
	public abstract LoanDAOInf getLoanDAO();
	public abstract ReservationDAOInf getReservationDAO();

}
