package com.example.mycalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.mycalendar.fragment.CalendarFragment;
import com.example.mycalendar.fragment.ClockFragment;
import com.example.mycalendar.fragment.EventView;
import com.example.mycalendar.fragment.FrgmntOverRide;
import com.example.mycalendar.fragment.NoteFragment;

import com.google.android.material.navigation.NavigationView;

import java.util.Stack;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private final int FRAGMENT_CALENDAR = 0;
    private final int FRAGMENT_NOTE = 1;
    private final int FRAGMENT_CLOCK = 2;
    static CalendarFragment cal;
    static ClockFragment clo;
    static NoteFragment not;

    Stack<FrgmntOverRide> stk = new Stack<FrgmntOverRide>();
    private int currentFragment = FRAGMENT_CALENDAR;
    private int currentCalendarFragment = R.layout.fragment_calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById((R.id.drawer_layout));

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        cal = new CalendarFragment(this);
        clo = new ClockFragment();
        not = new NoteFragment();
        replaceFragment(cal);
        currentFragment = FRAGMENT_CALENDAR;
        stk.clear();
        stk.push(cal);

        navigationView.getMenu().findItem(R.id.nav_calendar).setChecked(true);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // rotate process save instance state
        super.onSaveInstanceState(outState);
        outState.putInt("current_fragment", currentFragment);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        //restore instance state
        super.onRestoreInstanceState(savedInstanceState);
        currentFragment = savedInstanceState.getInt("current_fragment", 0);
        if (currentFragment == FRAGMENT_CALENDAR) replaceFragment(cal);
        else if (currentFragment == FRAGMENT_CLOCK) replaceFragment(clo);
        else if (currentFragment == FRAGMENT_NOTE) replaceFragment(not);
    }

    public void openEventView(String id, String date, Context context){
        //when click on event, open in full screen
        stk.push(new EventView(id, this, context));
        replaceFragment(stk.peek());
    }

    public void closeEventView(String date){
        // close
        stk.pop();
        replaceFragment(stk.peek());

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_calendar){
            if (currentFragment != FRAGMENT_CALENDAR){
                replaceFragment(cal);
                currentFragment = FRAGMENT_CALENDAR;
                stk.clear();
                stk.push(cal);
            }
        }
        else if (id == R.id.nav_note){
            if (currentFragment != FRAGMENT_NOTE){
                replaceFragment(clo);
                currentFragment = FRAGMENT_NOTE;
                stk.clear();
                stk.push(clo);
            }
        }
        else if (id == R.id.nav_clock){
            if (currentFragment != FRAGMENT_CLOCK){
                replaceFragment(not);
                currentFragment = FRAGMENT_CLOCK;
                stk.clear();
                stk.push(not);
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.screen_frame, fragment);
        transaction.commit();
    }

    public void narrowDownMenuPressed(View view) {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void expandMenuPressed(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
}