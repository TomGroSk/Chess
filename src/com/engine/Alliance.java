package com.engine;

public enum Alliance {
    white{
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public boolean isBlack() {
            return false;
        }
    },
    black{
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public boolean isBlack() {
            return true;
        }
    };
    public abstract int getDirection();

    public abstract boolean isBlack();
}
