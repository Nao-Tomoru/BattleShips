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
    // private static byte[][] gridArr = new byte[10][10]; //game grid

    public static void main(String[] args) { // main function

        for (int i = 0; i < length; i++) { // initialize field
            for (int j = 0; j < length; j++) {
                gridArr[i][j] = 0;
            }
        }
        generate(); // set ships
        print(); // show field
        startGame(); // start game

    }

    public static void print() {
        for (int i = 0; i < length; i++) {

            System.out.print("|");
            for (int j = 0; j < length; j++) {
                char square;
                switch (gridArr[i][j]) { // 0 - empty, 1 - has ship, 2 - hit, -1 - empty hit
                    case 0:
                    case 1:
                        square = ' ';
                        break;

                    case 2:
                        square = 'x';
                        break;
                    case -1:
                        square = '.';
                        break;
                    default:
                        square = 'e';
                        break;
                }
                System.out.print(square + "|");

            }
            System.out.println();
        }

    }

    public static void generate() {
        short maxBattleships = 1;
        short maxDestroyers = 2;
        short currentBattleships = 0;
        short currentDestroyers = 0;
        boolean orient;
        boolean isOk;
        boolean isBattleShip = true;

        do {
            int orientCoord = 0;
            int nonOrientCoord = 0;
            int orientInt = 0;
            int lenOfShip = 0;

            orientInt = rand.nextInt(2);
            if (orientInt == 0)
                orient = true; // horizontal
            else
                orient = false;// vertical

            if (currentBattleships < maxBattleships) {

                nonOrientCoord = rand.nextInt(10);
                orientCoord = rand.nextInt(5);
                lenOfShip = 5;
                isBattleShip =true;
            } else {

                nonOrientCoord = rand.nextInt(10);
                orientCoord = rand.nextInt(6);
                lenOfShip = 4;
                isBattleShip = false;
            }

            isOk = check(lenOfShip, orient, orientCoord, nonOrientCoord);
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
    public static Boolean check(int lenOfShip, boolean orient, int orientCoord, int nonOrientCoord) { //check if place for a ship is free
                                                                                                      // true -
                                                                                                      // horizontal,
                                                                                                      // false -
                                                                                                      // vertical
        boolean isOk = true;
        if (orientCoord + lenOfShip > 9)
            isOk = false;

        for (int i = 0; i < lenOfShip; i++) {
            if (orient) {
                if (gridArr[nonOrientCoord][orientCoord + i] == 1)
                    isOk = false;
            } else if (gridArr[orientCoord + i][nonOrientCoord] == 1)
                isOk = false;
        }

        return isOk;
    }

    public static void setShip(int lenOfShip, boolean orient, int orientCoord, int nonOrientCoord) { // put ship on the field
        Ship ship = new Ship(lenOfShip, orient, orientCoord, nonOrientCoord);
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
        Pattern pattern = Pattern.compile("[abcdefghij][0-9]{1,2}", Pattern.CASE_INSENSITIVE);
        Matcher matcher;
        do {
            command = scn.nextLine() + " ";
            matcher = pattern.matcher(command);
            if (!matcher.find()) {
                continue;
            }

            cmd = command.toUpperCase().toCharArray();
            x = cmd[0] - 65;
            char[] c = new char[] { cmd[1], cmd[2] };
            y = Integer.parseInt(new String(c).strip()) - 1;
            if (y > 9) {
                continue;
            }
            if (gridArr[x][y] == 1 || gridArr[x][y] == 2) {
                gridArr[x][y] = 2;
            } else {
                gridArr[x][y] = -1;
            }
            print();

            isAllDestroyed = checkShips();
        } while (!isAllDestroyed);
        System.out.println("You won!");
        scn.close();
    }

    public static boolean checkShips() {
        for (int i = 0; i < list.size(); i++) {
            Ship curShip = list.get(i);
            for (int j = 0; j < curShip.getLenOfShip(); j++) {
                if (curShip.getOrient()) {
                    if (gridArr[curShip.getNonOrientCoord()][curShip.getOrientCoord() + j] != 2) {
                        return false;
                    }
                } else if (gridArr[curShip.getOrientCoord() + j][curShip.getNonOrientCoord()] != 2) {
                    return false;
                }
            }
        }
        return true;

    }

}