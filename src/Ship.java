/**
 * Ship
 */
public class Ship {
    private int lenOfShip;
    private boolean orient;
    private int orientCoord;
    private int nonOrientCoord;

    public Ship(int lenOfShip, boolean orient, int orientCoord, int nonOrientCoord) {
        this.lenOfShip = lenOfShip;
        this.orient = orient;
        this.orientCoord = orientCoord;
        this.nonOrientCoord = nonOrientCoord;
    }   
    public int getLenOfShip(){
        return lenOfShip;
    }
    public boolean getOrient(){
        return orient;
    }
    public int getOrientCoord(){
        return orientCoord;
    }
    public int getNonOrientCoord(){
        return nonOrientCoord;
    }
    
}