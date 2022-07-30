package com.example.mycalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mycalendar.database.DBHelper;

import java.util.ArrayList;

public class EventViewHandler {

    private DBHelper db;
    private Context context;
    MainActivity main;
    public EventViewHandler(){
        super();
    }

    public EventViewHandler(Context context){
        this.context = context;
        db = new DBHelper(context);
    }

    public void getLayout(LinearLayout layout, String date, MainActivity main){
        layout.removeAllViews();

        // select db
        Cursor res = db.getEventDataByDate(new String[] {date});
        if (res.getCount() == 0){
            TextView textView = new TextView(context);
            textView.setText("No event");
            layout.addView(textView);
        }
        else {
            // create event component display as list in screen
            ArrayList<String[]> buffer = new ArrayList<String[]>();
            String[] b = new String[8];
            while (res.moveToNext()){
                for (int i = 0; i < 8; i++) {
                    b[i] = res.getString(i);
                }

                LinearLayout eventComponent = new LinearLayout(context);
                eventComponent.setOrientation(LinearLayout.HORIZONTAL);
                createEventComponent(b, eventComponent);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 20, 0,0);
                eventComponent.setLayoutParams(params);
                buffer.add(b);
                layout.addView(eventComponent);

                eventComponent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        main.openEventView(b[0], date, context);
                    }
                });
//
//                int[] attrs = new int[]{androidx.appcompat.R.attr.selectableItemBackground};
//                TypedArray typedArray = this..obtainStyledAttributes(attrs);
//                int backgroundResource = typedArray.getResourceId(0, 0);
            }
        }
    }

    void createEventComponent(String[] ev, LinearLayout eventComponent){

        LinearLayout timeComponent = new LinearLayout(context);
        timeComponent.setPadding(50, 50, 50, 50);
        timeComponent.setOrientation(LinearLayout.VERTICAL);

        TextView startView = new TextView(context);
        startView.setText(ev[3]);
        startView.setTypeface(null, Typeface.BOLD);
        TextView endView = new TextView(context);
        endView.setTypeface(null, Typeface.BOLD);
        endView.setText(ev[4]);

        timeComponent.addView(startView);
        timeComponent.addView(endView);

        eventComponent.addView(timeComponent);

        TextView titleView = new TextView(context);
        titleView.setText(ev[5]);

        titleView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        titleView.setGravity(Gravity.CENTER_VERTICAL);
        titleView.setPaddingRelative(50, 0, 0, 0);

        eventComponent.addView(titleView);
        GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadius( 40 );
        shape.setColor(Color.parseColor(ev[7]));

        eventComponent.setBackground(shape);
        eventComponent.setClickable(true);
    }
}
