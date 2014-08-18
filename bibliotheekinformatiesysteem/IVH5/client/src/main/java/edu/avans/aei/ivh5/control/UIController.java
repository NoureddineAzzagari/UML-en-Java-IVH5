/**
 * 
 */
package edu.avans.aei.ivh5.control;

import java.rmi.RemoteException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import edu.avans.aei.ivh5.api.RemoteMemberAdminManagerIF;
import edu.avans.aei.ivh5.model.domain.ImmutableMember;
import edu.avans.aei.ivh5.view.ui.LibraryUI;


/**
 * @author Robin Schellius
 *
 */
public class UIController {

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(UIController.class);

	private LibraryUI userinterface;
	private RemoteMemberAdminManagerIF manager;
	
	/**
	 * 
	 */
	public UIController(LibraryUI ui, RemoteMemberAdminManagerIF mgr) {
		
		userinterface = ui;
		manager = mgr;
	}
	/**
	 * Find the member identified by membershipNr and display its information.
	 * 
	 * @param membershipNr
	 */
	public boolean doFindMember(String hostname, String servicename, int membershipNr) 
	{
		logger.debug("doFindMember " + membershipNr + " on " + servicename + " at " + hostname);
		
		ImmutableMember member;
		boolean memberFound = false;

		if (manager == null) {
			logger.error("Manager is null! ");
		} else {
			try {
				member = manager.findMember(hostname, servicename, membershipNr);

				if (member != null) {
					userinterface.setMemberDetails(member);
					memberFound = true;
				} else {
					userinterface.setStatusText("Lidnummer " + membershipNr + " niet gevonden");
				}
			} catch (RemoteException e) {
				logger.error("Error: " + e.getMessage());
			}
		}
		return memberFound;
	}

	/**
	 * 
	 * @param membershipNr
	 */
	public void doFindAllMembers(String hostname, String servicename) {
		logger.debug("doFindAllMembers on " + servicename + " at " + hostname);

		ArrayList<String> memberIDs = null;

		if (manager != null) {
			try {
				memberIDs = manager.findAllMembers(hostname, servicename);
			} catch (RemoteException e) {
				logger.error("Error finding members: " + e.getMessage());
			}

			if (memberIDs != null) {
				logger.debug("Found members " + memberIDs.toString());
				// Display the results in JList in GUI. This is simpy done by
				// changing the values in the ListModel, which automatically 
				// synchronizes its results to the corresponding JList. 
//		        listModel.removeAllElements();
//				for(String memberID : memberIDs) {
//					listModel.addElement(memberID);
//				}
			} else {
				logger.debug("No members found on the given service.");
				userinterface.setStatusText("Geen informatie gevonden.");
			}
		}
	}

	

}
