/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.datastorage.daofactory.interfaces;

import java.util.ArrayList;
import library.domain.Member;
import library.domain.Reservation;

/**
 *
 * @author ppthgast
 */
public interface ReservationDAOInf
{
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
    public ArrayList<Reservation> findReservations(Member member);
    
}
