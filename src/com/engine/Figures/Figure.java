package com.engine.Figures;

import com.engine.Alliance;
import com.engine.Board.Board;
import com.engine.Board.Move;

import java.nio.channels.Pipe;
import java.util.Collection;

public abstract class Figure {
    protected final int position;
    protected final Alliance alliance;
    protected final boolean isFirstMove;
    protected final FigureType figureType;
    private final int cacheHash;

    Figure(final int position, final Alliance alliance, final FigureType figureType){
        this.position = position;
        this.alliance = alliance;
        this.isFirstMove = false;
        this.figureType = figureType;
        cacheHash = calculateHashCode();
    }

    private int calculateHashCode() {
        int result = figureType.hashCode();
        result = 31 * result + alliance.hashCode();
        result = 31 * result + position;
        result = 31 * result +(isFirstMove ? 1 : 0);
        return result;
    }

    public FigureType getFigureType() {
        return this.figureType;
    }
    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Figure)){
            return false;
        }
        final Figure otherFigure = (Figure) other;
        return position == otherFigure.getPosition() && figureType == otherFigure.getFigureType() &&
                alliance == otherFigure.getAlliance() && isFirstMove == otherFigure.isFirstMove();
    }
    @Override
    public int hashCode(){
        return this.cacheHash;
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
            public boolean isRook(){
                return false;
            }
        },
        KNIGHT("N"){
            public boolean isKing(){
                return false;
            }
            public boolean isRook(){
                return false;
            }
        },
        BISHOP("B"){
            public boolean isKing(){
                return false;
            }
            public boolean isRook(){
                return false;
            }
        },
        ROOK("R"){
            public boolean isKing(){
                return false;
            }
            public boolean isRook(){
                return true;
            }
        },
        QUEEN("Q"){
            public boolean isKing(){
                return false;
            }
            public boolean isRook(){
                return false;
            }
        },
        KING("K"){
            public boolean isKing(){
                return true;
            }
            public boolean isRook(){
                return false;
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
        public abstract boolean isRook();
    }
    public abstract Figure moveFigure(Move move);
    public abstract Collection<Move> calculatePossibleMoves(final Board board);
}

