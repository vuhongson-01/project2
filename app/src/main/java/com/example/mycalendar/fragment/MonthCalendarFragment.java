package com.example.mycalendar.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mycalendar.EventViewHandler;
import com.example.mycalendar.MainActivity;
import com.example.mycalendar.R;
import com.example.mycalendar.database.DBHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MonthCalendarFragment extends Fragment implements CalendarView.OnDateChangeListener {

    View view;
    CalendarView calendarView;
    LinearLayout eventContainer;
    DBHelper db;
    TextView backToCurrentMonth;
    DateFormat dateFormat;
    MainActivity main;

    public MonthCalendarFragment(){
        super();
    }
    public MonthCalendarFragment(MainActivity main){
        super();
        this.main = main;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_month_calendar, container, false);

        calendarView = (CalendarView) view.findViewById(R.id.month_calendar_type_view);
        calendarView.setOnDateChangeListener(this);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        backToCurrentMonth = (TextView) view.findViewById(R.id.back_to_current_month);
        backToCurrentMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.setDate(System.currentTimeMillis(),false,true);
                update(dateFormat.format(new Date()));
            }
        });

        db = new DBHelper(getContext());
        eventContainer = (LinearLayout) view.findViewById(R.id.eventContainer);
        update(dateFormat.format(calendarView.getDate()));
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("current_date", dateFormat.format(calendarView.getDate()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            String currentDate = savedInstanceState.getString("current_date", dateFormat.format(new Date()));
            update(currentDate);
        }
    }
    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
        String date = "";
        date += i;
        if (i1+1 < 10) date = date + "/0" + Integer.toString(i1+1);
        else date += "/" + Integer.toString(i1+1);
        if (i2 < 10) date = date + "/0" + Integer.toString(i2);
        else  date += "/" + Integer.toString(i2);

        update(date);
    }

    void update(String date){

        EventViewHandler evh = new EventViewHandler(getContext());
        evh.getLayout(eventContainer, date, main);
    }
}