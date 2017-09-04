package com.BuzzKora;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private ViewPagerAdapter ViewPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager ViewPager;

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private BroadcastReceiver mRegistrationBroadcastReceiver;


    Boolean isInternetPresent = false;
    ConnectionDetector cd;

    public String key_locale;

    String favs = "";
    String regId;


    private InterstitialAd mInterstitialAd;


    ArrayList<HashMap<String, String>> MenuList = new ArrayList<HashMap<String, String>>();

    static final String FAV_NID = "nid"; // parent node
    static final String FAV_NAME = "name";
    static final String FAV_IMAGE = "image";
    static final String FAVE_KEY = "key";

    private MenuAdapter MenuAdapter;
    RecyclerView Menulistview;


    private RecyclerView MainMenuRecyclerView;
    private MainMenuAdapter MainMenuAdapter;
    private RecyclerView.LayoutManager MainMenulayoutManager;
    String[] MenuArray;

    public static int MenuPos = 0;

    String countryCheck;

    ProgressBar Loader;

    Intent menuintent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(createBundleNoFragmentRestore(savedInstanceState));
        setContentView(R.layout.activity_main);

        android.util.Log.e("MainActivity", "Created");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);


        cd = new ConnectionDetector(MainActivity.this.getApplicationContext());

        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {

            final SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


            String Code = tm.getNetworkCountryIso().toUpperCase();

            Locale locale = new Locale("en", Code);

            String countryCode = locale.getISO3Country();

            countryCheck = SP.getString("key_country", countryCode);


            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            ViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            ViewPager = (ViewPager) findViewById(R.id.container);

            setupViewPager(ViewPager);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(ViewPager);

            ViewPager.setCurrentItem(2);


            Intent intent = getIntent();
            if (intent.hasExtra("updateUserData")) {

                if (!FirebaseInstanceId.getInstance().getToken().isEmpty()) {
                    Map<String, String> postData = new HashMap<>();

                    Map<String, ?> allEntries = SP.getAll();

                    for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                        favs = favs + "," + entry.getKey() + ":" + entry.getValue().toString();


                    }

                    android.util.Log.e("FAVS", favs);

                    regId = FirebaseInstanceId.getInstance().getToken();

                    new HttpPostAsyncTask().execute();
                }

            }


            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    // checking for type intent filter
                    if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {

                        FirebaseMessaging.getInstance().subscribeToTopic("key_master");


                    } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                        // new push notification is received

                        String message = intent.getStringExtra("message");

                        Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();


                    }
                }
            };


            String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
            Typeface font = Typeface.createFromAsset(getAssets(), fontPath);


            for (int counter = 0; counter < 3; counter++) {

                View headerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.tab_item, null, false);

                tabLayout.getTabAt(counter).setCustomView(headerView);
                TextView tabTitle = (TextView) headerView.findViewById(R.id.title);
                tabTitle.setTypeface(font);
                if (counter == 0) {
                    tabTitle.setText(R.string.Videos);
                } else if (counter == 1) {
                    tabTitle.setText(R.string.Schedule);
                } else {
                    tabTitle.setText(R.string.News);
                }
            }

            Loader = (ProgressBar) findViewById(R.id.loader);


            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Intent intent = new Intent(MainActivity.this,
                            FavoritActivity.class);
                    startActivity(intent);
                }
            });


            final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);


            Menulistview = (RecyclerView) findViewById(R.id.menuItems);


            /* build main menu */

            MenuArray = getResources().getStringArray(R.array.main_menu);


            MainMenuRecyclerView = (RecyclerView) findViewById(R.id.main_menu);
            MainMenuRecyclerView.setHasFixedSize(true);
            MainMenulayoutManager = new LinearLayoutManager(this);
            MainMenuRecyclerView.setLayoutManager(MainMenulayoutManager);
            MainMenuAdapter = new MainMenuAdapter(MenuArray);
            MainMenuRecyclerView.setAdapter(MainMenuAdapter);


            menuintent = new Intent(MainActivity.this, LeagueActivity.class);

            MainMenuRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(this, MainMenuRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                        @Override
                        public void onItemClick(View view, int position) {

                            MainMenuAdapter.notifyDataSetChanged();
                            MenuPos = position;

                            Menulistview.setVisibility(View.GONE);
                            Loader.setVisibility(View.VISIBLE);


                            switch (position) {
                                case 0:
                                    menuintent = new Intent(MainActivity.this, LeagueActivity.class);
                                    new SubMenuJsonTask().execute();
                                    break;
                                case 1:
                                    menuintent = new Intent(MainActivity.this, TeamActivity.class);
                                    new SubMenuJsonTask().execute();
                                    break;
                                case 2:
                                    menuintent = new Intent(MainActivity.this, TeamActivity.class);
                                    new SubMenuJsonTask().execute();
                                    break;
                                case 3:
                                    menuintent = new Intent(MainActivity.this, SuperActivity.class);
                                    startActivity(menuintent);
                                    Loader.setVisibility(View.GONE);
                                    drawer.closeDrawers();
                                    break;

                            }


                        }

                        @Override
                        public void onLongItemClick(View view, int position) {

                        }
                    })
            );




            /* load leagues as default sub menu */

            new SubMenuJsonTask().execute();


            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getString(R.string.admob_banner_interstitial));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());


        } else {
            ViewPager container = (ViewPager) findViewById(R.id.container);
            container.setVisibility(View.GONE);
            RelativeLayout no_internet = (RelativeLayout) findViewById(R.id.no_internet);
            no_internet.setVisibility(View.VISIBLE);
        }


        new SuggestionTask().execute();


    }


    private class SubMenuJsonTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {

            String jsonStr = parser.getDataFromUrl(Config.SUB_MENU_URL + MenuPos + "&locale=" + countryCheck);
            android.util.Log.e("SUBMENU", Config.SUB_MENU_URL + MenuPos + "&locale=" + countryCheck);

            return jsonStr;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(String result) {


            try {
                JSONArray jsonData = new JSONArray(result);

                MenuList.clear();


                final SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


                for (int i = 0; i < jsonData.length(); i++) {
                    JSONObject row = jsonData.getJSONObject(i);


                    HashMap<String, String> FavItem = null;
                    boolean Add = false;


                    String[] splitedKeys = row.getString("key").split(",");
                    for (int x = 0; x < splitedKeys.length; x++) {

                        String key = splitedKeys[x];
                        boolean flag = SP.getBoolean(key, false);
                        if (flag) {

                            FavItem = new HashMap<String, String>();


                            FavItem.put(FAV_NID, row.getString("nid"));
                            FavItem.put(FAV_NAME, row.getString("name"));
                            FavItem.put(FAV_IMAGE, row.getString("image"));
                            FavItem.put(FAVE_KEY, row.getString("key"));

                            Add = true;

                        }


                    }

                    if (Add) {

                        MenuList.add(FavItem);
                    }
                }


                MenuAdapter = new MenuAdapter(MenuList);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
                Menulistview.setLayoutManager(mLayoutManager);
                Menulistview.setItemAnimator(new DefaultItemAnimator());
                Menulistview.setAdapter(MenuAdapter);


                Menulistview.addOnItemTouchListener(
                        new RecyclerItemClickListener(getApplicationContext(), Menulistview, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                HashMap<String, String> league = MenuList.get(position);
                                String nid = league.get(FAV_NID);
                                Bundle bundle = new Bundle();
                                bundle.putInt("nid", Integer.parseInt(nid));

                                menuintent.putExtras(bundle);
                                startActivity(menuintent);

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


                Loader.setVisibility(View.GONE);
                Menulistview.setVisibility(View.VISIBLE);


            } catch (JSONException e) {
                android.util.Log.e("INFO", e.getMessage());
            }


        }


    }


    public class HttpPostAsyncTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {

            try {
                URL url = new URL(Config.USER_DATA_UPDATE);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("regId", regId)
                        .appendQueryParameter("favs", favs);

                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                android.util.Log.e("CONN", conn.getResponseMessage());


            } catch (final IOException e) {

            }

            return null;
        }
    }


    private class SuggestionTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {


            String jsonStr = parser.getDataFromUrl(Config.FOLLOW_SUGGESTION + System.currentTimeMillis());

            return jsonStr;
        }

        protected void onProgressUpdate(Integer... progress) {


        }

        protected void onPostExecute(String result) {


            try {


                JSONObject jsonData = new JSONObject(result);


                final SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


                String[] splitedKeys = jsonData.optString("conditions").split(",");


                if (SP.contains(jsonData.optString("key"))) {
                    android.util.Log.e("CONDITIONS", jsonData.optString("key") + " exists");
                    return;
                }


                for (int x = 0; x < splitedKeys.length; x++) {
                    String key = splitedKeys[x];
                    boolean conds = SP.getBoolean(key, false);
                    if (!conds) {
                        android.util.Log.e("CONDITIONS", key + " is false");
                        return;
                    }

                }


                boolean current = SP.getBoolean(jsonData.optString("key"), false);
                if (current) {
                    android.util.Log.e("CONDITIONS", current + " is true");
                    return;
                }


                android.util.Log.e("CONDITIONS", "Relevent to user");


                Intent intent = new Intent(MainActivity.this,
                        FollowActivity.class);


                startActivity(intent);


            } catch (JSONException e) {
                android.util.Log.e("INFO", e.getMessage());

            }


        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private static Bundle createBundleNoFragmentRestore(Bundle bundle) {
        if (bundle != null) {
            bundle.remove("android:support:fragments");
        }
        return bundle;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorit) {
            Intent intent = new Intent(MainActivity.this,
                    FavoritActivity.class);
            finish();
            startActivity(intent);

        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this,
                    SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {


        super.onResume();


        final SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        boolean flag = SP.getBoolean("key_registered", false);

        if (!flag) {
            Intent intent = new Intent(this,
                    RegisterActivityStepA.class);

            finish();
            startActivity(intent);


        }


        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());


    }

    @Override
    protected void onPause() {
        android.util.Log.e("MainActivity", "Paused");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        android.util.Log.e("MainActivity", "Stopped");
    }

    @Override
    protected void onStart() {
        super.onStart();
        android.util.Log.e("MainActivity", "Started");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        android.util.Log.e("MainActivity", "ReStarted");

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new VideoFragment(), "VIDE");
        adapter.addFrag(new ScheduleFragment(), "SCHED");
        adapter.addFrag(new NewsFragment(), "NEWS");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.EN) {
            // Handle the camera action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        public final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {

            }
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


    }


}
