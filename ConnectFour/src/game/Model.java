package game;

import interfaces.IModel;
import util.GameSettings;
import java.io.FileReader;
import java.io.IOException;


/**
 * This class represents the state of the game.
 * It has been partially implemented, but needs to be completed by you.
 *
 * @author <s1871633>
 */
public class Model implements IModel // DO NOT EDIT THIS LINE
{
    // A reference to the game settings from which you can retrieve the number
    // of rows and columns the board has and how long the win streak is.
    private GameSettings settings;
    public byte[][] board;
    private int numRows;
    private int numCols;

    private int streakLength;
    private int numOfTurns = 0;
    private byte gameStatus;

    // The default constructor.
    public Model() {
        // You probably won't need this.
    }

    // A constructor that takes another instance of the same type as its parameter.
    // This is called a copy constructor.
    public Model(IModel model) {
        // You may (or may not) find this useful for advanced tasks.
    }

    // Called when a new game is started on an empty board.
    public void initNewGame(GameSettings settings) {
        this.settings = settings;
        numRows = this.settings.nrRows;
        numCols = this.settings.nrCols;
        streakLength = this.settings.minStreakLength;
        board = new byte[numRows][numCols];
//		newGame = true;

        // This method still needs to be extended.
    }

    // Called when a game state should be loaded from the given file.
    public void initSavedGame(String fileName) {
        try {
            FileReader fileReader = new FileReader("saves/" + fileName);
            StringBuilder content = new StringBuilder();
            int i;
            while ((i = fileReader.read()) != -1) {
                content.append((char) i);
            }
            String[] contentList = content.toString().split("\n");
            int row = Integer.parseInt(contentList[0]);
            int col = Integer.parseInt(contentList[1]);
            int streak = Integer.parseInt(contentList[2]);
            byte activePlayer = Byte.parseByte(contentList[3]);

            // resumed game settings
            settings = new GameSettings(row,col,streak);
            numRows = settings.nrRows;
            numCols = settings.nrCols;
            streakLength = settings.minStreakLength;
            numOfTurns =  activePlayer - 1;
            board = new byte[numRows][numCols];

            for (int r = 0; r < numRows ; r++) {
                for (int c = 0; c < numCols; c++) {
                    byte piece = Byte.parseByte(Character.toString(contentList[4+r].toCharArray()[c]));
                    board[r][c] = piece;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Returns whether or not the passed in move is valid at this time.

    /**
     * , return false when column is full or move is outside range of cols else true
     *
     * @param move
     * @return
     */
    public boolean isMoveValid(int move) {
        if (move == -1 || ((move >= 0 && move <= numCols - 1) && getPieceIn(0, move) == 0)) {
            return true;
        }
        return false;
    }

    // Actions the given move if it is valid. Otherwise, does nothing.
    public void makeMove(int move) {
        if (move == -1) {
            gameStatus = getActivePlayer() == 1 ? IModel.GAME_STATUS_WIN_2 : IModel.GAME_STATUS_WIN_1;
            return;
        }

        int curr_pos = numRows - 1;
        for (int row = curr_pos; row >= 0; row--) {
            if (board[row][move] != 0) {
                curr_pos--;
            }
        }
        board[curr_pos][move] = getActivePlayer();
        numOfTurns++;

        if (hasWonVertical(curr_pos, move) || HasWonHorizontal(curr_pos, move) || hasWonDiagonal(curr_pos, move)) {
            gameStatus = getActivePlayer() != 1 ? IModel.GAME_STATUS_WIN_1 : IModel.GAME_STATUS_WIN_2;
            return;
        }

        if (isBoardFull()) {
            gameStatus = IModel.GAME_STATUS_TIE;
            return;
        }

        gameStatus = IModel.GAME_STATUS_ONGOING;
    }

    // Returns one of the following codes to indicate the game's current status.
    // IModel.java in the "interfaces" package defines constants you can use for this.
    // 0 = Game in progress
    // 1 = Player 1 has won
    // 2 = Player 2 has won
    // 3 = Tie (board is full and there is no winner)
    public byte getGameStatus() {
        return gameStatus;
    }

    // Returns the number of the player whose turn it is.
    public byte getActivePlayer() {
        return (byte) (numOfTurns % 2 == 0 ? 1 : 2);

    }

    // Returns the owner of the piece in the given row and column on the board.
    // Return 1 or 2 for players 1 and 2 respectively or 0 for empty cells.
    public byte getPieceIn(int row, int column) {
        return board[row][column];
    }

    // Returns a reference to the game settings, from which you can retrieve the
    // number of rows and columns the board has and how long the win streak is.
    public GameSettings getGameSettings() {
        return settings;
    }

    // =========================================================================
    // ================================ HELPERS ================================
    // =========================================================================

    // You may find it useful to define some helper methods here.

    //checks if a player has won vertically
    private boolean hasWonVertical(int curRow, int curCol) {
        int step = 0;
        for (int row = 0; row < numRows; row++) {
            if (getPieceIn(row, curCol) == getPieceIn(curRow, curCol)) {
                step++;
                if (step >= streakLength) {
                    return true;
                }
            } else {
                step = 0;
            }
        }
        return false;
    }

    // checks if a player has won horizontally
    private boolean HasWonHorizontal(int curRow, int curCol) {
        int step = 0;
        for (int col = 0; col < numCols; col++) {
            if (getPieceIn(curRow, col) == getPieceIn(curRow, curCol)) {
                step++;
                if (step >= streakLength) {
                    return true;
                }
            } else {
                step = 0;
            }
        }
        return false;
    }

    // checks if a player has won diagonally
    private boolean hasWonDiagonal(int curRow, int curCol){
        //  checking diagonals starting from top left to bottom right
        int step = 0;
        for (int row = 0; row <= numRows - streakLength; row++) {
            for (int col = 0; col <= numCols - streakLength;col++) {
                for (int i = 0; i < streakLength; i++) {
//                    int row_1 = row+i;
//                    int col_1 = col + i;
//                    System.out.println("row: " + row_1 + " col: " + col_1);
                    if (getPieceIn(row+i,col+i) == getPieceIn(curRow,curCol)){
                        step++;
                    }else {
                        step=0;
                    }
                }
                if (step >= streakLength) {
                    return true;
                }
            }
        }

        // checking diagonals starting from bottom left to top right
        step = 0;
        for (int row = streakLength-1; row < numRows; row++) {
            for (int col= 0; col <= numCols - streakLength; col++) {
                for (int i = 0; i < streakLength; i++) {
//                    int row_1 = row-i;
//                    int col_1 = col + i;
//                    System.out.println("row: " + row_1 + " col: " + col_1);
                    if (getPieceIn(row-i,col+i) == getPieceIn(curRow,curCol)){
                        step++;
                    }else {
                        step=0;
                    }
                }
                if (step >= streakLength) {
                    return true;
                }
            }
        }

        return false;
    }

    //checks if the board is full
    private boolean isBoardFull() {
        for (int col = 0; col < numCols; col++) {
            byte pieceIn = getPieceIn(0, col);
            if (pieceIn == 0) {
                return false;
            }
        }
        return true;
    }

}
