/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.businesslogic;

import java.util.ArrayList;
import java.util.HashMap;
import library.datastorage.LoanDAO;
import library.datastorage.MemberDAO;
import library.datastorage.ReservationDAO;
import library.domain.Loan;
import library.domain.Member;
import library.domain.Reservation;

/**
 *
 * @author ppthgast
 */
public class MemberAdminManager {
    
    private HashMap<Integer, Member> members;
    
    public MemberAdminManager()
    {
        members = new HashMap();
        
        //fillTestData();
    }

    private void fillTestData()
    {
        members.put(1000, new Member(1000, "Pascal", "van Gastel"));
        members.put(1001, new Member(1001, "Erco", "Argante"));
        members.put(1002, new Member(1002, "Marice", "Bastiaense"));
        members.put(1004, new Member(1004, "Floor", "van Gastel"));
        members.put(1005, new Member(1005, "Jet", "van Gastel"));
        members.put(1006, new Member(1006, "Marin", "van Gastel"));
    }
    
    public Member findMember(int membershipNumber)
    {
        Member member = members.get(membershipNumber);
        
        if(member == null)
        {
            // Member may not have been loaded from the database yet. Try to
            // do so.
            MemberDAO memberDAO = new MemberDAO();
            member = memberDAO.findMember(membershipNumber);
            
            if(member != null)
            {
                // Member successfully read. Now read its loans.
                LoanDAO loanDAO = new LoanDAO();
                ArrayList<Loan> loans = loanDAO.findLoans(member);

                for(Loan loan: loans)
                {
                    member.addLoan(loan);
                }
                
                // And read the reserverations from the database.
                ReservationDAO reservationDAO = new ReservationDAO();
                ArrayList<Reservation> reservations = reservationDAO.findReservations(member);

                for(Reservation reservation: reservations)
                {
                    member.addReservation(reservation);
                }
                
                // Cache the member that has been read from the database, to
                // avoid querying the database each time a member is needed.
                members.put(membershipNumber, member);
            }
        }

        return member;
    }
    
    public boolean removeMember(Member member)
    {
        boolean result = false;
        
        if(member.isRemovable())
        {
            int membershipNumber = member.getMembershipNumber();
                    
            // Let the member remove itself as a domain object. Do this before
            // removing from the database. In case something goes wrong, we
            // still have the data in the database. If first the member was
            // removed from the database, we might end up in an inconsistent
            // state.
            result = member.remove();
            
            if(result)
            {
                // Let the member remove itself from the database.
                MemberDAO memberDAO = new MemberDAO();
                result = memberDAO.removeMember(member);
                
                // In case something goes wrong here, we need to roll back.
                // But that's too much for this version of the POC.
            }
            
            // Finally, remove the member from the map in this manager.
            members.remove(membershipNumber);
        }
        else
        {
            result = false;
        }
        
        return result;
    }
}
