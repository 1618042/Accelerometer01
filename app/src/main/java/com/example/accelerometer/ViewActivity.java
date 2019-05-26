package com.example.accelerometer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ViewActivity extends AppCompatActivity implements View.OnClickListener{

    Button read_button;
    Cursor cursor;
    SQLiteDatabase db;
    ListView listView;
    OpenHelper helper;
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.view_main);

        read_button = findViewById(R.id.read_button);
        read_button.setOnClickListener(this);
        listView = findViewById(R.id.view01);
        listView.setAdapter(null);

    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.read_button :
                readData();
                break;
            default :
                break;
        }
    }

    public void readData(){
        helper = new OpenHelper(getApplicationContext());
        db = helper.getReadableDatabase();
        try{
            cursor = db.rawQuery("SELECT * from Test01db",null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0){
                Integer[] data = new Integer[cursor.getCount()];
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item);
                for (int cnt = 0; cnt < cursor.getCount(); cnt++){
                    data[cnt] = cursor.getInt(0);
                    adapter.add("ID : "+cursor.getString(0)+", time : "+cursor.getString(1)+", \nx_axis :"+cursor.getString(2)+", y_axis : "+cursor.getString(3)+", z_axis : "+cursor.getString(4)+", \nlatitude : "+cursor.getString(5)+", longitude : "+cursor.getString(6));
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
}
