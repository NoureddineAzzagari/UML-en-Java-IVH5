/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.businesslogic;

import java.util.ArrayList;
import java.util.HashMap;

import library.datastorage.daofactory.DAOFactory;
import library.datastorage.daofactory.interfaces.ReservationDAOInf;
import library.datastorage.daofactory.interfaces.LoanDAOInf;
import library.datastorage.daofactory.interfaces.MemberDAOInf;
import library.domain.ImmutableMember;
import library.domain.Loan;
import library.domain.Member;
import library.domain.Reservation;

/**
 *
 * @author ppthgast
 */
public class MemberAdminManagerImpl implements MemberAdminManager{
    
    private HashMap<Integer, Member> members;
    private DAOFactory daoFactory;
    
    // TODO: the reference to the factory class should be replaced by the use of e.g. a property.
	private String theFactoryClass = "library.datastorage.daofactory.rdbms.mysql.MySqlDAOFactory";

    
    public MemberAdminManagerImpl()
    {
        members = new HashMap<Integer, Member>();
    	daoFactory = DAOFactory.getDAOFactory(theFactoryClass);
        
        //fillTestData();
    }

//    private void fillTestData()
//    {
//        members.put(1000, new Member(1000, "Pascal", "van Gastel"));
//        members.put(1001, new Member(1001, "Erco", "Argante"));
//        members.put(1002, new Member(1002, "Marice", "Bastiaense"));
//        members.put(1004, new Member(1004, "Floor", "van Gastel"));
//        members.put(1005, new Member(1005, "Jet", "van Gastel"));
//        members.put(1006, new Member(1006, "Marin", "van Gastel"));
//    }
    
    public ImmutableMember findMember(int membershipNumber)
    {
        Member member = members.get(membershipNumber);
        
        if(member == null)
        {
            // Member may not have been loaded from the database yet. Try to
            // do so.
            MemberDAOInf memberDAO = daoFactory.getMemberDAO();
            member = memberDAO.findMember(membershipNumber);
            
            if(member != null)
            {
                // Member successfully read. Now read its loans.
                LoanDAOInf loanDAO = daoFactory.getLoanDAO();
                ArrayList<Loan> loans = loanDAO.findLoans(member);

                for(Loan loan: loans)
                {
                    member.addLoan(loan);
                }
                
                // And read the reserverations from the database.
                ReservationDAOInf reservationDAO = daoFactory.getReservationDAO();
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
    
    public boolean removeMember(ImmutableMember memberToRemove)
    {
        boolean result = false;
        
        if(memberToRemove.isRemovable())
        {
            // Cast the given member to the Member object, otherwise the
            // removal cannot be done since the interace does not support it.
            Member member = (Member)memberToRemove;
            
            int membershipNumber = memberToRemove.getMembershipNumber();
                    
            // Let the member remove itself as a domain object. Do this before
            // removing from the database. In case something goes wrong, we
            // still have the data in the database. If first the member was
            // removed from the database, we might end up in an inconsistent
            // state.
            result = member.remove();
            
            if(result)
            {
                // Let the member remove itself from the database.
                MemberDAOInf memberDAO = daoFactory.getMemberDAO();
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
