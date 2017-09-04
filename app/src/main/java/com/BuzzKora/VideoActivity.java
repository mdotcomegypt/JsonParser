package com.BuzzKora;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
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
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.*;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;

public class VideoActivity extends AppCompatActivity {

    private static final String TAG = "VideoActivity";
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";


    public  HashMap<String, String> video;

    ArrayList<HashMap<String, String>> VideoList ;
    ArrayList<HashMap<String, String>> OriginalVideoList ;

    RecyclerView Videolistview;

    boolean fullScreen = false;

    private MoreVideoListAdapter MoreVideoListAdapter;

    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;

    NestedScrollView content;

    android.widget.LinearLayout.LayoutParams Oparams;

    View video_holder;

    private ExoPlayer.EventListener exoPlayerEventListener;

    private AdView mAdView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);


        VideoList = (ArrayList<HashMap<String, String>>) getIntent().getExtras().get("Video");
        OriginalVideoList = (ArrayList<HashMap<String, String>>) getIntent().getExtras().get("Video");




        content = (NestedScrollView) findViewById(R.id.content);


        video_holder = findViewById(R.id.video_holder);




        TextView title = (TextView) findViewById(R.id.VideoTitle);

        TextView date = (TextView) findViewById(R.id.VideoDate);
        TextView more = (TextView) findViewById(R.id.MoreVideoTitle);








        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.share);











        Integer position = (Integer) getIntent().getExtras().get("position");

        video = VideoList.get(position);










        title.setText(video.get(MatchDetailsActivity.VIDEO_KEY_TITLE));
        date.setText(video.get(MatchDetailsActivity.VIDEO_KEY_DATE));

        String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
        Typeface tf = Typeface.createFromAsset(title.getContext().getAssets(), fontPath);
        title.setTypeface(tf);
        more.setTypeface(tf);


        // 1. Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector( videoTrackSelectionFactory);

// 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

// 3. Create the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
        simpleExoPlayerView = new SimpleExoPlayerView(this);
        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.video_view);







//Set media controller
        simpleExoPlayerView.setUseController(true);
        simpleExoPlayerView.requestFocus();

// Bind the player to the view.
        simpleExoPlayerView.setPlayer(player);



        Uri mp4VideoUri =Uri.parse(video.get(MatchDetailsActivity.VIDEO_KEY_MEDIA));

//Random indian livestream link (low quality):
//        Uri mp4VideoUri =Uri.parse("http://54.255.155.24:1935//Live/_definst_/amlst:sweetbcha1novD235L240P/playlist.m3u8");




// Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
//Produces DataSource instances through which media data is loaded.
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoplayer2example"), bandwidthMeterA);
//Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();



// II. ADJUST HERE:

//This is the MediaSource representing the media to be played:
//FOR SD CARD SOURCE:
//        MediaSource videoSource = new ExtractorMediaSource(mp4VideoUri, dataSourceFactory, extractorsFactory, null, null);

//FOR LIVESTREAM LINK:
        //MediaSource videoSource = new HlsMediaSource(mp4VideoUri, dataSourceFactory, 1, null, null);
        MediaSource videoSource = new ExtractorMediaSource(mp4VideoUri, dataSourceFactory, extractorsFactory, null, null);
        final LoopingMediaSource loopingSource = new LoopingMediaSource(videoSource , 1);



