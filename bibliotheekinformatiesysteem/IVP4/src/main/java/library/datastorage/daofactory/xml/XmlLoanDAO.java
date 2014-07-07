/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.datastorage.daofactory.xml;

import java.util.ArrayList;
import library.domain.Loan;
import library.domain.Member;
import library.datastorage.daofactory.interfaces.LoanDAOInf;

/**
 *
 * @author Robin Schellius
 */
public class XmlLoanDAO implements LoanDAOInf
{
    public XmlLoanDAO()
    {
        // Nothing to be initialized. This is a stateless class. Constructor
        // has been added to explicitely make this clear.
    }
    
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
    public ArrayList<Loan> findLoans(Member member)
    {
        
        return null;
    }
}
