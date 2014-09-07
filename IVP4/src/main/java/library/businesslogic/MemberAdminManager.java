/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package library.businesslogic;

import library.domain.ImmutableMember;

/**
 *
 * @author ppthgast
 */
public interface MemberAdminManager {
    
    /**
     * Tries to find the Member object matching the given membershipnumber.
     * 
     * @param membershipNumber the member unique number
     * @return if a matching member was found, a reference to the Member's
     * ImmutableMember interface is returned, null otherwise.
     */
    public ImmutableMember findMember(int membershipNumber);
    
    /**
     * Removes the given member from the system, including removal from the
     * persistent data store.
     * 
     * @param memberToRemove a reference to the member to be removed
     * @return true if removal was successful, false otherwise, i.e. when
     * it was not allowed to remove the member or when removal from the
     * data store did not succeed.
     */
    public boolean removeMember(ImmutableMember memberToRemove);
}
