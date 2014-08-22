/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.avans.aei.ivh5.model.dao.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import edu.avans.aei.ivh5.api.RemoteMemberAdminClientIF;
import edu.avans.aei.ivh5.api.RemoteMemberAdminServerIF;
import edu.avans.aei.ivh5.api.RemoteMemberInfo;
import edu.avans.aei.ivh5.model.dao.api.MemberDAOInf;
import edu.avans.aei.ivh5.model.domain.ImmutableMember;
import edu.avans.aei.ivh5.model.domain.Member;

/**
 * The RMI implementation for the MemberDAO class.
 * 
 * @author Robin Schellius
 */
public class RmiMemberDAO implements MemberDAOInf {

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(RmiMemberDAO.class);

	// The list of remaining services to be processed
	private ArrayList<String> remainingServices = null;
	
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
		logger.debug("findMember " + membershipNumber + " - NOT IMPLEMENTED YET!");
		Member member = null;

		// Use the previously created RmiConnection to connect
		// to the remote server.
//		try {
//			RemoteMemberAdminManagerIF remoteManager = 
//					null;
//			// 		(RemoteMemberAdminManagerIF) RmiConnection.getService("DUMMY", "DUMMY");
//			member = remoteManager.findMember(
//					RmiConnection.hostname, 
//					RmiConnection.servicename, membershipNumber);
//		} catch (RemoteException e) {
//			logger.error("Exception: " + e.getMessage());
//		}

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
	public ArrayList<ImmutableMember> findAllMembers() {
		logger.debug("findAllMembers");

		ArrayList<ImmutableMember> result = null;
		ArrayList<RemoteMemberInfo> memberList = new ArrayList<RemoteMemberInfo>();
		
		for(String nextService: remainingServices) {

			logger.debug("FindAll on remote " + nextService);
			
			try {
				Registry reg = RmiConnection.getRegistry("localhost");
				RemoteMemberAdminServerIF manager = (RemoteMemberAdminServerIF)reg.lookup(nextService);
				
				ArrayList<RemoteMemberInfo> list = manager.findAllMembers(null);
				
			} catch (Exception e) {
				logger.error("Could not contact remote DAO factory!");
			}
		}

		int count = (result == null) ? 0 : result.size();
		logger.debug("returning result: " + count + " items");

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

		// TODO Return valid result from insertMember
		return 0;
	}
	
	/**
	 * 
	 * @param services
	 */
	public void setRemainingServices(ArrayList<String> services) {
		remainingServices = services;
	}
}
