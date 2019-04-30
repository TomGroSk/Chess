package com.engine.Board;

public class BoardUtils {
    public static boolean[] firstColumn = initColumn(0);
    public static boolean[] secondColumn = initColumn(1);
    public static boolean[] seventhColumn = initColumn(6);
    public static boolean[] eighthColumn = initColumn(7);

    public static boolean[] secondRow = null;
    public static boolean[] seventhRow = null;

    public final static int numFields = 64;
    public final static int numFieldsPerRow = 8;



    public static boolean isValidCoordinate(int coordinate){
        return coordinate>=0 && coordinate<64;
    }

    private BoardUtils(){
        throw new RuntimeException("Can't instantiate BoardUtils!");
    }

    private static boolean[] initColumn(int columnNumber) {
        final boolean[] column = new boolean[64];
        do{
            column[columnNumber]=true;
            columnNumber+=8;
        }while (columnNumber<64);
        return column;
    }
}
