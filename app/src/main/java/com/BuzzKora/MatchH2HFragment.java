package com.BuzzKora;

import android.graphics.Typeface;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.BuzzKora.MatchDetailsActivity.TeamA;
import static com.BuzzKora.MatchDetailsActivity.TeamB;
import static com.BuzzKora.MatchDetailsActivity.matchformAM;
import static com.BuzzKora.MatchDetailsActivity.matchformBM;
import static com.BuzzKora.MatchDetailsActivity.matchh2hMatch;
import static com.BuzzKora.MatchDetailsActivity.matchh2hStats;
import static java.lang.Integer.*;


public class MatchH2HFragment extends Fragment {


    JSONArray MatchJsonData = null;
    JSONArray MatchTAJsonData = null;
    JSONArray MatchTBJsonData = null;
    JSONObject StatsJsonData = null;



    static final String H2H_MATCH_SATAUS = "startus";
    static final String H2H_MATCH_DATE = "date";
    static final String H2H_MATCH_ID = "id";
    static final String H2H_MATCH_LEAGUE = "league";
    static final String H2H_MATCH_TEAMA = "team_a";
    static final String H2H_MATCH_TEAMB = "team_b";
    static final String H2H_MATCH_SCOREA = "score_a";
    static final String H2H_MATCH_SCOREB = "score_b";
    static final String H2H_MATCH_THUMBA = "thumb_A";
    static final String H2H_MATCH_THUMBB = "thumb_B";


    static final String  STATS_TYPE = "type";
    static final String  STATS_VALUE_A = "team_A_value";
    static final String  STATS_VALUE_B = "team_B_value";

    ArrayList<HashMap<String, String>> StatsList = new ArrayList<HashMap<String, String>>();

    RecyclerView Statslistview;

    private MatchStatsAdapter MatchStatsAdapter;


    View rootView;


    SwipeRefreshLayout mSwipeRefreshLayout;

    ArrayList<HashMap<String, String>> H2HList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> TAList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> TBList = new ArrayList<HashMap<String, String>>();

    RecyclerView H2Hlistview;
    RecyclerView TAlistview;
    RecyclerView TBlistview;

