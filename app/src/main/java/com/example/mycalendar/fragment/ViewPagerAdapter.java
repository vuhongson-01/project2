package com.example.mycalendar.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.mycalendar.MainActivity;


//adapter for TabLayout (day, week, month) view
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    MainActivity main;
    DayCalendarFragment day;
    WeekCalendarFragment week;
    MonthCalendarFragment month;
    public ViewPagerAdapter(MainActivity main, @NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.main = main;
        day =  new DayCalendarFragment(main);
        week = new WeekCalendarFragment(main);
        month = new MonthCalendarFragment(main);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return day;
            case 1:
                return week;
            case 2:
                return month;
            default:
                return day;
        }
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0: {
                title = "Day";
                break;
            }
            case 1:{
                title = "Week";
                break;
            }
            case 2:{
                title = "Month";
                break;
            }
        }
        return title;
    }
}
