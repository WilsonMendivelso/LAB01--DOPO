import javax.swing.JOptionPane;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents the laberinth with the Robot
 * 
 * @author Wilson Mendivelso, David Garzon
 * @version 1.0
 */
public class RobotMaze {
    private Canvas tablero;
    private Robot robot;
    private int[] entrada;
    private int[] salida;
    private Rectangle[][] posicionesCuadrados;
    private ArrayList<int[]> path;
    private boolean continuar;
    
    private ArrayList<int[]> pathTraveled;
    private ArrayList<Character> lastChar;
    private ArrayList<Boolean> crashesIntoAWall;
    
    
    /**
     * Constructor for objects of class RobotMaze
     */
    public RobotMaze() {
        tablero = Canvas.getCanvas();
        int tamaño = 5;
        while (tamaño < 11 || tamaño > 22) {
            String tamLab = JOptionPane.showInputDialog("Tamaño del laberinto (entre 10 y 21)");
            if (tamLab != null && tamLab.length() != 0) {
                tamaño = 1 + Integer.parseInt(tamLab);
            }
        }
        tablero.setVisible(true);

        int pantalla = tablero.getJPanel().getWidth();
        entrada = new int[2];
        salida = new int[2];
        posicionesCuadrados = new Rectangle[tamaño + 1][tamaño + 1];
        generateEntryExit(tamaño);

        for (int i = 0; i <= tamaño; i++) {
            for (int j = 0; j <= tamaño; j++) {
                Rectangle cuadrado = new Rectangle();
                cuadrado.moveHorizontal(-cuadrado.getCoordinates()[0] + i * ((int) ((pantalla - pantalla / tamaño + 1) / tamaño)));
                cuadrado.moveVertical(-cuadrado.getCoordinates()[1] + j * ((int) ((pantalla - pantalla / tamaño + 1) / tamaño)));
                cuadrado.changeSize((int) ((pantalla - pantalla / tamaño + 1) / tamaño), (int) ((pantalla - pantalla / tamaño + 1) / tamaño));
                cuadrado.changeColor("white");

                if (i == entrada[0] && j == entrada[1]) {
                    cuadrado.changeColor("blue");
                } else if (i == salida[0] && j == salida[1]) {
                    cuadrado.changeColor("white");
                } else if (i == 0 || j == 0 || i == tamaño || j == tamaño) {
                    cuadrado.changeColor("black");
                }

                posicionesCuadrados[i][j] = cuadrado;
                cuadrado.makeVisible();
            }
        }
        putPacMan(salida[0], salida[1], (int) ((pantalla - pantalla / tamaño + 1) / tamaño));
        robot = new Robot(0, 0);
        robot.changePixelSize((int) ((pantalla - pantalla / tamaño + 1) / tamaño));
        if (entrada[0] != 0) {
            robot.makeInvisible();
            robot.turn('E');
            robot.move(entrada[0]);
        } else {
            robot.makeInvisible();
            robot.turn('S');
            robot.move(entrada[1]);
        }
        robot.turn('N');
        robot.adaptTriangle(posicionesCuadrados[robot.coordinates()[0]][robot.coordinates()[1]].getColor());
        robot.makeVisible();
        createPath();

        int cantPared = -1;
        int c = 0;
        while (cantPared < 0 || cantPared > tamaño - 2) {
            String cantPar = JOptionPane.showInputDialog("Cuantas paredes quieres colocar? (entre 0 y " + (tamaño - 2) + ")");
            if (cantPar != null) {
                if (cantPar.length() == 0) {
                    cantPar = "0";
                }
                cantPared = Integer.parseInt(cantPar);
                if (cantPared <= (tamaño - 2) && cantPared >= 0) {
                    for (int i = 0; i < cantPared; i++) {
                        if (putWall()) {
                        } else {
                            i--;

                        }
                    }
                }
            }
        }

        createLabyrinth();

        JOptionPane.showMessageDialog(null, "Se un buen fantasma y acaba con PacMan, intenta no chocar, si no, irás perdiendo todas tus vidas.");
        continuar = true;
        pathTraveled = new ArrayList<>();
        pathTraveled.add(new int[]{robot.coordinates()[0], robot.coordinates()[1]});
        
        lastChar = new ArrayList<>();
        lastChar.add('N');
        
        crashesIntoAWall = new ArrayList<>();
        crashesIntoAWall.add(false);
        


        while (robot.isOk() && continuar) {
            String[] opciones;
            if(pathTraveled.size() > 1){
                opciones =new String[] {"Moverse", "Cambiar dirección", "Terminar el juego", "Mostrar vida", "Haz un buen movimiento","Devolverse"};
            }
            else{
                opciones =new String[] {"Moverse", "Cambiar dirección", "Terminar el juego", "Mostrar vida", "Haz un buen movimiento"};
            }
            int opcion = JOptionPane.showOptionDialog(
                    tablero.getJPanel(),
                    "¿Qué deseas hacer?",
                    "¿Qué deseas hacer?",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    "Moverse");
            if (opcion == 0) {
                doOption0();
            } else if (opcion == 1) {
                doOption1();
            } else if (opcion == 2) {
                doOption2();

            } else if (opcion == 3) {
                doOption3();

            }else if(opcion ==4){
                doOption4();
            }
            else if (opcion ==5){
                doOption5();
            }
        }
    }
    
    
    /**
     * It's the option 5 from the option list. It does all the things about return a movement.
     */
    
