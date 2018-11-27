package com.naveed.myplan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static com.naveed.myplan.SignupActivity.MY_PREFS_NAME;
import static com.naveed.myplan.SignupActivity.PREF_NAME;
import static com.naveed.myplan.SignupActivity.PREF_PASSWORD;
import static com.naveed.myplan.SignupActivity.PREF_STATUS;
import static com.naveed.myplan.SignupActivity.PREF_USERNAME;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener ,
        AimFragment.OnFragmentInteractionListener , DietFragment.OnFragmentInteractionListener ,
        MeetingsFragment.OnFragmentInteractionListener ,ExerciseFragment.OnFragmentInteractionListener,
        MedicinesFragment.OnFragmentInteractionListener , ExpancesFragment.OnFragmentInteractionListener {


    String[] values = {"Aim", "Diet", "Meetings", "Exercise",
            "Medicines", "Expances"};


    MyDatabase db;
    NavigationView navigationView;
    SharedPreferences prefs;
    String prefsUname , prefsPass , prefsName;
    boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupRefs();





        navigationView = findViewById(R.id.nav_view);
        final Menu menu = navigationView.getMenu();
        for (int i = 0; i < values.length; i++) {
            menu.add(values[i]);
        }
        View headerView = navigationView.getHeaderView(0);
        //    ImageView drawerImage = headerView.findViewById(R.id.drawer_image);
        TextView drawername =  headerView.findViewById(R.id.navName);
        TextView drawerusername =  headerView.findViewById(R.id.navUsername);
        //     drawerImage.setImageDrawable(R.drawable.ic_user);
        drawername.setText(prefsName);
        drawerusername.setText(prefsUname);


        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupRefs() {

        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new MyDatabase(this);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.content_home, callFragment(0)).commit();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        checkUserLogin();
    }

    private void checkUserLogin() {

        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        prefsUname = prefs.getString(PREF_USERNAME, null);
        prefsPass = prefs.getString(PREF_PASSWORD, null);
        status = prefs.getBoolean(PREF_STATUS, false);


        if (prefsUname != null && prefsPass != null) {

            if (status){
                prefsName = prefs.getString(PREF_NAME, "No Name");

            }else {

                Log.e("My Plan", "checkUserLogin: In Home");
                Log.e("checkUserLogin: ",Boolean.toString(status));
                startActivity(new Intent(this , MainActivity.class));
            }

        }else if (!status) {
            startActivity(new Intent(this , SignupActivity.class));

        }



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!status){
                super.onBackPressed();
            }else{
                System.exit(0);
            }

            //super.onBackPressed();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_log_out) {
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putBoolean(PREF_STATUS , false);
            editor.apply();
            startActivity(new Intent(HomeActivity.this , MainActivity.class));
            Log.e("in Signout :", Boolean.toString(prefs.getBoolean(PREF_STATUS , true)));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        String nav_t = (String) item.getTitle();
        drawer.setFocusable(true);
//        Toast.makeText(this, nav_id, Toast.LENGTH_SHORT).show();

        switch (nav_t){
            case "Aim":
                setTitle(nav_t);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.content_home, callFragment(0)).commit();
                break;
            case "Diet":
                setTitle(nav_t);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.content_home, callFragment(1)).commit();
                break;
            case "Meetings":
                setTitle(nav_t);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.content_home, callFragment(2)).commit();
                break;
            case "Exercise":
                setTitle(nav_t);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.content_home, callFragment(3)).commit();
                break;
            case "Medicines":
                setTitle(nav_t);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.content_home, callFragment(4)).commit();
                break;
            case "Expances":
                setTitle(nav_t);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.content_home, callFragment(5)).commit();
                break;

        }



        return true;
    }


    private Fragment callFragment(int pos){
        Fragment[]fragments = new Fragment[]{
                new AimFragment() , new DietFragment() , new MeetingsFragment() ,
                new ExerciseFragment(), new MedicinesFragment() , new ExpancesFragment() };
        return fragments[pos];
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
