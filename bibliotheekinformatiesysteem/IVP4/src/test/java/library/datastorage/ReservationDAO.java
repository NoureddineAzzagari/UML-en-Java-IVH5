/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.datastorage;

import java.util.ArrayList;
import library.domain.Member;
import library.domain.Reservation;

/**
 * This is a stub for ReservationDAO. Purpose of this stub is to make the unit
 * test independent of the database.
 *
 * @author Erco
 */
public class ReservationDAO {
    public ArrayList<Reservation> findReservations(Member member) {
        System.out.println("ReservationDAO.findReservations() stub");
        return new ArrayList<>();       
    }
    
}
