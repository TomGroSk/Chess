package com.engine.Player;

import com.engine.Alliance;
import com.engine.Board.Board;
import com.engine.Board.Move;
import com.engine.Figures.Figure;

import java.util.Collection;

public class WhitePlayer extends Player {
    public WhitePlayer(Board board, Collection<Move> whiteLegalMoves, Collection<Move> blackLegalMoves) {
        super(board, whiteLegalMoves, blackLegalMoves);
    }

    @Override
    public Collection<Figure> getActiveFigures() {
        return this.board.getWhiteFigures();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.white;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }
}
