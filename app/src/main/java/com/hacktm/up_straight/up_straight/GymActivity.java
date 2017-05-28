package com.hacktm.up_straight.up_straight;

import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class GymActivity extends AppCompatActivity {

    TextView resultTV;
    TextView repCountTV;
    TextView goalTV;
    ImageButton circleIB;
    ImageButton startButton;
    ProgressBar progressBar;
    PulsatorLayout redPulsator;
    PulsatorLayout greenPulsator;


    static final int MAX_REP = 15;
    boolean isGood = true;
    boolean isStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);
        getSupportActionBar().hide();

        //init
        init();

        circleIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isGood)
                    addBadRep();
                else
                    addGoodRep();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleStartButton();
            }
        });
    }

    public void init(){
        resultTV = (TextView)findViewById(R.id.result_tv);
        repCountTV = (TextView)findViewById(R.id.rep_count_tv) ;
        goalTV = (TextView)findViewById(R.id.goal_tv);
        circleIB = (ImageButton)findViewById(R.id.circle_ib);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setMax(MAX_REP);
        greenPulsator = (PulsatorLayout)findViewById(R.id.green_pulsator);
        redPulsator = (PulsatorLayout)findViewById(R.id.red_pulsator);
        redPulsator.setVisibility(View.INVISIBLE);
        startButton = (ImageButton)findViewById(R.id.start_button);
    }

    public void addRep(int color){
        resultTV.setTextColor(color);

        repCountTV.setTextColor(color);

        goalTV.setTextColor(color);

        progressBar.setProgressTintList(ColorStateList.valueOf(color));
    }

    public void addGoodRep(){
        isGood = true;

        //set result
        resultTV.setText("Excellent!");

        //update primary color
        addRep(getResources().getColor(R.color.colorPrimary));

        //update progress
        repCountTV.setText("" + (Integer.parseInt((String) repCountTV.getText())+1));
        progressBar.setProgress(Integer.parseInt((String) repCountTV.getText()));

        //set good circle
        circleIB.setImageResource(R.mipmap.good);

        //stop old pulse
        greenPulsator.stop();
        greenPulsator.setVisibility(View.INVISIBLE);

        //pulse
        greenPulsator.setVisibility(View.VISIBLE);
        greenPulsator.start();
    }

    public void addBadRep(){
        isGood = false;

        //set result
        resultTV.setText("Bad form!");

        //update primary color
        addRep(getResources().getColor(R.color.badColor));

        //set bad circle
        circleIB.setImageResource(R.mipmap.bad);

        //stop old pulse
        greenPulsator.stop();
        greenPulsator.setVisibility(View.INVISIBLE);

        //pulse
        redPulsator.setVisibility(View.VISIBLE);
        redPulsator.start();
    }

    public void toggleStartButton(){
        if(isStarted){
            startButton.setImageResource(R.mipmap.pause);
        }else{
            startButton.setImageResource(R.mipmap.start);
        }
        isStarted = !isStarted;
    }
}
