package com.BuzzKora;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.BuzzKora.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;


/**
 * Created by mhosam on 7/16/17.
 */

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {


    ArrayList<HashMap<String, String>> CalendarDays = new ArrayList<HashMap<String, String>>();
    private int selectedItem;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case






        public TextView DayName;
        public TextView DayNum;
        public TextView Month;
        public LinearLayout wrap;

        private int selectedItem = -1;




        public ViewHolder(View vi) {
            super(vi);
            DayName = (TextView)vi.findViewById(R.id.day_name);
            DayNum = (TextView)vi.findViewById(R.id.day_num);
            Month = (TextView)vi.findViewById(R.id.month);
            wrap = (LinearLayout)vi.findViewById(R.id.calendar_holder);





        }
    }

    public CalendarAdapter(ArrayList<HashMap<String, String>> CalendarDays) {
        this.CalendarDays = CalendarDays;
    }


    @Override
    public int getItemViewType(int position) {
        //your code
       /* HashMap<String, String> day = new HashMap<String, String>();
        day = CalendarDays.get(position);
        String display = day.get(ScheduleFragment.CA_KEY_DISPLAY);

        int type = 1;

        android.util.Log.e("INFO", display);

        if (display.equals("Active")){
            type = 0;
        }

        return type;*/
       return position;
    }

    public void setSelecteditem(int selecteditem) {
        this.selectedItem = selecteditem;
        notifyDataSetChanged();
    }





    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {

        View itemView = null;

        int type = viewType;









            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.calendar_card, parent, false);









        return new ViewHolder(itemView);

        // set the view's size, margins, paddings and layout parameters
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(MoreNewsList[position]);

        HashMap<String, String> day = new HashMap<String, String>();
        day = CalendarDays.get(position);







        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        Log.d("Time zone: ", tz.getDisplayName());
        SimpleDateFormat formatdd = new SimpleDateFormat("EEE");
        SimpleDateFormat formatmm = new SimpleDateFormat("d");
        SimpleDateFormat formatDD = new SimpleDateFormat("MMM");
        formatdd.setTimeZone(tz);
        formatmm.setTimeZone(tz);
        formatDD.setTimeZone(tz);
        Long timestamp = Long.valueOf(day.get(ScheduleFragment.CA_KEY_TIME));
        String localTimeformatdd = formatdd.format(new Date(timestamp * 1000));
        String localTimeformatmm = formatmm.format(new Date(timestamp * 1000));
        String localTimeformatDD = formatDD.format(new Date(timestamp * 1000));

        String fontPathAnton = "fonts/en_font.ttf";
        Typeface tfAnton = Typeface.createFromAsset(holder.DayNum.getContext().getAssets(), fontPathAnton);
        holder.DayNum.setTypeface(tfAnton);

        Context context = holder.DayNum.getContext();

        if (position == 3) {
            holder.DayName.setText("");
            holder.DayNum.setText(R.string.Today);
            holder.Month.setText("");
            String fontPath = "fonts/gedinarone_web-bold-webfont.ttf";
            Typeface tf = Typeface.createFromAsset(holder.DayNum.getContext().getAssets(), fontPath);
            holder.DayNum.setTypeface(tf);
            holder.DayNum.setTextSize(15);

        } else {

            holder.DayName.setText(localTimeformatdd);
            holder.DayNum.setText(localTimeformatmm);
            holder.Month.setText(localTimeformatDD);

        }
        if (selectedItem == position) {
            holder.DayNum.setTextColor(context.getResources().getColor(R.color.calenderActiveTitle));
            holder.wrap.setBackground(context.getResources().getDrawable(R.color.calenderActiveBG));



        } else {
            holder.DayNum.setTextColor(context.getResources().getColor(R.color.calenderTitle));
            holder.wrap.setBackground(context.getResources().getDrawable(R.color.calenderBG));

        }
















    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return CalendarDays.size();
    }
}
