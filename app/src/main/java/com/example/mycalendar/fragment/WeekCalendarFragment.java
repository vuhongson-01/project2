package com.example.mycalendar.fragment;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;

import java.util.Calendar;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mycalendar.EventViewHandler;
import com.example.mycalendar.MainActivity;
import com.example.mycalendar.R;
import com.example.mycalendar.database.DBHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class WeekCalendarFragment extends Fragment implements View.OnClickListener {

    ImageButton preweek, nextweek;
    View view;
    TextView currentWeekInView, d1,d2,d3,d4,d5,d6,d7,backToCurrentWeek;
    DateFormat dateFormat;
    Calendar c;
    String currentDayChoice = "", dateChoiced;
    GradientDrawable shape =  new GradientDrawable();
    String [] appearenceWeek = new String[7];
    LinearLayout eventContainer, mon, tue, wed, thu, fri, sat, sun;
    DBHelper db;
    MainActivity main;

    public WeekCalendarFragment(){
        super();
    }
    public WeekCalendarFragment(MainActivity main){
        super();
        this.main = main;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_week_calendar, container, false);

        view.setOnTouchListener(new OnSwipeTouchListener(getContext()){
            public void onSwipeRight() {
                preWeek();
            }
            public void onSwipeLeft() {
                nextWeek();
            }
        });

        backToCurrentWeek = (TextView) view.findViewById(R.id.back_to_current_week);
        preweek = (ImageButton) view.findViewById(R.id.preWeekBtn);
        nextweek = (ImageButton) view.findViewById(R.id.nextWeekBtn);

        preweek.setOnClickListener(this);
        nextweek.setOnClickListener(this);
        backToCurrentWeek.setOnClickListener(this);
        currentWeekInView = (TextView) view.findViewById(R.id.currentWeekInView);

        dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        d1 = (TextView) view.findViewById(R.id.sun);
        d2 = (TextView) view.findViewById(R.id.mon);
        d3 = (TextView) view.findViewById(R.id.tue);
        d4 = (TextView) view.findViewById(R.id.wed);
        d5 = (TextView) view.findViewById(R.id.thu);
        d6 = (TextView) view.findViewById(R.id.fri);
        d7 = (TextView) view.findViewById(R.id.sat);

        mon = view.findViewById(R.id.mondayComponent);
        tue = view.findViewById(R.id.tuesdayComponent);
        wed = view.findViewById(R.id.wednesdayComponent);
        thu = view.findViewById(R.id.thusdayComponent);
        fri = view.findViewById(R.id.fridayComponent);
        sat = view.findViewById(R.id.saturdayComponent);
        sun = view.findViewById(R.id.sundayComponent);

        mon.setOnClickListener(this);
        tue.setOnClickListener(this);
        wed.setOnClickListener(this);
        thu.setOnClickListener(this);
        fri.setOnClickListener(this);
        sat.setOnClickListener(this);
        sun.setOnClickListener(this);

        db = new DBHelper(getContext());

        c = Calendar.getInstance();
        eventContainer = (LinearLayout) view.findViewById(R.id.eventContainer);
        updateCurrentWeekInView(c);
        return view;
    }

    private void updateCurrentWeekInView(Calendar c) {
        currentDayChoice = dateFormat.format(c.getTime());
        update(currentDayChoice);

        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String w = "";
        w = w + dateFormat.format(c.getTime()).substring(5, 10);

        appearenceWeek[0] = dateFormat.format(c.getTime());
        d1.setText(dateFormat.format(c.getTime()).substring(8,10));
        if (dateFormat.format(c.getTime()).equals(currentDayChoice))
            focus(sun.getId());
        if (haveEvent(dateFormat.format(c.getTime()))) view.findViewById(R.id.sunEvent).setVisibility(View.VISIBLE);
        else view.findViewById(R.id.sunEvent).setVisibility(View.INVISIBLE);

        c.add(Calendar.DATE, 1);
        appearenceWeek[1] = dateFormat.format(c.getTime());
        d2.setText(dateFormat.format(c.getTime()).substring(8,10));
        if (dateFormat.format(c.getTime()).equals(currentDayChoice))
            focus(mon.getId());
        if (haveEvent(dateFormat.format(c.getTime()))) view.findViewById(R.id.monEvent).setVisibility(View.VISIBLE);
        else view.findViewById(R.id.monEvent).setVisibility(View.INVISIBLE);

        c.add(Calendar.DATE, 1);
        appearenceWeek[2] = dateFormat.format(c.getTime());
        d3.setText(dateFormat.format(c.getTime()).substring(8,10));
        if (dateFormat.format(c.getTime()).equals(currentDayChoice))
            focus(tue.getId());
        if (haveEvent(dateFormat.format(c.getTime()))) view.findViewById(R.id.tueEvent).setVisibility(View.VISIBLE);
        else view.findViewById(R.id.tueEvent).setVisibility(View.INVISIBLE);

        c.add(Calendar.DATE, 1);
        appearenceWeek[3] = dateFormat.format(c.getTime());
        d4.setText(dateFormat.format(c.getTime()).substring(8,10));
        if (dateFormat.format(c.getTime()).equals(currentDayChoice))
            focus(wed.getId());
        if (haveEvent(dateFormat.format(c.getTime()))) view.findViewById(R.id.wedEvent).setVisibility(View.VISIBLE);
        else view.findViewById(R.id.wedEvent).setVisibility(View.INVISIBLE);

        c.add(Calendar.DATE, 1);
        appearenceWeek[4] = dateFormat.format(c.getTime());
        d5.setText(dateFormat.format(c.getTime()).substring(8,10));
        if (dateFormat.format(c.getTime()).equals(currentDayChoice))
            focus(thu.getId());
        if (haveEvent(dateFormat.format(c.getTime()))) view.findViewById(R.id.thuEvent).setVisibility(View.VISIBLE);
        else view.findViewById(R.id.thuEvent).setVisibility(View.INVISIBLE);

        c.add(Calendar.DATE, 1);
        appearenceWeek[5] = dateFormat.format(c.getTime());
        d6.setText(dateFormat.format(c.getTime()).substring(8,10));
        if (dateFormat.format(c.getTime()).equals(currentDayChoice))
            focus(fri.getId());
        if (haveEvent(dateFormat.format(c.getTime()))) view.findViewById(R.id.friEvent).setVisibility(View.VISIBLE);
        else view.findViewById(R.id.friEvent).setVisibility(View.INVISIBLE);

        c.add(Calendar.DATE, 1);
        appearenceWeek[6] = dateFormat.format(c.getTime());
        d7.setText(dateFormat.format(c.getTime()).substring(8,10));
        if (dateFormat.format(c.getTime()).equals(currentDayChoice))
            focus(sat.getId());
        if (haveEvent(dateFormat.format(c.getTime()))) view.findViewById(R.id.satEvent).setVisibility(View.VISIBLE);
        else view.findViewById(R.id.satEvent).setVisibility(View.INVISIBLE);

        w = w + " - " + dateFormat.format(c.getTime()).substring(5, 10);
        currentWeekInView.setText(w);

        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
    }

    boolean haveEvent(String date){
        Cursor res = db.getEventDataByDate(new String[] {date});
        if (res.getCount() != 0) return true;
        return false;
    }
    void nextWeek(){
        c.add(Calendar.DATE, 7);
        updateCurrentWeekInView(c);
    }

    void preWeek(){
        c.add(Calendar.DATE, -7);
        updateCurrentWeekInView(c);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.preWeekBtn){
            preWeek();
        }
        else if (id == R.id.nextWeekBtn){
            nextWeek();
        }
        else if (id == R.id.back_to_current_week){
            c = Calendar.getInstance();
            updateCurrentWeekInView(c);
        }
        else {
            focus(id);
        }
    }

    private void focus(int id) {
        //focus on day in week bar
        d1.setTextColor(Color.GRAY);
        d2.setTextColor(Color.GRAY);
        d3.setTextColor(Color.GRAY);
        d4.setTextColor(Color.GRAY);
        d5.setTextColor(Color.GRAY);
        d6.setTextColor(Color.GRAY);
        d7.setTextColor(Color.GRAY);

        sun.setBackground(null);
        mon.setBackground(null);
        tue.setBackground(null);
        wed.setBackground(null);
        thu.setBackground(null);
        fri.setBackground(null);
        sat.setBackground(null);

        TextView tv;
        if (id == sun.getId()) {
            tv = d1;
            dateChoiced = appearenceWeek[0];
        }
        else if (id == mon.getId()) {
            tv = d2;
            dateChoiced = appearenceWeek[1];
        }
        else if (id == tue.getId()) {
            tv = d3;
            dateChoiced = appearenceWeek[2];
        }
        else if (id == wed.getId()) {
            tv = d4;
            dateChoiced = appearenceWeek[3];
        }
        else if (id == thu.getId()) {
            tv = d5;
            dateChoiced = appearenceWeek[4];
        }
        else if (id == fri.getId()) {
            tv = d6;
            dateChoiced = appearenceWeek[5];
        }
        else {
            dateChoiced = appearenceWeek[6];
            tv = d7;
        }

        update(dateChoiced);
        shape.setStroke(5, Color.parseColor("#FF30B5BD"));
        view.findViewById(id).setBackground(shape);
        tv.setTextColor(Color.parseColor("#FF30B5BD"));
    }

    void update(String date){
        EventViewHandler evh = new EventViewHandler(getContext());
        evh.getLayout(eventContainer, date, main);
    }
}