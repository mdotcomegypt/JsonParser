package com.BuzzKora;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    Intent i;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {



            @Override
            public void run() {


                SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());


                boolean registered = SP.getBoolean("key_registered",false);

                if (registered){
                    i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                } else {
                    i = new Intent(SplashActivity.this, RegisterActivityStepA.class);
                    startActivity(i);
                }


                finish();
            }
        }, SPLASH_TIME_OUT);





    }

}