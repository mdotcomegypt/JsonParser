package com.BuzzKora;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BuzzKora.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class MatchActivity extends AppCompatActivity {

    public  HashMap<String, String> match;

    public TextView teama;
    public TextView teamb;
    public TextView score;
    public ImageView teamathumb;
    public ImageView teambthumb;
    public ImageView statusIcon;
    public TextView date;
    public TextView status;
    public TextView statusSub;
    public TextView league;
    public TextView scroers1;
    public TextView scroers2;
    public TextView score1;
    public TextView score2;
    public TextView penality;
    public TextView min;


    public TextView toolbar_title;



    String VideoURL = "http://90kora.com/videosbymatch.php?nid=";
    String DetailsURL = "http://www.goal.com/feed/matches/match-events?format=goal&edition=ar&matchId=";
    JSONArray jsonData = null;
    JSONArray PlayerJsonData = null;

    ArrayList<HashMap<String, String>> VideoList = new ArrayList<HashMap<String, String>>();

    static final String VIDEO_KEY_TITLE = "title"; // parent node
    static final String  VIDEO_KEY_THUMB = "thumb";
    static final String  VIDEO_KEY_DATE = "date";
    static final String  VIDEO_KEY_ID= "id";
    static final String  VIDEO_KEY_MEDIA= "mp4";
    static final String  VIDEO_KEY_URL= "url";
    RecyclerView Videolistview;
    private VideoMatchAdapter VideoMatchAdapter;




    ArrayList<HashMap<String, String>> EventList = new ArrayList<HashMap<String, String>>();

    static final String  LIVE_KEY_TIME = "time"; // parent node
    static final String  LIVE_KEY_TYPE = "eventType";
    static final String  LIVE_KEY_DISPLAY = "displayEventType";
    static final String  LIVE_KEY_COMMENTARY= "commentary";
    static final String  LIVE_KEY_PLAYERA= "playera";
    static final String  LIVE_KEY_PLAYERB= "playerb";
    RecyclerView Eventlistview;
    private EventMatchAdapter EventMatchAdapter;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        Context context = this.getApplicationContext();


        String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
        Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPath);


        String fontPathAnton = "fonts/Baloo-Regular.ttf";
        Typeface tfAnton = Typeface.createFromAsset(context.getAssets(), fontPathAnton);








        match = (HashMap<String, String>) getIntent().getExtras().get("Match");

        Integer position = (Integer) getIntent().getExtras().get("position");

        teama = (TextView) findViewById(R.id.TeamA);
        teamb = (TextView) findViewById(R.id.TeamB);
        score = (TextView) findViewById(R.id.Score);
        scroers1 = (TextView) findViewById(R.id.ScorersA);
        scroers2 = (TextView) findViewById(R.id.ScorersB);
        score1 = (TextView) findViewById(R.id.ScoreA);
        score2 = (TextView) findViewById(R.id.ScoreB);
        teamathumb = (ImageView) findViewById(R.id.TeamAT);
        teambthumb = (ImageView) findViewById(R.id.TeamBT);
        statusIcon = (ImageView) findViewById(R.id.statusIcon);
        date = (TextView) findViewById(R.id.Date);
        status = (TextView) findViewById(R.id.Status);
        statusSub = (TextView) findViewById(R.id.StatusSub);
        league = (TextView) findViewById(R.id.League);
        penality = (TextView) findViewById(R.id.Penality);
        min = (TextView) findViewById(R.id.Min);



        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);



        /*appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(collapsingToolbarLayout.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapsingToolbarLayout)) {
                    toolbar_title.animate().alpha(1).setDuration(400);
                } else {
                    toolbar_title.animate().alpha(0).setDuration(400);
                }
            }
        });
        */


        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        Log.d("Time zone: ", tz.getDisplayName());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat DayFormat = new SimpleDateFormat("EEE d MMM");
        sdf.setTimeZone(tz);
        DayFormat.setTimeZone(tz);
        Long timestamp = Long.valueOf(match.get(ScheduleFragment.MATCH_KEY_DATE));
        String localTime = sdf.format(new Date(timestamp * 1000));
        String localDay = DayFormat.format(new Date(timestamp * 1000));

        String fullDate =  localDay + ", " + localTime;








        String statusText = match.get(ScheduleFragment.MATCH_KEY_STATUS);

        switch (statusText){
            case "Finished":
                status.setText(R.string.Finished);
                status.setTypeface(tf);
                status.setTextSize(16);
                statusIcon.setVisibility(View.GONE);
                statusSub.setText(fullDate);

                break;
            case "Live now":
                status.setText(R.string.Livenow);
                status.setTypeface(tf);
                status.setTextSize(16);
                RelativeLayout Live = (RelativeLayout) findViewById(R.id.Live);
                Live.setVisibility(View.VISIBLE);
                min.setText(match.get(ScheduleFragment.MATCH_KEY_TIME));
                min.setTextSize(30);
                min.setTypeface(tfAnton);

                String min = match.get(ScheduleFragment.MATCH_KEY_TIME);

                int timmer;

                if (min.equals("HT")){
                    timmer = 45;
                } else if (min.equals("FT")) {
                    timmer = 90;
                } else {
                    timmer = Integer.parseInt(min);
                }

                final ProgressBar mProgress = (ProgressBar) findViewById(R.id.timer_progress);
                mProgress.setProgress(timmer);   // Main Progress
                mProgress.setSecondaryProgress(90); // Secondary Progress
                mProgress.setMax(90); // Maximum Progress

                statusSub.setVisibility(View.GONE);
                statusIcon.setVisibility(View.GONE);

                break;

            case "Coming up":
                status.setText(localTime);
                status.setTypeface(tfAnton);
                status.setTextSize(40);
                statusIcon.setVisibility(View.GONE);
                statusSub.setText(localDay);

                break;

        }



        teama.setText(match.get(ScheduleFragment.MATCH_KEY_TEAMA));
        teamb.setText(match.get(ScheduleFragment.MATCH_KEY_TEAMB));
        league.setText(match.get(ScheduleFragment.MATCH_KEY_LEAGUE));

        scroers1.setText(match.get(ScheduleFragment.MATCH_KEY_SCORER1));
        scroers2.setText(match.get(ScheduleFragment.MATCH_KEY_SCORER2));
        score1.setText(match.get(ScheduleFragment.MATCH_KEY_SCORE1));
        score2.setText(match.get(ScheduleFragment.MATCH_KEY_SCORE2));

        if (match.get(ScheduleFragment.MATCH_KEY_PENALITY).equals("null")) {
            penality.setVisibility(View.GONE);
        } else {
            penality.setText(match.get(ScheduleFragment.MATCH_KEY_PENALITY));
        }






        Picasso.with(context)
                .load(match.get(ScheduleFragment.MATCH_KEY_THUMBA))
                .placeholder(R.drawable.placeholder)
                .into(teamathumb);

        Picasso.with(context)
                .load(match.get(ScheduleFragment.MATCH_KEY_THUMBB))
                .placeholder(R.drawable.placeholder)
                .into(teambthumb);




        // Applying font
        teama.setTypeface(tf);
        teamb.setTypeface(tf);

        league.setTypeface(tf);
        scroers1.setTypeface(tf);
        scroers2.setTypeface(tf);
        penality.setTypeface(tfAnton);
        score1.setTypeface(tfAnton);
        score2.setTypeface(tfAnton);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.share);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = match.get(ScheduleFragment.MATCH_KEY_TITLE) + " " + match.get(ScheduleFragment.MATCH_KEY_URL) ;
                String shareSub = match.get(ScheduleFragment.MATCH_KEY_TITLE);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));


            }
        });

        VideoURL = VideoURL + match.get(ScheduleFragment.MATCH_KEY_ID);

        DetailsURL = DetailsURL + match.get(ScheduleFragment.MATCH_KEY_REF);

        new VideoJsonTask().execute();

        setRepeatingAsyncTask();



    }

    private void setRepeatingAsyncTask() {

        final Handler handler = new Handler();
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {

                            new LiveEventsJsonTask().execute();
                        } catch (Exception e) {
                            // error, do something
                        }
                    }
                });
            }
        };

        timer.schedule(task, 0, 60*1000);  // interval of one minute

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



    private class VideoJsonTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {
            String jsonStr = parser.getDataFromUrl(VideoURL);
            return jsonStr;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(String result) {



            try {
                jsonData = new JSONArray(result);

                VideoList.clear();


                for (int i = 0; i < jsonData.length(); i++) {
                    JSONObject row = jsonData.getJSONObject(i);


                    HashMap<String, String> Videos = new HashMap<String, String>();
                    Videos.put(VIDEO_KEY_TITLE, row.getString("title"));
                    Videos.put(VIDEO_KEY_THUMB, row.getString("thumb"));
                    Videos.put(VIDEO_KEY_DATE, row.getString("date"));
                    Videos.put(VIDEO_KEY_MEDIA, row.getString("mp4"));
                    Videos.put(VIDEO_KEY_ID, row.getString("id"));
                    Videos.put(VIDEO_KEY_URL, row.getString("url"));


                    VideoList.add(Videos);
                }




                Videolistview = (RecyclerView) findViewById(R.id.Videolistview);

                VideoMatchAdapter = new VideoMatchAdapter(VideoList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                Videolistview.setLayoutManager(mLayoutManager);
                Videolistview.setItemAnimator(new DefaultItemAnimator());
                Videolistview.setAdapter(VideoMatchAdapter);



                Videolistview.setVisibility(View.VISIBLE);


                Videolistview.addOnItemTouchListener(
                        new RecyclerItemClickListener(getApplicationContext(), Videolistview ,new RecyclerItemClickListener.OnItemClickListener() {
                            @Override public void onItemClick(View view, final int position) {
                                HashMap<String, String> video = VideoList.get(position);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("Video", video);
                                bundle.putInt("position", position);

                                // launch List activity
                                Intent intent = new Intent(MatchActivity.this, VideoActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);



                            }

                            @Override public void onLongItemClick(View view, int position) {
                                /*HashMap<String, String> video = VideoList.get(position);
                                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                String shareBody = video.get(VideoFragment.VIDEO_KEY_TITLE) + " " + video.get(VideoFragment.VIDEO_KEY_URL) ;
                                String shareSub = video.get(VideoFragment.VIDEO_KEY_TITLE);
                                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                                startActivity(Intent.createChooser(sharingIntent, "Share using"));*/
                            }
                        })
                );





            } catch (JSONException e) {
                android.util.Log.e("INFO", "JSON was invalid");
            }



        }
    }




    private class LiveEventsJsonTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {
            String jsonStr = parser.getDataFromUrl(DetailsURL);
            return jsonStr;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(String result) {



            try {
                jsonData = new JSONArray(result);

                EventList.clear();


                for (int i = jsonData.length()-1; i >= 0; i--) {
                    JSONObject row = jsonData.getJSONObject(i);


                    HashMap<String, String> Event = new HashMap<String, String>();

                    Event.put(LIVE_KEY_TIME, row.getString("time"));
                    Event.put(LIVE_KEY_TYPE, row.getString("eventType"));
                    Event.put(LIVE_KEY_DISPLAY, row.getString("displayEventType"));
                    Event.put(LIVE_KEY_COMMENTARY, row.getString("commentary"));






                        PlayerJsonData = new JSONArray(row.getString("players"));




                        if (PlayerJsonData.length() == 1) {
                            Event.put(LIVE_KEY_PLAYERA, PlayerJsonData.getString(0));
                            Event.put(LIVE_KEY_PLAYERB, null);

                        }
                        if (PlayerJsonData.length() > 1) {
                            Event.put(LIVE_KEY_PLAYERA, PlayerJsonData.getString(0));
                            Event.put(LIVE_KEY_PLAYERB, PlayerJsonData.getString(1));

                        }


                    EventList.add(Event);
                }




                Eventlistview = (RecyclerView) findViewById(R.id.Eventlistview);

                EventMatchAdapter = new EventMatchAdapter(EventList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                Eventlistview.setLayoutManager(mLayoutManager);
                Eventlistview.setItemAnimator(new DefaultItemAnimator());
                Eventlistview.setAdapter(EventMatchAdapter);



                Eventlistview.setVisibility(View.VISIBLE);








            } catch (JSONException e) {
                android.util.Log.e("INFO", "JSON was invalid");
            }



        }
    }

}
