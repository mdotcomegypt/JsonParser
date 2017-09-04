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


public class MatchTableFragment extends Fragment {


    JSONArray jsonData = null;

    View rootView;

    SwipeRefreshLayout mSwipeRefreshLayout;

    ArrayList<HashMap<String, String>> TableList = new ArrayList<HashMap<String, String>>();

    RecyclerView Tablelistview;
    private MatchTableAdapter MatchTableAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.match_table_fragment, container, false);
        setHasOptionsMenu(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swifeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new TableTask().execute();
            }
        });


        new TableTask().execute();
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        AnalyticsApp.getInstance().trackScreenView("Match Table");
    }




    private class TableTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {
            return MatchDetailsActivity.matchTable;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(String result) {


            try {
                android.util.Log.e("TABLE RESULTS", result);
                jsonData = new JSONArray(result);


                TableList.clear();


                for (int i = 0; i < jsonData.length(); i++) {
                    JSONObject row = jsonData.getJSONObject(i);
                    HashMap<String, String> table = new HashMap<String, String>();


                    String teamObj = row.getString("team");
                    JSONObject TeamJsonData = new JSONObject(teamObj);
                    String teamName = TeamJsonData.getString("name");
                    String id = TeamJsonData.getString("id");

                    table.put("id", id);
                    table.put("rank", row.getString("rank"));
                    table.put("team", teamName);
                    table.put("played", row.getString("played"));
                    table.put("win", row.getString("win"));
                    table.put("draw", row.getString("draw"));
                    table.put("lost", row.getString("lost"));
                    table.put("pro", row.getString("pro"));
                    table.put("against", row.getString("against"));
                    table.put("pts", row.getString("pts"));


                    TableList.add(table);
                }

                Tablelistview = (RecyclerView) rootView.findViewById(R.id.TableListView);
                MatchTableAdapter = new MatchTableAdapter(TableList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                Tablelistview.setLayoutManager(mLayoutManager);
                Tablelistview.setItemAnimator(new DefaultItemAnimator());
                Tablelistview.setAdapter(MatchTableAdapter);
                Tablelistview.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);

            } catch (JSONException e) {
                android.util.Log.e("INFO", "JSON was invalid");
            }


        }


    }





}
