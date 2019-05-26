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

public class SensorActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    Button start_button;
    Button stop_button;
    Button oneHz_button;
    Button eightHz_button;
    SensorManager manager;
    Sensor sensor;
    SensorEvent event1;
    SimpleDateFormat simpleDateFormat;
    String nowDate;
    String time;
    int Hz = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_main);
        manager = (SensorManager)getSystemService(Activity.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        buuttonset();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Calendar calendar  = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SS", Locale.getDefault());
                nowDate = simpleDateFormat.format(calendar.getTime());
                time = nowDate;
            }
        },0,1000);

    }
    public void buuttonset(){
        oneHz_button = findViewById(R.id.oneHz_button);
        oneHz_button.setOnClickListener(this);
        eightHz_button = findViewById(R.id.eightHz_button);
        eightHz_button.setOnClickListener(this);
        start_button = findViewById(R.id.start_button);
        start_button.setOnClickListener(this);
        stop_button = findViewById(R.id.stop_button);
        stop_button.setOnClickListener(this);
    }

    public void onSensorChanged(SensorEvent event){ //センサーの値変更時の処理
        event1 = event;

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy){ //センサー精度変更時の処理
    }
    protected void onResume(){
        super.onResume();
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);

    }
    protected void onPause(){ super.onPause(); manager.unregisterListener(this);}
    @Override
    public void onClick(View view){
        //System.out.println("onclick");
        switch (view.getId()){
            case R.id.oneHz_button :
                Hz = 1000;
                System.out.println("Hz : "+ Hz);
                break;
            case R.id.eightHz_button :
                Hz = 125;
                System.out.println("Hz : "+ Hz);
                break;
            case R.id.start_button :
                startclick();
                break;
            case R.id.stop_button :
                stopclick();
                break;
            default :
                break;

        }
    }
    public void startclick(){
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if ( (event1 != null) || (Hz != 0) ) {
                    System.out.println(time+" : event1 : "+event1.values[0]);
                    System.out.println(Hz);
                }else {
                    System.out.println(time+" : event : null");
                }
            }
        },0, Hz);//1Hz 1000ミリ秒, 8Hz 125ミリ秒
    }
    public void stopclick(){
        manager.unregisterListener(this);
        event1 = null;
        System.out.println(time+" : null");
    }
}

