package com.chess;

import com.chess.Engine.Board.Board;
import com.chess.Gui.Table;

public class JChess {

    public static void main(String[] args){
        Board board = Board.createStandardBoard();
        System.out.println(board);

        Table table = new Table();
    }
}
