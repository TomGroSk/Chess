package com.chess.Engine.Figures;

import com.chess.Engine.Alliance;
import com.chess.Engine.Board.BoardUtils;
import com.chess.Engine.Board.Field;
import com.chess.Engine.Board.Board;
import com.chess.Engine.Board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Knight extends Figure {
    private static int [] possiblyMoveTable = {-17,-15,-10,-6,6,10,15,17};

    public Knight(final int position, final Alliance alliance) {
        super(position, alliance,FigureType.KNIGHT, true);
    }
    public Knight(final int position, final Alliance alliance, final boolean isFirstMove) {
        super(position, alliance,FigureType.KNIGHT, isFirstMove);
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
    @Override
    public String toString(){
        return FigureType.KNIGHT.toString();
    }
    @Override
    public Knight moveFigure(Move move) {
        return new Knight(move.getDestinationCoordinate(), move.getMovedFigure().getAlliance());
    }
}
