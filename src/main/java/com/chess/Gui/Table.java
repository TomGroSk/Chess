package com.chess.Gui;

import com.chess.Engine.AI.MiniMax;
import com.chess.Engine.AI.MoveStrategy;
import com.chess.Engine.Board.Board;
import com.chess.Engine.Board.Field;
import com.chess.Engine.Move.Move;
import com.chess.Engine.Figures.Figure;
import com.chess.Engine.Move.MoveTransition;
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

import static com.chess.Engine.Move.Move.MoveFactory.createMove;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table extends Observable {

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    private final BoardPanel boardPanel;
    private Board chessBoard;
    private Field sourceField;
    private Field destinationField;
    private Figure humanMovedFigure;
    private BoardDirection boardDirection;
    private Move computerMove;
    private final GameSetup gameSetup;
    private JFrame gameFrame;
    private boolean highlightLegalMoves;



    private Color lightTileColor = Color.decode("#FFFFFF");
    private Color darkTileColor = Color.decode("#8b4513");

    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private final static Dimension FIELD_PANEL_DIMENSION = new Dimension(10,10);

    private static final Table instance = new Table();

    private Table(){
        this.gameFrame = new JFrame("Chess");
        this.gameFrame.setLayout(new BorderLayout());
        this.chessBoard = Board.createStandardBoard();
        final JMenuBar tableMenuBar = populateMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.boardPanel = new BoardPanel();
        this.highlightLegalMoves = true;
        this.addObserver(new TableGameAIWatcher());
        this.gameSetup = new GameSetup(this.gameFrame, true);
        this.boardDirection = BoardDirection.NORMAL;
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);

        this.gameFrame.setVisible(true);
        this.gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static Table get(){
        return instance;
    }
    private Board getGameBoard(){
        return this.chessBoard;
    }
    private JMenuBar populateMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        tableMenuBar.add(createOptionsMenu());
        return tableMenuBar;
    }


    private JMenu createPreferencesMenu(){
        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip board");
        final JMenu preferencesMenu = new JMenu("Preferences");
        flipBoardMenuItem.addActionListener(e -> {
            boardDirection = boardDirection.opposite();
            boardPanel.drawBoard(chessBoard);
        });
        preferencesMenu.add(flipBoardMenuItem);

        preferencesMenu.addSeparator();
        final JCheckBoxMenuItem legalMoveHighlightsCheckBox = new JCheckBoxMenuItem("Highlight Legal Moves", true);
        legalMoveHighlightsCheckBox.addActionListener(
                e -> highlightLegalMoves = legalMoveHighlightsCheckBox.isSelected()
        );
        preferencesMenu.add(legalMoveHighlightsCheckBox);

        return preferencesMenu;
    }
    private JMenu createOptionsMenu(){
        final JMenu optionsMenu = new JMenu("Options");
        final JMenuItem setupGameMenuItem = new JMenuItem("Setup Game");
        setupGameMenuItem.addActionListener(e -> {
            Table.get().getGameSetup().promptUser();
            Table.get().setupUpdate(Table.get().getGameSetup());
        });
        optionsMenu.add(setupGameMenuItem);
        return optionsMenu;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("New");

//        final JMenuItem openPGN = new JMenuItem("Load PGN file");
//        openPGN.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("Open up pgn file");
//            }
//        });
//        fileMenu.add(openPGN);

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(
                e -> System.exit(0)
        );
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }

    private class BoardPanel extends JPanel{
        final List<FieldPanel> boardFields;
        BoardPanel(){
            super(new GridLayout(8,8));
            this.boardFields = new ArrayList<>();
            for(int i = 0; i< Board.BoardUtils.numFields ; i++){
                final FieldPanel fieldPanel = new FieldPanel(this, i);
                this.boardFields.add(fieldPanel);
                add(fieldPanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }
        private void drawBoard(final Board board){
            removeAll();
            for(final FieldPanel fieldPanel: boardDirection.fReverse(boardFields)){
                fieldPanel.drawField(board);
                add(fieldPanel);
            }

            validate();
            repaint();
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
                            final Move move = createMove(chessBoard,
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
                        invokeLater(() ->{
                            if(gameSetup.isAIPlayer(chessBoard.currentPlayer())){
                                Table.get().moveMadeUpdate(PlayerType.human);
                            }
                            boardPanel.drawBoard(chessBoard);
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
                    String pieceIconPath = "asset/art/";
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
            if (Board.BoardUtils.firstRow[this.fieldID] ||
                    Board.BoardUtils.thirdRow[this.fieldID] ||
                    Board.BoardUtils.fifthRow[this.fieldID] ||
                    Board.BoardUtils.seventhRow[this.fieldID]) {
                setBackground(this.fieldID % 2 == 0 ? lightTileColor : darkTileColor);
            } else if(Board.BoardUtils.secondRow[this.fieldID] ||
                    Board.BoardUtils.fourthRow[this.fieldID] ||
                    Board.BoardUtils.sixthRow[this.fieldID]  ||
                    Board.BoardUtils.eighthRow[this.fieldID]) {
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


        private void drawField(final Board board){
            assignFieldColor();
            assignFieldPieceIcon(board);
            highlightsLegals(board);
            validate();
            repaint();
        }
    }

    public void show() {
        invokeLater(() -> Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard()));
    }

    private void setupUpdate(GameSetup gameSetup) {
        setChanged();
        notifyObservers(gameSetup);
    }

    private static class TableGameAIWatcher implements Observer{

        @Override
        public void update(Observable o, Object arg) {
            if(Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().currentPlayer()) &&
                !Table.get().getGameBoard().currentPlayer().isInCheckMate() &&
                    !Table.get().getGameBoard().currentPlayer().isInStaleMate()){

                final AIThinkTank thinkTank = new AIThinkTank();
                thinkTank.execute();
            }
            if(Table.get().getGameBoard().currentPlayer().isInCheckMate()){
                System.out.println("Check mate!");
            }
            if(Table.get().getGameBoard().currentPlayer().isInStaleMate()){
                System.out.println("Stale mate!");
            }
        }
    }

    private static class AIThinkTank extends SwingWorker<Move, String>{
        private AIThinkTank(){}
        @Override
        protected Move doInBackground() {
            final MoveStrategy miniMax = new MiniMax(3);
            final Move bestMove = miniMax.execute(Table.get().getGameBoard());
            return bestMove;
        }
        @Override
        public void done(){
            try {
                final Move bestMove = get();
                Table.get().updateComputerMove(bestMove);
                Table.get().updateGameBoard(Table.get().getGameBoard().currentPlayer().makeMove(bestMove).getTransitionBoard());
                Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
                Table.get().moveMadeUpdate(PlayerType.computer);
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void moveMadeUpdate(PlayerType playerType) {
        setChanged();
        notifyObservers(playerType);
    }

    private void updateComputerMove(Move bestMove) {
        this.computerMove = bestMove;
    }

    private void updateGameBoard(final Board board) {
        this.chessBoard = board;
    }

    private GameSetup getGameSetup() {
        return this.gameSetup;
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
    public enum PlayerType {
        computer,
        human
    }
}
