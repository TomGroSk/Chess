package com.engine.Board;


import com.engine.Pawns.Pawn;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public abstract class Field {
    protected final int coordinate;
    private static final Map<Integer, EmptyField> EMPTY_FIELDS = createAllEmptyFields();

    private static Map<Integer, EmptyField> createAllEmptyFields() {
        final Map<Integer, EmptyField> emptyFieldMap = new HashMap<>();

        for (int i=0;i<64;i++){
            emptyFieldMap.put(i, new EmptyField(i));
        }
        return ImmutableMap.copyOf(emptyFieldMap);
    }

    private Field(int coordinate){
        this.coordinate = coordinate;
    }

    public static Field createField(final int coordinate, Pawn pawn){
        return pawn != null ? new Field.OccupiedField(coordinate, pawn) : EMPTY_FIELDS.get(coordinate);
    }

    public abstract boolean isEmpty();
    public abstract Pawn getPawn();





    public static final class EmptyField extends Field{
        private EmptyField(int coordinate) {
            super(coordinate);
        }
        @Override
        public boolean isEmpty(){
            return true;
        }
        @Override
        public Pawn getPawn(){
            return null;
        }
    }


    public static final class OccupiedField extends Field{
        private final Pawn pawn;
        private OccupiedField(int coordinate, Pawn pawn){
            super(coordinate);
            this.pawn=pawn;
        }
        @Override
        public boolean isEmpty(){
            return false;
        }
        @Override
        public Pawn getPawn(){
            return this.pawn;
        }
    }
}
