package com.BuzzKora;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static com.BuzzKora.FavoritActivity.FavoritFragment.FAVE_KEY;
import static com.BuzzKora.FavoritActivity.FavoritFragment.FAV_IMAGE;
import static com.BuzzKora.FavoritActivity.FavoritFragment.FAV_NAME;


/**
 * Created by mhosam on 7/16/17.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {


    ArrayList<HashMap<String, String>> FavList = new ArrayList<HashMap<String, String>>();

    public boolean flag;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        public TextView name;
        public ImageView image;
        public LinearLayout follow;
        public ViewHolder(View vi) {
            super(vi);
            name = (TextView)vi.findViewById(R.id.name);
            image = (ImageView)vi.findViewById(R.id.image);
            follow = (LinearLayout) vi.findViewById(R.id.follow);
        }
    }

    public MenuAdapter(ArrayList<HashMap<String, String>> FavList) {
        this.FavList = FavList;
    }



    @Override
    public int getItemViewType(int position) {

        //your code
        return position;
    }

    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {

        View itemView = null;

        int position=viewType;




             itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.menu_item, parent, false);


        return new ViewHolder(itemView);

        // set the view's size, margins, paddings and layout parameters
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(MoreNewsList[position]);

        HashMap<String, String> fav = new HashMap<String, String>();
        fav = FavList.get(position);


        holder.name.setText(fav.get(FAV_NAME));

        final Context context = holder.image.getContext();

        Picasso.with(context)
                .load(fav.get(FAV_IMAGE))
                .placeholder(R.drawable.team_placeholder)
                .into(holder.image);

        String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
        Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPath);

        holder.name.setTypeface(tf);

















    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return FavList.size();
    }


}
