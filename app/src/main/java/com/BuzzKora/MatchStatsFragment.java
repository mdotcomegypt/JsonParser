package com.BuzzKora;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.BuzzKora.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MatchStatsFragment extends Fragment {


    JSONArray jsonData = null;


    static final String  STATS_TYPE = "type";
    static final String  STATS_VALUE_A = "team_A_value";
    static final String  STATS_VALUE_B = "team_B_value";









    View rootView;



    SwipeRefreshLayout mSwipeRefreshLayout;

    ArrayList<HashMap<String, String>> StatsList = new ArrayList<HashMap<String, String>>();

    RecyclerView Statslistview;

    private MatchStatsAdapter MatchStatsAdapter;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.match_stats_fragment, container, false);
        setHasOptionsMenu(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swifeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new StatsTask().execute();
            }
        });


        new StatsTask().execute();
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        AnalyticsApp.getInstance().trackScreenView("Match Stats");
    }




    private class StatsTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {

            return MatchDetailsActivity.matchStats;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(String result) {


            try {



                jsonData = new JSONArray(result);
                StatsList.clear();


                for (int i = 0; i < jsonData.length(); i++) {
                    JSONObject row = jsonData.getJSONObject(i);
                    HashMap<String, String> stat = new HashMap<String, String>();
                    stat.put(STATS_TYPE, row.getString("type"));
                    stat.put(STATS_VALUE_A, row.getString("team_A_value"));
                    stat.put(STATS_VALUE_B, row.getString("team_B_value"));


                    StatsList.add(stat);
                }


                Statslistview = (RecyclerView) rootView.findViewById(R.id.StatsListView);
                MatchStatsAdapter = new MatchStatsAdapter(StatsList);
                RecyclerView.LayoutManager LayoutManagerS = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                Statslistview.setLayoutManager(LayoutManagerS);
                Statslistview.setItemAnimator(new DefaultItemAnimator());
                Statslistview.setAdapter(MatchStatsAdapter);
                Statslistview.setVisibility(View.VISIBLE);


                mSwipeRefreshLayout.setRefreshing(false);

            } catch (JSONException e) {
                android.util.Log.e("INFO", e.getMessage());
            }


        }


    }





}
