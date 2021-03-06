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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

    int count = 0;

    Button start_button;
    Button stop_button;
    Button oneHz_button;
    Button eightHz_button;
    Button back_to_main_button;
    RadioGroup radiogroup;
    RadioButton radioButton1;
    RadioButton radiobutton2;
    TextView textView01;
    SensorManager manager;
    Sensor sensor;
    SensorEvent event1 = null;
    SensorEvent eventnull = null;
    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat simpleDateFormatname;
    String nowDate;
    Timer timer;
    String time;
    int Hz = 0;
    LocationManager locationManager;
    Location location1 = null;
    Double locationnull = Double.NaN;
    SQLiteDatabase db;
    OpenHelper helper;
    String filename;
    int j = 0;
    String text1;

    int kmlstart_i = 0;
    FileWriter fileWriter2;
    PrintWriter printWriter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_main);
        manager = (SensorManager)getSystemService(Activity.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        buuttonset();
        //back_to_main();

        //timer = new Timer();
        /*timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Calendar calendar  = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS", Locale.getDefault());
                simpleDateFormatname = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                nowDate = simpleDateFormat.format(calendar.getTime());
                time = nowDate;
            }
        },0,1000);*/
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
        radiogroup = findViewById(R.id.radiogroup);
        radioButton1 = findViewById(R.id.radioButton1);
        radiobutton2 = findViewById(R.id.radioButton2);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1){
                    if (findViewById(checkedId) == radioButton1){
                        //if (Hz==0){
                            Hz = 1000;
                            System.out.println(Hz);
                        //}
                    }
                    if (findViewById(checkedId) == radiobutton2){
                        //if (Hz==0){
                            Hz = 125;
                            System.out.println(Hz);
                        //}
                    }
                } else {
                    System.out.println("Hz が指定されていません。");
                }
            }
        });
    }
    public void onSensorChanged(SensorEvent event){ //センサーの値変更時の処理
        event1 = event;
        //count = count+1;
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy){ //センサー精度変更時の処理
    }
    public void onLocationChanged(Location location){
        location1 = location;
        System.out.println("location : "+location.getLatitude()+", "+location.getLongitude()+", "+location.getAltitude());
        count = count+1;
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
                    timer = new Timer();
                    Calendar calendar  = Calendar.getInstance();
                    simpleDateFormatname = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
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
                Calendar calendar  = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS", Locale.getDefault());
                nowDate = simpleDateFormat.format(calendar.getTime());
                time = nowDate;
                //String text;
                final String text1;
                //String a = "lo : "+location1+", null : "+locationnull;
                //System.out.println(a);
                if (location1 != null) {
                    if (locationnull == location1.getLatitude()) {
                        location1 = null;
                    } else {
                        locationnull = location1.getLatitude();
                    }
                }

                if (event1 != null){
                    if(location1 != null){
                        System.out.println("event != null, location != null");
                        //text = "time:"+time+"filename:"+filename+", x:"+event1.values[0]+", y:"+event1.values[1]+", z:"+event1.values[2]+", latitude:"+location1.getLatitude()+", longitude:"+location1.getLongitude();
                        text1 = time+", \n"+filename+", \n"+event1.values[0]+", "+event1.values[1]+", "+event1.values[2]+", \n"+location1.getLatitude()+", "+location1.getLongitude()+", "+location1.getAltitude();
                        //System.out.println(text1);
                    }else{
                        System.out.println("event != null, location == null");
                        text1 = time+", \n"+filename+", \n"+event1.values[0]+", "+event1.values[1]+", "+event1.values[2]+", \n"+"NULL, NULL, NULL";
                        //System.out.println(text1);
                    }
                }else {
                    if (location1 != null) {
                        System.out.println("event==null, location != null");
                        text1 = time+", \n"+filename+", \n"+event1+", "+event1+", "+event1+", \n"+location1.getLatitude()+", "+location1.getLongitude()+", "+location1.getAltitude();
                        //System.out.println(text1);
                    }else {
                        System.out.println("event==null, location == null");
                        text1 = time+", \n"+filename+", \n"+event1+", "+event1+", "+event1+", \n"+location1+", "+location1+", "+location1;
                        //System.out.println(text1);
                    }
                }
                System.out.println(text1);
                //System.out.println(count);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        textView01.setText(text1);
                    }
                });
                insertData();
                csvFile();
                kmlFile();
            }
        },0, Hz);//1Hz 1000ミリ秒, 8Hz 125ミリ秒
    }
    public void stopclick(){
        manager.unregisterListener(this);
        timer.cancel();
        event1 = null;
        String stop = "FINISH";
        textView01.setText(stop);
        System.out.println("stopclick time:"+time+", event:null");

        if (kmlstart_i == 1) {
            try {
                fileWriter2 = new FileWriter(getFilesDir() + "/" + filename + ".kml", true);
                printWriter2 = new PrintWriter(new BufferedWriter(fileWriter2));
                printWriter2.println("</Document>\n</kml>"); //kml file finish
                printWriter2.println();
                printWriter2.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void csvFile(){
        try{
            FileWriter fileWriter = new FileWriter(getFilesDir()+"/"+filename+".csv", true);
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(fileWriter));

            String[] datas1;
            if (event1 != null) {
                if (location1 != null) {
                    datas1 = new String[]{time, filename, String.valueOf(event1.values[0]), String.valueOf(event1.values[1]), String.valueOf(event1.values[2]), String.valueOf(location1.getLatitude()), String.valueOf(location1.getLongitude()), String.valueOf(location1.getAltitude())};
                }else {
                    datas1 = new String[]{time, filename, String.valueOf(event1.values[0]), String.valueOf(event1.values[1]), String.valueOf(event1.values[2]), String.valueOf(location1), String.valueOf(location1), String.valueOf(location1)};
                }
            }else {
                if (location1 != null){
                    datas1 = new String[]{time, filename, String.valueOf(event1), String.valueOf(event1), String.valueOf(event1), String.valueOf(location1.getLatitude()), String.valueOf(location1.getLongitude()), String.valueOf(location1.getAltitude())};
                }else {
                    datas1 = new String[]{time, filename, String.valueOf(event1), String.valueOf(event1), String.valueOf(event1), String.valueOf(location1), String.valueOf(location1), String.valueOf(location1)};
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

    private void kmlFile(){
        String kmlstart = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n<Document>\n";

        try{
            fileWriter2 = new FileWriter(getFilesDir()+"/"+filename+".kml", true);
            printWriter2 = new PrintWriter(new BufferedWriter(fileWriter2));

            String[] datas1;

            if (location1 != null) {
                datas1 = new String[]{time, String.valueOf(location1.getLatitude()), String.valueOf(location1.getLongitude()), String.valueOf(location1.getAltitude())};
            } else {
                datas1 = new String[]{time, String.valueOf(location1), String.valueOf(location1), String.valueOf(location1)};
            }

            if (kmlstart_i == 0) {
                kmlstart_i = 1;
                printWriter2.print(kmlstart);
            }
            if (location1 != null) {
                printWriter2.println("<Placemark>");
                printWriter2.println("<name>" + datas1[0] + "</name>");
                printWriter2.println("<description>" + datas1[0] + "</description>");
                printWriter2.println("<Point>\n<coordinates>" + datas1[2] + ", " + datas1[1] + ", " + datas1[3] + "</coordinates>\n</Point>");
                printWriter2.println("</Placemark>");
            }

            printWriter2.println();
            printWriter2.close();

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
        String[] keys = {"time","filename","x_axis","y_axis","z_axis","latitude","longitude","altitude"};
        String[] datas2 = {time, filename};
        Double[] datas3;
        Double Null = null;

        if (event1 != null) {
            if (location1 != null) {
                datas3 = new Double[]{Double.parseDouble(String.valueOf(event1.values[0])), Double.parseDouble(String.valueOf(event1.values[1])), Double.parseDouble(String.valueOf(event1.values[2])), Double.parseDouble(String.valueOf(location1.getLatitude())), Double.parseDouble(String.valueOf(location1.getLongitude())), Double.parseDouble(String.valueOf(location1.getAltitude()))};
            }else {
                datas3 = new Double[]{Double.parseDouble(String.valueOf(event1.values[0])), Double.parseDouble(String.valueOf(event1.values[1])), Double.parseDouble(String.valueOf(event1.values[2])), Null, Null, Null};
            }
        }else {
            if (location1 != null){
                datas3 = new Double[]{Double.parseDouble(String.valueOf(event1)), Double.parseDouble(String.valueOf(event1)), Double.parseDouble(String.valueOf(event1)), Double.parseDouble(String.valueOf(location1.getLatitude())), Double.parseDouble(String.valueOf(location1.getLongitude())), Double.parseDouble(String.valueOf(location1.getAltitude()))};
            }else {
                datas3 = new Double[]{Double.parseDouble(String.valueOf(event1)), Double.parseDouble(String.valueOf(event1)), Double.parseDouble(String.valueOf(event1)), Double.parseDouble(String.valueOf(location1)), Double.parseDouble(String.valueOf(location1)), Double.parseDouble(String.valueOf(location1))};
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

