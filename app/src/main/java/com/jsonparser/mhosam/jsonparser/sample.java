package com.jsonparser.mhosam.jsonparser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import com.jsonparser.mhosam.jsonparser.Log;

public class sample extends AppCompatActivity {


    TextView jsonresult;
    String URL;
    JSONArray jsonData = null;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        /*String jsonStr = parser.getDataFromUrl("http://90kora.com/config.json?v=17");
        jsonresult = (TextView)findViewById(R.id.jsonresults);
        jsonresult.setText(jsonStr);*/

        new DownloadFilesTask().execute();

    }




    private class DownloadFilesTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {
            String jsonStr = parser.getDataFromUrl("http://90kora.com/config.json?v=17");
            return jsonStr;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(String result) {



            try {
                jsonData = new JSONArray(result);
                for (int i = 0; i < jsonData.length(); i++) {
                    JSONObject row = jsonData.getJSONObject(i);
                    title = row.getString("title");
                    TextView txt = (TextView) findViewById(R.id.jsonresults);
                    txt.setText(title);
                }
            } catch (JSONException e) {
                android.util.Log.e("INFO", "JSON was invalid");
            }



        }
    }
}
