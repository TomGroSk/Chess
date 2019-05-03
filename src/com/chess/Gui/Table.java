package com.chess.Gui;

import com.chess.Engine.Board.Board;
import com.chess.Engine.Board.BoardUtils;
import com.chess.Engine.Board.Field;
import com.chess.Engine.Board.Move;
import com.chess.Engine.Figures.Figure;
import com.chess.Engine.Player.MoveTransition;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {

    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board chessBoard;
    private Field sourceField;
    private Field destinationField;
    private Figure humanMovedFigure;
    private BoardDirection boardDirection;

    private boolean highlightLegalMoves;

    private static String pieceIconPath = "asset/art/";


    private Color lightTileColor = Color.decode("#FFFACD");
    private Color darkTileColor = Color.decode("#593E1A");

    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private final static Dimension FIELD_PANEL_DIMENSION = new Dimension(10,10);

    public Table(){
        this.gameFrame = new JFrame("Chess");
        this.gameFrame.setLayout(new BorderLayout());
        this.chessBoard = Board.createStandardBoard();
        final JMenuBar tableMenuBar = populateMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.boardPanel = new BoardPanel();
        this.highlightLegalMoves = true;
        this.boardDirection = BoardDirection.NORMAL;
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);

        this.gameFrame.setVisible(true);
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private JMenuBar populateMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        return tableMenuBar;
    }


    private JMenu createPreferencesMenu(){
        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip board");
        final JMenu preferencesMenu = new JMenu("Preferences");
        flipBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
            }
        });
        preferencesMenu.add(flipBoardMenuItem);

        preferencesMenu.addSeparator();
        final JCheckBoxMenuItem legalMoveHighlightsCheckBox = new JCheckBoxMenuItem("Highlight Legal Moves", true);
        legalMoveHighlightsCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightLegalMoves = legalMoveHighlightsCheckBox.isSelected();
            }
        });
        preferencesMenu.add(legalMoveHighlightsCheckBox);

        return preferencesMenu;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");

        final JMenuItem openPGN = new JMenuItem("Load PGN file");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open up pgn file");
            }
        });
        fileMenu.add(openPGN);

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }

    private class BoardPanel extends JPanel{
        final List<FieldPanel> boardFields;
        BoardPanel(){
            super(new GridLayout(8,8));
            this.boardFields = new ArrayList<>();
            for(int i=0 ;i< BoardUtils.numFields ;i++){
                final FieldPanel fieldPanel = new FieldPanel(this, i);
                this.boardFields.add(fieldPanel);
                add(fieldPanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }
        public void drawBoard(final Board board){
            removeAll();
            for(final FieldPanel fieldPanel: boardDirection.fReverse(boardFields)){
                fieldPanel.drawField(board);
                add(fieldPanel);
            }

            validate();
            repaint();
        }
    }

    public static class MoveLog {
        private List<Move> moves;
        MoveLog(){
            this.moves = new ArrayList<>();
        }
        public List<Move> getMoves() {
            return this.moves;
        }
        public int size(){
            return this.moves.size();
        }
        public void clear(){
            this.moves.clear();
        }
        public void addMove(final Move move){
            this.moves.add(move);
        }
        public boolean remove(final Move move){
            return this.moves.remove(move);
        }
        public Move remove(final int index){
            return this.moves.remove(index);
        }

    }


    private class FieldPanel extends JPanel{
        private final int fieldID;
        FieldPanel(final BoardPanel boardPanel, final int fieldID){
            super(new GridBagLayout());
            this.fieldID = fieldID;
            setPreferredSize(FIELD_PANEL_DIMENSION);
            assignFieldColor();
            assignFieldPieceIcon(chessBoard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(isRightMouseButton(e)){
                        sourceField = null;
                        destinationField = null;
                        humanMovedFigure = null;
                    }
                    else if(isLeftMouseButton(e)){
                        if(sourceField == null){
                            sourceField = chessBoard.getField(fieldID);
                            humanMovedFigure = sourceField.getFigure();
                            if(humanMovedFigure==null){
                                sourceField = null;
                            }
                        }
                        else{
                            destinationField = chessBoard.getField(fieldID);
                            final Move move = Move.MoveFactory.createMove(chessBoard,
                                    sourceField.getFieldCoordinate(),
                                    destinationField.getFieldCoordinate());
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            if(transition.getMoveStatus().isDone()){
                                chessBoard = transition.getTransitionBoard();
                            }
                            sourceField = null;
                            destinationField = null;
                            humanMovedFigure = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            validate();
        }

        private void assignFieldPieceIcon(final Board board) {
            this.removeAll();
            if(!board.getField(this.fieldID).isEmpty()) {
                try{
                    final BufferedImage image = ImageIO.read(new File(pieceIconPath +
                            board.getField(this.fieldID).getFigure().getAlliance().toString().substring(0, 1) +
                            board.getField(this.fieldID).toString() +
                            ".gif"));
                    add(new JLabel(new ImageIcon(image)));
                } catch(final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void assignFieldColor() {
            if (BoardUtils.firstRow[this.fieldID] ||
                    BoardUtils.thirdRow[this.fieldID] ||
                    BoardUtils.fifthRow[this.fieldID] ||
                    BoardUtils.seventhRow[this.fieldID]) {
                setBackground(this.fieldID % 2 == 0 ? lightTileColor : darkTileColor);
            } else if(BoardUtils.secondRow[this.fieldID] ||
                    BoardUtils.fourthRow[this.fieldID] ||
                    BoardUtils.sixthRow[this.fieldID]  ||
                    BoardUtils.eighthRow[this.fieldID]) {
                setBackground(this.fieldID % 2 != 0 ? lightTileColor : darkTileColor);
            }
        }

        private void highlightsLegals(final Board board){
            if(highlightLegalMoves){
                for (final Move move: figureLegals(board)){
                    if(move.getDestinationCoordinate() == this.fieldID){
                        try{
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("asset/art/legals/green_dot.png")))));
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }
            }

        }
        private Collection<Move> figureLegals(final Board board){
            if(humanMovedFigure != null && humanMovedFigure.getAlliance() == board.currentPlayer().getAlliance()){
                return humanMovedFigure.calculatePossibleMoves(board);
            }
            return Collections.emptyList();
        }

        public void drawField(final Board board){
            assignFieldColor();
            assignFieldPieceIcon(board);
            highlightsLegals(board);
            validate();
            repaint();
        }
    }
    public enum BoardDirection {
        NORMAL{
            @Override
            List<FieldPanel> fReverse(List<FieldPanel> boardFields) {
                return boardFields;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED{
            @Override
            List<FieldPanel> fReverse(List<FieldPanel> boardFields) {
                return Lists.reverse(boardFields);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };
        abstract List<FieldPanel> fReverse(final List<FieldPanel> boardFields);
        abstract BoardDirection opposite();
    }
}
