package uk.conortyler.floodit;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by conortyler on 18/02/2018.
 */

public class StatisticsActivity extends AppCompatActivity {

    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);
        populateTable();
    }

    private Cursor getStatsRow(){
        FloodItDbHelper dbHelper = new FloodItDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                FloodItLeaderBoard.StatEntry._ID,
                FloodItLeaderBoard.StatEntry.COLUMN_NAME_USERNAME,
                FloodItLeaderBoard.StatEntry.COLUMN_NAME_MOVES,
                FloodItLeaderBoard.StatEntry.COLUMN_NAME_GAMES,
                FloodItLeaderBoard.StatEntry.COLUMN_NAME_GAMESWON,
                FloodItLeaderBoard.StatEntry.COLUMN_NAME_GAMESLOST
        };

        String sortOrder = FloodItLeaderBoard.StatEntry.COLUMN_NAME_USERNAME + " ASC";

        Cursor cursor = db.query(
                FloodItLeaderBoard.StatEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        return cursor;
    }

    private void populateTable(){
        Cursor cursor = getStatsRow();
        table = (TableLayout) findViewById(R.id.leaderTable);


        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(FloodItLeaderBoard.StatEntry._ID));
            String name = cursor.getString(cursor.getColumnIndex(FloodItLeaderBoard.StatEntry.COLUMN_NAME_USERNAME));
            int moves = cursor.getInt(cursor.getColumnIndex(FloodItLeaderBoard.StatEntry.COLUMN_NAME_MOVES));
            int gamePlayed = cursor.getInt(cursor.getColumnIndex(FloodItLeaderBoard.StatEntry.COLUMN_NAME_GAMES));
            int gameWon = cursor.getInt(cursor.getColumnIndex(FloodItLeaderBoard.StatEntry.COLUMN_NAME_GAMESWON));
            int gameLost = cursor.getInt(cursor.getColumnIndex(FloodItLeaderBoard.StatEntry.COLUMN_NAME_GAMESLOST));

            TableRow lbRow = new TableRow(this);
            lbRow.setId(id);

            TextView nameView = new TextView(this);
            TextView moveView = new TextView(this);
            TextView gamePlayedView = new TextView(this);
            TextView gameWonView = new TextView(this);
            TextView gameLostView = new TextView(this);

            nameView.setText(name);
            moveView.setText(Integer.toString(moves));
            gamePlayedView.setText(Integer.toString(gamePlayed));
            gameWonView.setText(Integer.toString(gameWon));
            gameLostView.setText(Integer.toString(gameLost));

            nameView.setPadding(3,3,3,3);
            moveView.setPadding(3,3,3,3);
            gamePlayedView.setPadding(3,3,3,3);
            gameWonView.setPadding(3,3,3,3);
            gameLostView.setPadding(3,3,3,3);

            nameView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            moveView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            gamePlayedView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            gameWonView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            gameLostView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            lbRow.addView(nameView);
            lbRow.addView(moveView);
            lbRow.addView(gamePlayedView);
            lbRow.addView(gameWonView);
            lbRow.addView(gameLostView);

            table.addView(lbRow, TableLayout.LayoutParams.WRAP_CONTENT);

        }
    }
}
