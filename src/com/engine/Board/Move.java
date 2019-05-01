package com.engine.Board;

import com.engine.Figures.Figure;

public abstract class Move {
    final int destinationCoordinate;
    final Board board;
    final Figure movedFigure;

    private Move(final Board board, final Figure movedFigure, final int destinationCoordinate){
        this.destinationCoordinate = destinationCoordinate;
        this.board = board;
        this.movedFigure = movedFigure;
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public abstract Board execute();

    public static class MajorMove extends Move{
        public MajorMove(final Board board, final Figure movedFigure, final int destinationCoordinate) {
            super(board, movedFigure, destinationCoordinate);
        }
        public Board execute(){
            return null;
        }
    }
    
    public static class AttackMove extends Move{
        final Figure attackedFigure;
        public AttackMove(final Board board, final Figure movedFigure, final Figure attackedFigure, final int destinationCoordinate) {
            super(board, movedFigure, destinationCoordinate);
            this.attackedFigure = attackedFigure;
        }
        public Board execute(){
            return null;
        }
    }
}
