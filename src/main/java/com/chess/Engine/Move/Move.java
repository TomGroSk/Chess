package com.chess.Engine.Move;

import com.chess.Engine.Board.Board;
import com.chess.Engine.Board.Board.Builder;
import com.chess.Engine.Figures.Figure;
import com.chess.Engine.Figures.Pawn;
import com.chess.Engine.Figures.Rook;

public abstract class Move {
    final int destinationCoordinate;
    final Board board;
    final Figure movedFigure;
    final boolean isFirstMove;

    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board board, final Figure movedFigure, final int destinationCoordinate){
        this.destinationCoordinate = destinationCoordinate;
        this.board = board;
        this.movedFigure = movedFigure;
        this.isFirstMove = movedFigure.isFirstMove();
    }
    private Move(final Board board, final int destinationCoordinate){
        this.destinationCoordinate = destinationCoordinate;
        this.board = board;
        this.movedFigure = null;
        this.isFirstMove = false;
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedFigure.hashCode();
        result = prime * result + this.movedFigure.getPosition();
        result = result + (isFirstMove ? 1 : 0);
        return result;
    }
    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(other instanceof Move){
            return false;
        }
        final Move otherMove = (Move) other;
        return getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                getMovedFigure() == otherMove.getMovedFigure() &&
                getCurrentCoordinate() == otherMove.getCurrentCoordinate();
    }

    public int getCurrentCoordinate(){
        return this.getMovedFigure().getPosition();
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }
    public Figure getMovedFigure(){
        return this.movedFigure;
    }
    public boolean isAttack(){
        return false;
    }
    public boolean isCastlingMove(){
        return false;
    }
    public Figure getAttackedFigure(){
        return null;
    }
    public Board getBoard(){
        return this.board;
    };

    public Board execute(){
        final Builder builder = new Builder();
        for(final Figure figure: this.board.currentPlayer().getActiveFigures()){
            if(!this.movedFigure.equals(figure)){
                builder.setFigure(figure);
            }
        }
        for(final Figure figure: this.board.currentPlayer().getOpponent().getActiveFigures()){
            builder.setFigure(figure);
        }
        builder.setFigure(this.movedFigure.moveFigure(this));
        builder.setNextMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

        return builder.build();
    }

    public static class MajorMove extends Move{
        public MajorMove(final Board board, final Figure movedFigure, final int destinationCoordinate) {
            super(board, movedFigure, destinationCoordinate);
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MajorMove && super.equals(other);
        }

        @Override
        public String toString(){
            return  movedFigure.getFigureType().toString() +  Board.BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }
    
    public static class AttackMove extends Move{
        final Figure attackedFigure;
        public AttackMove(final Board board, final Figure movedFigure,
                          final Figure attackedFigure, final int destinationCoordinate) {
            super(board, movedFigure, destinationCoordinate);
            this.attackedFigure = attackedFigure;
        }
        @Override
        public boolean isAttack(){
            return true;
        }
        @Override
        public Figure getAttackedFigure(){
            return this.attackedFigure;
        }
        @Override
        public int hashCode(){
            return this.attackedFigure.hashCode() + super.hashCode();
        }
        @Override
        public boolean equals(Object other){
            if(this == other){
                return true;
            }
            if(other instanceof AttackMove){
                return false;
            }
            final AttackMove otherMove = (AttackMove) other;
            return super.equals(otherMove) && getAttackedFigure().equals(otherMove.getAttackedFigure());
        }
    }

    public static class MajorAttackMove extends AttackMove{

        public MajorAttackMove(Board board, Figure movedFigure,
                               Figure attackedFigure, int destinationCoordinate) {
            super(board, movedFigure, attackedFigure, destinationCoordinate);
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MajorAttackMove && super.equals(other);
        }
        @Override
        public String toString(){
            return movedFigure.getFigureType() + Board.BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static class PawnMove extends Move{
        public PawnMove(final Board board, final Figure movedFigure,
                        final int destinationCoordinate) {
            super(board, movedFigure, destinationCoordinate);
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnMove && super.equals(other);
        }
        @Override
        public String toString(){
            return Board.BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static class PawnPromotion extends Move{

        final Move decoratedMove;
        final Pawn promotedPawn;

        public PawnPromotion(final Move decoratedMove) {
            super(decoratedMove.getBoard(), decoratedMove.getMovedFigure(), decoratedMove.getDestinationCoordinate());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedFigure();
        }

        @Override
        public Board execute(){
            final Board pawnMovedBoard = this.decoratedMove.execute();
            final Builder builder = new Builder();
            for(final Figure figure : pawnMovedBoard.currentPlayer().getActiveFigures()){
                if(!(this.promotedPawn.equals(figure))){
                    builder.setFigure(figure);
                }
            }
            for(final Figure figure : pawnMovedBoard.currentPlayer().getOpponent().getActiveFigures()){
                    builder.setFigure(figure);
            }
            builder.setFigure(this.promotedPawn.getPromotionFigure().moveFigure(this));
            builder.setNextMoveMaker(pawnMovedBoard.currentPlayer().getAlliance());

            return builder.build();
        }

        @Override
        public boolean isAttack() {
            return this.decoratedMove.isAttack();
        }

        @Override
        public Figure getAttackedFigure() {
            return this.decoratedMove.getAttackedFigure();
        }

        @Override
        public int hashCode() {
            return decoratedMove.hashCode() + (31 * promotedPawn.hashCode());
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnPromotion && (super.equals(other));
        }

        @Override
        public String toString() {
            return "";
        }
    }

    public static class PawnAttackMove extends AttackMove{
        public PawnAttackMove(final Board board, final Figure movedFigure,
                              final Figure attackedFigure, final int destinationCoordinate) {
            super(board, movedFigure, attackedFigure, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnAttackMove && super.equals(other);
        }
        @Override
        public String toString(){
            return Board.BoardUtils.getPositionAtCoordinate(this.movedFigure.getPosition()).substring(0,1) +
                    "x" + Board.BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static class PawnEnPassantAttackMove extends AttackMove{
        public PawnEnPassantAttackMove(final Board board, final Figure movedFigure,
                              final Figure attackedFigure, final int destinationCoordinate) {
            super(board, movedFigure, attackedFigure, destinationCoordinate);
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnEnPassantAttackMove && super.equals(other);
        }
        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Figure figure: this.board.currentPlayer().getActiveFigures()){
                if(!this.movedFigure.equals(figure)){
                    builder.setFigure(figure);
                }
            }
            for(final Figure figure: this.board.currentPlayer().getOpponent().getActiveFigures()){
                if(!figure.equals(this.getAttackedFigure())){
                    builder.setFigure(figure);
                }
            }
            builder.setFigure(this.movedFigure.moveFigure(this));
            builder.setNextMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
        @Override
        public String toString(){
            return Board.BoardUtils.getPositionAtCoordinate(this.movedFigure.getPosition()).substring(0,1) +
                    "x" + Board.BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static class PawnJump extends Move{
        public PawnJump(final Board board, final Figure movedFigure,
                        final int destinationCoordinate) {
            super(board, movedFigure, destinationCoordinate);
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Figure figure: this.board.currentPlayer().getActiveFigures()){
                if(!this.movedFigure.equals(figure)){
                    builder.setFigure(figure);
                }
            }
            for(final Figure figure: this.board.currentPlayer().getOpponent().getActiveFigures()){
                builder.setFigure(figure);
            }
            final Pawn movedPawn = (Pawn)this.movedFigure.moveFigure(this);
            builder.setFigure(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setNextMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

            return builder.build();
        }
        @Override
        public String toString(){
            return Board.BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    static abstract class CastleMove extends Move{
        protected final Rook castleRook;
        protected final int castleRookStartPosition, castleRookDestinationPosition;

        public CastleMove(final Board board, final Figure movedFigure,
                          final int destinationCoordinate, final Rook castleRook,
                          final int castleRookStartPosition, final int castleRookDestinationPosition) {
            super(board, movedFigure, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStartPosition = castleRookStartPosition;
            this.castleRookDestinationPosition = castleRookDestinationPosition;
        }
        public Rook getCastleRook(){
            return this.castleRook;
        }
        @Override
        public boolean isCastlingMove(){
            return true;
        }
        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Figure figure: this.board.currentPlayer().getActiveFigures()){
                if(!this.movedFigure.equals(figure) && !this.castleRook.equals(figure)){
                    builder.setFigure(figure);
                }
            }
            for(final Figure figure: this.board.currentPlayer().getOpponent().getActiveFigures()){
                builder.setFigure(figure);
            }
            builder.setFigure(this.movedFigure.moveFigure(this));

            builder.setFigure(new Rook(this.castleRookDestinationPosition, this.castleRook.getAlliance()));
            builder.setNextMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
        @Override
        public int hashCode(){
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestinationPosition;
            return result;
        }
        @Override
        public boolean equals(final Object other){
            if(this == other){
                return true;
            }
            if(!(other instanceof CastleMove)){
                return false;
            }
            final CastleMove otherCastleMove = (CastleMove) other;
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }
    }

    public static class KingCastleMove extends CastleMove{

        public KingCastleMove(Board board, Figure movedFigure, int destinationCoordinate,
                              Rook castleRook, int castleRookStartPosition,
                              int castleRookDestinationPosition) {

            super(board, movedFigure, destinationCoordinate, castleRook,
                    castleRookStartPosition, castleRookDestinationPosition);

        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof KingCastleMove && super.equals(other);
        }

//        @Override
//        public String toString(){
//            return "O-O";
//        }
    }

    public static class QueenCastleMove extends CastleMove{

        public QueenCastleMove(Board board, Figure movedFigure, int destinationCoordinate,
                               Rook castleRook, int castleRookStartPosition,
                               int castleRookDestinationPosition) {

            super(board, movedFigure, destinationCoordinate, castleRook,
                    castleRookStartPosition, castleRookDestinationPosition);

        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof QueenCastleMove && super.equals(other);
        }

//        @Override
//        public String toString(){
//            return "O-O-O";
//        }
    }

    public static class NullMove extends Move{
        public NullMove() {
            super(null, 65 );
        }
        @Override
        public Board execute(){
            throw new RuntimeException("Cannot execute null!");
        }
        @Override
        public int getCurrentCoordinate(){
            return -1;
        }
    }

    public static class MoveFactory{
        private MoveFactory(){
            throw  new RuntimeException("This is not instatiable!");
        }
        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate){
            for(final Move move: board.getAllLegalMoves()){
                if(move.getCurrentCoordinate()==currentCoordinate &&
                   move.getDestinationCoordinate() == destinationCoordinate){
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }

}
