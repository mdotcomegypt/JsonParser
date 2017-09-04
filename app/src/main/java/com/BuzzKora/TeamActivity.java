package com.BuzzKora;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeamActivity extends AppCompatActivity {

    public int nid;
    public String tab;


    public TextView teamName;
    public ImageView teamLogo;


    public boolean initialLoading = true;


    JSONObject teamDetails = null;

    public String teamDetailsURL;

    public static String teamSquad = null;
    public static String teamResults = null;
    public static String teamFixtures = null;
    public static String teamTables = null;
    public static String teamCompetitions = null;



    public static String teamNameShared = null;


    boolean fixturesTab = false;
    boolean resultsTab = false;
    boolean tablesTab = false;
    boolean squadTab = false;
    boolean competitionsTab = false;


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
        setContentView(R.layout.activity_team);
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
        //nid = 633;

        /*android.util.Log.e("LEAGUE NID", nid);*/


        teamName = (TextView) findViewById(R.id.teamName);
        teamLogo = (ImageView) findViewById(R.id.teamLogo);


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



        new TeamDetailsJsonTask().execute();


    }


    public void setInitialData(JSONObject leagueDetails) {


        try {


            teamNameShared = teamDetails.getString("name");


            teamName.setText(teamDetails.getString("name"));

            Picasso.with(context)
                    .load(leagueDetails.getString("logo"))
                    .placeholder(R.drawable.team_placeholder)
                    .into(teamLogo);


            teamName.setTypeface(tf);


            final String teamURL = teamDetails.getString("url");
            final String teamTitle = teamDetails.getString("name");


            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.share);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = null;
                    shareBody = teamTitle + " " + teamURL;
                    String shareSub = null;
                    shareSub = teamTitle;
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share using"));


                }
            });
        } catch (JSONException e) {
            android.util.Log.e("INFO", "JSON was invalid (1)");
        }


    }


    public void buildTabs(JSONObject leagueDetails) {

        try {


            teamTables = leagueDetails.getString("tables");
            teamFixtures = leagueDetails.getString("fixtures");
            teamResults = leagueDetails.getString("results");
            teamSquad = leagueDetails.getString("squad");
            teamCompetitions= leagueDetails.getString("competitions");



            if (!teamSquad.equals("null")) {
                squadTab = true;
                TabsTitles.add(getResources().getString(R.string.squad));
            }

            if (!teamResults.equals("null")) {
                resultsTab = true;
                TabsTitles.add(getResources().getString(R.string.results));
            }


            if (!teamFixtures.equals("null")) {
                fixturesTab = true;
                TabsTitles.add(getResources().getString(R.string.fixtures));
            }



            /*if (!teamTables.equals("null")) {
                tablesTab = true;
                TabsTitles.add(getResources().getString(R.string.tables));
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
            android.util.Log.e("INFO", "JSON was invalid (2)");
        }

    }


    private class TeamDetailsJsonTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {

            Long dfLong = System.currentTimeMillis() / 100000;
            String DayTimeStamp = dfLong.toString()+"00";

            teamDetailsURL = Config.TEAM_DETAILS_URL + nid + "&t=" + DayTimeStamp;
            android.util.Log.e("DetailsURL", teamDetailsURL);

            String jsonStr = parser.getDataFromUrl(teamDetailsURL);
            return jsonStr;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(String result) {


            try {


                teamDetails = new JSONObject(result);


                setInitialData(teamDetails);
                buildTabs(teamDetails);


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

        if (squadTab == true) {
            adapter.addFrag(new TeamSquadFragment(),  getResources().getString(R.string.squad));
        }


        if (resultsTab == true) {
            adapter.addFrag(new TeamResultsFragment(),  getResources().getString(R.string.results));
        }



        if (fixturesTab == true) {
            adapter.addFrag(new TeamFixturesFragment(),  getResources().getString(R.string.fixtures));
        }




        /*if (tablesTab == true) {
            adapter.addFrag(new LeagueTableFragment(),  getResources().getString(R.string.tables));
        }*/







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
