/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.datastorage;

import java.sql.ResultSet;

/**
 *
 * @author ppthgast
 */
public interface DAOInterface {
    
    public boolean openConnection(); 
    
    public boolean connectionIsOpen();
    
    public void closeConnection();

    public ResultSet executeSQLSelectStatement(String query);
    
    public boolean executeSQLDeleteStatement(String query);
}
