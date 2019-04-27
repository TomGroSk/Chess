package com.engine.Pawns;

public abstract class Pawn {
    int coordinateX, coordinateY;

    Pawn(int coordinateX, int coordinateY) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }
    public abstract boolean isEmpty();
    public abstract Pawn getPawn();

    public static final class EmptyPawn extends Pawn{
        EmptyPawn(int coordinateX, int coordinateY) {
            super(coordinateX, coordinateY);
        }
        public boolean isEmpty(){
            return true;
        }
        public Pawn getPawn(){
            return null;
        }
    }

    public static final class OccupiedPawn extends Pawn{
        Pawn pawn;
        OccupiedPawn(int coordinateX, int coordinateY, Pawn pawn){
            super(coordinateX, coordinateY);
            this.pawn=pawn;
        }
        public boolean isEmpty(){
            return false;
        }
        public Pawn getPawn(){
            return this.pawn;
        }
    }
}

