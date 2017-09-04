package com.BuzzKora;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static com.BuzzKora.FavoritActivity.FavoritFragment.FAV_IMAGE;
import static com.BuzzKora.FavoritActivity.FavoritFragment.FAV_NAME;
import static com.BuzzKora.MainActivity.MenuPos;
import static com.BuzzKora.RegisterActivityStepB.currentPos;


/**
 * Created by mhosam on 7/16/17.
 */

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.ViewHolder> {
    private String[] MenuArray;
    public MainMenuAdapter(String[] dataArgs){
        MenuArray = dataArgs;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txt;

        public ViewHolder(View vi) {
            super(vi);
            txt = (TextView)vi.findViewById(R.id.txt);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_menu_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Context context = holder.txt.getContext();


        String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
        Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPath);

        if (position == MenuPos){
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.MainMenuActiveBG));
            holder.txt.setTextColor(ContextCompat.getColor(context, R.color.MainMenuActiveTXT));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.MainMenuItemBG));
            holder.txt.setTextColor(ContextCompat.getColor(context, R.color.MainMenuItemTXT));
        }

        holder.txt.setText(MenuArray[position]);
        holder.txt.setTypeface(tf);
    }

    @Override
    public int getItemCount() {
        return MenuArray.length;
    }


}