package com.example.mycalendar.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.Date;

public class DayCalendarFragment extends Fragment implements View.OnClickListener {

    View view;
    ImageButton preday, nextday;
    TextView currentDayInView, backToCurrentDay;
    Date date;
    DateFormat dateFormat;
    LinearLayout eventContainer;
    DBHelper db;
    public static int currentCalendarView = 0;
    MainActivity main;

    private static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
    public DayCalendarFragment(){
        super();
    }

    public DayCalendarFragment(MainActivity main){
        super();
        this.main = main;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_day_calendar, container, false);

        db = new DBHelper(getContext());
        view.setOnTouchListener(new OnSwipeTouchListener(getContext()){
            public void onSwipeRight() {
                preDay();
            }
            public void onSwipeLeft() {
                nextDay();
            }
        });

        preday = (ImageButton) view.findViewById(R.id.preDayBtn);
        nextday = (ImageButton) view.findViewById(R.id.nextDayBtn);
        backToCurrentDay = (TextView) view.findViewById(R.id.back_to_current_day);

        currentDayInView = (TextView) view.findViewById(R.id.currentDayInView);

        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        date = new Date();

        currentDayInView.setText(dateFormat.format(date));

        preday.setOnClickListener(this);
        nextday.setOnClickListener(this);
        backToCurrentDay.setOnClickListener(this);

        eventContainer = (LinearLayout) view.findViewById(R.id.eventContainer);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("current_date", currentDayInView.getText().toString());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            String currentDate = savedInstanceState.getString("current_date");
            update(currentDate);

        }
        else {
            update(dateFormat.format(new Date()));
        }
    }

    void nextDay(){
        date.setTime(date.getTime() + MILLIS_IN_A_DAY);
        update(dateFormat.format(date.getTime()));
    }

    void preDay(){
        date.setTime(date.getTime() - MILLIS_IN_A_DAY);
        update(dateFormat.format(date.getTime()));
    }

    void update(String date){
        currentDayInView.setText(date);
        EventViewHandler evh = new EventViewHandler(getContext());
        evh.getLayout(eventContainer, date, main);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.preDayBtn){
            preDay();
        }
        else if (id == R.id.nextDayBtn){
            nextDay();
        }
        else if (id == R.id.back_to_current_day){
            date.setTime((new Date()).getTime());
            update(dateFormat.format(new Date()));
        }
    }
}