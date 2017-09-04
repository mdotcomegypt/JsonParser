package com.BuzzKora;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class MatchDetailsActivity extends AppCompatActivity {

    public  String ref;
    public  String lid;
    public  String tab;



    public TextView teama;
    public TextView teamb;
    public ImageView teamathumb;
    public ImageView teambthumb;
    public TextView date;
    public TextView status;
    public TextView statusSub;
    public TextView league;
    public TextView round;
    public TextView scroers1;
    public TextView scroers2;
    public TextView score1;
    public TextView score2;
    public TextView penA;
    public TextView penB;
    public TextView min;


    public TextView toolbar_title;

    public Handler handler;
    public Timer timer;

    public boolean initialLoading = true;




    JSONObject matchDetails = null;

    public String DetailsURL;

    public static String matchVideos = null;
    public static String matchLineUPA =  null;
    public static String matchLineUPB =  null;
    public static String matchStats =  null;
    public static String matchEvents =  null;
    public static String matchTable =  null;
    public static String matchFormA =  null;
    public static String matchFormB =  null;
    public static String matchformAM =  null;
    public static String matchformBM =  null;
    public static String matchFormationA =  null;
    public static String matchFormationB =  null;
    public static String matchVenue =  null;
    public static String matchh2hStats =  null;
    public static String matchh2hMatch =  null;
    public static String TeamA = null;
    public static String TeamB =  null;
    public static String teama_id =  null;
    public static String teamb_id =  null;

    boolean videoTab = false;
    boolean lineupTab = false;
    boolean tableTab = false;
    boolean eventTab = false;
    boolean statsTab = false;
    boolean h2hTab = false;


    TabLayout tabLayout;

    LinearLayout formLayoutA;
    LinearLayout formLayoutB;



    static final String VIDEO_KEY_TITLE = "title"; // parent node
    static final String  VIDEO_KEY_THUMB = "thumb";
    static final String  VIDEO_KEY_DATE = "date";
    static final String  VIDEO_KEY_ID= "id";
    static final String  VIDEO_KEY_MEDIA= "mp4";
    static final String  VIDEO_KEY_URL= "url";





    static final String  LIVE_KEY_TIME = "time"; // parent node
    static final String  LIVE_KEY_TYPE = "eventType";
    static final String  LIVE_KEY_DISPLAY = "displayEventType";
    static final String  LIVE_KEY_COMMENTARY= "commentary";
    static final String  LIVE_KEY_PLAYERA= "playera";
    static final String  LIVE_KEY_PLAYERB= "playerb";
    RecyclerView Eventlistview;
    private EventMatchAdapter EventMatchAdapter;

    private ViewPager ViewPager;

    private ViewPagerAdapter ViewPagerAdapter;

    public final List<String> TabsTitles = new ArrayList<>();

    public  String fontPath;
    public String fontPathAnton;

    public Typeface tfAnton;
    public Typeface tf;

    Context context;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);







        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);






        Bundle extras = getIntent().getExtras();


            ref = (String) getIntent().getExtras().get("ref");








        teama = (TextView) findViewById(R.id.TeamA);
        teamb = (TextView) findViewById(R.id.TeamB);
        scroers1 = (TextView) findViewById(R.id.ScorersA);
        scroers2 = (TextView) findViewById(R.id.ScorersB);
        score1 = (TextView) findViewById(R.id.ScoreA);
        score2 = (TextView) findViewById(R.id.ScoreB);
        teamathumb = (ImageView) findViewById(R.id.TeamAT);
        teambthumb = (ImageView) findViewById(R.id.TeamBT);
        date = (TextView) findViewById(R.id.Date);
        status = (TextView) findViewById(R.id.Status);
        statusSub = (TextView) findViewById(R.id.StatusSub);
        league = (TextView) findViewById(R.id.League);
        round = (TextView) findViewById(R.id.Round);
        penA = (TextView) findViewById(R.id.PenA);
        penB = (TextView) findViewById(R.id.PenB);
        min = (TextView) findViewById(R.id.Min);

        context = getApplication().getApplicationContext();


        String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
        tf = Typeface.createFromAsset(context.getAssets(), fontPath);


        String fontPathAnton = "fonts/en_font_bold.ttf";
        tfAnton = Typeface.createFromAsset(context.getAssets(), fontPathAnton);





    }


    public void runTimer(){
           handler = new Handler();
            timer = new Timer();

            final TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            try {

                                new LoadDetailsJsonTask().execute();
                            } catch (Exception e) {
                                // error, do something
                            }
                        }
                    });
                }
            };

            timer.schedule(task, 0, Config.MATCH_TIMER * 1000);
    }


    public void updateData(JSONObject matchDetails){


        try {


            Calendar cal = Calendar.getInstance();
            TimeZone tz = cal.getTimeZone();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            SimpleDateFormat DayFormat = new SimpleDateFormat("EEE d MMM");
            sdf.setTimeZone(tz);
            DayFormat.setTimeZone(tz);
            Long timestamp = Long.valueOf(matchDetails.getString("date"));
            String localTime = sdf.format(new Date(timestamp * 1000));
            String localDay = DayFormat.format(new Date(timestamp * 1000));
            String fullDate = localDay + ", " + localTime;



            String statusText = matchDetails.getString("status");


            RelativeLayout Live = (RelativeLayout) findViewById(R.id.Live);

            final ProgressBar mProgress = (ProgressBar) findViewById(R.id.timer_progress);

            mProgress.setMax(90);
            mProgress.setSecondaryProgress(120);


            switch (statusText) {
                case "Finished":
                    status.setText(R.string.Finished);
                    status.setTypeface(tf);
                    status.setTextSize(16);
                    status.setVisibility(View.VISIBLE);
                    statusSub.setText(fullDate);
                    Live.setVisibility(View.GONE);

                    break;
                case "Live now":

                    statusSub.setText(R.string.Livenow);



                    statusSub.setTypeface(tf);
                    statusSub.setTextSize(14);

                    Live.setVisibility(View.VISIBLE);
                    min.setText(matchDetails.getString("time"));
                    min.setTextSize(25);
                    min.setTypeface(tfAnton);

                    String minuit = matchDetails.getString("time");

                    int timmer = 0;

                    if (minuit.equals("HT")) {
                        timmer = 45;
                    } else if (minuit.equals("FT")) {
                        timmer = 90;
                    } else {
                        timmer = Integer.parseInt(minuit);
                    }


                    switch (matchDetails.getString("period")) {
                        case "First Half":
                            statusSub.setText(R.string.first_half);
                            break;
                        case "Second Half":
                            statusSub.setText(R.string.second_half);
                            break;
                        case "Half Time":
                            min.setText("HT");
                            statusSub.setText(R.string.half_time);
                            break;
                        case "ET Pending":
                            min.setText("ET");
                            statusSub.setText(R.string.ET_Pending);
                            break;
                        case "ET First Half":
                            statusSub.setText(R.string.ET_First_Half);
                            mProgress.setMax(120);
                            mProgress.setSecondaryProgress(120);
                            break;
                        case "ET Second Half":
                            statusSub.setText(R.string.ET_Second_Half);
                            mProgress.setMax(120);
                            mProgress.setSecondaryProgress(120);
                            break;

                    }


                    mProgress.setProgress(timmer);   // Main Progress
                     // Secondary Progress
                     // Maximum Progress

                    status.setVisibility(View.GONE);

                    break;

                case "Coming up":
                    status.setText(localTime);
                    status.setTypeface(tfAnton);
                    status.setTextSize(20);
                    status.setVisibility(View.VISIBLE);
                    statusSub.setText(localDay);
                    score1.setVisibility(View.GONE);
                    score2.setVisibility(View.GONE);

                    break;

            }

            scroers1.setText(matchDetails.getString("scorer1"));
            scroers2.setText(matchDetails.getString("scorer2"));
            score1.setText(matchDetails.getString("score1"));
            score2.setText(matchDetails.getString("score2"));
            if (matchDetails.getString("penality").equals("null")) {
                //
            } else {
                penA.setText(matchDetails.getString("penA"));
                penA.setVisibility(View.VISIBLE);
                penB.setText(matchDetails.getString("penB"));
                penB.setVisibility(View.VISIBLE);
            }


            scroers1.setTypeface(tf);
            scroers2.setTypeface(tf);
            score1.setTypeface(tfAnton);
            score2.setTypeface(tfAnton);




        } catch (JSONException e) {
            android.util.Log.e("INFO", "JSON was invalid");
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setInitialData(final JSONObject matchDetails){


        try {


            Calendar cal = Calendar.getInstance();
            TimeZone tz = cal.getTimeZone();
            Log.d("Time zone: ", tz.getDisplayName());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            SimpleDateFormat DayFormat = new SimpleDateFormat("EEE d MMM");
            sdf.setTimeZone(tz);
            DayFormat.setTimeZone(tz);
            Long timestamp = Long.valueOf(matchDetails.getString("date"));
            String localTime = sdf.format(new Date(timestamp * 1000));
            String localDay = DayFormat.format(new Date(timestamp * 1000));
            String fullDate = localDay + ", " + localTime;
            String statusText = matchDetails.getString("status");


            RelativeLayout Live = (RelativeLayout) findViewById(R.id.Live);

            final ProgressBar mProgress = (ProgressBar) findViewById(R.id.timer_progress);
            mProgress.setSecondaryProgress(90); // Secondary Progress
            mProgress.setMax(90); // Maximum Progress


            switch (statusText) {
                case "Finished":
                    status.setText(R.string.Finished);
                    status.setTypeface(tf);
                    status.setTextSize(16);
                    status.setVisibility(View.VISIBLE);
                    statusSub.setText(fullDate);
                    Live.setVisibility(View.GONE);

                    break;
                case "Live now":




                    statusSub.setText(R.string.Livenow);


                    min.setText(matchDetails.getString("time"));
                    min.setTextSize(25);
                    min.setTypeface(tfAnton);

                    switch (matchDetails.getString("period")) {
                        case "First Half":
                            statusSub.setText(R.string.first_half);
                            break;
                        case "Second Half":
                            statusSub.setText(R.string.second_half);
                            break;
                        case "Half Time":
                            statusSub.setText(R.string.half_time);
                            min.setText("HT");
                            break;
                        case "ET Pending":
                            statusSub.setText(R.string.ET_Pending);
                            min.setText("ET");
                            break;
                        case "ET First Half":
                            statusSub.setText(R.string.ET_First_Half);
                            mProgress.setSecondaryProgress(120); // Secondary Progress
                            mProgress.setMax(120); // Maximum Progress
                            break;
                        case "ET Second Half":
                            statusSub.setText(R.string.ET_Second_Half);
                            mProgress.setSecondaryProgress(120); // Secondary Progress
                            mProgress.setMax(120); // Maximum Progress
                            break;

                    }

                    statusSub.setTypeface(tf);
                    statusSub.setTextSize(14);

                    Live.setVisibility(View.VISIBLE);


                    String minuit = matchDetails.getString("time");

                    int timmer;

                    if (minuit.equals("HT")) {
                        timmer = 45;
                    } else if (minuit.equals("FT")) {
                        timmer = 90;
                    } else {
                        timmer = Integer.parseInt(minuit);
                    }

                    mProgress.setProgress(timmer);   // Main Progress

                    status.setVisibility(View.GONE);

                    break;

                case "Coming up":
                    status.setText(localTime);
                    status.setTypeface(tfAnton);
                    status.setTextSize(20);
                    status.setVisibility(View.VISIBLE);
                    statusSub.setText(localDay);
                    score1.setVisibility(View.GONE);
                    score2.setVisibility(View.GONE);

                    break;

            }
            teama.setText(matchDetails.getString("teama"));
            teamb.setText(matchDetails.getString("teamb"));
            league.setText(matchDetails.getString("league"));


            if (matchDetails.has("league_id") && !matchDetails.optString("league_id").equals("null")) {

                final int lnid = Integer.parseInt(matchDetails.optString("league_id"));


                league.setOnClickListener(new View.OnClickListener() {
                    @Override
                    //On click function
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("nid", lnid);
                        Intent intent = new Intent(view.getContext(), LeagueActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

            }



            if(!matchDetails.getString("round").equals("null")) {
                round.setText(matchDetails.getString("round"));
            }

            scroers1.setText(matchDetails.getString("scorer1"));
            scroers2.setText(matchDetails.getString("scorer2"));
            score1.setText(matchDetails.getString("score1"));
            score2.setText(matchDetails.getString("score2"));
            if (matchDetails.getString("penality").equals("null")) {
                //
            } else {
                penA.setText(matchDetails.getString("penA"));
                penA.setVisibility(View.VISIBLE);
                penB.setText(matchDetails.getString("penB"));
                penB.setVisibility(View.VISIBLE);
            }

            Picasso.with(context)
                    .load(matchDetails.getString("thumb_url_a"))
                    .placeholder(R.drawable.team_placeholder)
                    .into(teamathumb);

            teama_id = matchDetails.getString("teama_id");


            teamathumb.setOnClickListener(new View.OnClickListener() {
                @Override
                //On click function
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("nid", Integer.parseInt(teama_id));
                    Intent intent = new Intent(view.getContext(), TeamActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            Picasso.with(context)
                    .load(matchDetails.getString("thumb_url_b"))
                    .placeholder(R.drawable.team_placeholder)
                    .into(teambthumb);


            teamb_id = matchDetails.getString("teamb_id");
            teambthumb.setOnClickListener(new View.OnClickListener() {
                @Override
                //On click function
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("nid", Integer.parseInt(teamb_id));
                    Intent intent = new Intent(view.getContext(), TeamActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            teama.setTypeface(tf);
            teamb.setTypeface(tf);
            league.setTypeface(tf);
            round.setTypeface(tf);
            scroers1.setTypeface(tf);
            scroers2.setTypeface(tf);
            score1.setTypeface(tfAnton);
            score2.setTypeface(tfAnton);


            TeamA = matchDetails.getString("teama");
            TeamB = matchDetails.getString("teamb");


            final String matchURL = matchDetails.getString("url");
            final String matchTITLE = matchDetails.getString("title");




            matchFormA = matchDetails.getString("formA");
            matchFormB = matchDetails.getString("formB");




            if (matchFormA != null && matchFormB != null) {

                formBuilder(matchFormA, "A");
                formBuilder(matchFormB, "B");
            }


            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.share);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = null;
                    shareBody = matchTITLE + " " + matchURL;
                    String shareSub = null;
                    shareSub = matchTITLE;
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share using"));


                }
            });
        } catch (JSONException e) {
            android.util.Log.e("INFO", "JSON was invalid");
        }






    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void formBuilder(String form, String Team){







        for( int z = 0; z < form.length(); z++ )
        {
            String charcter = String.valueOf(form.charAt(z));

            android.util.Log.e("charcter", charcter);

            TextView item = new TextView(this);
            item.setText(charcter);

            item.setTypeface(tf);
            item.setMaxLines(1);
            item.setTextSize(9);

            item.setTextColor(context.getResources().getColor(R.color.formTXT));


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(45,45);

            params.setMargins(1,1,1,1);
            item.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            item.setGravity(Gravity.CENTER);

            item.setLayoutParams(params);


            switch (charcter) {
                case "W":
                    item.setBackground(context.getResources().getDrawable(R.color.win));
                    break;
                case "L":
                    item.setBackground(context.getResources().getDrawable(R.color.lose));
                    break;
                case "D":
                    item.setBackground(context.getResources().getDrawable(R.color.draw));
                    break;


            }




            switch (Team) {
                case "A":
                    formLayoutA = (LinearLayout) findViewById(R.id.formA);
                    formLayoutA.addView(item);
                    break;
                case "B":
                    formLayoutB = (LinearLayout) findViewById(R.id.formB);
                    formLayoutB.addView(item);
                    break;

            }

        }
    }


    public void buildTabs (JSONObject matchDetails) {

        try {


            matchVideos = matchDetails.getString("videos");
            matchLineUPA = matchDetails.getString("lineUPA");
            matchLineUPB = matchDetails.getString("lineUPB");
            matchStats = matchDetails.getString("stats");
            matchEvents = matchDetails.getString("events");
            matchTable = matchDetails.getString("table");

            matchFormationA = matchDetails.getString("formationA");
            matchFormationB = matchDetails.getString("formationB");
            matchVenue = matchDetails.getString("venue");
            matchh2hStats = matchDetails.getString("h2hStats");
            matchh2hMatch = matchDetails.getString("h2hMatch");

            matchformAM = matchDetails.getString("formAmatches");
            matchformBM = matchDetails.getString("formBmatches");




            if (!matchEvents.equals("null")) {
                eventTab = true;
                TabsTitles.add(getResources().getString(R.string.events));
            }

            if (!matchVideos.equals("null")) {
                videoTab = true;
                TabsTitles.add(getResources().getString(R.string.highlights));
            }

            if (!matchLineUPA.equals("null")) {
                lineupTab = true;
                TabsTitles.add(getResources().getString(R.string.lineup));
            }


            if (!matchStats.equals("null")) {
                statsTab = true;
                TabsTitles.add(getResources().getString(R.string.stats));
            }

            if (!matchh2hStats.equals("null") && !matchh2hMatch.equals("null")) {
                h2hTab = true;
                TabsTitles.add(getResources().getString(R.string.h2h));
            }

            if (!matchTable.equals("null")) {
                tableTab = true;
                TabsTitles.add(getResources().getString(R.string.standings));
            }


            ViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            ViewPager = (ViewPager) findViewById(R.id.container);

            setupViewPager(ViewPager);

            tabLayout = (TabLayout) findViewById(R.id.tabs);


            tabLayout.setupWithViewPager(ViewPager);


            android.util.Log.e("SIZE", String.valueOf(TabsTitles.size()));

            if (TabsTitles.size() > 4) {
                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            }

            for (int counter = 0; counter < TabsTitles.size(); counter++) {
                View headerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.match_tab_item, null, false);
                TextView tabTitle = (TextView) headerView.findViewById(R.id.title);
                tabLayout.getTabAt(counter).setCustomView(headerView);
                tabTitle.setText(TabsTitles.get(counter));
                tabTitle.setTypeface(tf);
            }
        } catch (JSONException e) {
            android.util.Log.e("INFO", "JSON was invalid");
        }

    }


    public void updateTabsData (JSONObject matchDetails){
        try {
            matchVideos = matchDetails.getString("videos");
            matchLineUPA = matchDetails.getString("lineUPA");
            matchLineUPB = matchDetails.getString("lineUPB");
            matchStats = matchDetails.getString("stats");
            matchEvents = matchDetails.getString("events");
            matchTable = matchDetails.getString("table");
            matchFormA = matchDetails.getString("formA");
            matchFormB = matchDetails.getString("formB");
            matchFormationA = matchDetails.getString("formationA");
            matchFormationB = matchDetails.getString("formationB");
            matchVenue = matchDetails.getString("venue");
            matchh2hStats = matchDetails.getString("h2hStats");
            matchh2hMatch = matchDetails.getString("h2hMatch");

            matchformAM = matchDetails.getString("formAmatches");
            matchformBM = matchDetails.getString("formBmatches");
        } catch (JSONException e) {
            android.util.Log.e("INFO", "JSON was invalid");
        }
    }


    private class LoadDetailsJsonTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {

            Long dfLong = System.currentTimeMillis() / 10000;
            String DayTimeStamp = dfLong.toString()+"0";

            DetailsURL = Config.MATCH_DETAILS_URL + ref + "&t=" + DayTimeStamp;

            android.util.Log.e("DetailsURL", DetailsURL);
            String jsonStr = parser.getDataFromUrl(DetailsURL);
            return jsonStr;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(String result) {



            try {





                matchDetails = new JSONObject(result);



                //initai data



                updateData(matchDetails);



                //update tabs content

                updateTabsData(matchDetails);




                //build tabs
                if (initialLoading) {
                    setInitialData(matchDetails);
                    buildTabs(matchDetails);

                    initialLoading = false;
                }














            } catch (JSONException e) {
                android.util.Log.e("INFO", "JSON was invalid");
            }






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


    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (eventTab == true) {
            adapter.addFrag(new MatchEventsFragment(), getResources().getString(R.string.events));
        }
        if (videoTab == true) {
            adapter.addFrag(new MatchVideoFragment(), getResources().getString(R.string.Highlights));
        }

        if (lineupTab == true) {
            adapter.addFrag(new MatchLineupFragment(),  getResources().getString(R.string.lineup));
        }




        if (statsTab == true) {
            adapter.addFrag(new MatchStatsFragment(),  getResources().getString(R.string.stats));
        }


        if (h2hTab == true) {
            adapter.addFrag(new MatchH2HFragment(),  getResources().getString(R.string.h2h));
        }

        if (tableTab == true) {
            adapter.addFrag(new MatchTableFragment(),  getResources().getString(R.string.standings));
        }







        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        public final List<String> mFragmentTitleList = new ArrayList<>();



        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position)
        {
            if(position == 0)
            {

            }
            return mFragmentList.get(position);
        }



        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title)
        {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }






    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


    @Override
    public void onResume() {
        super.onResume();

        runTimer();
        AnalyticsApp.getInstance().trackScreenView("Match Page");
    }


    @Override
    public void onStop() {
        super.onStop();

        if (timer != null) {
            timer.cancel();
            timer.purge();
        }


    }


    @Override
    public void onPause() {
        super.onPause();

        if (timer != null) {


            timer.cancel();
            timer.purge();
        }

    }

















}
