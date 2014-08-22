package edu.avans.aei.ivh5.api;

import java.io.Serializable;

import edu.avans.aei.ivh5.model.domain.ImmutableMember;

/**
 * 
 * @author Robin Schellius
 *
 */
public class RemoteMemberInfo implements Serializable {

	private String hostname;
	private String servicename;
	private ImmutableMember member;
	
	// required by Serializable
	private static final long serialVersionUID = 2736416361882370162L;

	/**
	 * Constructor.
	 * 
	 * @param host
	 * @param service
	 */
	public RemoteMemberInfo(String host, String service, ImmutableMember m) {

		hostname = host;
		servicename = service;
		member = m;
	}
	
	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * @return the servicename
	 */
	public String getServicename() {
		return servicename;
	}

	/**
	 * @return the member
	 */
	public ImmutableMember getMember() {
		return member;
	}
	
}
