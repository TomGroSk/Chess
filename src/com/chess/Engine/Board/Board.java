package com.chess.Engine.Board;

import com.chess.Engine.Alliance;
import com.chess.Engine.Figures.*;
import com.chess.Engine.Move.Move;
import com.chess.Engine.Player.BlackPlayer;
import com.chess.Engine.Player.Player;
import com.chess.Engine.Player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;

import java.util.*;

public class Board {

    private final List<Field> gameBoard;

    private final Collection<Figure> whiteFigures;
    private final Collection<Figure> blackFigures;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;

    private final Player currentPlayer;

    private  final Pawn enPassantPawn;

    private Board(final Builder builder){
        this.gameBoard = createGameBoard(builder);

        this.enPassantPawn = builder.enPassantPawn;

        this.whiteFigures = calculateActiveFigures(this.gameBoard, Alliance.white);
        this.blackFigures = calculateActiveFigures(this.gameBoard, Alliance.black);

        final Collection<Move>whiteLegalMoves = calculateLegalMoves(this.whiteFigures);
        final Collection<Move>blackLegalMoves = calculateLegalMoves(this.blackFigures);

        this.whitePlayer = new WhitePlayer(this, whiteLegalMoves, blackLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteLegalMoves, blackLegalMoves);

        this.currentPlayer=builder.nextMove.choosePlayer(this.whitePlayer, this.blackPlayer);
    }

    public Pawn getEnPassantPawn(){
        return this.enPassantPawn;
    }

    public Player whitePlayer(){
        return this.whitePlayer;
    }

    public Player blackPlayer(){
        return this.blackPlayer;
    }

    public Player currentPlayer(){
        return this.currentPlayer;
    }

    private Collection<Move> calculateLegalMoves(final Collection<Figure> figures) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final Figure figure: figures){
            legalMoves.addAll(figure.calculatePossibleMoves(this));
        }
        return ImmutableList.copyOf(legalMoves);
    }

    public Collection<Figure>getBlackFigures(){
        return this.blackFigures;
    }

    public Collection<Figure>getWhiteFigures(){
        return this.whiteFigures;
    }

    @Override
    public String toString(){
        final StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<BoardUtils.numFields;i++){
            final String fieldText = this.gameBoard.get(i).toString();
            stringBuilder.append(String.format("%3s", fieldText));
            if((i+1)%BoardUtils.numFieldsPerRow == 0){
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

//    private static String boardPrint(final Field field) {
//        if(!field.isEmpty()){
//            return field.getFigure().getAlliance().isBlack() ? field.toString().toLowerCase() : field.toString();
//        }
//        return field.toString();
//    }

    private static Collection<Figure> calculateActiveFigures(final List<Field> gameBoard, final Alliance alliance) {
        final List<Figure> activePieces = new ArrayList<>();
        for(final Field field: gameBoard){
            if(!field.isEmpty()){
                final Figure figure = field.getFigure();
                if(figure.getAlliance() == alliance){
                    activePieces.add(figure);
                }
            }
        }
        return ImmutableList.copyOf(activePieces);
    }

    public Field getField(final int coordinate){
        return gameBoard.get(coordinate);
    }

    private static List<Field> createGameBoard(final Builder builder){
        final Field[] fields = new Field[BoardUtils.numFields];
        for(int i=0;i<BoardUtils.numFields;i++){
            fields[i] = Field.createField(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(fields);
    }
    public static Board createStandardBoard(){
        Builder builder=new Builder();

        builder.setFigure(new Rook(0, Alliance.black));
        builder.setFigure(new Knight(1, Alliance.black));
        builder.setFigure(new Bishop(2, Alliance.black));
        builder.setFigure(new Queen(3, Alliance.black));
        builder.setFigure(new King(4, Alliance.black));
        builder.setFigure(new Bishop(5, Alliance.black));
        builder.setFigure(new Knight(6, Alliance.black));
        builder.setFigure(new Rook(7, Alliance.black));
        builder.setFigure(new Pawn(8, Alliance.black));
        builder.setFigure(new Pawn(9, Alliance.black));
        builder.setFigure(new Pawn(10, Alliance.black));
        builder.setFigure(new Pawn(11, Alliance.black));
        builder.setFigure(new Pawn(12, Alliance.black));
        builder.setFigure(new Pawn(13, Alliance.black));
        builder.setFigure(new Pawn(14, Alliance.black));
        builder.setFigure(new Pawn(15, Alliance.black));

        builder.setFigure(new Pawn(48, Alliance.white));
        builder.setFigure(new Pawn(49, Alliance.white));
        builder.setFigure(new Pawn(50, Alliance.white));
        builder.setFigure(new Pawn(51, Alliance.white));
        builder.setFigure(new Pawn(52, Alliance.white));
        builder.setFigure(new Pawn(53, Alliance.white));
        builder.setFigure(new Pawn(54, Alliance.white));
        builder.setFigure(new Pawn(55, Alliance.white));
        builder.setFigure(new Rook(56, Alliance.white));
        builder.setFigure(new Knight(57, Alliance.white));
        builder.setFigure(new Bishop(58, Alliance.white));
        builder.setFigure(new Queen(59, Alliance.white));
        builder.setFigure(new King(60, Alliance.white));
        builder.setFigure(new Bishop(61, Alliance.white));
        builder.setFigure(new Knight(62, Alliance.white));
        builder.setFigure(new Rook(63, Alliance.white));

        builder.setNextMoveMaker(Alliance.white);

        return builder.build();
    }

    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getPlayerMoves(),
                this.blackPlayer.getPlayerMoves()));
    }


    public static class Builder{
        Map<Integer, Figure> boardConfig;
        Alliance nextMove;
        Pawn enPassantPawn;
        public Builder(){
            this.boardConfig = new HashMap<>();
        }
        public Builder setFigure(final Figure figure){
            this.boardConfig.put(figure.getPosition(), figure);
            return this;
        }

        public Builder setNextMoveMaker(final Alliance nextMove) {
            this.nextMove = nextMove;
            return this;
        }

        public Board build(){
            return new Board(this);
        }

        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }
    }

    public static class BoardUtils {
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
}
