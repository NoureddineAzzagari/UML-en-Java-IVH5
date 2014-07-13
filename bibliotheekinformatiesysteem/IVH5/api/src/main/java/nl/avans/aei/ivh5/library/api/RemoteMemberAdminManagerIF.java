package nl.avans.aei.ivh5.library.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import library.domain.ImmutableMember;

/**
 * Interface that describes the available methods on the remote Library server.
 * 
 * @author Robin Schellius
 *
 */
public interface RemoteMemberAdminManagerIF extends Remote {

    /**
     * Tries to find the Member object matching the given membershipnumber.
     * 
     * @param membershipNumber the member unique number
     * @return if a matching member was found, a reference to the Member's
     * ImmutableMember interface is returned, null otherwise.
     */
	public ImmutableMember findMember(int membershipNumber) throws RemoteException;
	
	/**
	 * Find all members.
	 * 
	 * @return A list of retrieved members, or null is none were found.
	 * @throws RemoteException
	 */
	public ArrayList<ImmutableMember> findMembers() throws RemoteException;

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

}
