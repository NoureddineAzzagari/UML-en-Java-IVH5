package edu.avans.aei.ivh5.view.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventListener;

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
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import edu.avans.aei.ivh5.api.RemoteMemberAdminManagerIF;
import edu.avans.aei.ivh5.control.Controller;
import edu.avans.aei.ivh5.model.domain.ImmutableMember;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import edu.avans.aei.ivh5.view.ui.DataTableModel;

/**
 * User Interface class setting up the screen.
 * 
 * @author Robin Schellius
 */
public class LibraryUI implements ActionListener, EventListener  {

	public JFrame applicationFrame;
	private JTextField txtSearchBox;
	private JTextField txtFirstname;
	private JTextField txtLastname;
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
	// private String[] serverNames;
	
	// The controller handles all work that follows from events or actions.
	private Controller controller;
	
	// The datamodel to be displayed in the JTable.
	private DataTableModel dataTableModel;

	// The MemberAdminManager to delegate the real work (use cases!) to.
	private RemoteMemberAdminManagerIF manager;

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(LibraryUI.class);

	/**
	 * Constructor
	 * 
	 * @param mgr The manager referencing all business logic.
	 */
	public LibraryUI(RemoteMemberAdminManagerIF mgr,
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
		applicationFrame.setResizable(true);
		applicationFrame.setTitle("Library Information System");
		applicationFrame.setBounds(100, 100, 500, 350);
		applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		applicationFrame.getContentPane().setLayout(new BorderLayout(0, 0));

		// Container containing list- and detail information.
		JPanel memberInfoContainer = new JPanel();
		memberInfoContainer.add(setupMemberListPanel(), BorderLayout.NORTH);
		memberInfoContainer.add(setupMemberDetailPanel(), BorderLayout.CENTER);
		
		applicationFrame.getContentPane().add(setupSearchPanel(serverNames), BorderLayout.NORTH);
		applicationFrame.getContentPane().add(memberInfoContainer, BorderLayout.CENTER);
		applicationFrame.getContentPane().add(setupStatusInfoPanel(), BorderLayout.SOUTH);
	}
	
	/**
	 * 
	 * @return
	 */
	private JPanel setupSearchPanel(String[] servers) {
		
		logger.debug("setupSearchPanel");

		JPanel pnlSearch = new JPanel();
		pnlSearch.setBorder(new TitledBorder(
				new LineBorder(new Color(0, 0, 0)), "Search member",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlSearch.setPreferredSize(new Dimension(480, 55));
		FlowLayout flowLayout = (FlowLayout) pnlSearch.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);

		txtStatusText = new JTextField();
		txtStatusText.setHorizontalAlignment(SwingConstants.RIGHT);
		txtStatusText.setEditable(false);
		txtStatusText.setColumns(30);

		pnlSearch.add(new JLabel("Member Nr:"));

		txtSearchBox = new JTextField();
		pnlSearch.add(txtSearchBox);
		txtSearchBox.setColumns(8);

		btnSearch = new JButton("Search");
		// The actionPerformed method handles the action for this button.
		btnSearch.addActionListener(this);

		// Enable Enter-key as input for search button.
		applicationFrame.getRootPane().setDefaultButton(btnSearch);
		pnlSearch.add(btnSearch);

		cmbSelectServer = new JComboBox<String>();
		cmbSelectServer.setModel(new DefaultComboBoxModel<String>(servers));
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

		logger.debug("setupMemberListPanel");

		// The table containing the data
		tableMembers = new JTable(dataTableModel);
		String [] headers = new String[] {"Nr", "Firstname", "Lastname", "Library"};
		dataTableModel.setTableHeader(headers);
		
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
	 * 
	 * @return
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
	 * 
	 * @return
	 */
	private JPanel setupStatusInfoPanel() {
	
		logger.debug("setStatusInfoPanel");
	
		JPanel pnlStatusInfo = new JPanel();
		applicationFrame.getContentPane().add(pnlStatusInfo, BorderLayout.SOUTH);
		pnlStatusInfo.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		pnlStatusInfo.add(txtStatusText);
	
		return pnlStatusInfo;
	}

	/**
	 * Erase the contents of the Member detail panel, in order for new
	 * information to be displayed.
	 */
	private void eraseMemberDetails() {
		
		logger.debug("eraseMemberDetails");

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

	public DataTableModel getDataTableModel() {
		return dataTableModel;
	}

	public String getSelectedService() {
		return (String) cmbSelectServer.getSelectedItem();
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
			eraseMemberDetails();
	
			try {
				int membershipNr = Integer.parseInt(txtSearchBox.getText().trim());
				String selectedService = (String) cmbSelectServer.getSelectedItem();
	
				// The controller is responsible for doing the actual work 
				controller.doFindMember(hostname, selectedService, membershipNr);
			} catch (NumberFormatException ex) {
				logger.error("Wrong input, only numbers allowed");
				txtStatusText.setText("Wrong input, only numbers allowed.");
				txtSearchBox.setText("");
				txtSearchBox.requestFocus();
			}
		} else if (e.getSource() == btnListMembers) {
			try {
				String selectedService = (String) cmbSelectServer.getSelectedItem();
	
				// The controller is responsible for doing the actual work 
				controller.doFindAllMembers(hostname, selectedService);
			} catch (Exception ex) {
				logger.error("Error: " + ex.getMessage());
			}
		}
	}
}


