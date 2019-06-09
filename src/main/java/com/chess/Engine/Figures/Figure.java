package com.chess.Engine.Figures;

import com.chess.Engine.Alliance;
import com.chess.Engine.Board.Board;
import com.chess.Engine.Move.Move;

import java.util.Collection;

public abstract class Figure {
    protected final int position;
    protected final Alliance alliance;
    protected final boolean isFirstMove;
    protected final FigureType figureType;
    private final int cacheHash;

    Figure(final int position, final Alliance alliance, final FigureType figureType, final boolean isFirstMove){
        this.position = position;
        this.alliance = alliance;
        this.isFirstMove = isFirstMove;
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

    public int getFigureValue() {
        return this.figureType.figureValue;
    }

    public enum FigureType{
        PAWN("P",100){
            public boolean isKing(){
                return false;
            }
            public boolean isRook(){
                return false;
            }
        },
        KNIGHT("N",300){
            public boolean isKing(){
                return false;
            }
            public boolean isRook(){
                return false;
            }
        },
        BISHOP("B",300){
            public boolean isKing(){
                return false;
            }
            public boolean isRook(){
                return false;
            }
        },
        ROOK("R",500){
            public boolean isKing(){
                return false;
            }
            public boolean isRook(){
                return true;
            }
        },
        QUEEN("Q",900){
            public boolean isKing(){
                return false;
            }
            public boolean isRook(){
                return false;
            }
        },
        KING("K",10000){
            public boolean isKing(){
                return true;
            }
            public boolean isRook(){
                return false;
            }
        };

        private String figureName;
        private int figureValue;
        FigureType(String figureName, int figureValue){
            this.figureName = figureName;
            this.figureValue=figureValue;
        }
        @Override
        public String toString(){
            return this.figureName;
        }
        public int getFigureValue(){
            return this.figureValue;
        }
        public abstract boolean isKing();
        public abstract boolean isRook();
    }
    public abstract Figure moveFigure(Move move);
    public abstract Collection<Move> calculatePossibleMoves(final Board board);
}

