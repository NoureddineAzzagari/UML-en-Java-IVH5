/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.presentation;

import library.businesslogic.MemberAdminManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import library.domain.Member;

/**
 *
 * @author ppthgast
 */
public class MemberAdminUI extends javax.swing.JFrame {

    // These GUI components have been defined as fields, because they are
    // used in several methods.
    private JTextField textFieldMembershipNr;
    private JTextArea textAreaMemberInfo;
    private JButton removeMemberButton;
    private JButton searchButton;
            
    // The MemberAdminManager to delegate the real work (use cases!) to.
    private MemberAdminManager manager;

    // A reference to the last member that has been found. At start up and
    // in case a member could not be found for some membership nr, this
    // field has the value null.
    private Member currentMember;
    
    /**
     * Creates new form MemberAdminUI
     */
    public MemberAdminUI() {
        initComponents();
        setupFrame();
        
        manager = new MemberAdminManager();
        currentMember = null;
    }

    private void setupFrame()
    {
        setTitle("Member administration");
        
        // The layout is a Borderlayout with
        // North: search panel
        // Center: used to display information about the member; for this
        //         simple POC, it is just a multiline text box.
        // South: panel for operations on the currently displayed member.
        //        For this POC it is just the delete button; a possible extension
        //        is edit.
        // East + west: not used
        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout(5, 5));
        
        // Setup of the north-area
        JPanel searchPanel = createSearchPanel();

         // Setup of the center-area
        JPanel memberInfoPanel = new JPanel();
        GridLayout grid = new GridLayout();
        memberInfoPanel.setLayout(grid);
        
        textAreaMemberInfo = new JTextArea();
        memberInfoPanel.add(textAreaMemberInfo);
        textAreaMemberInfo.setText("");
        textAreaMemberInfo.setEditable(false);
        
        // Setup of the south-area
        removeMemberButton = new JButton("Schrijf lid uit");
        
        // Initially, there is no current member set, so the button to remove
        // a member should be disabled.
        removeMemberButton.setEnabled(false);
        
        JPanel memberOperationsPanel = createMemberOperationsPanel();
                
        contentPane.add(searchPanel, BorderLayout.NORTH);
        contentPane.add(memberInfoPanel, BorderLayout.CENTER);
        contentPane.add(memberOperationsPanel, BorderLayout.SOUTH);

        // Event handlers
        searchButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                int membershipNr = Integer.parseInt(textFieldMembershipNr.getText());
                doFindMember(membershipNr);
            }
        });      
 
        removeMemberButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                doRemoveMember();
            }
        });
        
        // Now let the layout managers do their job.
        pack();
        
        // At last, set the size of the window.
        setSize(300, 350);
    }
    
    private JPanel createSearchPanel()
    {
        // Make a panel with controls to be able to search a member based on
        // its membership number.
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        
        searchPanel.add(new JLabel("Voer lidnummer in:"));
        
        textFieldMembershipNr = new JTextField(10);
        textFieldMembershipNr.setSize(new Dimension(6, 20));
        searchPanel.add(textFieldMembershipNr);
        
        searchButton = new JButton("Zoek");
        searchButton.setSize(new Dimension(73, 23));
        searchPanel.add(searchButton);
        
        return searchPanel;
    }
    
    private JPanel createMemberOperationsPanel()
    {
        JPanel memberOperationsPanel = new JPanel();
        memberOperationsPanel.setLayout(
                new BoxLayout(memberOperationsPanel, BoxLayout.X_AXIS));
        memberOperationsPanel.add(removeMemberButton);
        
        return memberOperationsPanel;
    }
    
    private void doFindMember(int membershipNr)
    {
        currentMember = manager.findMember(membershipNr);
        String memberInfo = "Lid niet gevonden";
        boolean memberCanBeRemoved = false;
        
        if(currentMember != null)
        {
            String boekenGeleend = "nee";
            if(currentMember.hasLoans())
            {
                boekenGeleend = "ja";
            }
            
            String heeftReserveringen = "nee";
            if(currentMember.hasReservations())
            {
                heeftReserveringen = "ja";
            }
            
            memberInfo =
                    currentMember.getFirstname() + " " + currentMember.getLastname() +
                    "\n" +
                    currentMember.getStreet() + " " + currentMember.getHouseNumber() +
                    "\n" +
                    currentMember.getCity() +
                    "\n" +
                    "\n" +
                    "Boete: " + currentMember.getFine() +
                    "\n" +
                    "\n" +
                    "Heeft boeken geleend: " + boekenGeleend +
                    "\n" +
                    "Heeft reserveringen: " + heeftReserveringen;
            
            memberCanBeRemoved = currentMember.isRemovable();
        }
        // else memberInfo has already proper value. The button that removes a
        // member from the system needs to be disabled. No work needed for that
        // in the else since the value of memberCanBeRemoved is correct.
        
        removeMemberButton.setEnabled(memberCanBeRemoved);
        textAreaMemberInfo.setText(memberInfo);
    }
    
    private void doRemoveMember()
    {
        if(currentMember != null)
        {
            String message = "";
            boolean memberRemoved = manager.removeMember(currentMember);

            if(memberRemoved)
            {
                message = "Lid is succesvol uitgeschreven";
            }
            else
            {
                message = "Er is een fout opgetreden. Het lid is niet uitgeschreven";
            }

            textAreaMemberInfo.setText(message);

            // Reset the currentMember field, since the member it reffered
            // to has been removed from the system.
            currentMember = null;
            removeMemberButton.setEnabled(false);
        }
    }
            
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
