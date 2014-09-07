package library.presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import library.businesslogic.MemberAdminManager;
import library.domain.ImmutableMember;

import org.apache.log4j.Logger;

/**
 * Alternative User Interface class setting up the screen.
 * 
 * @author Robin Schellius
 */
public class LibraryUI implements ActionListener {

	public JFrame frmLibraryInformationSystem;
	private JTextField txtSearchBox;
	private JTextField txtFirstname;
	private JTextField txtLastname;
	private JLabel lblSearch;
	private JButton btnSearch;

	// The MemberAdminManager to delegate the real work (use cases!) to.
	private MemberAdminManager manager;

	// A reference to the last member that has been found. At start up and
	// in case a member could not be found for some membership nr, this
	// field has the value null.
	private ImmutableMember currentMember;
	private JTextField txtStatusText;

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(LibraryUI.class);
	private JTextField txtStreetname;
	private JLabel lblCity;
	private JTextField txtCityname;

	/**
	 * Constructor
	 * 
	 * @param memberAdminmanager
	 *            The manager referencing all business logic.
	 */
	public LibraryUI(MemberAdminManager memberAdminmanager) {
		logger.debug("Constructor (MemberAdminManager)");
		initialize();

		manager = memberAdminmanager;
		currentMember = null;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLibraryInformationSystem = new JFrame();
		frmLibraryInformationSystem.setResizable(false);
		frmLibraryInformationSystem.setTitle("Bibliotheek Informatie Systeem");
		frmLibraryInformationSystem.setBounds(100, 100, 492, 354);
		frmLibraryInformationSystem
				.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLibraryInformationSystem.getContentPane().setLayout(
				new BorderLayout(0, 0));

		JPanel pnlSearch = new JPanel();
		pnlSearch.setBorder(new TitledBorder(
				new LineBorder(new Color(0, 0, 0)), "Zoek lid",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
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

		JPanel pnlMainContent = new JPanel();
		pnlMainContent.setBorder(new TitledBorder(new LineBorder(new Color(0,
				0, 0)), "Member", TitledBorder.LEADING, TitledBorder.TOP, null,
				null));
		frmLibraryInformationSystem.getContentPane().add(pnlMainContent,
				BorderLayout.CENTER);
		GridBagLayout gbl_pnlMainContent = new GridBagLayout();
		gbl_pnlMainContent.columnWidths = new int[] {0, 40, 40, 40, 40};
		gbl_pnlMainContent.rowHeights = new int[] { 20, 0, 0, 0, 0 };
		gbl_pnlMainContent.columnWeights = new double[] { 0.0, 1.0, 0.0, 2.0,
				0.0 };
		gbl_pnlMainContent.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		pnlMainContent.setLayout(gbl_pnlMainContent);

		JLabel lblFirstname = new JLabel("Voornaam");
		GridBagConstraints gbc_lblFirstname = new GridBagConstraints();
		gbc_lblFirstname.anchor = GridBagConstraints.EAST;
		gbc_lblFirstname.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirstname.gridx = 0;
		gbc_lblFirstname.gridy = 0;
		pnlMainContent.add(lblFirstname, gbc_lblFirstname);
		lblFirstname.setLabelFor(txtFirstname);

		txtFirstname = new JTextField();
		txtFirstname.setEditable(false);
		GridBagConstraints gbc_txtVoornaam = new GridBagConstraints();
		gbc_txtVoornaam.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtVoornaam.anchor = GridBagConstraints.WEST;
		gbc_txtVoornaam.insets = new Insets(0, 0, 5, 5);
		gbc_txtVoornaam.gridx = 1;
		gbc_txtVoornaam.gridy = 0;
		pnlMainContent.add(txtFirstname, gbc_txtVoornaam);
		txtFirstname.setColumns(20);

		JLabel lblLastname = new JLabel("Achternaam");
		GridBagConstraints gbc_lblLastname = new GridBagConstraints();
		gbc_lblLastname.anchor = GridBagConstraints.EAST;
		gbc_lblLastname.insets = new Insets(0, 0, 5, 5);
		gbc_lblLastname.gridx = 2;
		gbc_lblLastname.gridy = 0;
		pnlMainContent.add(lblLastname, gbc_lblLastname);
		lblLastname.setLabelFor(txtLastname);

		txtLastname = new JTextField();
		txtLastname.setEditable(false);
		GridBagConstraints gbc_txtAchternaam = new GridBagConstraints();
		gbc_txtAchternaam.gridwidth = 2;
		gbc_txtAchternaam.fill = GridBagConstraints.BOTH;
		gbc_txtAchternaam.insets = new Insets(0, 0, 5, 0);
		gbc_txtAchternaam.anchor = GridBagConstraints.WEST;
		gbc_txtAchternaam.gridx = 3;
		gbc_txtAchternaam.gridy = 0;
		pnlMainContent.add(txtLastname, gbc_txtAchternaam);
		txtLastname.setColumns(20);

		JLabel lblStreet = new JLabel("Straat");
		GridBagConstraints gbc_lblStreet = new GridBagConstraints();
		gbc_lblStreet.anchor = GridBagConstraints.EAST;
		gbc_lblStreet.insets = new Insets(0, 0, 5, 5);
		gbc_lblStreet.gridx = 0;
		gbc_lblStreet.gridy = 1;
		pnlMainContent.add(lblStreet, gbc_lblStreet);

		txtStreetname = new JTextField();
		txtStreetname.setEditable(false);
		GridBagConstraints gbc_txtStreetname = new GridBagConstraints();
		gbc_txtStreetname.gridwidth = 2;
		gbc_txtStreetname.insets = new Insets(0, 0, 5, 5);
		gbc_txtStreetname.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtStreetname.gridx = 1;
		gbc_txtStreetname.gridy = 1;
		pnlMainContent.add(txtStreetname, gbc_txtStreetname);
		txtStreetname.setColumns(10);

		lblCity = new JLabel("Stad");
		lblCity.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblCity = new GridBagConstraints();
		gbc_lblCity.anchor = GridBagConstraints.EAST;
		gbc_lblCity.insets = new Insets(0, 0, 5, 5);
		gbc_lblCity.gridx = 0;
		gbc_lblCity.gridy = 2;
		pnlMainContent.add(lblCity, gbc_lblCity);

		txtCityname = new JTextField();
		txtCityname.setEditable(false);
		GridBagConstraints gbc_txtCityname = new GridBagConstraints();
		gbc_txtCityname.insets = new Insets(0, 0, 5, 5);
		gbc_txtCityname.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCityname.gridx = 1;
		gbc_txtCityname.gridy = 2;
		pnlMainContent.add(txtCityname, gbc_txtCityname);
		txtCityname.setColumns(10);

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
			currentMember = manager.findMember(membershipNr);

			if (currentMember != null) {
				txtFirstname.setText(currentMember.getFirstname());
				txtLastname.setText(currentMember.getLastname());
				txtStreetname.setText(currentMember.getStreet() + " " + currentMember.getHouseNumber());
				txtCityname.setText(currentMember.getCity());

				// memberCanBeRemoved = currentMember.isRemovable();
				if (currentMember.hasLoans()) {
					// boekenGeleend = "ja";
				}

				if (currentMember.hasReservations()) {
					// heeftReserveringen = "ja";
				}
				memberFound = true;
			}

			// removeMemberButton.setEnabled(memberCanBeRemoved);
			// textAreaMemberInfo.setText(memberInfo);
		}
		return memberFound;
	}

	/**
	 * Performs the corresponding action for a button.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btnSearch) {
			// Empty the fields
			txtStatusText.setText("");
			txtFirstname.setText("");
			txtLastname.setText("");

			try {
				int membershipNr = Integer.parseInt(txtSearchBox.getText()
						.trim());
				logger.debug("Finding member " + membershipNr);
				boolean found = doFindMember(membershipNr);
				if (!found) {
					txtStatusText.setText("Lidnummer niet gevonden");
				}
			} catch (NumberFormatException ex) {
				logger.error("Wrong input, only numbers allowed");
				txtStatusText.setText("Het lidnummer bestaat uit 4 cijfers.");
				txtSearchBox.setText("");
				txtSearchBox.requestFocus();
			}
		}

	}
}
