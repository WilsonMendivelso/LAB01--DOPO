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
        while(tamaño < 10 || tamaño > 20 ){
            String tamLab = JOptionPane.showInputDialog("Tamaño del laberinto (entre 10 y 20)");
            if (tamLab != null){
                tamaño = Integer.parseInt(tamLab);
            }
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
                cuadrado.moveHorizontal(-cuadrado.getCoordinates()[0] + i*((int)((pantalla-pantalla/tamaño+1)/tamaño)));
                cuadrado.moveVertical(-cuadrado.getCoordinates()[1] + j*((int)((pantalla-pantalla/tamaño+1)/tamaño)));
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
        robot.changePixelSize((int)((pantalla-pantalla/tamaño+1)/tamaño));
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
        robot.adaptTriangle(posicionesCuadrados[robot.coordinates()[0]][robot.coordinates()[1]].getColor());
        robot.makeVisible();
        
        int cantPared=-1;
        int c = 0;
        while(cantPared < 0 || cantPared > tamaño-2 ){
            String cantPar = JOptionPane.showInputDialog("Cuantas paredes quieres colocar? (entre 0 y "+(tamaño-2)+")");
            if (cantPar != null){
                cantPared = Integer.parseInt(cantPar);
                
                for(int i=0 ; i < cantPared; i++){
                    if(putWall()){
                    }else{
                       i--;
                    }
                }
            }
            
        }

        JOptionPane.showMessageDialog(null, "Se un buen fantasma y acaba con PacMan, intenta no chocar, si no, irás perdiendo todas tus vidas.");
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
                String time = JOptionPane.showInputDialog("¿Cuántas veces?");
                if(time !=null){
                    int times = Integer.parseInt(time);
                    for(int i = 0; i< times; i++){
                        if(validNextWall()){
                            movingRobot(1);
                            if(robot.coordinates()[0] == salida[0] && robot.coordinates()[1] == salida[1]){//
                                JOptionPane.showMessageDialog(null, "¡GANASTE! :D");
                                si=false;
                                break;
                            }      
                        }else{
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
     * put a Wall in the board
     * @return boolean for the cycle of cantPared
     */
    public boolean putWall(){
        JOptionPane.showMessageDialog(null, "Escriba las coordenadas, Primero X y luego Y"); 
        String equis = JOptionPane.showInputDialog("X: ");
        String ye = JOptionPane.showInputDialog("Y:");
        int X = Integer.parseInt(equis);
        int Y = Integer.parseInt(ye);
        if(posicionesCuadrados[X][Y].getColor() == "white"){
            posicionesCuadrados[X][Y].changeColor("black");
            return true;
        }else{
            JOptionPane.showMessageDialog(null, "Opcion invalida, esta posicion ya esta ocupada");
            return false;
        }
            
    }
    
    
    /**
     * It moves the robot.
     * @param times it indicates how many times the robot will be moved
     */
    public void movingRobot(int times){
        robot.makeInvisible();
        robot.move(times);
        robot.adaptTriangle(posicionesCuadrados[robot.coordinates()[0]][robot.coordinates()[1]].getColor());
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
    
    /**
     * Checks if next direction is a valid direction to be moved
     */
    public boolean validNextWall(){
        int[] coords = robot.coordinates();
        int posX=coords[0];
        int posY=coords[1];
        if((robot.direction() == 'N' && posY-1 < 0 )|| robot.direction() == 'W' && posX-1 < 0){
            JOptionPane.showMessageDialog(null, "Movimiento te lleva fuera del mapa");
            return false;
        }
        else if(robot.direction() =='N'){
            if(posicionesCuadrados[posX][posY-1].getColor() == "black" || posicionesCuadrados[posX][posY-1].getColor() == "gray"){
                robotWasDamaged();
                posicionesCuadrados[posX][posY-1].changeColor("gray");
                JOptionPane.showMessageDialog(null, "Auch, chocaste");
                return false;
                }
            }
        else if(robot.direction() =='S'){
            if(posicionesCuadrados[posX][posY+1].getColor() == "black" || posicionesCuadrados[posX][posY+1].getColor() == "gray"){
                robotWasDamaged();
                posicionesCuadrados[posX][posY+1].changeColor("gray");
                JOptionPane.showMessageDialog(null, "Auch, chocaste");
                return false;
                }
            }
        else if(robot.direction() =='W'){
            if(posicionesCuadrados[posX-1][posY].getColor() == "black" || posicionesCuadrados[posX-1][posY].getColor() == "gray"){
                robotWasDamaged();
                posicionesCuadrados[posX-1][posY].changeColor("gray");
                JOptionPane.showMessageDialog(null, "Auch, chocaste");
                return false;
            }
        }                        
        else if(robot.direction() =='E'){
            if(posicionesCuadrados[posX+1][posY].getColor() == "black" || posicionesCuadrados[posX+1][posY].getColor() == "gray"){
                robotWasDamaged();
                posicionesCuadrados[posX+1][posY].changeColor("gray");
                JOptionPane.showMessageDialog(null, "Auch, chocaste");
                return false;
            }
        }
        return true;
    }
}    
    
