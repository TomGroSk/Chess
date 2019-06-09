package com.chess.Engine.AI;

import com.chess.Engine.Board.Board;
import com.chess.Engine.Move.Move;

public interface MoveStrategy {
    Move execute(Board board);
}
