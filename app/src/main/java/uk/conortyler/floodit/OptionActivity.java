package uk.conortyler.floodit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class OptionActivity extends Activity {

    Config config;
    Intent intent;

    int height = Game.DEFAULT_HEIGHT;
    int width = Game.DEFAULT_WIDTH;
    int clrCount = 6;
    int clrCountModifier = 0;
    int maxRound = Game.DEFAULT_MAX_ROUND;

    ToggleButton smallBtn;
    ToggleButton medBtn;
    ToggleButton largeBtn;
    ToggleButton custBtn;

    ToggleButton clr4Btn;
    ToggleButton clr6Btn;
    ToggleButton clr8Btn;
    ToggleButton clr10Btn;

    Spinner userSpin;

    List<String> userList;

    FloodItDbHelper dbHelper;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        intent = getIntent();

        if(intent.hasExtra("config")) {
            Bundle confData = intent.getExtras();
            config = (Config) confData.getParcelable("config");
        }

        smallBtn = findViewById(R.id.small);
        medBtn = findViewById(R.id.medium);
        largeBtn = findViewById(R.id.large);
        custBtn = findViewById(R.id.custom);

        clr4Btn = findViewById(R.id.colour4);
        clr6Btn = findViewById(R.id.colour6);
        clr8Btn = findViewById(R.id.colour8);
        clr10Btn = findViewById(R.id.colour10);

        populateSpinnerDB();
        getSelected();

    }

    public void addBtn(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add User");

        final EditText nameInput = new EditText(this);
        nameInput.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setView(nameInput);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addToDB(nameInput.getText().toString());
                populateSpinnerDB();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }

    public void delBtn(View view){
        String name = userSpin.getSelectedItem().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to delete " + name + "?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delFromDB(userSpin.getSelectedItem().toString());
                populateSpinnerDB();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }

    private void delFromDB(String name){
        String selection = FloodItLeaderBoard.StatEntry.COLUMN_NAME_USERNAME + "= ?";
        String[] selectionArgs = {name};

        db.delete(FloodItLeaderBoard.StatEntry.TABLE_NAME, selection, selectionArgs);
    }

    private void addToDB(String name){

        ContentValues values = new ContentValues();
        values.put(FloodItLeaderBoard.StatEntry.COLUMN_NAME_USERNAME, name);
        values.put(FloodItLeaderBoard.StatEntry.COLUMN_NAME_GAMES, 0);
        values.put(FloodItLeaderBoard.StatEntry.COLUMN_NAME_GAMESLOST, 0);
        values.put(FloodItLeaderBoard.StatEntry.COLUMN_NAME_GAMESWON, 0);
        values.put(FloodItLeaderBoard.StatEntry.COLUMN_NAME_MOVES, 0);

        long newRowId = db.insertOrThrow(FloodItLeaderBoard.StatEntry.TABLE_NAME, null, values);
    }

    private void populateSpinnerDB(){
        dbHelper = new FloodItDbHelper(this);
        db = dbHelper.getWritableDatabase();

        userSpin = (Spinner) findViewById(R.id.userSpinner);
        ArrayAdapter<String> adapter;
        userList = new ArrayList<String>();

        String[] projection = {
                FloodItLeaderBoard.StatEntry.COLUMN_NAME_USERNAME
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

        while(cursor.moveToNext()){
            String userName = cursor.getString(cursor.getColumnIndex(FloodItLeaderBoard.StatEntry.COLUMN_NAME_USERNAME));

            userList.add(userName);
        }

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, userList);
        userSpin.setAdapter(adapter);
    }

    private void getSelected(){
        if(config != null){
            switch(config.getHeight()){
                case 8:
                    smallBtn.setChecked(true);
                    break;
                case 15:
                    medBtn.setChecked(true);
                    break;
                case 24:
                    largeBtn.setChecked(true);
                    break;
                default:
                    custBtn.setChecked(true);
            }

            switch(config.getClrCount()){
                case 4:
                    clr4Btn.setChecked(true);
                    break;
                case 6:
                    clr6Btn.setChecked(true);
                    break;
                case 8:
                    clr8Btn.setChecked(true);
                    break;
                case 10:
                    clr10Btn.setChecked(true);
                    break;

            }

            userSpin.setSelection(userList.indexOf(config.getUserName()));
        } else{
            medBtn.setChecked(true);
            clr6Btn.setChecked(true);
        }
    }

    public void smallBtn(View view){
        height = 8;
        width = 8;
        maxRound = height*2;
        medBtn.setChecked(false);
        largeBtn.setChecked(false);
        custBtn.setChecked(false);
    }

    public void medBtn(View view){
        height = 15;
        width = 15;
        maxRound = height*2;
        smallBtn.setChecked(false);
        largeBtn.setChecked(false);
        custBtn.setChecked(false);
    }

    public void largeBtn(View view){
        height = 24;
        width = 24;
        maxRound = height*2;
        smallBtn.setChecked(false);
        medBtn.setChecked(false);
        custBtn.setChecked(false);
    }

    public void customBtn(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Grid Size");

        final EditText sizeInput = new EditText(this);
        sizeInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(sizeInput);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int size = Integer.parseInt(sizeInput.getText().toString());
                height = size;
                width = size;
                maxRound = height*2;
                smallBtn.setChecked(false);
                medBtn.setChecked(false);
                largeBtn.setChecked(false);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                custBtn.setChecked(false);
            }
        });

        builder.show();
    }

    public void clrCountBtn(View view){
        ToggleButton btn = (ToggleButton) view;
        int choice = Integer.parseInt(btn.getText().toString());
        clrCount = choice;
        switch(choice){
            case 4:
                clrCountModifier = -2;
                clr6Btn.setChecked(false);
                clr8Btn.setChecked(false);
                clr10Btn.setChecked(false);
                break;
            case 6:
                clrCountModifier = 0;
                clr4Btn.setChecked(false);
                clr8Btn.setChecked(false);
                clr10Btn.setChecked(false);
                break;
            case 8:
                clrCountModifier = 4;
                clr4Btn.setChecked(false);
                clr6Btn.setChecked(false);
                clr10Btn.setChecked(false);
                break;
            case 10:
                clrCountModifier = 8;
                clr4Btn.setChecked(false);
                clr6Btn.setChecked(false);
                clr8Btn.setChecked(false);
                break;
        }
    }

    public void saveConfig(View view){
        String userName = userSpin.getSelectedItem().toString();

        if(config != null){
            config.setHeight(height);
            config.setWidth(width);
            config.setClrCount(clrCount);
            config.setMaxRound(maxRound + clrCountModifier);
            config.setUserName(userName);
        } else{
            config = new Config(height,width,clrCount, maxRound + clrCountModifier, userName);
        }

        Intent menu = new Intent(OptionActivity.this, MainActivity.class);
        menu.putExtra("config", config);
        startActivity(menu);
        finish();
    }

    public void discardConfig(View view){
        Intent menu = new Intent(OptionActivity.this, MainActivity.class);
        if(config != null){
            menu.putExtra("config", config);
        }
        startActivity(menu);
        finish();
    }
}
