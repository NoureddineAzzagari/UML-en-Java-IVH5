/**
 * Avans Academie voor ICT & Engineering
 * Worked example - Library Information System.
 */
package edu.avans.aei.ivh5.control;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import edu.avans.aei.ivh5.api.RemoteMemberAdminManagerIF;
import edu.avans.aei.ivh5.model.domain.ImmutableMember;
import edu.avans.aei.ivh5.model.domain.Member;
import edu.avans.aei.ivh5.view.ui.LibraryUI;

/**
 * <p>
 * This class contains the controller functionality from the Model-View-Control approach. 
 * The controller handles all actions and events that result from interaction with the system, 
 * in whatever way - being user interaction via the UI, or machine-to-machine interaction via
 * a data access object.
 * </p>
 * <p>
 * The controller separates the controlling functionality from the model (managing access to data)
 * and the view (managing the displaying of data).
 * </p>
 * 
 * @author Robin Schellius
 */
public class Controller implements ListSelectionListener {

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(Controller.class);

	// A reference to the user interface for access to view components (text fields, tables)
	private LibraryUI userinterface = null;
	
	// Reference to the member manager.
	private RemoteMemberAdminManagerIF manager = null;
	
	/**
	 * Constructor, initializing generally required references to user interface components.
	 */
	public Controller(LibraryUI ui, RemoteMemberAdminManagerIF mgr) 
	{
		logger.debug("Constructor");
		
		// Saving references to UI and Mgr for later use.
		userinterface = ui;
		manager = mgr;
	}
	
	/**
	 * Find the member identified by membershipNr and display its information.
	 * 
	 * @param hostname Name/IP address of the host to find the member on.
	 * @param servicename Name of the service to find the host on.
	 * @param membershipNr Number of the manager to be found.
	 */
	public boolean doFindMember(String hostname, String servicename, int membershipNr) 
	{
		logger.debug("doFindMember " + membershipNr + " on " + servicename + " at " + hostname);
		
		Member member;
		boolean memberFound = false;

		if (manager == null || userinterface == null) {
			logger.error("Null values (manager/userinterface)!");
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
				userinterface.setMemberListData(memberIDs);
			} else {
				logger.debug("No members found on the given service.");
				userinterface.setStatusText("Geen informatie gevonden.");
			}
		} else {
			logger.error("Manager is null! Could not find members!");
		}
	}

	/**
	 * This method handles list selection events. Whenever the user selects a row in the memberlist
	 * of the user interface, valueChanged is fired and the appropriate action is taken. 
	 */
    public void valueChanged(ListSelectionEvent e) {
    	
        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
        String selectedMember = null;

        // There can be multiple events going on, since the user could possibly select multiple
        // rows and even individual columns. We first wait until the user is finished adjusting. 
        if(e.getValueIsAdjusting() == false) {

        	// There could be multiple rows (or columns) selected, even 
        	// though ListSelectionModel = SINGLE_SELECTION. Find out which 
        	// indexes are selected.
            int minIndex = lsm.getMinSelectionIndex();
            int maxIndex = lsm.getMaxSelectionIndex();
            for (int i = minIndex; i <= maxIndex; i++) {
                if (lsm.isSelectedIndex(i)) 
                {
                    selectedMember = (String) userinterface.getDataTableModel().getValueAt(i, 0);
                    if (selectedMember != null) {
                        
                        logger.debug("Selected member = " + selectedMember);
                        doFindMember("localhost", userinterface.getSelectedService(), Integer.parseInt(selectedMember));
                    }
                }
            }
        }
    }

}
