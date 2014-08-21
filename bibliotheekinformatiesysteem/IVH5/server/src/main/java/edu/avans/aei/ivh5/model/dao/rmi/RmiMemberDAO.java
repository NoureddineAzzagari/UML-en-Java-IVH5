/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.avans.aei.ivh5.model.dao.rmi;

import java.rmi.RemoteException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import edu.avans.aei.ivh5.api.RemoteMemberAdminManagerIF;
import edu.avans.aei.ivh5.model.dao.api.MemberDAOInf;
import edu.avans.aei.ivh5.model.domain.ImmutableMember;
import edu.avans.aei.ivh5.model.domain.Member;
import edu.avans.aei.ivh5.util.RmiConnection;

/**
 * The RMI implementation for the MemberDAO class.
 * 
 * @author Robin Schellius
 */
public class RmiMemberDAO implements MemberDAOInf {

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(RmiMemberDAO.class);

	/**
	 * Constructor
	 */
	public RmiMemberDAO() {

	}

	/**
	 * Tries to find the member identified by the given membership number in the
	 * persistent data store. All loans and reservations for the member are
	 * loaded as well. In this POC, the reserved books and/or lend copies of the
	 * books are not loaded - it is out of scope for now.
	 * 
	 * @param membershipNumber
	 *            identifies the member to be loaded from the database
	 * 
	 * @return the Member object to be found. In case member could not be found,
	 *         null is returned.
	 */
	public Member findMember(int membershipNumber) {
		logger.debug("findMember " + membershipNumber);
		Member member = null;

		// Use the previously created RmiConnection to connect
		// to the remote server.
		try {
			RemoteMemberAdminManagerIF remoteManager = 
					(RemoteMemberAdminManagerIF) RmiConnection.getService("DUMMY", "DUMMY");
			member = remoteManager.findMember(RmiConnection.hostname, RmiConnection.servicename, membershipNumber);
		} catch (RemoteException e) {
			logger.error("Exception: " + e.getMessage());
		}

		return member;
	}

	/**
	 * Returns a list of MembershipNumbers of Members that exist in the given
	 * DAO, or null if none were found.
	 * 
	 * @return Array of integers indicating the MembershipNumbers, or null if
	 *         none were found.
	 * 
	 * @author Robin Schellius
	 */
	public ArrayList<String> findAllMembers() {
		logger.debug("findAllMembers");
		ArrayList<String> result = null;

		// Use the previously created RmiConnection to connect
		// to the remote server.
//		RemoteMemberAdminManagerIF remoteManager = 
//				(RemoteMemberAdminManagerIF) RmiConnection.getRemoteService();
//		if (remoteManager != null) {
//			logger.debug("Found RMI connection to remote server!");
//			try {
//				if(RmiConnection.hostname != null && RmiConnection.servicename != null) { 
//					result = remoteManager.findAllMembers(RmiConnection.hostname, RmiConnection.servicename);
//				} else {
//					logger.error("Remote hostname and/or service unknown!");
//				}
//			} catch (RemoteException e) {
//				logger.error("Exception: " + e.getMessage());
//			}
//		} else {
//			logger.error("No RMI connection to remote server!");
//		}
//		
//		if (result != null)
//			logger.debug("Returning " + result.toString());
//		else
//			logger.debug("Found no members at remote server.");
		return result;
	}

	/**
	 * Removes the given member.
	 * 
	 * @param memberToBeRemoved
	 *            an object of the Member class representing the member to be
	 *            removed.
	 * 
	 * @return true if deletion was successful, false otherwise.
	 */
	public boolean removeMember(Member memberToBeRemoved) {
		logger.debug("removeMember membershipNumber - not yet implemented");

		boolean result = false;
		return result;
	}

	/**
	 * Inserts the given member into the datasource.
	 * 
	 * @param newMember
	 *            an object of the Member class representing the member to be
	 *            inserted.
	 * 
	 * @return true if execution was successful, false otherwise.
	 */
	public int insertMember(Member newMember) {

		logger.debug("insertMember " + newMember.getMembershipNumber());

		// TODO Return valid result from insertMember in XmlDOMMemberDAO
		return 0;
	}
}
