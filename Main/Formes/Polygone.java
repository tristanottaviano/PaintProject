/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Formes;

import java.util.ArrayList;
import GUI.Ardoise;
import java.awt.Color;


/*
@author tristan
*/
public class Polygone implements Dessinable, Perimetrable, Surfacable  {
  
    //ATTRIBUTS
    ArrayList<Point> pointList;
    public int brushSize=1;
    public Color brushColor = Color.BLACK;
    
    
    //CONSTRUCTEUR
    public Polygone(Point firstPoint, int sz){
        
        this.pointList = new ArrayList<Point>();
        this.pointList.add(firstPoint);
        this.brushSize = sz;
        
        
    }
    
    public Polygone(Point firstPoint, int sz, Color c){
        
        this.pointList = new ArrayList<Point>();
        this.pointList.add(firstPoint);
        this.brushSize = sz;
        this.brushColor = c;
        
    }
    
    public Polygone(ArrayList<Point> list, int sz){
        
        this.pointList = list;
        this.brushSize = sz;
        
    }
   
    //GETTER
    public ArrayList<Point> getPoints(){
        
        return this.pointList;
        
    }
    
    
    public int getBrushSize(){
        
        return this.brushSize;
        
    }
    
    public Color getBrushColor(){
    
        return this.brushColor;
        
    }
    
    
    
