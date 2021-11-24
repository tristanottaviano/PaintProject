
package Formes;

/**
 *
 * @author pierrecharbit
 */
public class Vecteur {
    double x;
    double y;

    public Vecteur(double x, double y) {
        this.x = x;
        this.y = y;
    }

    
    public Vecteur(Point a, Point b) {
        this.x = b.x-a.x;
        this.y = b.y-a.y;
    } 
    
    public double getX(){
        return x;
    }
    
    public double getY(){
        return y;
    }
    
    public double getNorm2(){
        return scal(this);
    }
            
    public double scal(Vecteur v){
        return this.getX()*v.getX()+this.getY()*v.getY();
    }
    
    public double prod(Vecteur v){
        return this.getX()*v.getY()-this.getY()*v.getX();
    }
    
    public boolean isColinear(Vecteur v){
        return Calcul.isNul(this.prod(v));
    }
    
    public boolean isOrthogonal(Vecteur v){
        return Calcul.isNul(this.scal(v));
    }
}
