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


/**
 * Created by mhosam on 7/16/17.
 */

public class EventMatchAdapter extends RecyclerView.Adapter<EventMatchAdapter.ViewHolder> {


    ArrayList<HashMap<String, String>> EventList = new ArrayList<HashMap<String, String>>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        public TextView time;
        public TextView displayEventType;
        public TextView player1;
        public TextView player2;

        public ImageView icn1;
        public ImageView icn2;

        public ViewHolder(View vi) {
            super(vi);
            time = (TextView)vi.findViewById(R.id.time);
            displayEventType = (TextView)vi.findViewById(R.id.displayEventType);
            player1 = (TextView)vi.findViewById(R.id.player1);
            player2 = (TextView)vi.findViewById(R.id.player2);
            icn1 = (ImageView)vi.findViewById(R.id.icn1);
            icn2 = (ImageView)vi.findViewById(R.id.icn2);

        }
    }

    public EventMatchAdapter(ArrayList<HashMap<String, String>> EventList) {
        this.EventList = EventList;
    }



    @Override
    public int getItemViewType(int position) {
        //your code
        return position;
    }

    @Override
    public EventMatchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {

        View itemView = null;


             itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.event_row, parent, false);


        return new ViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        HashMap<String, String> event = new HashMap<String, String>();
        event = EventList.get(position);


        //holder.title.setText(video.get(MatchActivity.VIDEO_KEY_TITLE));

        String Type = event.get(MatchActivity.LIVE_KEY_TYPE);

        int icn1;
        int icn2;
        String min;
        int title;
        String playername1;
        String playername2;

        min = event.get(MatchActivity.LIVE_KEY_TIME);
        playername1 = event.get(MatchActivity.LIVE_KEY_PLAYERA);
        playername2 = event.get(MatchActivity.LIVE_KEY_PLAYERB);

        Context context = holder.icn1.getContext();




        switch (Type){
            case "goal":
                icn1 = R.drawable.bc_icn_goal;
                Picasso.with(context).load(icn1).into(holder.icn1);
                holder.displayEventType.setText(R.string.goal);
                holder.time.setText(min);
                holder.player1.setText(playername1);
                break;
            case "assist":
                icn1 = R.drawable.bc_icn_goal;
                Picasso.with(context).load(icn1).into(holder.icn1);
                holder.icn1.setVisibility(View.GONE);
                holder.displayEventType.setText(R.string.assist);
                holder.time.setText(min);
                holder.player1.setText(playername1);
                break;
            case "substitution":
                icn1 = R.drawable.bc_icn_goal;
                icn2 = R.drawable.bc_icn_goal;
                Picasso.with(context).load(icn1).into(holder.icn1);
                Picasso.with(context).load(icn2).into(holder.icn2);
                holder.icn2.setVisibility(View.VISIBLE);
                holder.displayEventType.setText(R.string.sub);
                holder.time.setText(min);
                holder.player1.setText(playername1);
                holder.player2.setText(playername2);
                holder.player2.setVisibility(View.VISIBLE);
                break;
            case "yellow-card":
                icn1 = R.drawable.icn_match_ycard;
                Picasso.with(context).load(icn1).into(holder.icn1);
                holder.displayEventType.setText(R.string.yellow_card);
                holder.time.setText(min);
                holder.player1.setText(playername1);
                break;
            case "red-card":
                icn1 = R.drawable.icn_match_rcard;
                Picasso.with(context).load(icn1).into(holder.icn1);
                holder.displayEventType.setText(R.string.red_card);
                holder.time.setText(min);
                holder.player1.setText(playername1);
                break;
            case "penalty-goal":
                icn1 = R.drawable.bc_icn_pen_goal;
                Picasso.with(context).load(icn1).into(holder.icn1);
                holder.displayEventType.setText(R.string.pen_goal);
                holder.time.setText(min);
                holder.player1.setText(playername1);
                break;
            case "penalty-save":
                icn1 = R.drawable.bc_icn_pen_miss;
                Picasso.with(context).load(icn1).into(holder.icn1);
                holder.displayEventType.setText(R.string.pen_save);
                holder.time.setText(min);
                holder.player1.setText(playername1);
                break;
            case "missed-penalty":
                icn1 = R.drawable.bc_icn_pen_miss;
                Picasso.with(context).load(icn1).into(holder.icn1);
                holder.displayEventType.setText(R.string.pen_miss);
                holder.time.setText(min);
                holder.player1.setText(playername1);
                break;
            case "own-goal":
                icn1 = R.drawable.bc_icn_goal;
                Picasso.with(context).load(icn1).into(holder.icn1);
                holder.displayEventType.setText(R.string.own_goal);
                holder.time.setText(min);
                holder.player1.setText(playername1);
                break;
            case "pen-so-goal":
                icn1 = R.drawable.bc_icn_pen_goal;
                Picasso.with(context).load(icn1).into(holder.icn1);
                holder.displayEventType.setText(R.string.so_goal);
                holder.time.setText(min);
                holder.player1.setText(playername1);
                break;
            case "pen-so-miss":
                icn1 = R.drawable.bc_icn_pen_miss;
                Picasso.with(context).load(icn1).into(holder.icn1);
                holder.displayEventType.setText(R.string.so_miss);
                holder.time.setText(min);
                holder.player1.setText(playername1);
                break;

        }




        String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
        Typeface tf = Typeface.createFromAsset(holder.time.getContext().getAssets(), fontPath);


        holder.displayEventType.setTypeface(tf);
        holder.player1.setTypeface(tf);
        holder.player2.setTypeface(tf);

    }

    @Override
    public int getItemCount() {
        return EventList.size();
    }
}
