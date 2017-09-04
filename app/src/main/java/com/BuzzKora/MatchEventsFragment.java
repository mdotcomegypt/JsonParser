package com.BuzzKora;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.BuzzKora.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MatchEventsFragment extends Fragment {


    JSONArray jsonData = null;







    View rootView;



    SwipeRefreshLayout mSwipeRefreshLayout;

    ArrayList<HashMap<String, String>> EventList = new ArrayList<HashMap<String, String>>();

    RecyclerView Eventlistview;
    private MatchEventAdapter MatchEventAdapter;


    static final String  EVENT_TYPE = "type";
    static final String  EVENT_MIN = "min";
    static final String  EVENT_TEAM = "team";
    static final String  EVENT_PLAYER1 = "player1";
    static final String  EVENT_PLAYER2 = "player2";





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.match_events_fragment, container, false);
        setHasOptionsMenu(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swifeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new EventTask().execute();
            }
        });


        new EventTask().execute();
        return rootView;
    }



    @Override
    public void onResume() {
        super.onResume();
        AnalyticsApp.getInstance().trackScreenView("Match Events");
    }




    private class EventTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {
            return MatchDetailsActivity.matchEvents;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(String result) {


            try {
                android.util.Log.e("EVENTS RESULTS", result);
                jsonData = new JSONArray(result);


                EventList.clear();


                for (int i = 0; i < jsonData.length(); i++) {


                    JSONObject row = jsonData.getJSONObject(i);


                    HashMap<String, String> Event = new HashMap<String, String>();

                    Event.put(EVENT_TYPE, row.getString("type"));
                    Event.put(EVENT_MIN, row.getString("min"));
                    Event.put(EVENT_TEAM, row.getString("team"));
                    Event.put(EVENT_PLAYER1, row.getString("player1"));
                    Event.put(EVENT_PLAYER2, row.getString("player2"));

                    EventList.add(Event);
                }

                Eventlistview = (RecyclerView) rootView.findViewById(R.id.Eventlistview);
                MatchEventAdapter = new MatchEventAdapter(EventList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                Eventlistview.setLayoutManager(mLayoutManager);
                Eventlistview.setItemAnimator(new DefaultItemAnimator());
                Eventlistview.setAdapter(MatchEventAdapter);
                Eventlistview.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);

            } catch (JSONException e) {
                android.util.Log.e("INFO", "JSON was invalid");
            }


        }


    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 260;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }




}
