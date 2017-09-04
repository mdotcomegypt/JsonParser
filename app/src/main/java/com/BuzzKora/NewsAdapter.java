package com.BuzzKora;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.BuzzKora.R;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by mhosam on 7/12/17.
 */

public class NewsAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    public NewsAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
            if (position == 0 && convertView!=null) {
                vi = inflater.inflate(R.layout.news_head_item, null);
            } else {
                vi = inflater.inflate(R.layout.news_more_item, null);
            }

        TextView title = (TextView)vi.findViewById(R.id.NewsTitle);
        TextView author = (TextView)vi.findViewById(R.id.NewsAuthor);
        ImageView image = (ImageView)vi.findViewById(R.id.NewsImage);
        TextView date = (TextView)vi.findViewById(R.id.NewsDate);





        HashMap<String, String> article = new HashMap<String, String>();
        article = data.get(position);

        // Setting all values in listview
        title.setText(article.get(NewsFragment.NEWS_KEY_TITLE));
        author.setText(article.get(NewsFragment.NEWS_KEY_AUTHOR));
        date.setText(article.get(NewsFragment.NEWS_KEY_DATE));

        Picasso.with(activity.getApplicationContext())
                .load(article.get(NewsFragment.NEWS_KEY_IMAGE))
                .placeholder(R.drawable.placeholder)
                .into(image);

        String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(title.getContext().getAssets(), fontPath);


        // Applying font
        title.setTypeface(tf);

        return vi;
    }
}