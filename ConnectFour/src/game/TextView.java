package game;

import interfaces.*;
import players.*;
import util.GameSettings;
import util.InputUtil;

/**
 * This class is used to interact with the user.
 * It has been partially implemented, but needs to be completed by you.
 *
 * @author <s1871633>
 */
public class TextView implements IView // DO NOT EDIT THIS LINE
{
	public void displayWelcomeMessage()
	{
		System.out.println("Welcome to Connect Four!");
	}
	
	public void displayChosenMove(int move)
	{
		System.out.println("Selected move: " + move);
	}
	
	public void displayMoveRejectedMessage(int move)
	{
		System.out.println("The move (" + move + ") was rejected, please try again.");
	}
	
	public void displayActivePlayer(byte playerID)
	{
		System.out.println("\nPlayer " + playerID + " is next!");
	}
	
	public void displayGameStatus(byte gameStatus)
	{
		System.out.print("\nGame status: ");
		
		switch(gameStatus)
		{
			case IModel.GAME_STATUS_ONGOING: System.out.println("The game is in progress."); break;
			case IModel.GAME_STATUS_WIN_1: System.out.println("Player 1 has won!"); break;
			case IModel.GAME_STATUS_WIN_2: System.out.println("Player 2 has won!"); break;
			case IModel.GAME_STATUS_TIE: System.out.println("The game has ended in a tie!"); break;
			default : System.out.println("Error: Invalid/unknown game status"); break;
		}
	}
	
	public void displayBoard(IModel model)
	{
		System.out.println("\n-------- BOARD --------");
		
		int nrRows = model.getGameSettings().nrRows;
		int nrCols = model.getGameSettings().nrCols;

		// Remove this and replace it with an actual representation of the board.
		String pos_value = "";
		for(int row=0;row<nrRows;row++) {
			for (int col = 0; col < nrCols; col++) {
				if (model.getPieceIn(row, col) == 1) {
					pos_value+="X ";
				} else if (model.getPieceIn(row, col) == 2) {
					pos_value+="O ";
				} else {
					pos_value+="_ ";
				}
			}
			pos_value+="\n";
		}
		System.out.print(pos_value);
		// Here is an example of how the output should look:
		//_ _ O O _ _ X
		//_ _ X O _ _ X
		//_ O X X _ _ O
		//_ X X O _ X O
		//X O O X O O O
		//X O X X X O X
	}
	
	public char requestMenuSelection()
	{
		// Display menu options.
		System.out.println("\n-------- MENU --------");
		System.out.println("(1) Start new game");
		System.out.println("(2) Resume saved game");
		System.out.println("(3) Change game settings");
		System.out.println("(4) Change players");
		
		// Request and return user input.
		System.out.print("Select an option and confirm with enter or use any other key to quit: ");
		return InputUtil.readCharFromUser();
	}
	
	public String requestSaveFileName()
	{
		System.out.println("\n-------- LOAD GAME --------");
		System.out.print("File name (e.g. Save.txt): ");
		return InputUtil.readStringFromUser();
	}
	
	public GameSettings requestGameSettings()
	{
		System.out.println("\n-------- GAME SETTINGS --------");
		
		// Replace these lines with code that asks the user to enter the values.

		System.out.println("Enter a board height (number of rows)");
		int nrRows = InputUtil.readIntFromUser();
		System.out.println("Enter a board width (number of columns)");
		int nrCols = InputUtil.readIntFromUser();
		System.out.println("Enter a streak Length");
		int streak = InputUtil.readIntFromUser();

		if (nrRows < IModel.MIN_ROWS || nrRows > IModel.MAX_ROWS ||
				nrCols < IModel.MIN_COLS || nrCols > IModel.MAX_COLS ||
				streak > nrRows || streak > nrCols || streak <= 0){
			System.out.println("Board height (number of rows) and board width (number of columns) should be \n" +
					"between 3 and 10 and streak length should not be less than or equal to zero or greater \n" +
					"than board width or height");
			return requestGameSettings();
	}

		// Wrap the selected settings in a GameSettings instance and return (leave this code here).
		return new GameSettings(nrRows, nrCols, streak);
	}
	
	public IPlayer requestPlayerSelection(byte playerId)
	{
		System.out.println("\n-------- CHOOSE PLAYER " + playerId + " --------");
		System.out.println("(1) HumanPlayer");
		System.out.println("(2) RoundRobinPlayer");
		System.out.println("(3) WinDetectingPlayer");
		System.out.println("(4) CompetitivePlayer");
		
		// Request user input.
		System.out.print("Select an option and confirm with enter (invalid input will select a HumanPlayer): ");
		char selectedPlayer = InputUtil.readCharFromUser();
		
		// Instantiate the selected player class.
		switch(selectedPlayer)
		{
			case '2': return new RoundRobinPlayer();
			case '3': return new WinDetectingPlayer();
			case '4': return new CompetitivePlayer();
			default: return new HumanPlayer();
		}
	}
}
