package com.example.accelerometer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.PermissionChecker;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsyncHttp extends AsyncTask<String, Integer, Boolean> {
    Double id, x_axis, y_axis, z_axis, latitude, longitude;
    String time, filename;

    public AsyncHttp(Double id, String time, String filename, Double x_axis, Double y_axis, Double z_axis, Double latitude,Double longitude) {
        this.id = id;
        this.time = time;
        this.filename = filename;
        this.x_axis = x_axis;
        this.y_axis = y_axis;
        this.z_axis = z_axis;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    HttpURLConnection urlConnection = null; //HTTPコネクション管理用
    Boolean flg = false;

    @Override
    protected Boolean doInBackground(String... params) {
        String urlinput = "http://mznjerk.mizunolab.info/tests/add";

        URL url = null;
        try {
            url = new URL(urlinput);
            //System.out.println(url);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            String postDataSample = "id="+this.id+"&time="+this.time+"&management_id="+this.filename+"&x_axis="+this.x_axis+"&y_axis="+this.y_axis+"&z_axis="+this.z_axis+"&latitude="+this.latitude+"&longitude="+this.longitude;
            OutputStream out = urlConnection.getOutputStream();
            out.write(postDataSample.getBytes());
            out.flush();
            out.close();
            urlConnection.getInputStream();
            flg = true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flg;
    }
}