    private MatchH2HAdapter MatchH2HAdapter;
    private MatchH2HAdapter MatchTAAdapter;
    private MatchH2HAdapter MatchTBAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.match_h2h_fragment, container, false);
        setHasOptionsMenu(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swifeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new H2HTask().execute();
            }
        });


        new H2HTask().execute();
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        AnalyticsApp.getInstance().trackScreenView("Match H2H");
    }


    private class H2HTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {

            return matchh2hMatch;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(String result) {


            try {


                StatsJsonData = new JSONObject(matchh2hStats);


                String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
                Typeface tf = Typeface.createFromAsset(getContext().getAssets(), fontPath);

                String fontPathAnton = "fonts/en_font_bold.ttf";
                Typeface tfAnton = Typeface.createFromAsset(getContext().getAssets(), fontPathAnton);


                String team_a_win = StatsJsonData.getString("team_A_win");
                String team_a_goals = StatsJsonData.getString("team_A_goals");
                String team_b_win = StatsJsonData.getString("team_B_win");
                String team_b_goals = StatsJsonData.getString("team_B_goals");
                String draw = StatsJsonData.getString("draw");
                String total = StatsJsonData.getString("total");
                String team_a_win_prec = StatsJsonData.getString("team_A_win_prec");
                String team_b_win_prec = StatsJsonData.getString("team_B_win_prec");
                String draw_prec = StatsJsonData.getString("draw_prec");


                TextView win_a_txt = (TextView) rootView.findViewById(R.id.win_a_txt);
                TextView win_b_txt = (TextView) rootView.findViewById(R.id.win_b_txt);
                TextView win_a_num = (TextView) rootView.findViewById(R.id.win_a_num);
                TextView win_b_num = (TextView) rootView.findViewById(R.id.win_b_num);
                TextView draw_txt = (TextView) rootView.findViewById(R.id.draw_txt);
                TextView draw_num = (TextView) rootView.findViewById(R.id.draw_num);

                TextView win_a_conc = (TextView) rootView.findViewById(R.id.win_a_conc);
                TextView win_b_conc = (TextView) rootView.findViewById(R.id.win_b_conc);
                TextView draw_conc = (TextView) rootView.findViewById(R.id.draw_conc);

                ProgressBar win_a_progress = (ProgressBar) rootView.findViewById(R.id.win_a_progress) ;
                ProgressBar win_b_progress = (ProgressBar) rootView.findViewById(R.id.win_b_progress) ;
                ProgressBar draw_progress = (ProgressBar) rootView.findViewById(R.id.draw_progress) ;

                win_a_txt.setText(team_a_win + " / " + total);
                win_b_txt.setText(team_b_win + " / " + total);
                draw_txt.setText(draw + " / " + total);

                win_a_txt.setTypeface(tfAnton);
                win_b_txt.setTypeface(tfAnton);
                draw_txt.setTypeface(tfAnton);


                win_a_num.setText(team_a_win_prec + "%" );
                win_b_num.setText(team_b_win_prec + "%" );
                draw_num.setText(draw_prec + "%" );

                win_a_num.setTypeface(tfAnton);
                win_b_num.setTypeface(tfAnton);
                draw_num.setTypeface(tfAnton);

                win_a_conc.setText(getString(R.string.team_win) + " " + TeamA );
                win_b_conc.setText(getString(R.string.team_win) + " " +TeamB );
                draw_conc.setText(getString(R.string.team_draw));

                win_a_conc.setTypeface(tf);
                win_b_conc.setTypeface(tf);
                draw_conc.setTypeface(tf);


                win_a_progress.setProgress((int) Float.parseFloat(team_a_win_prec));
                win_b_progress.setProgress((int) Float.parseFloat(team_b_win_prec));
                draw_progress.setProgress((int) Float.parseFloat(draw_prec));











                /*StatsJsonData = new JSONArray(matchh2hStats);

                StatsList.clear();


                for (int i = 0; i < StatsJsonData.length(); i++) {
                    JSONObject row = StatsJsonData.getJSONObject(i);
                    HashMap<String, String> stat = new HashMap<String, String>();
                    stat.put(STATS_TYPE, row.getString("type"));
                    stat.put(STATS_VALUE_A, row.getString("team_A_value"));
                    stat.put(STATS_VALUE_B, row.getString("team_B_value"));


                    StatsList.add(stat);
                }


                Statslistview = (RecyclerView) rootView.findViewById(R.id.h2hStatsListView);
                MatchStatsAdapter = new MatchStatsAdapter(StatsList);
                RecyclerView.LayoutManager LayoutManagerStats = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                Statslistview.setLayoutManager(LayoutManagerStats);
                Statslistview.setItemAnimator(new DefaultItemAnimator());
                Statslistview.setAdapter(MatchStatsAdapter);
                Statslistview.setVisibility(View.VISIBLE);


                TextView team_a = (TextView) rootView.findViewById(R.id.team_a);
                TextView team_b = (TextView) rootView.findViewById(R.id.team_b);
                TextView draw = (TextView) rootView.findViewById(R.id.draw);
                TextView goal_a = (TextView) rootView.findViewById(R.id.goals_a);
                TextView goal_b = (TextView) rootView.findViewById(R.id.goals_b);


                team_a.setText(StatsJsonData.getString("team_A_win"));
                team_b.setText(StatsJsonData.getString("team_B_win"));
                draw.setText(StatsJsonData.getString("draw"));
                goal_a.setText(StatsJsonData.getString("team_A_goals"));
                goal_b.setText(StatsJsonData.getString("team_B_goals"));*/


                H2HList.clear();

                MatchJsonData = new JSONArray(matchh2hMatch);


                for (int i = 0; i < MatchJsonData.length(); i++) {
                    JSONObject row = MatchJsonData.getJSONObject(i);
                    HashMap<String, String> match = new HashMap<String, String>();
                    match.put(H2H_MATCH_ID, row.getString("id"));
                    match.put(H2H_MATCH_DATE, row.getString("date"));
                    match.put(H2H_MATCH_SATAUS, row.getString("status"));
                    //match.put(H2H_MATCH_LEAGUE, row.getString("league"));
                    match.put(H2H_MATCH_TEAMA, row.getString("team_A"));
                    match.put(H2H_MATCH_THUMBA, row.getString("thumb_A"));
                    match.put(H2H_MATCH_TEAMB, row.getString("team_B"));
                    match.put(H2H_MATCH_THUMBB, row.getString("thumb_B"));
                    match.put(H2H_MATCH_SCOREA, row.getString("score_A"));
                    match.put(H2H_MATCH_SCOREB, row.getString("score_B"));


                    H2HList.add(match);
                }


                int numberOfMatches = H2HList.size();








                H2Hlistview = (RecyclerView) rootView.findViewById(R.id.h2hMatchesListView);
                MatchH2HAdapter = new MatchH2HAdapter(H2HList);
                RecyclerView.LayoutManager LayoutManagerS = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                H2Hlistview.setLayoutManager(LayoutManagerS);
                H2Hlistview.setItemAnimator(new DefaultItemAnimator());
                H2Hlistview.setAdapter(MatchH2HAdapter);
                H2Hlistview.setVisibility(View.VISIBLE);




                TAList.clear();

                MatchTAJsonData = new JSONArray(matchformAM);


                for (int i = 0; i < MatchTAJsonData.length(); i++) {
                    JSONObject row = MatchTAJsonData.getJSONObject(i);
                    HashMap<String, String> match = new HashMap<String, String>();
                    match.put(H2H_MATCH_ID, row.getString("id"));
                    match.put(H2H_MATCH_DATE, row.getString("date"));
                    match.put(H2H_MATCH_SATAUS, row.getString("status"));
                    //match.put(H2H_MATCH_LEAGUE, row.getString("league"));
                    match.put(H2H_MATCH_TEAMA, row.getString("team_A"));
                    match.put(H2H_MATCH_THUMBA, row.getString("thumb_A"));
                    match.put(H2H_MATCH_TEAMB, row.getString("team_B"));
                    match.put(H2H_MATCH_THUMBB, row.getString("thumb_B"));
                    match.put(H2H_MATCH_SCOREA, row.getString("score_A"));
                    match.put(H2H_MATCH_SCOREB, row.getString("score_B"));


                    TAList.add(match);
                }


                TAlistview = (RecyclerView) rootView.findViewById(R.id.TAStatsListView);
                MatchTAAdapter = new MatchH2HAdapter(TAList);
                RecyclerView.LayoutManager LayoutManagerTA = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                TAlistview.setLayoutManager(LayoutManagerTA);
                TAlistview.setItemAnimator(new DefaultItemAnimator());
                TAlistview.setAdapter(MatchTAAdapter);
                TAlistview.setVisibility(View.VISIBLE);




                TBList.clear();

                MatchTBJsonData = new JSONArray(matchformBM);


                for (int i = 0; i < MatchTBJsonData.length(); i++) {
                    JSONObject row = MatchTBJsonData.getJSONObject(i);
                    HashMap<String, String> match = new HashMap<String, String>();
                    match.put(H2H_MATCH_ID, row.getString("id"));
                    match.put(H2H_MATCH_DATE, row.getString("date"));
                    match.put(H2H_MATCH_SATAUS, row.getString("status"));
                    //match.put(H2H_MATCH_LEAGUE, row.getString("league"));
                    match.put(H2H_MATCH_TEAMA, row.getString("team_A"));
                    match.put(H2H_MATCH_THUMBA, row.getString("thumb_A"));
                    match.put(H2H_MATCH_TEAMB, row.getString("team_B"));
                    match.put(H2H_MATCH_THUMBB, row.getString("thumb_B"));
                    match.put(H2H_MATCH_SCOREA, row.getString("score_A"));
                    match.put(H2H_MATCH_SCOREB, row.getString("score_B"));


                    TBList.add(match);
                }


                TBlistview = (RecyclerView) rootView.findViewById(R.id.TBStatsListView);
                MatchTBAdapter = new MatchH2HAdapter(TBList);
                RecyclerView.LayoutManager LayoutManagerTB = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                TBlistview.setLayoutManager(LayoutManagerTB);
                TBlistview.setItemAnimator(new DefaultItemAnimator());
                TBlistview.setAdapter(MatchTBAdapter);
                TBlistview.setVisibility(View.VISIBLE);


                TextView history_chart_title = (TextView) rootView.findViewById(R.id.history_chart_title);
                TextView teama_match_title = (TextView) rootView.findViewById(R.id.teama_match_title);
                TextView teamb_match_title = (TextView) rootView.findViewById(R.id.teamb_match_title);


                String history_chart_title_txt = getString(R.string.history_match_title_p1) + " " + total + " " + getString(R.string.history_match_title_p2);
                String history_teama_title_txt = getString(R.string.history_team_p1) + " " + String.valueOf(numberOfMatches) + " " + getString(R.string.history_team_p2)  + " " + TeamA;
                String history_teamb_title_txt = getString(R.string.history_team_p1) + " " + String.valueOf(numberOfMatches) + " " + getString(R.string.history_team_p2) + " " + TeamB;


                history_chart_title.setText(history_chart_title_txt);
                teama_match_title.setText(history_teama_title_txt);
                teamb_match_title.setText(history_teamb_title_txt);




                history_chart_title.setTypeface(tf);
                teama_match_title.setTypeface(tf);
                teamb_match_title.setTypeface(tf);






                mSwipeRefreshLayout.setRefreshing(false);

            } catch (JSONException e) {
                android.util.Log.e("INFO", e.getMessage());
            }


        }


    }




}
