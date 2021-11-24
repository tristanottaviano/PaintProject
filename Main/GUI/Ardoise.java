package GUI;

import Formes.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.Font;


/**
 *
 * @author pierrecharbit
 */
public class Ardoise extends JPanel {

    
    /**
     * l'objet graphics qui sert aux méthodes de dessin. 
     */
    Graphics2D crayon;
    
    //La taille du crayon
    int currBrushSize;
    
    //La Couoleur du Crayon
    Color currBrushColor = Color.BLACK;
    
    //Si on Lock à la grille
    boolean gridLock = false;
    
    //Si on affiche la grille ou non
    boolean displayGrid = true;
    
    /**
     * La liste des objets dessinables
     */
    private ArrayList<Dessinable> liste;
    
    /**
     * le Dessinable courant *
     */
    private Dessinable dCourant;
    
    
    //Le recctangle du zoom
    private Dessinable zoomRect;
    /**
     * le MouseAdapter ecouteur du panneau. Cet ecouteur va changer selon ce qu'on est en train
     * de faire, on va donc créer des classes internes (voir fin du fichier) pour les différents modes de l'appli
     */
    private MouseAdapter mouseAda;
    
    /**
     * les coordonées min max de la partie visible actuellement dans l'ardoise
     */
    private double xMin, xMax, yMin, yMax;

    /**
     * Un pointeur vers l'appli qui contient cette ardoise (pour avoir un lien
     * dans les deux sens).
     * Ca pourra vous être utile.
     */
    private Appli app;

    /**
     * Le constructeur : il prend en arguemnt les coordonées de la fenetre
     * visible
     */
    public Ardoise(double xMin, double xMax, double yMin, double yMax) {
        this.setBackground(Color.white);
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;

        //creation des listes et de la map
        liste = new ArrayList<>();

        // au début un mouseSelecAdapter qui ne fait rien du tout 
        mouseAda = new SelecMouseAdapter();

    }

    /**
     *
     */
    public Ardoise() {
        this(-1, 10, -1, 10);
    }



    /**
     * change le mode de la souris
     *
     * @param mode un String, peut etre mieux de remplacer ca par un int ou un
     * membre d'une Enum (TO DO)
     */
    public void setMode(String mode) {
        this.removeMouseListener(mouseAda);
        this.removeMouseMotionListener(mouseAda);
        
        if (!mode.equals("Move") && !mode.equals("Zoom")) {
            setdCourant(null);
            repaint();
        }
        
        if (mode.equals("Cercle")) {
            mouseAda = new CircleMouseAdapter();
        }
        else if (mode.equals("Select")) {
            mouseAda = new SelecMouseAdapter();
        }
        else if(mode.equals("LigneBrisee")){
            mouseAda = new LigneBriseeMouseAdapter();
        }
        else if(mode.equals("Polygone")){
            mouseAda = new PolygoneMouseAdapter();
        }
        else if(mode.equals("Parallelogramme")){
            mouseAda = new ParallelogrammeMouseAdapter();
        }
        else if(mode.equals("Rectangle")){
            mouseAda = new RectangleMouseAdapter();
        }
        else if(mode.equals("Pencil")){
            mouseAda = new PencilMouseAdapter();
        }
        else if(mode.equals("Move")){
            mouseAda = new MoveMouseAdapter();
        }
        else if(mode.equals("Text")){
            mouseAda = new TextMouseAdapter();
        }
        else if(mode.equals("Zoom")){
            mouseAda = new ZoomMouseAdapter();
        }
        else if(mode.equals("Right")){
            mouseAda = new RightMouseAdapter();
        }
        this.addMouseListener(mouseAda);
        this.addMouseMotionListener(mouseAda);

    }

     //PERMET DE CHANGER L'EPAISSEUR DU CRAYON
    public void setBrushSize(double s){
        
      this.currBrushSize = (int)s;
        
    }
    
