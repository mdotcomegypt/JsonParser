package com.BuzzKora;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.BuzzKora.R;
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

import static com.BuzzKora.FavoritActivity.FavoritFragment.FAVE_KEY;


public class VideoFragment extends Fragment {


    JSONArray jsonData = null;

    ArrayList<HashMap<String, String>> VideoList = new ArrayList<HashMap<String, String>>();

    static final String VIDEO_KEY_TITLE = "title"; // parent node
    static final String VIDEO_KEY_THUMB = "thumb";
    static final String VIDEO_KEY_DATE = "date";
    static final String VIDEO_KEY_ID = "id";
    static final String VIDEO_KEY_MEDIA = "mp4";
    static final String VIDEO_KEY_URL = "url";
    static final String VIDEO_KEY_KEYS = "keys";


    public View VideoLoader;

    RecyclerView Videolistview;

    public boolean VideoLoading = true;

    private InterstitialAd mInterstitialAd;

    SwipeRefreshLayout mSwipeRefreshLayout;

    public TextView noContent;

    View rootView;


    private RecyclerView recyclerView;
    private VideoListAdapter VideoListAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView( inflater,  container,
                savedInstanceState);

        if (jsonData == null) {


            rootView = inflater.inflate(R.layout.video, container, false);
            setHasOptionsMenu(true);

            mInterstitialAd = new InterstitialAd(getActivity());
            mInterstitialAd.setAdUnitId(getString(R.string.admob_banner_interstitial));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setImmersiveMode(true);

            VideoLoader = rootView.findViewById(R.id.loader);
            noContent = (TextView) rootView.findViewById(R.id.no_content);
            Videolistview = (RecyclerView) rootView.findViewById(R.id.Videolistview);


            mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swifeRefresh);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new VideoJsonTask().execute();
                }
            });


            new VideoJsonTask().execute();
        }
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.refresh_menu, menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.refresh:
                if (!VideoLoading) {
                    VideoLoader.setVisibility(View.VISIBLE);
                    Videolistview.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Loading .. ", Toast.LENGTH_LONG).show();
                    new VideoJsonTask().execute();


                } else {
                    Toast.makeText(getActivity(), "Already Loading .. ", Toast.LENGTH_LONG).show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class VideoJsonTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {
            Calendar cal = Calendar.getInstance();
            TimeZone tz = cal.getTimeZone();
            SimpleDateFormat cashTimeFormat = new SimpleDateFormat("dMyHHmm");
            cashTimeFormat.setTimeZone(tz);
            String tzName = tz.getID();
            String cashTime = cashTimeFormat.format(new Date(System.currentTimeMillis())) + tzName;
            String jsonStr = parser.getDataFromUrl(Config.VIDEO_SOURCE + cashTime);
            return jsonStr;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(String result) {


            try {
                jsonData = new JSONArray(result);

                VideoList.clear();

                final SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());


                for (int i = 0; i < jsonData.length(); i++) {
                    JSONObject row = jsonData.getJSONObject(i);

                    HashMap<String, String> Videos = null;
                    boolean Add = false;


                    String[] splitedKeys = row.getString("keys").split(",");
                    for (int x = 0; x < splitedKeys.length; x++) {


                        String key = splitedKeys[x];
                        boolean flag = SP.getBoolean(key, false);
                        if (flag) {

                            Videos = new HashMap<String, String>();


                            Calendar cal = Calendar.getInstance();
                            TimeZone tz = cal.getTimeZone();
                            Log.d("Time zone: ", tz.getDisplayName());
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm  a");
                            sdf.setTimeZone(tz);
                            Long timestamp = Long.valueOf(row.getString("date"));
                            String localTime = sdf.format(new Date(timestamp * 1000));



                            Videos.put(VIDEO_KEY_TITLE, row.getString("title"));
                            Videos.put(VIDEO_KEY_THUMB, row.getString("thumb"));
                            Videos.put(VIDEO_KEY_DATE, localTime);
                            Videos.put(VIDEO_KEY_MEDIA, row.getString("mp4"));
                            Videos.put(VIDEO_KEY_ID, row.getString("id"));
                            Videos.put(VIDEO_KEY_URL, row.getString("url"));

                            android.util.Log.e("VALID", row.getString("title"));

                            Add = true;


                        }

                    }

                    if (Add) {

                        VideoList.add(Videos);
                    }


                }




                VideoListAdapter = new VideoListAdapter(VideoList);
                int numOfCol = calculateNoOfColumns(getActivity().getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), numOfCol);
                Videolistview.setLayoutManager(mLayoutManager);
                Videolistview.setItemAnimator(new DefaultItemAnimator());
                Videolistview.setAdapter(VideoListAdapter);




                Videolistview.addOnItemTouchListener(
                        new RecyclerItemClickListener(getActivity().getApplicationContext(), Videolistview, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                //HashMap<String, String> video = VideoList.get(position);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("Video", VideoList);
                                bundle.putInt("position", position);

                                // launch List activity
                                Intent intent = new Intent(getActivity(),
                                        VideoActivity.class);
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
                               /* HashMap<String, String> video = VideoList.get(position);
                                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                String shareBody = video.get(VideoFragment.VIDEO_KEY_TITLE) + " " + video.get(VideoFragment.VIDEO_KEY_URL) ;
                                String shareSub = video.get(VideoFragment.VIDEO_KEY_TITLE);
                                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                                startActivity(Intent.createChooser(sharingIntent, "Share using"));*/
                            }
                        })
                );


                VideoLoader.setVisibility(View.GONE);
                noContent.setVisibility(View.GONE);
                Videolistview.setVisibility(View.VISIBLE);




                VideoLoading = false;
                mSwipeRefreshLayout.setRefreshing(false);


            } catch (JSONException e) {
                android.util.Log.e("INFO", "JSON was invalid");
                VideoLoader.setVisibility(View.GONE);
                Videolistview.setVisibility(View.GONE);
                noContent.setVisibility(View.VISIBLE);
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


    @Override
    public void onResume() {
        super.onResume();

        // Tracking the screen view
        AnalyticsApp.getInstance().trackScreenView("Home Video Screen");
    }


}
