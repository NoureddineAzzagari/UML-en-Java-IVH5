/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.main;

import library.businesslogic.MemberAdminManagerImpl;
import library.presentation.MemberAdminUI;

/**
 *
 * @author ppthgast
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        MemberAdminUI ui = new MemberAdminUI(new MemberAdminManagerImpl());
        ui.setVisible(true);
    }
}
