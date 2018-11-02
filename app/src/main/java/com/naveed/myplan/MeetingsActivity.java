package com.naveed.myplan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MeetingsActivity extends AppCompatActivity {
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings);
        Intent i = getIntent();
        title = i.getStringExtra("title");
        setTitle(title);



    }
}
