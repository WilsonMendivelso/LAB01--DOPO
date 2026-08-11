import javax.swing.JOptionPane;

/**
 * Represents a Robot
. * 
 * @author Wilson Mendivelso, David Garzon
 * @version version 1
 */
public class Robot{
    private int positionX;
    private int positionY;
    private boolean isVisible;
    private char direction;
    private int health;
    private int[] lastMove; 
    private Rectangle[] CuerpoRectangular;
    private Circle[] CuerpoCircular;
    private Triangle[] CuerpoTriangular;
    private int distanceMove;
    
    /**
     * Constructor for objects of class Robot
     * @param x is Position in x
     * @param y is Position in y
     */
    public Robot(int x, int y){
        positionX = x;
        positionY = y;
        direction = 'N';
        health = 10;
        lastMove = new int[2];
        
        distanceMove = 20;

        
        CuerpoCircular= new Circle[5];
        CuerpoCircular[0] = new Circle();
        CuerpoCircular[0].changeSize(20);
        CuerpoCircular[0].changeColor("red");
        CuerpoCircular[0].moveHorizontal(1+(distanceMove/25)-CuerpoCircular[0].getCoordenates()[0]+20*x);
        CuerpoCircular[0].moveVertical((-1-CuerpoCircular[0].getCoordenates()[1]+20*y));

        
        CuerpoCircular[1] = new Circle();
        CuerpoCircular[1].changeColor("white");

                
        CuerpoCircular[2] = new Circle();
        CuerpoCircular[2].changeColor("white");
       
        CuerpoRectangular= new Rectangle[1];
        CuerpoRectangular[0] = new Rectangle();
        CuerpoRectangular[0].changeColor("red");

        
        CuerpoTriangular = new Triangle[3];
        CuerpoTriangular[0] = new Triangle();
        CuerpoTriangular[0].changeColor("white");
        
        CuerpoTriangular[1] = new Triangle();
        CuerpoTriangular[1].changeColor("white");
                
        CuerpoTriangular[2] = new Triangle();
        CuerpoTriangular[2].changeColor("white");
        
        changePixelSize(CuerpoCircular[0].getDiameter());    
    }
    
    /**
     * Return Robot's coordinates
     * @return Robot's coordinates
     */
    public int[] coordinates(){
        int[] coords = new int[2];
        coords[0]=positionX;
        coords[1]=positionY;
        return coords;
    }
    
    /**
     * Return Robot's direction
     * @return Robot's direction
     */
    public char direction(){
        return direction;
    }
    
    /**
     * Se encarga de mover al Robot
     * @param step son la cantidad de pasos que se mueve, puede ser negativo.
     */
    public void move(int step){
        for(int i = 0; i < Math.abs(step); i++){
            if(direction == 'N'){
                if(positionY - (step)/Math.abs(step) >= 0){
                    positionY = positionY - (step)/Math.abs(step);
                    makeInvisible();       
                    CuerpoCircular[0].moveVertical(-distanceMove);
                    moveBody();

                }
                else{
                    JOptionPane.showMessageDialog(null, "Movimiento erróneo.");
                    break;
                }
            }
            else if(direction == 'S'){
                if(positionY + (step)/Math.abs(step) < 1+(int)(280/distanceMove)){
                    positionY = positionY + (step)/Math.abs(step);
                    makeInvisible(); 
                    CuerpoCircular[0].moveVertical(distanceMove);
                    moveBody();

                }
                else{
                    JOptionPane.showMessageDialog(null, "Movimiento erróneo.");
                    break;
                }
            }
            else if(direction == 'E'){
                if(positionX + (step)/Math.abs(step) <1+ (int)(280/distanceMove)){
                    positionX = positionX + (step)/Math.abs(step);
                    makeInvisible(); 
                    CuerpoCircular[0].moveHorizontal(distanceMove);
                    moveBody();

                }
                else{
                    JOptionPane.showMessageDialog(null, "Movimiento erróneo.");
                    break;
                }
            }else if(direction == 'W'){
                if(positionX - (step)/Math.abs(step) >= 0){
                    positionX = positionX - (step)/Math.abs(step);                    
                    makeInvisible(); 
                    CuerpoCircular[0].moveHorizontal(-distanceMove);
                    moveBody();

                }
                else{
                    JOptionPane.showMessageDialog(null, "Movimiento erróneo.");
                    break;
                }
            }
            
            if(!isOk()){
                JOptionPane.showMessageDialog(null, "Fin del juego. ¡JAJA!");
            }
            
            lastMove[0] = positionX;
            lastMove[1] = positionY;
            
        }
        
    }
    
