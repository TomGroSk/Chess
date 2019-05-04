package com.chess.Engine.Figures;

import com.chess.Engine.Alliance;
import com.chess.Engine.Board.Board;
import com.chess.Engine.Move.Move;
import com.chess.Engine.Move.Move.*;
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
            final int currentDestinationCoordinate = this.position+(this.alliance.getDirection() * currentPossibleMove);
            if(!Board.BoardUtils.isValidCoordinate(currentDestinationCoordinate)){
                continue;
            }
            if(currentPossibleMove == 8 && board.getField(currentDestinationCoordinate).isEmpty()){

                if(this.alliance.isPawnPromotionSquare(currentDestinationCoordinate)){
                    possibleMoves.add(new PawnPromotion(new PawnMove(board,
                                            this,
                                                        currentDestinationCoordinate)));
                }
                else{
                    possibleMoves.add(new PawnMove(board, this, currentDestinationCoordinate));
                }
            }
            else if(currentPossibleMove == 16 && this.isFirstMove() &&
                    ((Board.BoardUtils.secondRow[this.position] && this.getAlliance().isBlack()) ||
                    (Board.BoardUtils.seventhRow[this.position] && !this.getAlliance().isBlack()))){
                final int behindTemp = this.position + (this.getAlliance().getDirection() * 8);
                if(board.getField(behindTemp).isEmpty() && board.getField(currentDestinationCoordinate).isEmpty()){
                    possibleMoves.add(new PawnJump(board, this, currentDestinationCoordinate));
                }
            }
            else if(currentPossibleMove == 7 &&
                    !((Board.BoardUtils.eighthColumn[this.position] && !this.alliance.isBlack()) ||
                    (Board.BoardUtils.firstColumn[this.position] && this.alliance.isBlack()))){
                if(!board.getField(currentDestinationCoordinate).isEmpty()){
                    Figure figureOnCandidate = board.getField(currentDestinationCoordinate).getFigure();
                    if(this.alliance != figureOnCandidate.alliance){
                        if(this.alliance.isPawnPromotionSquare(currentDestinationCoordinate)){
                            possibleMoves.add(new PawnPromotion(new PawnAttackMove(board,
                                                    this,
                                                                figureOnCandidate,
                                                                currentDestinationCoordinate)));
                        }
                        else {
                            possibleMoves.add(new PawnAttackMove(board,
                                                    this,
                                                                figureOnCandidate,
                                                                currentDestinationCoordinate));
                        }
                    }
                }
                else if(board.getEnPassantPawn() != null){
                    if(board.getEnPassantPawn().getPosition() == (this.position + (this.alliance.getOppositeDirection()))){
                        final Figure figureOnCandidate = board.getEnPassantPawn();
                        if(this.alliance != figureOnCandidate.alliance){
                            possibleMoves.add(new PawnEnPassantAttackMove(board,
                                                              this,
                                                                          figureOnCandidate,
                                                                          currentDestinationCoordinate));
                        }
                    }
                }
            }
            else if(currentPossibleMove == 9 &&
                    !((Board.BoardUtils.eighthColumn[this.position] && this.alliance.isBlack()) ||
                      (Board.BoardUtils.firstColumn[this.position] && !this.alliance.isBlack()))){
                if(!board.getField(currentDestinationCoordinate).isEmpty()){
                    Figure figureOnCandidate = board.getField(currentDestinationCoordinate).getFigure();
                    if(this.alliance != figureOnCandidate.alliance){
                        if(this.alliance.isPawnPromotionSquare(currentDestinationCoordinate)){
                            possibleMoves.add(new PawnPromotion(new PawnAttackMove(board,
                                                                        this,
                                                                                    figureOnCandidate,
                                                                                    currentDestinationCoordinate)));
                        }
                        else {
                        possibleMoves.add(new PawnAttackMove(board,
                                                 this,
                                                             figureOnCandidate,
                                                             currentDestinationCoordinate));
                        }
                    }
                }
                else if(board.getEnPassantPawn() != null){
                    if(board.getEnPassantPawn().getPosition() == (this.position - (this.alliance.getOppositeDirection()))){
                        final Figure figureOnCandidate = board.getEnPassantPawn();
                        if(this.alliance != figureOnCandidate.alliance){
                            possibleMoves.add(new PawnEnPassantAttackMove(board,
                                                              this,
                                                                          figureOnCandidate,
                                                                          currentDestinationCoordinate));
                        }
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

    public Figure getPromotionFigure(){
        return new Queen(this.position, this.alliance, false);
    }
}
