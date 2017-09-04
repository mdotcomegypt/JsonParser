package com.BuzzKora;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.BuzzKora.R;
import com.squareup.picasso.Picasso;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;

public class ArticleActivity extends AppCompatActivity {

    public  HashMap<String, String> article;

    static final String NEWS_KEY_TITLE = "title"; // parent node
    static final String  NEWS_KEY_THUMB = "thumb";
    static final String  NEWS_KEY_IMAGE = "image";
    static final String  NEWS_KEY_DATE = "date";
    static final String  NEWS_KEY_BODY = "body";
    static final String  NEWS_KEY_AUTHOR = "author";
    static final String  NEWS_KEY_URL = "url";
    public TextView toolbar_title;

    ArrayList<HashMap<String, String>> NewsList ;
    ArrayList<HashMap<String, String>> OriginalNewsList ;
    ArrayList<HashMap<String, String>> MoreNewsList ;


    RecyclerView Newslistview;

    private boolean NewsLoading = true;

    private AdView mAdView;



    private RecyclerView recyclerView;
    private MoreNewsListAdapter MoreNewsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);











        //article = (HashMap<String, String>) getIntent().getExtras().get("Article");

        Integer position = (Integer) getIntent().getExtras().get("position");

        NewsList = (ArrayList<HashMap<String, String>>) getIntent().getExtras().get("NewsList");
        OriginalNewsList = (ArrayList<HashMap<String, String>>) getIntent().getExtras().get("NewsList");

        article = NewsList.get(position);




        TextView title = (TextView) findViewById(R.id.NewsTitle);
        TextView author = (TextView) findViewById(R.id.NewsAuthor);
        ImageView image = (ImageView) findViewById(R.id.NewsImage);
        TextView date = (TextView) findViewById(R.id.NewsDate);
        TextView body = (TextView) findViewById(R.id.NewsBody);
        TextView MoreNewsTitle = (TextView) findViewById(R.id.MoreNewsTitle);


        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //collapsingToolbarLayout.setTitle(article.get(News.NEWS_KEY_TITLE));
        //collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);



        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(collapsingToolbarLayout.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapsingToolbarLayout)) {
                    toolbar_title.animate().alpha(1).setDuration(400);
                } else {
                    toolbar_title.animate().alpha(0).setDuration(400);
                }
            }
        });


        

        



        title.setText(article.get(NewsFragment.NEWS_KEY_TITLE));
        toolbar_title.setText(article.get(NewsFragment.NEWS_KEY_TITLE));
        author.setText(article.get(NewsFragment.NEWS_KEY_AUTHOR));
        date.setText(article.get(NewsFragment.NEWS_KEY_DATE));
        body.setText(article.get(NewsFragment.NEWS_KEY_BODY));

        Picasso.with(this.getApplicationContext())
                .load(article.get(NewsFragment.NEWS_KEY_IMAGE))
                .placeholder(R.drawable.placeholder)
                .into(image);

        String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
        Typeface tf = Typeface.createFromAsset(title.getContext().getAssets(), fontPath);


        String ReadingfontPath = "fonts/GeezaPro-Bold.ttf";
        Typeface rtf = Typeface.createFromAsset(title.getContext().getAssets(), ReadingfontPath);


        Button source = (Button) findViewById(R.id.source);

        source.setTypeface(tf);

        source.setOnClickListener(new View.OnClickListener() {
            @Override
            //On click function
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("nid", article.get(NewsFragment.NEWS_KEY_SOURCE));
                Intent intent = new Intent(view.getContext(), BrowserActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });






        // Applying font
        title.setTypeface(tf);
        toolbar_title.setTypeface(tf);
        body.setTypeface(rtf);
        MoreNewsTitle.setTypeface(tf);
        author.setTypeface(tf);
        //collapsingToolbarLayout.setExpandedTitleTypeface(tf);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.share);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = article.get(NewsFragment.NEWS_KEY_TITLE) + " " + article.get(NewsFragment.NEWS_KEY_URL) ;
                String shareSub = article.get(NewsFragment.NEWS_KEY_TITLE);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));


            }
        });












        //NewsList.subList(position, position+1).clear();

        if (NewsList.size() > 6) {

            NewsList.subList(6, NewsList.size()).clear();
        }











        MoreNewsListAdapter = new MoreNewsListAdapter(NewsList);



        Newslistview = (RecyclerView) findViewById(R.id.Newslistview);
        int numOfCol = calculateNoOfColumns(getApplicationContext());
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());int numOfCol = calculateNoOfColumns(getActivity().getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager =  new GridLayoutManager(getApplicationContext(), numOfCol);
        Newslistview.setLayoutManager(mLayoutManager);
        Newslistview.setItemAnimator(new DefaultItemAnimator());
        Newslistview.setAdapter(MoreNewsListAdapter);


        Newslistview.setVisibility(View.VISIBLE);


        Newslistview.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), Newslistview ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, final int position) {
                        //HashMap<String, String> article = NewsList.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("NewsList", OriginalNewsList);
                        bundle.putInt("position", position);

                        Intent intent = new Intent(ArticleActivity.this,
                                ArticleActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);



                    }

                    @Override public void onLongItemClick(View view, int position) {
                               /* HashMap<String, String> article = MoreNewsList.get(position);
                                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                String shareBody = article.get(NewsFragment.NEWS_KEY_TITLE) + " " + article.get(NewsFragment.NEWS_KEY_URL) ;
                                String shareSub = article.get(NewsFragment.NEWS_KEY_TITLE);
                                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                                startActivity(Intent.createChooser(sharingIntent, "Share using"));*/
                    }
                })
        );
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 260;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }


    public int getPosition(Context context) {
        Integer position = (Integer) getIntent().getExtras().get("position");
        return position;
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsApp.getInstance().trackScreenView("News|"+article.get(NewsFragment.NEWS_KEY_TITLE));

    }
}
