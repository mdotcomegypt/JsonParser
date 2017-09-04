package com.BuzzKora;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.BuzzKora.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;


public class ScheduleFragment extends Fragment {


    public String DayTimeStamp;
    JSONArray jsonData = null;

    public Handler handler;
    public Timer timer;

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


    static final String CA_KEY_TIME = "timestamp";
    static final String CA_KEY_DISPLAY = "dispaly";


    private View ScheduleLoader;

    RecyclerView Schedulelistview;

    private boolean ScheduleLoading = true;

    private InterstitialAd mInterstitialAd;

    View rootView;


    private RecyclerView recyclerView;
    public ScheduleAdapter ScheduleAdapter;
    private CalendarAdapter CalendarAdapter;

    SwipeRefreshLayout mSwipeRefreshLayout;

    AsyncTask<String, Integer, String> runTask;

    private Long Start;

    private TextView noContent;

    Switch match_toggle;

    boolean filter = false;

    boolean MatchFlag;


    public int newPosition = 3;

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

        if (jsonData == null) {
            rootView = inflater.inflate(R.layout.schedule, container, false);
            setHasOptionsMenu(true);


            String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), fontPath);


            match_toggle = (Switch) rootView.findViewById(R.id.match_toggle);

            match_toggle.setTypeface(tf);

            final SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

            MatchFlag = SP.getBoolean("key_match_filter", false);


            if (MatchFlag) {
                match_toggle.setChecked(true);
                filter = true;
            } else {
                match_toggle.setChecked(false);
                filter = false;
            }


            match_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    SharedPreferences.Editor editor = SP.edit();

                    if (isChecked) {
                        filter = true;
                        editor.putBoolean("key_match_filter", true);
                    } else {
                        filter = false;
                        editor.putBoolean("key_match_filter", false);
                    }

                    editor.commit();




                    ScheduleAdapter = null;



                    new ScheduleJsonTask().execute();


                }
            });


            for (int i = 0; i < 20; i++) {


                Long Today = System.currentTimeMillis();

                Start = Today - (3 * 86400000);

                HashMap<String, String> Day = new HashMap<String, String>();

                Long DSLong = (Start + i * 86400000) / 1000;
                String DS = DSLong.toString();
                Day.put(CA_KEY_TIME, DS);
                Day.put(CA_KEY_DISPLAY, "normal");
                CalendarDays.add(Day);
            }

            context = getActivity().getApplicationContext();

            ScheduleLoader = rootView.findViewById(R.id.loader);
            noContent = (TextView) rootView.findViewById(R.id.no_content);
            Schedulelistview = (RecyclerView) rootView.findViewById(R.id.Schedulelistview);


            final LinearLayoutManager ClayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            CalendarAdapter = new CalendarAdapter(CalendarDays);

            final RecyclerView calendarList = (RecyclerView) rootView.findViewById(R.id.calendar);

            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(calendarList);
            calendarList.setOnFlingListener(snapHelper);

            calendarList.setLayoutManager(ClayoutManager);
            calendarList.setItemAnimator(new DefaultItemAnimator());
            calendarList.setAdapter(CalendarAdapter);
            CalendarAdapter.setSelecteditem(3);
            calendarList.smoothScrollToPosition(5);


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


                            if (timer != null) {


                                timer.cancel();
                                timer.purge();
                            }

                            if (runTask.getStatus() == AsyncTask.Status.RUNNING || runTask.getStatus() == AsyncTask.Status.PENDING) {
                                runTask.cancel(true);
                            }


                            ScheduleAdapter = null;


                            int centerOfScreen = calendarList.getWidth() / 2 - view.getWidth() / 2;
                            ClayoutManager.scrollToPositionWithOffset(position, centerOfScreen);
                            CalendarAdapter.setSelecteditem(position);


                            ScheduleLoader.setVisibility(View.VISIBLE);
                            Schedulelistview.setVisibility(View.GONE);
                            noContent.setVisibility(View.GONE);


                            runTimer(position);

                            newPosition = position;


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
        }


        return rootView;
    }


    public void runTimer(int pos) {


        android.util.Log.e("INFO", String.valueOf(pos));


        if (pos == 3) {


            handler = new Handler();
            timer = new Timer();

            final TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            try {
                                Long dfLong = System.currentTimeMillis() / 10000;
                                DayTimeStamp = dfLong.toString()+"0";
                                runTask = new ScheduleJsonTask().execute();
                            } catch (Exception e) {
                                // error, do something
                            }
                        }
                    });
                }
            };

            timer.schedule(task, 0, Config.MATCH_LIST_TIMER * 1000);
        } else {

            Long tsLong = (Start + pos * 86400000) / 10000;
            DayTimeStamp = tsLong.toString()+"0";
            new ScheduleJsonTask().execute();
            timer.cancel();
            timer.purge();

        }
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


            //String jsonStr = parser.getDataFromUrl(URL + ts);


            String jsonStr = parser.getDataFromUrl(Config.MATCH_BY_DATE_URL + DayTimeStamp + "&tz=" + tzName);

            android.util.Log.e("INFO", Config.MATCH_BY_DATE_URL + DayTimeStamp + "&tz=" + tzName);

            return jsonStr;
        }

        protected void onProgressUpdate(Integer... progress) {


        }

        protected void onPostExecute(String result) {


            try {

                String header;
                String oldleague = null;
                String league = null;

                jsonData = new JSONArray(result);

                ScheduleList.clear();


                final SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());








                for (int i = 0; i < jsonData.length(); i++) {
                    JSONObject row = jsonData.getJSONObject(i);

                    HashMap<String, String> matches = null;
                    boolean Add = false;





                    String[] splitedKeys = row.getString("keys").split(",");

                    int length;

                    if (filter) {
                        length = splitedKeys.length - 1;
                    } else {
                        length = splitedKeys.length;
                    }

                    for (int x = 0; x < length ; x++) {


                        String key = splitedKeys[x];
                        boolean flag = SP.getBoolean(key, false);
                        if (flag || !filter) {



                            league = row.getString("league_id");

                            header = "1";

                            if (league.equals(oldleague)){
                                header = "0";
                            }



                            matches = new HashMap<String, String>();
                            matches.put(MATCH_KEY_ID, row.getString("id"));
                            matches.put(MATCH_KEY_REF, row.getString("ref"));
                            matches.put(MATCH_KEY_TEAMA, row.getString("teama"));
                            matches.put(MATCH_KEY_TEAMB, row.getString("teamb"));
                            matches.put(MATCH_KEY_DATE, row.getString("date"));
                            matches.put(MATCH_KEY_TIME, row.getString("time"));
                            matches.put(MATCH_KEY_THUMBA, row.getString("thumb_url_a"));
                            matches.put(MATCH_KEY_THUMBB, row.getString("thumb_url_b"));
                            matches.put(MATCH_KEY_STATUS, row.getString("status"));
                            matches.put(MATCH_KEY_LEAGUE, row.getString("league"));
                            matches.put(MATCH_KEY_SCORE1, row.getString("score1"));
                            matches.put(MATCH_KEY_SCORE2, row.getString("score2"));
                            matches.put(MATCH_KEY_HEADER, header);







                            Add = true;






                        }

                    }


                    if (Add || !filter) {

                        ScheduleList.add(matches);
                        oldleague = league;
                    }


                }


                if (ScheduleList != null) {

                    if (ScheduleAdapter == null) {
                        Log.d("COUNT1", "initial");
                        Log.d("COUNTList", String.valueOf(ScheduleList.size()));


                        ScheduleAdapter = new ScheduleAdapter(ScheduleList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                        Schedulelistview.setLayoutManager(mLayoutManager);
                        Schedulelistview.setItemAnimator(new DefaultItemAnimator());


                        Schedulelistview.setAdapter(ScheduleAdapter);

                        Log.d("COUNT2", String.valueOf(ScheduleAdapter.getItemCount()));


                        Schedulelistview.setVisibility(View.VISIBLE);
                        ScheduleLoader.setVisibility(View.GONE);
                        noContent.setVisibility(View.GONE);


                        Schedulelistview.addOnItemTouchListener(
                                new RecyclerItemClickListener(context, Schedulelistview, new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, final int position) {
                                        HashMap<String, String> match = ScheduleList.get(position);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("ref", match.get(MATCH_KEY_REF));


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
                                /*HashMap<String, String> match = ScheduleList.get(position);
                                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                String shareBody = match.get(ScheduleFragment.MATCH_KEY_TITLE) + " " + match.get(ScheduleFragment.MATCH_KEY_URL) ;
                                String shareSub = match.get(ScheduleFragment.MATCH_KEY_TITLE);
                                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                                startActivity(Intent.createChooser(sharingIntent, "Share using"));*/
                                    }
                                })
                        );

                    } else {
                        Log.d("COUNT2", String.valueOf(ScheduleAdapter.getItemCount()));
                        Log.d("COUNTList", String.valueOf(ScheduleList.size()));

                        ScheduleAdapter.notifyDataSetChanged();

                    }

                    ScheduleLoader.setVisibility(View.GONE);
                    noContent.setVisibility(View.GONE);
                    Schedulelistview.setVisibility(View.VISIBLE);


                    ScheduleLoading = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                }


            } catch (JSONException e) {
                android.util.Log.e("INFO", e.getMessage());
                ScheduleLoader.setVisibility(View.GONE);
                Schedulelistview.setVisibility(View.GONE);
                noContent.setVisibility(View.VISIBLE);
            }


        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Tracking the screen view
        runTimer(newPosition);
        AnalyticsApp.getInstance().trackScreenView("Home Schedule Screen");
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (timer != null) {


            timer.cancel();
            timer.purge();
        }


    }
}
