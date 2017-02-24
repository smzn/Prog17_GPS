package com.example.mizuno.prog17_02;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mizuno on 2017/02/22.
 */

public class AsyncHttp extends AsyncTask<String, Integer, Boolean> {
    String device_id;
    double latitude, longitude, elevation;

    public AsyncHttp(String device_id, double latitude, double longitude, double elevation) {
        this.device_id = device_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
    }

    HttpURLConnection urlConnection = null; //HTTPコネクション管理用
    Boolean flg = false;

    @Override
    protected Boolean doInBackground(String... params) {
        String urlinput = "http://ms000.sist.ac.jp/oc/positions/add";

        URL url = null;
        try {
            url = new URL(urlinput);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            String postDataSample = "device_id="+this.device_id+"&latitude="+this.latitude+"&longitude="+this.longitude+"&elevation="+this.elevation;
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
