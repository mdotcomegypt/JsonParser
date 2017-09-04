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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.ViewGroup;

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


public class NewsFragment extends Fragment {


    String URL;
    JSONArray jsonData = null;

    ArrayList<HashMap<String, String>> NewsList = new ArrayList<HashMap<String, String>>();
    //ArrayList<HashMap<String, String>> MoreNewsList = new ArrayList<HashMap<String, String>>();

    static final String NEWS_KEY_TITLE = "title"; // parent node
    static final String  NEWS_KEY_THUMB = "thumb";
    static final String  NEWS_KEY_IMAGE = "image";
    static final String  NEWS_KEY_DATE = "date";
    static final String  NEWS_KEY_BODY = "body";
    static final String  NEWS_KEY_AUTHOR = "author";
    static final String  NEWS_KEY_URL = "url";
    static final String  NEWS_KEY_SOURCE = "source";

    NewsAdapter NewsAdapter;

    private View NewsLoader;

    private InterstitialAd mInterstitialAd;

    RecyclerView Newslistview;

    private boolean NewsLoading = true;

    View rootView;


    private RecyclerView recyclerView;
    private NewsListAdapter NewsListAdapter;

    SwipeRefreshLayout mSwipeRefreshLayout;

    private TextView noContent;


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.util.Log.e("LIFECYCLE", "onCreate");

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView( inflater,  container,
                 savedInstanceState);

        if (jsonData == null) {

            android.util.Log.e("LIFECYCLE", "onCreateView");

            rootView = inflater.inflate(R.layout.news, container, false);
            setHasOptionsMenu(true);

            mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swifeRefresh);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new NewsJsonTask().execute();
                }
            });


            mInterstitialAd = new InterstitialAd(getActivity());
            mInterstitialAd.setAdUnitId(getString(R.string.admob_banner_interstitial));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setImmersiveMode(true);

            noContent = (TextView) rootView.findViewById(R.id.no_content);


            new NewsJsonTask().execute();
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
                if (!NewsLoading){
                    NewsLoader.setVisibility(View.VISIBLE);
                    Newslistview.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Loading .. ", Toast.LENGTH_LONG).show();
                    new NewsJsonTask().execute();


                } else {
                    Toast.makeText(getActivity(), "Already Loading .. ", Toast.LENGTH_LONG).show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class NewsJsonTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {

            Calendar cal = Calendar.getInstance();
            TimeZone tz = cal.getTimeZone();
            SimpleDateFormat cashTimeFormat = new SimpleDateFormat("dMyHHmm");
            cashTimeFormat.setTimeZone(tz);
            String tzName = tz.getID();
            String cashTime = cashTimeFormat.format(new Date(System.currentTimeMillis())) + tzName;



            URL = Config.NEWS_SOURCE;
            String jsonStr = parser.getDataFromUrl(URL + cashTime );
            Log.e("URL", URL + cashTime);
            return jsonStr;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(String result) {


            try {
                jsonData = new JSONArray(result);

                NewsList.clear();


                final SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());


                for (int i = 0; i < jsonData.length(); i++) {
                    JSONObject row = jsonData.getJSONObject(i);


                    HashMap<String, String> MoreNews = null;
                    boolean Add = false;


                    String[] splitedKeys = row.getString("keys").split(",");
                    for (int x = 0; x < splitedKeys.length; x++) {


                        String key = splitedKeys[x];
                        boolean flag = SP.getBoolean(key, false);
                        if (flag) {

                            MoreNews = new HashMap<String, String>();

                            Calendar cal = Calendar.getInstance();
                            TimeZone tz = cal.getTimeZone();
                            Log.d("Time zone: ", tz.getDisplayName());
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                            sdf.setTimeZone(tz);
                            Long timestamp = Long.valueOf(row.getString("date"));
                            String localTime = sdf.format(new Date(timestamp * 1000));

                            MoreNews.put(NEWS_KEY_TITLE, row.getString("title"));
                            MoreNews.put(NEWS_KEY_THUMB, row.getString("thumb"));
                            MoreNews.put(NEWS_KEY_IMAGE, row.getString("image"));
                            MoreNews.put(NEWS_KEY_BODY, row.getString("body"));
                            MoreNews.put(NEWS_KEY_DATE, localTime);
                            MoreNews.put(NEWS_KEY_AUTHOR, row.getString("author"));
                            MoreNews.put(NEWS_KEY_URL, row.getString("url"));
                            MoreNews.put(NEWS_KEY_SOURCE, row.getString("source"));


                            Add = true;


                        }

                    }

                    if (Add) {

                        NewsList.add(MoreNews);
                    }

                }





                Newslistview = (RecyclerView) rootView.findViewById(R.id.Newslistview);

                NewsListAdapter = new NewsListAdapter(NewsList);
                int numOfCol = calculateNoOfColumns(getActivity().getApplicationContext());
                //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());int numOfCol = calculateNoOfColumns(getActivity().getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager =  new GridLayoutManager(getActivity().getApplicationContext(), numOfCol);
                Newslistview.setLayoutManager(mLayoutManager);
                Newslistview.setItemAnimator(new DefaultItemAnimator());
                Newslistview.setAdapter(NewsListAdapter);


                NewsLoader = rootView.findViewById(R.id.loader);
                NewsLoader.setVisibility(View.GONE);
                Newslistview.setVisibility(View.VISIBLE);


                Newslistview.addOnItemTouchListener(
                        new RecyclerItemClickListener(getActivity().getApplicationContext(), Newslistview ,new RecyclerItemClickListener.OnItemClickListener() {
                            @Override public void onItemClick(View view, final int position) {
                                //HashMap<String, String> article = NewsList.get(position);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("NewsList", NewsList);
                                bundle.putInt("position", position);

                                Intent intent = new Intent(getActivity(),
                                        ArticleActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);

                                if (mInterstitialAd.isLoaded()) {
                                    mInterstitialAd.show();
                                } else {
                                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                                }



                            }

                            @Override public void onLongItemClick(View view, int position) {
                               /* HashMap<String, String> article = MoreNewsList.get(position);
                                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                String shareBody = article.get(NewsFragment.NEWS_KEY_TITLE) + " " + article.get(NewsFragment.NEWS_KEY_URL) ;
                                String shareSub = article.get(NewsFragment.NEWS_KEY_TITLE);
                                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                                startActivity(Intent.createChooser(sharingIntent, "Share using"));*/
                            }
                        })
                );


                NewsLoading = false;

                mSwipeRefreshLayout.setRefreshing(false);


            } catch (JSONException e) {
                android.util.Log.e("INFO", "JSON was invalid");
                NewsLoader.setVisibility(View.GONE);
                Newslistview.setVisibility(View.GONE);
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
        AnalyticsApp.getInstance().trackScreenView("Home News Screen");
    }




}
