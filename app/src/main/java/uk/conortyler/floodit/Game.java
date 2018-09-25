package uk.conortyler.floodit;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by conor on 30/01/2018.
 */

public class Game extends AbstractGame {

    public static int DEFAULT_MAX_ROUND = 30;

    private int round = 1;
    private int maxRound = DEFAULT_MAX_ROUND;

    private int moves = 0;
    private int gamesPlayed = 0;
    private int gamesWon = 0;
    private int gamesLost = 0;

    private String userName = "Invalid User";
    private int[][] gameBoard;

    /**
     * Constructor which is used to create a new instance of game
     * @param width Desired width
     * @param height Desired height
     * @param colourCount Desired colour count
     * @param maxRound Desired max round
     * @param userName Desired username
     */
    public Game(int width, int height, int colourCount, int maxRound, String userName){
        super(width, height, colourCount);
        Random rand = new Random();

        this.maxRound = maxRound;
        this.userName = userName;

        gameBoard = new int[width][height];

        for(int wid = 0; wid < width ; wid++){
            for(int hei = 0; hei < height; hei++){
                gameBoard[wid][hei] = rand.nextInt(colourCount);
            }
        }


    }


    /**
     * Getter to return the current round
     *
     * @return The current round
     */
    @Override
    public int getRound() {
        return round;
    }

    /**
     * Set the colour at position (x,y) to the colour identified by the colour parameter
     *
     * @param x      The column to change
     * @param y      The row to change
     * @param colour The new colour.
     */
    @Override
    protected void setColor(int x, int y, int colour) {
        //System.out.println("Set " + x + " " + y + " to " + colour );
        gameBoard[x][y] = colour;
    }

    /**
     * Get the colour at position (x,y)
     *
     * @param x The column to change
     * @param y The row to change
     * @return The colour at the coordinates.
     */
    @Override
    public int getColor(int x, int y) {
        return gameBoard[x][y];
    }

    /**
     * Calls the gameplaylistener with onGameChanged to check the current status of the game
     *
     * @param round The round that the mGame is in.
     */
    @Override
    void notifyMove(int round) {
        for(GamePlayListener gpl : getGamePlayListeners()){
            gpl.onGameChanged(this, round);
        }
    }

    /**
     * Calls the gameWinListener with onWon to check if the game has been won
     *
     * @param round The round that the mGame is in / the amount of rounds used.
     */
    @Override
    void notifyWin(int round) {
        for(GameWinListener gwl : getGameWinListeners()){
            gwl.onWon(this, round);
        }
    }

    /**
     * Gets the top-left colour and then runs the fill method to flood-fill any valid cell
     *
     * @param clr The colour to fill with.
     */
    @Override
    void playColour(int clr) {
        int beforeColour = gameBoard[0][0];

        fill(beforeColour,clr,0,0);

        if(beforeColour != gameBoard[0][0]){
            notifyMove(round);
            round++;
        }
    }

    /**
     * Recursive method to implement the flood fill algorithm
     * @param targetColour Starting Colour
     * @param newColour Chosen Colour
     * @param x X co-ordinate of cell to fill
     * @param y Y co-ordinate of cell to fill
     */
    private void fill(int targetColour, int newColour, int x, int y){
        if(targetColour == newColour) return;
        if(x < 0 || y < 0) return;
        if(x >= getWidth() || y >= getHeight()) return;
        if(getColor(x,y) != targetColour) return;

        setColor(x,y,newColour);

        fill(targetColour,newColour,x,y-1);
        fill(targetColour,newColour,x,y+1);
        fill(targetColour,newColour,x-1,y);
        fill(targetColour,newColour,x+1,y);

        return;
    }

    /**
     * Getter to return the max round
     * @return Max Round
     */
    public int getMaxRound(){
        return maxRound;
    }

    /**
     * Getter to return the username
     * @return Username
     */
    public String getUserName() { return userName; }

