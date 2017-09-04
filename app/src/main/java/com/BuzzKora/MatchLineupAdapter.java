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

import static com.BuzzKora.MatchLineupFragment.LINEUP_IMAGE;
import static com.BuzzKora.MatchLineupFragment.LINEUP_NAME;
import static com.BuzzKora.MatchLineupFragment.LINEUP_NUMBER;
import static com.BuzzKora.MatchLineupFragment.LINEUP_POSITION_AR;
import static com.BuzzKora.MatchLineupFragment.LINEUP_TEAM;


/**
 * Created by mhosam on 7/16/17.
 */

public class MatchLineupAdapter extends RecyclerView.Adapter<MatchLineupAdapter.ViewHolder> {


    ArrayList<HashMap<String, String>> LineUpList = new ArrayList<HashMap<String, String>>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        public TextView number;
        public TextView name;
        public TextView position;
       /* public TextView position;
        public TextView x;
        public TextView y;*/
       public ImageView image;



        public ViewHolder(View vi) {
            super(vi);
            number = (TextView)vi.findViewById(R.id.number);
            name = (TextView)vi.findViewById(R.id.name);
            image = (ImageView)vi.findViewById(R.id.image);
            position = (TextView)vi.findViewById(R.id.position);
            /*  x = (TextView)vi.findViewById(R.id.x);
            y = (TextView)vi.findViewById(R.id.y);*/

        }
    }

    public MatchLineupAdapter(ArrayList<HashMap<String, String>> LineUpList) {
        this.LineUpList = LineUpList;
    }



    @Override
    public int getItemViewType(int position) {
        HashMap<String, String> row = new HashMap<String, String>();
        row = LineUpList.get(position);
        if (row.get(LINEUP_TEAM).equals("A")){
            return 0;
        } else {
            return 1;
        }

    }

    @Override
    public MatchLineupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {

        View itemView = null;

            if (viewType == 0) {
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.match_lineup_row_a, parent, false);
            }
            else {
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.match_lineup_row_a, parent, false);
            }


            return new ViewHolder(itemView);


    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        HashMap<String, String> row = new HashMap<String, String>();
        row = LineUpList.get(position);
        holder.number.setText(row.get(LINEUP_NUMBER));
        holder.name.setText(row.get(LINEUP_NAME));
        holder.position.setText(row.get(LINEUP_POSITION_AR));

        String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
        Typeface tf = Typeface.createFromAsset(holder.name.getContext().getAssets(), fontPath);


        String fontPathAnton = "fonts/en_font_bold.ttf";
        Typeface tfAnton = Typeface.createFromAsset(holder.name.getContext().getAssets(), fontPathAnton);

        holder.number.setTypeface(tfAnton);
        holder.name.setTypeface(tf);
        holder.position.setTypeface(tf);

        /*holder.x.setText(row.get(LINEUP_X));
        holder.y.setText(row.get(LINEUP_Y));*/

        Context context = holder.image.getContext();

        Picasso.with(context).load(row.get(LINEUP_IMAGE)).into(holder.image);


    }

    @Override
    public int getItemCount() {
        return LineUpList.size();
    }
}
