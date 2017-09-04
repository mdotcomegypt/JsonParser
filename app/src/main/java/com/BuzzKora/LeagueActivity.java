package com.BuzzKora;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class LeagueActivity extends AppCompatActivity {

    public int nid;
    public String tab;


    public TextView leagueName;
    public ImageView leagueLogo;
    public TextView leagueSeason;


    public boolean initialLoading = true;


    JSONObject leagueDetails = null;

    public String LeagueDetailsURL;

    public static String leagueVideos = null;
    public static String leagueStats = null;
    public static String leagueTable = null;
    public static String leagueNews = null;
    public static String leagueWeeks = null;
    public static String leagueTeams = null;



    public static String leagueNameShared = null;


    boolean videoTab = false;
    boolean newsTab = false;
    boolean tableTab = false;
    boolean statsTab = false;
    boolean weeksTab = false;
    boolean teamsTab = false;


    TabLayout tabLayout;


    private ViewPager ViewPager;

    private ViewPagerAdapter ViewPagerAdapter;

    public final List<String> TabsTitles = new ArrayList<>();

    public String fontPath;
    public String fontPathAnton;

    public Typeface tfAnton;
    public Typeface tf;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


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







        nid = (int) getIntent().getExtras().get("nid");

        /*android.util.Log.e("LEAGUE NID", nid);*/


        leagueName = (TextView) findViewById(R.id.leagueName);
        leagueSeason = (TextView) findViewById(R.id.leagueSeason);
        leagueLogo = (ImageView) findViewById(R.id.leagueLogo);


        context = getApplication().getApplicationContext();


        String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
        tf = Typeface.createFromAsset(context.getAssets(), fontPath);


        String fontPathAnton = "fonts/en_font_bold.ttf";
        tfAnton = Typeface.createFromAsset(context.getAssets(), fontPathAnton);



        final SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);

        final Button follow = (Button) findViewById(R.id.follow);

        final String key = "key_"+nid;



        if (SP.getBoolean(key,false)){
            follow.setBackgroundResource(R.drawable.follow_button_active);
            follow.setText(context.getString(R.string.remove_follow_txt));
        } else {
            follow.setBackgroundResource(R.drawable.follow_button);
            follow.setText(context.getString(R.string.follow_txt));
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
                    follow.setText(context.getString(R.string.follow_txt));
                    editor.commit();

                } else {

                    follow.setBackgroundResource(R.drawable.follow_button_active);
                    editor.putBoolean(key, true);
                    FirebaseMessaging.getInstance().subscribeToTopic(key);
                    follow.setText(context.getString(R.string.remove_follow_txt));
                    editor.commit();

                }
            }
        });

        follow.setTypeface(tf);


        new LeagueDetailsJsonTask().execute();


    }


    public void setInitialData(JSONObject leagueDetails) {


        try {


            leagueNameShared = leagueDetails.getString("name");


            leagueName.setText(leagueDetails.getString("name"));
            leagueSeason.setText(leagueDetails.getString("season"));

            Picasso.with(context)
                    .load(leagueDetails.getString("logo"))
                    .placeholder(R.drawable.team_placeholder)
                    .into(leagueLogo);


            leagueName.setTypeface(tf);
            leagueSeason.setTypeface(tf);


            final String leagueURL = leagueDetails.getString("url");
            final String leagueTitle = leagueDetails.getString("title");


            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.share);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = null;
                    shareBody = leagueTitle + " " + leagueURL;
                    String shareSub = null;
                    shareSub = leagueTitle;
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share using"));


                }
            });
        } catch (JSONException e) {
            android.util.Log.e("INFO", "JSON was invalid");
        }


    }


    public void buildTabs(JSONObject leagueDetails) {

        try {


            //leagueVideos = leagueDetails.getString("videos");
            //leagueNews = leagueDetails.getString("news");
            leagueTable = leagueDetails.getString("table");
            leagueWeeks = leagueDetails.getString("weeks");
            //leagueResult = leagueDetails.getString("result");
           /* leagueStats = leagueDetails.getString("stats");
            leagueTable = leagueDetails.getString("table");
            leagueTeams = leagueDetails.getString("teams");*/




            /*if (!leagueNews.equals("null")) {
                newsTab = true;
                TabsTitles.add(getResources().getString(R.string.League_News));
            }

            if (!leagueVideos.equals("null")) {
                videoTab = true;
                TabsTitles.add(getResources().getString(R.string.League_Videos));
            }*/

            if (!leagueTable.equals("null")) {
                tableTab = true;
                TabsTitles.add(getResources().getString(R.string.League_Table));
            }

            if (!leagueWeeks.equals("null")) {
                weeksTab = true;
                TabsTitles.add(getResources().getString(R.string.League_Fixture));
            }

            /*

            if (!leagueResult.equals("null")) {
                resultTab = true;
                TabsTitles.add(getResources().getString(R.string.League_Result));
            }


            if (!leagueStats.equals("null")) {
                statsTab = true;
                TabsTitles.add(getResources().getString(R.string.League_Stats));
            }


            if (!leagueTeams.equals("null")) {
                teamsTab = true;
                TabsTitles.add(getResources().getString(R.string.League_Teams));
            }*/


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

            ViewPager.setCurrentItem(TabsTitles.size());

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


    private class LeagueDetailsJsonTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {

            Long dfLong = System.currentTimeMillis() / 100000;
            String DayTimeStamp = dfLong.toString()+"00";

            LeagueDetailsURL = Config.LEAGUE_DETAILS_URL + nid + "&t=" + DayTimeStamp;
            android.util.Log.e("DetailsURL", LeagueDetailsURL);
            String jsonStr = parser.getDataFromUrl(LeagueDetailsURL);
            return jsonStr;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(String result) {


            try {


                leagueDetails = new JSONObject(result);


                setInitialData(leagueDetails);
                buildTabs(leagueDetails);


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


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        /*if (newsTab == true) {
            adapter.addFrag(new LeagueNewsFragment(), getResources().getString(R.string.League_News));
        }
        if (videoTab == true) {
            adapter.addFrag(new LeagueVideoFragment(), getResources().getString(R.string.League_Videos));
        }*/

        if (tableTab == true) {
            adapter.addFrag(new LeagueTableFragment(),  getResources().getString(R.string.League_Table));
        }


        if (weeksTab == true) {
            adapter.addFrag(new LeagueWeeksFragment(),  getResources().getString(R.string.League_Fixture));
        }





        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        public final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {

            }
            return mFragmentList.get(position);
        }


        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
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
        AnalyticsApp.getInstance().trackScreenView("League Page");


    }


    @Override
    public void onStop() {
        super.onStop();


    }


    @Override
    public void onPause() {
        super.onPause();


    }


}
