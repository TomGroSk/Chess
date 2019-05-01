package com.engine.Figures;

import com.engine.Alliance;
import com.engine.Board.Board;
import com.engine.Board.Move;

import java.util.Collection;

public abstract class Figure {
    protected final int position;
    protected final Alliance alliance;
    protected final boolean isFirstMove;
    protected final FigureType figureType;

    Figure(final int position, final Alliance alliance, final FigureType figureType){
        this.position = position;
        this.alliance = alliance;
        this.isFirstMove = false;
        this.figureType = figureType;
    }

    public FigureType getFigureType() {
        return this.figureType;
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
        PAWN("P"){
            public boolean isKing(){
                return false;
            }
        },
        KNIGHT("N"){
            public boolean isKing(){
                return false;
            }
        },
        BISHOP("B"){
            public boolean isKing(){
                return false;
            }
        },
        ROOK("R"){
            public boolean isKing(){
                return false;
            }
        },
        QUEEN("Q"){
            public boolean isKing(){
                return false;
            }
        },
        KING("K"){
            public boolean isKing(){
                return true;
            }
        };

        private String figureName;
        FigureType(String figureName){
            this.figureName = figureName;
        }
        @Override
        public String toString(){
            return this.figureName;
        }
        public abstract boolean isKing();
    }

    public abstract Collection<Move> calculatePossibleMoves(final Board board);
}

