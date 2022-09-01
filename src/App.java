import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class App {
    private static Random rand = new Random();
    private static List<int[]> list = new ArrayList<int[]>();
    private static int length = 10; // lenght of columns and rows
    private static byte[][] gridArr = new byte[10][10]; // game grid
    // private static byte[][] gridArr = new byte[10][10]; //game grid

    public static void main(String[] args) { // main function
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                gridArr[i][j] = 0;
            }
        }
        generate();
        print();
        startGame();

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
        int orientCoord;
        int nonOrientCoord;
        int orientInt;
        boolean orient;
        boolean isOk;

        do {
            orientInt = rand.nextInt(2);
            if (orientInt == 0)
                orient = true; // horizontal
            else
                orient = false;// vertical

            if (currentBattleships < maxBattleships) {

                nonOrientCoord = rand.nextInt(10);
                orientCoord = rand.nextInt(5);
                isOk = check(5, orient, orientCoord, nonOrientCoord);
                if (isOk) {
                    setShip(5, orient, orientCoord, nonOrientCoord);
                    currentBattleships++;
                }
            }

            if (currentDestroyers < maxDestroyers) {

                nonOrientCoord = rand.nextInt(10);
                orientCoord = rand.nextInt(6);
                isOk = check(4, orient, orientCoord, nonOrientCoord);
                if (isOk) {
                    setShip(4, orient, orientCoord, nonOrientCoord);
                    currentDestroyers++;
                }
            }
        } while (currentBattleships != maxBattleships || currentDestroyers != maxDestroyers);

    }

    public static Boolean check(int lenOfShip, boolean orient, int orientCoord, int nonOrientCoord) { // true -
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

    public static void setShip(int lenOfShip, boolean orient, int orientCoord, int nonOrientCoord) {
        int[] ship = new int[4];
        for (int i = 0; i < lenOfShip; i++) {
            if (orient) {
                gridArr[nonOrientCoord][orientCoord + i] = 1;
            } else
                gridArr[orientCoord + i][nonOrientCoord] = 1;
        }
        ship[0] = orientCoord;
        ship[1] = nonOrientCoord;
        if (orient) {
            ship[2] = 0;
        } else {
            ship[2] = 1;
        }
        ship[3] = lenOfShip;
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
            x = (int) cmd[0] - 65;
            char[] c = new char[] { cmd[1], cmd[2] };
            y = Integer.parseInt(new String(c).strip()) - 1;
            if (y > 9) {
                continue;
            }
            if (gridArr[x][y] == 1) {
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
            int[] curShip = list.get(i);
            for (int j = 0; j < curShip[3]; j++) {
                if (curShip[2] == 0) {
                    if (gridArr[curShip[1]][curShip[0] + j] != 2) {
                        return false;
                    }
                } else if (gridArr[curShip[0] + j][curShip[1]] != 2) {
                    return false;
                }
            }
        }
        return true;

    }

}