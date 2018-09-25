package uk.conortyler.floodit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by conor on 12/02/2018.
 */

public class GameView extends View {

    private Paint mGridPaint;
    private Paint mBtnPaint;
    private Paint mBGPaint;
    private Paint mStrokePaint;

    private TextPaint textPaint;

    private int blockWidth;
    private int blockHeight;
    private int numRows;
    private int numCols;

    private GestureDetector mGestureDetector;
    private AbstractGame.GameWinListener gameWinListener;
    private AbstractGame.GamePlayListener gamePlayListener;

    private FloodItDbHelper dbHelper;
    private SQLiteDatabase leaderboard;

    AlertDialog winDialog;
    AlertDialog loseDialog;

    Game mGame;

    public GameView(Context context, Config config) {
        super(context);
        init();


        if(config == null) {
            mGame = new Game(Game.DEFAULT_WIDTH, Game.DEFAULT_HEIGHT, Game.DEFAULT_COLOUR_COUNT, Game.DEFAULT_MAX_ROUND, "Default");
        } else{
            mGame = new Game(config.getWidth(), config.getHeight(), config.getClrCount(), config.getMaxRound(), config.getUserName());
        }
        mGestureDetector = new GestureDetector(context, new MyGestureListener());

        dbHelper = new FloodItDbHelper(getContext());
        leaderboard = dbHelper.getWritableDatabase();

        AlertDialog.Builder builder = new AlertDialog.Builder(GameViewActivity.mDialogContext);
        builder.setTitle("You Win!\nWould you like to play Again?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id){
                Activity host = (Activity) getContext();
                Intent intent = host.getIntent();
                host.finish();
                host.startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                Activity host = (Activity) getContext();
                host.finish();
            }
        });

        winDialog = builder.create();

        builder.setTitle("You Lose\nWould you like to play again?");
        loseDialog = builder.create();

        gameWinListener = new AbstractGame.GameWinListener() {
            @Override
            public void onWon(AbstractGame game, int rounds) {
                if(rounds < mGame.getMaxRound()){
                    winDialog.show();
                    mGame.addStats(leaderboard, true);
                    mGame.addToLeaderboard(leaderboard);
                }
            }
        };

        gamePlayListener = new AbstractGame.GamePlayListener() {
            @Override
            public void onGameChanged(AbstractGame game, int round) {
                if(round >= mGame.getMaxRound()){
                    loseDialog.show();
                    mGame.addStats(leaderboard, false);
                }
            }
        };

        mGame.addGameWinListener(gameWinListener);
        mGame.addGamePlayListener(gamePlayListener);


    }

    void init(){
        mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGridPaint.setStyle(Paint.Style.FILL);

        mBtnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBtnPaint.setStyle(Paint.Style.FILL);

        mBGPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBGPaint.setStyle(Paint.Style.FILL);
        mBGPaint.setColor(Color.BLACK);

        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setColor(Color.BLACK);
        mStrokePaint.setStrokeWidth(6);

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(200);
        textPaint.setColor(Color.WHITE);

    }

    private int assignColour(int clr){
        switch(clr){
            case 0:
                return Color.MAGENTA;
            case 1:
                return Color.BLUE;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.RED;
            case 4:
                return Color.YELLOW;
            case 5:
                return Color.GRAY;
            case 6:
                return Color.parseColor("#008080");
            case 7:
                return Color.parseColor("#BA4A00");
            case 8:
                return Color.parseColor("#ABEBC6");
            case 9:
                return Color.parseColor("#8E44AD");
        }

        return -1;
    }

    @Override
    protected void onDraw(Canvas canvas){

        Paint paint;

        numRows = mGame.getWidth();
        numCols = mGame.getHeight();
        blockWidth = getWidth() / numRows;
        blockHeight = getWidth() / numCols;

        canvas.drawRect(0,0, getWidth(), getHeight(), mBGPaint);

        for(int row = 0; row < numRows; row++){
            for(int col = 0; col < numCols; col++){
                mGridPaint.setColor(assignColour(mGame.getColor(row,col)));

                canvas.drawRect(row * blockWidth, col * blockHeight, (row+1) * blockWidth, (col+1) * blockHeight, mGridPaint);
            }
        }

        int btnWidth = getWidth() / mGame.getColourCount();

        for(int clrCount = 0; clrCount < mGame.getColourCount(); clrCount++){
            mBtnPaint.setColor(assignColour(clrCount));

            canvas.drawRect(clrCount * btnWidth, getHeight() - getHeight()/6, (clrCount+1) * btnWidth, getBottom(), mBtnPaint);
            canvas.drawRect(clrCount * btnWidth, getHeight() - getHeight()/6, (clrCount+1) * btnWidth, getBottom(), mStrokePaint);
        }

        canvas.drawText("Round " + mGame.getRound() + "/" + mGame.getMaxRound(), getWidth()/2,getHeight() - getHeight()/4.7f, textPaint);
    }

    public boolean onTouchEvent(MotionEvent ev){
        boolean r = this.mGestureDetector.onTouchEvent(ev);

        return super.onTouchEvent(ev) || r;
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDown(MotionEvent ev){

            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent ev){

            int clrPress = getColourPressed(ev);

            if(clrPress == -1){
                return false;
            } else{
                mGame.playColour(clrPress);
                invalidate();
                mGame.isWon();
                return true;
            }


        }

        public int getColourPressed(MotionEvent ev){
            float x = ev.getX();
            float y = ev.getY();

            float topBtn = getHeight() - getHeight()/6;

            int btnWidth = getWidth() / mGame.getColourCount();

            for(int clrCount = 0; clrCount < mGame.getColourCount(); clrCount++){

                if(y > topBtn){
                    if(x > (clrCount * btnWidth) && x < ((clrCount+1) * btnWidth)){
                        return clrCount;
                    }
                }
            }

            return -1;

        }
    }

}
