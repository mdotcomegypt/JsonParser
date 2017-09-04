package com.BuzzKora;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerTitleStrip;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.BuzzKora.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavoritActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;


    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        PagerTitleStrip pagerTitle = (PagerTitleStrip) findViewById(R.id.pager_title_strip);

        String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        for (int counter = 0 ; counter<pagerTitle.getChildCount(); counter++) {

            if (pagerTitle.getChildAt(counter) instanceof TextView) {
                ((TextView)pagerTitle.getChildAt(counter)).setTypeface(tf);
                ((TextView)pagerTitle.getChildAt(counter)).setTextSize(14);
            }

        }

        TextView activityTitle = (TextView) findViewById(R.id.toolbar_title);
        activityTitle.setTypeface(tf);







    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Bundle bundle = new Bundle();
                bundle.putBoolean("updateUserData", true);
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtras(bundle);
                finish();
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }











    public static class FavoritFragment extends Fragment {

        private static final String ARG_KEY = "key";


        JSONArray jsonData = null;

        String URL = null;


        ArrayList<HashMap<String, String>> FavList = new ArrayList<HashMap<String, String>>();

        static final String FAV_NID = "nid"; // parent node
        static final String FAV_NAME = "name";
        static final String FAV_IMAGE = "image";
        static final String FAVE_KEY = "key";




        RecyclerView Favlistview;

        private boolean FavLoading = true;

        View rootView;


        private RecyclerView recyclerView;
        private FavoritAdapter FavoritAdapter;



        public FavoritFragment() {
        }


        public static FavoritFragment newInstance(String key) {
            FavoritFragment fragment = new FavoritFragment();
            Bundle args = new Bundle();
            args.putString(ARG_KEY, key);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_favorit, container, false);

            Favlistview = (RecyclerView) rootView.findViewById(R.id.Favoritlistview);



            URL = "http://90kora.com/fav-list.php?key=" + getArguments().getString(ARG_KEY);
            android.util.Log.e("URL", URL);
            new FavoritJsonTask().execute();
            return rootView;
        }


        private class FavoritJsonTask extends AsyncTask<String, Integer, String> {
            protected String doInBackground(String... urls) {
                String jsonStr = parser.getDataFromUrl(URL);
                return jsonStr;
            }

            protected void onProgressUpdate(Integer... progress) {

            }

            protected void onPostExecute(String result) {


                try {
                    jsonData = new JSONArray(result);

                    FavList.clear();


                    for (int i = 0; i < jsonData.length(); i++) {
                        JSONObject row = jsonData.getJSONObject(i);


                        HashMap<String, String> FavItem = new HashMap<String, String>();
                        FavItem.put(FAV_NID, row.getString("nid"));
                        FavItem.put(FAV_NAME, row.getString("name"));
                        FavItem.put(FAV_IMAGE, row.getString("image"));
                        FavItem.put(FAVE_KEY, row.getString("key"));


                        FavList.add(FavItem);
                    }


                    FavoritAdapter = new FavoritAdapter(FavList);
                    int numOfCol = calculateNoOfColumns(getActivity().getApplicationContext());
                    //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());int numOfCol = calculateNoOfColumns(getActivity().getApplicationContext());
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), numOfCol);
                    Favlistview.setLayoutManager(mLayoutManager);
                    Favlistview.setItemAnimator(new DefaultItemAnimator());
                    Favlistview.setAdapter(FavoritAdapter);


                    Favlistview.setVisibility(View.VISIBLE);





                    FavLoading = false;






                } catch (JSONException e) {
                    android.util.Log.e("INFO", "JSON was invalid");
                }


            }


        }


        public static int calculateNoOfColumns(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            int scalingFactor = 120;
            int noOfColumns = (int) (dpWidth / scalingFactor);
            return noOfColumns;
        }

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public  String[] FavKeys = getResources().getStringArray(R.array.favorit_array);
        public  String[] FavTitles = getResources().getStringArray(R.array.favorit_titles);

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FavoritFragment.newInstance(FavKeys[position]);
        }

        @Override
        public int getCount() {
            return  FavKeys.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
                return FavTitles[position];
        }
    }




    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("updateUserData", true);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(bundle);
        finish();
        startActivity(intent);
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsApp.getInstance().trackScreenView("Favorite Page");
    }
}
