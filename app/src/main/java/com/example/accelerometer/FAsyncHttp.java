package com.example.accelerometer;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FAsyncHttp extends AsyncTask<String, Integer, Boolean> {
    String filename;
    Double id;
    HttpURLConnection urlConnection = null; //HTTPコネクション管理用
    Boolean flg = false;
    public FAsyncHttp(Double id, String filename){
        this.id = id;
        this.filename = filename;
    }
    @Override
    protected Boolean doInBackground(String... params) {
        String urlinput = "http://mznjerk.mizunolab.info/managements/add";

        URL url = null;
        try {
            url = new URL(urlinput);
            //System.out.println(url);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            String postDataSample = "id="+this.filename+"&filename="+this.filename;
            System.out.println(postDataSample);
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
