package com.example.accelerometer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);

        Button insert_button = findViewById(R.id.insert_button);
        insert_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1= new Intent(getApplication(), SensorActivity.class);
                startActivity(intent1);
            }
        });

        Button view_button = findViewById(R.id.view_button);
        view_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2= new Intent(getApplication(), ViewActivity.class);
                startActivity(intent2);
            }
        });
    }
}
