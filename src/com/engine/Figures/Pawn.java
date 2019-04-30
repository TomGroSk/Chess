package com.engine.Figures;

import com.engine.Alliance;
import com.engine.Board.Board;
import com.engine.Board.BoardUtils;
import com.engine.Board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Figure{

    private final static int [] possiblyMoveTable = {7, 8, 9, 16};

    Pawn(int position, Alliance alliance) {
        super(position, alliance);
    }

    @Override
    public Collection<Move> calculatePossibleMoves(Board board) {
        final List<Move> possibleMoves = new ArrayList<>();
        for(final int currentPossibleMove:possiblyMoveTable){
            final int temp = this.position+(this.alliance.getDirection() * currentPossibleMove);
            if(!BoardUtils.isValidCoordinate(temp)){
                continue;
            }
            if(currentPossibleMove == 8 && board.getField(temp).isEmpty()){
                possibleMoves.add(new Move.MajorMove(board, this, temp));
            }
            else if(currentPossibleMove == 16 && this.isFirstMove() &&
                    (BoardUtils.secondRow[this.position] && this.getAlliance().isBlack()) ||
                    (BoardUtils.seventhRow[this.position] && !this.getAlliance().isBlack())){
                final int behindTemp = this.position+ (this.getAlliance().getDirection() * 8);
                if(board.getField(behindTemp).isEmpty() && board.getField(temp).isEmpty()){
                    possibleMoves.add(new Move.MajorMove(board, this, temp));
                }
            }
            else if(currentPossibleMove == 7 &&
                    !((BoardUtils.eighthColumn[this.position] && !this.alliance.isBlack()) ||
                    (BoardUtils.firstColumn[this.position] && this.alliance.isBlack()))){
                if(!board.getField(temp).isEmpty()){
                    Figure figure = board.getField(temp).getFigure();
                    if(this.alliance != figure.alliance){
                        possibleMoves.add(new Move.MajorMove(board, this, temp));
                    }
                }
            }
            else if(currentPossibleMove == 9 &&
                    !((BoardUtils.eighthColumn[this.position] && this.alliance.isBlack()) ||
                      (BoardUtils.firstColumn[this.position] && !this.alliance.isBlack()))){
                if(!board.getField(temp).isEmpty()){
                    Figure figure = board.getField(temp).getFigure();
                    if(this.alliance != figure.alliance){
                        possibleMoves.add(new Move.MajorMove(board, this, temp));
                    }
                }
            }
        }
        return ImmutableList.copyOf(possibleMoves);
    }
}
