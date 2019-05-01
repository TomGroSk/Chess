package com.engine.Player;

import com.engine.Alliance;
import com.engine.Board.Board;
import com.engine.Board.Field;
import com.engine.Board.Move;
import com.engine.Figures.Figure;
import com.engine.Figures.King;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {
    private final boolean isInCheck;
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> playerMoves;

    Player(final Board board, final Collection<Move> playerMoves, final Collection<Move> opponentMoves){
        this.board = board;
        this.playerMoves = ImmutableList.copyOf(Iterables.concat(playerMoves, calculateKingCastles(playerMoves, opponentMoves)));
        this.playerKing = establishKing();
        this.isInCheck=!Player.calculateAttackOnField(this.playerKing.getPosition(), opponentMoves).isEmpty();
    }


    protected static Collection<Move> calculateAttackOnField(int position, Collection<Move> opponentMoves) {
        final List<Move> attacksMoves = new ArrayList<>();
        for(final Move move: opponentMoves){
            if(position == move.getDestinationCoordinate()){
                attacksMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attacksMoves);
    }

    public King getPlayerKing(){
        return this.playerKing;
    }

    public Collection<Move> getPlayerMoves() {
        return this.playerMoves;
    }

    private King establishKing() {
        for(final Figure figure: getActiveFigures()){
            if(figure.getFigureType().isKing()){
                return (King) figure;
            }
        }
        throw new RuntimeException("Shouldn't reach this exception!");
    }

    public boolean isMoveLegal(final Move move){
        return this.playerMoves.contains(move);
    }
    public boolean isInCheck(){
        return this.isInCheck;
    }
    public boolean isInCheckMate(){
        return this.isInCheck && !canEscape();
    }

    public boolean isInStaleMate(){
        return !this.isInCheck && !canEscape();
    }

    public boolean isCastled(){
        return false;
    }

    public MoveTransition makeMove(final Move move){
        if(!isMoveLegal(move)){
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionBoard = move.execute();

        final Collection<Move> kingAttacks = Player.calculateAttackOnField(
                transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPosition(),
                transitionBoard.currentPlayer().getPlayerMoves());
        if(!kingAttacks.isEmpty()){
            return new MoveTransition(this.board, move, MoveStatus.LEAVE_PLAYER_IN_CHECK);
        }
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    protected boolean canEscape() {
        for(final Move move: this.playerMoves){
            final MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone()){
                return true;
            }
        }
        return false;
    }

    public abstract Collection<Figure> getActiveFigures();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals,
                                                             Collection<Move>opponentsLegals);
}
