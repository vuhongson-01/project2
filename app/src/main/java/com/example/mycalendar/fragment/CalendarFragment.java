package com.example.mycalendar.fragment;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.mycalendar.MainActivity;
import com.example.mycalendar.R;
import com.example.mycalendar.database.DBHelper;
import com.google.android.material.tabs.TabLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarFragment extends FrgmntOverRide{

    View view;
    private TabLayout tabLayout;
    private NoneSwipeableViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    ImageButton newEvent, cancel, save;
    EditText title, note;
    TextView startDate, endDate, startTime, endTime, setColorBtn;
    DateFormat dateFormat;
    SimpleDateFormat timeFormat;
    LinearLayout pickTimeLayout, pickDateLayout;
    DBHelper db;
    String color = "#FF6262";
    MainActivity main;
    public static int currentCalendarView = 0;

    public CalendarFragment(){
        super();
    }
    public CalendarFragment(MainActivity main){
        super();
        this.main = main;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        newEvent = view.findViewById(R.id.add_event_btn);

        viewPagerAdapter = new ViewPagerAdapter(
                main,
                getFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentCalendarView = tabLayout.getSelectedTabPosition();;
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        newEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewEvent();
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();
        viewPagerAdapter = new ViewPagerAdapter(
                main,
                getFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void createNewEvent(){
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View eventPopupView = getLayoutInflater().inflate(R.layout.popup_create_new_event, null);

        dialogBuilder.setView(eventPopupView);
        dialog = dialogBuilder.create();

        cancel = eventPopupView.findViewById(R.id.create_cancel);
        save = eventPopupView.findViewById(R.id.create_save);
        startDate = eventPopupView.findViewById(R.id.date_start_picker);
        endDate = eventPopupView.findViewById(R.id.date_end_picker);
        startTime = eventPopupView.findViewById(R.id.time_start_picker);
        endTime = eventPopupView.findViewById(R.id.time_end_picker);
        setColorBtn = eventPopupView.findViewById(R.id.set_color_button);

        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        timeFormat = new SimpleDateFormat("HH:mm");

        startDate.setText(dateFormat.format(new Date()));
        startTime.setText(timeFormat.format(new Date()));

        endDate.setText(dateFormat.format(new Date()));
        endTime.setText(timeFormat.format(new Date()));

        color = "#FF6262";

        final boolean[] pickColorIsOpen = {false};
        setColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioGroup colorGroup = eventPopupView.findViewById(R.id.color_group);
                pickColorIsOpen[0] = !pickColorIsOpen[0];
                if (pickColorIsOpen[0]) {

                    colorGroup.setVisibility(View.VISIBLE);
                    colorGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {

                            if (i%6 == 1) color = String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.color1)));
                            if (i%6 == 2) color = String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.color2)));
                            if (i%6 == 3) color = String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.color3)));
                            if (i%6 == 4) color = String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.color4)));
                            if (i%6 == 5) color = String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.color5)));
                            if (i%6 == 0) color = String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.color6)));

                            setColorBtn.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN);
                            pickColorIsOpen[0] = !pickColorIsOpen[0];
                            colorGroup.setVisibility(View.GONE);
                        }
                    });
                }
                else {
                    colorGroup.setVisibility(View.GONE);
                }
            }
        });
        //-----------------
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });

        //-----------------
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickTimeLayout = eventPopupView.findViewById(R.id.pick_time_layout);
                pickTimeLayout.setVisibility(View.VISIBLE);
                eventPopupView.findViewById(R.id.pick_date_layout).setVisibility(View.GONE);
                eventPopupView.findViewById(R.id.notice_invalid_time).setVisibility(View.GONE);
                setTime(startTime, pickTimeLayout);
            }
        });
        //-----------------
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDateLayout = eventPopupView.findViewById(R.id.pick_date_layout);
                pickDateLayout.setVisibility(View.VISIBLE);
                eventPopupView.findViewById(R.id.pick_time_layout).setVisibility(View.GONE);
                eventPopupView.findViewById(R.id.notice_invalid_time).setVisibility(View.GONE);
                setDate(startDate, pickDateLayout);
            }
        });
        //----------------
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDateLayout = eventPopupView.findViewById(R.id.pick_date_layout);
                pickDateLayout.setVisibility(View.VISIBLE);

                eventPopupView.findViewById(R.id.pick_time_layout).setVisibility(View.GONE);
                eventPopupView.findViewById(R.id.notice_invalid_time).setVisibility(View.GONE);
                setDate(endDate, pickDateLayout);
            }
        });
        //----------------
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickTimeLayout = eventPopupView.findViewById(R.id.pick_time_layout);
                pickTimeLayout.setVisibility(View.VISIBLE);
                eventPopupView.findViewById(R.id.notice_invalid_time).setVisibility(View.GONE);
                eventPopupView.findViewById(R.id.pick_date_layout).setVisibility(View.GONE);
                setTime(endTime, pickTimeLayout);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = new DBHelper(getContext());

                title = eventPopupView.findViewById(R.id.evetn_title_input);
                note = eventPopupView.findViewById(R.id.event_note);

                DateFormat formatInsertData = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
                String id = formatInsertData.format(new Date());

                if (endDate.getText().toString().compareTo(startDate.getText().toString()) < 0){
                    eventPopupView.findViewById(R.id.notice_invalid_time).setVisibility(View.VISIBLE);
                    return;
                }

                if (endTime.getText().toString().compareTo(startTime.getText().toString()) < 0
                 && endDate.getText().toString().compareTo(startDate.getText().toString()) == 0){
                    eventPopupView.findViewById(R.id.notice_invalid_time).setVisibility(View.VISIBLE);
                    return;
                }

                try {
                    Date sd = new SimpleDateFormat("yyyy/MM/dd").parse(startDate.getText().toString());
                    Date ed = new SimpleDateFormat("yyyy/MM/dd").parse(endDate.getText().toString());

                    int i = 1;

                    if (dateFormat.format(sd).compareTo(dateFormat.format(ed)) < 0){
                        insertDB(db,
                                id+i,
                                dateFormat.format(sd),
                                dateFormat.format(sd),
                                startTime.getText().toString(),
                                "23:59",
                                (title.getText().toString().isEmpty())?"No Title": title.getText().toString(),
                                (note.getText().toString().isEmpty()) ? "No note" : note.getText().toString(),
                                color);

                        sd.setTime(sd.getTime() + 1000 * 60 * 60 * 24);
                        i++;
                    }

                    while (dateFormat.format(sd).compareTo(dateFormat.format(ed)) < 0){
                        insertDB(db,
                                id+i,
                                dateFormat.format(sd),
                                dateFormat.format(sd),
                                "00:00",
                                "23:59",
                                (title.getText().toString().isEmpty())?"No Title": title.getText().toString(),
                                (note.getText().toString().isEmpty()) ? "No note" : note.getText().toString(),
                                color);
                        i++;
                        sd.setTime(sd.getTime() + 1000 * 60 * 60 * 24);
                    }

                    if (i > 1) {
                        insertDB(db,
                                id + i,
                                dateFormat.format(sd),
                                dateFormat.format(sd),
                                "00:00",
                                endTime.getText().toString(),
                                (title.getText().toString().isEmpty()) ? "No Title" : title.getText().toString(),
                                (note.getText().toString().isEmpty()) ? "No note" : note.getText().toString(),
                                color);
                    }
                    else {
                        insertDB(db,
                                id + i,
                                dateFormat.format(sd),
                                dateFormat.format(sd),
                                startTime.getText().toString(),
                                endTime.getText().toString(),
                                (title.getText().toString().isEmpty()) ? "No Title" : title.getText().toString(),
                                (note.getText().toString().isEmpty()) ? "No note" : note.getText().toString(),
                                color);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                onResume();
                dialog.hide();
            }
        });
        dialog.show();
    }

    void insertDB(DBHelper db, String id, String start_date, String end_date, String start_time,
                  String end_time, String title, String note, String color){
        boolean check = db.insertEventData( id,
                                            start_date,
                                            end_date,
                                            start_time,
                                            end_time,
                                            title,
                                            note,
                                            color);
        if (check) {
            Toast toast = Toast.makeText(getContext(), "create done", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(getContext(), "create fail", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    void setTime(TextView timeView,LinearLayout pickTimeLayout){
        ImageButton setTimeDone = pickTimeLayout.findViewById(R.id.set_time_done);
        setTimeDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePicker timePicker = pickTimeLayout.findViewById(R.id.time_picker);

                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                String time = "";
                if (hour < 10) time = time + "0" + hour + ":";
                else time = time + hour + ":";

                if (minute < 10) time = time + "0" + minute;
                else  time = time + minute;

                timeView.setText(time);
                pickTimeLayout.setVisibility(View.GONE);
            }
        });
    }

    void setDate(TextView dateView, LinearLayout pickDateLayout){
        ImageButton setTimeDone = pickDateLayout.findViewById(R.id.set_date_done);
        setTimeDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePicker datePicker =(DatePicker) pickDateLayout.findViewById(R.id.date_picker);

                String date = "";
                date = dateFormat.format(datePicker.getCalendarView().getDate());

                dateView.setText(date);
                pickDateLayout.setVisibility(View.GONE);
            }
        });
    }

}
