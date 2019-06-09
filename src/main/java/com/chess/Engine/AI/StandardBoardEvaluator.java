package com.chess.Engine.AI;

import com.chess.Engine.Board.Board;
import com.chess.Engine.Figures.Figure;
import com.chess.Engine.Player.Player;

public final class StandardBoardEvaluator implements BoardEvaluator {
    private final static int CHECK_MATE_BONUS = 10000;
    private final static int CHECK_BONUS = 50;
    private final static int CASTLE_BONUS = 60;
    private final static int MOBILITY_MULTIPLIER = 2;
    @Override
    public int evaluate(Board board, int depth) {
        return score(board.whitePlayer(), depth) - score(board.blackPlayer(), depth);
    }
    private static int score(final Player player,
                             final int depth) {
        return pieceValue(player) + check(player) + checkMate(player, depth)
                + mobility(player) + castled(player);
    }

    private static int pieceValue(Player player) {
        int figureValueScore = 0;
        for(final Figure figure: player.getActiveFigures()){
            figureValueScore += figure.getFigureValue();
        }
        return figureValueScore;
    }

    private static int mobility(final Player player) {
        return MOBILITY_MULTIPLIER * player.getPlayerMoves().size();
    }
    private static int check(final Player player) {
        return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
    }

    private static int depthBonus(final int depth) {
        return depth == 0 ? 1 : 100 * depth;
    }

    private static int checkMate(final Player player, final int depth) {
        return player.getOpponent().isInCheckMate() ? CHECK_MATE_BONUS  * depthBonus(depth) : 0;
    }

    private static int castled(final Player player) {
        return player.isCastled() ? CASTLE_BONUS : 0;
    }

}
