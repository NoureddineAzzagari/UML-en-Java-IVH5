package edu.avans.aei.ivh5.view.ui;

// import QueryTableModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import edu.avans.aei.ivh5.api.RemoteMemberAdminManagerIF;
import edu.avans.aei.ivh5.control.UIController;
import edu.avans.aei.ivh5.model.domain.ImmutableMember;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

/**
 * Alternative User Interface class setting up the screen.
 * 
 * @author Robin Schellius
 */
public class LibraryUI implements ActionListener, EventListener, ListSelectionListener  {

	public JFrame applicationFrame;
	private JTextField txtSearchBox;
	private JTextField txtFirstname;
	private JTextField txtLastname;
	private JLabel lblSearch;
	private JButton btnSearch;
	private JButton btnListMembers;
	private JTable tableMembers;
	private JTextField txtStatusText;
	private JTextField txtStreetname;
	private JLabel lblCity;
	private JTextField txtCityname;
	private JComboBox<String> cmbSelectServer;
	private Dimension preferredSize = new Dimension(480, 100);

	private String hostname;
	private String[] serverNames;
	
	// The controller handles all work that follows from events or actions.
	private UIController controller;

	// The MemberAdminManager to delegate the real work (use cases!) to.
	private RemoteMemberAdminManagerIF manager;

	// A reference to the last member that has been found. At start up and
	// in case a member could not be found for some membership nr, this
	// field has the value null.
	private ImmutableMember currentMember;

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(LibraryUI.class);

	/**
	 * Constructor
	 * 
	 * @param memberAdminmanager
	 *            The manager referencing all business logic.
	 */
	public LibraryUI(RemoteMemberAdminManagerIF mgr,
			String host, String[] servers) {
		
		logger.debug("Constructor (MemberAdminManager)");
		
		hostname = host;
		manager = mgr;
		currentMember = null;
		
		controller = new UIController(this, manager);
		initializeUserInterface(servers);
	}

	/**
	 * Initialize the user interface.
	 * 
	 * @param serverNames
	 *            Names of RMI servers found in the registry. Selecting a name
	 *            creates a connection to that server. Any following findMember
	 *            actions will be sent to that server.
	 */
	private void initializeUserInterface(String[] serverNames) {

		applicationFrame = new JFrame();
		applicationFrame.setResizable(true);
		applicationFrame.setTitle("Bibliotheek Informatie Systeem");
		applicationFrame.setBounds(100, 100, 500, 350);
		applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		applicationFrame.getContentPane().setLayout(new BorderLayout(0, 0));

		// Container met list- en detailinformatie.
		JPanel memberInfoContainer = new JPanel();
		memberInfoContainer.add(setupMemberListPanel(), BorderLayout.NORTH);
		memberInfoContainer.add(setupMemberDetailPanel(), BorderLayout.CENTER);
		
		applicationFrame.getContentPane().add(setupSearchPanel(), BorderLayout.NORTH);
		applicationFrame.getContentPane().add(memberInfoContainer, BorderLayout.CENTER);
		applicationFrame.getContentPane().add(setupStatusInfoPanel(), BorderLayout.SOUTH);
	}
	
