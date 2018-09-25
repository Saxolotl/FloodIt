package uk.conortyler.floodit;

import android.provider.BaseColumns;

/**
 * Created by conortyler on 17/02/2018.
 */

public class FloodItLeaderBoard {

    private FloodItLeaderBoard() {}

    static class LBEntry implements BaseColumns{
        static final String TABLE_NAME = "leaderboard";
        static final String COLUMN_NAME_USERNAME = "name";
        static final String COLUMN_NAME_GRIDSIZE = "gridsize";
        static final String COLUMN_NAME_CLRCOUNT = "clrcount";
        static final String COLUMN_NAME_ROUND = "round";
        static final String COLUMN_NAME_SCORE = "score";
    }

    static class StatEntry implements BaseColumns{
        static final String TABLE_NAME = "statistics";
        static final String COLUMN_NAME_USERNAME = "username";
        static final String COLUMN_NAME_MOVES = "Moves";
        static final String COLUMN_NAME_GAMESWON = "GamesWon";
        static final String COLUMN_NAME_GAMESLOST = "GamesLost";
        static final String COLUMN_NAME_GAMES = "GamesPlayed";
    }

}
