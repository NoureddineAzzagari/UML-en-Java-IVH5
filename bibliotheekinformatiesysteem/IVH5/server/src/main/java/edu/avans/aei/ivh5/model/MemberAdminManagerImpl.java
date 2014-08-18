/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.avans.aei.ivh5.model;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import edu.avans.aei.ivh5.api.RemoteMemberAdminManagerIF;
import edu.avans.aei.ivh5.model.dao.DAOFactory;
import edu.avans.aei.ivh5.model.dao.api.LoanDAOInf;
import edu.avans.aei.ivh5.model.dao.api.MemberDAOInf;
import edu.avans.aei.ivh5.model.dao.api.ReservationDAOInf;
import edu.avans.aei.ivh5.model.dao.rmi.RmiConnection;
import edu.avans.aei.ivh5.model.domain.ImmutableMember;
import edu.avans.aei.ivh5.model.domain.Loan;
import edu.avans.aei.ivh5.model.domain.Member;
import edu.avans.aei.ivh5.model.domain.Reservation;
import edu.avans.aei.ivh5.model.main.LibraryServer;

/**
 * The server side implementation of the remote interface. This class implements
 * the methods that were specified in the api interface, and enables clients to
 * call these methods remotely.
 * <p>
 * Note that this class is not the RMI server itself; it is the server-side
 * implementation stub which must be instantiated and made publicly available by
 * a RMI server class.
 * </p>
 * 
 * @see RemoteMemberAdminManagerIF
 * @see edu.avans.aei.ivh5.model.main.LibraryServer
 * @author ppthgast, rschelli
 */
public class MemberAdminManagerImpl implements RemoteMemberAdminManagerIF {

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(MemberAdminManagerImpl.class);

	// When a member is found, we cache it in a hashmap for fast retrieval in a next search.
	private HashMap<Integer, Member> members;

	// The factories that provides DAO instances for domain classes.
	private DAOFactory localDaoFactory; // Handles data requests on the local server.
	private DAOFactory remoteDaoFactory; // Handles remote data requests via RMI.

	// The servicename that identifies this service in the RMI registry.
	// When finding all available services in the registry we want to exclude
	// ourselves.
	private String servicename;

	/**
	 * Instantiate and initialize the server stub. The servicename is the name
	 * that identifies this server in the RMI registry. It is received
	 * from the server and stored locally for later lookup. When communicating beween
	 * multiple servers we can thus distinguish ourselves from the remote server.
	 * 
	 * @param servicename The name that identifies this server in the RMI registry.
	 */
	public MemberAdminManagerImpl(String servicename) throws RemoteException {
		logger.debug("Constructor");
		
		// Create a list of members for fast lookup.
		members = new HashMap<Integer, Member>();
		
		// Our 'own' servicename; prevents looking it up in the registry as a remote server.
		this.servicename = servicename;

		// Getting DAOFactory for local data access
		localDaoFactory = DAOFactory.getDAOFactory(LibraryServer.daofactoryclassname);

		// Getting DAOFactory for remote data access
		remoteDaoFactory = DAOFactory.getDAOFactory(LibraryServer.rmifactoryclassname);
	}

