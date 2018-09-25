package uk.conortyler.floodit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getIntent().hasExtra("config")){
            Bundle confData = getIntent().getExtras();
            config = (Config) confData.getParcelable("config");

        }

    }

    /**
     * Called by the play game button, used to start the game activity with the config passed as an
     * intent
     * @param view Button which has triggered this method
     */

    public void gameBtn(View view){
        Intent game = new Intent(MainActivity.this, GameViewActivity.class);
        if(config == null){
            game.putExtra("config", new Config(Game.DEFAULT_HEIGHT,Game.DEFAULT_WIDTH,Game.DEFAULT_COLOUR_COUNT, Game.DEFAULT_MAX_ROUND, "Default"));
        } else{
            game.putExtra("config", config);
        }
        this.startActivity(game);
    }

    /**
     * Called by the leaderboard button to start the leaderboard activity
     * @param view Button which has triggered this method
     */

    public void leaderBtn(View view){
        Intent leader = new Intent(MainActivity.this, LeaderboardActivity.class);
        this.startActivity(leader);
    }

    /**
     * Called by the statistics button to start the statistics activity
     * @param view Button which has triggered this method
     */

    public void statsBtn(View view){
        Intent stat = new Intent(MainActivity.this, StatisticsActivity.class);
        this.startActivity(stat);
    }

    /**
     * Called by the How to Play button to start the howtoPlay Activity
     * @param view Button which has triggered this method
     */

    public void howToBtn(View view){
        Intent howTo = new Intent(MainActivity.this, HowToActivity.class);
        this.startActivity(howTo);
    }

    /**
     * Called by the Options button to start the Options Activity
     * @param view Button which has triggered this method
     */

    public void optionsBtn(View view){
        Intent options = new Intent(MainActivity.this, OptionActivity.class);
        if(config == null){
            options.putExtra("config", new Config(Game.DEFAULT_HEIGHT,Game.DEFAULT_WIDTH,Game.DEFAULT_COLOUR_COUNT, Game.DEFAULT_MAX_ROUND, "Default"));
        } else{
            options.putExtra("config", config);
        }
        this.startActivity(options);
        finish();
    }
}
