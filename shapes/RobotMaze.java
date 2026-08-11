import javax.swing.JOptionPane;
import java.util.Random;

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
    int[] entrada;
    int[] salida;
    Rectangle[][] posicionesCuadrados;

    /**
     * Constructor for objects of class RobotMaze
     */
    public RobotMaze(){
        tablero = Canvas.getCanvas();
        int tamaño = 5;
        while(tamaño < 7 || tamaño > 14 ){
            String tamLab = JOptionPane.showInputDialog("Tamaño del laberinto (entre 7 y 14)");
            tamaño = Integer.parseInt(tamLab);
        }
        tablero.setVisible(true);
        int pantalla =tablero.getJPanel().getWidth();
        entrada = new int[2];
        salida = new int[2];
        posicionesCuadrados = new Rectangle[tamaño+1][tamaño+1];
        generateEntryExit(tamaño);

        
        for(int i = 0; i<=tamaño; i++){
            for(int j = 0; j <=tamaño; j++){
                Rectangle cuadrado = new Rectangle();
                cuadrado.moveHorizontal(-cuadrado.getCoordenates()[0] + i*(280/tamaño));
                cuadrado.moveVertical(-cuadrado.getCoordenates()[1] + j*(280/tamaño));
                cuadrado.changeSize((int)((pantalla-pantalla/tamaño+1)/tamaño), (int)((pantalla-pantalla/tamaño+1)/tamaño));
                cuadrado.changeColor("white");
                if(i==0 || j== 0 || i== tamaño || j==tamaño){
                    cuadrado.changeColor("black");
                }
                
                if(i==entrada[0] && j==entrada[1]){
                    cuadrado.changeColor("blue");
                }
                
                if(i==salida[0] && j==salida[1]){
                    cuadrado.changeColor("yellow");
                }
                
                posicionesCuadrados[i][j]=cuadrado;
                cuadrado.makeVisible();
            }
        }
        
        robot = new Robot(0,0);
        robot.changePixelSize((280/tamaño));
        if(entrada[0] !=0){
            robot.makeInvisible();
            robot.turn('E');
            robot.move(entrada[0]);
        }
        else{
            robot.makeInvisible();
            robot.turn('S');
            robot.move(entrada[1]);

        }
        robot.turn('N');
        robot.makeVisible();

        JOptionPane.showMessageDialog(null, "Se un buen fantasma y acaba con PacMan, intenta no chocar, si no, perderás todas tus vidas.");
        boolean si = true;
        
        
         while(robot.isOk() && si){
            String[] opciones = {"Moverse", "Cambiar dirección", "Terminar el juego", "Mostrar vida"};
            
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
                String time = JOptionPane.showInputDialog("¿Cuántas veces? ");
                if(time !=null){
                    int times = Integer.parseInt(time);
                    for(int i = 0; i< times; i++){
                        int[] coords = robot.coordinates();
                        int posX=coords[0];
                        int posY=coords[1];
                        
                        //Puede pasar que inicie y se vaya para la izquierda
                        if(robot.direction() =='N'){
                            if(posicionesCuadrados[posX][posY-1].getColor() == "black" || posicionesCuadrados[posX][posY-1].getColor() == "green"){
                                robotWasDamaged();
                                posicionesCuadrados[posX][posY-1].changeColor("green");
                                JOptionPane.showMessageDialog(null, "Auch, chocaste");
                                break;
                            }
                        }
                        else if(robot.direction() =='S'){
                            if(posicionesCuadrados[posX][posY+1].getColor() == "black" || posicionesCuadrados[posX][posY+1].getColor() == "green"){
                                robotWasDamaged();
                                posicionesCuadrados[posX][posY+1].changeColor("green");
                                JOptionPane.showMessageDialog(null, "Auch, chocaste");
                                break;
                            }
                        }
                        else if(robot.direction() =='W'){
                            if(posicionesCuadrados[posX-1][posY].getColor() == "black" || posicionesCuadrados[posX-1][posY].getColor() == "green"){
                                robotWasDamaged();
                                posicionesCuadrados[posX-1][posY].changeColor("green");
                                JOptionPane.showMessageDialog(null, "Auch, chocaste");
                                break;
                            }
                        }                        
                        else if(robot.direction() =='E'){
                            if(posicionesCuadrados[posX+1][posY].getColor() == "black" || posicionesCuadrados[posX+1][posY].getColor() == "green"){
                                robotWasDamaged();
                                posicionesCuadrados[posX+1][posY].changeColor("green");
                                JOptionPane.showMessageDialog(null, "Auch, chocaste");
                                break;
                            }
                        }
                        
                        movingRobot(1);
                        if(robot.coordinates()[0] == salida[0] && robot.coordinates()[1] == salida[1]){//
                            JOptionPane.showMessageDialog(null, "¡GANASTE! :D");
                            si=false;
                            break;
                        }                        
                    }
                }
            }

            else if (opcion == 1){
                String dir = JOptionPane.showInputDialog("¿Hacia cuál dirección? ");
                char direction;
                if(dir == null){
                    direction ='L';
                }else{
                    direction = dir.charAt(0);
                }
                if (direction!= 'W' && direction!= 'S' && direction!= 'N' && direction!= 'E'){
                    JOptionPane.showMessageDialog(null, "Opcion invalida, solamente: (N, S, E, W)");
                }else{    
                    turningRobot(direction);
                }              
            }else if (opcion == 2){
                int va= getRobotsHealth();
                for(int i = 0; i<(va) ; i++){
                    robotWasDamaged();
                }
                robot.makeVisibleDeathRobot();
                JOptionPane.showMessageDialog(null, "¡Termino el juego! :( ");
                si=false;
                break;
            
            }else if(opcion ==3){
                JOptionPane.showMessageDialog(null, "Corazones: "+getRobotsHealth());
                
            }

        }
    }
    

    
    
    /**
     * It moves the robot.
     * @param times it indicates how many times the robot will be moved
     */
    public void movingRobot(int times){
        robot.makeInvisible();
        robot.move(times);
        robot.makeVisible();
    }
    
    /**
     * It turns robot to a specific direction
     * @param direction is the new direction
     */
    public void turningRobot(char direction){
        robot.turn(direction);
        robot.makeVisible();

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
    
    
    /**
     * Generates the Entry and the Exit of the map.
     */
    public void generateEntryExit(int tamaño){
        entrada = new int[2];
        salida = new int[2];
        Random aleatorio1 = new Random();
        int e=(aleatorio1.nextInt(2)+1);     
                
        if(e==1){
            int f=(aleatorio1.nextInt(tamaño-1)+1);
            int c=0;
            entrada[0]=c;
            entrada[1]=f;
            
            int f1=(aleatorio1.nextInt(tamaño-1)+1);
            int c1 = tamaño;
            salida[0]=c1;
            salida[1]=f1;
        }else{
            int c=(aleatorio1.nextInt(tamaño-1)+1);
            int f=0;
            entrada[0]=c;
            entrada[1]=f;
            
            int c1=(aleatorio1.nextInt(tamaño-1)+1);
            int f1 = tamaño;
            salida[0]=c1;
            salida[1]=f1;
        }
    }
}