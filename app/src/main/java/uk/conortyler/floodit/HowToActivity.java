package uk.conortyler.floodit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class HowToActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to);
    }

    public void closeBtn(View view){
        finish();
    }
}
