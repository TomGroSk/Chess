package com.chess.Engine.AI;

import com.chess.Engine.Board.Board;

public interface BoardEvaluator {
    int evaluate(Board board, int depth);
}