// Prepare the player with the source.
        player.prepare(loopingSource);



        int width = simpleExoPlayerView.getWidth();
        int Height = (int) (width * 405f / 720f);


        DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
        params.width =  params.MATCH_PARENT;
        params.height = Height;

        simpleExoPlayerView.setLayoutParams(params);

        Oparams = (android.widget.LinearLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();

        video_holder.setSystemUiVisibility(View.VISIBLE);


        player.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {
                Log.v(TAG,"Listener-onTimelineChanged...");

            }



            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.v(TAG,"Listener-onTracksChanged...");

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Log.v(TAG,"Listener-onLoadingChanged...");

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.v(TAG,"Listener-onPlayerStateChanged...");
                if(playbackState == player.STATE_ENDED){
                    player.seekTo(0);
                    player.setPlayWhenReady(false);
                }
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.v(TAG,"Listener-onPlayerError...");
                player.prepare(loopingSource);
                player.setPlayWhenReady(true);

            }

            @Override
            public void onPositionDiscontinuity() {
                Log.v(TAG,"Listener-onPositionDiscontinuity...");

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }
        });



        player.setPlayWhenReady(true);



        outFullScreen();










        ImageButton fullscreen = (ImageButton) findViewById(R.id.fullscreen);

        fullscreen.setOnClickListener(new View.OnClickListener(){
            @Override
            //On click function
            public void onClick(View view) {

                toggle();
            }
        });



        ImageButton shareButton = (ImageButton) findViewById(R.id.shareButton);

        shareButton.setOnClickListener(new View.OnClickListener(){
            @Override
            //On click function
            public void onClick(View view) {

                player.setPlayWhenReady(false);
                player.getPlaybackState();



                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = video.get(MatchDetailsActivity.VIDEO_KEY_TITLE) + " " + video.get(MatchDetailsActivity.VIDEO_KEY_URL) ;
                String shareSub = video.get(MatchDetailsActivity.VIDEO_KEY_TITLE);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });




        if (VideoList.size() > 10) {

            Videolistview = (RecyclerView) findViewById(R.id.Videolistview);


            VideoList.subList(10, VideoList.size()).clear();

            MoreVideoListAdapter = new MoreVideoListAdapter(VideoList);




            int numOfCol = calculateNoOfColumns(getApplicationContext());
            RecyclerView.LayoutManager mLayoutManager =  new GridLayoutManager(getApplicationContext(), numOfCol);
            Videolistview.setLayoutManager(mLayoutManager);
            Videolistview.setItemAnimator(new DefaultItemAnimator());
            Videolistview.setAdapter(MoreVideoListAdapter);


            Videolistview.setVisibility(View.VISIBLE);




            Videolistview.addOnItemTouchListener(
                    new RecyclerItemClickListener(getApplicationContext(), Videolistview ,new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, final int position) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("Video", OriginalVideoList);
                            bundle.putInt("position", position);

                            Intent intent = new Intent(VideoActivity.this,
                                    VideoActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);



                        }

                        @Override public void onLongItemClick(View view, int position) {

                        }
                    })
            );

        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }





    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }


    private void goFullScreen() {
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
        params.width =  params.FILL_PARENT;
        params.height = params.FILL_PARENT;
        simpleExoPlayerView.setLayoutParams(params);

        video_holder.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.KEEP_SCREEN_ON
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        fullScreen = true;



        makeAllGone();
    }

    private void outFullScreen(){
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
        params.width =  params.FILL_PARENT;
        params.height = params.FILL_PARENT;

        simpleExoPlayerView.setLayoutParams(Oparams);

        video_holder.setSystemUiVisibility(View.VISIBLE | View.KEEP_SCREEN_ON);

        fullScreen = false;

        makeAllVisible();
    }

    private void makeAllGone(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        content.setVisibility(View.GONE);

    }

    private void makeAllVisible(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        content.setVisibility(View.VISIBLE);
    }


    private void toggle() {
        if (fullScreen) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            outFullScreen();
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            goFullScreen();
        }
    }







    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG,"onStop()...");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG,"onStart()...");

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        AnalyticsApp.getInstance().trackScreenView("Video|" + video.get(MatchDetailsActivity.VIDEO_KEY_TITLE));

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG,"onPause()...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"onDestroy()...");
        player.release();

    }

    @Override
    public void onBackPressed() {
        if (fullScreen) {
            toggle();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            goFullScreen();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            outFullScreen();
        }
    }







}