    /**
     * Return false if health is equal to 0
     * @return false if Robot is death.
     */
    public boolean isOk(){
        if (health==0){
            return false;
        }
        return true;
    }   
    
    /**
     * Change Robot's direction
     * @param direction it only can be 'N', 'S', 'E', 'W'.
     */
    public void turn(char direction){
        this.direction = direction;
        makeInvisible();
    }
    /**
     * Makes visible all Robot's body parts.
     */
    public void makeVisible(){
        for(Rectangle i: CuerpoRectangular){
            i.makeVisible();
        }
        CuerpoCircular[0].makeVisible();
        for(Triangle i: CuerpoTriangular){
            i.makeVisible();
        }
        CuerpoCircular[1].makeVisible();
        CuerpoCircular[2].makeVisible();

        
        
        Circle ojo1 = new Circle();
        ojo1.changeSize(1+CuerpoCircular[1].getDiameter()/2);
        ojo1.moveHorizontal(-20-1 + (int)(CuerpoCircular[1].getCoordenates()[0]+ ojo1.getDiameter()/2) - ojo1.getDiameter()/6);
        ojo1.moveVertical(-15 + CuerpoCircular[1].getCoordenates()[1] + ojo1.getDiameter()/2);
        ojo1.changeColor("blue");
        
        
        Circle ojo2 = new Circle();
        ojo2.changeSize(1+CuerpoCircular[1].getDiameter()/2);
        ojo2.moveHorizontal(-20-1 + (int)(CuerpoCircular[2].getCoordenates()[0] + ojo2.getDiameter()/2) - ojo2.getDiameter()/6);
        ojo2.moveVertical(-15 + CuerpoCircular[2].getCoordenates()[1] + ojo2.getDiameter()/2);
        ojo2.changeColor("blue");
        
        if(direction == 'N'){
            ojo1.moveVertical(-1-ojo1.getDiameter()/2);
            ojo1.moveHorizontal(1);
            ojo2.moveVertical(-1-ojo2.getDiameter()/2);
            ojo2.moveHorizontal(1);
        }
        else if(direction == 'S'){
            ojo1.moveVertical(ojo1.getDiameter()/2);
            ojo2.moveVertical(ojo2.getDiameter()/2);
        }        
        else if(direction == 'W'){
            ojo1.moveHorizontal(-ojo1.getDiameter()/2);
            ojo2.moveHorizontal(-ojo2.getDiameter()/2);
        }else if(direction == 'E'){
            ojo1.moveHorizontal(1+ojo1.getDiameter()/2);
            ojo2.moveHorizontal(1+ojo2.getDiameter()/2);
        }
        
        ojo1.makeVisible();
        ojo2.makeVisible();
        
        CuerpoCircular[3] = ojo1;
        CuerpoCircular[4] = ojo2;
    }
    /**
     * Makes invisible all Robot's body parts.
     */
    public void makeInvisible(){
        for(Rectangle i: CuerpoRectangular){
            i.makeInvisible();
        }
        for(Circle i: CuerpoCircular){
            if( i!= null){
                i.makeInvisible();
            }
            
        }
        for(Triangle i: CuerpoTriangular){
            i.makeInvisible();
        }
    }
    
    /**
     * Scales Robot's body parts to different pixels scales.
     * @param pixel is the pixel size
     */
    public void changePixelSize(int pixel){
        makeInvisible();
        CuerpoCircular[0].moveHorizontal(-(distanceMove/25));
        
        distanceMove = pixel;
        
        CuerpoCircular[0].changeSize(pixel-5);
        CuerpoCircular[0].moveHorizontal((distanceMove/25));
        
        CuerpoCircular[1].changeSize((int)CuerpoCircular[0].getDiameter()/4);

        CuerpoCircular[2].changeSize((int)CuerpoCircular[0].getDiameter()/4);

        CuerpoRectangular[0].changeSize((CuerpoCircular[0].getDiameter()*3/5),CuerpoCircular[0].getDiameter());
    
        CuerpoTriangular[0].changeSize((CuerpoCircular[0].getDiameter()/5),(CuerpoCircular[0].getDiameter()*3/10));

        CuerpoTriangular[1].changeSize((CuerpoCircular[0].getDiameter()/5),(CuerpoCircular[0].getDiameter()*3/10));

        CuerpoTriangular[2].changeSize((CuerpoCircular[0].getDiameter()/5),(CuerpoCircular[0].getDiameter()*3/10));

        moveBody();
    }
    