    public void doOption5(){
        int posX = robot.coordinates()[0];
        int posY = robot.coordinates()[1];
                    
        while((posX == robot.coordinates()[0] && posY == robot.coordinates()[1])  && crashesIntoAWall.size() > 1){
            returned();
                    
        }
    }
    /**
     * It's the option 4 from the options list. It does all the things about to have a good movement.
     */
    public void doOption4(){
        if(entrada[0] == robot.coordinates()[0] && entrada[1] == robot.coordinates()[1]){
            if(entrada[0] == 0){
                turningRobot('E');
                pathTraveled.add(new int[]{robot.coordinates()[0], robot.coordinates()[1]});
                lastChar.add('E');
                crashesIntoAWall.add(false); 
                
                movingRobot(1);
                robot.makeVisible();
                pathTraveled.add(new int[]{robot.coordinates()[0], robot.coordinates()[1]});
                lastChar.add(robot.direction());
                crashesIntoAWall.add(false);
            }
            else if(entrada[1] == 0){
                turningRobot('S');
                pathTraveled.add(new int[]{robot.coordinates()[0], robot.coordinates()[1]});
                lastChar.add('S');
                crashesIntoAWall.add(false);    
            
                movingRobot(1);
                robot.makeVisible();
                pathTraveled.add(new int[]{robot.coordinates()[0], robot.coordinates()[1]});
                lastChar.add(robot.direction());
                crashesIntoAWall.add(false);
            }
        }                                                                               
        else if(esParteDelCamino(robot.coordinates()[0], robot.coordinates()[1])){
            if(salida[0] == robot.coordinates()[0] + 1 && salida[1] == robot.coordinates()[1]){
                turningRobot('E');
                movingRobot(1);
                robot.makeVisible();
            
                JOptionPane.showMessageDialog(null, "¡GANASTE! :D");
                continuar = false;
                return;
            }
            else if(salida[1] == robot.coordinates()[1] + 1 && salida[0] == robot.coordinates()[0]){
                turningRobot('S');
                movingRobot(1);
                robot.makeVisible();
            
                JOptionPane.showMessageDialog(null, "¡GANASTE! :D");
                continuar = false;
                return;
            }
            else{
                int idx = 0;
                for(int i = 0; i < path.size(); i++){
                    if(robot.coordinates()[0] == path.get(i)[0] && robot.coordinates()[1] == path.get(i)[1]){
                        idx = i;
                        break;
                    }
                }
            
                int newPosX = path.get(idx +1)[0];
                int newPosY = path.get(idx +1)[1];
            
                if(newPosX != robot.coordinates()[0]){
                    if(newPosX -1== robot.coordinates()[0]){
                        turningRobot('E');
                        pathTraveled.add(new int[]{robot.coordinates()[0], robot.coordinates()[1]});
                        lastChar.add('E');
                        crashesIntoAWall.add(false); 
            
                        movingRobot(1);
                        robot.makeVisible();
                        pathTraveled.add(new int[]{robot.coordinates()[0], robot.coordinates()[1]});
                        lastChar.add(robot.direction());
                        crashesIntoAWall.add(false);
                    }
                    else{
                        turningRobot('W');
                        pathTraveled.add(new int[]{robot.coordinates()[0], robot.coordinates()[1]});
                        lastChar.add('W');
                        crashesIntoAWall.add(false); 
            
                        movingRobot(1);
                        robot.makeVisible();
                        pathTraveled.add(new int[]{robot.coordinates()[0], robot.coordinates()[1]});
                        lastChar.add(robot.direction());
                        crashesIntoAWall.add(false);
                    }
                }
                else{
                    if(newPosY +1== robot.coordinates()[1]){
                        turningRobot('N');
                         pathTraveled.add(new int[]{robot.coordinates()[0], robot.coordinates()[1]});
                        lastChar.add('N');
                        crashesIntoAWall.add(false); 
            
                        movingRobot(1);
                        robot.makeVisible();
                        pathTraveled.add(new int[]{robot.coordinates()[0], robot.coordinates()[1]});
                        lastChar.add(robot.direction());
                        crashesIntoAWall.add(false);
                    }
                    else{
                        turningRobot('S');
                        pathTraveled.add(new int[]{robot.coordinates()[0], robot.coordinates()[1]});
                        lastChar.add('S');
                        crashesIntoAWall.add(false); 
            
                        movingRobot(1);
                        robot.makeVisible();
                        pathTraveled.add(new int[]{robot.coordinates()[0], robot.coordinates()[1]});
                        lastChar.add(robot.direction());
                        crashesIntoAWall.add(false);
                    }
                } 
            }
        }
        else{
            moveNearPath();
        }
    }
    /**
     * It's the option 3 from the options list. It gives the Robot's health.
     */
    public void doOption3(){
        JOptionPane.showMessageDialog(null, "Corazones: " + getRobotsHealth());
    }
    
    /**
     * It's the option 2 from the options list. It ends the game if the user wants to.
     */
    public void doOption2(){  
        int va = getRobotsHealth();
        for (int i = 0; i < (va); i++) {
            robotWasDamaged();
        }
        robot.makeVisibleDeathRobot();
        JOptionPane.showMessageDialog(null, "¡Terminó el juego! :( ");
        continuar = false;
    }
    /**
     * It's the option 1 from the options list. This changes the Robot's direction
     */
    public void doOption1(){
        String dir = JOptionPane.showInputDialog("¿Hacia cuál dirección? ");
        char direction;
        if (dir == null) {
            direction = 'L';
        } else {
            direction = dir.charAt(0);
        }
        if(direction == robot.direction()){
           return;
        }
        else if (direction != 'W' && direction != 'S' && direction != 'N' && direction != 'E') {
            JOptionPane.showMessageDialog(null, "Opcion invalida, solamente: (N, S, E, W)");
        } else {
            turningRobot(direction);
            pathTraveled.add(new int[]{robot.coordinates()[0], robot.coordinates()[1]});
            lastChar.add(direction);
            crashesIntoAWall.add(false);                    
            robot.makeVisible();

        }
    }
    
