package library.presentation;

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
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import library.domain.ImmutableMember;
import nl.avans.aei.ivh5.library.api.RemoteMemberAdminManagerIF;

import org.apache.log4j.Logger;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

/**
 * Alternative User Interface class setting up the screen.
 * 
 * @author Robin Schellius
 */
public class LibraryUI implements ActionListener, ListSelectionListener {

	public JFrame frmLibraryInformationSystem;
	private JTextField txtSearchBox;
	private JTextField txtFirstname;
	private JTextField txtLastname;
	private JLabel lblSearch;
	private JButton btnSearch;
	private JButton btnListMembers;
	private JList<String> listMembers;
	private DefaultListModel<String> listModel;

	// The MemberAdminManager to delegate the real work (use cases!) to.
	private RemoteMemberAdminManagerIF manager;

	// A reference to the last member that has been found. At start up and
	// in case a member could not be found for some membership nr, this
	// field has the value null.
	private ImmutableMember currentMember;
	private JTextField txtStatusText;
	private JTextField txtStreetname;
	private JLabel lblCity;
	private JTextField txtCityname;
	private JComboBox<String> cmbSelectServer;
	private String hostname;

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(LibraryUI.class);

	/**
	 * Constructor
	 * 
	 * @param memberAdminmanager
	 *            The manager referencing all business logic.
	 */
	public LibraryUI(RemoteMemberAdminManagerIF remoteMemberAdminmanager,
			String hostname, String[] serverNames) {
		logger.debug("Constructor (MemberAdminManager)");
		this.hostname = hostname;
		initialize(serverNames);

		manager = remoteMemberAdminmanager;
		currentMember = null;
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @param serverNames
	 *            Names of RMI servers found in the registry. Selecting a name
	 *            creates a connection to that server. Any following findMember
	 *            actions will be sent to that server.
	 */
	private void initialize(String[] serverNames) {

		frmLibraryInformationSystem = new JFrame();
		frmLibraryInformationSystem.setResizable(true);
		frmLibraryInformationSystem.setTitle("Bibliotheek Informatie Systeem");
		frmLibraryInformationSystem.setBounds(100, 100, 500, 350);
		frmLibraryInformationSystem
				.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLibraryInformationSystem.getContentPane().setLayout(
				new BorderLayout(0, 0));

		Dimension preferredSize = new Dimension(480, 100);

		// --------------------------------------------------------------------------------
		// Zoekpanel.
		//
		JPanel pnlSearch = new JPanel();
		pnlSearch.setBorder(new TitledBorder(
				new LineBorder(new Color(0, 0, 0)), "Zoek lid",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlSearch.setPreferredSize(new Dimension(480, 55));
		FlowLayout flowLayout = (FlowLayout) pnlSearch.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		frmLibraryInformationSystem.getContentPane().add(pnlSearch,
				BorderLayout.NORTH);

		txtStatusText = new JTextField();
		txtStatusText.setHorizontalAlignment(SwingConstants.RIGHT);
		txtStatusText.setEditable(false);
		txtStatusText.setColumns(30);

		lblSearch = new JLabel("Zoekterm:");
		pnlSearch.add(lblSearch);

		txtSearchBox = new JTextField();
		pnlSearch.add(txtSearchBox);
		txtSearchBox.setColumns(15);

		btnSearch = new JButton("Zoek lid");
		// The actionPerformed method handles the action for this button.
		btnSearch.addActionListener(this);
		pnlSearch.add(btnSearch);

		cmbSelectServer = new JComboBox<String>();
		cmbSelectServer.setModel(new DefaultComboBoxModel<String>(serverNames));
		pnlSearch.add(cmbSelectServer);

		btnListMembers = new JButton("List");
		// The actionPerformed method handles the action for this button.
		btnListMembers.addActionListener(this);
		pnlSearch.add(btnListMembers);

		// --------------------------------------------------------------------------------
		// Scrollable lijst met members.
		//
		listModel = new DefaultListModel<String>();
		// TODO: hier de lijst met members op de server ophalen.
		// Dummy-waarden vervangen!
		listModel.addElement("0999");
		listModel.addElement("1000");

		listMembers = new JList<String>(listModel);
		listMembers
				.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listMembers.setLayoutOrientation(JList.VERTICAL);
		listMembers.setVisibleRowCount(-1);
		JScrollPane pnlMemberList = new JScrollPane(listMembers);
		pnlMemberList.setPreferredSize(preferredSize);
		pnlMemberList.setBorder(new TitledBorder(new LineBorder(new Color(0, 0,
				0)), "Leden overzicht", TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		listMembers.addListSelectionListener(this);

		// --------------------------------------------------------------------------------
		// Details van een geselecteerde member.
		//
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

		// --------------------------------------------------------------------------------
		// Container met list- en detailinformatie.
		//
		JPanel pnlMemberInfo = new JPanel();
		pnlMemberInfo.add(pnlMemberList, BorderLayout.NORTH);
		pnlMemberInfo.add(pnlMemberDetails, BorderLayout.CENTER);
		frmLibraryInformationSystem.getContentPane().add(pnlMemberInfo,
				BorderLayout.CENTER);

		// --------------------------------------------------------------------------------
		// Statusinfo
		//
		JPanel pnlStatus = new JPanel();
		frmLibraryInformationSystem.getContentPane().add(pnlStatus,
				BorderLayout.SOUTH);
		pnlStatus.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		pnlStatus.add(txtStatusText);

		// Enable Enter-key as input for search button.
		frmLibraryInformationSystem.getRootPane().setDefaultButton(btnSearch);

	}

	/**
	 * Find the member identified by membershipNr and display its information.
	 * 
	 * @param membershipNr
	 */
	private boolean doFindMember(int membershipNr) {
		logger.debug("doFindMember " + membershipNr);
		boolean memberFound = false;

		if (manager == null) {
			logger.error("Manager is null! ");
		} else {
			try {
				currentMember = manager.findMember(membershipNr);
			} catch (RemoteException e) {
				logger.error("Error: " + e.getMessage());
			}

			if (currentMember != null) {
				txtFirstname.setText(currentMember.getFirstname());
				txtLastname.setText(currentMember.getLastname());
				txtStreetname.setText(currentMember.getStreet() + " "
						+ currentMember.getHouseNumber());
				txtCityname.setText(currentMember.getCity());

				if (currentMember.hasLoans()) {
					// boekenGeleend = "ja";
				}

				if (currentMember.hasReservations()) {
					// heeftReserveringen = "ja";
				}
				memberFound = true;
			} else {
				txtStatusText.setText("Lidnummer " + membershipNr
						+ " niet gevonden");
			}
		}
		return memberFound;
	}

	/**
	 * 
	 * @param membershipNr
	 */
	private void doFindAllMembers(String hostname, String servicename) {
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
		        listModel.removeAllElements();
				for(String memberID : memberIDs) {
					listModel.addElement(memberID);
				}
			} else {
				logger.debug("No members found on the given service.");
				txtStatusText.setText("Geen informatie gevonden.");
			}
		}
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
			eraseMemberDetailPanel();

			try {
				int membershipNr = Integer.parseInt(txtSearchBox.getText()
						.trim());
				logger.debug("Finding member " + membershipNr);
				doFindMember(membershipNr);
			} catch (NumberFormatException ex) {
				logger.error("Wrong input, only numbers allowed");
				txtStatusText.setText("Het lidnummer bestaat uit 4 cijfers.");
				txtSearchBox.setText("");
				txtSearchBox.requestFocus();
			}
		} else if (e.getSource() == btnListMembers) {
			try {
				String selectedService = (String) cmbSelectServer.getSelectedItem();
				doFindAllMembers(this.hostname, selectedService);
			} catch (Exception ex) {
				logger.error("Error: " + ex.getMessage());
				// ex.printStackTrace();
			}
		}
	}

	/**
	 * Erase the contents of the Member detail panel, in order for new
	 * information to be displayed.
	 */
	private void eraseMemberDetailPanel() {
		txtStatusText.setText("");
		txtFirstname.setText("");
		txtLastname.setText("");
		txtCityname.setText("");
		txtStreetname.setText("");
	}

	/**
	 * Handle selectionchanges in the memberlist. When the selection changes we
	 * try to find the corresponding Member on the chosen server.
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {
			eraseMemberDetailPanel();
			if (listMembers.getSelectedIndex() == -1) {
				// No selection
			} else {
				// Selection made, find the selected member and display details.
				String name = listModel.getElementAt(
						listMembers.getSelectedIndex()).toString();
				logger.debug("Selected member is " + name);
				doFindMember(Integer.parseInt(name));
			}
		}
	}
}