    public int getBrushSize(){
        
        return this.currBrushSize;
        
    }
    /*
    * ATTENTION ON A FAIT LE CHOIX D'AVOIR LES OBJETS DESSINABLES (POINTS, DROITES ETC)
    * DONNÉES AVEC DES COORDONNÉES DOUBLE DANS UN REPERE ORTHONORMÉ MATHEMATIQUE STANDARD
    * 
    * LE PANNEAU FONCTIONNE POUR LE DESSIN EN PIXELS (ENTIERS) ET L'ORIGINE EST EN HAUT A GAUCHE DE L'ARDOISE
    * 
    * ICI ON TROUVE LES FONCTIONS DE CONVERSIONS ENTRE LES DEUX SYSTEMES DE COORDONNEES
     */
    /**
     * @param xRepere l'abscisse double du Point en tant qu'objet mathematique
     * de notre modele
     * @return l'abscisse entière du point dans le repère du panneau pour le
     * dessin. Peut etre negatif ou plus grand que la taille de la fenetre si le
     * point demandé est hors de la fenetre xmin xmax ymin ymax
     */
    public int getXArdoise(double xRepere) {
        int largeurArdoise = this.getWidth();
        return (int) ((xRepere - xMin) * largeurArdoise / (xMax - xMin));
    }

    /**
     * @param yRepere l'ordonnée double du Point en tant qu'objet mathematique
     * de notre modele
     * @return l'ordonnée entière du point dans le repère du panneau pour le
     * dessin. Peut etre negatif ou plus grand que la taille de la fenetre si le
     * point demandé est hors de la fenetre xmin xmax ymin ymax
     *
     */
    public int getYArdoise(double yRepere) {
        int hauteurArdoise = this.getHeight();
        return hauteurArdoise - (int) ((yRepere - yMin) * hauteurArdoise / (yMax - yMin));
    }

    /**
     *
     * @param xArdoise
     * @return fonction inverse de getXArdoise
     */
    public double getXRepere(int xArdoise) {
        int largeurArdoise = this.getWidth();
        if (!gridLock) return xMin + (0.0 + xArdoise) / largeurArdoise * (xMax - xMin);
        else return Math.round(xMin + (0.0 + xArdoise) / largeurArdoise * (xMax - xMin));
    }

    /**
     *
     * @param yArdoise
     * @return fonction inverse de getYArdoise
     */
    public double getYRepere(int yArdoise) {
        int hauteurArdoise = this.getHeight();
        if (!gridLock) return (hauteurArdoise - yArdoise) * (yMax - yMin) / (0.0 + hauteurArdoise) + yMin;
        else return Math.round((hauteurArdoise - yArdoise) * (yMax - yMin) / (0.0 + hauteurArdoise) + yMin);
    }

    /**
     * ajoute un Dessinable a la liste
     *
     * @param d le DEssinable a jouter
     */
    public void ajoute(Dessinable d) {
        liste.add(d);
    }

    /*
    * Les trois commandes de base pour dessiner les formes
    * Elles sont utilisées par les méthodes dessineSur de chacune des classes Dessinable
    * Notez que l'on commence par convertir les coordonées double en int pour avoir les coordonées pour le dessin dans le panneau
     */
    public void dessineCercle(double x, double y, double rayon) {
        int xx = getXArdoise(x);
        int yy = getYArdoise(y);
        int rrx = (int) (rayon * this.getWidth() / (xMax - xMin));
        int rry = (int) (rayon * this.getHeight() / (yMax - yMin));
        
        crayon.drawOval(xx - rrx, yy - rry, 2 * rrx, 2 * rry);
        
    }

    public void dessinePoint(double x, double y) {
        int xx = getXArdoise(x);
        int yy = getYArdoise(y);
        crayon.fillOval(xx - 5, yy - 5, 10, 10);
    }
    
    public void dessineString(String s,double x, double y){
        int xx = getXArdoise(x);
        int yy = getYArdoise(y);
        crayon.drawString(s,xx,yy);
    }

    public void dessineSegment(double x1, double y1, double x2, double y2) {
        int xx1 = getXArdoise(x1);
        int yy1 = getYArdoise(y1);
        int xx2 = getXArdoise(x2);
        int yy2 = getYArdoise(y2);
        crayon.drawLine(xx1, yy1, xx2, yy2);
    }

    /* Tous les getters et setters publics */
    public double getxMin() {
        return xMin;
    }

