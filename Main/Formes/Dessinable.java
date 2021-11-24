
package Formes;

import GUI.Ardoise;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;


/**
 *
 * @author pierrecharbit
 */
public interface Dessinable extends Serializable {
    int getBrushSize();
    void setBrushSize(int sz);
    Color getBrushColor();
    void setBrushColor(Color c);
    ArrayList<Point> getConstituant();
    void translate(Point A, Point B);
    void dessineSur(Ardoise a);
    double distance(Point p);
}

