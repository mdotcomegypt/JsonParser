package com.BuzzKora;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.BuzzKora.MatchDetailsActivity.matchVideos;


public class MatchVideoFragment extends Fragment {


    JSONArray videoData = null;


    static final String VIDEO_KEY_TITLE = "title"; // parent node
    static final String  VIDEO_KEY_THUMB = "thumb";
    static final String  VIDEO_KEY_DATE = "date";
    static final String  VIDEO_KEY_ID= "id";
    static final String  VIDEO_KEY_MEDIA= "mp4";
    static final String  VIDEO_KEY_URL= "url";









    View rootView;



    SwipeRefreshLayout mSwipeRefreshLayout;

    ArrayList<HashMap<String, String>> VideoList = new ArrayList<HashMap<String, String>>();

    RecyclerView Videolistview;

    private MatchVideoAdapter MatchVideoAdapter;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.match_video_fragment, container, false);
        setHasOptionsMenu(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swifeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new VideoTask().execute();
            }
        });


        new VideoTask().execute();
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        AnalyticsApp.getInstance().trackScreenView("Match Video");
    }




    private class VideoTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {

            return matchVideos;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(String result) {


            try {



                videoData = new JSONArray(result);
                VideoList.clear();


                for (int i = 0; i < videoData.length(); i++) {
                    JSONObject row = videoData.getJSONObject(i);
                    HashMap<String, String> video = new HashMap<String, String>();
                    video.put(VIDEO_KEY_TITLE, row.getString("title"));
                    video.put(VIDEO_KEY_THUMB, row.getString("thumb"));
                    video.put(VIDEO_KEY_DATE, row.getString("date"));
                    video.put(VIDEO_KEY_MEDIA, row.getString("mp4"));
                    video.put(VIDEO_KEY_ID, row.getString("id"));
                    video.put(VIDEO_KEY_URL, row.getString("url"));


                    VideoList.add(video);
                }


                Videolistview = (RecyclerView) rootView.findViewById(R.id.Videolistview);
                MatchVideoAdapter = new MatchVideoAdapter(VideoList);
                RecyclerView.LayoutManager LayoutManagerS = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                Videolistview.setLayoutManager(LayoutManagerS);
                Videolistview.setItemAnimator(new DefaultItemAnimator());
                Videolistview.setAdapter(MatchVideoAdapter);
                Videolistview.setVisibility(View.VISIBLE);


                Videolistview.addOnItemTouchListener(
                        new RecyclerItemClickListener(getActivity().getApplicationContext(), Videolistview ,new RecyclerItemClickListener.OnItemClickListener() {
                            @Override public void onItemClick(View view, final int position) {
                                //HashMap<String, String> video = VideoList.get(position);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("Video", VideoList);
                                bundle.putInt("position", position);

                                // launch List activity
                                Intent intent = new Intent(getActivity(),
                                        VideoActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);



                            }

                            @Override public void onLongItemClick(View view, int position) {
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


                mSwipeRefreshLayout.setRefreshing(false);

            } catch (JSONException e) {
                android.util.Log.e("INFO", e.getMessage());
            }


        }


    }





}
