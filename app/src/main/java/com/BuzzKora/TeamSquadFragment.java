package com.BuzzKora;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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


public class TeamSquadFragment extends Fragment {


    public String DayTimeStamp;
    JSONArray Squad = null;

    ArrayList<HashMap<String, String>> SquadList = new ArrayList<HashMap<String, String>>();

    static final String PLAYER_KEY_ID = "id";
    static final String PLAYER_KEY_NAME = "name";
    static final String PLAYER_KEY_POSITION = "position";
    static final String PLAYER_KEY_AGE = "age";
    static final String PLAYER_KEY_APPEARANCES = "appearances";
    static final String PLAYER_KEY_IMAGE = "image";
    static final String PLAYER_KEY_GOALS = "goals";
    static final String PLAYER_KEY_NUMBER = "number";


    private View SquadLoader;

    RecyclerView Squadlistview;

    private boolean SquadLoading = true;

    private InterstitialAd mInterstitialAd;

    View rootView;


    private RecyclerView recyclerView;
    public TeamSquadAdapter TeamSquadAdapter;

    SwipeRefreshLayout mSwipeRefreshLayout;

    AsyncTask<String, Integer, String> runTask;

    private Long Start;

    private TextView noContent;



    Context context;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container,
                savedInstanceState);




            rootView = inflater.inflate(R.layout.team_squad_fragment, container, false);
            setHasOptionsMenu(true);



            context = getActivity().getApplicationContext();

            SquadLoader = rootView.findViewById(R.id.loader);
            noContent = (TextView) rootView.findViewById(R.id.no_content);
            Squadlistview = (RecyclerView) rootView.findViewById(R.id.SquadListView);





            //default loading


            SquadLoader.setVisibility(View.VISIBLE);
            Squadlistview.setVisibility(View.GONE);
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
                    new SquadJsonTask().execute();
                }
            });




        new SquadJsonTask().execute();



        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.refresh_menu, menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.refresh:
                if (!SquadLoading) {
                    SquadLoader.setVisibility(View.VISIBLE);
                    Squadlistview.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Loading .. ", Toast.LENGTH_LONG).show();
                    new SquadJsonTask().execute();


                } else {
                    Toast.makeText(getActivity(), "Already Loading .. ", Toast.LENGTH_LONG).show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class SquadJsonTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {

            String jsonStr = TeamActivity.teamSquad;


            return jsonStr;
        }

        protected void onProgressUpdate(Integer... progress) {


        }

        protected void onPostExecute(String result) {


            try {
                Squad = new JSONArray(result);

                SquadList.clear();





                for (int i = 0; i < Squad.length(); i++) {


                    JSONObject row = Squad.getJSONObject(i);

                    if(row.has("player")) {


                        HashMap<String, String> player = new HashMap<String, String>();


                        JSONObject playerData = new JSONObject(row.getString("player"));

                        JSONArray playerPosition = new JSONArray(row.getString("position"));


                        String image = "http://cache.images.core.optasports.com/soccer/players/150x150/" + playerData.getString("id") + ".png";


                        player.put(PLAYER_KEY_ID, playerData.getString("id"));
                        player.put(PLAYER_KEY_NAME, playerData.getString("name"));
                        player.put(PLAYER_KEY_IMAGE, image);
                        player.put(PLAYER_KEY_AGE, row.getString("age"));
                        player.put(PLAYER_KEY_APPEARANCES, row.getString("appearances"));
                        player.put(PLAYER_KEY_NUMBER, row.getString("number"));
                        player.put(PLAYER_KEY_POSITION, playerPosition.getString(0));
                        player.put(PLAYER_KEY_GOALS, row.getString("goals"));


                        SquadList.add(player);
                    }


                }


                TeamSquadAdapter = new TeamSquadAdapter(SquadList);
                int numOfCol = calculateNoOfColumns(getActivity().getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), numOfCol);
                Squadlistview.setLayoutManager(mLayoutManager);
                Squadlistview.setItemAnimator(new DefaultItemAnimator());


                Squadlistview.setAdapter(TeamSquadAdapter);


                Squadlistview.setVisibility(View.VISIBLE);
                SquadLoader.setVisibility(View.GONE);
                noContent.setVisibility(View.GONE);


                Squadlistview.addOnItemTouchListener(
                        new RecyclerItemClickListener(context, Squadlistview, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                               /* HashMap<String, String> player = SquadList.get(position);
                                Bundle bundle = new Bundle();
                                bundle.putString("pid", player.get(PLAYER_KEY_ID));


                                Intent intent = new Intent(getActivity(),
                                        MatchDetailsActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);

                                if (mInterstitialAd.isLoaded()) {
                                    mInterstitialAd.show();
                                } else {
                                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                                }*/


                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        })
                );


                SquadLoader.setVisibility(View.GONE);
                noContent.setVisibility(View.GONE);
                Squadlistview.setVisibility(View.VISIBLE);


                SquadLoading = false;
                mSwipeRefreshLayout.setRefreshing(false);


            } catch (JSONException e) {
                android.util.Log.e("INFO", e.getMessage());
                SquadLoader.setVisibility(View.GONE);
                Squadlistview.setVisibility(View.GONE);
                noContent.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
            }


        }

    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Tracking the screen view
        AnalyticsApp.getInstance().trackScreenView("Team Squad Screen");
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
