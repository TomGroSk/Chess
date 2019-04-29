package com.engine.Board;


import com.engine.Figures.Figure;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public abstract class Field {
    protected final int coordinate;
    private static final Map<Integer, EmptyField> EMPTY_FIELDS = createAllEmptyFields();

    private static Map<Integer, EmptyField> createAllEmptyFields() {
        final Map<Integer, EmptyField> emptyFieldMap = new HashMap<>();

        for (int i=0;i<BoardUtils.numFields;i++){
            emptyFieldMap.put(i, new EmptyField(i));
        }
        return ImmutableMap.copyOf(emptyFieldMap);
    }

    private Field(final int coordinate){
        this.coordinate = coordinate;
    }

    public static Field createField(final int coordinate, Figure figure){
        return figure != null ? new Field.OccupiedField(coordinate, figure) : EMPTY_FIELDS.get(coordinate);
    }

    public abstract boolean isEmpty();
    public abstract Figure getFigure();





    public static final class EmptyField extends Field{
        private EmptyField(int coordinate) {
            super(coordinate);
        }
        @Override
        public boolean isEmpty(){
            return true;
        }
        @Override
        public Figure getFigure(){
            return null;
        }
    }


    public static final class OccupiedField extends Field{
        private final Figure figure;
        private OccupiedField(int coordinate, Figure figure){
            super(coordinate);
            this.figure = figure;
        }
        @Override
        public boolean isEmpty(){
            return false;
        }
        @Override
        public Figure getFigure(){
            return this.figure;
        }
    }
}