    /**
     * It's the option 0 from the options list. This moves the Robot n moves.
     */
    public void doOption0() {
        String time = JOptionPane.showInputDialog("¿Cuántas veces?");
        if (time != null && time.length() != 0) {
            int times = Integer.parseInt(time);

            if (times > 0) {
                for (int i = 0; i < times; i++) {
                    if (validNextWall()) {
                        movingRobot(1);
                        robot.makeVisible();
                        pathTraveled.add(new int[]{robot.coordinates()[0], robot.coordinates()[1]});
                        lastChar.add(robot.direction());
                        crashesIntoAWall.add(false);
                        moveNearPath();
                        if (robot.coordinates()[0] == salida[0] && robot.coordinates()[1] == salida[1]) {
                            JOptionPane.showMessageDialog(null, "¡GANASTE! :D");
                            continuar = false;
                            return;
                        }
                    } else {
                        pathTraveled.add(new int[]{robot.coordinates()[0], robot.coordinates()[1]});
                        lastChar.add(robot.direction());
                        crashesIntoAWall.add(true);
                        moveNearPath();
                        return;
                    }
                }
            } else {
                for (int i = 0; i < Math.abs(times); i++) {
                    if (!moveBack()) {
                        return;
                    }
                    moveNearPath();
                    if (robot.coordinates()[0] == salida[0] && robot.coordinates()[1] == salida[1]) {
                        JOptionPane.showMessageDialog(null, "¡GANASTE! :D");
                        continuar = false;
                        return;
                    }
                }
            }
        }
    }
    /**
     * This function moves the Robot a little bit closer to the original path.
     */
    
    public void moveNearPath() {
        robot.makeInvisible();

        int otherIdx = pathTraveled.size() - 1;

        while (otherIdx > 0) {
            int puntoAx = robot.coordinates()[0];
            int puntoAy = robot.coordinates()[1];

            boolean enCamino = false;
            for (int[] p : path) {
                if (p[0] == puntoAx && p[1] == puntoAy) {
                    enCamino = true;
                    break;
                }
            }

            if (enCamino) {
                break;
            }

            int[] coordenadas= pathTraveled.get(otherIdx - 1);
            boolean crashed = crashesIntoAWall.get(otherIdx);

            if (crashed) {
                if (robot.getHealth() != 10){
                    robotIsHealthed();
                }
                char prevDir = lastChar.get(otherIdx - 1);
                turningRobot(prevDir);

                pathTraveled.add(new int[]{puntoAx, puntoAy});
                lastChar.add(prevDir);
                crashesIntoAWall.add(false);
            } else if (coordenadas[0] == puntoAx && coordenadas[1] == puntoAy) {
                char prevDir = lastChar.get(otherIdx - 1);
                turningRobot(prevDir);

                pathTraveled.add(new int[]{puntoAx, puntoAy});
                lastChar.add(prevDir);
                crashesIntoAWall.add(false);
            } else {
                char disDesde = 'N';
                if (puntoAx > coordenadas[0]){
                    disDesde = 'E';
                }
                else if (puntoAx < coordenadas[0]) {
                    disDesde = 'W';
                }
                else if (puntoAy > coordenadas[1]) {
                    disDesde = 'S';
                }
                else if (puntoAy < coordenadas[1]) {
                    disDesde = 'N';
                }

                char dirHacia = 'N';
                if (coordenadas[0] > puntoAx){ 
                    dirHacia = 'E';
                }
                else if (coordenadas[0] < puntoAx){ 
                    dirHacia = 'W';
                }
                else if (coordenadas[1] > puntoAy){ 
                    dirHacia = 'S';
                }
                else if (coordenadas[1] < puntoAy){
                    dirHacia = 'N';
                }

                turningRobot(disDesde);
                moveBack();

                turningRobot(dirHacia);

                pathTraveled.add(new int[]{coordenadas[0], coordenadas[1]});
                lastChar.add(dirHacia);
                crashesIntoAWall.add(false);
            }

            otherIdx--;
        }

        robot.makeVisible();
    }
    /**
     * This function returns the Robot to its last position.
     */
    public void returned(){
  
        if(pathTraveled.size() > 1){
            robot.makeInvisible();
            int len = pathTraveled.size();
            int puntoAx = robot.coordinates()[0];
            int puntoAy = robot.coordinates()[1];
            int[] lastCoords = pathTraveled.get(len-2);
        
        
            if(lastCoords[1] == puntoAy && lastCoords[0] == puntoAx && crashesIntoAWall.get(len-1)){
                pathTraveled.remove(len-1);
                turningRobot(lastChar.get(len-1));
                if(robot.direction() == 'N' && robot.coordinates()[1]-1 >=0){
                    posicionesCuadrados[robot.coordinates()[0]][robot.coordinates()[1]-1].changeColor("black");
                }
                else if(robot.direction() == 'S'){
                    posicionesCuadrados[robot.coordinates()[0]][robot.coordinates()[1]+1].changeColor("black");
                }
                else if(robot.direction() == 'W' && robot.coordinates()[0]-1 >=0){
                    posicionesCuadrados[robot.coordinates()[0]-1][robot.coordinates()[1]].changeColor("black");
                }else if(robot.direction() == 'E'){
                    posicionesCuadrados[robot.coordinates()[0]+1][robot.coordinates()[1]].changeColor("black");
                }
                lastChar.remove(len-1);
                robotIsHealthed();
                crashesIntoAWall.remove(len-1);
                
            }
            else if(lastCoords[1] == puntoAy && lastCoords[0] == puntoAx && !crashesIntoAWall.get(len-1)){
                pathTraveled.remove(len-1);
                turningRobot(lastChar.get(len-2));
                lastChar.remove(len-1);
                crashesIntoAWall.remove(len-1);
            }
            else if (lastCoords[1]==puntoAy){
                if(lastCoords[0]+1==puntoAx){
                    moveBack();
                }else{
                    moveBack();
                }
                pathTraveled.remove(len-1);
                turningRobot(lastChar.get(len-2));
                lastChar.remove(len-1);
                crashesIntoAWall.remove(len-1);
            }
        
            else if (lastCoords[0]==puntoAx){
                if(lastCoords[1]-1==puntoAy){
                    
                    moveBack();
                }else{
                    turningRobot(lastChar.get(len-1));
                    moveBack();
                }
                pathTraveled.remove(len-1);
                turningRobot(lastChar.get(len-2));
                lastChar.remove(len-1);
                crashesIntoAWall.remove(len-1);
            }
            robot.makeVisible();
        }
        
    }
    
