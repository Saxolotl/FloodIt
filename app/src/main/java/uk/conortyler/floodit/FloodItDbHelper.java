package uk.conortyler.floodit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by conortyler on 17/02/2018.
 */

public class FloodItDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_LEADER =
            "CREATE TABLE " + FloodItLeaderBoard.LBEntry.TABLE_NAME + " (" +
                    FloodItLeaderBoard.LBEntry._ID + " INTEGER PRIMARY KEY," +
                    FloodItLeaderBoard.LBEntry.COLUMN_NAME_USERNAME + " TEXT," +
                    FloodItLeaderBoard.LBEntry.COLUMN_NAME_GRIDSIZE + " TEXT," +
                    FloodItLeaderBoard.LBEntry.COLUMN_NAME_CLRCOUNT + " INTEGER," +
                    FloodItLeaderBoard.LBEntry.COLUMN_NAME_ROUND + " TEXT," +
                    FloodItLeaderBoard.LBEntry.COLUMN_NAME_SCORE + " INTEGER)";

    public static final String SQL_CREATE_STATS =
            "CREATE TABLE " + FloodItLeaderBoard.StatEntry.TABLE_NAME + " (" +
                    FloodItLeaderBoard.StatEntry._ID + " INTEGER PRIMARY KEY," +
                    FloodItLeaderBoard.StatEntry.COLUMN_NAME_USERNAME + " TEXT," +
                    FloodItLeaderBoard.StatEntry.COLUMN_NAME_MOVES + " TEXT," +
                    FloodItLeaderBoard.StatEntry.COLUMN_NAME_GAMES + " TEXT," +
                    FloodItLeaderBoard.StatEntry.COLUMN_NAME_GAMESLOST + " TEXT," +
                    FloodItLeaderBoard.StatEntry.COLUMN_NAME_GAMESWON + " TEXT)";

    private static final String SQL_DELETE_LEADER =
            "DROP TABLE IF EXISTS " + FloodItLeaderBoard.LBEntry.TABLE_NAME;

    private static final String SQL_DELETE_STATS =
            "DROP TABLE IF EXISTS " + FloodItLeaderBoard.StatEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "FloodItLB.db";

    public FloodItDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_LEADER);
        db.execSQL(SQL_CREATE_STATS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL(SQL_DELETE_LEADER);
        db.execSQL(SQL_DELETE_STATS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVer, int newVer){
        onUpgrade(db, oldVer, newVer);
    }
}
