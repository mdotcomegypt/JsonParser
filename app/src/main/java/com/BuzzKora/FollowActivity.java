package com.BuzzKora;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FollowActivity extends AppCompatActivity {

    TextView question;
    ImageView background;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        question = (TextView) findViewById(R.id.question);
        background = (ImageView) findViewById(R.id.background);

        new FollowTask().execute();




    }




    private class FollowTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {



            String jsonStr = parser.getDataFromUrl(Config.FOLLOW_SUGGESTION + System.currentTimeMillis());

            return jsonStr;
        }

        protected void onProgressUpdate(Integer... progress) {


        }

        protected void onPostExecute(String result) {


            try {


                JSONObject jsonData = new JSONObject(result);

                String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
                Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), fontPath);



                final SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



                final Button follow = (Button) findViewById(R.id.follow);

                final String key = jsonData.optString("key");



                if (SP.getBoolean(key,false)){
                    follow.setBackgroundResource(R.drawable.follow_button_active);
                    follow.setText(getApplicationContext().getString(R.string.remove_follow_txt));
                } else {
                    follow.setBackgroundResource(R.drawable.follow_button);
                    follow.setText(getApplicationContext().getString(R.string.follow_txt));
                }


                follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    //On click function
                    public void onClick(View view) {

                        boolean flag= SP.getBoolean(key,false);



                        SharedPreferences.Editor editor = SP.edit();

                        if (flag){
                            follow.setBackgroundResource(R.drawable.follow_button);
                            editor.putBoolean(key, false);
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(key);
                            follow.setText(getApplicationContext().getString(R.string.follow_txt));
                            editor.commit();

                        } else {

                            follow.setBackgroundResource(R.drawable.follow_button_active);
                            editor.putBoolean(key, true);
                            FirebaseMessaging.getInstance().subscribeToTopic(key);
                            follow.setText(getApplicationContext().getString(R.string.remove_follow_txt));
                            editor.commit();

                        }


                        finish();
                    }
                });


                question.setText(jsonData.optString("question"));


                Picasso.with(getApplicationContext())
                        .load(jsonData.optString("background"))
                        .into(background);

                follow.setTypeface(tf);
                question.setTypeface(tf);





            } catch (JSONException e) {
                android.util.Log.e("INFO", e.getMessage());

            }


        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }







    @Override
    protected void onResume() {
        super.onResume();

    }



    @Override
    public void onBackPressed() {

            super.onBackPressed();
    }








}