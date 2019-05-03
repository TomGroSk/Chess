package com.tests.chess.Engine.Board;

import com.chess.Engine.Board.Board;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {
    @Test
    public void initialBoard() {

        final Board board = Board.createStandardBoard();


        assertEquals(board.currentPlayer().getPlayerMoves().size(), 20);
        assertEquals(board.currentPlayer().getOpponent().getPlayerMoves().size(), 20);
        assertFalse(board.currentPlayer().isInCheck());
        assertFalse(board.currentPlayer().isInCheckMate());
        assertFalse(board.currentPlayer().isCastled());


        assertEquals(board.currentPlayer(), board.whitePlayer());
        assertEquals(board.currentPlayer().getOpponent(), board.blackPlayer());
        assertFalse(board.currentPlayer().getOpponent().isInCheck());
        assertFalse(board.currentPlayer().getOpponent().isInCheckMate());
        assertFalse(board.currentPlayer().getOpponent().isCastled());
    }
}