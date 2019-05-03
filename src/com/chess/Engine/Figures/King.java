package com.chess.Engine.Figures;

import com.chess.Engine.Alliance;
import com.chess.Engine.Board.Board;
import com.chess.Engine.Board.BoardUtils;
import com.chess.Engine.Board.Field;
import com.chess.Engine.Board.Move;
import com.chess.Engine.Board.Move.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class King extends Figure {

    private final static int [] possiblyMoveTable = {-9, -8, -7, -1, 1, 7, 8, 9};

    public King(final int position, final Alliance alliance) {
        super(position, alliance,FigureType.KING, true);
    }
    public King(final int position, final Alliance alliance, final boolean isFirstMove) {
        super(position, alliance,FigureType.KING, isFirstMove);
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
                        possibleMoves.add(new MajorAttackMove(board, this, figureAtDestination, temp));
                    }
                }
            }
        }
        return ImmutableList.copyOf(possibleMoves);
    }

    @Override
    public King moveFigure(Move move) {
        return new King(move.getDestinationCoordinate(), move.getMovedFigure().getAlliance());
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