	/**
	 * 
	 * @return
	 */
	private JPanel setupSearchPanel() {
		JPanel pnlSearch = new JPanel();
		pnlSearch.setBorder(new TitledBorder(
				new LineBorder(new Color(0, 0, 0)), "Zoek lid",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlSearch.setPreferredSize(new Dimension(480, 55));
		FlowLayout flowLayout = (FlowLayout) pnlSearch.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);

		txtStatusText = new JTextField();
		txtStatusText.setHorizontalAlignment(SwingConstants.RIGHT);
		txtStatusText.setEditable(false);
		txtStatusText.setColumns(30);

		lblSearch = new JLabel("Zoekterm:");
		pnlSearch.add(lblSearch);

		txtSearchBox = new JTextField();
		pnlSearch.add(txtSearchBox);
		txtSearchBox.setColumns(8);

		btnSearch = new JButton("Zoek lid");
		// The actionPerformed method handles the action for this button.
		btnSearch.addActionListener(this);

		// Enable Enter-key as input for search button.
		applicationFrame.getRootPane().setDefaultButton(btnSearch);
		pnlSearch.add(btnSearch);

		cmbSelectServer = new JComboBox<String>();
		cmbSelectServer.setModel(new DefaultComboBoxModel<String>(serverNames));
		pnlSearch.add(cmbSelectServer);

		btnListMembers = new JButton("List");
		// The actionPerformed method handles the action for this button.
		btnListMembers.addActionListener(this);
		pnlSearch.add(btnListMembers);
		
		return pnlSearch;
	}

	/**
	 * 
	 * @return
	 */
	private JComponent setupMemberListPanel() {

		// The datamodel containing the data to be displayed in the table.
		// Changing the data in the DataTableModel automatically updates the table.
		DataTableModel qtm = new DataTableModel();
		String[][] rs = new String[][] {
				{"1000","Robin","Schellius", "Breda"},
				{"1001","Erco","Argante", "Oosterhout"},
				{"1002","Pascal","van Gastel", "Breda"},
				{"1003","Erco","Argante", "Oosterhout"},
				{"1004","Pascal","van Gastel", "Breda"},
				{"1005","Erco","Argante", "Oosterhout"},
				{"1006","Pascal","van Gastel", "Breda"},
				};
		qtm.setValues(rs);

		// The table containing the data
		tableMembers = new JTable(qtm);
		TableColumn column = null;
	    column = tableMembers.getColumnModel().getColumn(0);
	    column.setPreferredWidth(6);
		
		tableMembers.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		// The scrollable pane that contains the table
		JScrollPane pnlMemberList = new JScrollPane(tableMembers);
		pnlMemberList.setPreferredSize(preferredSize);
		pnlMemberList.setBorder(new TitledBorder(new LineBorder(new Color(0, 0,
				0)), "Leden overzicht", TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		
		// pnlMemberList.addListSelectionListener(new UIController());

		return pnlMemberList;
	}
	
	/**
	 * 
	 * @return
	 */
	private JPanel setupMemberDetailPanel() {

		JPanel pnlMemberDetails = new JPanel();
		pnlMemberDetails.setBorder(new TitledBorder(new LineBorder(new Color(0,
				0, 0)), "Lid details", TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		pnlMemberDetails.setPreferredSize(preferredSize);

		GridBagLayout gbl_pnlMemberDetails = new GridBagLayout();
		gbl_pnlMemberDetails.columnWidths = new int[] { 0, 40, 40, 40, 40 };
		gbl_pnlMemberDetails.rowHeights = new int[] { 20, 0, 0, 0, 0 };
		gbl_pnlMemberDetails.columnWeights = new double[] { 0.0, 1.0, 0.0, 2.0,
				0.0 };
		gbl_pnlMemberDetails.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		pnlMemberDetails.setLayout(gbl_pnlMemberDetails);

		Insets insets = new Insets(0, 0, 5, 5);

		JLabel lblFirstname = new JLabel("Voornaam");
		GridBagConstraints gbc_lblFirstname = new GridBagConstraints();
		gbc_lblFirstname.anchor = GridBagConstraints.EAST;
		gbc_lblFirstname.insets = insets;
		gbc_lblFirstname.gridx = 0;
		gbc_lblFirstname.gridy = 0;
		pnlMemberDetails.add(lblFirstname, gbc_lblFirstname);
		lblFirstname.setLabelFor(txtFirstname);

		txtFirstname = new JTextField();
		txtFirstname.setEditable(false);
		GridBagConstraints gbc_txtVoornaam = new GridBagConstraints();
		gbc_txtVoornaam.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtVoornaam.anchor = GridBagConstraints.WEST;
		gbc_txtVoornaam.insets = insets;
		gbc_txtVoornaam.gridx = 1;
		gbc_txtVoornaam.gridy = 0;
		pnlMemberDetails.add(txtFirstname, gbc_txtVoornaam);
		txtFirstname.setColumns(20);

		JLabel lblLastname = new JLabel("Achternaam");
		GridBagConstraints gbc_lblLastname = new GridBagConstraints();
		gbc_lblLastname.anchor = GridBagConstraints.EAST;
		gbc_lblLastname.insets = insets;
		gbc_lblLastname.gridx = 2;
		gbc_lblLastname.gridy = 0;
		pnlMemberDetails.add(lblLastname, gbc_lblLastname);
		lblLastname.setLabelFor(txtLastname);

		txtLastname = new JTextField();
		txtLastname.setEditable(false);
		GridBagConstraints gbc_txtAchternaam = new GridBagConstraints();
		gbc_txtAchternaam.gridwidth = 2;
		gbc_txtAchternaam.fill = GridBagConstraints.BOTH;
		gbc_txtAchternaam.insets = insets;
		gbc_txtAchternaam.anchor = GridBagConstraints.WEST;
		gbc_txtAchternaam.gridx = 3;
		gbc_txtAchternaam.gridy = 0;
		pnlMemberDetails.add(txtLastname, gbc_txtAchternaam);
		txtLastname.setColumns(20);

		JLabel lblStreet = new JLabel("Straat");
		GridBagConstraints gbc_lblStreet = new GridBagConstraints();
		gbc_lblStreet.anchor = GridBagConstraints.EAST;
		gbc_lblStreet.insets = insets;
		gbc_lblStreet.gridx = 0;
		gbc_lblStreet.gridy = 1;
		pnlMemberDetails.add(lblStreet, gbc_lblStreet);

		txtStreetname = new JTextField();
		txtStreetname.setEditable(false);
		GridBagConstraints gbc_txtStreetname = new GridBagConstraints();
		gbc_txtStreetname.gridwidth = 2;
		gbc_txtStreetname.insets = insets;
		gbc_txtStreetname.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtStreetname.gridx = 1;
		gbc_txtStreetname.gridy = 1;
		pnlMemberDetails.add(txtStreetname, gbc_txtStreetname);
		txtStreetname.setColumns(10);

		lblCity = new JLabel("Stad");
		lblCity.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblCity = new GridBagConstraints();
		gbc_lblCity.anchor = GridBagConstraints.EAST;
		gbc_lblCity.insets = insets;
		gbc_lblCity.gridx = 0;
		gbc_lblCity.gridy = 2;
		pnlMemberDetails.add(lblCity, gbc_lblCity);

		txtCityname = new JTextField();
		txtCityname.setEditable(false);
		GridBagConstraints gbc_txtCityname = new GridBagConstraints();
		gbc_txtCityname.insets = insets;
		gbc_txtCityname.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCityname.gridx = 1;
		gbc_txtCityname.gridy = 2;
		pnlMemberDetails.add(txtCityname, gbc_txtCityname);
		txtCityname.setColumns(10);
		
		return pnlMemberDetails;
	}

	/**
	 * Erase the contents of the Member detail panel, in order for new
	 * information to be displayed.
	 */
	private void eraseMemberDetails() {
		txtStatusText.setText("");
		txtFirstname.setText("");
		txtLastname.setText("");
		txtCityname.setText("");
		txtStreetname.setText("");
	}

	/**
	 * Display member details.
	 * 
	 * @param member The member to be displayed.
	 */
	public void setMemberDetails(ImmutableMember member) {
		
		txtFirstname.setText(member.getFirstname());
		txtLastname.setText(member.getLastname());
		txtCityname.setText(member.getCity());
		txtStreetname.setText(member.getStreet());
		// More information to be displayed.
	}
	
	/**
	 * 
	 */
	public void setStatusText(String text) {
		txtStatusText.setText(text);
	}

	/**
	 * 
	 * @return
	 */
	private JPanel setupStatusInfoPanel() {
		JPanel pnlStatusInfo = new JPanel();
		applicationFrame.getContentPane().add(pnlStatusInfo, BorderLayout.SOUTH);
		pnlStatusInfo.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		pnlStatusInfo.add(txtStatusText);

		return pnlStatusInfo;
	}

	/**
	 * Performs the corresponding action for a button. This method can handle
	 * actions for each button in the window. The method itself selects the
	 * appropriate handling based on the button clicked. Provide the appropriate
	 * handling when adding a button.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btnSearch) {
			// erase the detail panel in order to display new information.
			eraseMemberDetails();

			try {
				int membershipNr = Integer.parseInt(txtSearchBox.getText().trim());
				String selectedService = (String) cmbSelectServer.getSelectedItem();
				logger.debug("Finding member " + membershipNr);
				controller.doFindMember(this.hostname, selectedService, membershipNr);
			} catch (NumberFormatException ex) {
				logger.error("Wrong input, only numbers allowed");
				txtStatusText.setText("Het lidnummer bestaat uit 4 cijfers.");
				txtSearchBox.setText("");
				txtSearchBox.requestFocus();
			}
		} else if (e.getSource() == btnListMembers) {
			try {
				String selectedService = (String) cmbSelectServer.getSelectedItem();
				controller.doFindAllMembers(this.hostname, selectedService);
			} catch (Exception ex) {
				logger.error("Error: " + ex.getMessage());
			}
		}
	}

	/**
	 * Handle selectionchanges in the memberlist. When the selection changes we
	 * try to find the corresponding Member on the chosen server.
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		logger.debug("ListSelectionEvent");

//		if (e.getValueIsAdjusting() == false) {
//			eraseMemberDetailPanel();
//			if (listMembers.getSelectedIndex() == -1) {
//				// No selection
//			} else {
//				// Selection made, find the selected member and display details.
//				String name = listModel.getElementAt(
//						listMembers.getSelectedIndex()).toString();
//				logger.debug("Selected member is " + name);
//				doFindMember(Integer.parseInt(name));
//			}
//		}
	}

}
