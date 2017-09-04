package com.BuzzKora;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class RegisterActivityStepC extends AppCompatActivity {


    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] TitlesList;
    private String[] TitlesKey;


    public static int currentPos;


    private ViewPager mViewPager;


    private static final String ARG_KEY = "key";
    private static final String ARG_TITLE = "title";
    private static final String ARG_POS = "pos";


    JSONArray jsonData = null;


    ArrayList<HashMap<String, String>> FavList = new ArrayList<HashMap<String, String>>();

    static final String FAV_NID = "nid"; // parent node
    static final String FAV_NAME = "name";
    static final String FAV_IMAGE = "image";
    static final String FAVE_KEY = "key";

    String URL;

    ProgressBar Loader;
    TextView title;
    TextView txt;

    Button submit;


    RecyclerView Favlistview;

    private RecyclerView recyclerView;
    private RegisterAdapter RegisterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_c);

        Context mContext = this;


        TitlesList = getResources().getStringArray(R.array.favorit_titles);


        final SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);


        String Code = tm.getNetworkCountryIso().toUpperCase();

        Locale locale = new Locale("en",Code);

        String countryCode = locale.getISO3Country();



        String countryCheck = SP.getString("key_country", countryCode);
        SharedPreferences.Editor editor = SP.edit();
        editor.putString("key_country", countryCode);
        editor.commit();

        android.util.Log.e("key_country", SP.getString("key_country",countryCode));


        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            //On click function
            public void onClick(View view) {
                //Create the intent to start another activity



                boolean flag = SP.getBoolean("key_registered", false);

                SharedPreferences.Editor editor = SP.edit();
                editor.putBoolean("key_registered", true);
                editor.commit();
                Bundle bundle = new Bundle();
                bundle.putBoolean("updateUserData", true);
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtras(bundle);
                finish();
                startActivity(intent);
            }
        });

        Favlistview = (RecyclerView) findViewById(R.id.listFavs);

        String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), fontPath);

        Loader = (ProgressBar) findViewById(R.id.loader);
        title = (TextView) findViewById(R.id.title);
        txt = (TextView) findViewById(R.id.txt);

        title.setTypeface(tf);
        txt.setTypeface(tf);
        submit.setTypeface(tf);


        /*recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                        adapter.notifyDataSetChanged();
                        view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.RegisterLeaguesBG));
                        currentPos = position;

                        Favlistview.setVisibility(View.GONE);
                        Loader.setVisibility(View.VISIBLE);

                        URL = BASE_URL + TitlesKey[position];
                        new FavoritJsonTask().execute();


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );*/


        URL = Config.COUNTRY_LISTING + "locale=" + countryCode;
        android.util.Log.e("FRAGMENTREQUEST", URL);
        new FavoritJsonTask().execute();


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

                android.util.Log.e("FRAGMENTREQUEST", URL);


                for (int i = 0; i < jsonData.length(); i++) {
                    JSONObject row = jsonData.getJSONObject(i);


                    HashMap<String, String> FavItem = new HashMap<String, String>();
                    FavItem.put(FAV_NID, row.getString("nid"));
                    FavItem.put(FAV_NAME, row.getString("name"));
                    FavItem.put(FAV_IMAGE, row.getString("image"));
                    FavItem.put(FAVE_KEY, row.getString("key"));


                    FavList.add(FavItem);
                }

                android.util.Log.e("FavList", String.valueOf(FavList.size()));


                RegisterAdapter = new RegisterAdapter(FavList);
                int numOfCol = calculateNoOfColumns(getApplicationContext());
                //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());int numOfCol = calculateNoOfColumns(getActivity().getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), numOfCol);
                Favlistview.setLayoutManager(mLayoutManager);
                Favlistview.setItemAnimator(new DefaultItemAnimator());
                Favlistview.setAdapter(RegisterAdapter);

                Favlistview.setVisibility(View.VISIBLE);
                Loader.setVisibility(View.GONE);


            } catch (JSONException e) {
                android.util.Log.e("INFO", e.getMessage());
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

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsApp.getInstance().trackScreenView("Register Step C");
    }
}
