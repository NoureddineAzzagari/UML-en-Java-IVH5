/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.datastorage.daofactory.interfaces;

import library.domain.Member;

/**
 *
 * @author Robin Schellius
 */
public interface MemberDAOInf {
    
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
    public Member findMember(int membershipNumber);

    /**
     * Removes the given member from the database.
     * 
     * @param memberToBeRemoved an object of the Member class representing the
     * member to be removed.
     * 
     * @return true if execution of the SQL-statement was successful, false
     * otherwise.
     */
    public boolean removeMember(Member memberToBeRemoved);

    public int insertMember(Member memberToInsert);

//    public boolean deleteMember(...);
//    public Customer findCustomer(...);
//    public boolean updateMember(...);
//    public RowSet selectMemberRS(...);
//    public Collection selectMemberssTO(...);

}
