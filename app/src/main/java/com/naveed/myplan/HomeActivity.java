package com.naveed.myplan;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    ListView listView;
    String[] values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        // Get ListView object from xml
        listView = findViewById(R.id.mainListView);

        // Defined Array values to show in ListView
        values = new String[]{"Aim", "Diet", "Meetings", "Exercise",
                "Medicines", "Expances"};


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String itemValue = (String) listView.getItemAtPosition(position);
                Intent i = callActivity(position);
                i.putExtra("title" , itemValue);
                startActivity(i);
            }

        });
    }


    private Intent callActivity(int pos){
        Intent[] intents = new Intent[]{
                new Intent(HomeActivity.this,
                AimActivity.class), new Intent(HomeActivity.this,
                DietActivity.class), new Intent(HomeActivity.this,
                MeetingsActivity.class), new Intent(HomeActivity.this,
                ExerciseActivity.class), new Intent(HomeActivity.this,
                MedicinesActivity.class) ,new Intent(HomeActivity.this,
                ExpencesActivity.class) };
        return intents[pos];
    }


    @Override
    public void onBackPressed() {

    }


}
