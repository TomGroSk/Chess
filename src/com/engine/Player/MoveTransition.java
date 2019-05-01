package com.engine.Player;

import com.engine.Board.Board;
import com.engine.Board.Move;

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
}
