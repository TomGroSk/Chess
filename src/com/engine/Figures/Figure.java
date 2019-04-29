package com.engine.Figures;

import com.engine.Alliance;
import com.engine.Board.Board;
import com.engine.Board.Move;

import java.util.Collection;

public abstract class Figure {
    protected final int position;
    protected final Alliance alliance;

    Figure(final int position, final Alliance alliance){
        this.position = position;
        this.alliance = alliance;
    }
    public Alliance getAlliance(){
        return this.alliance;
    }

    public abstract Collection<Move> calculatePossibleMoves(final Board board);
}

