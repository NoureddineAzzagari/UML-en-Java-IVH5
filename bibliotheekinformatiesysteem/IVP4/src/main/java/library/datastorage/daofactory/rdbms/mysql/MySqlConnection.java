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

/**
 *
 * @author ppthgast
 */
public class MySqlConnection {
    
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

            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception e) {
                System.out.println(e);
        }
    }
    
    public boolean openConnection()
    {
        boolean result = false;

        if(connection == null)
        {
            try
            {   
                // Try to create a connection with the library database
                connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/library" , "biblio1", "boekje");

                if(connection != null)
                {
                    statement = connection.createStatement();
                }
                
                result = true;
            }
            catch(SQLException e)
            {
                System.out.println(e);
                result = false;
            }
        }
        else
        {
            // A connection was already initalized.
            result = true;
        }
        
        return result;
    }
    
    public boolean connectionIsOpen()
    {
        boolean open = false;
        
        if(connection != null && statement != null)
        {
            try
            {
                open = !connection.isClosed() && !statement.isClosed();
            }
            catch(SQLException e)
            {
                System.out.println(e);
                open = false;
            }
        }
        // Else, at least one the connection or statement fields is null, so
        // no valid connection.
        
        return open;
    }
    
    public void closeConnection()
    {
        try
        {
            statement.close();
            
            // Close the connection
            connection.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
    
    public ResultSet executeSQLStatement(String query)
    {
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
                System.out.println(e);
                resultset = null;
            }
        }
        
        return resultset;
    }
    
}
