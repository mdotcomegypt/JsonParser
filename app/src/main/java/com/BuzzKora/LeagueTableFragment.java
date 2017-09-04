package com.BuzzKora;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class LeagueTableFragment extends Fragment {


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




    private class TableTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {
            return LeagueActivity.leagueTable;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected void onPostExecute(String result) {


            try {

                TableList.clear();



                JSONArray Tables = new JSONArray(result);







                for (int x = 0; x < Tables.length(); x++) {

                    JSONObject jsonData = Tables.getJSONObject(x);




                    JSONArray tableItem = jsonData.getJSONArray("table");

                    //android.util.Log.e("TABLES", String.valueOf(tableItem));










                    for (int i = 0; i < tableItem.length(); i++) {
                        JSONObject row = tableItem.getJSONObject(i);
                        HashMap<String, String> table = new HashMap<String, String>();


                        String teamObj = row.getString("team");
                        JSONObject TeamJsonData = new JSONObject(teamObj);
                        String teamName = TeamJsonData.getString("name");

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
