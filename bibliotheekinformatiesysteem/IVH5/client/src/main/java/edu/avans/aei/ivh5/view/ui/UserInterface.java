package edu.avans.aei.ivh5.view.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import edu.avans.aei.ivh5.api.RemoteMemberAdminManagerIF;
import edu.avans.aei.ivh5.control.Controller;
import edu.avans.aei.ivh5.model.domain.ImmutableMember;

/**
 * User Interface class setting up the screen.
 * 
 * @author Robin Schellius
 */
public class UserInterface {

	public JFrame applicationFrame;
	private JTextField txtSearchBox;
	private JTextField txtFirstname;
	private JTextField txtLastname;
	private JTextField txtStatusText;
	private JTextField txtStreetname;
	private JTextField txtCityname;
	private JTable tableMembers;
	private JLabel lblCity;
	private JComboBox<String> cmbSelectServer;
	private JComboBox<String> cmbSelectService;
	private Dimension preferredSize = new Dimension(480, 140);

	private String hostname;
	
	// The controller handles all work that follows from events or actions.
	private Controller controller;
	
	// The datamodel to be displayed in the JTable.
	private DataTableModel dataTableModel;

	// The MemberAdminManager to delegate the real work (use cases!) to.
	private RemoteMemberAdminManagerIF manager;

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(UserInterface.class);

	/**
	 * Constructor
	 * 
	 * @param mgr The manager referencing all business logic.
	 */
	public UserInterface(RemoteMemberAdminManagerIF mgr,
			String host, String[] servers) {
		
		logger.debug("Constructor (MemberAdminManager)");
		
		hostname = host;
		manager = mgr;
		
		// The controller handling (controlling) all actions resulting from the view (GUI).
		controller = new Controller(this, manager);

		// The datamodel containing the data to be displayed in the table.
		// Changing the data in the DataTableModel automatically updates the table.
		dataTableModel = new DataTableModel();

		initializeUserInterface(servers);
	}

	/**
	 * Initialize the user interface.
	 * 
	 * @param serverNames
	 *            Names of RMI servers found in the registry. 
	 */
	private void initializeUserInterface(String[] serverNames) {
		
		logger.debug("initializeUserInterface");

		applicationFrame = new JFrame();
		applicationFrame.setResizable(false);
		applicationFrame.setTitle("Library Information System");
		applicationFrame.setBounds(100, 100, 496, 480);
		applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		applicationFrame.getContentPane().setLayout(new BorderLayout(0, 0));

		// Top side: Search and Server information panels, collected in a container.		
		JPanel serverContainer = new JPanel();
		serverContainer.setLayout(new BorderLayout());
		serverContainer.add(setupServerPanel(serverNames), BorderLayout.NORTH);
		serverContainer.add(setupSearchPanel(), BorderLayout.SOUTH);

		// Center part: Member list- and detail information, collected in a container.
		JPanel memberInfoContainer = new JPanel();
		memberInfoContainer.add(setupMemberListPanel(), BorderLayout.NORTH);
		memberInfoContainer.add(setupMemberDetailPanel(), BorderLayout.CENTER);
		
		// Display the various user interface components at their right position.
		applicationFrame.getContentPane().add(serverContainer, BorderLayout.NORTH);
		applicationFrame.getContentPane().add(memberInfoContainer, BorderLayout.CENTER);
		applicationFrame.getContentPane().add(setupStatusInfoPanel(), BorderLayout.SOUTH);
	}
	
