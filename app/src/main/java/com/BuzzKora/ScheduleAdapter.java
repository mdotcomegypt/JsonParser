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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BuzzKora.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;


/**
 * Created by mhosam on 7/16/17.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {


    ArrayList<HashMap<String, String>> ScheduleList = new ArrayList<HashMap<String, String>>();

    int lastPosition = -1;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case






        public TextView teama;
        public TextView teamb;
        public ImageView teamathumb;
        public ImageView teambthumb;
        public TextView date;
        public TextView status;
        public TextView SubStatus;
        public TextView league;
        public TextView score1;
        public TextView score2;
        public TextView min;
        public RelativeLayout Live;
        public ProgressBar mProgress;


        public ViewHolder(View vi) {
            super(vi);
            teama = (TextView)vi.findViewById(R.id.TeamA);
            teamb = (TextView)vi.findViewById(R.id.TeamB);
            score1 = (TextView)vi.findViewById(R.id.ScoreA);
            score2 = (TextView)vi.findViewById(R.id.ScoreB);
            teamathumb = (ImageView)vi.findViewById(R.id.TeamAT);
            teambthumb = (ImageView)vi.findViewById(R.id.TeamBT);
            date = (TextView)vi.findViewById(R.id.Date);
            status = (TextView)vi.findViewById(R.id.Status);
            SubStatus = (TextView)vi.findViewById(R.id.SubStatus);
            league = (TextView)vi.findViewById(R.id.League);
            min = (TextView)vi.findViewById(R.id.min);
            Live = (RelativeLayout)vi.findViewById(R.id.Live);
            mProgress = (ProgressBar)vi.findViewById(R.id.timer_progress);
        }
    }

    public ScheduleAdapter(ArrayList<HashMap<String, String>> ScheduleList) {
        this.ScheduleList = ScheduleList;
    }



    @Override
    public int getItemViewType(int position) {
        //your code
        return position;
    }

    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {

        View itemView = null;

        int position=viewType;

        HashMap<String, String> match = new HashMap<String, String>();
        match = ScheduleList.get(position);

        String status = match.get(ScheduleFragment.MATCH_KEY_STATUS);

        if(status.equals("Live now")) {

             itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.match_card, parent, false);
        }
        else {
             itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.match_card, parent, false);
        }

        return new ViewHolder(itemView);

        // set the view's size, margins, paddings and layout parameters
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(MoreNewsList[position]);

        HashMap<String, String> match = new HashMap<String, String>();
        match = ScheduleList.get(position);

        String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
        Typeface tf = Typeface.createFromAsset(holder.teama.getContext().getAssets(), fontPath);


        String fontPathAnton = "fonts/en_font_bold.ttf";
        Typeface tfAnton = Typeface.createFromAsset(holder.teama.getContext().getAssets(), fontPathAnton);


        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        Log.d("Time zone: ", tz.getDisplayName());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(tz);
        Long timestamp = Long.valueOf(match.get(ScheduleFragment.MATCH_KEY_DATE));
        String localTime = sdf.format(new Date(timestamp * 1000));






        String status = match.get(ScheduleFragment.MATCH_KEY_STATUS);

        switch (status){
            case "Finished":
                holder.status.setText(R.string.Finished);
                holder.status.setTypeface(tf);
                holder.status.setTextSize(14);
                holder.Live.setVisibility(View.GONE);

                holder.SubStatus.setText(localTime);
                holder.SubStatus.setTypeface(tfAnton);
                holder.SubStatus.setTextSize(11);
                holder.SubStatus.setVisibility(View.VISIBLE);


                break;
            case "Live now":

                holder.status.setVisibility(View.GONE);
                holder.min.setText(R.string.Livenow);
                holder.min.setTextSize(13);
                holder.min.setTypeface(tf);

                holder.Live.setVisibility(View.VISIBLE);

                String min = match.get(ScheduleFragment.MATCH_KEY_TIME);

                int timmer;

                if (min.equals("HT")){
                    timmer = 45;
                } else if (min.equals("FT")) {
                    timmer = 90;
                } else {
                    timmer = Integer.parseInt(min);
                }


                holder.mProgress.setProgress(timmer);   // Main Progress
                holder.mProgress.setSecondaryProgress(90); // Secondary Progress
                holder.mProgress.setMax(90); // Maximum Progress

                break;

            case "Coming up":
                holder.status.setText(localTime);
                holder.status.setTypeface(tfAnton);
                holder.status.setTextSize(16);
                holder.score1.setVisibility(View.GONE);
                holder.score2.setVisibility(View.GONE);


                break;

        }



        holder.teama.setText(match.get(ScheduleFragment.MATCH_KEY_TEAMA));
        holder.teamb.setText(match.get(ScheduleFragment.MATCH_KEY_TEAMB));
        holder.league.setText(match.get(ScheduleFragment.MATCH_KEY_LEAGUE));

        holder.score1.setText(match.get(ScheduleFragment.MATCH_KEY_SCORE1));
        holder.score2.setText(match.get(ScheduleFragment.MATCH_KEY_SCORE2));



        String header = match.get(ScheduleFragment.MATCH_KEY_HEADER);




        switch (header){
            case "0" :
                holder.league.setVisibility(View.GONE);
                break;
            case "1":
                holder.league.setVisibility(View.VISIBLE);
                break;
        }






        Context context = holder.teamathumb.getContext();

        Picasso.with(context)
                .load(match.get(ScheduleFragment.MATCH_KEY_THUMBA))
                .placeholder(R.drawable.team_placeholder)
                .into(holder.teamathumb);

        Picasso.with(context)
                .load(match.get(ScheduleFragment.MATCH_KEY_THUMBB))
                .placeholder(R.drawable.team_placeholder)
                .into(holder.teambthumb);

        /*Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = position;*/




        // Applying font
        holder.teama.setTypeface(tf);
        holder.teamb.setTypeface(tf);

        holder.league.setTypeface(tf);
        holder.score1.setTypeface(tfAnton);
        holder.score2.setTypeface(tfAnton);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return ScheduleList.size();
    }
}
