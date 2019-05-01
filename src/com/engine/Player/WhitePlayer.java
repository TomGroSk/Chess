package com.engine.Player;

import com.engine.Alliance;
import com.engine.Board.Board;
import com.engine.Board.Field;
import com.engine.Board.Move;
import com.engine.Figures.Figure;
import com.engine.Figures.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentsLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if(this.playerKing.isFirstMove() && !this.isInCheck()){
            if(this.board.getField(61).isEmpty() && this.board.getField(62).isEmpty()){
                final Field rookField = this.board.getField(63);
                if(!rookField.isEmpty() && rookField.getFigure().isFirstMove()){
                    if(Player.calculateAttackOnField(61,opponentsLegals).isEmpty() &&
                            Player.calculateAttackOnField(62,opponentsLegals).isEmpty() &&
                            rookField.getFigure().getFigureType().isRook()){
                        kingCastles.add(new Move.KingCastleMove(this.board,this.playerKing,
                                             62,
                                                                (Rook) rookField.getFigure(),
                                                                rookField.getFieldCoordinate(),
                                       61));
                    }
                }
            }
            if(this.board.getField(59).isEmpty() && this.board.getField(58).isEmpty() &&
                this.board.getField(57).isEmpty()){
                final Field rookField = this.board.getField(56);
                if(!rookField.isEmpty() && rookField.getFigure().isFirstMove()){
                    kingCastles.add(new Move.QueenCastleMove(this.board,this.playerKing,
                                          58,
                                                            (Rook) rookField.getFigure(),
                                                            rookField.getFieldCoordinate(),
                                    59));
                }
            }
        }

        return ImmutableList.copyOf(kingCastles);
    }
}
