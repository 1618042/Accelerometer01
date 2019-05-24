package com.example.accelerometer;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    Button start_button;
    Button stop_button;
    SensorManager manager;
    Sensor sensor;
    SensorEvent event1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_main);
        manager = (SensorManager)getSystemService(Activity.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        System.out.println(manager);
        System.out.println(sensor);
        start_button = findViewById(R.id.start_button);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startclick();
            }
        });
        stop_button = findViewById(R.id.stop_button);
        stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopclick();
            }
        });

        Handler handler =new Handler(getMainLooper());
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Calendar calendar  = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SS", Locale.getDefault());
                String nowDate = simpleDateFormat.format(calendar.getTime());
                String time = nowDate;

                if (event1!=null) {
                    System.out.println(time+" : "+event1.values[0]);
                }else {
                    System.out.println(time+" : null");
                }
            }
        },0, 125);//1Hz 1000ミリ秒, 8Hz 125ミリ秒
    }

    public void onSensorChanged(SensorEvent event){ //センサーの値変更時の処理
        event1 = event;

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy){ //センサー精度変更時の処理
    }
    protected void onResume(){ super.onResume(); /*manager.registerListener(this, sensor, 625000);*/}
    protected void onPause(){ super.onPause(); /*manager.unregisterListener(this);*/}
    public void startclick(){
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);

    }
    public void stopclick(){
        manager.unregisterListener(this);
    }
}