    /**
     * It moves all the body parts of the Robot.
     */
    private void moveBody(){
        makeInvisible();
        
        CuerpoCircular[1].moveHorizontal(-CuerpoCircular[1].getCoordenates()[0]+ (int)((CuerpoCircular[0].getCoordenates()[0])) + 6*CuerpoCircular[1].getDiameter()/7);
        CuerpoCircular[1].moveVertical(-CuerpoCircular[1].getCoordenates()[1]+ (int)((CuerpoCircular[0].getCoordenates()[1])) + CuerpoCircular[1].getDiameter());
        
        CuerpoCircular[2].moveHorizontal(-CuerpoCircular[2].getCoordenates()[0] + (int)((CuerpoCircular[0].getCoordenates()[0])) + 7*CuerpoCircular[2].getDiameter()/3);
        CuerpoCircular[2].moveVertical(-CuerpoCircular[2].getCoordenates()[1] + (int)((CuerpoCircular[0].getCoordenates()[1])) + CuerpoCircular[2].getDiameter());
        
        CuerpoRectangular[0].moveHorizontal(-CuerpoRectangular[0].getCoordenates()[0] + (int)((CuerpoCircular[0].getCoordenates()[0])));
        CuerpoRectangular[0].moveVertical(-CuerpoRectangular[0].getCoordenates()[1] + (int)(14* CuerpoCircular[0].getDiameter()/25) + (int)((CuerpoCircular[0].getCoordenates()[1])));
        
        CuerpoTriangular[0].moveHorizontal(-CuerpoTriangular[0].getCoordenates()[0] + CuerpoTriangular[0].getSize()[1]/2 + CuerpoTriangular[0].getSize()[0]/10 + (int)(CuerpoCircular[0].getCoordenates()[0]));
        CuerpoTriangular[0].moveVertical(-CuerpoTriangular[0].getCoordenates()[1] + CuerpoCircular[0].getDiameter() - CuerpoTriangular[0].getSize()[0]/4 + (int)((CuerpoCircular[0].getCoordenates()[1])));

        CuerpoTriangular[1].moveHorizontal(-CuerpoTriangular[1].getCoordenates()[0] + 3*CuerpoTriangular[1].getSize()[1]/2 +3*CuerpoTriangular[1].getSize()[0]/10 + (int)(CuerpoCircular[0].getCoordenates()[0]));
        CuerpoTriangular[1].moveVertical(-CuerpoTriangular[1].getCoordenates()[1]+ CuerpoCircular[0].getDiameter() - CuerpoTriangular[1].getSize()[0]/4 + (int)((CuerpoCircular[0].getCoordenates()[1])));

        CuerpoTriangular[2].moveHorizontal(-CuerpoTriangular[2].getCoordenates()[0]+ 5*CuerpoTriangular[2].getSize()[1]/2 + 5*CuerpoTriangular[2].getSize()[0]/10 + (int)(CuerpoCircular[0].getCoordenates()[0]));
        CuerpoTriangular[2].moveVertical(-CuerpoTriangular[2].getCoordenates()[1]+ CuerpoCircular[0].getDiameter() - CuerpoTriangular[2].getSize()[0]/4 + (int)((CuerpoCircular[0].getCoordenates()[1])));
    
    }
    
    /**
     * Gets robot's health.
     * @return robot's health.
     */
    public int getHealth(){
        return health;
    }
    
    /**
     * Robot's health is decreased by one.
     */
    public void lessOneHearth(){
        health--;
    }
    
