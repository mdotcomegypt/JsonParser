package com.BuzzKora;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.BuzzKora.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by mhosam on 7/16/17.
 */

public class MoreVideoListAdapter extends RecyclerView.Adapter<MoreVideoListAdapter.ViewHolder> {


    ArrayList<HashMap<String, String>> VideoList = new ArrayList<HashMap<String, String>>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        public TextView title;
        public ImageView image;
        public TextView date;
        public CardView cardview;
        public ViewHolder(View vi) {
            super(vi);
            title = (TextView)vi.findViewById(R.id.VideoTitle);
            image = (ImageView)vi.findViewById(R.id.VideoImage);
            date = (TextView)vi.findViewById(R.id.VideoDate);
            cardview = (CardView) vi.findViewById(R.id.card_view);
        }
    }

    public MoreVideoListAdapter(ArrayList<HashMap<String, String>> VideoList) {
        this.VideoList = VideoList;
    }



    @Override
    public int getItemViewType(int position) {
        //your code
        return position;
    }

    @Override
    public MoreVideoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {

        View itemView = null;



            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.video_card, parent, false);


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


        holder.title.setText(video.get(VideoFragment.VIDEO_KEY_TITLE));
        holder.date.setText(video.get(VideoFragment.VIDEO_KEY_DATE));

        Context context = holder.image.getContext();

        Picasso.with(context)
                .load(video.get(VideoFragment.VIDEO_KEY_THUMB))
                .placeholder(R.drawable.placeholder)
                .into(holder.image);

        String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
        Typeface tf = Typeface.createFromAsset(holder.title.getContext().getAssets(), fontPath);


        holder.title.setTypeface(tf);
        holder.title.setTextSize(15);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return VideoList.size();
    }
}
