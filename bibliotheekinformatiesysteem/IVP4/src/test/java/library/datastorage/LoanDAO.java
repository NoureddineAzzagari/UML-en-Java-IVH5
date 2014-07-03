/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.datastorage;

import java.util.ArrayList;
import library.domain.Loan;
import library.domain.Member;

/**
 *
 * This is a stub for LoanDAO. Purpose of this stub is to make the unit test
 * independent of the database.
 * 
 * @author Erco
 */
public class LoanDAO {
    
    public ArrayList<Loan> findLoans(Member member) {
        System.out.println("LoanDAO.findLoans() stub");
        return new ArrayList<>();
    }

}