    public void setxMin(double xMin) {
        this.xMin = xMin;
    }

    public double getxMax() {
        return xMax;
    }

    public void setxMax(double xMax) {
        this.xMax = xMax;
    }

    public double getyMin() {
        return yMin;
    }

    public void setyMin(double yMin) {
        this.yMin = yMin;
    }

    public double getyMax() {
        return yMax;
    }

    public void setyMax(double yMax) {
        this.yMax = yMax;
    }

    public ArrayList<Dessinable> getListeDes() {
        return liste;
    }

    public void setListeDes(ArrayList<Dessinable> listeDes) {
        this.liste = listeDes;
    }

    public Dessinable getdCourant() {
        return dCourant;
    }

    public void setdCourant(Dessinable dCourant) {
        this.dCourant = dCourant;
    }

    public void setApp(Appli app) {
        this.app = app;
    }

    /**
     * La méthode qui contient les instructions de redessinage du panneau Elle
     * est appelée a chaque fois que la fenetre doit s'actualiser On peut la
     * rappeler par la commande repaint()
     *
     * @param g le contexte graphique de dessin
     */
    @Override
    protected void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        // on reactualise le crayon
        crayon = (Graphics2D) gg;

        //On dessine le repère
        crayon.setStroke(new BasicStroke(1));
        //Si displayGrid = true, on trace les points du repère
        if(displayGrid){
            
            for (int i=(int)xMin; i<=xMax+1; i++){

                for (int j=(int)yMin; j<=yMax+1; j++){

                    crayon.fillOval(getXArdoise(i), getYArdoise(j), 3, 3);

                }

            }
            //On trace les axes
            crayon.drawLine(getXArdoise(0), getYArdoise(yMin), getXArdoise(0), getYArdoise(yMax));
            crayon.drawLine(getXArdoise(xMin), getYArdoise(0), getXArdoise(xMax), getYArdoise(0));
            for (int i=(int)xMin; i<=xMax; i++){
                crayon.drawString(String.valueOf(i), getXArdoise(i), getYArdoise(0)+15);
            }
            for (int j=(int)yMin; j<=yMax; j++){
                if (j!=0) crayon.drawString(String.valueOf(j), getXArdoise(0)-15, getYArdoise(j));
            }    

        }
        
        //On dessine tous les dessinables de la liste
        for (Dessinable d : liste) { 
            
            if(d instanceof Text) crayon.setFont(new Font("TimesRoman", Font.PLAIN, (int)(d.getBrushSize()*100/((xMax-xMin)*(yMax-yMin)))+10));
            crayon.setStroke(new BasicStroke((int)(d.getBrushSize()*100/((xMax-xMin)*(yMax-yMin)))));
            crayon.setColor(d.getBrushColor());
            d.dessineSur(this);
            
        }
        
        try {
            
            //Si il n'est pas vide on dessine le Rectangle du Zoom
            if (zoomRect != null){
                
                crayon.setColor(Color.BLUE);
                crayon.setStroke(new BasicStroke(1));
                zoomRect.dessineSur(this);
                
            }
            
            //S'il n'est pas vide, on dessine le dessinable courant
            if (dCourant != null) {
                crayon.setColor(Color.red);
                if(dCourant instanceof Text) crayon.setFont(new Font("TimesRoman", Font.PLAIN, (int)(dCourant.getBrushSize()*100/((xMax-xMin)*(yMax-yMin)))+10));
                crayon.setStroke(new BasicStroke( (int)(dCourant.getBrushSize()*100/((xMax-xMin)*(yMax-yMin)))));
                dCourant.dessineSur(this);
                
                crayon.setColor(Color.DARK_GRAY);
                //On dessines les points qui le constitu
                for (int i=0; i<dCourant.getConstituant().size(); i++){
                    
                    dCourant.getConstituant().get(i).dessineSur(this);
                    
                }
                
                //Si dCourant est périmetrable on affiche son périmetre
                if (dCourant instanceof Perimetrable){
                    Perimetrable tmp = (Perimetrable)dCourant;
                    this.app.setPerimetre(String.format("%.2f", tmp.getPerimetre()));
                }
                else this.app.setPerimetre("NA");

                //Si dCourant est surfacable
                if (dCourant instanceof Surfacable){
                    Surfacable tmp = (Surfacable)dCourant;
                    if (tmp.getSurface()<0) this.app.setArea("Croisé");
                    else this.app.setArea(String.format("%.2f", tmp.getSurface()));
                    if(tmp.isConvex()) this.app.setConvex("Yes");
                    else this.app.setConvex("No");
                }
                else {
                    this.app.setConvex("NA");
                    this.app.setArea("NA");
                }

            }


            else {

                this.app.setPerimetre("NA");
                this.app.setArea("NA");
                this.app.setConvex("NA");

            }

        }
        
