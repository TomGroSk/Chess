package com.engine.Figures;

import com.engine.Alliance;
import com.engine.Board.Board;
import com.engine.Board.BoardUtils;
import com.engine.Board.Field;
import com.engine.Board.Move;
import com.engine.Board.Move.AttackMove;
import com.engine.Board.Move.MajorMove;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class King extends Figure {

    private final static int [] possiblyMoveTable = {-9, -8, -7, -1, 1, 7, 8, 9};

    public King(int position, Alliance alliance) {
        super(position, alliance, FigureType.KING);
    }

    @Override
    public Collection<Move> calculatePossibleMoves(Board board) {
        final List<Move> possibleMoves = new ArrayList<>();

        for(final int currentPossibleMove:possiblyMoveTable){
            final int temp = this.position+currentPossibleMove;

            if(isAtFirstColumn(this.position, currentPossibleMove) ||
                    isAtEighthColumn(this.position, currentPossibleMove)){
                continue;
            }
            if(BoardUtils.isValidCoordinate(temp)){
                final Field possiblyDestinationField = board.getField(temp);
                if(possiblyDestinationField.isEmpty()){
                    possibleMoves.add(new MajorMove(board, this, temp));
                }
                else{
                    final Figure figureAtDestination = possiblyDestinationField.getFigure();
                    final Alliance figureAlliance = figureAtDestination.getAlliance();
                    if(this.alliance != figureAlliance){
                        possibleMoves.add(new AttackMove(board, this, figureAtDestination, temp));
                    }
                }
            }
        }
        return ImmutableList.copyOf(possibleMoves);
    }
    @Override
    public String toString(){
        return FigureType.KING.toString();
    }
    private static boolean isAtFirstColumn(final int currentPosition, final int candidateMove){
        return BoardUtils.firstColumn[currentPosition] && ((candidateMove==-9) || (candidateMove==-1) ||
                (candidateMove==7));
    }

    private static boolean isAtEighthColumn(final int currentPosition, final int candidateMove){
        return BoardUtils.secondColumn[currentPosition] && ((candidateMove==-7) || (candidateMove==1) ||
                (candidateMove==9));
    }
}