	/**
     * Find a single Member based on its membershipNumber. If no Member is
     * found, null is returned. Before searching in the datasource, we first
     * check if it already exists in the list of previously found members.
     * 
     * @param membershipNumber The Member's membershipNumber.
     */
	public ImmutableMember findMember(String hostname, String service, int membershipNumber) 
			throws RemoteException {
		logger.debug("findMember " + membershipNumber + " on \"" + service + "\" at " + hostname);

		MemberDAOInf memberDAO = null;
		Member member = null;

		// First do a lookup in the members cache.
		member = members.get(membershipNumber);

		if (member == null) {
			logger.debug("Member not found in cache, lookup in datasource.");

			if (service != null && hostname != null) 
			{
				if (service.equals(this.servicename)) 
				{
					logger.debug("Perform lookup in the local datasource.");					
					memberDAO = localDaoFactory.getMemberDAO();
				} 
				else 
				{
					logger.debug("Perform lookup on remote service.");
					memberDAO = remoteDaoFactory.getMemberDAO();
					// At this point we know that we are going to make a remote
					// lookup. We know the hostname and the remote service name.
					// We therefore setup the connection here, so that the memberDAO
					// can use it when it performs the actual lookup.
					RmiConnection.connectToService(hostname, service);
				}
				
				if(memberDAO != null)
				{
					logger.debug("TODO - not implemented yet!");
					/*	
					member = memberDAO.findMember(hostname, service, membershipNumber);
					if (member != null) {
						logger.debug("Member found in datasource.");
	
						LoanDAOInf loanDAO = localDaoFactory.getLoanDAO();
						ArrayList<Loan> loans = loanDAO.findLoans(member);
	
						if (loans != null) {
							for (Loan loan : loans) {
								if (loan != null) {
									member.addLoan(loan);
								}
							}
						}
	
						ReservationDAOInf reservationDAO = localDaoFactory.getReservationDAO();
						ArrayList<Reservation> reservations = reservationDAO.findReservations(member);
	
						if (reservations != null) {
							for (Reservation reservation : reservations) {
								if (reservation != null) {
									member.addReservation(reservation);
								}
							}
						}
	
						// Cache the member that has been read from the database, to
						// avoid querying the database each time a member is needed.
						members.put(membershipNumber, member);
					} else {
						logger.debug("Member not found in datasource.");
					}
					*/
				}				
			}
		}
		return member;
	}

	/**
	 * <p>Create a list of membershipNumbers of all available Members in the
	 * datasource of the given service on the given host machine. This can
	 * result in a lookup on a remote machine.</p>
	 * 
	 * <p>The service name is selected by the user via the user interface. The service
	 * that the user selects could be the local server, or a remote server. In case of a 
	 * local server we immediately lookup the members in the datasoure. In case of a 
	 * remote server we setup a connection to the remote server and perform a lookup
	 * there.</p>
	 * 
	 * @return The list of membershipNumbers of members in the datasource.
	 * @author Robin Schellius
	 */
	@Override
	public ArrayList<String> findAllMembers(String hostname, String service)
			throws RemoteException {

		logger.debug("findAllMembers on \"" + service + "\" at " + hostname);

		MemberDAOInf memberDAO = null;
		ArrayList<String> result = null;

		if (service != null && hostname != null) {
			if (service.equals(this.servicename)) {
				logger.debug("Perform lookup in the local datasource.");
				memberDAO = localDaoFactory.getMemberDAO();
			} else {
				logger.debug("Perform lookup on remote service.");
				memberDAO = remoteDaoFactory.getMemberDAO();
				// At this point we know that we are going to make a remote
				// lookup. We know the hostname and the remote service name.
				// We therefore setup the connection here, so that the memberDAO
				// can use it when it performs the actual lookup.
				RmiConnection.connectToService(hostname, service);
			}

			if (memberDAO != null) {
				result = memberDAO.findAllMembers();
			} else {
				logger.error("memberDAO was null!");
			}
		}
		if(result != null)
			logger.debug("Found members: " + result.toString());
		else
			logger.debug("Found no members!");

		return result;
	}

	/**
	 * 
	 */
	public boolean removeMember(ImmutableMember memberToRemove) {
		logger.debug("removeMember " + memberToRemove);

		boolean result = false;

		if (memberToRemove.isRemovable()) {
			// Cast the given member to the Member object, otherwise the
			// removal cannot be done since the interface does not support it.
			Member member = (Member) memberToRemove;

			int membershipNumber = memberToRemove.getMembershipNumber();

			// Let the member remove itself as a domain object. Do this before
			// removing from the database. In case something goes wrong, we
			// still have the data in the database. If first the member was
			// removed from the database, we might end up in an inconsistent
			// state.
			result = member.remove();

			if (result) {
				// Let the member remove itself from the database.
				MemberDAOInf memberDAO = localDaoFactory.getMemberDAO();
				result = memberDAO.removeMember(member);

				// In case something goes wrong here, we need to roll back.
				// But that's too much for this version of the POC.
			}

			// Finally, remove the member from the map in this manager.
			members.remove(membershipNumber);
		} else {
			result = false;
		}

		return result;
	}
}
