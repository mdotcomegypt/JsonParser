package com.BuzzKora;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.BuzzKora.RegisterActivityStepB.currentPos;


/**
 * Created by mhosam on 7/16/17.
 */

public class FavoritTitlesAdapter extends RecyclerView.Adapter<FavoritTitlesAdapter.ViewHolder>  {


    String[] TitlesList;

    public int selectedItem = 0;

    public Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        public TextView title;
        private int selectedItem = -1;

        public ViewHolder(View vi) {
            super(vi);
            title = (TextView)vi.findViewById(R.id.Title);

        }
    }



    public FavoritTitlesAdapter(String[]  TitlesList) {
        this.TitlesList = TitlesList;
    }



    @Override
    public int getItemViewType(int position) {

        //your code
        return position;
    }

    @Override
    public FavoritTitlesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {

        View itemView = null;


             itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.register_titles_item, parent, false);


        return new ViewHolder(itemView);

        // set the view's size, margins, paddings and layout parameters
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(MoreNewsList[position]);




        holder.title.setText(TitlesList[position]);

        context = holder.title.getContext();




        android.util.Log.e("INFO", String.valueOf(currentPos));

        if (position == currentPos){

            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.RegisterLeaguesBGSelected));

        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.RegisterLeaguesBG));
        }







        String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(holder.title.getContext().getAssets(), fontPath);


        // Applying font
        holder.title.setTypeface(tf);





    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return TitlesList.length;
    }








}
