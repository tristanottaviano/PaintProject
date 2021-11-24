/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Formes;

import Formes.*;
import java.awt.Color;
import java.util.ArrayList;
import GUI.Ardoise;

/**
 * @author tristan
 */

public class Right implements Dessinable {
    
    //Var
    ArrayList<Point> pointList = new ArrayList<Point>();
    int brushSize;
    Color brushColor = Color.BLACK;
    
    //CONSTRUCTEUR
    public Right(Point p1, int sz){
        
        this.pointList = new ArrayList<Point>();
        this.pointList.add(p1);
        this.brushSize = sz;
        
    }
    
    public Right(Point p1, Point p2, int sz, Color c){
        
        this.pointList = new ArrayList<Point>();
        this.pointList.add(p1);
        this.pointList.add(p2);
        this.brushSize = sz;
         this.brushColor = c;
        
    }
    
    public Right(Point p1, int sz, Color c){
        
        this.pointList = new ArrayList<Point>();
        this.pointList.add(p1);
        this.brushSize = sz;
        this.brushColor = c;
        
    }
    
    //GETTER
    @Override
    public Color getBrushColor(){
    
        return brushColor;
    
    }
    
    @Override
    public int getBrushSize(){
        
        return brushSize;
        
    }
    
    @Override
    public ArrayList<Point> getConstituant(){
    
        return this.pointList;
    
    }
    
    //SETTER
    @Override
    public void setBrushColor(Color c){
        
        this.brushColor = c;
        
    }
    
    @Override
    public void setBrushSize(int sz){
        
        this.brushSize = sz;
        
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

            Point p1 = this.pointList.get(0);
            Point p2 = this.pointList.get(1);
            double A = (p2.y-p1.y)/(p2.x-p1.x);
            double B = p1.y - A*p1.x;
            
            Point dP1 = new Point(a.getxMin(), A*a.getxMin()+B,1);
            Point dP2 = new Point(a.getxMax(), A*a.getxMax()+B,1);;
            
            //Si la droite est verticale
            if (Calcul.isNul(p2.x-p1.x)) {
                
                dP1.x = p1.x;
                dP1.y = a.getyMax();
                dP2.x = p1.x;
                dP2.y = a.getyMin();

                
            }

            a.dessineSegment(dP1.x, dP1.y, dP2.x, dP2.y);

    }
    
    public double distance(Point p){
     
        double a,b,aPrime, bPrime;
        Point point, point2, proj;        

        //Equation de la droite associée au vecteur
        point = this.pointList.get(0);
        point2 = this.pointList.get(1);

        a = (point2.y-point.y)/(point2.x-point.x);
        b = point.y - a * point.x;
        

        //L'équation de la droite perpendicualire passant par le point p
        aPrime = -(point2.x-point.x)/(point2.y-point.y);
        bPrime = p.y - aPrime * p.x;
        
        //Si la droite est Verticale
        if (Calcul.isNul(point2.x-point.x)) proj = new Point(point.x, p.y,1);
        
        //Si elle est Horizontale
        else if (Calcul.isNul(point2.y-point.y)) proj = new Point(p.x, point.y,1);

        //On calcul le projeté de ce point sur la droite
        else proj = new Point((bPrime-b)/(a-aPrime), a*((bPrime-b)/(a-aPrime))+b,1);
         
        
        //Distance entre point et segment
        return p.distance(proj);
        
     }
    
           
}
