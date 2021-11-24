/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Formes;
 
import java.util.ArrayList;
import GUI.Ardoise;
import java.awt.Color;

/**
 *
 * @author tristan
 */
public class LigneBrisee implements Dessinable, Perimetrable {

    //ATTRIBUTS
    ArrayList<Point> pointList;
    public int brushSize = 1;
    public Color brushColor = Color.BLACK;
    
    //CONSTRUCTEUR
    public LigneBrisee(Point firstPoint, int sz){
        
        this.pointList = new ArrayList<Point>();
        this.pointList.add(firstPoint);
        this.brushSize = sz;
        
    }
    
    public LigneBrisee(Point firstPoint, int sz, Color c){
        
        this.pointList = new ArrayList<Point>();
        this.pointList.add(firstPoint);
        this.brushSize = sz;
        this.brushColor = c;
        
    }
    
    public LigneBrisee(ArrayList<Point> list, int sz){
        
        this.pointList = list;
        this.brushSize = sz;
        
    }
   
    
    //GETTER
    public ArrayList<Point> getPoints(){
        
        return this.pointList;
        
    }
    
    public Color getBrushColor(){
    
        return this.brushColor;
        
    }
    
    public int getBrushSize(){
        
        return this.brushSize;
        
    }
    
    public double getPerimetre(){
        
        double perimetre=0;
        
        for (int i=1; i<this.pointList.size(); i++){
            
              perimetre += this.pointList.get(i).distance(this.pointList.get(i-1));
            
        }
        
        return perimetre;
        
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
    public void translate(Point A, Point B){
        
        for (int i=0; i<pointList.size(); i++){
            
            pointList.get(i).x += B.x-A.x;
            pointList.get(i).y += B.y-A.y;
            
        }
        
    }
    
    @Override
    public void dessineSur(Ardoise a){
        
        Point p1, p2;
        
        for(int i=1; i<this.pointList.size(); i++){
            
            p1 = this.pointList.get(i-1);
            p2 = this.pointList.get(i);

            a.dessineSegment(p1.x, p1.y, p2.x, p2.y);
            
        }   
        
    }
    
    @Override
    public ArrayList<Point> getConstituant(){
        
        return this.pointList;
        
    }
    
    
    public boolean isOnSegment(Point a, Point b,  Point c){
    
        double crossproduct = (c.y - a.y) * (b.x - a.x) - (c.x - a.x) * (b.y - a.y);
        if (!Calcul.isNul(Math.abs(crossproduct))) return false;
        double dotproduct = (c.x - a.x) * (b.x - a.x) + (c.y - a.y)*(b.y - a.y);
        if (dotproduct < 0) return false;
        double  squaredlengthba = (b.x - a.x)*(b.x - a.x) + (b.y - a.y)*(b.y - a.y);
        if (dotproduct > squaredlengthba) return false;
        
        return true;
        
    }
    
    public double distance(Point p){
     
        //Variables 
        double tmpDist = this.pointList.get(0).distance(p);
        double segDist = this.pointList.get(0).distance(p);
        
        //On parcours les points
        for (int i=1; i<this.pointList.size(); i++){
            
            if (tmpDist>this.pointList.get(i).distance(p)){
                
                tmpDist = this.pointList.get(i).distance(p);
                
            }
            
        }
        
        //Puis on parcours les segments
        double a,b,aPrime, bPrime;
        Point point, point2, proj;
        for (int i=1; i<this.pointList.size(); i++){
        
            //Equation de la droite associée au vecteur
            point = this.pointList.get(i);
            point2 = this.pointList.get(i-1);

            a = (point2.y-point.y)/(point2.x-point.x);
            b = point.y - a * point.x;
            
            //L'équation de la droite perpendicualire passant par le point p
            aPrime = -(point2.x-point.x)/(point2.y-point.y);
            bPrime = p.y - aPrime * p.x;
            //On calcul le projeté de ce point sur la droite
             //Si la droite est Verticale
            if (Calcul.isNul(point2.x-point.x)) proj = new Point(point.x, p.y,1);
        
            //Si elle est Horizontale
            else if (Calcul.isNul(point2.y-point.y)) proj = new Point(p.x, point.y,1);

            //On calcul le projeté de ce point sur la droite
            else proj = new Point((bPrime-b)/(a-aPrime), a*((bPrime-b)/(a-aPrime))+b,1);
            
            //Distance entre point et segment
            segDist = p.distance(proj);

            if (isOnSegment(point, point2, proj) && segDist<tmpDist){

                tmpDist = segDist;
                
            }
            
        }
        
        return tmpDist;
        
     }
    
}
