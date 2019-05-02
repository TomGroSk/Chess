package com.chess.Engine.Figures;

import com.chess.Engine.Alliance;
import com.chess.Engine.Board.Board;
import com.chess.Engine.Board.BoardUtils;
import com.chess.Engine.Board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Figure{

    private final static int [] possiblyMoveTable = {7, 8, 9, 16};

    public Pawn(final int position, final Alliance alliance) {
        super(position, alliance,FigureType.PAWN, true);
    }
    public Pawn(final int position, final Alliance alliance, final boolean isFirstMove) {
        super(position, alliance,FigureType.PAWN, isFirstMove);
    }
    @Override
    public Pawn moveFigure(Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedFigure().getAlliance());
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
                    ((BoardUtils.secondRow[this.position] && this.getAlliance().isBlack()) ||
                    (BoardUtils.seventhRow[this.position] && !this.getAlliance().isBlack()))){
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
    @Override
    public String toString(){
        return FigureType.PAWN.toString();
    }
}
