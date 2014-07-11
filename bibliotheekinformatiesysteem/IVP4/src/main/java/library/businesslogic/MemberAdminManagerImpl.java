/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.businesslogic;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import library.datastorage.daofactory.DAOFactory;
import library.datastorage.daofactory.interfaces.ReservationDAOInf;
import library.datastorage.daofactory.interfaces.LoanDAOInf;
import library.datastorage.daofactory.interfaces.MemberDAOInf;
import library.datastorage.daofactory.xml.dom.XmlDOMMemberDAO;
import library.domain.ImmutableMember;
import library.domain.Loan;
import library.domain.Member;
import library.domain.Reservation;

/**
 *
 * @author ppthgast, rschelli
 */
public class MemberAdminManagerImpl implements MemberAdminManager{
    
	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(MemberAdminManagerImpl.class);

	private HashMap<Integer, Member> members;
    private DAOFactory daoFactory;
    
    // TODO: the reference to the factory class should be replaced 
    // by the use of e.g. a property.
	private String theDAOFactoryClass = 
			// "library.datastorage.daofactory.rdbms.mysql.MySqlDAOFactory";
			"library.datastorage.daofactory.xml.dom.XmlDOMDAOFactory";
  
	/**
	 * 
	 */
    public MemberAdminManagerImpl()
    {
		members = new HashMap<Integer, Member>();
    	daoFactory = DAOFactory.getDAOFactory(theDAOFactoryClass);
    }

    /**
     * 
     */
    public ImmutableMember findMember(int membershipNumber)
    {
		logger.debug("findMember " + membershipNumber);

		Member member = members.get(membershipNumber);
        
        if(member == null)
        {
    		logger.debug("Member was not found in cache, so lookup in datasource");

            // Member may not have been loaded from the datasource yet. Try to
            // do so.
            MemberDAOInf memberDAO = daoFactory.getMemberDAO();
            member = memberDAO.findMember(membershipNumber);
            
            if(member != null)
            {
        		logger.debug("Member was found in datasource; reconstructing it.");

                LoanDAOInf loanDAO = daoFactory.getLoanDAO();
                ArrayList<Loan> loans = loanDAO.findLoans(member);

                if(loans != null) {
	                for(Loan loan: loans) {
	                	if(loan != null) {
	                		member.addLoan(loan);
	                	}
	                }
                }
                
                // And read the reserverations from the database.
                ReservationDAOInf reservationDAO = daoFactory.getReservationDAO();
                ArrayList<Reservation> reservations = reservationDAO.findReservations(member);

                if(reservations != null) {
	                for(Reservation reservation: reservations)
	                {
	                	if(reservation != null) {
	                    	member.addReservation(reservation);
	                	}
	                }
                }
                
                // Cache the member that has been read from the database, to
                // avoid querying the database each time a member is needed.
        		logger.debug("Done. Putting member in cache.");
                members.put(membershipNumber, member);
            }
        }

        return member;
    }
    
    public boolean removeMember(ImmutableMember memberToRemove)
    {
		logger.debug("removeMember " + memberToRemove);
		
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
