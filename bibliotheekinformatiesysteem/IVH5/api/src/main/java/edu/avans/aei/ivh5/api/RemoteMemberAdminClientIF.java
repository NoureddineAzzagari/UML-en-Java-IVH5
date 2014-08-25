package edu.avans.aei.ivh5.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import edu.avans.aei.ivh5.model.domain.ImmutableMember;
import edu.avans.aei.ivh5.model.domain.Member;

/**
 * Interface that describes the available methods on a remote library server.
 * 
 * @author Robin Schellius
 *
 */
public interface RemoteMemberAdminClientIF extends Remote {

	/**
     * Tries to find the Member object matching the given membership number.
     * 
     * @param membershipNumber the member unique number
     * @return if a matching member was found, a reference to the Member's
     * ImmutableMember interface is returned, null otherwise.
     */
	// public Member findMember(int membershipNumber) throws RemoteException;

	/**
	 * Find a member which could possibly be on a remote service, on a remote host.
	 * 
	 * @param hostname
	 * @param service
	 * @param membershipNumber
	 * @return
	 * @throws RemoteException
	 */
	public Member findMember(String hostname, String service, int membershipNumber) throws RemoteException;

	/**
	 * Find all members on all available servers.  
	 * 
	 * @return A list of retrieved members, or null is none were found.
	 * @throws RemoteException
	 */
	public ArrayList<RemoteMemberInfo> findAllMembers() throws RemoteException;

    /**
     * Removes the given member from the system, including removal from the
     * persistent data store.
     * 
     * @param memberToRemove a reference to the member to be removed
     * @return true if removal was successful, false otherwise, i.e. when
     * it was not allowed to remove the member or when removal from the
     * data store did not succeed.
     */
    public boolean removeMember(ImmutableMember memberToRemove)  throws RemoteException;

    /**
     * Here we add more methods that could be called by the client.
     */
}
