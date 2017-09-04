package com.BuzzKora;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.BuzzKora.R;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by mhosam on 7/16/17.
 */

public class VideoMatchAdapter extends RecyclerView.Adapter<VideoMatchAdapter.ViewHolder> {


    ArrayList<HashMap<String, String>> VideoList = new ArrayList<HashMap<String, String>>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        public TextView title;

        public ViewHolder(View vi) {
            super(vi);
            title = (TextView)vi.findViewById(R.id.VideoTitle);

        }
    }

    public VideoMatchAdapter(ArrayList<HashMap<String, String>> VideoList) {
        this.VideoList = VideoList;
    }



    @Override
    public int getItemViewType(int position) {
        //your code
        return position;
    }

    @Override
    public VideoMatchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {

        View itemView = null;

        int position=viewType;

        if(position == 0) {

             itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.match_video, parent, false);
        }
        else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.match_video, parent, false);
        }

        return new ViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        HashMap<String, String> video = new HashMap<String, String>();
        video = VideoList.get(position);


        //holder.title.setText(video.get(MatchActivity.VIDEO_KEY_TITLE));
        holder.title.setText(R.string.Highlights);



        String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
        Typeface tf = Typeface.createFromAsset(holder.title.getContext().getAssets(), fontPath);
        holder.title.setTypeface(tf);

    }

    @Override
    public int getItemCount() {
        return VideoList.size();
    }
}
