package com.engine.Pawns;

import com.engine.Alliance;
import com.engine.Board.Board;
import com.engine.Board.Move;

import java.util.Collection;

public abstract class Pawn {
    protected final int position;
    protected final Alliance alliance;

    Pawn(final int position, final Alliance alliance){
        this.position = position;
        this.alliance = alliance;
    }
    public Alliance getAlliance(){
        return this.alliance;
    }

    public abstract Collection<Move> calculatePossibleMoves(final Board board);
}

