import javax.swing.JOptionPane;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

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
    ArrayList<int[]> path;
    
    /**
     * Constructor for objects of class RobotMaze
     */
    public RobotMaze(){
        tablero = Canvas.getCanvas();
        int tamaño = 5;
        while(tamaño < 10 || tamaño > 21 ){
            String tamLab = JOptionPane.showInputDialog("Tamaño del laberinto (entre 10 y 20)");
            if (tamLab != null){
                tamaño = 1+ Integer.parseInt(tamLab);
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

                if(i==entrada[0] && j==entrada[1]){
                    cuadrado.changeColor("blue");
                }
                else if(i==salida[0] && j==salida[1]){
                    cuadrado.changeColor("white");
                }
                else if(i==0 || j== 0 || i== tamaño || j==tamaño){
                    cuadrado.changeColor("black");
                }
                
                
                
                
                posicionesCuadrados[i][j]=cuadrado;
                cuadrado.makeVisible();
            }
        }
        putPacMan(salida[0],salida[1],(int)((pantalla-pantalla/tamaño+1)/tamaño));
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
        createPath();
        
        int cantPared=-1;
        int c = 0;
        while(cantPared < 0 || cantPared > tamaño-2 ){
            String cantPar = JOptionPane.showInputDialog("Cuantas paredes quieres colocar? (entre 0 y "+(tamaño-2)+")");
            if (cantPar != null){
                cantPared = Integer.parseInt(cantPar);
                if(cantPared <= (tamaño-2) && cantPared>= 0){
                    for(int i=0 ; i < cantPared; i++){
                        if(putWall()){
                        }else{
                           i--;
                            }
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
                    
                    if (times > 0){
                        for(int i = 0; i< times; i++){
                            if(validNextWall()){
                                movingRobot(1);
                                robot.makeVisible();
                                if(robot.coordinates()[0] == salida[0] && robot.coordinates()[1] == salida[1]){
                                    JOptionPane.showMessageDialog(null, "¡GANASTE! :D");
                                    si=false;
                                    break;
                                }      
                            }else{
                                break;
                            }
                        }
                    }
                    else{
                        for(int i = 0; i < Math.abs(times); i++){
                            if(!moveBack()){
                                break;
                            }
                            if(robot.coordinates()[0] == salida[0] && robot.coordinates()[1] == salida[1]){
                                JOptionPane.showMessageDialog(null, "¡GANASTE! :D");
                                si=false;
                                break;
                            }
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
                    robot.makeVisible();

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
        
        if(equis == null || ye == null || equis == "" || ye == ""){
            return false;
        }
        int X = Integer.parseInt(equis);
        int Y = Integer.parseInt(ye);
        if(X < 0 || Y < 0 || X > posicionesCuadrados[0].length -1 || Y > posicionesCuadrados[0].length - 1){
            return false;
        }
        if(entrada[0] == 0){
            if((entrada[0]+ 1 == X && entrada[1] == Y)){
                JOptionPane.showMessageDialog(null, "Opcion invalida, no se puede tapar la entrada");
                return false;
            } else if(salida[0]-1 == X && salida[1] == Y){
                JOptionPane.showMessageDialog(null, "Opcion invalida, no se puede tapar la salida");
                return false;
            }
        }
        if(entrada[1] == 0){
            if((entrada[0] == X && entrada[1]+1 == Y)){
                JOptionPane.showMessageDialog(null, "Opcion invalida, no se puede tapar la entrada");
                return false;
            } else if(salida[0] == X && salida[1]-1 == Y){
                JOptionPane.showMessageDialog(null, "Opcion invalida, no se puede tapar la salida");
                return false;
            }
        }
        if(!(posicionesCuadrados[X][Y].getColor() == "black")){
            posicionesCuadrados[X][Y].changeColor("black");
            
            int[] prueba = {X,Y};
            
            for(int[] i: path){
                if(Arrays.equals(prueba, i)){
                    if(!(changePath(X,Y, path.indexOf(i)))){
                        posicionesCuadrados[X][Y].changeColor("green"); //cambiar
                        JOptionPane.showMessageDialog(null, "Opcion invalida, no es posible poner acá un muro");
                        return false;
                    }
                    break;
                }
            }
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
            
            while(salida[1] == entrada[1] || salida[1] == entrada[1] +1 || salida[1] == entrada[1] - 1){
                salida[1] = f1=(aleatorio1.nextInt(tamaño-1)+1);
            }
        }else{
            int c=(aleatorio1.nextInt(tamaño-1)+1);
            int f=0;
            entrada[0]=c;
            entrada[1]=f;
            
            int c1=(aleatorio1.nextInt(tamaño-1)+1);
            int f1 = tamaño;
            salida[0]=c1;
            salida[1]=f1;
            
            while(entrada[0] == salida[0]|| entrada[0] == salida[0]+1 || entrada[0] == salida[0]-1){
                salida[0] = (aleatorio1.nextInt(tamaño-1)+1);
            }
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
            if(!robot.getIsVisible()){
                robot.makeVisible();
            }
            JOptionPane.showMessageDialog(null, "Movimiento te lleva fuera del mapa");
            return false;
        }
        else if(robot.direction() =='N'){
            if(posicionesCuadrados[posX][posY-1].getColor() == "black" || posicionesCuadrados[posX][posY-1].getColor() == "gray"){
                if(!robot.getIsVisible()){
                    robot.makeVisible();
                }
                robotWasDamaged();
                posicionesCuadrados[posX][posY-1].changeColor("gray");
                JOptionPane.showMessageDialog(null, "Auch, chocaste");
                return false;
                }
            }
        else if(robot.direction() =='S'){
            if(posicionesCuadrados[posX][posY+1].getColor() == "black" || posicionesCuadrados[posX][posY+1].getColor() == "gray"){
                if(!robot.getIsVisible()){
                    robot.makeVisible();
                }
                robotWasDamaged();
                posicionesCuadrados[posX][posY+1].changeColor("gray");
                JOptionPane.showMessageDialog(null, "Auch, chocaste");
                return false;
                }
            }
        else if(robot.direction() =='W'){
            if(posicionesCuadrados[posX-1][posY].getColor() == "black" || posicionesCuadrados[posX-1][posY].getColor() == "gray"){
                if(!robot.getIsVisible()){
                    robot.makeVisible();
                }
                robotWasDamaged();
                posicionesCuadrados[posX-1][posY].changeColor("gray");
                JOptionPane.showMessageDialog(null, "Auch, chocaste");
                return false;
            }
        }                        
        else if(robot.direction() =='E'){
            if(posicionesCuadrados[posX+1][posY].getColor() == "black" || posicionesCuadrados[posX+1][posY].getColor() == "gray"){
                if(!robot.getIsVisible()){
                    robot.makeVisible();
                }
                robotWasDamaged();
                posicionesCuadrados[posX+1][posY].changeColor("gray");
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
    
    public boolean moveBack(){
        robot.makeInvisible();
        char originalDirection= robot.direction();
        if(originalDirection == 'E'){
            robot.turn('W');        
        }
        else if(originalDirection == 'W'){
            robot.turn('E');
        }
        else if(originalDirection == 'S'){
            robot.turn('N');
        }
        else{
            robot.turn('S');
        }
        
        if(validNextWall()){
            robot.makeInvisible();
            movingRobot(1);
            turningRobot(originalDirection);
            robot.makeVisible();

            return true;
        } else{
            turningRobot(originalDirection);
            robot.makeVisible();

            return false;
        }
    }
    /**
    * Puts in the exit place a PacMan that must be defeated.
    */
    public void putPacMan(int i, int j, int tamaño){
        Circle pacMan = new Circle();
        pacMan.changeSize(tamaño-2);
        pacMan.changeColor("yellow");
        pacMan.moveHorizontal(((int)((500-500/tamaño+1)/tamaño)/25)-pacMan.getCoordinates()[0]+tamaño*i);
        pacMan.moveVertical((-pacMan.getCoordinates()[1]+tamaño*j));        
        pacMan.makeVisible();
        
        if(entrada[0] ==0 ){
            Triangle bocaSup = new Triangle();
            bocaSup.changeSize((tamaño-2)/2, ((tamaño-2)*3)/2);
            bocaSup.changeColor("white");
            bocaSup.moveHorizontal(-bocaSup.getCoordinates()[0] + pacMan.getCoordinates()[0] + (6*tamaño)/5);
            bocaSup.moveVertical(-bocaSup.getCoordinates()[1] + pacMan.getCoordinates()[1]);
            bocaSup.makeVisible();
        
            Triangle bocaInf = new Triangle();
            bocaInf.changeSize(-(tamaño-2)/2, ((tamaño-2)*3)/2);
            bocaInf.changeColor("white");
            bocaInf.moveHorizontal(-bocaInf.getCoordinates()[0] + pacMan.getCoordinates()[0] + (6*tamaño)/5);
            bocaInf.moveVertical(-bocaInf.getCoordinates()[1] + pacMan.getCoordinates()[1] + (tamaño-2));
            bocaInf.makeVisible();
            
        }   
        else if(entrada[1] ==0 ){
            Triangle boca = new Triangle();
            boca.changeSize(3*(tamaño-2)/4, 3*(tamaño-2)/4);
            boca.changeColor("white");
            boca.moveHorizontal(-boca.getCoordinates()[0] + pacMan.getCoordinates()[0] + (tamaño)/2);
            boca.moveVertical(-boca.getCoordinates()[1] + pacMan.getCoordinates()[1] + (9*tamaño)/20);
            boca.makeVisible();

        }
    }
    
    public void createPath(){
        int espacio = posicionesCuadrados.length - 2;
        
        path = new ArrayList<>();
        
        //Mapa tipo 1: 3 lineas.
        
        //Izquierda a derecha
        if(entrada[0] ==0){
            int primera_lin = selectRandInt(2, (int)(espacio/2));
            espacio -= primera_lin;
            
            for(int i = 0; i < primera_lin; i++){
                int[] pareja = {entrada[0] + i+1, entrada[1]};
                path.add(pareja);
                posicionesCuadrados[entrada[0]+i+1][entrada[1]].changeColor("green");
            }
            
            int segunda_lin = Math.abs(entrada[1]-salida[1]);
            int[] ultimoCuadrado = path.get(path.size()-1); 
            if(entrada[1] < salida[1]){
                for(int i = 0; i < segunda_lin; i++){
                    int[] pareja = {ultimoCuadrado[0], ultimoCuadrado[1]+ 1 + i};
                    path.add(pareja);
                    posicionesCuadrados[ultimoCuadrado[0]][ultimoCuadrado[1]+1+i].changeColor("green");
                }
            }
            else if(entrada[1] > salida[1]){
                for(int i = 0; i < segunda_lin; i++){
                    int[] pareja = {ultimoCuadrado[0], ultimoCuadrado[1]- 1 - i};
                    path.add(pareja);
                    posicionesCuadrados[ultimoCuadrado[0]][ultimoCuadrado[1]-1-i].changeColor("green");
                }
            }
            ultimoCuadrado = path.get(path.size()-1); 
            for(int i = 0; i < espacio; i++){
                int[] pareja = {ultimoCuadrado[0]+ 1 + i, ultimoCuadrado[1]};
                path.add(pareja);
                posicionesCuadrados[ultimoCuadrado[0]+ 1 + i][ultimoCuadrado[1]].changeColor("green");
                }
            }
        
        //Arriba a abajo
        else if(entrada[1]== 0){
            int primera_lin = selectRandInt(2, (int)(espacio/2));
            espacio -= primera_lin;
            for(int i = 0; i < primera_lin; i++){
                int[] pareja = {entrada[0], entrada[1] + i+1};
                path.add(pareja);
                posicionesCuadrados[entrada[0]][entrada[1] + i+1].changeColor("green");
            }
            
            int segunda_lin = Math.abs(entrada[0]-salida[0]);
            int[] ultimoCuadrado = path.get(path.size()-1); 

            if(entrada[0] < salida[0]){
                for(int i = 0; i < segunda_lin; i++){
                    int[] pareja = {ultimoCuadrado[0] + 1 +i, ultimoCuadrado[1]};
                    path.add(pareja);
                    posicionesCuadrados[ultimoCuadrado[0] +1 + i][ultimoCuadrado[1]].changeColor("green");
                }
            }
            else if(entrada[0] > salida[0]){
                for(int i = 0; i < segunda_lin; i++){
                    int[] pareja = {ultimoCuadrado[0]- 1 - i, ultimoCuadrado[1]};
                    path.add(pareja);
                    posicionesCuadrados[ultimoCuadrado[0]- 1 - i][ultimoCuadrado[1]].changeColor("green");
                }
            }
            ultimoCuadrado = path.get(path.size()-1); 
            for(int i = 0; i < espacio; i++){
                int[] pareja = {ultimoCuadrado[0], ultimoCuadrado[1] + 1 + i};
                path.add(pareja);
                posicionesCuadrados[ultimoCuadrado[0]][ultimoCuadrado[1]+ 1 + i].changeColor("green");
            }
        }
    }
public boolean changePath(int X, int Y, int idx){
    if(idx <= 0 || idx >= path.size()-1){
        return false;
    }

    int anteriorX = path.get(idx-1)[0];
    int anteriorY = path.get(idx-1)[1];

    int siguienteX = path.get(idx+1)[0];
    int siguienteY = path.get(idx+1)[1];

    /*
     * CASO 1:
     * El camino venia horizontalmente.
     */
    if(anteriorY == Y && siguienteY == Y){

        // Intentamos pasar por arriba
        if(Y-1 >= 0 &&
           !posicionesCuadrados[anteriorX][Y-1].getColor().equals("black") &&
           !posicionesCuadrados[X][Y-1].getColor().equals("black") &&
           !posicionesCuadrados[siguienteX][Y-1].getColor().equals("black")){

            path.remove(idx);

            int[] temp1 = {anteriorX, Y-1};
            int[] temp2 = {X, Y-1};
            int[] temp3 = {siguienteX, Y-1};

            path.add(idx, temp1);
            path.add(idx+1, temp2);
            path.add(idx+2, temp3);

            return true;
        }

        // Intentamos pasar por abajo
        else if(Y+1 < posicionesCuadrados[0].length &&
                !posicionesCuadrados[anteriorX][Y+1].getColor().equals("black") &&
                !posicionesCuadrados[X][Y+1].getColor().equals("black") &&
                !posicionesCuadrados[siguienteX][Y+1].getColor().equals("black")){

            path.remove(idx);

            int[] temp1 = {anteriorX, Y+1};
            int[] temp2 = {X, Y+1};
            int[] temp3 = {siguienteX, Y+1};

            path.add(idx, temp1);
            path.add(idx+1, temp2);
            path.add(idx+2, temp3);

            return true;
        }

        // Intentamos dar una vuelta por arriba
        else if(Y-1 >= 0 &&
                Y-2 >= 0 &&
                !posicionesCuadrados[anteriorX][Y-1].getColor().equals("black") &&
                !posicionesCuadrados[anteriorX][Y-2].getColor().equals("black") &&
                !posicionesCuadrados[X][Y-2].getColor().equals("black") &&
                !posicionesCuadrados[siguienteX][Y-2].getColor().equals("black")){

            path.remove(idx);

            int[] temp1 = {anteriorX, Y-1};
            int[] temp2 = {anteriorX, Y-2};
            int[] temp3 = {X, Y-2};
            int[] temp4 = {siguienteX, Y-2};
            int[] temp5 = {siguienteX, Y-1};

            path.add(idx, temp1);
            path.add(idx+1, temp2);
            path.add(idx+2, temp3);
            path.add(idx+3, temp4);
            path.add(idx+4, temp5);

            return true;
        }

        // Intentamos dar una vuelta por abajo
        else if(Y+1 < posicionesCuadrados[0].length &&
                Y+2 < posicionesCuadrados[0].length &&
                !posicionesCuadrados[anteriorX][Y+1].getColor().equals("black") &&
                !posicionesCuadrados[anteriorX][Y+2].getColor().equals("black") &&
                !posicionesCuadrados[X][Y+2].getColor().equals("black") &&
                !posicionesCuadrados[siguienteX][Y+2].getColor().equals("black")){

            path.remove(idx);

            int[] temp1 = {anteriorX, Y+1};
            int[] temp2 = {anteriorX, Y+2};
            int[] temp3 = {X, Y+2};
            int[] temp4 = {siguienteX, Y+2};
            int[] temp5 = {siguienteX, Y+1};

            path.add(idx, temp1);
            path.add(idx+1, temp2);
            path.add(idx+2, temp3);
            path.add(idx+3, temp4);
            path.add(idx+4, temp5);

            return true;
        }
    }

    /*
     * CASO 2:
     * El camino venia verticalmente.
     */
    else if(anteriorX == X && siguienteX == X){

        // Intentamos pasar por la izquierda
        if(X-1 >= 0 &&
           !posicionesCuadrados[X-1][anteriorY].getColor().equals("black") &&
           !posicionesCuadrados[X-1][Y].getColor().equals("black") &&
           !posicionesCuadrados[X-1][siguienteY].getColor().equals("black")){

            path.remove(idx);

            int[] temp1 = {X-1, anteriorY};
            int[] temp2 = {X-1, Y};
            int[] temp3 = {X-1, siguienteY};

            path.add(idx, temp1);
            path.add(idx+1, temp2);
            path.add(idx+2, temp3);

            return true;
        }

        // Intentamos pasar por la derecha
        else if(X+1 < posicionesCuadrados.length &&
                !posicionesCuadrados[X+1][anteriorY].getColor().equals("black") &&
                !posicionesCuadrados[X+1][Y].getColor().equals("black") &&
                !posicionesCuadrados[X+1][siguienteY].getColor().equals("black")){

            path.remove(idx);

            int[] temp1 = {X+1, anteriorY};
            int[] temp2 = {X+1, Y};
            int[] temp3 = {X+1, siguienteY};

            path.add(idx, temp1);
            path.add(idx+1, temp2);
            path.add(idx+2, temp3);

            return true;
        }
    }

    /*
     * CASO 3:
     * El camino llega a una esquina.
     */
    else if(anteriorX == X){

        if(anteriorX+1 < posicionesCuadrados.length &&
           !posicionesCuadrados[anteriorX+1][anteriorY].getColor().equals("black") &&
           !posicionesCuadrados[anteriorX+1][Y].getColor().equals("black")){

            path.remove(idx);

            int[] temp1 = {anteriorX+1, anteriorY};
            int[] temp2 = {anteriorX+1, Y};

            path.add(idx, temp1);
            path.add(idx+1, temp2);

            return true;
        }
    }

    else if(anteriorY == Y){

        if(anteriorY+1 < posicionesCuadrados[0].length &&
           !posicionesCuadrados[anteriorX][anteriorY+1].getColor().equals("black") &&
           !posicionesCuadrados[X][anteriorY+1].getColor().equals("black")){

            path.remove(idx);

            int[] temp1 = {anteriorX, anteriorY+1};
            int[] temp2 = {X, anteriorY+1};

            path.add(idx, temp1);
            path.add(idx+1, temp2);

            return true;
        }
    }

    // No existe ningún caso que sepamos resolver
    return false;
}
    private int selectRandInt(int infLimit, int supLimit){
        int numeroRand = infLimit +(int)(Math.random()*supLimit);
        return numeroRand;
    }
}
    
