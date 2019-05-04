package com.chess.Engine.Figures;

import com.chess.Engine.Alliance;
import com.chess.Engine.Board.Board;
import com.chess.Engine.Board.Field;
import com.chess.Engine.Move.Move;
import com.chess.Engine.Move.Move.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Rook extends Figure {

    private final static int [] possiblyMoveTable = {-8, -1, 1, 8};
    public Rook(final int position, final Alliance alliance) {
        super(position, alliance,FigureType.ROOK, true);
    }
    public Rook(final int position, final Alliance alliance, final boolean isFirstMove) {
        super(position, alliance,FigureType.ROOK, isFirstMove);
    }
    @Override
    public Rook moveFigure(Move move) {
        return new Rook(move.getDestinationCoordinate(), move.getMovedFigure().getAlliance());
    }
    @Override
    public Collection<Move> calculatePossibleMoves(Board board) {
        final List<Move> possibleMoves = new ArrayList<>();
        int temp ;
        for(final int currentPossibleMove:possiblyMoveTable){
            temp = this.position;
            while (Board.BoardUtils.isValidCoordinate(temp)){
                if(isAtFirstColumn(temp, currentPossibleMove) ||
                        isAtEighthColumn(temp, currentPossibleMove)){
                    break;
                }
                temp +=currentPossibleMove;
                if(Board.BoardUtils.isValidCoordinate(temp)){
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
    private static boolean isAtFirstColumn(final int currentPosition, final int candidateMove){
        return Board.BoardUtils.firstColumn[currentPosition] && (candidateMove == -1);
    }
    private static boolean isAtEighthColumn(final int currentPosition, final int candidateMove){
        return Board.BoardUtils.eighthColumn[currentPosition] && (candidateMove == 1);
    }
    @Override
    public String toString(){
        return FigureType.ROOK.toString();
    }
}
