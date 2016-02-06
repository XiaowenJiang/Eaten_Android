package com.example.w28l30.foursquareexample.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import com.example.w28l30.foursquareexample.Model.Titanic;
import com.example.w28l30.foursquareexample.Model.TitanicTextView;
import com.example.w28l30.foursquareexample.R;


public class SplashActivity extends Activity {

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    TitanicTextView tv = (TitanicTextView) findViewById(R.id.my_text_view);


    // set fancy typeface
    tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/luoma.TTF"));

    // start animation
    new Titanic().start(tv);

    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(2500);
                startActivity(new Intent(SplashActivity.this, ConnectActivity.class));
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }).start();
}

}
