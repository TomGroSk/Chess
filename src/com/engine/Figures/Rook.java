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

public class Rook extends Figure {

    private final static int [] possiblyMoveTable = {-8, -1, 1, 8};
    Rook(int position, Alliance alliance) {
        super(position, alliance);
    }

    @Override
    public Collection<Move> calculatePossibleMoves(Board board) {
        final List<Move> possibleMoves = new ArrayList<>();
        int temp;
        for(final int currentPossibleMove:possiblyMoveTable){
            temp = this.position;
            while (BoardUtils.isValidCoordinate(temp)){
                if(isAtFirstColumn(this.position, currentPossibleMove) ||
                        isAtEighthColumn(this.position, currentPossibleMove)){

                    break;
                }
                temp +=currentPossibleMove;
                if(BoardUtils.isValidCoordinate(temp)){
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
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(possibleMoves);
    }
    private static boolean isAtFirstColumn(final int currentPosition, final int candidateMove){
        return BoardUtils.firstColumn[currentPosition] && (candidateMove == -1);
    }
    private static boolean isAtEighthColumn(final int currentPosition, final int candidateMove){
        return BoardUtils.eighthColumn[currentPosition] && (candidateMove == 1);
    }
}