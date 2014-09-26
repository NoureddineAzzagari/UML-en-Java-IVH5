/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.avans.aei.ivh5.model.main;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import org.apache.log4j.Logger;

import edu.avans.aei.ivh5.api.RemoteMemberAdminClientIF;
import edu.avans.aei.ivh5.api.RemoteMemberAdminServerIF;
import edu.avans.aei.ivh5.api.RemoteMemberInfo;
import edu.avans.aei.ivh5.model.dao.DAOFactory;
import edu.avans.aei.ivh5.model.dao.api.LoanDAOInf;
import edu.avans.aei.ivh5.model.dao.api.MemberDAOInf;
import edu.avans.aei.ivh5.model.dao.api.ReservationDAOInf;
import edu.avans.aei.ivh5.model.dao.rmi.RmiConnection;
import edu.avans.aei.ivh5.model.domain.ImmutableMember;
import edu.avans.aei.ivh5.model.domain.Loan;
import edu.avans.aei.ivh5.model.domain.Member;
import edu.avans.aei.ivh5.model.domain.Reservation;
import edu.avans.aei.ivh5.util.Settings;

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
 * @see RemoteMemberAdminClientIF
 * @see edu.avans.aei.ivh5.model.main.LibraryServer
 * @author ppthgast, rschelli
 */
