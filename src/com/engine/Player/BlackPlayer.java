package com.engine.Player;

import com.engine.Alliance;
import com.engine.Board.Board;
import com.engine.Board.Move;
import com.engine.Figures.Figure;

import java.util.Collection;

public class BlackPlayer extends Player {
    public BlackPlayer(Board board, Collection<Move> whiteLegalMoves, Collection<Move> blackLegalMoves) {
        super(board, blackLegalMoves, whiteLegalMoves);
    }

    @Override
    public Collection<Figure> getActiveFigures() {
        return this.board.getBlackFigures();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.black;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }
}