    /**
     * put a Wall on the board
     * @return a boolean for the cycle of cantPared
     */
    public boolean putWall() {
        JOptionPane.showMessageDialog(null, "Escriba las coordenadas, Primero X y luego Y");
        String equis = JOptionPane.showInputDialog("X: ");
        String ye = JOptionPane.showInputDialog("Y:");

        if (equis == null || ye == null || equis.length() == 0 || ye.length() == 0) {
            return false;
        }
        int X = Integer.parseInt(equis);
        int Y = Integer.parseInt(ye);
        if (X < 0 || Y < 0 || X > posicionesCuadrados[0].length - 1 || Y > posicionesCuadrados[0].length - 1) {
            return false;
        }
        if (entrada[0] == 0) {
            if ((entrada[0] + 1 == X && entrada[1] == Y)) {
                JOptionPane.showMessageDialog(null, "Opcion invalida, no se puede tapar la entrada");
                return false;
            } else if (salida[0] - 1 == X && salida[1] == Y) {
                JOptionPane.showMessageDialog(null, "Opcion invalida, no se puede tapar la salida");
                return false;
            }
        }
        if (entrada[1] == 0) {
            if ((entrada[0] == X && entrada[1] + 1 == Y)) {
                JOptionPane.showMessageDialog(null, "Opcion invalida, no se puede tapar la entrada");
                return false;
            } else if (salida[0] == X && salida[1] - 1 == Y) {
                JOptionPane.showMessageDialog(null, "Opcion invalida, no se puede tapar la salida");
                return false;
            }
        }
        if (!(posicionesCuadrados[X][Y].getColor() == "black")) {
            posicionesCuadrados[X][Y].changeColor("black");

            int[] prueba = {X, Y};

            for (int[] i : path) {
                if (Arrays.equals(prueba, i)) {
                    if (!(changePath(X, Y, path.indexOf(i)))) {
                        JOptionPane.showMessageDialog(null, "Opcion invalida, no es posible poner acá un muro");
                        return false;
                    }

                    break;
                }
            }
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Opcion invalida, esta posicion ya esta ocupada");
            return false;
        }

    }


    /**
     * It moves the robot.
     * @param times it indicates how many times the robot will be moved
     */
    public void movingRobot(int times) {
        robot.makeInvisible();
        robot.move(times);
        robot.adaptTriangle(posicionesCuadrados[robot.coordinates()[0]][robot.coordinates()[1]].getColor());
    }

    /**
     * It turns robot to a specific direction
     * @param direction is the new direction
     */
    public void turningRobot(char direction) {
        robot.turn(direction);

    }

    /**
     * Gets robot's health.
     * @return robot's health.
     */
    public int getRobotsHealth() {
        return robot.getHealth();
    }

    /**
     * If robot was damaged it will decreased it health by one.
     */
    public void robotWasDamaged() {
        robot.lessOneHearth();
    }

    /**
     * If robot is cured it will increase it health by one.
     */
    public void robotIsHealthed(){
        robot.plusOneHearth();
    }
    
    /**
     * Generates the Entry and the Exit of the map.
     */
    public void generateEntryExit(int tamaño) {
        entrada = new int[2];
        salida = new int[2];
        Random aleatorio1 = new Random();
        int e = (aleatorio1.nextInt(2) + 1);

        if (e == 1) {
            int f = (aleatorio1.nextInt(tamaño - 1) + 1);
            int c = 0;
            entrada[0] = c;
            entrada[1] = f;

            int f1 = (aleatorio1.nextInt(tamaño - 1) + 1);
            int c1 = tamaño;
            salida[0] = c1;
            salida[1] = f1;

            while (salida[1] == entrada[1] || salida[1] == entrada[1] + 1 || salida[1] == entrada[1] - 1) {
                salida[1] = f1 = (aleatorio1.nextInt(tamaño - 1) + 1);
            }
        } else {
            int c = (aleatorio1.nextInt(tamaño - 1) + 1);
            int f = 0;
            entrada[0] = c;
            entrada[1] = f;

            int c1 = (aleatorio1.nextInt(tamaño - 1) + 1);
            int f1 = tamaño;
            salida[0] = c1;
            salida[1] = f1;

            while (entrada[0] == salida[0] || entrada[0] == salida[0] + 1 || entrada[0] == salida[0] - 1) {
                salida[0] = (aleatorio1.nextInt(tamaño - 1) + 1);
            }
        }
    }

