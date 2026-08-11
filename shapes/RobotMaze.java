import javax.swing.JOptionPane;

/**
 * Represents the laberinth with the Robot
 * 
 * @author Wilson Mendivelso, David Garzon
 * @version 1.0
 */
public class RobotMaze
{
    Canvas tablero;
    Robot robot;
    
    /**
     * Constructor for objects of class RobotMaze
     */
    public RobotMaze(){
        tablero = Canvas.getCanvas();
        int tamaño = 5;
        while(tamaño < 6 || tamaño > 14 ){
            String tamLab = JOptionPane.showInputDialog("Tamaño del laberinto (entre 6 y 14)");
            tamaño = Integer.parseInt(tamLab);
        }
        tablero.setVisible(true);
        int pantalla =tablero.getJPanel().getWidth();
        for(int i = 0; i<=tamaño; i++){
            for(int j = 0; j <=tamaño; j++){
                Rectangle cuadrado = new Rectangle();
                cuadrado.moveHorizontal(-cuadrado.getCoordenates()[0] + i*(280/tamaño));
                cuadrado.moveVertical(-cuadrado.getCoordenates()[1] + j*(280/tamaño));
                cuadrado.changeSize((int)((pantalla-pantalla/tamaño+1)/tamaño), (int)((pantalla-pantalla/tamaño+1)/tamaño));
                if((i+j)%2 == 0){
                    cuadrado.changeColor("black");
                }
                else{
                    cuadrado.changeColor("white");
                }
                
                if(i==0 || j== 0 || i== tamaño || j==tamaño){
                    cuadrado.changeColor("gray");
                }
                cuadrado.makeVisible();
            }
        }
        robot = new Robot(14,14);
        robot.changePixelSize((int)(280/tamaño));
        
        while(robot.isOk()){
            String[] opciones = {"Moverse", "Cambiar dirección", "Terminar el juego"};
            
            int opcion = JOptionPane.showOptionDialog(
                tablero.getJPanel(),
                "¿Qué deseas hacer?",
                "¿Qué deseas hacer?",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                "Moverse");  
            if (opcion == 0){
                String time = JOptionPane.showInputDialog("Cuantas veces? ");
                int times = Integer.parseInt(time);
                movingRobot(times);
            
            }else if (opcion == 1){
                String dir = JOptionPane.showInputDialog("A donde? ");
                char direction = dir.charAt(0);
                if (dir.charAt(0)!= 'W' || dir.charAt(0)!= 'S' || dir.charAt(0)!= 'N' || dir.charAt(0)!= 'E'){
                    JOptionPane.showMessageDialog(null, "Opcion invalida, solamente: (N, S, E, W)");
                }else{    
                    turningRobot(direction);
                }              
            }else if (opcion == 2){
                int va= getRobotsHealth();
                for(int i = 0; i<=(va) ; i++){
                    robotWasDamaged();
                }
                robot.makeVisibleDeathRobot();
                JOptionPane.showMessageDialog(null, "!Termino el juego! :( ");
                break;
            }

        }
    }
    
    
    /**
     * It moves the robot.
     * @param times it indicates how many times the robot will be moved
     */
    public void movingRobot(int times){
        robot.move(times);
    }
    
    /**
     * It turns robot to a specific direction
     * @param direction is the new direction
     */
    public void turningRobot(char direction){
        robot.turn(direction);
    }

    /**
     * Gets robot's health.
     * @return robot's health.
     */
    public int getRobotsHealth(){
        return robot.getHealth();
    }
    
    /**
     * If robot was damaged it will decreased it health by one.
     */
    public void robotWasDamaged(){
        robot.lessOneHearth();
    }
}