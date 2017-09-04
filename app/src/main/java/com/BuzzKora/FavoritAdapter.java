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

import static android.support.v4.content.ContextCompat.getDrawable;
import static com.BuzzKora.FavoritActivity.FavoritFragment.FAVE_KEY;
import static com.BuzzKora.FavoritActivity.FavoritFragment.FAV_IMAGE;
import static com.BuzzKora.FavoritActivity.FavoritFragment.FAV_NAME;


/**
 * Created by mhosam on 7/16/17.
 */

public class FavoritAdapter extends RecyclerView.Adapter<FavoritAdapter.ViewHolder> {


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

    public FavoritAdapter(ArrayList<HashMap<String, String>> FavList) {
        this.FavList = FavList;
    }



    @Override
    public int getItemViewType(int position) {

        //your code
        return position;
    }

    @Override
    public FavoritAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {

        View itemView = null;

        int position=viewType;




             itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fav_card, parent, false);


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


        final SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);

        final String[] splitedKeys = fav.get(FAVE_KEY).split(",");
        final String  key = splitedKeys[0];




        //check for default icon

        flag= SP.getBoolean(key,false);

        if (flag){
            holder.follow.setBackgroundResource(R.drawable.selected_borders);
        } else {
            holder.follow.setBackgroundResource(0);
        }




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flag= SP.getBoolean(key,false);

                SharedPreferences.Editor editor = SP.edit();

                if (flag){

                    holder.follow.setBackgroundResource(0);

                    for (int x = 0; x < splitedKeys.length; x++) {
                        String NewKey = splitedKeys[x];
                        editor.putBoolean(NewKey, false);
                        android.util.Log.e("PRESSED", "Remove from Favs " + NewKey);
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(NewKey);
                    }

                    editor.commit();


                } else {

                    holder.follow.setBackgroundResource(R.drawable.selected_borders);

                    for (int x = 0; x < splitedKeys.length; x++) {
                        String NewKey = splitedKeys[x];
                        editor.putBoolean(NewKey, true);
                        android.util.Log.e("PRESSED", "Add to Favs " + NewKey);
                        FirebaseMessaging.getInstance().subscribeToTopic(NewKey);
                    }

                    editor.commit();


                }
            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return FavList.size();
    }


}
