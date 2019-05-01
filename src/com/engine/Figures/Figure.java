package com.engine.Figures;

import com.engine.Alliance;
import com.engine.Board.Board;
import com.engine.Board.Move;

import java.util.Collection;

public abstract class Figure {
    protected final int position;
    protected final Alliance alliance;
    protected final boolean isFirstMove;

    Figure(final int position, final Alliance alliance){
        this.position = position;
        this.alliance = alliance;
        this.isFirstMove = false;
    }
    public Alliance getAlliance(){
        return this.alliance;
    }
    public boolean isFirstMove(){
        return this.isFirstMove;
    }
    public int getPosition(){
        return this.position;
    }

    public enum FigureType{
        PAWN("P"),
        KNIGHT("N"),
        BISHOP("B"),
        ROOK("R"),
        QUEEN("Q"),
        KING("K");

        private String figureName;
        FigureType(String figureName){
            this.figureName = figureName;
        }
        @Override
        public String toString(){
            return this.figureName;
        }
    }

    public abstract Collection<Move> calculatePossibleMoves(final Board board);
}

