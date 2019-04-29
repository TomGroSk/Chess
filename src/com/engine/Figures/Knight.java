package com.engine.Figures;

import com.engine.Alliance;
import com.engine.Board.Board;
import com.engine.Board.BoardUtils;
import com.engine.Board.Field;
import com.engine.Board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Knight extends Figure {
    private static int [] possiblyMoveTable = {-17,-15,-10,-6,6,10,15,17};

    Knight(final int position, final Alliance alliance) {
        super(position, alliance);
    }

    @Override
    public Collection<Move> calculatePossibleMoves(final Board board) {
        int temp;
        final List<Move> possibleMoves = new ArrayList<>();

        for(final int currentPossibleMove: possiblyMoveTable){
            temp = this.position + currentPossibleMove;
            if(BoardUtils.isValidCoordinate(temp)){
                if(isAtFirstColumn(this.position, currentPossibleMove) ||
                        isAtSecondColumn(this.position, currentPossibleMove) ||
                        isAtSeventhColumn(this.position, currentPossibleMove) ||
                        isAtEighthColumn(this.position, currentPossibleMove)){

                    continue;
                }
                final Field possiblyDestinationField = board.getField(temp);
                if(possiblyDestinationField.isEmpty()){
                    possibleMoves.add(new Move.MajorMove(board, this, temp));
                }
                else{
                    final Figure figureAtDestination = possiblyDestinationField.getFigure();
                    final Alliance figureAlliance = figureAtDestination.getAlliance();
                    if(this.alliance != figureAlliance){
                        possibleMoves.add(new Move.AttackMove(board, this, figureAtDestination, temp));
                    }
                }
            }
        }
        return ImmutableList.copyOf(possibleMoves);
    }
    private static boolean isAtFirstColumn(final int currentPosition, final int candidateMove){
        return BoardUtils.firstColumn[currentPosition] && ((candidateMove==-17) || (candidateMove==-10) ||
                (candidateMove==6) ||(candidateMove==15));
    }
    private static boolean isAtSecondColumn(final int currentPosition, final int candidateMove){
        return BoardUtils.secondColumn[currentPosition] && ((candidateMove==-10) || (candidateMove==6));
    }
    private static boolean isAtSeventhColumn(final int currentPosition, final int candidateMove){
        return BoardUtils.seventhColumn[currentPosition] && ((candidateMove==-6) || (candidateMove==10));
    }
    private static boolean isAtEighthColumn(final int currentPosition, final int candidateMove){
        return BoardUtils.eighthColumn[currentPosition] && ((candidateMove==-15) || (candidateMove==-6) ||
                (candidateMove==10) ||(candidateMove==17));
    }
}
