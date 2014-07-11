/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.datastorage.daofactory.interfaces;

import java.util.ArrayList;
import library.domain.Loan;
import library.domain.Member;

/**
 * This interface describes the methods that must be available in the LoanDAO 
 * implementation for a specific data source. 
 *
 * @author ppthgast
 */
public interface LoanDAOInf
{
    /**
     * Tries to find the loans for the given in the persistent data store, in
     * this case a MySQL database.In this POC, the lend copies of the books are
     * not loaded - it is out of scope for now.
     * 
     * @param member identifies the member whose loans are to be
     * loaded from the database
     * 
     * @return an ArrayList object containing the Loan objects that were found.
     * In case no loan could be found, still a valid ArrayList object is returned.
     * It does not contain any objects.
     */
    public ArrayList<Loan> findLoans(Member member);
    
    /*
     * This interface is not completed yet. Methods that probably should
     * be available here could handle the creating, updating and deleting o
     * of Loan information from the data source that is used.
     * 
     */
}
