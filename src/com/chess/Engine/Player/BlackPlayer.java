package com.chess.Engine.Player;

import com.chess.Engine.Alliance;
import com.chess.Engine.Board.Field;
import com.chess.Engine.Figures.Figure;
import com.chess.Engine.Figures.Rook;
import com.chess.Engine.Board.Board;
import com.chess.Engine.Board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentsLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if(this.playerKing.isFirstMove() && !this.isInCheck()){
            if(this.board.getField(5).isEmpty() && this.board.getField(6).isEmpty()){
                final Field rookField = this.board.getField(7);
                if(!rookField.isEmpty() && rookField.getFigure().isFirstMove()){
                    if(Player.calculateAttackOnField(5,opponentsLegals).isEmpty() &&
                            Player.calculateAttackOnField(6,opponentsLegals).isEmpty() &&
                            rookField.getFigure().getFigureType().isRook()){
                        kingCastles.add(new Move.KingCastleMove(this.board,this.playerKing,
                                             6,
                                                                (Rook) rookField.getFigure(),
                                                                rookField.getFieldCoordinate(),
                                       5));
                    }
                }
            }
            if(this.board.getField(1).isEmpty() && this.board.getField(2).isEmpty() &&
                    this.board.getField(3).isEmpty()){
                final Field rookField = this.board.getField(0);
                if(!rookField.isEmpty() && rookField.getFigure().isFirstMove()){
                    kingCastles.add(new Move.QueenCastleMove(this.board,this.playerKing,
                                          2,
                                                             (Rook) rookField.getFigure(),
                                                              rookField.getFieldCoordinate(),
                                     3));
                }
            }
        }

        return ImmutableList.copyOf(kingCastles);
    }
}
