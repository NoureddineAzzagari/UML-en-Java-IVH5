/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datastorage;

import java.sql.ResultSet;
import library.datastorage.DatabaseConnection;
import org.junit.*;
import static org.junit.Assert.*;

/**
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
        DatabaseConnection connection = new DatabaseConnection();

        ResultSet rs = connection.executeSQLSelectStatement("select * from member");
    }
}
