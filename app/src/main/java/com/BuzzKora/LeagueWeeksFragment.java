package com.BuzzKora;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import static com.BuzzKora.LeagueActivity.leagueNameShared;
import static com.BuzzKora.LeagueActivity.leagueWeeks;


public class LeagueWeeksFragment extends Fragment {


    public String DayTimeStamp;
    JSONArray weeks = null;

    ArrayList<HashMap<String, String>> ScheduleList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> CalendarDays = new ArrayList<HashMap<String, String>>();

    static final String MATCH_KEY_ID = "id";
    static final String MATCH_KEY_TEAMA = "teama";
    static final String MATCH_KEY_TEAMB = "teamb";
    static final String MATCH_KEY_SCORE = "score";
    static final String MATCH_KEY_DATE = "date";
    static final String MATCH_KEY_LEAGUE = "league";
    static final String MATCH_KEY_THUMBA = "thumb_url_a";
    static final String MATCH_KEY_THUMBB = "thumb_url_b";
    static final String MATCH_KEY_TIME = "time";
    static final String MATCH_KEY_STATUS = "status";
    static final String MATCH_KEY_SCORER1 = "scorer1";
    static final String MATCH_KEY_SCORER2 = "scorer2";
    static final String MATCH_KEY_PENA = "penality";
    static final String MATCH_KEY_PENB = "penA";
    static final String MATCH_KEY_PENALITY = "penB";
    static final String MATCH_KEY_SCORE1 = "score1";
    static final String MATCH_KEY_SCORE2 = "score2";
    static final String MATCH_KEY_TITLE = "title";
    static final String MATCH_KEY_URL = "url";
    static final String MATCH_KEY_REF = "ref";
    static final String MATCH_KEY_HEADER = "header";


    static final String CA_KEY_TITLE = "title";
    static final String CA_KEY_NAME = "name";
    static final String CA_KEY_DISPLAY = "dispaly";


    private View ScheduleLoader;

    RecyclerView Schedulelistview;

    private boolean ScheduleLoading = true;

    private InterstitialAd mInterstitialAd;

    View rootView;


    private RecyclerView recyclerView;
    public LeagueWeeksAdapter LeagueWeeksAdapter;
    private LeagueWeeksCalendarAdapter LeagueWeeksCalendarAdapter;

    SwipeRefreshLayout mSwipeRefreshLayout;

    AsyncTask<String, Integer, String> runTask;

    private Long Start;

    private TextView noContent;


    public int newPosition = 0;

    Context context;

