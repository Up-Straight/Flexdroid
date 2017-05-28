package com.hacktm.up_straight.up_straight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Button connectButton = (Button)findViewById(R.id.connectTestButton);
        Button messageButton = (Button)findViewById(R.id.messageTestButton);

        BluetoothHelper bluetoothHelper = null;
        try {
            bluetoothHelper = new BluetoothHelper(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final BluetoothHelper finalBluetoothHelper = bluetoothHelper;
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalBluetoothHelper.connectSocket();
            }
        });


        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalBluetoothHelper.startReading();
            }
        });
    }
}
