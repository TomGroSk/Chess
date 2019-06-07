package com.chess.Engine;

import com.chess.Engine.Board.Board;
import com.chess.Engine.Player.BlackPlayer;
import com.chess.Engine.Player.Player;
import com.chess.Engine.Player.WhitePlayer;

public enum Alliance {
    white{
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public int getOppositeDirection() {
            return 1;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
            return whitePlayer;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return Board.BoardUtils.firstRow[position];
        }
    },
    black{
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public int getOppositeDirection() {
            return -1;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return Board.BoardUtils.eighthRow[position];
        }

        @Override
        public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
            return blackPlayer;
        }


    };
    public abstract int getDirection();
    public abstract int getOppositeDirection();
    public abstract boolean isBlack();
    public abstract boolean isPawnPromotionSquare(int position);

    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
