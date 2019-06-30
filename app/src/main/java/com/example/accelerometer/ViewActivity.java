package com.example.accelerometer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ViewActivity extends AppCompatActivity implements View.OnClickListener{

    Button read_button;
    Button php_button;
    Button mappu_button;
    Cursor cursor;
    SQLiteDatabase db;
    ListView listView;
    OpenHelper helper;
    String[] latitude;
    String[] longitude;
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.view_main);

        read_button = findViewById(R.id.read_button);
        read_button.setOnClickListener(this);
        php_button = findViewById(R.id.php_button);
        php_button.setOnClickListener(this);
        mappu_button =findViewById(R.id.mappu_button);
        listView = findViewById(R.id.view01);
        listView.setAdapter(null);

    }
    protected void onResume(){
        super.onResume();
        mappu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = helper.getReadableDatabase();
                try {
                    cursor = db.rawQuery("SELECT * from Test01db", null);
                    cursor.moveToFirst();
                    if (cursor.getCount() > 0){
                        latitude = new String[cursor.getCount()];
                        longitude = new String[cursor.getCount()];
                        for (int cnt = 0; cnt < cursor.getCount(); cnt++){
                            latitude[cnt] = cursor.getString(6);
                            longitude[cnt] = cursor.getString(7);
                            cursor.moveToNext();
                        }
                        cursor.close();
                    }
                    cursor.close();
                }finally {
                    db.close();
                }
                System.out.println("view");
                Intent intent3 = new Intent(getApplication(), MapsActivity.class);
                intent3.putExtra("String latitude", latitude);
                intent3.putExtra("String longitude", longitude);
                startActivity(intent3);

            }
        });
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.read_button :
                readData();
                break;
            case R.id.php_button :
                phpset();
                break;
            default :
                break;
        }
    }

    public void readData(){
        helper = new OpenHelper(getApplicationContext());
        db = helper.getReadableDatabase();
        try{
            //画面表示
            cursor = db.rawQuery("SELECT * from Test01db where filename>=20190630101557;",null);
            System.out.println(cursor);
            cursor.moveToFirst();
            if (cursor.getCount() > 0){
                Integer[] data = new Integer[cursor.getCount()];
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item);
                adapter.add("id, time, filename, x_axis, y_axis, z_axis, latitude, longitude, altitude                                                                                                      ");
                for (int cnt = 0; cnt < cursor.getCount(); cnt++){
                    data[cnt] = cursor.getInt(0);
                    adapter.add(""+cursor.getString(0)+", "+cursor.getString(1)+", "+cursor.getString(2)+", "+cursor.getString(3)+", "+cursor.getString(4)+", "+cursor.getString(5)+", "+cursor.getString(6)+", "+cursor.getString(7)+", "+ cursor.getString(8));
                    cursor.moveToNext();
                    listView.setAdapter(adapter);
                }
                cursor.close();
            }else {
                listView.setAdapter(null);
            }
        }finally {
            db.close();
        }
    }

    public void phpset(){
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){
            System.out.println("1");
        }else {
            System.out.println("0");
        }
        helper = new OpenHelper(getApplicationContext());
        db = helper.getReadableDatabase();
        String[] file_id = null;
        try {
            //Managementテーブルにあげるもの
            cursor = db.rawQuery("SELECT * from Management01db;",null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0){
                Integer[] data = new Integer[cursor.getCount()];
                file_id = new String[cursor.getCount()];
                for (int cnt = 0; cnt < cursor.getCount(); cnt++){
                    FAsyncHttp post1 = new FAsyncHttp(cursor.getDouble(0), cursor.getString(1));
                    file_id[cnt] = cursor.getString(1);
                    //System.out.println("file_id[cnt] : "+file_id[cnt]);
                    post1.execute();
                    cursor.moveToNext();
                }
                cursor.close();
            }
            cursor.close();
        }finally {
            db.close();
        }
        test01(file_id);

    }
    public void test01(String[] file_id){
        db = helper.getReadableDatabase();
        try {
            //Testテーブルにあげるもの
            //cursor = db.rawQuery("SELECT * from Test01db",null);
            cursor = db.rawQuery("SELECT * from Test01db where filename>=20190630101557; ",null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0){
                Integer[] data = new Integer[cursor.getCount()];
                int i=0;
                for (int cnt = 0; cnt < cursor.getCount(); cnt++){
                    if (Double.parseDouble(cursor.getString(2)) != Double.parseDouble(file_id[i])){
                        i++;
                    }
                    AsyncHttp post = new AsyncHttp(cursor.getDouble(0), cursor.getString(1), file_id[i], cursor.getDouble(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getDouble(6), cursor.getDouble(7), cursor.getDouble(8));
                    post.execute();
                    cursor.moveToNext();
                }
                cursor.close();
            }else {
                listView.setAdapter(null);
            }
            cursor.close();
        }finally {
            db.close();
        }
    }
}