    TimeZone tz;
    String tzName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container,
                savedInstanceState);


        Calendar cal = Calendar.getInstance();
        tz = cal.getTimeZone();
        tzName = tz.getID();

        try {
            weeks = new JSONArray(leagueWeeks);
            rootView = inflater.inflate(R.layout.schedule, container, false);
            setHasOptionsMenu(true);

            Switch match_toggle = (Switch) rootView.findViewById(R.id.match_toggle);
            match_toggle.setVisibility(View.GONE);


            for (int i = 0; i < weeks.length(); i++) {

                JSONObject week = weeks.getJSONObject(i);

                String WeekNumber = week.optString("name");

                if (week.has("gameweek")){

                }

                String Active = week.optString("active");

                if(Active.equals("true")){
                    newPosition = i;
                }

                HashMap<String, String> Day = new HashMap<String, String>();


                if (week.has("gameweek")){
                    Day.put(CA_KEY_TITLE, "week");
                } else {
                    Day.put(CA_KEY_TITLE, "stage");
                }


                Day.put(CA_KEY_NAME, WeekNumber);
                CalendarDays.add(Day);
            }

            context = getActivity().getApplicationContext();

            ScheduleLoader = rootView.findViewById(R.id.loader);
            noContent = (TextView) rootView.findViewById(R.id.no_content);
            Schedulelistview = (RecyclerView) rootView.findViewById(R.id.Schedulelistview);


            final LinearLayoutManager ClayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            LeagueWeeksCalendarAdapter = new LeagueWeeksCalendarAdapter(CalendarDays);

            final RecyclerView calendarList = (RecyclerView) rootView.findViewById(R.id.calendar);

            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(calendarList);
            calendarList.setOnFlingListener(snapHelper);

            calendarList.setLayoutManager(ClayoutManager);
            calendarList.setItemAnimator(new DefaultItemAnimator());
            calendarList.setAdapter(LeagueWeeksCalendarAdapter);
            LeagueWeeksCalendarAdapter.setSelecteditem(newPosition);
            calendarList.smoothScrollToPosition(newPosition + 2);


            //default loading


            ScheduleLoader.setVisibility(View.VISIBLE);
            Schedulelistview.setVisibility(View.GONE);
            noContent.setVisibility(View.GONE);


            mInterstitialAd = new InterstitialAd(getActivity());
            mInterstitialAd.setAdUnitId(getString(R.string.admob_banner_interstitial));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setImmersiveMode(true);


            calendarList.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity().getApplicationContext(), calendarList, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, final int position) {



                            LeagueWeeksAdapter = null;


                            int centerOfScreen = calendarList.getWidth() / 2 - view.getWidth() / 2;
                            ClayoutManager.scrollToPositionWithOffset(position, centerOfScreen);
                            LeagueWeeksCalendarAdapter.setSelecteditem(position);


                            ScheduleLoader.setVisibility(View.VISIBLE);
                            Schedulelistview.setVisibility(View.GONE);
                            noContent.setVisibility(View.GONE);




                            newPosition = position;

                            new ScheduleJsonTask().execute();


                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                            //Toast.makeText(getActivity(), "Long CLICK", Toast.LENGTH_LONG).show();
                        }
                    })
            );


            //runTimer(3);


            mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swifeRefresh);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new ScheduleJsonTask().execute();
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

        new ScheduleJsonTask().execute();



        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.refresh_menu, menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.refresh:
                if (!ScheduleLoading) {
                    ScheduleLoader.setVisibility(View.VISIBLE);
                    Schedulelistview.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Loading .. ", Toast.LENGTH_LONG).show();
                    new ScheduleJsonTask().execute();


                } else {
                    Toast.makeText(getActivity(), "Already Loading .. ", Toast.LENGTH_LONG).show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ScheduleJsonTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {

            String jsonStr = LeagueActivity.leagueWeeks;


            return jsonStr;
        }

        protected void onProgressUpdate(Integer... progress) {


        }

        protected void onPostExecute(String result) {


            try {
                weeks = new JSONArray(result);

                ScheduleList.clear();

                JSONObject week = weeks.getJSONObject(newPosition); // repleace with week number - 1


                String weekMatches = week.getString("matches");

                android.util.Log.e("weekMatches", weekMatches);


                JSONArray weekMatchesArray = new JSONArray(weekMatches);


                String GroupDate = "";



                for (int i = 0; i < weekMatchesArray.length(); i++) {


                    JSONObject row = weekMatchesArray.getJSONObject(i);


                    HashMap<String, String> matches = new HashMap<String, String>();


                    JSONObject team_A = new JSONObject(row.getString("team_A"));
                    String team_A_name = team_A.getString("name");
                    String team_A_id = team_A.getString("id");
                    String Thumb_A = "http://cache.images.core.optasports.com/soccer/teams/150x150/"+team_A_id+".png";


                    JSONObject team_B = new JSONObject(row.getString("team_B"));
                    String team_B_name = team_B.getString("name");
                    String team_B_id = team_B.getString("id");
                    String Thumb_B = "http://cache.images.core.optasports.com/soccer/teams/150x150/"+team_B_id+".png";




                    Timestamp timestamp = Timestamp.valueOf(row.getString("date_time_utc"));
                    long timeParameter = timestamp.getTime() + 10800000;


                    Calendar cal = Calendar.getInstance();
                    TimeZone tz = cal.getTimeZone();
                    Log.d("Time zone: ", tz.getDisplayName());
                    SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
                    format.setTimeZone(tz);
                    Long NewTimestamp = timeParameter;


                    String NewGroupDate = format.format(new Date(NewTimestamp));

                    if (NewGroupDate.equals(GroupDate)) {
                        matches.put(MATCH_KEY_HEADER, "0");
                    } else {
                        matches.put(MATCH_KEY_HEADER, "1");
                    }

                    GroupDate = NewGroupDate;


                    android.util.Log.e("FORMATTEDDAY", GroupDate);





                    matches.put(MATCH_KEY_ID, row.getString("id"));
                    matches.put(MATCH_KEY_TEAMA, team_A_name);
                    matches.put(MATCH_KEY_TEAMB, team_B_name);
                    matches.put(MATCH_KEY_DATE, String.valueOf(timeParameter));
                    matches.put(MATCH_KEY_TIME, "00");
                    matches.put(MATCH_KEY_THUMBA, Thumb_A);
                    matches.put(MATCH_KEY_THUMBB, Thumb_B);
                    matches.put(MATCH_KEY_STATUS, row.getString("status"));
                    matches.put(MATCH_KEY_LEAGUE, leagueNameShared);
                    matches.put(MATCH_KEY_SCORE1, row.optString("fts_A"));
                    matches.put(MATCH_KEY_SCORE2, row.optString("fts_B"));



                    ScheduleList.add(matches);


                }


                LeagueWeeksAdapter = new LeagueWeeksAdapter(ScheduleList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                Schedulelistview.setLayoutManager(mLayoutManager);
                Schedulelistview.setItemAnimator(new DefaultItemAnimator());


                Schedulelistview.setAdapter(LeagueWeeksAdapter);


                Schedulelistview.setVisibility(View.VISIBLE);
                ScheduleLoader.setVisibility(View.GONE);
                noContent.setVisibility(View.GONE);


                Schedulelistview.addOnItemTouchListener(
                        new RecyclerItemClickListener(context, Schedulelistview, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                HashMap<String, String> match = ScheduleList.get(position);
                                Bundle bundle = new Bundle();
                                bundle.putString("ref", match.get(MATCH_KEY_ID));


                                Intent intent = new Intent(getActivity(),
                                        MatchDetailsActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);

                                if (mInterstitialAd.isLoaded()) {
                                    mInterstitialAd.show();
                                } else {
                                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                                }


                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        })
                );


                ScheduleLoader.setVisibility(View.GONE);
                noContent.setVisibility(View.GONE);
                Schedulelistview.setVisibility(View.VISIBLE);


                ScheduleLoading = false;
                mSwipeRefreshLayout.setRefreshing(false);


            } catch (JSONException e) {
                android.util.Log.e("INFO", e.getMessage());
                ScheduleLoader.setVisibility(View.GONE);
                Schedulelistview.setVisibility(View.GONE);
                noContent.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
            }


        }

    }

    @Override
    public void onResume() {
        super.onResume();

        // Tracking the screen view
        AnalyticsApp.getInstance().trackScreenView("League Schedule Screen");
    }


    @Override
    public void onStop() {
        super.onStop();




    }


    @Override
    public void onPause() {
        super.onPause();



    }

    @Override
    public void onDestroy() {
        super.onDestroy();



    }
}
