    
package Formes;

import GUI.Ardoise;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author pierrecharbit
 */
public class Cercle implements CourbeFermee  {

    Point centre;
    Point bord;
    int brushSize;
    Color brushColor = Color.BLACK;

    
    public Cercle(Point centre, Point bord, int sz){
        this.centre=centre;
        this.bord=bord;
        this.brushSize = sz;
    }
     
    public Cercle(Point centre, Point bord, int sz, Color c){
        this.centre=centre;
        this.bord=bord;
        this.brushSize = sz;
        this.brushColor = c;
    }

    public int getBrushSize(){
        
        return this.brushSize;
        
    }
    
    
    public Color getBrushColor(){
    
        return this.brushColor;
        
    }
    
    
    public double getRayon(){
        return centre.distance(bord);
    }
    
    @Override
    public void translate(Point A, Point B){
        
        this.centre.x += B.x-A.x;
        this.centre.y += B.y-A.y;
        
        this.bord.x += B.x-A.x;
        this.bord.y += B.y-A.y;

        
    }
    
    @Override
    public double distance(Point p){
        
        return Math.abs(centre.distance(p) - this.getRayon());
        
    }
    
    @Override
    public ArrayList<Point> getConstituant(){
        
        ArrayList<Point> constituant = new ArrayList<Point>();
        constituant.add(centre);
        constituant.add(bord);
        return constituant;
        
    }
    
    @Override
    public boolean contient(Point p) {
        return Calcul.isNul(p.distance(centre)-getRayon());
    }

 
     @Override
    public boolean interieurContient(Point p) {
        return p.distance(centre)<getRayon();
    }

    @Override
    public double getPerimetre() {
        return 2*Math.PI*getRayon();
    }

    @Override
    public double getSurface() {
        return Math.PI*getRayon()*getRayon();
    }

    @Override
    public void setBrushSize(int sz){
        
        this.brushSize = sz;
        
    }
    
    @Override
    public void setBrushColor(Color c){
        
        this.brushColor = c;
        
    }

    @Override
    public boolean isConvex() {
        return true;
    }



    @Override
    public void dessineSur(Ardoise a) {
        a.dessineCercle(centre.x,centre.y,getRayon());
    }
        
}
