package com.engine.Board;

public class BoardUtils {
    public static boolean[] firstColumn = null;
    public static boolean[] secondColumn = null;
    public static boolean[] seventhColumn = null;
    public static boolean[] eighthColumn = null;

    private BoardUtils(){
        throw new RuntimeException("Can't instantiate BoardUtils!");
    }

    public static boolean isValidCoodinate(int coordinate){
        return coordinate>=0 && coordinate<64;
    }
}