    @Override
    public void dessineSur(Ardoise a){
        
        Point p1=null, p2=null;
        
        //On parcours les points, puis on les relis
        for(int i=1; i<this.pointList.size(); i++){
            
            p1 = this.pointList.get(i-1);
            p2 = this.pointList.get(i);

            a.dessineSegment(p1.x, p1.y, p2.x, p2.y);
            
        }   
        
        //On trace le segment qui relie le derrnier point au premier
        a.dessineSegment(p2.x, p2.y, pointList.get(0).x, pointList.get(0).y);
        
    }
    
    
    @Override
    public ArrayList<Point> getConstituant(){
        
        return pointList;
        
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
    
    public boolean ligneCroise(Point p1, Point p2, Point p3, Point p4){
        
        double deltaX1 = (p2.x-p1.x);
        double deltaX2 = (p4.x-p3.x);
        double a1 = (p2.y-p1.y)/deltaX1;
        double a2 = (p4.y-p3.y)/deltaX2;
        double b1 = p1.y - p1.x*a1;
        double b2 = p3.y - p3.x*a2;
        Point proj;
        
        //Si la droite P1P2 est vertcale
        if ((Calcul.isNul(deltaX1) && !Calcul.isNul(deltaX2))) {
            
           proj = new Point (p1.x, a2*(p1.x)+b2,1);
            
        }
        
        //Si la droite P3P4 est verticale
        else if (!Calcul.isNul(deltaX1) && Calcul.isNul(deltaX2)){
            
            proj = new Point (p3.x, a1*(p3.x)+b1,1);

        }
        
        //Sinon
        else {
            
            proj = new Point((b2-b1)/(a1-a2), a1*((b2-b1)/(a1-a2))+b1,1);
        
        }
        
        if (isOnSegment(p1,p2,proj) && isOnSegment(p3,p4,proj)) {

            return true;
        }
       
        else return false;
       
    }
    
    public boolean isOnSegment(Point a, Point b,  Point c){
    
        double crossProduct = (c.y - a.y) * (b.x - a.x) - (c.x - a.x) * (b.y - a.y);
        if (!Calcul.isNul(Math.abs(crossProduct))) return false;
        double dotProduct = (c.x - a.x) * (b.x - a.x) + (c.y - a.y)*(b.y - a.y);
        if (dotProduct < 0) return false;
        double length = (b.x - a.x)*(b.x - a.x) + (b.y - a.y)*(b.y - a.y);
        if (dotProduct > length) return false;
        
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
        for (int i=1; i<this.pointList.size()+1; i++){
        
            //Equation de la droite associée au vecteur
            if (i==this.pointList.size()) point = this.pointList.get(0); 
            else point = this.pointList.get(i);
            point2 = this.pointList.get(i-1);

            a = (point2.y-point.y)/(point2.x-point.x);
            b = point.y - a * point.x;
            
            //L'équation de la droite perpendicualire passant par le point p
            aPrime = -(point2.x-point.x)/(point2.y-point.y);
            bPrime = p.y - aPrime * p.x;
        
            //On calcul le projeté de ce point sur la droite
            //Si la droite P1P2 est vertcale
            if ((Calcul.isNul(point2.x-point.x) && !Calcul.isNul((point2.y-point.y)))) {

               proj = new Point (point.x, aPrime*(point.x)+bPrime,1);

            }

            //Si la droite P3P4 est verticale
            else if (!Calcul.isNul(point2.x-point.x) && Calcul.isNul((point2.y-point.y))){

                proj = new Point (p.x, a*(p.x)+b,1);

            }

            //Sinon
            else {

                proj = new Point((bPrime-b)/(a-aPrime), a*((bPrime-b)/(a-aPrime))+b,1);
           
            }
            
            
            //Distance entre point et segment
            segDist = p.distance(proj);
            
            if (isOnSegment(point, point2, proj) && segDist<tmpDist){

                tmpDist = segDist;
                
            }
            
        }
        
        return tmpDist;
        
    }
    
    public double getPerimetre(){
        
        double perimetre=0;
        
        for (int i=1; i<this.pointList.size(); i++){
            
              perimetre += this.pointList.get(i).distance(this.pointList.get(i-1));
            
        }
        
        perimetre += this.pointList.get(0).distance(this.pointList.get(this.pointList.size()-1));
                
        return perimetre;
        
    }
    
    private boolean croise(){
        
        Point p1, p2, p3, p4;
        int size =this.pointList.size();
        
        //Si il y a moins de 4 point, impossible de croisser
        if (size<4) return false;
        
        //On parcours tous les segments et on check si il se croisent deux à deux (sauf les adj.)
        for (int i=0; i<size; i++){
            
            p1 = this.pointList.get(i%size);
            p2 = this.pointList.get((i+1)%size);
            
            for (int j=0; j<size; j++){
                
                //On ne check pas les seg. adj.
                if (j%size==i%size || j%size==(i+1)%size || (j+1)%size==i%size || (j+1)%size==(i+1)%size) continue;
                
                p3 = this.pointList.get(j%size);
                p4 = this.pointList.get((j+1)%size);

                if (ligneCroise(p1,p2,p3,p4)) return true;

            }
            
        }
        
        return false;
            
    }
    
    public double getSurface(){
    
        //Variables
        double[][] points = new double[this.pointList.size()+1][2]; 
        double Sum=0;
        
        //Si le polynome est croisé, on renvois -1
        if (croise()) return -1;
        
        //On remplit un tableau avec les coord. de chaques points
        for (int i=0; i<this.pointList.size(); i++){
        
            points[i][0] = this.pointList.get(i).x;
            points[i][1] = this.pointList.get(i).y;

        }
        //On remplit la dernière case pour que ça boucle        
        points[this.pointList.size()][0] = this.pointList.get(0).x;
        points[this.pointList.size()][1] = this.pointList.get(0).y;

        
        //On mutliplie les x de i avec les y de i+1 et on somme (et vis versa)
        for (int i=1; i<this.pointList.size()+1; i++){
    
            Sum += points[i-1][0]*points[i][1]-points[i-1][1]*points[i][0];
     
        }
        
        //On renvois la diff. des deux sommes divisée par deux
        return Math.abs(Sum/2);
        
    }
    
    public boolean isConvex(){

        int size =this.pointList.size();
        
        //Si il y a moins de 4 point, impossible d'être concave
        if (size<4) return true;

        Point p1 = this.pointList.get(0);
        Point p2 = this.pointList.get(1);
        Point p3 = this.pointList.get(2);
        Vecteur v1 = new Vecteur(p2,p1);
        Vecteur v2 = new Vecteur(p2,p3);
        
        double firstProduct = v1.prod(v2);
        
        //On parcours tous les segments (par paire) et on regarde si leur produit vectoriel est toujours du même signe
        //On prend le signe du premier produit vect
        
        for (int i=1; i<size; i++){

            p1 = this.pointList.get(i%size);
            p2 = this.pointList.get((i+1)%size);
            p3 = this.pointList.get((i+2)%size);

            v1 = new Vecteur(p2,p1);
            v2 = new Vecteur(p2,p3);
            
            //Si on a un prod. vect de signe différent des autres: Concave
            if (v1.prod(v2)*firstProduct<0) return false;

        }
                        
        return true;

    }
  
    
}
