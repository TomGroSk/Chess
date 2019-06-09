package Board;

import com.chess.Engine.AI.MiniMax;
import com.chess.Engine.AI.MoveStrategy;
import com.chess.Engine.Alliance;
import com.chess.Engine.Board.Board;

import com.chess.Engine.Figures.King;
import com.chess.Engine.Figures.Knight;
import com.chess.Engine.Figures.Queen;
import com.chess.Engine.Move.Move;
import com.chess.Engine.Move.MoveTransition;
import org.junit.Test;

import java.util.Collection;

import static com.chess.Engine.Board.Board.*;
import static com.chess.Engine.Move.Move.*;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

public class BoardTest {
    @Test
    public void initialBoard() {

        final Board board = createStandardBoard();

        assertEquals(board.currentPlayer().getPlayerMoves().size(), 20);
        assertEquals(board.currentPlayer().getOpponent().getPlayerMoves().size(), 20);
        assertFalse(board.currentPlayer().isInCheck());
        assertFalse(board.currentPlayer().isInCheckMate());

        assertEquals(board.currentPlayer(), board.whitePlayer());
        assertEquals(board.currentPlayer().getOpponent(), board.blackPlayer());
        assertFalse(board.currentPlayer().getOpponent().isInCheck());
        assertFalse(board.currentPlayer().getOpponent().isInCheckMate());
    }

    @Test
    public void testMiddleQueenOnEmptyBoard() {
        final Builder builder = new Builder();
        builder.setFigure(new King(4, Alliance.black, false));
        builder.setFigure(new Queen(36, Alliance.white));
        builder.setFigure(new King(60, Alliance.white, false));
        builder.setNextMoveMaker(Alliance.white);
        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getPlayerMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getPlayerMoves();
        assertEquals(whiteLegals.size(), 31);
        assertEquals(blackLegals.size(), 5);
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e8"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e7"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e6"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e5"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e3"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e2"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("a4"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("b4"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("c4"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("d4"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("f4"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("g4"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("h4"))));
    }

    @Test
    public void testLegalMoveAllAvailable() {

        final Builder boardBuilder = new Builder();
        // Black Layout
        boardBuilder.setFigure(new King(4, Alliance.black, false));
        boardBuilder.setFigure(new Knight(28, Alliance.black));
        // White Layout
        boardBuilder.setFigure(new Knight(36, Alliance.white));
        boardBuilder.setFigure(new King(60, Alliance.white, false));
        // Set the current player
        boardBuilder.setNextMoveMaker(Alliance.white);
        final Board board = boardBuilder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getPlayerMoves();
        assertEquals(whiteLegals.size(), 13);
        final Move wm1 = MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("d6"));
        final Move wm2 = MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("f6"));
        final Move wm3 = MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("c5"));
        final Move wm4 = MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("g5"));
        final Move wm5 = MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("c3"));
        final Move wm6 = MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("g3"));
        final Move wm7 = MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("d2"));
        final Move wm8 = MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("f2"));

        assertTrue(whiteLegals.contains(wm1));
        assertTrue(whiteLegals.contains(wm2));
        assertTrue(whiteLegals.contains(wm3));
        assertTrue(whiteLegals.contains(wm4));
        assertTrue(whiteLegals.contains(wm5));
        assertTrue(whiteLegals.contains(wm6));
        assertTrue(whiteLegals.contains(wm7));
        assertTrue(whiteLegals.contains(wm8));

        final Builder boardBuilder2 = new Builder();
        // Black Layout
        boardBuilder2.setFigure(new King(4, Alliance.black, false));
        boardBuilder2.setFigure(new Knight(28, Alliance.black));
        // White Layout
        boardBuilder2.setFigure(new Knight(36, Alliance.white));
        boardBuilder2.setFigure(new King(60, Alliance.white, false));
        // Set the current player
        boardBuilder2.setNextMoveMaker(Alliance.black);
        final Board board2 = boardBuilder2.build();
        final Collection<Move> blackLegals = board.blackPlayer().getPlayerMoves();

        final Move bm1 = MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("d7"));
        final Move bm2 = MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("f7"));
        final Move bm3 = MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("c6"));
        final Move bm4 = MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("g6"));
        final Move bm5 = MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("c4"));
        final Move bm6 = MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("g4"));
        final Move bm7 = MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("d3"));
        final Move bm8 = MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("f3"));

        assertEquals(blackLegals.size(), 13);

        assertFalse(blackLegals.contains(bm1));
        assertFalse(blackLegals.contains(bm2));
        assertFalse(blackLegals.contains(bm3));
        assertFalse(blackLegals.contains(bm4));
        assertFalse(blackLegals.contains(bm5));
        assertFalse(blackLegals.contains(bm6));
        assertFalse(blackLegals.contains(bm7));
        assertFalse(blackLegals.contains(bm8));
    }

    @Test
    public void testKingEquality() {
        final Board board = createStandardBoard();
        final Board board2 = createStandardBoard();
        assertNotEquals(board.getField(60), board2.getField(60));
        assertFalse(board.getField(60).equals(null));
    }

    @Test
    public void testFoolsMate() {

        final Board board = createStandardBoard();
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("f2"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getTransitionBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t1.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getTransitionBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t2.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("g2"),
                        BoardUtils.getCoordinateAtPosition("g4")));

        assertTrue(t3.getMoveStatus().isDone());

        final MoveTransition t4 = t3.getTransitionBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t3.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d8"),
                        BoardUtils.getCoordinateAtPosition("h4")));

        assertTrue(t4.getMoveStatus().isDone());

        assertTrue(t4.getTransitionBoard().currentPlayer().isInCheckMate());

    }

}