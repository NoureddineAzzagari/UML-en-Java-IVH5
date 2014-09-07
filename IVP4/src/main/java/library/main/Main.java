/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.main;

import library.businesslogic.MemberAdminManagerImpl;
import library.presentation.LibraryUI;
import library.presentation.MemberAdminUI;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author ppthgast
 */
public class Main {

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(Main.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
		// Configure logging. 
		PropertyConfigurator.configure("./logsettings.cnf");
		logger.debug("Starting application ---------------------------------");

//		MemberAdminUI ui = new MemberAdminUI(new MemberAdminManagerImpl());
//        ui.setVisible(true);
        
        LibraryUI ui = new LibraryUI(new MemberAdminManagerImpl());
		ui.frmLibraryInformationSystem.setVisible(true);
    }
}
