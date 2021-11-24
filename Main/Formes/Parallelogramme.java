/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Formes;

import java.util.ArrayList;
import java.awt.Color;

/*
@author tristan
*/
public class Parallelogramme extends Polygone {
    
    //CONSTRUCTEUR
    public Parallelogramme (Point p, int sz){
        
        super(p, sz);
        
    }
    
       public Parallelogramme (Point p, int sz, Color c){
        
        super(p, sz, c);
        
    }
    
    
    public Parallelogramme(ArrayList<Point> list, int sz){
        
        super(list, sz);
    }
    
}
