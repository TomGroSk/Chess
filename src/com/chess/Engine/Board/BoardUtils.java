package com.chess.Engine.Board;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class BoardUtils {
    public static boolean[] firstColumn = initColumn(0);
    public static boolean[] secondColumn = initColumn(1);
    public static boolean[] seventhColumn = initColumn(6);
    public static boolean[] eighthColumn = initColumn(7);

    public static boolean[] firstRow = initRow(0);
    public static boolean[] secondRow = initRow(8);
    public static boolean[] thirdRow = initRow(16);
    public static boolean[] fourthRow = initRow(24);
    public static boolean[] fifthRow = initRow(32);
    public static boolean[] sixthRow = initRow(40);
    public static boolean[] seventhRow = initRow(48);
    public static boolean[] eighthRow = initRow(56);

    public static final String[] ALGEBRAIC_NOTATION = initAlgebricNotation();
    public static final Map<String, Integer> POSITION_TO_COORDINATE = initPositionToCoordinate();

    private static boolean[] initRow(int i) {
        final boolean[] row = new boolean[numFields];
        for(int j=0;j<numFieldsPerRow;j++){
            row[i+j]=true;
        }
        return row;
    }

    public final static int numFields = 64;
    public final static int numFieldsPerRow = 8;



    public static boolean isValidCoordinate(int coordinate){
        return coordinate>=0 && coordinate<64;
    }

    private BoardUtils(){
        throw new RuntimeException("Can't instantiate BoardUtils!");
    }

    private static boolean[] initColumn(int columnNumber) {
        final boolean[] column = new boolean[numFields];
        do{
            column[columnNumber]=true;
            columnNumber+=8;
        }while (columnNumber<64);
        return column;
    }

    private static String[] initAlgebricNotation() {
        return new String[] {
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
        };
    }

    private static Map<String, Integer> initPositionToCoordinate() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = 0; i < numFields; i++) {
            positionToCoordinate.put(ALGEBRAIC_NOTATION[i], i);
        }
        return ImmutableMap.copyOf(positionToCoordinate);
    }

    public static int getCoordinateAtPosition(final String position) {
        return POSITION_TO_COORDINATE.get(position);
    }
    public static String getPositionAtCoordinate(final int coordinate){
        return ALGEBRAIC_NOTATION[coordinate];
    }
}