    /**
     * Determine whether the mGame has been won.
     *
     * @return <code>true</code> if won, <code>false</code> if the mGame has not yet been won.
     */
    @Override
    public boolean isWon() {

        int count = 0;
        int target = getHeight() * getWidth();
        int startColour = getColor(0,0);

        for(int col = 0; col < getWidth(); col++){
            for(int row = 0; row < getHeight(); row++){
                if(getColor(col,row) == startColour){
                    count++;
                }
            }
        }

        if(count == target){
            notifyWin(round);
            return true;
        } else{
            return false;
        }
    }

    /**
     * Used to add the current game to the leaderboard database
     * @param db Leaderboard database
     */
    public void addToLeaderboard(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(FloodItLeaderBoard.LBEntry.COLUMN_NAME_USERNAME, getUserName());
        values.put(FloodItLeaderBoard.LBEntry.COLUMN_NAME_GRIDSIZE, getWidth() + "x" + getHeight());
        values.put(FloodItLeaderBoard.LBEntry.COLUMN_NAME_CLRCOUNT, getColourCount());
        values.put(FloodItLeaderBoard.LBEntry.COLUMN_NAME_ROUND, getRound() + "/" + getMaxRound());
        values.put(FloodItLeaderBoard.LBEntry.COLUMN_NAME_SCORE, getMaxRound() - getRound());



        long newRowId = db.insertOrThrow(FloodItLeaderBoard.LBEntry.TABLE_NAME, null, values);

    }

    /**
     * Used to retrieve the statistics for a specific username from the database
     * @param db Leaderboard database
     */

    public void getStats(SQLiteDatabase db){
        String[] projection = {
                FloodItLeaderBoard.StatEntry._ID,
                FloodItLeaderBoard.StatEntry.COLUMN_NAME_USERNAME,
                FloodItLeaderBoard.StatEntry.COLUMN_NAME_MOVES,
                FloodItLeaderBoard.StatEntry.COLUMN_NAME_GAMES,
                FloodItLeaderBoard.StatEntry.COLUMN_NAME_GAMESWON,
                FloodItLeaderBoard.StatEntry.COLUMN_NAME_GAMESLOST
        };

        String selection = FloodItLeaderBoard.StatEntry.COLUMN_NAME_USERNAME + "= ?";
        System.out.println(selection);
        String[] selectionArgs = {getUserName()};

        Cursor cursor = db.query(
                FloodItLeaderBoard.StatEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while(cursor.moveToNext()){
            moves = cursor.getInt(cursor.getColumnIndex(FloodItLeaderBoard.StatEntry.COLUMN_NAME_MOVES));
            gamesPlayed = cursor.getInt(cursor.getColumnIndex(FloodItLeaderBoard.StatEntry.COLUMN_NAME_GAMES));
            gamesWon = cursor.getInt(cursor.getColumnIndex(FloodItLeaderBoard.StatEntry.COLUMN_NAME_GAMESWON));
            gamesLost = cursor.getInt(cursor.getColumnIndex(FloodItLeaderBoard.StatEntry.COLUMN_NAME_GAMESLOST));
        }

    }

    public void addStats(SQLiteDatabase db, boolean won){
        getStats(db);

        ContentValues values = new ContentValues();
        values.put(FloodItLeaderBoard.StatEntry.COLUMN_NAME_MOVES, moves + getRound());
        values.put(FloodItLeaderBoard.StatEntry.COLUMN_NAME_GAMES, gamesPlayed + 1);

        if(won){
            values.put(FloodItLeaderBoard.StatEntry.COLUMN_NAME_GAMESWON, gamesWon + 1);
            System.out.println("We won");
        } else{
            values.put(FloodItLeaderBoard.StatEntry.COLUMN_NAME_GAMESLOST, gamesLost + 1);
            System.out.println("We lost");
        }

        String selection = FloodItLeaderBoard.StatEntry.COLUMN_NAME_USERNAME + "= ?";
        String[] selectionArgs = {getUserName()};

        int count = db.update(FloodItLeaderBoard.StatEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }
}
