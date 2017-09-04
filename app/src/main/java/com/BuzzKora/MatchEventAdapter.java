package com.BuzzKora;

import android.content.Context;
import android.graphics.Typeface;
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

import static com.BuzzKora.MatchEventsFragment.EVENT_MIN;
import static com.BuzzKora.MatchEventsFragment.EVENT_PLAYER1;
import static com.BuzzKora.MatchEventsFragment.EVENT_PLAYER2;
import static com.BuzzKora.MatchEventsFragment.EVENT_TEAM;
import static com.BuzzKora.MatchEventsFragment.EVENT_TYPE;


/**
 * Created by mhosam on 7/16/17.
 */

public class MatchEventAdapter extends RecyclerView.Adapter<MatchEventAdapter.ViewHolder> {


    ArrayList<HashMap<String, String>> EventList = new ArrayList<HashMap<String, String>>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        public TextView type;
        public TextView min;
        public TextView player1;
        public TextView player2;
        public ImageView icn;



        public ViewHolder(View vi) {
            super(vi);
            min = (TextView)vi.findViewById(R.id.min);
            type = (TextView)vi.findViewById(R.id.type);
            player1 = (TextView)vi.findViewById(R.id.player1);
            player2 = (TextView)vi.findViewById(R.id.player2);
            icn = (ImageView) vi.findViewById(R.id.icn);

        }
    }

    public MatchEventAdapter(ArrayList<HashMap<String, String>> EventList) {
        this.EventList = EventList;
    }



    @Override
    public int getItemViewType(int position) {
        //your code
        return position;
    }

    @Override
    public MatchEventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {

        View itemView = null;


        HashMap<String, String> event = new HashMap<String, String>();
        event = EventList.get(viewType);
        String team = event.get(EVENT_TEAM);

        if (team.equals("A")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.match_event_row_a, parent, false);
        } else if (team.equals("B")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.match_event_row_b, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.match_event_row_n, parent, false);
        }


        return new ViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        HashMap<String, String> event = new HashMap<String, String>();
        event = EventList.get(position);

        Context context = holder.icn.getContext();


        String Type = event.get(EVENT_TYPE);
        int icnImage;

        switch (Type){
            case "G":
                icnImage = R.drawable.bc_icn_goal;
                Picasso.with(context).load(icnImage).into(holder.icn);
                holder.player1.setText(event.get(EVENT_PLAYER1));
                holder.type.setText(R.string.goal);
                holder.min.setText(event.get(EVENT_MIN));
                holder.player2.setText(context.getString(R.string.assist) + ": " + event.get(EVENT_PLAYER2));
                break;

            case "START":
                icnImage = R.drawable.bc_icn_kickoff;
                Picasso.with(context).load(icnImage).into(holder.icn);
                holder.type.setText(R.string.kickoff);
                break;
            case "END":
                icnImage = R.drawable.bc_icn_fulltime;
                Picasso.with(context).load(icnImage).into(holder.icn);
                holder.type.setText(R.string.fulltime);
                break;

            case "S":
                icnImage = R.drawable.icn_inout;
                Picasso.with(context).load(icnImage).into(holder.icn);
                holder.type.setText(R.string.sub);
                holder.min.setText(event.get(EVENT_MIN));
                holder.player1.setText(event.get(EVENT_PLAYER1));
                holder.player2.setText(context.getString(R.string.out) + ": " + event.get(EVENT_PLAYER2));
                break;
            case "YC":
                icnImage = R.drawable.icn_match_ycard;
                Picasso.with(context).load(icnImage).into(holder.icn);
                holder.type.setText(R.string.yellow_card);
                holder.min.setText(event.get(EVENT_MIN));
                holder.player1.setText(event.get(EVENT_PLAYER1));
                holder.player2.setVisibility(View.GONE);
                break;
            case "Y2C":
                icnImage = R.drawable.ryc_icn;
                Picasso.with(context).load(icnImage).into(holder.icn);
                holder.type.setText(R.string.red_card);
                holder.min.setText(event.get(EVENT_MIN));
                holder.player1.setText(event.get(EVENT_PLAYER1));
                holder.player2.setVisibility(View.GONE);
                break;
            case "RC":
                icnImage = R.drawable.icn_match_rcard;
                Picasso.with(context).load(icnImage).into(holder.icn);
                holder.type.setText(R.string.red_card);
                holder.min.setText(event.get(EVENT_MIN));
                holder.player1.setText(event.get(EVENT_PLAYER1));
                holder.player2.setVisibility(View.GONE);
                break;
            case "PG":
                icnImage = R.drawable.bc_icn_pen_goal;
                Picasso.with(context).load(icnImage).into(holder.icn);
                holder.type.setText(R.string.pen_goal);
                holder.type.setVisibility(View.VISIBLE);
                holder.min.setText(event.get(EVENT_MIN));
                holder.player1.setText(event.get(EVENT_PLAYER1));
                holder.player2.setVisibility(View.GONE);
                break;
            case "MPG":
                icnImage = R.drawable.bc_icn_pen_miss;
                Picasso.with(context).load(icnImage).into(holder.icn);
                holder.type.setText(R.string.pen_miss);
                holder.type.setVisibility(View.VISIBLE);
                holder.min.setText(event.get(EVENT_MIN));
                holder.player1.setText(event.get(EVENT_PLAYER1));
                holder.player2.setVisibility(View.GONE);
                break;
            case "OG":
                icnImage = R.drawable.bc_icn_owngoal;
                Picasso.with(context).load(icnImage).into(holder.icn);
                holder.type.setText(R.string.own_goal);
                holder.type.setVisibility(View.VISIBLE);
                holder.min.setText(event.get(EVENT_MIN));
                holder.player1.setText(event.get(EVENT_PLAYER1));
                holder.player2.setVisibility(View.GONE);
                break;


        }

        String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
        Typeface tf = Typeface.createFromAsset(holder.icn.getContext().getAssets(), fontPath);


        String fontPathAnton = "fonts/en_font.ttf";
        Typeface tfAnton = Typeface.createFromAsset(holder.icn.getContext().getAssets(), fontPathAnton);

       /* if (event.get(EVENT_PLAYER1).equals("null")){
            holder.player1.setVisibility(View.GONE);
        }*/

        if (event.get(EVENT_PLAYER1).equals("null")){
            holder.player1.setVisibility(View.GONE);
            holder.type.setVisibility(View.VISIBLE);
        }



        if (event.get(EVENT_PLAYER2).equals("null")){
            holder.player2.setVisibility(View.GONE);
        }

        holder.type.setTypeface(tf);
        holder.min.setTypeface(tfAnton);
        holder.player1.setTypeface(tf);
        holder.player2.setTypeface(tf);

    }

    @Override
    public int getItemCount() {
        return EventList.size();
    }
}
