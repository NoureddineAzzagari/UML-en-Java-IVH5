/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.datastorage.daofactory.rdbms.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

/**
 *
 * @author ppthgast, rschelli
 */
public class MySqlConnection {
	
	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(MySqlConnection.class);

	// TODO move username, password and connection strings out of the code. 
	private final static String username = "biblio1";
	private final static String password = "boekje";
	private final static String dbDriverName = "com.mysql.jdbc.Driver";	
	private final static String connectionString = "jdbc:mysql://localhost/library";
    
    private Connection connection;
    
    // The Statement object has been defined as a field because some methods
    // may return a ResultSet object. If so, the statement object may not
    // be closed as you would do when it was a local variable in the query
    // execution method.
    private Statement statement;
    
    public MySqlConnection()
    {
		connection = null;
        statement = null;
		
		try {
            // The newInstance() call is a work around for some
            // broken Java implementations
			logger.debug("Class.forname new driver instance");
            Class.forName(dbDriverName).newInstance();
        } catch (Exception e) {
                logger.error(e.getMessage());
        }

		logger.debug("Done");    	
    }
        
    public boolean openConnection()
    {
		logger.debug("openConnection");
		
        boolean result = false;

        if(connection == null)
        {
            try
            {   
                // Try to create a connection with the library database
                connection = DriverManager.getConnection(
                    connectionString , username, password);

                if(connection != null)
                {
            		logger.debug("create new SQL Statement");
                    statement = connection.createStatement();
                }
                
                result = true;
            }
            catch(SQLException e)
            {
                logger.error(e.getMessage());
                result = false;
            }
        }
        else
        {
    		logger.debug("using existing statement");
            result = true;
        }
        
        return result;
    }
    
    public boolean connectionIsOpen()
    {
        boolean open = false;
        
		logger.debug("connectionIsOpen");

		if(connection != null && statement != null)
        {
            try
            {
                open = !connection.isClosed() && !statement.isClosed();
            }
            catch(SQLException e)
            {
                logger.error(e.getMessage());
                open = false;
            }
        }
        // Else, at least one the connection or statement fields is null, so
        // no valid connection.
        
        return open;
    }
    
    public void closeConnection()
    {
		logger.debug("closeConnection");
        try
        {
            statement.close();
            
            // Close the connection
            connection.close();
        }
        catch(Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    public ResultSet executeSQLStatement(String query)
    {
		logger.debug("executeSQLStatement(" + query + ")");

		ResultSet resultset = null;
        
        // First, check whether a some query was passed and the connection with
        // the database.
        if(query != null && connectionIsOpen())
        {
            // Then, if succeeded, execute the query.
            try
            {
                resultset = statement.executeQuery(query);
            }
            catch(SQLException e)
            {
                logger.error(e.getMessage());
                resultset = null;
            }
        }
        
        return resultset;
    }
    
}