    /**
     * Shows that robot is death
     */
    public void makeVisibleDeathRobot(){
        makeInvisible();
        CuerpoCircular[0].changeColor("blue");
        CuerpoCircular[0].makeVisible();
        
        CuerpoRectangular[0].changeColor("blue");
        CuerpoRectangular[0].makeVisible();
        
        for(Triangle i: CuerpoTriangular){
            i.makeVisible();
        }
        
        Rectangle eye1 = new Rectangle();
        eye1.changeSize((int)(CuerpoCircular[0].getDiameter()/7), (int)(CuerpoCircular[0].getDiameter()/7));
        eye1.changeColor("white");
        eye1.moveHorizontal(-eye1.getCoordenates()[0]+ (int)((CuerpoCircular[0].getCoordenates()[0]*20)/20) +3*CuerpoCircular[0].getDiameter()/5);
        eye1.moveVertical(-eye1.getCoordenates()[1]+ (int)((CuerpoCircular[0].getCoordenates()[1]*20)/20) + 3*CuerpoCircular[0].getDiameter()/10);
        eye1.makeVisible();

        Rectangle eye2 = new Rectangle();
        eye2.changeSize((int)(CuerpoCircular[0].getDiameter()/7), (int)(CuerpoCircular[0].getDiameter()/7));
        eye2.changeColor("white");
        eye2.moveHorizontal(-eye2.getCoordenates()[0]+ (int)((CuerpoCircular[0].getCoordenates()[0]*20)/20) +CuerpoCircular[0].getDiameter()/4);
        eye2.moveVertical(-eye2.getCoordenates()[1]+ (int)((CuerpoCircular[0].getCoordenates()[1]*20)/20) + 3*CuerpoCircular[0].getDiameter()/10);
        eye2.makeVisible();
        
        /**
        Rectangle boca1 = new Rectangle();
        boca1.changeSize((int)(CuerpoCircular[0].getDiameter()/14), (int)(CuerpoCircular[0].getDiameter()/7));
        boca1.changeColor("green");
        boca1.moveHorizontal(-boca1.getCoordenates()[0]+ (int)(CuerpoCircular[0].getCoordenates()[0]) +2* boca1.getSize()[0]);
        boca1.moveVertical(-boca1.getCoordenates()[1]+ 17*(CuerpoCircular[0].getCoordenates()[1])/10);
        boca1.makeVisible();
        
        Rectangle boca2 = new Rectangle();
        boca2.changeSize((int)(CuerpoCircular[0].getDiameter()/14), (int)(CuerpoCircular[0].getDiameter()/7));
        boca2.changeColor("green");
        boca2.moveHorizontal(-boca2.getCoordenates()[0]+ (int)(CuerpoCircular[0].getCoordenates()[0]) +24* boca2.getSize()[0]/5);
        boca2.moveVertical(-boca2.getCoordenates()[1]+ 27*(CuerpoCircular[0].getCoordenates()[1])/10);
        boca2.makeVisible();
        
        Rectangle boca3 = new Rectangle();
        boca3.changeSize((int)(CuerpoCircular[0].getDiameter()/14), (int)(CuerpoCircular[0].getDiameter()/7));
        boca3.changeColor("green");
        boca3.moveHorizontal(-boca3.getCoordenates()[0]+ (int)(CuerpoCircular[0].getCoordenates()[0]) +38* boca3.getSize()[0]/5);
        boca3.moveVertical(-boca3.getCoordenates()[1]+ 29*(CuerpoCircular[0].getCoordenates()[1])/10);
        boca3.makeVisible();
        
        Rectangle boca4 = new Rectangle();
        boca4.changeSize((int)(CuerpoCircular[0].getDiameter()/14), (int)(CuerpoCircular[0].getDiameter()/7));
        boca4.changeColor("green");
        boca4.moveHorizontal(-boca4.getCoordenates()[0]+ (int)(CuerpoCircular[0].getCoordenates()[0]) +10* boca4.getSize()[0]);
        boca4.moveVertical(-boca4.getCoordenates()[1]+ 27*(CuerpoCircular[0].getCoordenates()[1])/10);
        boca4.makeVisible();
               
        Rectangle boca5 = new Rectangle();
        boca5.changeSize((int)(CuerpoCircular[0].getDiameter()/14), (int)(CuerpoCircular[0].getDiameter()/7));
        boca5.changeColor("green");
        boca5.moveHorizontal(-boca5.getCoordenates()[0]+ (int)(CuerpoCircular[0].getCoordenates()[0]) +63* boca5.getSize()[0]/5);
        boca5.moveVertical(-boca5.getCoordenates()[1]+ 29*(CuerpoCircular[0].getCoordenates()[1])/10);
        boca5.makeVisible();
        */
    }
}