package com.BuzzKora;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import static com.BuzzKora.MatchH2HFragment.H2H_MATCH_DATE;
import static com.BuzzKora.MatchH2HFragment.H2H_MATCH_LEAGUE;
import static com.BuzzKora.MatchH2HFragment.H2H_MATCH_SATAUS;
import static com.BuzzKora.MatchH2HFragment.H2H_MATCH_SCOREA;
import static com.BuzzKora.MatchH2HFragment.H2H_MATCH_SCOREB;
import static com.BuzzKora.MatchH2HFragment.H2H_MATCH_TEAMA;
import static com.BuzzKora.MatchH2HFragment.H2H_MATCH_TEAMB;
import static com.BuzzKora.MatchH2HFragment.H2H_MATCH_THUMBA;
import static com.BuzzKora.MatchH2HFragment.H2H_MATCH_THUMBB;


/**
 * Created by mhosam on 7/16/17.
 */

public class MatchH2HAdapter extends RecyclerView.Adapter<MatchH2HAdapter.ViewHolder> {


    ArrayList<HashMap<String, String>> H2HList = new ArrayList<HashMap<String, String>>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        public TextView team_a;
        public TextView team_b;
        public TextView status;
        public TextView SubStatus;
        public TextView score_a;
        public TextView score_b;
        public ImageView thumb_a;
        public ImageView thumb_b;
        public TextView header;






        public ViewHolder(View vi) {
            super(vi);
            status = (TextView)vi.findViewById(R.id.Status);
            SubStatus = (TextView)vi.findViewById(R.id.SubStatus);
            team_a = (TextView)vi.findViewById(R.id.TeamA);
            team_b = (TextView)vi.findViewById(R.id.TeamB);
            score_a = (TextView) vi.findViewById(R.id.ScoreA);
            score_b = (TextView) vi.findViewById(R.id.ScoreB);
            thumb_a = (ImageView) vi.findViewById(R.id.TeamAT);
            thumb_b = (ImageView) vi.findViewById(R.id.TeamBT);
            header = (TextView) vi.findViewById(R.id.League);


        }
    }

    public MatchH2HAdapter(ArrayList<HashMap<String, String>> H2HList) {
        this.H2HList = H2HList;
    }



    @Override
    public int getItemViewType(int position) {
        //your code
        return position;
    }

    @Override
    public MatchH2HAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {

        View itemView = null;


             itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.match_card, parent, false);


        return new ViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        HashMap<String, String> row = new HashMap<String, String>();
        row = H2HList.get(position);
        String Type = row.get(MatchStatsFragment.STATS_TYPE);

        Context context = holder.team_b.getContext();


        holder.team_a.setText(row.get(H2H_MATCH_TEAMA));
        holder.team_b.setText(row.get(H2H_MATCH_TEAMB));


        holder.score_a.setText(row.get(H2H_MATCH_SCOREA));
        holder.score_b.setText(row.get(H2H_MATCH_SCOREB));


        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        Log.d("Time zone: ", tz.getDisplayName());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat DayFormat = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setTimeZone(tz);
        DayFormat.setTimeZone(tz);
        Long timestamp = Long.valueOf(row.get(H2H_MATCH_DATE));
        String localTime = sdf.format(new Date(timestamp * 1000));
        String localDay = DayFormat.format(new Date(timestamp * 1000));
        String fullDate =  localTime+ " " + localDay ;

        holder.status.setVisibility(View.GONE);
        holder.header.setVisibility(View.GONE);


        holder.SubStatus.setText(fullDate);
        holder.SubStatus.setVisibility(View.VISIBLE);
        holder.SubStatus.setTextSize(11);



        Picasso.with(context)
                .load(row.get(H2H_MATCH_THUMBA))
                .placeholder(R.drawable.team_placeholder)
                .into(holder.thumb_a);

        Picasso.with(context)
                .load(row.get(H2H_MATCH_THUMBB))
                .placeholder(R.drawable.team_placeholder)
                .into(holder.thumb_b);






        String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
        Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPath);


        String fontPathAnton = "fonts/en_font.ttf";
        Typeface tfAnton = Typeface.createFromAsset(context.getAssets(), fontPathAnton);

        holder.team_a.setTypeface(tf);
        holder.team_b.setTypeface(tf);
        holder.score_a.setTypeface(tfAnton);
        holder.score_b.setTypeface(tfAnton);
        holder.SubStatus.setTypeface(tfAnton);


    }

    @Override
    public int getItemCount() {
        return H2HList.size();
    }
}
