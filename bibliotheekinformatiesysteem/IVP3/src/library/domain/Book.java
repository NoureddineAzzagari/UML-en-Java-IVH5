/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library.domain;

import java.util.ArrayList;

/**
 *
 * @author ppthgast
 */
public class Book {
    
    private int ISBN;
    private String title;
    private String author;
    private int edition;
    
    private ArrayList<Copy> copies;
    private ArrayList<Reservation> reservations;
    
    public Book(int ISBN, String title, String author, int edition)
    {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.edition = edition;
        
        copies = new ArrayList();
        reservations = new ArrayList();
    }

    public int getISBN()
    {
        return ISBN;
    }

    public String getAuthor()
    {
        return author;
    }

    public int getEdition()
    {
        return edition;
    }

    public String getTitle()
    {
        return title;
    }
    
    public void removeReservation(Reservation reservation)
    {
        reservations.remove(reservation);
    }
    
    @Override
    public boolean equals(Object o)
    {
        boolean equal = false;
        
        if(o == this)
        {
            // Dezelfde instantie van de klasse, dus per definitie hetzelfde.
            equal = true;
        }
        else
        {
            if(o instanceof Book)
            {
                Book b = (Book)o;
                
                // Book wordt geidentificeerd door ISBN, dus alleen hierop
                // controlleren is voldoend.
                equal = this.ISBN == b.ISBN;
            }
        }
        
        return equal;
    }
    
    @Override
    public int hashCode()
    {
        // Deze implementatie is gebaseerd op de best practice zoals beschreven
        // in Effective Java, 2nd edition, Joshua Bloch.
        
        // ISBN is uniek, dus voldoende als hashcode.
        return ISBN;
    }
}
