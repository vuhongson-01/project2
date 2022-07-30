package com.example.mycalendar.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.example.mycalendar.MainActivity;
import com.example.mycalendar.R;
import com.example.mycalendar.database.DBHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventView extends FrgmntOverRide {

    View view;
    ImageButton save, exit;
    MainActivity main;
    String id, color;
    DBHelper db;
    EditText title, note;
    TextView colorBtn, dateStart, dateEnd, timeStart, timeEnd, editBtn, cancelBtn, deleteBtn;
    RadioGroup colorGroup;
    LinearLayout pickTimeLayout, pickDateLayout;
    DateFormat dateFormat, timeFormat;
    Button noBtn, yesBtn;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private boolean editable = false;

    // process when click for display detail event
    public EventView(String id, MainActivity main, Context context){
        super();
        this.main = main;
        this.id = id;
        db = new DBHelper(context);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.event_view, container, false);
        save = view.findViewById(R.id.view_save);
        exit = view.findViewById(R.id.view_cancel);
        deleteBtn = view.findViewById(R.id.delete_btn);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        timeFormat = new SimpleDateFormat("HH:mm");
        colorBtn = view.findViewById(R.id.color_button_edit);
        save.setVisibility(View.GONE);
        deleteBtn.setVisibility(View.GONE);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.closeEventView("2022/07/28");
            }
        });

        ArrayList<String[]> buffer = new ArrayList<String[]>();
        String[] b = new String[8];
        Cursor res = db.getEventDataByID(new String[] {id});
        res.moveToNext();
        for (int i = 0; i < 8; i++) {
            b[i] = res.getString(i);
        }

        title = view.findViewById(R.id.event_title_display);
        title.setText(b[5]);
        activeEdittext(title, false);

        pickDateLayout = view.findViewById(R.id.pick_date_layout_edit);
        dateStart = view.findViewById(R.id.date_start);
        dateStart.setText(b[1]);
        dateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                if (editable){
                    pickDateLayout.setVisibility(View.VISIBLE);
                    view.findViewById(R.id.pick_time_layout_edit).setVisibility(View.GONE);
                    view.findViewById(R.id.notice_invalid_time_edit).setVisibility(View.GONE);
                    setDate(dateStart, pickDateLayout);
                }
            }
        });

        dateEnd = view.findViewById(R.id.date_end);
        dateEnd.setText(b[2]);

        dateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                if (editable){
                    pickDateLayout.setVisibility(View.VISIBLE);
                    view.findViewById(R.id.pick_time_layout_edit).setVisibility(View.GONE);
                    view.findViewById(R.id.notice_invalid_time_edit).setVisibility(View.GONE);
                    setDate(dateEnd, pickDateLayout);
                }
            }
        });

        pickTimeLayout = view.findViewById(R.id.pick_time_layout_edit);
        timeStart = view.findViewById(R.id.time_start);
        timeStart.setText(b[3]);
        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                if (editable){
                    pickTimeLayout.setVisibility(View.VISIBLE);
                    view.findViewById(R.id.pick_date_layout_edit).setVisibility(View.GONE);
                    view.findViewById(R.id.notice_invalid_time_edit).setVisibility(View.GONE);
                    setTime(timeStart, pickTimeLayout);
                }
            }
        });

        timeEnd = view.findViewById(R.id.time_end);
        timeEnd.setText(b[4]);
        timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                if (editable){
                    pickTimeLayout.setVisibility(View.VISIBLE);
                    view.findViewById(R.id.notice_invalid_time_edit).setVisibility(View.GONE);
                    view.findViewById(R.id.pick_date_layout_edit).setVisibility(View.GONE);
                    setTime(timeEnd, pickTimeLayout);
                }
            }
        });

        note = view.findViewById(R.id.event_note);
        note.setText(b[6]);
        activeEdittext(note, false);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogBuilder = new AlertDialog.Builder(getContext());
                final View eventPopupView = getLayoutInflater().inflate(R.layout.popup_cf, null);
                dialogBuilder.setView(eventPopupView);
                dialog = dialogBuilder.create();

                noBtn = eventPopupView.findViewById(R.id.no_cf);
                yesBtn = eventPopupView.findViewById(R.id.yes_cf);

                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.hide();
                    }
                });

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean check = db.deleteEventData(b[0]);
                        main.closeEventView("2022/07/28");

                        if (check){
                            Toast toast = Toast.makeText(getContext(), "delete event done", Toast.LENGTH_SHORT);
                            toast.show();
                        }

                        else {
                            Toast toast = Toast.makeText(getContext(), "something wrong!", Toast.LENGTH_SHORT);
                            toast.show();
                        }

                        dialog.hide();
                    }
                });

                dialog.show();
            }
        });

        editBtn = view.findViewById(R.id.edit_btn);
        cancelBtn = view.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editable = false;
                activeEdittext(title, false);
                activeEdittext(note, false);
                exit.setVisibility(View.VISIBLE);
                save.setVisibility(View.GONE);
                editBtn.setVisibility(View.VISIBLE);
                cancelBtn.setVisibility(View.GONE);
                pickDateLayout.setVisibility(View.GONE);
                title.setText(b[5]);
                colorBtn.getBackground().setColorFilter(Color.parseColor(b[7]), PorterDuff.Mode.SRC_IN);
                note.setText(b[6]);
                dateStart.setText(b[1]);
                dateEnd.setText(b[2]);
                timeStart.setText(b[3]);
                timeEnd.setText(b[4]);
                deleteBtn.setVisibility(View.GONE);
                Toast toast = Toast.makeText(getContext(), "edit cancel", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);
                editBtn.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.VISIBLE);
                activeEdittext(title, true);
                activeEdittext(note, true);
                deleteBtn.setVisibility(View.VISIBLE);
                editable = true;
            }
        });

        color = b[7];
        colorBtn.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN);
        colorGroup = view.findViewById(R.id.edit_color_group);
        final boolean[] pickColorIsOpen = {false};
        colorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editable){
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

                                colorBtn.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN);
                                pickColorIsOpen[0] = !pickColorIsOpen[0];
                                colorGroup.setVisibility(View.GONE);
                            }
                        });
                    }
                    else {
                        colorGroup.setVisibility(View.GONE);
                    }
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {

                String id = b[0].substring(0, b[0].length()-1);

                if (dateEnd.getText().toString().compareTo(dateStart.getText().toString()) < 0){
                    view.findViewById(R.id.notice_invalid_time).setVisibility(View.VISIBLE);
                    return;
                }

                if (timeEnd.getText().toString().compareTo(timeStart.getText().toString()) < 0
                        && dateEnd.getText().toString().compareTo(dateStart.getText().toString()) == 0){
                    view.findViewById(R.id.notice_invalid_time).setVisibility(View.VISIBLE);
                    return;
                }

                try {
                    Date sd = new SimpleDateFormat("yyyy/MM/dd").parse(dateStart.getText().toString());
                    Date ed = new SimpleDateFormat("yyyy/MM/dd").parse(dateEnd.getText().toString());

                    int i = 1;
                    db.deleteEventData(id);
                    if (dateFormat.format(sd).compareTo(dateFormat.format(ed)) < 0){
                        insertDB(db,
                                id+i,
                                dateFormat.format(sd),
                                dateFormat.format(sd),
                                timeStart.getText().toString(),
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
                                timeEnd.getText().toString(),
                                (title.getText().toString().isEmpty()) ? "No Title" : title.getText().toString(),
                                (note.getText().toString().isEmpty()) ? "No note" : note.getText().toString(),
                                color);
                    }
                    else {
                        insertDB(db,
                                id + i,
                                dateFormat.format(sd),
                                dateFormat.format(sd),
                                timeStart.getText().toString(),
                                timeEnd.getText().toString(),
                                (title.getText().toString().isEmpty()) ? "No Title" : title.getText().toString(),
                                (note.getText().toString().isEmpty()) ? "No note" : note.getText().toString(),
                                color);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                main.closeEventView("2022/07/28");
            }
        });
        return view;
    }

    void activeEdittext(EditText editText, boolean state){
        editText.setFocusable(state);
        editText.setFocusableInTouchMode(state);
        editText.setClickable(state);
    }
    void setTime(TextView timeView,LinearLayout pickTimeLayout){
        ImageButton setTimeDone = pickTimeLayout.findViewById(R.id.set_time_done);
        setTimeDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                TimePicker timePicker = view.findViewById(R.id.time_picker_edit);

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
            public void onClick(View view1) {
                DatePicker datePicker =(DatePicker) view.findViewById(R.id.date_picker_edit);
                String date = "";
                date = dateFormat.format(datePicker.getCalendarView().getDate());
                dateView.setText(date);
                pickDateLayout.setVisibility(View.GONE);
            }
        });
    }
    public void insertDB(DBHelper db, String id, String start_date, String end_date, String start_time,
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
                Toast toast = Toast.makeText(getContext(), "update done", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getContext(), "update fail, something went wrong", Toast.LENGTH_SHORT);
                toast.show();
            }
    }
}
