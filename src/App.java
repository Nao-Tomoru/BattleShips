import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    private static Random rand = new Random();
    private static List<Ship> list = new ArrayList<>();
    private static int length = 10; // lenght of columns and rows
    private static byte[][] gridArr = new byte[10][10]; // game grid

    public static void main(String[] args) { // main function

        for (int i = 0; i < length; i++) { // initialize field
            for (int j = 0; j < length; j++) {
                gridArr[i][j] = 0;
            }
        }
        generate(); // set ships
        ClearConsole();
        print(); // show field
        startGame(); // start game

    }

    public static void ClearConsole() {
        try {
            String operatingSystem = System.getProperty("os.name");// Check the current operating system

            if (operatingSystem.contains("Windows")) {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();

                startProcess.waitFor();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void print() { // print field

        System.out.println("| |1|2|3|4|5|6|7|8|9|10");
        for (int i = 0; i < length; i++) {
            int iChar = 65 + i;
            char cChar = (char) iChar;
            System.out.print("|" + cChar + "|");
            for (int j = 0; j < length; j++) {
                char square;
                switch (gridArr[i][j]) { // 0 - empty, 1 - has ship, 2 - hit, -1 - empty hit
                    case 0:
                    case 1:
                        square = ' '; // default tile, empty or with hidden ship
                        break;

                    case 2:
                        square = 'x'; // ship is hit
                        break;
                    case -1:
                        square = '.'; // miss
                        break;
                    default:
                        square = 'e'; // Error if something goes wrong
                        break;
                }
                System.out.print(square + "|");

            }
            System.out.println();
        }

    }

    public static void print(String lastInput) {
        ClearConsole();
        System.out.println("Last input: " + lastInput);
        System.out.println("Ships remaning: " + list.size());
        print();
    }

    public static void generate() {
        short maxBattleships = 1;
        short maxDestroyers = 2;
        short currentBattleships = 0;
        short currentDestroyers = 0;
        boolean orient; // 0 - ship is horizontal , 1 - ship is vertical
        boolean isOk;
        boolean isBattleShip = true;

        do {
            int orientCoord = 0; // orientation coordinate is coordinate where is ship going to take it's lenght
                                 // space
            int nonOrientCoord = 0; // non orientation coordinate is coordinate where is going to take only 1 space
            int orientInt = 0; // used to conver random int to orientation of the ship
            int lenOfShip = 0; // lenght of the ship

            orientInt = rand.nextInt(2);
            if (orientInt == 0)
                orient = true; // horizontal
            else
                orient = false;// vertical

            if (currentBattleships < maxBattleships) {

                nonOrientCoord = rand.nextInt(10);
                orientCoord = rand.nextInt(5); // because battleship is going to take 5 spaces in one direction -
                                               // orientation coordinate is
                                               // generated on 5 less, than other coordinate to prevent out of bounds
                lenOfShip = 5;
                isBattleShip = true;
            } else {

                nonOrientCoord = rand.nextInt(10);
                orientCoord = rand.nextInt(6); // similar as in case of battleship
                lenOfShip = 4;
                isBattleShip = false;
            }

            isOk = check(lenOfShip, orient, orientCoord, nonOrientCoord); // checking if spaces for ship are free
            if (isOk) {
                setShip(lenOfShip, orient, orientCoord, nonOrientCoord);
                if (isBattleShip) {
                    currentBattleships++;
                } else {
                    currentDestroyers++;
                }
            }

        } while (currentBattleships != maxBattleships || currentDestroyers != maxDestroyers);

    }

    public static Boolean check(int lenOfShip, boolean orient, int orientCoord, int nonOrientCoord) { // check if place
                                                                                                      // for a ship is
                                                                                                      // free
                                                                                                      // true -
                                                                                                      // horizontal,
                                                                                                      // false -
                                                                                                      // vertical
        boolean isOk = true;
        if (orientCoord + lenOfShip > 9) // check if out of bounds
            isOk = false;

        for (int i = 0; i < lenOfShip; i++) {
            if (orient) {
                if (gridArr[nonOrientCoord][orientCoord + i] == 1) // checking horizontal coord
                    isOk = false;
            } else if (gridArr[orientCoord + i][nonOrientCoord] == 1) // checking vertical coord
                isOk = false;
        }

        return isOk;
    }

    public static void setShip(int lenOfShip, boolean orient, int orientCoord, int nonOrientCoord) { // put ship on the
                                                                                                     // field
        Ship ship = new Ship(lenOfShip, orient, orientCoord, nonOrientCoord); // create new Ship
        for (int i = 0; i < lenOfShip; i++) {
            if (orient) {
                gridArr[nonOrientCoord][orientCoord + i] = 1;
            } else
                gridArr[orientCoord + i][nonOrientCoord] = 1;
        }
        list.add(ship);
    }

    public static void startGame() {
        boolean isAllDestroyed = false;
        Scanner scn = new Scanner(System.in);
        String command;
        char[] cmd;
        int x, y;
        Pattern pattern = Pattern.compile("^[a-j][\\d]{1,2}$", Pattern.CASE_INSENSITIVE); // regex pattern for
                                                                                          // input check
        Matcher matcher;
        do {
            command = scn.nextLine(); // adding space to prevent out-of-bounds in parsing
            matcher = pattern.matcher(command);
            if (!matcher.find()) {
                continue;
            }
            cmd = command.toUpperCase().toCharArray();
            x = cmd[0] - 65; // converting char to int
            char[] c;
            if (cmd.length >= 3) {
                c = new char[] { cmd[1], cmd[2] }; // getting last to characters from input string
                if (!Character.isDigit(cmd[2]) || cmd[1] == '0')
                    continue;
            } else
                c = new char[] { cmd[1] };

            y = Integer.parseInt(new String(c)) - 1;
            if (y > 9 || y < 0) {// check if number part is correct
                continue;
            }
            switch (gridArr[x][y]) {
                case 1:
                case 2:
                    gridArr[x][y]=2;
                    break;
            
                default:
                    gridArr[x][y]=-1;
                    break;
            }
            isAllDestroyed = checkShips();
            print(command); // draw a new field

        } while (!isAllDestroyed);
        System.out.println("You won!");
        scn.close();
    }

    public static boolean checkShips() {

        int partsDestroyed;
        for (int i = 0; i < list.size(); i++) { // getting amount of ships
            partsDestroyed = 0;
            Ship curShip = list.get(i);
            for (int j = 0; j < curShip.getLenOfShip(); j++) { // check on how many parts destroyed
                if (curShip.getOrient()) {
                    if (gridArr[curShip.getNonOrientCoord()][curShip.getOrientCoord() + j] == 2) {
                        partsDestroyed++;
                    }
                } else if (gridArr[curShip.getOrientCoord() + j][curShip.getNonOrientCoord()] == 2) {
                    partsDestroyed++;
                }
            }

            if (partsDestroyed == curShip.getLenOfShip()) { // if all parts destroyed
                list.remove(i); // delete ship
            }
        }
        return list.isEmpty(); // return if there are any more ships

    }
}