public class MemberAdminManagerImpl implements RemoteMemberAdminClientIF,
		RemoteMemberAdminServerIF {

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(MemberAdminManagerImpl.class);

	// When a member is found, we cache it in a hashmap for fast retrieval in a
	// next search.
	private HashMap<Integer, Member> members;

	// The factories that provides DAO instances for domain classes.
	private DAOFactory daoFactory; // Handles data requests on the local
										// server.

	// The servicename that identifies this service in the RMI registry.
	// When finding all available services in the registry we want to exclude
	// ourselves.
	private String myServicename;

	/**
	 * Instantiate and initialize the server stub. The servicename is the name
	 * that identifies this server in the RMI registry. It is received from the
	 * server and stored locally for later lookup. When communicating beween
	 * multiple servers we can thus distinguish ourselves from the remote
	 * server.
	 * 
	 * @param servicename
	 *            The name that identifies this server in the RMI registry.
	 */
	public MemberAdminManagerImpl(String servicename) throws RemoteException {

		logger.debug("Constructor using " + servicename);

		// Create a list of members for fast lookup.
		members = new HashMap<Integer, Member>();

		// Our 'own' servicename; prevents looking it up in the registry as a
		// remote server.
		myServicename = servicename;

		try {
			// Getting DAOFactory for local and remote data access
			daoFactory = DAOFactory.getDAOFactory(Settings.props
					.getProperty(Settings.propDataStore));
		} catch (NoClassDefFoundError e) {
			logger.fatal("Error: Class not found! (Is it in the propertyfile?)");
			logger.fatal(e.getMessage());
		}
	}

	/**
	 * Find a list of available services.
	 * 
	 * @return List of servicenames, or null if none were found.
	 * @throws RemoteException
	 */
	// public ArrayList<String> findAvailableServices(String hostname, String
	// serviceGroup){
	//
	// logger.debug("findAvailableServices");
	//
	// ArrayList<String> availableServices = null;
	//
	// if(remoteDaoFactory != null) {
	// availableServices = ((RmiDAOFactory)remoteDaoFactory)
	// .findAvailableServices(hostname, serviceGroup);
	// } else {
	// logger.error("Could not contact remote DAO factory!");
	// }
	//
	// return availableServices;
	// }

	// /**
	// * Find a single Member based on its membershipNumber. If no Member is
	// * found, null is returned. Before searching in the datasource, we first
	// * check if it already exists in the list of previously found members.
	// *
	// * @param membershipNumber The Member's membershipNumber.
	// */
	// public Member findMember(int membershipNumber) throws RemoteException {
	//
	// logger.debug("findMember " + membershipNumber);
	//
	// MemberDAOInf memberDAO = null;
	// Member member = null;
	//
	// // First do a lookup in the members cache.
	// member = members.get(membershipNumber);
	//
	// if (member == null) {
	// logger.debug("Member not found in cache, lookup in datasource.");
	//
	// if (localDaoFactory != null) {
	// memberDAO = localDaoFactory.getMemberDAO();
	//
	// if(memberDAO != null)
	// {
	// member = memberDAO.findMember(membershipNumber);
	// if (member != null) {
	// logger.debug("Member found in datasource.");
	//
	// LoanDAOInf loanDAO = localDaoFactory.getLoanDAO();
	// ArrayList<Loan> loans = loanDAO.findLoans(member);
	//
	// if (loans != null) {
	// for (Loan loan : loans) {
	// if (loan != null) {
	// member.addLoan(loan);
	// }
	// }
	// }
	//
	// ReservationDAOInf reservationDAO = localDaoFactory.getReservationDAO();
	// ArrayList<Reservation> reservations =
	// reservationDAO.findReservations(member);
	//
	// if (reservations != null) {
	// for (Reservation reservation : reservations) {
	// if (reservation != null) {
	// member.addReservation(reservation);
	// }
	// }
	// }
	//
	// // Cache the member that has been read from the database, to
	// // avoid querying the database each time a member is needed.
	// members.put(membershipNumber, member);
	// } else {
	// logger.debug("Member not found in datasource.");
	// }
	// } else {
	// logger.debug("MemberDAO not found!");
	// }
	// }
	// }
	// return member;
	// }

	/**
	 * 
	 */
	@Override
	public Member findMember(String hostname, String service,
			int membershipNumber) throws RemoteException {

		logger.debug("findMember on " + hostname + " " + service);

		Member member = null;

		if (service.equals(myServicename)
				&& hostname
						.equals(System.getProperty(Settings.propRmiHostName))) {

			member = findMemberOnServer(hostname, service, membershipNumber);

		} else {
			// Search remote server
			try {
				Registry registry = null;
				RemoteMemberAdminServerIF remoteMgr;
				registry = RmiConnection.getRegistry(hostname);
				remoteMgr = (RemoteMemberAdminServerIF) registry
						.lookup(service);
				member = remoteMgr.findMemberOnServer(hostname, service,
						membershipNumber);
			} catch (RemoteException e) {
				logger.error("RemoteException: " + e.getMessage());
			} catch (NotBoundException e) {
				logger.error("NotBoundException: " + e.getMessage());
			}
		}

		if (member != null)
			logger.debug("Found member " + member.getMembershipNumber());
		else
			logger.debug("Member " + membershipNumber + " not found");

		return member;
	}

	/**
	 * 
	 */
	@Override
	public Member findMemberOnServer(String hostname, String service,
			int membershipNumber) throws RemoteException {

		logger.debug("findMemberOnServer on " + hostname + " " + service);

		Member member = null;

		// Search in our local DAO
		if (daoFactory != null) {
			MemberDAOInf memberDAO = daoFactory.getMemberDAO();
			member = memberDAO.findMember(membershipNumber);
		} else {
			logger.error("Could not contact local DAO factory!");
		}

		if (member != null)
			logger.debug("Found member " + member.getMembershipNumber());
		else
			logger.debug("Member " + membershipNumber + " not found");

		return member;
	}

	/**
	 * <p>
	 * Create a list of membershipNumbers of all available Members in the
	 * datasource of the given service on the given host machine. This can
	 * result in a lookup on a remote machine.
	 * </p>
	 * 
	 * <p>
	 * The service name is selected by the user via the user interface. The
	 * service that the user selects could be the local server, or a remote
	 * server. In case of a local server we immediately lookup the members in
	 * the datasoure. In case of a remote server we setup a connection to the
	 * remote server and perform a lookup there.
	 * </p>
	 * 
	 * @return The list of membershipNumbers of members in the datasource.
	 * @author Robin Schellius
	 */
	@Override
	public ArrayList<RemoteMemberInfo> findAllMembers() throws RemoteException {

		logger.debug("findAllMembers");

		// ServiceGroup defines the group of services that we can connect to
		String serviceGroup = Settings.props
				.getProperty(Settings.propRmiServiceGroup);

		// A future addition could be to search multiple hosts for members.
		ArrayList<String> availableHosts = new ArrayList<String>();

		// Currently we only search one single host - the one this server is running on.
		// Add our own host to search for available services
		availableHosts.add(System.getProperty(Settings.propRmiHostName));

		// Find all hosts that we can connect to from the properties 
		String propRemoteHosts = Settings.props.getProperty(Settings.propRmiServiceHosts);

		if (propRemoteHosts != null) {
			// Remove spaces
			propRemoteHosts = propRemoteHosts.replace(" ", "");
			// Split in substrings
			String[] remoteHosts = propRemoteHosts.split(",");
			// Add to list of available hosts, if it's not already there
			for (String host : remoteHosts) {
				if (!availableHosts.contains(host))
					availableHosts.add(host);
			}
		}
		logger.debug("Available hosts: " + availableHosts.toString());

		// List of resulting members
		ArrayList<RemoteMemberInfo> memberList = new ArrayList<RemoteMemberInfo>();

		//
		// For each host, find the available services and find members they
		// contain.
		//
		for (String host : availableHosts) {

			// Find available services on this host
			ArrayList<String> availableServices = RmiConnection
					.findAvailableServices(host, serviceGroup);
			if (availableServices != null) {

				logger.debug("Connectiong to all services on host " + host
						+ " ...");
				for (String service : availableServices) {

					// If this service is our own local service, process it
					// locally, and add results to memberList
					if (service.equals(myServicename)
							&& host.equals(System
									.getProperty(Settings.propRmiHostName))) {
						ArrayList<RemoteMemberInfo> members = findAllMembersOnServer();
						if(members != null) {
							memberList.addAll(members);
						}
					} else {
						logger.debug("FindAllMembers at " + host + " "
								+ service);

						// Do a search on a remote machine
						try {
							Registry registry = null;
							RemoteMemberAdminServerIF remoteMgr;

							registry = RmiConnection.getRegistry(host);
							remoteMgr = (RemoteMemberAdminServerIF) registry
									.lookup(service);
							ArrayList<RemoteMemberInfo> list = remoteMgr
									.findAllMembersOnServer();
							if(list != null) {
								memberList.addAll(list);
							}

						} catch (NotBoundException e) {
							logger.error("NotBoundException: " + e.getMessage());
						} catch (RemoteException e) {
							logger.error("RemoteException: " + e.getMessage());
						}
					}
				}
			}
		}

		/**
		 * We want to sort the resulting arraylist, but to do that, we need to
		 * define how to sort it. Since we defined a comparator method on
		 * RemoteMemberInfo, we can compare objects to each other. The order
		 * that was chosen in that method defines the ordering in the sort
		 * method. See the RemoteMemberInfo class.
		 */
		Collections.sort(memberList);

		int count = (memberList == null) ? 0 : memberList.size();
		logger.debug("Returning " + count + " items");

		return memberList;
	}

	/**
	 * Search for members on the local data source.
	 * 
	 * @return A list of RemoteMemberInfo objects, or null of none were found.
	 * @see RemoteMemberInfo
	 */
	@Override
	public ArrayList<RemoteMemberInfo> findAllMembersOnServer()
			throws RemoteException {

		logger.debug("FindAllMembersOnServer " + myServicename);

		ArrayList<RemoteMemberInfo> resultList = null;

		if (daoFactory != null) {
			MemberDAOInf memberDAO = daoFactory.getMemberDAO();
			ArrayList<ImmutableMember> list = memberDAO.findAllMembers();
			if (list != null && list.size() > 0) {
				resultList = new ArrayList<RemoteMemberInfo>();
				String myHostname = System.getProperty(Settings.propRmiHostName);
				for (ImmutableMember mem : list) {
					resultList.add(new RemoteMemberInfo(myHostname,	myServicename, (Member) mem));
				}
			}
		} else {
			logger.error("Could not contact local DAO factory!");
		}

		return resultList;

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
				MemberDAOInf memberDAO = daoFactory.getMemberDAO();
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
