package com.chess.Engine.Player;

import com.chess.Engine.Board.Board;
import com.chess.Engine.Board.Move;

public class MoveTransition {
    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus moveStatus;
    public MoveTransition(Board transitionBoard, Move move, MoveStatus moveStatus){
        this.move=move;
        this.moveStatus=moveStatus;
        this.transitionBoard=transitionBoard;
    }
    public MoveStatus getMoveStatus(){
        return this.moveStatus;
    }
    public Board getTransitionBoard(){
        return this.transitionBoard;
    }
}
