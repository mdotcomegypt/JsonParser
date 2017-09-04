package com.BuzzKora;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.BuzzKora.R;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by mhosam on 7/16/17.
 */

public class MatchTableAdapter extends RecyclerView.Adapter<MatchTableAdapter.ViewHolder> {


    ArrayList<HashMap<String, String>> TableList = new ArrayList<HashMap<String, String>>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        public TextView rank;
        public TextView team;
        public TextView played;
        public TextView win;
        public TextView draw;
        public TextView lost;
        public TextView pro;
        public TextView against;
        public TextView pts;
        public LinearLayout tableHeader;



        public ViewHolder(View vi) {
            super(vi);
            rank = (TextView)vi.findViewById(R.id.rank);
            team = (TextView)vi.findViewById(R.id.team);
            played = (TextView)vi.findViewById(R.id.played);
            win = (TextView)vi.findViewById(R.id.win);
            draw = (TextView)vi.findViewById(R.id.draw);
            lost = (TextView)vi.findViewById(R.id.lost);
            pro = (TextView)vi.findViewById(R.id.pro);
            against = (TextView)vi.findViewById(R.id.against);
            pts = (TextView)vi.findViewById(R.id.pts);
            tableHeader = (LinearLayout)vi.findViewById(R.id.tableHeader);

        }
    }

    public MatchTableAdapter(ArrayList<HashMap<String, String>> TableList) {
        this.TableList = TableList;
    }



    @Override
    public int getItemViewType(int position) {
        //your code
        return position;
    }

    @Override
    public MatchTableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {

        View itemView = null;


             itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.match_table_row, parent, false);


        return new ViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        HashMap<String, String> row = new HashMap<String, String>();
        row = TableList.get(position);


        String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
        Typeface tf = Typeface.createFromAsset(holder.rank.getContext().getAssets(), fontPath);


        String fontPathAnton = "fonts/en_font.ttf";
        Typeface tfAnton = Typeface.createFromAsset(holder.rank.getContext().getAssets(), fontPathAnton);



        if(row.get("rank").equals("1")){
            holder.tableHeader.setVisibility(View.VISIBLE);
            for (int i=0; i < holder.tableHeader.getChildCount(); i++) {
                TextView x = (TextView) holder.tableHeader.getChildAt(i);
                x.setTypeface(tf);
            }

        }


        holder.rank.setText(row.get("rank"));
        holder.team.setText(row.get("team"));
        holder.played.setText(row.get("played"));
        holder.win.setText(row.get("win"));
        holder.draw.setText(row.get("draw"));
        holder.lost.setText(row.get("lost"));
        holder.pro.setText(row.get("pro"));
        holder.against.setText(row.get("against"));
        holder.pts.setText(row.get("pts"));


        holder.rank.setTypeface(tfAnton);
        holder.team.setTypeface(tf);
        holder.played.setTypeface(tfAnton);
        holder.win.setTypeface(tfAnton);
        holder.draw.setTypeface(tfAnton);
        holder.lost.setTypeface(tfAnton);
        holder.pro.setTypeface(tfAnton);
        holder.against.setTypeface(tfAnton);
        holder.pts.setTypeface(tfAnton);

    }

    @Override
    public int getItemCount() {
        return TableList.size();
    }
}
