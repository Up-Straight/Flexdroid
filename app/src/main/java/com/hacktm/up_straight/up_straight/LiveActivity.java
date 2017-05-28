package com.hacktm.up_straight.up_straight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        getSupportActionBar().hide();
    }
}