    /**
     * Checks if next direction is a valid direction to be moved
     * @return if the next wall is valid
     */
    public boolean validNextWall() {
        int[] coords = robot.coordinates();
        int posX = coords[0];
        int posY = coords[1];
        if ((robot.direction() == 'N' && posY - 1 < 0) || robot.direction() == 'W' && posX - 1 < 0) {
            if (!robot.getIsVisible()) {
                robot.makeVisible();
            }
            JOptionPane.showMessageDialog(null, "Movimiento te lleva fuera del mapa");
            return false;
        } else if (robot.direction() == 'N') {
            if (esPared(posX, posY-1)){
                if (!robot.getIsVisible()) {
                    robot.makeVisible();
                }
                robotWasDamaged();
                posicionesCuadrados[posX][posY - 1].changeColor("gray");
                JOptionPane.showMessageDialog(null, "Auch, chocaste");
                return false;
            }
        } else if (robot.direction() == 'S') {
            if (esPared(posX, posY+1)){
                if (!robot.getIsVisible()) {
                    robot.makeVisible();
                }
                robotWasDamaged();
                posicionesCuadrados[posX][posY + 1].changeColor("gray");
                JOptionPane.showMessageDialog(null, "Auch, chocaste");
                return false;
            }
        } else if (robot.direction() == 'W') {
            if (esPared(posX-1, posY)) {
                if (!robot.getIsVisible()) {
                    robot.makeVisible();
                }
                robotWasDamaged();
                posicionesCuadrados[posX - 1][posY].changeColor("gray");
                JOptionPane.showMessageDialog(null, "Auch, chocaste");
                return false;
            }
        } else if (robot.direction() == 'E') {
            if (esPared(posX+1, posY)){
                if (!robot.getIsVisible()) {
                    robot.makeVisible();
                }
                robotWasDamaged();
                posicionesCuadrados[posX + 1][posY].changeColor("gray");
                JOptionPane.showMessageDialog(null, "Auch, chocaste");
                return false;
            }
        }
        return true;
    }

    /**
     * It moves the Robot backs, it happens if the move is negative.
     * @return if the move was done correctly and it can do another
     */

    public boolean moveBack() {
        robot.makeInvisible();
        char originalDirection = robot.direction();
        if (originalDirection == 'E') {
            robot.turn('W');
        } else if (originalDirection == 'W') {
            robot.turn('E');
        } else if (originalDirection == 'S') {
            robot.turn('N');
        } else {
            robot.turn('S');
        }

        if (validNextWall()) {
            robot.makeInvisible();
            movingRobot(1);
            turningRobot(originalDirection);
            robot.makeVisible();

            return true;
        } else {
            turningRobot(originalDirection);
            robot.makeVisible();

            return false;
        }
    }

