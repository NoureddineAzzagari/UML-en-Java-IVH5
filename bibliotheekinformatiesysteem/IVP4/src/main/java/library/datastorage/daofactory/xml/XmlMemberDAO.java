/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.datastorage.daofactory.xml;

import library.domain.Member;
import library.datastorage.daofactory.interfaces.MemberDAOInf;

/**
 *
 * @author Robin Schellius
 */
public class XmlMemberDAO implements MemberDAOInf {
    
    public XmlMemberDAO()
    {
        // Nothing to be initialized. This is a stateless class. Constructor
        // has been added to explicitely make this clear.
    }
    
    /**
     * Tries to find the member identified by the given membership number
     * in the persistent data store. All loans
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
        Member member = null;
        
        return member;
    }

    /**
     * Removes the given member.
     * 
     * @param memberToBeRemoved an object of the Member class representing the
     * member to be removed.
     * 
     * @return true if deletion was successful, false otherwise.
     */
    public boolean removeMember(Member memberToBeRemoved)
    {
        boolean result = false;
                
        return result;
    }
}