	/**
	 * Setup the part of the screen that displays search functionality.
	 * 
	 * @return The created panel.
	 */
	private JPanel setupSearchPanel() {
		
		logger.debug("setupSearchPanel");

		JPanel pnlSearch = new JPanel();
		pnlSearch.setBorder(new TitledBorder(
				new LineBorder(new Color(0, 0, 0)), "Search member",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlSearch.setPreferredSize(new Dimension(480, 55));
		FlowLayout flowLayout = (FlowLayout) pnlSearch.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);

		pnlSearch.add(new JLabel("Member Nr:"));

		txtSearchBox = new JTextField();
		txtSearchBox.setColumns(8);
		pnlSearch.add(txtSearchBox);

		JButton btnSearch = new JButton("Search");
		// The controller handles the action for this button.
		btnSearch.addActionListener(controller);
		// Set a name for the command of this button, so we can retrieve 
		// the button in the Controller class.
		btnSearch.setActionCommand("SEARCH");
		// Enable Enter-key as input for search button.
		applicationFrame.getRootPane().setDefaultButton(btnSearch);
		pnlSearch.add(btnSearch);

		return pnlSearch;
	}

	/**
	 * Setup the part of the screen that displays search functionality.
	 * 
	 * @return The created panel.
	 */
	private JPanel setupServerPanel(String[] services) {
		
		logger.debug("setupServerPanel");

		JPanel pnlServerInfo = new JPanel();
		pnlServerInfo.setBorder(new TitledBorder(
				new LineBorder(new Color(0, 0, 0)), "Select server/service",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlServerInfo.setPreferredSize(new Dimension(480, 55));
		FlowLayout flowLayout = (FlowLayout) pnlServerInfo.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);

		pnlServerInfo.add(new JLabel("Server:"));

		JComboBox<String> cmbSelectServer = new JComboBox<String>();
		String[] hosts = {"localhost", "127.0.0.1", "192.168.1.1"};
		cmbSelectServer.setModel(new DefaultComboBoxModel<String>(hosts));

		pnlServerInfo.add(cmbSelectServer);

		JButton btnConnect = new JButton("Connect");
		// The controller handles the action for this button.
		btnConnect.addActionListener(controller);
		// Set a name for the command of this button, so we can retrieve 
		// the button in the Controller class.
		btnConnect.setActionCommand("CONNECT_TO_SEVER");
		pnlServerInfo.add(btnConnect);

		pnlServerInfo.add(new JLabel("Service:"));

		cmbSelectService = new JComboBox<String>();
		cmbSelectService.setModel(new DefaultComboBoxModel<String>(services));
		pnlServerInfo.add(cmbSelectService);

		JButton btnListMembers = new JButton("List");
		// The controller handles the action for this button.
		btnListMembers.addActionListener(controller);
		// Set a name for the command of this button, so we can retrieve 
		// the button in the Controller class.
		btnListMembers.setActionCommand("LIST");
		pnlServerInfo.add(btnListMembers);
		

		return pnlServerInfo;
	}

	/**
	 * Setup the part of the screen that displays the list of members that were found.
	 * 
	 * @return The created panel.
	 */
	private JComponent setupMemberListPanel() {

		logger.debug("setupMemberListPanel");

		// The table containing the data
		tableMembers = new JTable(dataTableModel);
		String [] headers = new String[] {"Nr", "Firstname", "Lastname", "Library"};
		dataTableModel.setTableHeader(headers);
		// The table does not display well without contents, so 
		// we put some empty strings in the data model.
		String[][] initialValues = new String[][] { {"","","",""} };
		dataTableModel.setValues(initialValues);
		
		TableColumn column = tableMembers.getColumnModel().getColumn(0);
	    column.setPreferredWidth(6);

	    // Handle row selection events.
	    // User can only select one row in the table, not multiple.
		tableMembers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    tableMembers.getSelectionModel().addListSelectionListener(controller);
	    
		// The scrollable pane that contains the table
		JScrollPane pnlMemberList = new JScrollPane(tableMembers);
		pnlMemberList.setPreferredSize(preferredSize);
		pnlMemberList.setBorder(new TitledBorder(new LineBorder(new Color(0, 0,
				0)), "Member list", TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		
		return pnlMemberList;
	}
	
	/**
	 * Setup the part of the screen that displays detailed information 
	 * of a single selected member.
	 * 
	 * @return The created panel.
	 */
	private JPanel setupMemberDetailPanel() {

		logger.debug("setupMemberDetailPanel");

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

		JLabel lblFirstname = new JLabel("FirstName");
		GridBagConstraints gbc_lblFirstname = new GridBagConstraints();
		gbc_lblFirstname.anchor = GridBagConstraints.EAST;
		gbc_lblFirstname.insets = insets;
		gbc_lblFirstname.gridx = 0;
		gbc_lblFirstname.gridy = 0;
		pnlMemberDetails.add(lblFirstname, gbc_lblFirstname);
		lblFirstname.setLabelFor(txtFirstname);

		txtFirstname = new JTextField();
		txtFirstname.setEditable(false);
		GridBagConstraints gbc_txtFirstName = new GridBagConstraints();
		gbc_txtFirstName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFirstName.anchor = GridBagConstraints.WEST;
		gbc_txtFirstName.insets = insets;
		gbc_txtFirstName.gridx = 1;
		gbc_txtFirstName.gridy = 0;
		pnlMemberDetails.add(txtFirstname, gbc_txtFirstName);
		txtFirstname.setColumns(20);

		JLabel lblLastname = new JLabel("Lastname");
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

		JLabel lblStreet = new JLabel("Street");
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

		lblCity = new JLabel("City");
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
	 * Setup the status bar at the bottom of the screen. The status bar can display short
	 * messages about the status or result of an action.
	 * 
	 * @return The created panel.
	 */
	private JPanel setupStatusInfoPanel() {
	
		logger.debug("setStatusInfoPanel");
	
		JPanel pnlStatusInfo = new JPanel();
		applicationFrame.getContentPane().add(pnlStatusInfo, BorderLayout.SOUTH);
		pnlStatusInfo.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

		txtStatusText = new JTextField();
		txtStatusText.setHorizontalAlignment(SwingConstants.RIGHT);
		txtStatusText.setEditable(false);
		txtStatusText.setColumns(30);
		pnlStatusInfo.add(txtStatusText);
	
		return pnlStatusInfo;
	}

	/**
	 * Display member details.
	 * 
	 * @param member The member to be displayed.
	 */
	public void setMemberDetails(ImmutableMember member) {
		
		logger.debug("setMemberDetails");

		txtFirstname.setText(member.getFirstname());
		txtLastname.setText(member.getLastname());
		txtCityname.setText(member.getCity());
		txtStreetname.setText(member.getStreet());
		// More information to be displayed.
	}
	
	/**
	 * Display member details.
	 * 
	 * @param member The member to be displayed.
	 */
	public void setMemberListData(ArrayList<String> memberData) {
		
		logger.debug("setMemberListData");

		if(memberData != null) {
			String[][] data = new String[memberData.size()][4];
			for(int i = 0; i < memberData.size(); i++ ) {
				data[i][0]= memberData.get(i);				
			}
			dataTableModel.setValues(data);
		}
	}

	/**
	 * Display the given text in the status bar at the bottom of the user interface.
	 * 
	 * @param text Text to be displayed.
	 */
	public void setStatusText(String text) {
		txtStatusText.setText(text);
	}

	/**
	 * Set the text in the search box, and set focus to it.
	 */
	public void setSearchBoxText(String text) {
		txtSearchBox.setText(text);
		txtSearchBox.requestFocus();
	}

	public DataTableModel getDataTableModel() {
		return dataTableModel;
	}

	public String getSelectedService() {
		return (String) cmbSelectService.getSelectedItem();
	}

	public String getHostname() {
		return hostname;
	}

	public String getSearchValue() {
		return txtSearchBox.getText();
	}

	/**
	 * Erase the contents of the Member detail panel, in order for new
	 * information to be displayed.
	 */
	public void eraseMemberDetails() {
		
		logger.debug("eraseMemberDetails");
	
		txtStatusText.setText("");
		txtFirstname.setText("");
		txtLastname.setText("");
		txtCityname.setText("");
		txtStreetname.setText("");
	}
}


