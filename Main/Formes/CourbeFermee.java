package Formes;

/**
 *
 * @author pierrecharbit
 */

public interface CourbeFermee extends Courbe, Perimetrable, Surfacable{
    
    boolean interieurContient(Point p);   
    
}
