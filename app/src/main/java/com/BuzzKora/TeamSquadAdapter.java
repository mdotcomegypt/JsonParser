package com.BuzzKora;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class TeamSquadAdapter extends RecyclerView.Adapter<TeamSquadAdapter.ViewHolder> {


    ArrayList<HashMap<String, String>> SquadList = new ArrayList<HashMap<String, String>>();

    int lastPosition = -1;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case






        public TextView name;
        public ImageView image;
        public TextView number;
        public TextView position;
        public TextView goals;
        public TextView appearances;
        public TextView tshirt_number;
        public TextView goals_number;
        public TextView appearances_number;


        public ViewHolder(View vi) {
            super(vi);
            name = (TextView)vi.findViewById(R.id.name);
            image = (ImageView)vi.findViewById(R.id.image);
            number = (TextView)vi.findViewById(R.id.number);
            position = (TextView)vi.findViewById(R.id.position);
            goals = (TextView)vi.findViewById(R.id.goals);
            appearances = (TextView)vi.findViewById(R.id.appearances);
            tshirt_number = (TextView)vi.findViewById(R.id.tshirt_number);
            goals_number = (TextView)vi.findViewById(R.id.goals_number);
            appearances_number = (TextView)vi.findViewById(R.id.appearances_number);
        }
    }

    public TeamSquadAdapter(ArrayList<HashMap<String, String>> SquadList) {
        this.SquadList = SquadList;
    }



    @Override
    public int getItemViewType(int position) {
        //your code
        return position;
    }

    @Override
    public TeamSquadAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {

        View itemView = null;

        int position=viewType;



             itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.player_card, parent, false);


        return new ViewHolder(itemView);

        // set the view's size, margins, paddings and layout parameters
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(MoreNewsList[position]);

        HashMap<String, String> player = new HashMap<String, String>();
        player = SquadList.get(position);

        Context context = holder.name.getContext();

        String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
        Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPath);


        String fontPathAnton = "fonts/en_font_bold.ttf";
        Typeface tfAnton = Typeface.createFromAsset(context.getAssets(), fontPathAnton);





        holder.name.setText(player.get(TeamSquadFragment.PLAYER_KEY_NAME));
        holder.goals.setText(player.get(TeamSquadFragment.PLAYER_KEY_GOALS));
        holder.appearances.setText(player.get(TeamSquadFragment.PLAYER_KEY_APPEARANCES));
        holder.position.setText(player.get(TeamSquadFragment.PLAYER_KEY_POSITION));
        holder.number.setText(player.get(TeamSquadFragment.PLAYER_KEY_NUMBER));



        Picasso.with(context)
                .load(player.get(TeamSquadFragment.PLAYER_KEY_IMAGE))
                .placeholder(R.drawable.team_placeholder)
                .into(holder.image);



        // Applying font
        holder.name.setTypeface(tf);
        holder.position.setTypeface(tf);

        holder.tshirt_number.setTypeface(tf);
        holder.appearances_number.setTypeface(tf);
        holder.goals_number.setTypeface(tf);

        holder.appearances.setTypeface(tfAnton);
        holder.number.setTypeface(tfAnton);
        holder.goals.setTypeface(tfAnton);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return SquadList.size();
    }
}
