package com.BuzzKora;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.BuzzKora.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by mhosam on 7/16/17.
 */

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {


    ArrayList<HashMap<String, String>> VideoList = new ArrayList<HashMap<String, String>>();

    int lastPosition = -1;

    int cols;

    private AdView mAdView;



    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        public TextView title;
        public ImageView image;
        public TextView date;
        View ad_holder;
        public ViewHolder(View vi) {
            super(vi);
            title = (TextView)vi.findViewById(R.id.VideoTitle);
            image = (ImageView)vi.findViewById(R.id.VideoImage);
            date = (TextView)vi.findViewById(R.id.VideoDate);
            ad_holder = vi.findViewById(R.id.ad_holder);
        }
    }

    public VideoListAdapter(ArrayList<HashMap<String, String>> VideoList) {
        this.VideoList = VideoList;
    }



    @Override
    public int getItemViewType(int position) {
        //your code
        return position;
    }

    @Override
    public VideoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {

        View itemView = null;

        int position=viewType;

        cols = VideoFragment.calculateNoOfColumns(parent.getContext());

        if(position == 0) {

             itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.video_card, parent, false);
        }
        else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.video_card, parent, false);
        }

        return new ViewHolder(itemView);

        // set the view's size, margins, paddings and layout parameters
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(MoreNewsList[position]);

        HashMap<String, String> video = new HashMap<String, String>();
        video = VideoList.get(position);


        if (cols == 1) {

            if (position == 4 || position == 10 || position == 15 || position == 20  || position == 25) {
                holder.ad_holder.setVisibility(View.VISIBLE);

                mAdView = (AdView) holder.ad_holder.findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);

            }

        }


        holder.title.setText(video.get(VideoFragment.VIDEO_KEY_TITLE));
        holder.date.setText(video.get(VideoFragment.VIDEO_KEY_DATE));

        Context context = holder.image.getContext();

        Picasso.with(context)
                .load(video.get(VideoFragment.VIDEO_KEY_THUMB))
                .placeholder(R.drawable.placeholder)
                .into(holder.image);

        String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(holder.title.getContext().getAssets(), fontPath);

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = position;


        // Applying font
        holder.title.setTypeface(tf);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return VideoList.size();
    }
}
