package com.engine.Pawns;

import com.engine.Alliance;
import com.engine.Board.Board;
import com.engine.Board.Move;

import java.util.List;

public abstract class Pawn {
    protected final int position;
    protected final Alliance alliance;

    Pawn(final int position, final Alliance alliance){
        this.position = position;
        this.alliance = alliance;
    }
     public abstract List<Move> calculatePossibleMoves(final Board board);
}

