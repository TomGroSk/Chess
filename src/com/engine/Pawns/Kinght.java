package com.engine.Pawns;

import com.engine.Alliance;
import com.engine.Board.Board;
import com.engine.Board.Move;

import java.util.List;

public class Kinght extends Pawn {

    Kinght(final int position, final Alliance alliance) {
        super(position, alliance);
    }

    @Override
    public List<Move> calculatePossibleMoves(Board board) {
        return null;
    }
}
