package com.BuzzKora;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.BuzzKora.R;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by mhosam on 7/16/17.
 */

public class MatchStatsAdapter extends RecyclerView.Adapter<MatchStatsAdapter.ViewHolder> {


    ArrayList<HashMap<String, String>> StatsList = new ArrayList<HashMap<String, String>>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        public TextView type;
        public TextView team_a;
        public TextView team_b;
        public ProgressBar ProgressBarA;
        public ProgressBar ProgressBarB;




        public ViewHolder(View vi) {
            super(vi);
            type = (TextView)vi.findViewById(R.id.type);
            team_a = (TextView)vi.findViewById(R.id.team_a);
            team_b = (TextView)vi.findViewById(R.id.team_b);
            ProgressBarA = (ProgressBar) vi.findViewById(R.id.ProgressBarA);
            ProgressBarB = (ProgressBar) vi.findViewById(R.id.ProgressBarB);


        }
    }

    public MatchStatsAdapter(ArrayList<HashMap<String, String>> StatsList) {
        this.StatsList = StatsList;
    }



    @Override
    public int getItemViewType(int position) {
        //your code
        return position;
    }

    @Override
    public MatchStatsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {

        View itemView = null;


             itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.match_stats_row, parent, false);


        return new ViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        HashMap<String, String> row = new HashMap<String, String>();
        row = StatsList.get(position);
        String Type = row.get(MatchStatsFragment.STATS_TYPE);

        Context context = holder.team_b.getContext();


       // holder.type.setText(context.getResources().getIdentifier(Type, "string", context.getPackageName()));
        holder.type.setText(row.get(MatchStatsFragment.STATS_TYPE));
        holder.team_a.setText(row.get(MatchStatsFragment.STATS_VALUE_A));
        holder.team_b.setText(row.get(MatchStatsFragment.STATS_VALUE_B));

        int total = (int) Float.parseFloat(row.get(MatchStatsFragment.STATS_VALUE_A)) + (int) Float.parseFloat(row.get(MatchStatsFragment.STATS_VALUE_B));
        holder.ProgressBarA.setMax(total);
        holder.ProgressBarB.setMax(total);
        holder.ProgressBarA.setProgress((int) Float.parseFloat(row.get(MatchStatsFragment.STATS_VALUE_A)));
        holder.ProgressBarB.setProgress((int) Float.parseFloat(row.get(MatchStatsFragment.STATS_VALUE_B)));


        String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
        Typeface tf = Typeface.createFromAsset(holder.type.getContext().getAssets(), fontPath);


        String fontPathAnton = "fonts/en_font.ttf";
        Typeface tfAnton = Typeface.createFromAsset(holder.type.getContext().getAssets(), fontPathAnton);

        holder.type.setTypeface(tf);
        holder.team_a.setTypeface(tfAnton);
        holder.team_b.setTypeface(tfAnton);


    }

    @Override
    public int getItemCount() {
        return StatsList.size();
    }
}
