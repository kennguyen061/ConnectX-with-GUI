package cpsc2150.connectX;

import java.util.ArrayList;
import java.util.List;

/**
 * The controller class will handle communication between our View and our Model (IGameBoard)
 *
 * This is where you will write code
 *
 * You will need to include your IGameBoard interface
 * and both of the IGameBoard implementations from Homework 3
 * If your code was correct you will not need to make any changes to your IGameBoard implementation class
 */

public class ConnectXController {
    //our current game that is being played
    private IGameBoard curGame;


    //The screen that provides our view
    private ConnectXView screen;



    public static final int MAX_PLAYERS = 10;
    //our play tokens are hard coded. We could make a screen to get those from the user, but


    int numPlayers;

    //List of characters for the player
    private List<Character> playerList;
    //The active player
    private int currentPlayer = -1;
    //Next two variables are for starting a new game
    private Boolean newGametimeWin = false;
    private Boolean newGametimeTie = false;


    /**
     *
     * @param model the board implementation
     * @param view the screen that is shown
     * @post the controller will respond to actions on the view using the model.
     */
    ConnectXController(IGameBoard model, ConnectXView view, int np){
        this.curGame = model;
        this.screen = view;
        numPlayers = np;
        //Initializes currentplayer to 0
        currentPlayer = 0;
        //Array list containing the characters
        List<Character> charList = new ArrayList<Character>();
        charList.add('X');
        charList.add('O');
        charList.add('A');
        charList.add('B');
        charList.add('C');
        charList.add('D');
        charList.add('E');
        charList.add('F');
        charList.add('G');
        charList.add('H');

        //Allocates memory for playerList
        playerList = new ArrayList<Character>();

        //Runs a for loop to fill up playerList depending on the amount of players playing
        for(int i = 0; i < numPlayers; i++) {
            playerList.add(charList.get(i));
        }

    }

    /**
     *
     *
     * @param col the column of the activated button
     * @post will allow the player to place a token in the column if it is not full, otherwise it will display an error
     * and allow them to pick again. Will check for a win as well. If a player wins it will allow for them to play another
     * game hitting any button
     */
    public void processButtonClick(int col) {
        //Checks if the column is full
        //If yes, displays an error
        if(curGame.checkIfFree(col) == false) {


            //Check if a tie has occured
            if(curGame.checkTie() == true) {
                //If this is the first time the loop is visited:
                if(newGametimeTie == false) {
                    //Prints tie message
                    screen.setMessage("Tie Game! Click anywhere to play again.");
                    //sets Boolean to true
                    newGametimeTie = true;
                }
                //If this is the second time the loop is visited, start a new game
                else {
                    this.newGame();
                }
            }
            //Otherwise prints the standard error
            else {
                screen.setMessage("Error, column is full, please choose another column.");
            }
        }
        //Otherwise, places the token and checks for a win
        else {
            //Places the token for the currentplayer on the GUI
            for(int i = 0; i < curGame.getNumRows(); i++) {
                //Creates a BoardPosition object at the current pos
                BoardPosition pos = new BoardPosition(i, col);
                //If the position is free, places the marker on the screen at that position
                if(curGame.whatsAtPos(pos) == ' ') {
                    screen.setMarker(i ,col, playerList.get(currentPlayer));
                    break;
                }
            }
            //Places the token for the currentplayer on the GameBoard
            curGame.placeToken(playerList.get(currentPlayer),col);

            //Then checks for a win
            if(curGame.checkForWin(col) == true) {
                //If this is the first time the loop is visited:
                if(newGametimeWin == false) {
                    //Prints victory message
                    screen.setMessage("Player "+playerList.get(currentPlayer)+" has won! Click anywhere to play again");
                    //Sets Boolean to true
                    newGametimeWin = true;
                }

                //If this is the second time the loop is visited, start a new game
                else {
                    this.newGame();
                }
            }

            //Otherwise keeps playing
            else {
                //If the current active player is the last one, the first player goes again next turn
                if(currentPlayer == playerList.size()-1) {
                    currentPlayer = 0;
                }
                //Otherwise, the active player counter is incremented
                else{
                    currentPlayer += 1;
                }

                //Prints out asking the next player to pick a column
                screen.setMessage("It is " + playerList.get(currentPlayer) + "'s turn. ");
            }


        }
    }

    /**
     * This method will start a new game by returning to the setup screen and controller
     */
    private void newGame()
    {
        //close the current screen
        screen.dispose();
        //start back at the set up menu
        SetupView screen = new SetupView();
        SetupController controller = new SetupController(screen);
        screen.registerObserver(controller);
    }
}
