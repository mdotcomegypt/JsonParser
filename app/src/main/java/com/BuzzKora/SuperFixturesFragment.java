package com.BuzzKora;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.HashMap;
import java.util.TimeZone;


public class SuperFixturesFragment extends Fragment {


    public String DayTimeStamp;
    JSONArray Matches = null;

    ArrayList<HashMap<String, String>> ScheduleList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> CalendarDays = new ArrayList<HashMap<String, String>>();

    static final String MATCH_KEY_ID = "id";
    static final String MATCH_KEY_TEAMA = "teama";
    static final String MATCH_KEY_TEAMB = "teamb";
    static final String MATCH_KEY_SCORE = "score";
    static final String MATCH_KEY_DATE = "date";
    static final String MATCH_KEY_LEAGUE = "competition";
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
    public SuperFixturesAdapter SuperFixturesAdapter;
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




            rootView = inflater.inflate(R.layout.schedule, container, false);
            setHasOptionsMenu(true);

            Switch match_toggle = (Switch) rootView.findViewById(R.id.match_toggle);
            match_toggle.setVisibility(View.GONE);




            context = getActivity().getApplicationContext();

            ScheduleLoader = rootView.findViewById(R.id.loader);
            noContent = (TextView) rootView.findViewById(R.id.no_content);
            Schedulelistview = (RecyclerView) rootView.findViewById(R.id.Schedulelistview);





            //default loading


            ScheduleLoader.setVisibility(View.VISIBLE);
            Schedulelistview.setVisibility(View.GONE);
            noContent.setVisibility(View.GONE);


            mInterstitialAd = new InterstitialAd(getActivity());
            mInterstitialAd.setAdUnitId(getString(R.string.admob_banner_interstitial));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setImmersiveMode(true);





            //runTimer(3);


            mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swifeRefresh);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new ScheduleJsonTask().execute();
                }
            });




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

            String jsonStr = SuperActivity.superFixtures;


            return jsonStr;
        }

        protected void onProgressUpdate(Integer... progress) {


        }

        protected void onPostExecute(String result) {


            try {

                android.util.Log.e("SUPERCONTENT", result);
                Matches = new JSONArray(result);

                ScheduleList.clear();





                for (int i = 0; i < Matches.length(); i++) {


                    JSONObject row = Matches.getJSONObject(i);


                    HashMap<String, String> matches = new HashMap<String, String>();





                    matches.put(MATCH_KEY_HEADER, "1");
                    matches.put(MATCH_KEY_ID, row.getString("ref"));
                    matches.put(MATCH_KEY_TEAMA, row.getString("teama"));
                    matches.put(MATCH_KEY_TEAMB, row.getString("teamb"));
                    matches.put(MATCH_KEY_DATE, row.getString("date") + "000");
                    matches.put(MATCH_KEY_TIME, "00");
                    matches.put(MATCH_KEY_THUMBA, row.getString("thumb_url_a"));
                    matches.put(MATCH_KEY_THUMBB, row.getString("thumb_url_b"));
                    matches.put(MATCH_KEY_STATUS, row.getString("status"));
                    matches.put(MATCH_KEY_LEAGUE, row.getString("league"));
                    matches.put(MATCH_KEY_SCORE1, row.optString("score1"));
                    matches.put(MATCH_KEY_SCORE2, row.optString("score2"));



                    ScheduleList.add(matches);


                }


                SuperFixturesAdapter = new SuperFixturesAdapter(ScheduleList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                Schedulelistview.setLayoutManager(mLayoutManager);
                Schedulelistview.setItemAnimator(new DefaultItemAnimator());


                Schedulelistview.setAdapter(SuperFixturesAdapter);


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
