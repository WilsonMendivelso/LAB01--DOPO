
/**
 * Represents a Robot
 * 
 * @author Wilson Mendivelso, David Garzon
 * @version version 1
 */
public class Robot{
    private int positionX;
    private int positionY;
    private boolean isVisible;
    private char direction;
    private int health;
    
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
    
    public void move(int step){
        
        
    }
    
    /**
     * return false if healt = 0
     * @return 
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
}