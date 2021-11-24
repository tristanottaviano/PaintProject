/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Formes;

import java.util.ArrayList;
import java.awt.Color;
import GUI.Ardoise;

/**
 * @author tristan
 */
public class Text implements Dessinable{
   
    //Variables
    Color brushColor = Color.BLACK;
    int brushSize = 1;
    Point coord=null;
    String stringToDraw;
    
    //CONSTRUCTEUR
    public Text(String s,Point p, int i){
        
        this.stringToDraw = s;
        this.coord = p;
        this.brushSize = i;
        
    }
    
    public Text(String s,Point p, int i, Color c){

        this.stringToDraw = s;        
        this.coord = p;
        this.brushSize = i;
        this.brushColor = c;
        
    }
    
    //GETTER
    @Override
    public Color getBrushColor(){
        
        return this.brushColor;
        
    }
    
    @Override
    public int getBrushSize(){
        
        return this.brushSize;
        
    }
    
    //SETTERS
    @Override
    public void setBrushColor(Color c){
        
        this.brushColor = c;
        
    }
    
    @Override
    public void setBrushSize(int sz){
        
        this.brushSize = sz;
        
    }
    
    @Override
    public double distance(Point p){
        
        return coord.distance(p);
        
    }
    
    @Override
    public void translate(Point A, Point B){
        
        this.coord.x += B.x - A.x;
        this.coord.y += B.y - A.y;
        
    }
    
    @Override
    public ArrayList<Point> getConstituant(){
        
        ArrayList<Point> constituant = new ArrayList<Point>();
        constituant.add(this.coord);
        return constituant;
                
    }
    
    @Override
    public void dessineSur(Ardoise a){
        
        a.dessineString(stringToDraw,coord.x, coord.y);
        
    }
    
    
}
