package com.BuzzKora;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BuzzKora.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MatchLineupFragment extends Fragment {


    JSONArray jsonDataA = null;
    JSONArray jsonDataB = null;


    static final String  LINEUP_NUMBER = "number";
    static final String  LINEUP_POSITION = "position";
    static final String  LINEUP_POSITION_AR = "position_ar";
    static final String  LINEUP_TEAM = "team";
    static final String  LINEUP_NAME = "name";
    static final String  LINEUP_IMAGE = "image";
    static final String  LINEUP_X = "x";
    static final String  LINEUP_Y = "y";

    TextView home_lineup;
    TextView away_lineup;
    Context context;







    View rootView;



    SwipeRefreshLayout mSwipeRefreshLayout;

    ArrayList<HashMap<String, String>> LineUpListA = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> LineUpListASub = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> LineUpListB = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> LineUpListBSub = new ArrayList<HashMap<String, String>>();

    RecyclerView LineuplistviewA;
    RecyclerView LineuplistviewASub;
    RecyclerView LineuplistviewB;
    RecyclerView LineuplistviewBSub;

    LinearLayout homeContainer;
    LinearLayout awayContainer;

    private MatchLineupAdapter MatchLineupAdapter;


    NestedScrollView lineUpHolder;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.match_lineup_fragment, container, false);
        setHasOptionsMenu(true);







        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swifeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LineUpTask().execute();
            }
        });


        new LineUpTask().execute();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        AnalyticsApp.getInstance().trackScreenView("Match Lineup");
    }




    private class LineUpTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {

            return MatchDetailsActivity.matchLineUPA;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(String result) {


            try {


                context = getActivity().getApplicationContext();


                final RelativeLayout homeField = (RelativeLayout) rootView.findViewById(R.id.homeField);
                final RelativeLayout awayField = (RelativeLayout) rootView.findViewById(R.id.awayField);

                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();


                float dpwidth = homeField.getWidth();
                float ratio = dpwidth / 100;
                float width = dpwidth/6 ;
                float index = dpwidth/12;

                int padding = (int) (width/20);

                double pitchRatio = 1.37;

                double newHeight = (displayMetrics.widthPixels / pitchRatio);

                android.util.Log.e("dpwidth", String.valueOf(dpwidth));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, displayMetrics.widthPixels);


                //params.setMargins((int) margin,(int) margin,(int) margin,(int) margin);



                String formationA = MatchDetailsActivity.matchFormationA;
                String formationB = MatchDetailsActivity.matchFormationB;

                String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
                Typeface tf = Typeface.createFromAsset(getContext().getAssets(), fontPath);


                String fontPathAnton = "fonts/en_font.ttf";
                Typeface tfAnton = Typeface.createFromAsset(getContext().getAssets(), fontPathAnton);


                home_lineup = (TextView) rootView.findViewById(R.id.home_lineup);
                home_lineup.setText(MatchDetailsActivity.TeamA);

                away_lineup = (TextView) rootView.findViewById(R.id.away_lineup);
                away_lineup.setText(MatchDetailsActivity.TeamB);

                homeContainer = (LinearLayout) rootView.findViewById(R.id.homeContainer);
                awayContainer = (LinearLayout) rootView.findViewById(R.id.awayContainer);

                lineUpHolder = (NestedScrollView) rootView.findViewById(R.id.lineUpHolder);

                showHome();

                home_lineup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showHome();

                    }
                });

                away_lineup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showAway();

                    }
                });

                TextView formationATXT = (TextView) rootView.findViewById(R.id.FormationA);
                formationATXT.setText(formationA);

                TextView formationBTXT = (TextView) rootView.findViewById(R.id.FormationB);
                formationBTXT.setText(formationB);


                /*TextView TeamATXT = (TextView) rootView.findViewById(R.id.teamA);
                TeamATXT.setText(MatchDetailsActivity.TeamA);

                TextView TeamBTXT = (TextView) rootView.findViewById(R.id.teamB);
                TeamBTXT.setText(MatchDetailsActivity.TeamB);*/


                TextView SubA = (TextView) rootView.findViewById(R.id.subtitleA);
                TextView SubB = (TextView) rootView.findViewById(R.id.subtitleB);



                home_lineup.setTypeface(tf);
                away_lineup.setTypeface(tf);
                SubA.setTypeface(tf);
                SubB.setTypeface(tf);



                jsonDataA = new JSONArray(MatchDetailsActivity.matchLineUPA);
                LineUpListA.clear();
                LineUpListASub.clear();


               
                for (int i = 0; i < jsonDataA.length(); i++) {
                    JSONObject row = jsonDataA.getJSONObject(i);
                    HashMap<String, String> player = new HashMap<String, String>();
                    player.put(LINEUP_NAME, row.getString("name"));
                    player.put(LINEUP_NUMBER, row.getString("number"));
                    player.put(LINEUP_IMAGE, row.getString("image"));
                    player.put(LINEUP_POSITION, row.getString("position"));
                    player.put(LINEUP_POSITION_AR, row.getString("position_AR"));
                    player.put(LINEUP_TEAM, row.getString("team"));
                    player.put(LINEUP_X, row.getString("x"));
                    player.put(LINEUP_Y, row.getString("y"));
                    if (row.getString("position").equals("Substitute")) {
                        LineUpListASub.add(player);
                    } else {
                        LineUpListA.add(player);
                    }
                }




                homeField.setLayoutParams(params);


                    for (int y = 0; y < LineUpListA.size() ; y++) {

                        if (!LineUpListA.get(y).get(LINEUP_X).equals("null")) {
                            TextView player = new TextView(getActivity().getApplicationContext());
                            player.setText(LineUpListA.get(y).get(LINEUP_NAME));
                            player.setTypeface(tf);
                            player.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            player.setWidth((int) width);
                            player.setMaxLines(1);
                            player.setTextSize(9);
                            player.setPadding(padding,padding,padding,padding);
                            player.setTextColor(context.getResources().getColor(R.color.playerTXT));
                            player.setBackground(context.getResources().getDrawable(R.color.playerBG));
                            player.setTranslationX(Integer.parseInt(LineUpListA.get(y).get(LINEUP_X)) * ratio - index);
                            player.setTranslationY(Integer.parseInt(LineUpListA.get(y).get(LINEUP_Y)) * (ratio));
                            homeField.addView(player);
                        }
                    }







                LineuplistviewA = (RecyclerView) rootView.findViewById(R.id.LineupListViewA);
                MatchLineupAdapter = new MatchLineupAdapter(LineUpListA);
                RecyclerView.LayoutManager LayoutManagerA = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                LineuplistviewA.setLayoutManager(LayoutManagerA);
                LineuplistviewA.setItemAnimator(new DefaultItemAnimator());
                LineuplistviewA.setAdapter(MatchLineupAdapter);
                LineuplistviewA.setVisibility(View.VISIBLE);

                LineuplistviewASub = (RecyclerView) rootView.findViewById(R.id.LineupListViewASub);
                MatchLineupAdapter = new MatchLineupAdapter(LineUpListASub);
                RecyclerView.LayoutManager LayoutManagerASub = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                LineuplistviewASub.setLayoutManager(LayoutManagerASub);
                LineuplistviewASub.setItemAnimator(new DefaultItemAnimator());
                LineuplistviewASub.setAdapter(MatchLineupAdapter);
                LineuplistviewASub.setVisibility(View.VISIBLE);







                jsonDataB = new JSONArray(MatchDetailsActivity.matchLineUPB);
                LineUpListB.clear();
                LineUpListBSub.clear();


                for (int i = 0; i < jsonDataB.length(); i++) {
                    JSONObject row = jsonDataB.getJSONObject(i);
                    HashMap<String, String> player = new HashMap<String, String>();
                    player.put(LINEUP_NAME, row.getString("name"));
                    player.put(LINEUP_NUMBER, row.getString("number"));
                    player.put(LINEUP_IMAGE, row.getString("image"));
                    player.put(LINEUP_POSITION, row.getString("position"));
                    player.put(LINEUP_POSITION_AR, row.getString("position_AR"));
                    player.put(LINEUP_TEAM, row.getString("team"));
                    player.put(LINEUP_X, row.getString("x"));
                    player.put(LINEUP_Y, row.getString("y"));
                    if (row.getString("position").equals("Substitute")) {
                        LineUpListBSub.add(player);
                    } else {
                        LineUpListB.add(player);
                    }
                }




                awayField.setLayoutParams(params);

                    for (int z = 0; z < LineUpListA.size(); z++) {

                        if (!LineUpListA.get(z).get(LINEUP_X).equals("null")) {
                            TextView player = new TextView(getActivity().getApplicationContext());
                            player.setText(LineUpListB.get(z).get(LINEUP_NAME));

                            player.setTypeface(tf);
                            player.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            player.setWidth((int) width);
                            player.setMaxLines(1);
                            player.setTextSize(9);
                            player.setPadding(padding,padding,padding,padding);
                            player.setTextColor(context.getResources().getColor(R.color.playerTXT));
                            player.setBackground(context.getResources().getDrawable(R.color.playerBG));
                            player.setTranslationX(Integer.parseInt(LineUpListB.get(z).get(LINEUP_X)) * ratio - index);
                            player.setTranslationY(Integer.parseInt(LineUpListB.get(z).get(LINEUP_Y)) * (ratio));
                            awayField.addView(player);
                        }
                    }





                LineuplistviewB = (RecyclerView) rootView.findViewById(R.id.LineupListViewB);
                MatchLineupAdapter = new MatchLineupAdapter(LineUpListB);
                RecyclerView.LayoutManager LayoutManagerB = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                LineuplistviewB.setLayoutManager(LayoutManagerB);
                LineuplistviewB.setItemAnimator(new DefaultItemAnimator());
                LineuplistviewB.setAdapter(MatchLineupAdapter);
                LineuplistviewB.setVisibility(View.VISIBLE);


                LineuplistviewBSub = (RecyclerView) rootView.findViewById(R.id.LineupListViewBSub);
                MatchLineupAdapter = new MatchLineupAdapter(LineUpListBSub);
                RecyclerView.LayoutManager LayoutManagerBSub = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                LineuplistviewBSub.setLayoutManager(LayoutManagerBSub);
                LineuplistviewBSub.setItemAnimator(new DefaultItemAnimator());
                LineuplistviewBSub.setAdapter(MatchLineupAdapter);
                LineuplistviewBSub.setVisibility(View.VISIBLE);





                mSwipeRefreshLayout.setRefreshing(false);

            } catch (JSONException e) {
                android.util.Log.e("INFO", e.getMessage());
            }


        }


    }

    void showHome(){
        homeContainer.setVisibility(View.VISIBLE);
        awayContainer.setVisibility(View.GONE);
        home_lineup.setTextColor(context.getResources().getColor(R.color.lineubTabTXTselected));
        home_lineup.setBackground(context.getResources().getDrawable(R.color.lineupTabBGselected));

        away_lineup.setTextColor(context.getResources().getColor(R.color.lineubTabTXT));
        away_lineup.setBackground(context.getResources().getDrawable(R.color.lineupTabBG));
        lineUpHolder.scrollTo(0,0);


    }

    void showAway(){
        homeContainer.setVisibility(View.GONE);
        awayContainer.setVisibility(View.VISIBLE);

        away_lineup.setTextColor(context.getResources().getColor(R.color.lineubTabTXTselected));
        away_lineup.setBackground(context.getResources().getDrawable(R.color.lineupTabBGselected));

        home_lineup.setTextColor(context.getResources().getColor(R.color.lineubTabTXT));
        home_lineup.setBackground(context.getResources().getDrawable(R.color.lineupTabBG));
        lineUpHolder.scrollTo(0,0);
    }





}
