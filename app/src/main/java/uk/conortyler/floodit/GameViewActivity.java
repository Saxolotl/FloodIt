package uk.conortyler.floodit;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by conortyler on 17/02/2018.
 */

public class GameViewActivity extends AppCompatActivity {

    static Context mDialogContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDialogContext = this;
        Bundle confData = (Bundle) getIntent().getExtras();
        Config config = (Config) confData.getParcelable("config");
        GameView gameView = new GameView(this, config);
        setContentView(gameView);

    }
}
