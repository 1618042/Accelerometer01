package com.example.accelerometer;


import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class SensorActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener, LocationListener {

    Button start_button;
    Button stop_button;
    Button oneHz_button;
    Button eightHz_button;
    Button back_to_main_button;
    TextView textView01;
    SensorManager manager;
    Sensor sensor;
    SensorEvent event1 = null;
    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat simpleDateFormatname;
    String nowDate;
    Timer timer;
    String time;
    int Hz = 0;
    LocationManager locationManager;
    Location location1 = null;
    SQLiteDatabase db;
    OpenHelper helper;
    String filename;
    int j = 0;
    String text1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_main);
        manager = (SensorManager)getSystemService(Activity.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        buuttonset();
        //back_to_main();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Calendar calendar  = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SS", Locale.getDefault());
                simpleDateFormatname = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
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
        textView01 = findViewById(R.id.textView01);
    }
    public void onSensorChanged(SensorEvent event){ //センサーの値変更時の処理
        event1 = event;
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy){ //センサー精度変更時の処理
    }
    public void onLocationChanged(Location location){
        location1 = location;
    }
    public void onStatusChanged(String provider, int status, Bundle extras){

    }
    public void onProviderEnabled(String provider){

    }
    public void onProviderDisabled(String provider){

    }
    protected void onResume(){
        super.onResume();
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if ( (PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                (PermissionChecker.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) ){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
            System.out.println("GPS");
        }else if ((PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                (PermissionChecker.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) ){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,this);
            System.out.println("Wi-fi");
        }else{
            System.out.println("位置情報取得できない");
        }

    }
    protected void onPause(){ super.onPause(); manager.unregisterListener(this);}
    public void onClick(View view){
        switch (view.getId()){
            case R.id.oneHz_button :
                if (Hz == 0) {
                    Hz = 1000;
                }
                System.out.println("Hz : "+ Hz);
                break;
            case R.id.eightHz_button :
                if (Hz == 0) {
                    Hz = 125;
                }
                System.out.println("Hz : "+ Hz);
                break;
            case R.id.start_button :
                if (Hz != 0) {
                    Calendar calendar  = Calendar.getInstance();
                    filename = simpleDateFormatname.format(calendar.getTime());
                    startclick();
                }else {
                    System.out.println("Hz が指定されていません。");
                }
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
        final Handler handler = new Handler();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String text;
                final String text1;
                if (event1 != null){
                    if(location1 != null){
                        text = "time:"+time+"filename:"+filename+", x:"+event1.values[0]+", y:"+event1.values[1]+", z:"+event1.values[2]+", latitude:"+location1.getLatitude()+", longitude:"+location1.getLongitude();
                        text1 = time+", "+filename+", "+event1.values[0]+", "+event1.values[1]+", "+event1.values[2]+", "+location1.getLatitude()+", "+location1.getLongitude();
                        System.out.println(text1);
                    }else{
                        System.out.println("event != null, location == null");
                        text1 = time+", "+filename+", "+event1.values[0]+", "+event1.values[1]+", "+event1.values[2]+", "+"NULL"+", "+"NULL";
                        System.out.println(text1);
                    }
                }else {
                    if (location1 != null) {
                        System.out.println("event==null, location != null");
                        text1 = time+", "+filename+", "+event1+", "+event1+", "+event1+", "+location1.getLatitude()+", "+location1.getLongitude();
                        System.out.println(text1);
                    }else {
                        System.out.println("event==null, location == null");
                        text1 = time+", "+filename+", "+event1+", "+event1+", "+event1+", "+location1+", "+location1;
                        System.out.println(text1);
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        textView01.setText(text1);
                    }
                });
                insertData();
                csvFile();
            }
        },0, Hz);//1Hz 1000ミリ秒, 8Hz 125ミリ秒
    }
    public void stopclick(){
        manager.unregisterListener(this);
        timer.cancel();
        event1 = null;
        System.out.println("stopclick time:"+time+", event:null");
    }

    public void csvFile(){
        try{
            FileWriter fileWriter = new FileWriter(getFilesDir()+"/"+filename+".csv", true);
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(fileWriter));
            String[] datas1;
            if (event1 != null) {
                if (location1 != null) {
                    datas1 = new String[]{time, filename, String.valueOf(event1.values[0]), String.valueOf(event1.values[1]), String.valueOf(event1.values[2]), String.valueOf(location1.getLatitude()), String.valueOf(location1.getLongitude())};
                }else {
                    datas1 = new String[]{time, filename, String.valueOf(event1.values[0]), String.valueOf(event1.values[1]), String.valueOf(event1.values[2]), String.valueOf(location1), String.valueOf(location1)};
                }
            }else {
                if (location1 != null){
                    datas1 = new String[]{time, filename, String.valueOf(event1), String.valueOf(event1), String.valueOf(event1), String.valueOf(location1.getLatitude()), String.valueOf(location1.getLongitude())};
                }else {
                    datas1 = new String[]{time, filename, String.valueOf(event1), String.valueOf(event1), String.valueOf(event1), String.valueOf(location1), String.valueOf(location1)};
                }
            }
            for (int i = 0; i < datas1.length; i++){
                if (datas1[i] != null){
                    printWriter.print(datas1[i]);
                }else {
                    printWriter.print("NULL");
                }
                if (i <= datas1.length -2){
                    printWriter.print(", ");
                }
            }
            printWriter.println();
            printWriter.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void insertData(){
        helper = new OpenHelper(this);
        db = helper.getWritableDatabase();
        ContentValues values;
        if (j==0){
            values = new ContentValues();
            String[] keys1 = {"filename"};
            String[] data4 = {filename};
            for (int i = 0; i < keys1.length; i++) {
                if (data4[i] != null) {
                    values.put(keys1[i], data4[i]);
                } else {
                    values.put(keys1[i], "NULL");
                }
            }
            System.out.println(values);
            db.insert("Management01db", null, values);
            j=1;
        }
        values = new ContentValues();
        String[] keys = {"time","filename","x_axis","y_axis","z_axis","latitude","longitude"};
        String[] datas2 = {time, filename};
        Double[] datas3;
        Double Null = null;
        if (event1 != null) {
            if (location1 != null) {
                datas3 = new Double[]{Double.parseDouble(String.valueOf(event1.values[0])), Double.parseDouble(String.valueOf(event1.values[1])), Double.parseDouble(String.valueOf(event1.values[2])), Double.parseDouble(String.valueOf(location1.getLatitude())), Double.parseDouble(String.valueOf(location1.getLongitude()))};
            }else {
                datas3 = new Double[]{Double.parseDouble(String.valueOf(event1.values[0])), Double.parseDouble(String.valueOf(event1.values[1])), Double.parseDouble(String.valueOf(event1.values[2])), Null, Null};
            }
        }else {
            if (location1 != null){
                datas3 = new Double[]{Double.parseDouble(String.valueOf(event1)), Double.parseDouble(String.valueOf(event1)), Double.parseDouble(String.valueOf(event1)), Double.parseDouble(String.valueOf(location1.getLatitude())), Double.parseDouble(String.valueOf(location1.getLongitude()))};
            }else {
                datas3 = new Double[]{Double.parseDouble(String.valueOf(event1)), Double.parseDouble(String.valueOf(event1)), Double.parseDouble(String.valueOf(event1)), Double.parseDouble(String.valueOf(location1)), Double.parseDouble(String.valueOf(location1))};
            }
        }
        for (int i = 0; i < datas2.length + datas3.length; i++){
            if (i < datas2.length) {
                values.put(keys[i], datas2[i]);
            }else if (datas3[i-datas2.length] != null){
                values.put(keys[i], datas3[i-datas2.length]);
            }else {
                values.put(keys[i],"NULL");
            }
        }
        System.out.println(values);
        db.insert("Test01db", null, values);
        db.close();
    }

    public void back_to_main(){
        back_to_main_button = findViewById(R.id.back_to_main_button);
        stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getApplication(), MainActivity.class);
                startActivity(intent3);
            }
        });
    }

}

