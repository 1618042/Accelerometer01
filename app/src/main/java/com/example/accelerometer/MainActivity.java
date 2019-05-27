package com.example.accelerometer;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    SensorActivity sensorActivity;
    Location location1;

    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onResume() {
        super.onResume();
        Button insert_button = findViewById(R.id.insert_button);
        insert_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplication(), SensorActivity.class);
                startActivity(intent1);
            }
        });

        Button view_button = findViewById(R.id.view_button);
        view_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplication(), ViewActivity.class);
                startActivity(intent2);
            }
        });

        sensorActivity = new SensorActivity();
        location1 = sensorActivity.location1;
        Button maps_button = findViewById(R.id.maps_button);
        maps_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location1 != null) {
                    Intent intent3 = new Intent(getApplication(), MapsActivity.class);
                    startActivity(intent3);
                } else {
                    System.out.println("location == NULL");
                }
            }
        });

    }
}
