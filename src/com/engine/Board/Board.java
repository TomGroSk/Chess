package com.engine.Board;

import com.engine.Alliance;
import com.engine.Figures.Figure;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;

public class Board {

    private final List<Field> gameBoard;

    private Board(Builder builder){
        this.gameBoard = createGameBoard(builder);
    }
    public Field getField(final int coordinate){
        return null;
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
        final Board board = new Board(builder);
        return board;
    }

    public static class Builder{

        Map<Integer, Figure> boardConfig;
        Alliance nextMove;
        public Builder(){

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
    }
}
