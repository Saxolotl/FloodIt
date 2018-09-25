package uk.conortyler.floodit;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by conortyler on 18/02/2018.
 */

public class LeaderboardActivity extends AppCompatActivity {

    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);
        populateTable();
    }

    private Cursor getLeaderboardRows(){
        FloodItDbHelper dbHelper = new FloodItDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                FloodItLeaderBoard.LBEntry._ID,
                FloodItLeaderBoard.LBEntry.COLUMN_NAME_USERNAME,
                FloodItLeaderBoard.LBEntry.COLUMN_NAME_GRIDSIZE,
                FloodItLeaderBoard.LBEntry.COLUMN_NAME_CLRCOUNT,
                FloodItLeaderBoard.LBEntry.COLUMN_NAME_ROUND,
                FloodItLeaderBoard.LBEntry.COLUMN_NAME_SCORE
        };

        String sortOrder = FloodItLeaderBoard.LBEntry.COLUMN_NAME_SCORE + " DESC";

        Cursor cursor = db.query(
                FloodItLeaderBoard.LBEntry.TABLE_NAME,
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
        Cursor cursor = getLeaderboardRows();
        table = (TableLayout) findViewById(R.id.leaderTable);


        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(FloodItLeaderBoard.LBEntry._ID));
            String name = cursor.getString(cursor.getColumnIndex(FloodItLeaderBoard.LBEntry.COLUMN_NAME_USERNAME));
            String gridSize = cursor.getString(cursor.getColumnIndex(FloodItLeaderBoard.LBEntry.COLUMN_NAME_GRIDSIZE));
            int clrCount = cursor.getInt(cursor.getColumnIndex(FloodItLeaderBoard.LBEntry.COLUMN_NAME_CLRCOUNT));
            String round = cursor.getString(cursor.getColumnIndex(FloodItLeaderBoard.LBEntry.COLUMN_NAME_ROUND));
            int score = cursor.getInt(cursor.getColumnIndex(FloodItLeaderBoard.LBEntry.COLUMN_NAME_SCORE));

            TableRow lbRow = new TableRow(this);
            lbRow.setId(id);

            TextView nameView = new TextView(this);
            TextView gridSizeView = new TextView(this);
            TextView clrCountView = new TextView(this);
            TextView roundView = new TextView(this);
            TextView scoreView = new TextView(this);

            nameView.setText(name);
            gridSizeView.setText(gridSize);
            clrCountView.setText(Integer.toString(clrCount));
            roundView.setText(round);
            scoreView.setText(Integer.toString(score));

            nameView.setPadding(3,3,3,3);
            gridSizeView.setPadding(3,3,3,3);
            clrCountView.setPadding(3,3,3,3);
            roundView.setPadding(3,3,3,3);
            scoreView.setPadding(3,3,3,3);

            nameView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            gridSizeView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            clrCountView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            roundView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            scoreView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            lbRow.addView(nameView);
            lbRow.addView(gridSizeView);
            lbRow.addView(clrCountView);
            lbRow.addView(roundView);
            lbRow.addView(scoreView);

            table.addView(lbRow, TableLayout.LayoutParams.WRAP_CONTENT);

        }
    }
}
