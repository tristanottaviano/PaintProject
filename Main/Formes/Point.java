
package Formes;

import GUI.Ardoise;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author pierrecharbit
 */
public class Point implements Dessinable{
    String label;
    public double x;
    public double y;
    public int brushSize = 1;
    public Color brushColor = Color.BLACK;

    public Point(double x, double y, int sz) {
        this.x = x;
        this.y = y;
        this.brushSize = sz;
    }
    
    public Point(double x, double y, int sz, Color c) {
        this.x = x;
        this.y = y;
        this.brushSize = sz;
        this.brushColor = c;
    }


    public Point(String label, double x, double y, int sz) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.brushSize = sz;
    }  
    
    
    public int getBrushSize(){
        
        return this.brushSize;
        
    }
    
    public Color getBrushColor(){
    
        return this.brushColor;
        
    }
    
    @Override
    public ArrayList<Point> getConstituant(){
        
        ArrayList<Point> constituant = new ArrayList<Point>();
        constituant.add(this);
        return constituant;
        
    }
    
    @Override
    public void translate(Point A, Point B){
        
        this.x += B.x - A.x;
        this.y += B.y - A.y;
        
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
    public double distance(Point p){
        return Calcul.norm2(x-p.x,y-p.y);
    }
    
    @Override
    public void dessineSur(Ardoise a) {
        a.dessinePoint(this.x,this.y);
    }

    @Override
    public String toString() {
        return "Point : "+this.x+","+this.y;
    }

}