    /**
     * Puts in the exit place a PacMan that must be defeated.
     * @param i is the XPosition on the board, where pacMan will be placed.
     * @param j is the YPosition on the board, where pacMan will be placed.
     * @param tamaño is the PacMan's size.
     */
    public void putPacMan(int i, int j, int tamaño) {
        Circle pacMan = new Circle();
        pacMan.changeSize(tamaño - 2);
        pacMan.changeColor("yellow");
        pacMan.moveHorizontal(((int) ((500 - 500 / tamaño + 1) / tamaño) / 25) - pacMan.getCoordinates()[0] + tamaño * i);
        pacMan.moveVertical((-pacMan.getCoordinates()[1] + tamaño * j));
        pacMan.makeVisible();

        if (entrada[0] == 0) {
            Triangle bocaSup = new Triangle();
            bocaSup.changeSize((tamaño - 2) / 2, ((tamaño - 2) * 3) / 2);
            bocaSup.changeColor("white");
            bocaSup.moveHorizontal(-bocaSup.getCoordinates()[0] + pacMan.getCoordinates()[0] + (6 * tamaño) / 5);
            bocaSup.moveVertical(-bocaSup.getCoordinates()[1] + pacMan.getCoordinates()[1]);
            bocaSup.makeVisible();

            Triangle bocaInf = new Triangle();
            bocaInf.changeSize(-(tamaño - 2) / 2, ((tamaño - 2) * 3) / 2);
            bocaInf.changeColor("white");
            bocaInf.moveHorizontal(-bocaInf.getCoordinates()[0] + pacMan.getCoordinates()[0] + (6 * tamaño) / 5);
            bocaInf.moveVertical(-bocaInf.getCoordinates()[1] + pacMan.getCoordinates()[1] + (tamaño - 2));
            bocaInf.makeVisible();

        } else if (entrada[1] == 0) {
            Triangle boca = new Triangle();
            boca.changeSize(3 * (tamaño - 2) / 4, 3 * (tamaño - 2) / 4);
            boca.changeColor("white");
            boca.moveHorizontal(-boca.getCoordinates()[0] + pacMan.getCoordinates()[0] + (tamaño) / 2);
            boca.moveVertical(-boca.getCoordinates()[1] + pacMan.getCoordinates()[1] + (9 * tamaño) / 20);
            boca.makeVisible();

        }
    }
    /**
     * It creates the path that goes from Entry to the Exit, this path won't be seen by the user, besides, with this method we know that there's a win possibility.
     */
    public void createPath() {
	    path = new ArrayList<>();
	    int limit = posicionesCuadrados.length - 2;

	    int rand = selectRandInt(1, 3); 
	    int numTramos = 0;
	    if (rand == 1) {
	        numTramos = 5;
	    } else if (rand == 2) {
	        numTramos =7;
	    } else {
	        numTramos = 9;
	    }

	    int tramosEjePrincipal = (numTramos + 1) / 2;
	    int tramosEjeSecundario = numTramos / 2;

	    //Mapa: Izquierda a derecha
	    if (entrada[0] == 0) {
	        int currentX = entrada[0];
	        int currentY = entrada[1];

	        int remainingX = limit;
	        int[] pasosX = new int[tramosEjePrincipal];

	        int mitad = limit / 3;
	        if (mitad < 2) {
	            mitad = 2;
	        }
	        pasosX[0] = selectRandInt(2, mitad);
	        remainingX = remainingX - pasosX[0];

	        for (int i = 1; i < tramosEjePrincipal - 1; i++) {
	            int maxRango = remainingX - (2 * (tramosEjePrincipal - 1 - i));
	            if (maxRango < 2) {
	                maxRango = 2;
	            }
	            int paso = selectRandInt(2, maxRango);
	            pasosX[i] = paso;
	            remainingX = remainingX - paso;
	        }
	        pasosX[tramosEjePrincipal - 1] = remainingX;

	        for (int step = 0; step < tramosEjeSecundario; step++) {
	            for (int i = 0; i < pasosX[step]; i++) {
	                currentX = currentX + 1;
	                int[] pareja = {currentX, currentY};
	                path.add(pareja);
	            }

	            int targetY = 0;
	            if (step == tramosEjeSecundario - 1) {
	                targetY = salida[1]; 
	            } else {
	                targetY = selectRandInt(1, limit);
	                while (targetY == currentY || targetY == salida[1]) {
	                    targetY = selectRandInt(1, limit);
	                }
	            }

	            int dirY = 1;
	            if (targetY < currentY) {
	                dirY = -1; 
	            }

	            int moveY = Math.abs(targetY - currentY);
	            for (int i = 0; i < moveY; i++) {
	                currentY = currentY + dirY;
	                int[] pareja = {currentX, currentY};
	                path.add(pareja);
	            }
	        }

	        for (int i = 0; i < pasosX[tramosEjePrincipal - 1]; i++) {
	            currentX = currentX + 1;
	            int[] pareja = {currentX, currentY};
	            path.add(pareja);
	        }

	    }
	    //Mapa arriba-abajo
	    else if (entrada[1] == 0) {
	        int currentX = entrada[0];
	        int currentY = entrada[1];

	        int remainingY = limit;
	        int[] pasosY = new int[tramosEjePrincipal];

	        int mitad = limit / 2;
	        if (mitad < 2) {
	            mitad = 2;
	        }
	        pasosY[0] = selectRandInt(2, mitad);
	        remainingY = remainingY - pasosY[0];

	        for (int i = 1; i < tramosEjePrincipal - 1; i++) {
	            int maxRango = remainingY - (2 * (tramosEjePrincipal - 1 - i));
	            if (maxRango < 2) {
	                maxRango = 2;
	            }
	            int paso = selectRandInt(2, maxRango);
	            pasosY[i] = paso;
	            remainingY = remainingY - paso;
	        }
	        pasosY[tramosEjePrincipal - 1] = remainingY;

	        for (int step = 0; step < tramosEjeSecundario; step++) {
	            for (int i = 0; i < pasosY[step]; i++) {
	                currentY = currentY + 1;
	                int[] pareja = {currentX, currentY};
	                path.add(pareja);
	            }

	            int targetX = 0;
	            if (step == tramosEjeSecundario - 1) {
	                targetX = salida[0]; 
	            } else {
	                targetX = selectRandInt(1, limit);
	                while (targetX == currentX || targetX == salida[0]) {
	                    targetX = selectRandInt(1, limit);
	                }
	            }

	            int dirX = 1;
	            if (targetX < currentX) {
	                dirX = -1; 
	            }

	            int moveX = Math.abs(targetX - currentX);
	            for (int i = 0; i < moveX; i++) {
	                currentX = currentX + dirX;
	                int[] pareja = {currentX, currentY};
	                path.add(pareja);
	            }
	        }

	        for (int i = 0; i < pasosY[tramosEjePrincipal - 1]; i++) {
	            currentY = currentY + 1;
	            int[] pareja = {currentX, currentY};
	            path.add(pareja);
	        }
	    }
    }
    /**
     * If user puts a wall in the winning path, we have to recalculate the path to win.
     * @param x is the XPosition of the new wall.
     * @param y is the YPosition of the new wall.
     * @param idx is the index of the path in the path attribute
     * @return if the Path can change.
     */
    public boolean changePath(int X, int Y, int idx) {
        if (idx <= 0 || idx >= path.size() - 1) {
            return false;
        }

        int anteriorX = path.get(idx - 1)[0];
        int anteriorY = path.get(idx - 1)[1];

        int siguienteX = path.get(idx + 1)[0];
        int siguienteY = path.get(idx + 1)[1];

        // No permitimos colocar un muro en una esquina del camino
        if ((anteriorX == X && siguienteY == Y) ||
                (anteriorY == Y && siguienteX == X)) {
            return false;
        }
        // No permitimos poner un muro justo al lado de otro muro
        if (anteriorX == X && siguienteX == X) {
            if (Y - 1 >= 0 &&
                    posicionesCuadrados[X][Y - 1].getColor().equals("black")) {
                return false;
            }

            if (Y + 1 < posicionesCuadrados[0].length &&
                    posicionesCuadrados[X][Y + 1].getColor().equals("black")) {
                return false;
            }
        }

        if (anteriorY == Y && siguienteY == Y) {
            if (X - 1 >= 0 &&
                    posicionesCuadrados[X - 1][Y].getColor().equals("black")) {
                return false;
            }

            if (X + 1 < posicionesCuadrados.length &&
                    posicionesCuadrados[X + 1][Y].getColor().equals("black")) {
                return false;
            }
        }
         //Camino horizontal
        if (anteriorY == Y && siguienteY == Y) {

            // Arriba
            if (Y - 1 >= 0 &&
                    !posicionesCuadrados[anteriorX][Y - 1].getColor().equals("black") &&
                    !posicionesCuadrados[X][Y - 1].getColor().equals("black") &&
                    !posicionesCuadrados[siguienteX][Y - 1].getColor().equals("black")) {

                path.remove(idx);

                int[] temp1 = {anteriorX, Y - 1};
                int[] temp2 = {X, Y - 1};
                int[] temp3 = {siguienteX, Y - 1};

                path.add(idx, temp1);
                path.add(idx + 1, temp2);
                path.add(idx + 2, temp3);

                return true;
            }

            // Ver abajo
            else if (Y + 1 < posicionesCuadrados[0].length &&
                    !posicionesCuadrados[anteriorX][Y + 1].getColor().equals("black") &&
                    !posicionesCuadrados[X][Y + 1].getColor().equals("black") &&
                    !posicionesCuadrados[siguienteX][Y + 1].getColor().equals("black")) {

                path.remove(idx);

                int[] temp1 = {anteriorX, Y + 1};
                int[] temp2 = {X, Y + 1};
                int[] temp3 = {siguienteX, Y + 1};

                path.add(idx, temp1);
                path.add(idx + 1, temp2);
                path.add(idx + 2, temp3);

                return true;
            }

            // Vuelta arriba
            else if (Y - 1 >= 0 &&
                    Y - 2 >= 0 &&
                    !posicionesCuadrados[anteriorX][Y - 1].getColor().equals("black") &&
                    !posicionesCuadrados[anteriorX][Y - 2].getColor().equals("black") &&
                    !posicionesCuadrados[X][Y - 2].getColor().equals("black") &&
                    !posicionesCuadrados[siguienteX][Y - 2].getColor().equals("black")) {

                path.remove(idx);

                int[] temp1 = {anteriorX, Y - 1};
                int[] temp2 = {anteriorX, Y - 2};
                int[] temp3 = {X, Y - 2};
                int[] temp4 = {siguienteX, Y - 2};
                int[] temp5 = {siguienteX, Y - 1};

                path.add(idx, temp1);
                path.add(idx + 1, temp2);
                path.add(idx + 2, temp3);
                path.add(idx + 3, temp4);
                path.add(idx + 4, temp5);

                return true;
            }

            // Intentamos dar una vuelta por abajo
            else if (Y + 1 < posicionesCuadrados[0].length &&
                    Y + 2 < posicionesCuadrados[0].length &&
                    !posicionesCuadrados[anteriorX][Y + 1].getColor().equals("black") &&
                    !posicionesCuadrados[anteriorX][Y + 2].getColor().equals("black") &&
                    !posicionesCuadrados[X][Y + 2].getColor().equals("black") &&
                    !posicionesCuadrados[siguienteX][Y + 2].getColor().equals("black")) {

                path.remove(idx);

                int[] temp1 = {anteriorX, Y + 1};
                int[] temp2 = {anteriorX, Y + 2};
                int[] temp3 = {X, Y + 2};
                int[] temp4 = {siguienteX, Y + 2};
                int[] temp5 = {siguienteX, Y + 1};

                path.add(idx, temp1);
                path.add(idx + 1, temp2);
                path.add(idx + 2, temp3);
                path.add(idx + 3, temp4);
                path.add(idx + 4, temp5);

                return true;
            }
        }


        //El camino venia verticalmente.

        else if (anteriorX == X && siguienteX == X) {

            // Intentamos pasar por la izquierda
            if (X - 1 >= 0 &&
                    !posicionesCuadrados[X - 1][anteriorY].getColor().equals("black") &&
                    !posicionesCuadrados[X - 1][Y].getColor().equals("black") &&
                    !posicionesCuadrados[X - 1][siguienteY].getColor().equals("black")) {

                path.remove(idx);

                int[] temp1 = {X - 1, anteriorY};
                int[] temp2 = {X - 1, Y};
                int[] temp3 = {X - 1, siguienteY};

                path.add(idx, temp1);
                path.add(idx + 1, temp2);
                path.add(idx + 2, temp3);

                return true;
            }

            // Intentamos pasar por la derecha
            else if (X + 1 < posicionesCuadrados.length &&
                    !posicionesCuadrados[X + 1][anteriorY].getColor().equals("black") &&
                    !posicionesCuadrados[X + 1][Y].getColor().equals("black") &&
                    !posicionesCuadrados[X + 1][siguienteY].getColor().equals("black")) {

                path.remove(idx);

                int[] temp1 = {X + 1, anteriorY};
                int[] temp2 = {X + 1, Y};
                int[] temp3 = {X + 1, siguienteY};

                path.add(idx, temp1);
                path.add(idx + 1, temp2);
                path.add(idx + 2, temp3);

                return true;
            }
        }


        //El camino llega a una esquina.
        else if (anteriorX == X) {

            if (anteriorX + 1 < posicionesCuadrados.length &&
                    !posicionesCuadrados[anteriorX + 1][anteriorY].getColor().equals("black") &&
                    !posicionesCuadrados[anteriorX + 1][Y].getColor().equals("black")) {

                path.remove(idx);

                int[] temp1 = {anteriorX + 1, anteriorY};
                int[] temp2 = {anteriorX + 1, Y};

                path.add(idx, temp1);
                path.add(idx + 1, temp2);

                return true;
            }
        } else if (anteriorY == Y) {

            if (anteriorY + 1 < posicionesCuadrados[0].length &&
                    !posicionesCuadrados[anteriorX][anteriorY + 1].getColor().equals("black") &&
                    !posicionesCuadrados[X][anteriorY + 1].getColor().equals("black")) {

                path.remove(idx);

                int[] temp1 = {anteriorX, anteriorY + 1};
                int[] temp2 = {X, anteriorY + 1};

                path.add(idx, temp1);
                path.add(idx + 1, temp2);

                return true;
            }
        }

        // El usuario puso un caso demasiado especifico el cual no fue planeado, entonces simplemente devolvemos false así no lo dejamos hacerlo
        return false;
    }
    
    
    /**
     * It creates the labyrinth
     */
    public void createLabyrinth() {
        int tamaño = posicionesCuadrados.length - 1;

        for (int i = 2; i < tamaño; i = i + 2) {
            for (int j = 2; j < tamaño; j = j + 2) {
                if (esParteDelCamino(i, j) == false && esPared(i, j) == false) {
                    generarParedes(i, j, tamaño);
                }
            }
        }

        boolean seModifico = true;

        while (seModifico == true) {
            seModifico = false;

            for (int i = 1; i < tamaño; i++) {
                for (int j = 1; j < tamaño; j++) {

                    if (esPared(i, j) == false && esPared(i + 1, j) == false && esPared(i, j + 1) == false && esPared(i + 1, j + 1) == false) {

                        int[][] opciones = {{i, j}, {i + 1, j}, {i, j + 1}, {i + 1, j + 1}};

                        for (int k = 0; k < 4; k++) {
                            int r = (int) (Math.random() * 4);
                            int[] temp = opciones[k];
                            opciones[k] = opciones[r];
                            opciones[r] = temp;
                        }

                        for (int k = 0; k < 4; k++) {
                            int x = opciones[k][0];
                            int y = opciones[k][1];

                            if (esParteDelCamino(x, y) == true) {
                                continue;
                            }

                            posicionesCuadrados[x][y].changeColor("black");
                            boolean encierro = false;

                            if (revisarSiEncierraVecino(x, y - 1, tamaño) == true) {
                                encierro = true;
                            } else if (revisarSiEncierraVecino(x, y + 1, tamaño) == true) {
                                encierro = true;
                            } else if (revisarSiEncierraVecino(x - 1, y, tamaño) == true) {
                                encierro = true;
                            } else if (revisarSiEncierraVecino(x + 1, y, tamaño) == true) {
                                encierro = true;
                            }

                            if (encierro == true) {
                                posicionesCuadrados[x][y].changeColor("white");
                            } else {
                                seModifico = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * To create the laberinth is necessary to see if a route is closed.
     * @return if are there open exits.
     */
    public boolean revisarSiEncierraVecino(int vecinoX, int vecinoY, int tamaño) {
        if (vecinoX > 0 && vecinoX < tamaño && vecinoY > 0 && vecinoY < tamaño) {
            if (esPared(vecinoX, vecinoY) == false) {
                
                int salidasAbiertas = 0;
                
                if (vecinoY - 1 > 0 && esPared(vecinoX, vecinoY - 1) == false) {
                    salidasAbiertas++;
                }
                if (vecinoY + 1 < tamaño && esPared(vecinoX, vecinoY + 1) == false) {
                    salidasAbiertas++;
                }
                if (vecinoX - 1 > 0 && esPared(vecinoX - 1, vecinoY) == false) {
                    salidasAbiertas++;
                }
                if (vecinoX + 1 < tamaño && esPared(vecinoX + 1, vecinoY) == false) {
                    salidasAbiertas++;
                }
                
                if (salidasAbiertas == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Uses DFS to create the map, it paints the wall 
     * @param x is the x position where the wall starts
     * @param y is the y position where the wall starts
     * @param tamaño is the size of the map.
     */
    public void generarParedes(int x, int y, int tamaño) {
        posicionesCuadrados[x][y].changeColor("black");
        
        int[] direcciones = {0, 1, 2, 3};
        
        for (int i = 0; i < 4; i++) {
            int r = (int)(Math.random() * 4);
            int temp = direcciones[i]; 
            direcciones[i] = direcciones[r]; 
            direcciones[r] = temp;
        }
        
        for (int i = 0; i < 4; i++) {
            int dir = direcciones[i];
            int dx = 0;
            int dy = 0;
            
            if (dir == 0) {
                dy = -2;
            } else if (dir == 1) {
                dx = 2;
            } else if (dir == 2) {
                dy = 2;
            } else if (dir == 3) {
                dx = -2;
            }
            
            int nextX = x + dx;
            int nextY = y + dy;
            
            if (nextX > 0 && nextX < tamaño && nextY > 0 && nextY < tamaño) {
                int interX = x + (dx / 2);
                int interY = y + (dy / 2);
                
                if (esPared(nextX, nextY) == false && esParteDelCamino(nextX, nextY) == false && esParteDelCamino(interX, interY) == false) {
                    posicionesCuadrados[interX][interY].changeColor("black");
                    generarParedes(nextX, nextY, tamaño);
                }
            }
        }
    }
    /**
     * It returns, if there is a wall.
     * @param x is the X position of the wall
     * @param y is the Y position of the wall
     * @return if there is a wall there
     */
    public boolean esPared(int x, int y) {
        if (posicionesCuadrados[x][y].getColor().equals("black") || posicionesCuadrados[x][y].getColor().equals("gray")) {
            return true;
        }
        return false;
    }

    /**
     * It returns if a specific place in the map is part of the path
     * @param x is the X position of the place on the map
     * @param y is the Y position of the place on the map
     * @return if it is a part of the path
     */
    
    public boolean esParteDelCamino(int x, int y) {
        if (path != null) {
            for (int[] coordenada : path) {
                if (coordenada[0] == x && coordenada[1] == y) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * It returns a random number
     * @param infLimit represents the minimum number that we can have
     * @param supLimit represents the maximum number -1 that we can have, because we cannot have the supLimit
     * @return an int random number beetween infLimit and supLimit-1
     */
    public int selectRandInt(int infLimit, int supLimit){
        int numeroRand = infLimit +(int)(Math.random()*supLimit);
        return numeroRand;
    }
    
    
}