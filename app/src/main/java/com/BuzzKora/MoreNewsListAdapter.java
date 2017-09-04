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

public class MoreNewsListAdapter extends RecyclerView.Adapter<MoreNewsListAdapter.ViewHolder> {


    ArrayList<HashMap<String, String>> NewsList = new ArrayList<HashMap<String, String>>();


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        public TextView title;
        public TextView author;
        public ImageView image;
        public TextView date;

        public ViewHolder(View vi) {
            super(vi);
            title = (TextView)vi.findViewById(R.id.NewsTitle);
            author = (TextView)vi.findViewById(R.id.NewsAuthor);
            image = (ImageView)vi.findViewById(R.id.NewsImage);
            date = (TextView)vi.findViewById(R.id.NewsDate);

        }
    }

    public MoreNewsListAdapter(ArrayList<HashMap<String, String>> NewsList) {
        this.NewsList = NewsList;
    }



    @Override
    public int getItemViewType(int position) {

        //your code
        return position;
    }

    @Override
    public MoreNewsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {

        View itemView = null;





            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_more_item, parent, false);



        return new ViewHolder(itemView);

        // set the view's size, margins, paddings and layout parameters
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(MoreNewsList[position]);

        HashMap<String, String> article = new HashMap<String, String>();
        article = NewsList.get(position);



            holder.title.setText(article.get(NewsFragment.NEWS_KEY_TITLE));
            holder.author.setText(article.get(NewsFragment.NEWS_KEY_AUTHOR));
            holder.date.setText(article.get(NewsFragment.NEWS_KEY_DATE));

            Context context = holder.image.getContext();

            Picasso.with(context)
                    .load(article.get(NewsFragment.NEWS_KEY_IMAGE))
                    .placeholder(R.drawable.placeholder)
                    .into(holder.image);

            String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
            // Loading Font Face
            Typeface tf = Typeface.createFromAsset(holder.title.getContext().getAssets(), fontPath);


            // Applying font
            holder.title.setTypeface(tf);



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return NewsList.size();
    }


}
