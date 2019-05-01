package com.engine.Board;

import com.engine.Alliance;
import com.engine.Figures.*;
import com.engine.Player.BlackPlayer;
import com.engine.Player.Player;
import com.engine.Player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;

public class Board {

    private final List<Field> gameBoard;

    private final Collection<Figure> whiteFigures;
    private final Collection<Figure> blackFigures;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;

    private final Player currentPlayer;

    private Board(final Builder builder){
        this.gameBoard = createGameBoard(builder);
        this.whiteFigures = calculateActiveFigures(this.gameBoard, Alliance.white);
        this.blackFigures = calculateActiveFigures(this.gameBoard, Alliance.black);
        final Collection<Move>whiteLegalMoves = calculateLegalMoves(this.whiteFigures);
        final Collection<Move>blackLegalMoves = calculateLegalMoves(this.blackFigures);
        this.whitePlayer = new WhitePlayer(this, whiteLegalMoves, blackLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteLegalMoves, blackLegalMoves);
        this.currentPlayer=builder.nextMove.choosePlayer(this.whitePlayer, this.blackPlayer);
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
        //WHITE
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
        //BLACK
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
        builder.setFigure(new King(59, Alliance.white));
        builder.setFigure(new Queen(60, Alliance.white));
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
}
