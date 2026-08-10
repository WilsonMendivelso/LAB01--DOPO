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
    private Rectangle[] Cuerpo1;
    private Triangle[] Cuerpo2;
    private Circle[] Cuerpo3;
    
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
    }
    
    public void makeVisible(){
        for(Rectangle i: Cuerpo1){
            i.makeVisible();
        }
        for(Triangle i: Cuerpo2){
            i.makeVisible();
        }
        for(Circle i: Cuerpo3){
            i.makeVisible();
        }
    }
    
    public void makeInvisible(){
        
    }
}