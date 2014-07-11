package library.datastorage;

import static org.junit.Assert.*;

import java.util.Date;

import library.datastorage.daofactory.DAOFactory;
import library.datastorage.daofactory.interfaces.MemberDAOInf;
import library.domain.Book;
import library.domain.Copy;
import library.domain.Loan;
import library.domain.Member;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class XmlDOMMemberDAOTest {

    private MemberDAOInf memberDAO = null;
    private Member newMember = null;

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(XmlDOMMemberDAOTest.class);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Configure logging. Necessary; otherwise logging in code under test
		// would not be initialized properly.
		PropertyConfigurator.configure("./logsettings.cnf");

		logger.debug("setUpBeforeClass");

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		logger.debug("setUp");
		
		String theDAOFactoryClass = 
				"library.datastorage.daofactory.xml.dom.XmlDOMDAOFactory";
    	DAOFactory daoFactory = DAOFactory.getDAOFactory(theDAOFactoryClass);
    	memberDAO = daoFactory.getMemberDAO();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInsertNewMember() {
		
		logger.debug("testInsertMember");
		
		Book newBook1 = new Book(857865544, "The life of Pi", "Yann Martel", 12);
		Copy newCopy1 = new Copy(4, 3, newBook1);
		
		Book newBook2 = new Book(834586544, "The old man and the sea", "Ernest Hemingway", 31);
		Copy newCopy2 = new Copy(2, 1, newBook2);
			
		newMember = new Member(1004, "Robin", "Schellius");
		newMember.setCity("Breda");
		newMember.setEmailaddress("r.schellius@avans.nl");
		newMember.setStreet("Hierdestraat");
		newMember.setHouseNumber("5");
		newMember.setPhoneNumber("076-19682014");
		newMember.setFine(2.75);
		
		newMember.addLoan(new Loan(new Date(2014, 05, 16), newMember, newCopy1));
		newMember.addLoan(new Loan(new Date(2014, 06, 02), newMember, newCopy2));

		memberDAO.insertMember(newMember);

		// TODO Retrieve the newly inserted member
	}

}
