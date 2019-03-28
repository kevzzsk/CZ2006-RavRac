package com.example.kevzzsk.dengueradar;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;


public class MenuInterface extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MenuInterface";
    private DrawerLayout drawer;
    private EditText feedback;

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    // make menuItem clicks become active
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_map:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MapsInterface()).commit();
                break;
            case R.id.nav_statistics:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new StatisticInterface() ).commit();
                break;
            case R.id.nav_tips:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TipsInterface() ).commit();
                break;
            case R.id.nav_feedback:
                handleFeedback();
                break;
            case R.id.nav_faq:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void handleFeedback(){

        LinearLayout viewGroup = (LinearLayout) findViewById(R.id.feedback);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.feedback_form, viewGroup);

        final PopupWindow changeSortPopUp = new PopupWindow(this);
        changeSortPopUp.setContentView(layout);
        changeSortPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeSortPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeSortPopUp.setFocusable(true);

        // Some offset to align the popup a bit to the left, and a bit down, relative to button's position.
        int OFFSET_X = -20;
        int OFFSET_Y = 95;

        // clear default transcluscent background
        changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());

        changeSortPopUp.showAtLocation(layout, Gravity.CENTER, 0,0);

        EditText msg = (EditText) layout.findViewById(R.id.feedback_body);
        EditText title = (EditText) layout.findViewById(R.id.feedback_title);
        Button submit = (Button) layout.findViewById(R.id.submit_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send to firebase
            }
        });

        Button cancel = (Button) layout.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                changeSortPopUp.dismiss();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_interface);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dengue Radar");


        drawer = findViewById(R.id.drawerLayout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this, drawer,
                toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        Log.d(TAG, "onCreate: drawer message!"+ String.valueOf(drawer == null) );
        drawer.addDrawerListener(toggle);
        toggle.syncState();




        //set MapInterface to be default start up page
        // savedInstanceState ensures that rotating device will not switch back to default page
        if(savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MapsInterface()).commit();
                navigationView.setCheckedItem(R.id.nav_map);

        }
        String message = getIntent().getStringExtra("action");
        //go to tips if user click on notification
        Log.d(TAG, "onCreate: "+message);
        if(message !=null && message.equals("goToTipPage")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new TipsInterface()).commit();
            navigationView.setCheckedItem(R.id.nav_tips);
        }

    }


}
