
package Formes;

/**
 * Une classe contenant juste une méthode statique pour dire qu'un double peit etre considéré comme nul
 * @author pierrecharbit
 */
public class Calcul {
    static final double precision = 0.0000000001;
    
    public static boolean isNul(double d){
        return Math.abs(d)<precision;
    }
    
    public static double norm2(double a, double b){
        return Math.sqrt(a*a+b*b);
    }
}
