package com.chess.Engine.Move;

import com.chess.Engine.Board.Board;
import com.chess.Engine.Move.Move;

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

    public enum MoveStatus {
        DONE{
            @Override
            public boolean isDone() {
                return true;
            }
        },
        ILLEGAL_MOVE{
            @Override
            public boolean isDone() {
                return false;
            }
        },
        LEAVE_PLAYER_IN_CHECK{
            @Override
            public boolean isDone() {
                return false;
            }
        };
        public abstract boolean isDone();
    }
}
