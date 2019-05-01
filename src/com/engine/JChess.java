package com.engine;

import com.engine.Board.Board;

public class JChess {

    public static void main(String[] args){
        Board board = Board.createStandardBoard();
        System.out.println(board);
    }
}
