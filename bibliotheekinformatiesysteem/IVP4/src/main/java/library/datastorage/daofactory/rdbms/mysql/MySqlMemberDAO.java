/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.datastorage.daofactory.rdbms.mysql;

import library.domain.Member;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import library.datastorage.daofactory.interfaces.MemberDAOInf;

/**
 *
 * @author ppthgast
 */
public class MySqlMemberDAO implements MemberDAOInf {
 
	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(MySqlMemberDAO.class);

	private MySqlConnection connection;

    public MySqlMemberDAO()
    {
    	connection = new MySqlConnection();
    }
    
    /**
     * Tries to find the member identified by the given membership number
     * in the persistent data store, in this case a MySQL database. All loans
     * and reservations for the member are loaded as well. In this POC, the
     * reserved books and/or lend copies of the books are not loaded - it is
     * out of scope for now.
     * 
     * @param membershipNumber identifies the member to be loaded from the database
     * 
     * @return the Member object to be found. In case member could not be found,
     * null is returned.
     */
    public Member findMember(int membershipNumber)
    {
		logger.debug("findMember " + membershipNumber);

        Member member = null;
        
        // First open a database connection
        if(connection.openConnection())
        {
            // If a connection was successfully setup, execute the SELECT statement.
            ResultSet resultset = connection.executeSQLStatement(
                "SELECT * FROM member WHERE MembershipNumber = " + membershipNumber + ";");

            if(resultset != null)
            {
        		logger.debug("resultset was found");
                try
                {
                    // The membershipnumber for a member is unique, so in case the
                    // resultset does contain data, we need its first entry.
                    if(resultset.next())
                    {
                        int membershipNumberFromDb = resultset.getInt("MembershipNumber");
                        String firstNameFromDb = resultset.getString("FirstName");
                        String lastNameFromDb = resultset.getString("LastName");

                        member = new Member(
                            membershipNumberFromDb,
                            firstNameFromDb,
                            lastNameFromDb);
                        
                        member.setStreet(resultset.getString("Street"));
                        member.setHouseNumber(resultset.getString("HouseNumber"));
                        member.setCity(resultset.getString("City"));
                        member.setFine(resultset.getDouble("Fine"));
                    }
                }
                catch(SQLException e)
                {
                    System.out.println(e);
                    member = null;
                }
            } else {
        		logger.debug("resultset was returned, no results found");
            }

            // We had a database connection opened. Since we're finished,
            // we need to close it.
            connection.closeConnection();
        }
        
        return member;
    }

    /**
     * Removes the given member from the database.
     * 
     * @param memberToBeRemoved an object of the Member class representing the
     * member to be removed.
     * 
     * @return true if execution of the SQL-statement was successful, false
     * otherwise.
     */
    @SuppressWarnings("unused")
	public boolean removeMember(Member memberToBeRemoved)
    {
		logger.debug("removeMember " + memberToBeRemoved);

        boolean result = false;
        ResultSet resultset = null;
        
        if(memberToBeRemoved != null)
        {
            // First open the database connection.
            if(connection.openConnection())
            {
                // Execute the delete statement using the membership number to
                // identify the member row.
                resultset = connection.executeSQLStatement(
                    "DELETE FROM member WHERE MembershipNumber = " + memberToBeRemoved.getMembershipNumber() + ";");
                
                // Finished with the connection, so close it.
                connection.closeConnection();
            }
            // else an error occurred leave 'member' to null.
        }
        
        // TODO: vertalen van ResultSet naar Boolean.
        return result;
    }


    /**
     * Inserts the given member into the database.
     * 
     * @param memberToInsert an object of the Member class representing the
     * member to be inserted.
     * 
     * @return true if execution was successful, false otherwise.
     */
	public int insertMember(Member memberToInsert)
    {
		logger.debug("TODO: insertMember " + memberToInsert.getFirstname());
        
        // TODO Implement insertMember through MySql
        return 0;
    }
}
