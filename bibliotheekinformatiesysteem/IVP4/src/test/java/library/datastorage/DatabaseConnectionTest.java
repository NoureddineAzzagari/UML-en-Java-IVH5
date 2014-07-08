/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.datastorage;

import java.sql.ResultSet;

import org.junit.*;
import static org.junit.Assert.*;
import library.datastorage.daofactory.rdbms.mysql.MySqlConnection;

/**
 * For this test to pass a database must be present and running
 * 
 * @author ppthgast
 */
public class DatabaseConnectionTest {
    
    public DatabaseConnectionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void doConnectTest()
    {
        // test preparation and execution
        ResultSet rs = null;
        MySqlConnection connection = new MySqlConnection();
        boolean result = connection.openConnection();
        assertTrue("Could not establish database connection (is the database running?)", result);
		
        if(result) {
            rs = connection.executeSQLStatement("select * from member");
			// test verification
			assertTrue("Result set not null", rs != null);
		}
	}
        
}