        catch (NullPointerException e){
            
            System.out.println("NullPointer: app");
            return;
            
        }
       
    }
    

    /*
     * Ici on va écrire les différentes classes internes de MouseAdpater     
     */
    
    
    /*-   -    -    -   -   -   -   -   - SELECT  -   -   -   -   -   -   -   -   -   -   -   -   -   -*/
    class SelecMouseAdapter extends MouseAdapter {
   
        //Variables
        Point pOld = new Point(0,0,1);
        Point pNew;
        boolean dragging = false;
        
        //Quand on clic
        public void mouseClicked(MouseEvent e) {
            
            if(e.getButton() == MouseEvent.BUTTON1){
        
                boolean isLock=false;
                
                //Si la liste est vide, pas la peinne de continuer
                if (liste.isEmpty()) return; 

                //On passe temporairement en gridLock = false
                if (gridLock){

                    isLock = true;
                    gridLock = false;
                
                };

                //Varible
                Point p = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
                //On met à jour pOld
                pOld = p; 
                double tmpDist;
                int tmpInd;
        
                //Si dCourant est null, alors on veut séléctionner une forme
                if (dCourant == null || dCourant instanceof Point){
                    
                    tmpDist = liste.get(0).distance(p);
                    tmpInd = 0;
                    
                    //On parcours toutes les formes
                    for (int i=0; i<liste.size(); i++){

                        //On compare et on prend la distance la plus ptite à chaque fois
                        if (tmpDist>liste.get(i).distance(p)) {
                            tmpDist = liste.get(i).distance(p);
                            tmpInd = i;
                        }

                    }

                    //Une fois qu'on a la distance la plus petite, on regarde si elle est asser petite
                    if (tmpDist<=2*(xMax-xMin)/100 || tmpDist<=2*(yMax-yMin)/100){

                        dCourant = liste.get(tmpInd);

                    }
                    
                    else dCourant = null;
                    
                }
                
                //Sinon on veut selectioner un point de cette forme
                else{
                    
                    tmpDist = dCourant.getConstituant().get(0).distance(p);
                    tmpInd = 0;
                    
                    //On parcours tous les points
                    for (int i=0; i<dCourant.getConstituant().size(); i++){

                        //On compare et on prend la distance la plus ptite à chaque fois
                        if (tmpDist>dCourant.getConstituant().get(i).distance(p)) {
                            tmpDist = dCourant.getConstituant().get(i).distance(p);
                            tmpInd = i;
                        }

                    }
                    
                    //Une fois qu'on a la distance la plus petite, on regarde si elle est asser petite
                    if (tmpDist<=2*(xMax-xMin)/100 || tmpDist<=2*(yMax-yMin)/100){

                        dCourant = dCourant.getConstituant().get(tmpInd);

                    }
                    
                    else{
                        
                        dCourant = null;
                        
                    }
                    
                }
                
                repaint();
                
                //Si on était en gridLock avant, on repasse en gripLock
                if (isLock) gridLock = true;

                return;
                
            }

        }
        
        //Si dCourant != NULL & on reste apuyyé
        public void mouseDragged(MouseEvent e){
            
            if(dCourant != null){
                
                pNew = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
                if (dragging){
             
                    dCourant.translate(pOld, pNew);
                    pOld = pNew;
                
                }
                
            }
            
            repaint();
            
        }
        
        //On remet pOld au bon endroit à chaque fois qu'on "commence" le mouseDragged pour éviter de la téléportation
        public void mousePressed(MouseEvent e){
            
            if (dCourant != null){

                pNew = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);            
                if(dCourant.distance(pNew)<=3*(xMax-xMin)/100 || dCourant.distance(pNew)<=3*(xMax-xMin)/100){
                    
                    dragging = true;
                    pOld = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);

                }
                
            }
            
        }
        
        public void mouseReleased(MouseEvent e){
            
            dragging = false;
            
        }
        
    }

    
    /*-   -    -    -   -   -   -   -   - CERCLE  -   -   -   -   -   -   -   -   -   -   -   -   -   -*/

    /**
     * Le MouseAdapter utilisé quand on dessine un cercle
     */
    class CircleMouseAdapter extends MouseAdapter {

        Point centre;

        @Override
        public void mouseClicked(MouseEvent e) {
            
            if(e.getButton() == MouseEvent.BUTTON1){

                //le premier clic, on crée le cercle et on l'affecte a dCourant
               if (centre == null) {
                   
                   centre = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
                   dCourant = new Cercle(centre, centre, (int)(currBrushSize*((xMax-xMin)*(yMax-yMin))/100),currBrushColor);
             
               }
               //le second clic, on l'ajoute a la liste et on reinitialise
               else {
                   Ardoise.this.liste.add(dCourant);
                   dCourant = null;
                   centre = null;
               }

            }
            
            //Si c'est clic droit on annule le tracage
            else if(e.getButton() == MouseEvent.BUTTON3){
                
               dCourant = null;
               centre = null;
                
            }
            
            repaint();

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            
            //si le centre a deja ete choisi, on reaffecte dCour en fonction de l'emplacement de la souris
            if (centre != null) {
                Point p = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
                dCourant = new Cercle(centre, p, (int)(currBrushSize*((xMax-xMin)*(yMax-yMin))/100),currBrushColor);
            }
            repaint();
        }

    }
    
    
    /*-   -    -    -   -   -   -   -   - LIGNE BRISEE  -   -   -   -   -   -   -   -   -   -   -   -   -   -*/

    
    class LigneBriseeMouseAdapter extends MouseAdapter {

        Point firstPoint;
        LigneBrisee tmpLigneBrisee;
        
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1){
   
                //Au premier clic, on créé la ligneBrisee
                if (firstPoint == null) {
                    firstPoint = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
                    tmpLigneBrisee = new LigneBrisee(firstPoint,(int)(currBrushSize*((xMax-xMin)*(yMax-yMin))/100),currBrushColor);

                }
                
                //Sinon on  ajoute un nouveau point
                else{
                    Point p= new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
                    tmpLigneBrisee.getPoints().add(p);

                }
                
            }
            
            //Si on fait un clic droit, on valide la figure
            else if (e.getButton() == MouseEvent.BUTTON3 && firstPoint!= null) {
             
                dCourant = tmpLigneBrisee;
                Ardoise.this.liste.add(dCourant);
                tmpLigneBrisee = null;
                firstPoint = null;
                dCourant= null;
            
            }
            
            repaint();

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            //si la ligne n'est pas vide (il y a des points), on reaffecte dCour en fonction de l'emplacement de la souris
            
            if (firstPoint!=null) {
                
                Point tmp = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
                ArrayList<Point> tmpList = (ArrayList<Point>)tmpLigneBrisee.getPoints().clone();
                tmpList.add(tmp);
                dCourant = new LigneBrisee(tmpList,(int)(currBrushSize*((xMax-xMin)*(yMax-yMin))/100));
                
                
            }
           
             repaint();
             
        }

    }
    

    /*-   -    -    -   -   -   -   -   - POLYGONE  -   -   -   -   -   -   -   -   -   -   -   -   -   -*/
    
    class PolygoneMouseAdapter extends MouseAdapter {

        Point firstPoint=null;
        Polygone tmpPolygone;

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1){
   
                //Au premier clic, on init. le ppollygone
                if (firstPoint == null) {
                    firstPoint = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
                    tmpPolygone = new Polygone(firstPoint, (int)(currBrushSize*((xMax-xMin)*(yMax-yMin))/100),currBrushColor);
                }
                
                //Sinon on  ajoute un nouveau point
                else{
                    Point p= new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
                    tmpPolygone.getPoints().add(p);
                }

            }
            
            //Si on fait un clic droit, on valide la figure
            else if (e.getButton() == MouseEvent.BUTTON3 && tmpPolygone.getPoints().size()>=2) {
             
                dCourant = tmpPolygone;
                Ardoise.this.liste.add(dCourant);
                tmpPolygone = null;
                firstPoint = null;
                dCourant= null;
            
            }
            
            repaint();

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            //si la ligne n'est pas vide (il y a des points), on reaffecte dCour en fonction de l'emplacement de la souris
            if (firstPoint!=null) {
                
                Point tmp = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
                ArrayList<Point> tmpList = (ArrayList<Point>)tmpPolygone.getPoints().clone();
                tmpList.add(tmp);
                dCourant = new Polygone(tmpList, (int)(currBrushSize*((xMax-xMin)*(yMax-yMin))/100));
                
            }
            
            repaint();
        
        }

    }

    
    /*-   -    -    -   -   -   -   -   - PARALLELOGRAMME  -   -   -   -   -   -   -   -   -   -   -   -   -   -*/

    class ParallelogrammeMouseAdapter extends MouseAdapter {

        Point firstPoint=null, p, lastPoint=null;
        Vecteur vect=null;
        Polygone tmpPar;

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1){
   
                //Au premier clic, on initialise le le Parallelograme
                if (firstPoint == null) {
                    firstPoint = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
                    tmpPar = new Polygone(firstPoint, (int)(currBrushSize*((xMax-xMin)*(yMax-yMin))/100),currBrushColor);
                }
                
                //Au deuxième clic on récupère "le vecteur" qui va nous permettre de tracer le dernier point
                else if(vect == null){
                    p =new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
                    vect = new Vecteur(firstPoint,p);
                    tmpPar.getPoints().add(p);
                }
                
                //Au torisième point on place aussi le dernier point (3ème-Vect)
                else{
                    p= new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
                    tmpPar.getPoints().add(p);
                    lastPoint = new Point(p.x-vect.getX(), p.y-vect.getY(),1);
                    tmpPar.getPoints().add(lastPoint);
                    
                     //On ajoute la figure
                    dCourant = tmpPar;
                    Ardoise.this.liste.add(dCourant);
                    tmpPar = null;
                    firstPoint = null;
                    vect = null;
                    dCourant= null;

                }

            }
            
            //Si on fait un clic droit,on annule la figure
            else if (e.getButton() == MouseEvent.BUTTON3 && firstPoint!= null) {
             
                tmpPar.getPoints().clear();
                tmpPar = null;
                firstPoint = null;
                vect=null;
                dCourant= null;
            
            }
            
            repaint();

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            //si la ligne n'est pas vide (il y a des points), on reaffecte dCour en fonction de l'emplacement de la souris
            if (firstPoint!=null) {
                
                Point tmp = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
                ArrayList<Point> tmpList = (ArrayList<Point>)tmpPar.getPoints().clone();
                tmpList.add(tmp);
                
                if(tmpPar.getPoints().size()==2){
                
                    tmpList.add(new Point(tmp.x-vect.getX(), tmp.y-vect.getY(),1));
                
                }

                dCourant = new Polygone(tmpList, (int)(currBrushSize*((xMax-xMin)*(yMax-yMin))/100));
                
            }
            
            repaint();
        
        }

    }
    
    
    /*-   -    -    -   -   -   -   -   - RECTANGLE  -   -   -   -   -   -   -   -   -   -   -   -   -   -*/
    class RectangleMouseAdapter extends MouseAdapter {

        Point firstPoint=null, p, p2, lastPoint=null;
        Vecteur vect=null;
        Polygone tmpPar;

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1){
   
                //Au premier clic, on créé (initialise) le rectangle
                if (firstPoint == null) {
                    firstPoint = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
                    tmpPar = new Polygone(firstPoint, (int)(currBrushSize*((xMax-xMin)*(yMax-yMin))/100),currBrushColor);
                }
                
                //Au deuxième clic on récupère "le vecteur" qui va nous permettre de tracer le dernier point
                else if(vect == null){
                    p =new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
                    vect = new Vecteur(firstPoint,p);
                    tmpPar.getPoints().add(p);
                }
                
                //Au torisième point on place aussi le dernier point (en prennant en compte l'équation de la droite perpandiculaire à Vect)
                else{
                    
                    p2= new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
                    if (Math.abs(vect.getX())>=Math.abs(vect.getY()))  p2.x = (p2.y - p.y)/(-vect.getX()/vect.getY())+p.x;
                    else p2.y = (p2.x - p.x)*(-vect.getX()/vect.getY())+p.y;
                    
                    tmpPar.getPoints().add(p2);
                    //On rajoute le derrnier point (3ème point-vect)
                    lastPoint = new Point(p2.x-vect.getX(), p2.y-vect.getY(),1);
                    tmpPar.getPoints().add(lastPoint);
                    
                    //On ajoute la figure
                    dCourant = tmpPar;
                    Ardoise.this.liste.add(dCourant);
                    tmpPar = null;
                    firstPoint = null;
                    vect = null;
                    dCourant= null;
                    
                }

            }
            
            //Si on fait un clic droit,on annule la figure
            else if (e.getButton() == MouseEvent.BUTTON3 && firstPoint!= null) {
             
                tmpPar.getPoints().clear();
                tmpPar = null;
                vect=null;
                firstPoint = null;
                dCourant= null;
            
            }
            
            repaint();

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            //si la ligne n'est pas vide (il y a des points), on reaffecte dCour en fonction de l'emplacement de la souris
            if (firstPoint!=null) {
                
                Point tmp = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
                ArrayList<Point> tmpList = (ArrayList<Point>)tmpPar.getPoints().clone();
                
                if(tmpPar.getPoints().size()==2){
                    //Si on place un recatngle plus Vertical, on calcule x en fonction de y (pour une utilisation plus propore)    
                    if (Math.abs(vect.getX())>=Math.abs(vect.getY())) tmp.x = (tmp.y - p.y)/(-vect.getX()/vect.getY())+p.x;
                    //Sinon on calcule y en fonction de x
                    else tmp.y = (tmp.x - p.x)*(-vect.getX()/vect.getY())+p.y;

                    tmpList.add(tmp);
                    tmpList.add(new Point(tmp.x-vect.getX(), tmp.y-vect.getY(),1));

                }
                
                else tmpList.add(tmp);

                dCourant = new Polygone(tmpList, (int)(currBrushSize*((xMax-xMin)*(yMax-yMin))/100));
                
            }
            
            repaint();
        
        }

    }
    
    /*-   -    -    -   -   -   -   -   - CRAYON/PENCIL  -   -   -   -   -   -   -   -   -   -   -   -   -   -*/
    class PencilMouseAdapter extends MouseAdapter{
        
        //Variables 
        Point newPoint = null;
        Point lastPoint = null;
        LigneBrisee tmpPolyline;
        
        public void mousePressed(MouseEvent e){

            lastPoint = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
            tmpPolyline = new LigneBrisee(lastPoint, (int)(currBrushSize*((xMax-xMin)*(yMax-yMin))/100),currBrushColor);
            repaint();
        }
        
        public void mouseDragged(MouseEvent e){
            
            newPoint = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
            
            if (newPoint.distance(lastPoint)>=10*(xMax-xMin)*(yMax-yMin)/10000){
                
                tmpPolyline.getPoints().add(newPoint);
                dCourant = tmpPolyline;
                lastPoint = newPoint;
                repaint();
                
            }
            
        }
        
        public void mouseReleased(MouseEvent e){
            
            liste.add(tmpPolyline);
            dCourant = null;
            repaint();
            
        }
        
    }
    
    /*-   -    -    -   -   -   -   -   - MOVE -   -   -   -   -   -   -   -   -   -   -   -   -   -*/
    class MoveMouseAdapter extends MouseAdapter{
        
        //Variables 
        Point newPoint = null;
        Point lastPoint = null;
            
        
        public void mousePressed(MouseEvent e){

            lastPoint = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
            repaint();
        }
        
        
        public void mouseDragged(MouseEvent e){
            
            
            newPoint = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
            
            xMin -= newPoint.x - lastPoint.x;
            xMax -= newPoint.x - lastPoint.x;
        
            yMin -= newPoint.y - lastPoint.y;
            yMax -= newPoint.y - lastPoint.y;
            
            app.setZoomFiedls(xMin, xMax, yMin, yMax);

            
            repaint();
                                
        }
        
    }
    
    /*-   -    -    -   -   -   -   -   - TEXT  -   -   -   -   -   -   -   -   -   -   -   -   -   -*/
    class TextMouseAdapter extends MouseAdapter{
        
        //Variables
        Point p;
        
        public void mouseClicked(MouseEvent e){

            p = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
            
            if(e.getButton() == MouseEvent.BUTTON1){
                
                Text t = new Text(app.getText(), p, (int)(currBrushSize*((xMax-xMin)*(yMax-yMin))/100), currBrushColor);
                liste.add(t);
                repaint();
                
            }

            else if (e.getButton() == MouseEvent.BUTTON3){
                
                app.openTextFrame();
                
            }

            
        }
        
    }
    
    /*-   -    -    -   -   -   -   -   - ZOOM  -   -   -   -   -   -   -   -   -   -   -   -   -   -*/
    class ZoomMouseAdapter extends MouseAdapter{
        
        //Variables 
        Point secondPoint = null;
        Point firstPoint = null;
        Polygone tmpRect;
        ArrayList<Point> tmpList = new ArrayList<Point>();
            
        
        public void mousePressed(MouseEvent e){

            firstPoint = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
            
        }
        
        
        public void mouseDragged(MouseEvent e){
            
            secondPoint = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
            
            tmpList.clear();
            tmpList.add(firstPoint);
            tmpList.add(new Point(firstPoint.x, secondPoint.y,1));
            tmpList.add(secondPoint);
            tmpList.add(new Point(secondPoint.x, firstPoint.y,1));
            
            zoomRect = new Polygone(tmpList,2);
            
           
            repaint();
                                
        }
        
        public void mouseReleased(MouseEvent e){
            
            if(secondPoint == null) return;
            
            zoomRect = null;
            
            xMax = secondPoint.x;
            xMin = firstPoint.x;
            yMax = firstPoint.y;
            yMin = secondPoint.y;
            app.setZoomFiedls(xMin, xMax, yMin, yMax);
   
            repaint();
            
        }
        
    }
    
     /*-   -    -    -   -   -   -   -   - DROITE/RIGHT  -   -   -   -   -   -   -   -   -   -   -   -   -   -*/

    /**
     * Le MouseAdapter utilisé quand on dessine un cercle
     */
    class RightMouseAdapter extends MouseAdapter {

        Point firstPoint;

        @Override
        public void mouseClicked(MouseEvent e) {
            
            if(e.getButton() == MouseEvent.BUTTON1){

                //le premier clic, on crée le cercle et on l'affecte a dCourant
               if (firstPoint == null) {
                   
                   firstPoint = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
                   dCourant = new Right(firstPoint, firstPoint, (int)(currBrushSize*((xMax-xMin)*(yMax-yMin))/100),currBrushColor);
             
               }
               //le second clic, on l'ajoute a la liste et on reinitialise
               else {
                   Ardoise.this.liste.add(dCourant);
                   dCourant = null;
                   firstPoint = null;
               }

            }
            
            //Si c'est clic droit on annule le tracage
            else if(e.getButton() == MouseEvent.BUTTON3){
                
               dCourant = null;
               firstPoint = null;
                
            }
            
            repaint();

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            
            //si le centre a deja ete choisi, on reaffecte dCour en fonction de l'emplacement de la souris
            if (firstPoint != null) {
                Point p = new Point(getXRepere(e.getX()), getYRepere(e.getY()),1);
                dCourant = new Right(firstPoint, p, (int)(currBrushSize*((xMax-xMin)*(yMax-yMin))/100),currBrushColor);
            }
            repaint();
        }

    }
    
    
}