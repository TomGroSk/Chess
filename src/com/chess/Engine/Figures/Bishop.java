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

public class Bishop extends Figure {
    private final static int [] possiblyMoveTable = {-9, -7, 7, 9};
    public Bishop(final int position, final Alliance alliance) {
        super(position, alliance,FigureType.BISHOP, true);
    }
    public Bishop(final int position, final Alliance alliance, final boolean isFirstMove) {
        super(position, alliance,FigureType.BISHOP, isFirstMove);
    }

    @Override
    public Bishop moveFigure(Move move) {
        return new Bishop(move.getDestinationCoordinate(), move.getMovedFigure().getAlliance());
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
                        possibleMoves.add(new MajorMove(board, this, temp));
                    }
                    else{
                        final Figure figureAtDestination = possiblyDestinationField.getFigure();
                        final Alliance figureAlliance = figureAtDestination.getAlliance();
                        if(this.alliance != figureAlliance){
                            possibleMoves.add(new MajorAttackMove(board, this, figureAtDestination, temp));
                        }
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(possibleMoves);
    }
    @Override
    public String toString(){
        return FigureType.BISHOP.toString();
    }
    private static boolean isAtFirstColumn(final int currentPosition, final int candidateMove){
        return BoardUtils.firstColumn[currentPosition] && ((candidateMove == -9) || (candidateMove == 7));
    }
    private static boolean isAtEighthColumn(final int currentPosition, final int candidateMove){
        return BoardUtils.eighthColumn[currentPosition] && ((candidateMove == -7) || (candidateMove == 9));
    }
}
