package com.engine.Board;

import com.engine.Pawns.Pawn;

public abstract class Move {
    final int destinationCoordinate;
    final Board board;
    final Pawn movedPawn;

    private Move(final Board board, final Pawn movedPawn, final int destinationCoordinate){
        this.destinationCoordinate = destinationCoordinate;
        this.board = board;
        this.movedPawn = movedPawn;
    }

    public static final class majorMove extends Move{
        public majorMove(final Board board, final Pawn movedPawn, final int destinationCoordinate) {
            super(board, movedPawn, destinationCoordinate);
        }
    }

    public static final class attackMove extends Move{
        final Pawn attackedPawn;
        public attackMove(final Board board, final Pawn movedPawn, final Pawn attackedPawn, final int destinationCoordinate) {
            super(board, movedPawn, destinationCoordinate);
            this.attackedPawn = attackedPawn;
        }
    }
}
