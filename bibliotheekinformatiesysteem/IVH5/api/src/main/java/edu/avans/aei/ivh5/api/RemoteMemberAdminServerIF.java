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
public interface RemoteMemberAdminServerIF extends Remote {

	/**
     * Find all the available services, as seen by the server. A server can have access to other services
     * on remote machines, so the amount of services can be larger than only the services that the server
     * shares on the same host.
     * 
     * @return A list of <servicename, hostname> pairs, or null if none were found.
     */
	public ArrayList<String> findAvailableServices() throws RemoteException;

	/**
	 * Find all members on all of the available services, and return all results in a single list.
	 * 
	 * @return A list of information about the member, and the host and service that is was found on.
	 * @throws RemoteException
	 */
	public ArrayList<RemoteMemberInfo> findAllMembers(ArrayList<String> visitedServices) throws RemoteException;

	/**
     * Tries to find the Member object matching the given membership number.
     * 
     * @param membershipNumber the member unique number
     * @return if a matching member was found, a reference to the Member's
     * ImmutableMember interface is returned, null otherwise.
     */
	public Member findMember(String hostname, String service, int membershipNumber) throws RemoteException;


}
