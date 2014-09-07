/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.datastorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import library.domain.Member;
import library.domain.Reservation;

/**
 *
 * @author ppthgast
 */
public class ReservationDAO
{
    public ReservationDAO()
    {
        // Nothing to be initialized. This is a stateless class. Constructor
        // has been added to explicitely make this clear.
    }
    
    /**
     * Tries to find the reservations for the given in the persistent data store, in
     * this case a MySQL database.In this POC, the lend copies of the books are
     * not loaded - it is out of scope for now.
     * 
     * @param member identifies the member whose reservations are to be
     * loaded from the database
     * 
     * @return an ArrayList object containing the Reservation objects that were found.
     * In case no reservation could be found, still a valid ArrayList object is returned.
     * It does not contain any objects.
     */
    public ArrayList<Reservation> findReservations(Member member)
    {
        ArrayList<Reservation> reservations = new ArrayList<>();
        
        if(member != null)
        {
            // First open a database connnection
            DatabaseConnection connection = new DatabaseConnection();
            if(connection.openConnection())
            {
                // If a connection was successfully setup, execute the SELECT statement.
                int membershipNumber = member.getMembershipNumber();
                ResultSet resultset = connection.executeSQLSelectStatement(
                    "SELECT * FROM reservation WHERE MembershipNumber = " + membershipNumber + ";");

                if(resultset != null)
                {
                    try
                    {
                        while(resultset.next())
                        {
                            // The value for the BookISBN in the row is ignored
                            // for this POC: no Book objects are loaded. Having the
                            // Reservation objects without the Book objects will do fine
                            // to determine whether the owning Member can be removed.
                            Date reservationDate = resultset.getDate("ReservationDate");

                            Reservation newReservation = new Reservation(reservationDate, member, null);
                            reservations.add(newReservation);
                       }
                    }
                    catch(SQLException e)
                    {
                        System.out.println(e);
                        reservations.clear();
                    }
                }
                // else an error occurred leave array list empty.

                // We had a database connection opened. Since we're finished,
                // we need to close it.
                connection.closeConnection();
            }
        }
        
        return reservations;
    }
}
