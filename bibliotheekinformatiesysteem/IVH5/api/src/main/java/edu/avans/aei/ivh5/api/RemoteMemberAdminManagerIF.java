package edu.avans.aei.ivh5.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import edu.avans.aei.ivh5.model.domain.ImmutableMember;

/**
 * Interface that describes the available methods on a remote Library server.
 * 
 * @author Robin Schellius
 *
 */
public interface RemoteMemberAdminManagerIF extends Remote {

    /**
     * Tries to find the Member object matching the given membership number.
     * 
     * @param membershipNumber the member unique number
     * @return if a matching member was found, a reference to the Member's
     * ImmutableMember interface is returned, null otherwise.
     */
	public ImmutableMember findMember(String hostname, String service, int membershipNumber) throws RemoteException;
	
	/**
	 * Find all members on a server. Since a request to this method can be invoked
	 * by the corresponding client (GUI client to server) or by a remote server 
	 * (local server to remote server) we need the hostname and service name to make
	 * the difference. If the servicename equals our own name, we make a local lookup
	 * for members. If the servicename is different, it is remote and we perform 
	 * a remote lookup.
	 * 
	 * @param hostName Name or IP-address of the local or remote host.
	 * @param serviceName Name of the, possibly remote, service as registered in the registry.
	 * @return A list of retrieved members, or null is none were found.
	 * @throws RemoteException
	 */
	public ArrayList<String> findAllMembers(String hostname, String service) throws RemoteException;

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
