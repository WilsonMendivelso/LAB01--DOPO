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
    private Circle[] Cuerpo2;
    private Triangle[] Cuerpo3;
    
    /**
     * Constructor for objects of class Robot
     * @param x is Position in x
     * @param y is Position in y
     */
    public Robot(int x, int y){
        positionX = x;
        positionY = y;
        direction = 'N';
        health = 0;
        lastMove = new int[2]; 
        
        
        //Inicializar cuerpos
        
        //Dibujarlos
        
        CuerpoRectangular= new Rectangle[1];
        CuerpoRectangular[0] = new Rectangle();
        CuerpoRectangular[0].changeSize(30,50);
        CuerpoRectangular[0].changeColor("red");
        CuerpoRectangular[0].moveHorizontal(-50);
        CuerpoRectangular[0].moveVertical(28);
        
        
        Cuerpo2= new Circle[5];
        Cuerpo2[0] = new Circle();
        Cuerpo2[0].changeSize(50);
        Cuerpo2[0].changeColor("red");
        
        Cuerpo2[1] = new Circle();
        Cuerpo2[1].changeSize(12);
        Cuerpo2[1].changeColor("white");
        Cuerpo2[1].moveHorizontal(9);
        Cuerpo2[1].moveVertical(15);
                
        Cuerpo2[2] = new Circle();
        Cuerpo2[2].changeSize(12);
        Cuerpo2[2].changeColor("white");
        Cuerpo2[2].moveHorizontal(27);
        Cuerpo2[2].moveVertical(15);
        
        Cuerpo3 = new Triangle[3];
        Cuerpo3[0] = new Triangle();
        Cuerpo3[0].changeSize(10,15);
        Cuerpo3[0].moveHorizontal(-112);
        Cuerpo3[0].moveVertical(48);
        Cuerpo3[0].changeColor("white");

        Cuerpo3[1] = new Triangle();
        Cuerpo3[1].changeSize(10,15);
        Cuerpo3[1].moveHorizontal(-95);
        Cuerpo3[1].moveVertical(48);
        Cuerpo3[1].changeColor("white");
                
        Cuerpo3[2] = new Triangle();
        Cuerpo3[2].changeSize(10,15);
        Cuerpo3[2].moveHorizontal(-79);
        Cuerpo3[2].moveVertical(48);
        Cuerpo3[2].changeColor("white");

        
        makeVisible();
        
        
        
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
                }
                else{
                    JOptionPane.showMessageDialog(null, "Movimiento erróneo.");
                    break;
                }
            }
            else if(direction == 'S'){
                if(positionY + (step)/Math.abs(step) >= 0){
                    positionY = positionY + (step)/Math.abs(step);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Movimiento erróneo.");
                    break;
                }
            }
            else if(direction == 'E'){
                if(positionX + (step)/Math.abs(step) >= 0){
                    positionX = positionX + (step)/Math.abs(step);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Movimiento erróneo.");
                    break;
                }
            }else if(direction == 'W'){
                if(positionX - (step)/Math.abs(step) >= 0){
                    positionX = positionX - (step)/Math.abs(step);
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
        makeVisible();
    }
    
    public void makeVisible(){
        for(Rectangle i: CuerpoRectangular){
            i.makeVisible();
        }
        Cuerpo2[0].makeVisible();
        for(Triangle i: Cuerpo3){
            i.makeVisible();
        }
        Cuerpo2[1].makeVisible();
        Cuerpo2[2].makeVisible();

        
        
        Circle ojo1 = new Circle();
        ojo1.moveHorizontal(-20 + Cuerpo2[1].getCoordenates()[0]+ 3);
        ojo1.moveVertical(-15 + Cuerpo2[1].getCoordenates()[1] + 3);
        ojo1.changeSize(6);
        ojo1.changeColor("blue");
        
        
        Circle ojo2 = new Circle();
        ojo2.moveHorizontal(-20 + Cuerpo2[2].getCoordenates()[0] + 3);
        ojo2.moveVertical(-15 + Cuerpo2[2].getCoordenates()[1] + 3);
        ojo2.changeColor("blue");
        ojo2.changeSize(6);
        if(direction == 'N'){
            ojo1.moveVertical(-3);
            ojo2.moveVertical(-3);
        }
        else if(direction == 'S'){
            ojo1.moveVertical(3);
            ojo2.moveVertical(3);
        }        
        else if(direction == 'W'){
            ojo1.moveHorizontal(-3);
            ojo2.moveHorizontal(-3);
        }else if(direction == 'E'){
            ojo1.moveHorizontal(3);
            ojo2.moveHorizontal(3);
        }
        
        ojo1.makeVisible();
        ojo2.makeVisible();
        
        Cuerpo2[3] = ojo1;
        Cuerpo2[4] = ojo2;
    }
    
    public void makeInvisible(){
        for(Rectangle i: CuerpoRectangular){
            i.makeInvisible();
        }
        for(Circle i: Cuerpo2){
            i.makeInvisible();
        }
        for(Triangle i: Cuerpo3){
            i.makeInvisible();
        }
    }
}