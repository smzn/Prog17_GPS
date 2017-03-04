package com.example.mizuno.prog17_02;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mizuno on 2017/02/26.
 */

public class AsyncHttpGetJson extends AsyncTask<Void, Void, String> {
    private String device_id;
    private String result = new String();
    private TraceActivity activity;

    public AsyncHttpGetJson(TraceActivity activity, String device_id) {
        this.activity = activity;
        this.device_id = device_id;
    }

    @Override
    protected String doInBackground(Void... params) {
        HttpURLConnection con = null;
        URL url = null;
        //String urlSt = "http://ms000.sist.ac.jp/oc/positions/api/" + device_id;
        String urlSt = "http://ms000.sist.ac.jp/oc/devices/json/" + device_id;

        try {
            // URLの作成
            url = new URL(urlSt);
            // 接続用HttpURLConnectionオブジェクト作成
            con = (HttpURLConnection)url.openConnection();
            // リクエストメソッドの設定
            con.setRequestMethod("POST");
            // リダイレクトを自動で許可しない設定
            con.setInstanceFollowRedirects(false);
            // URL接続からデータを読み取る場合はtrue
            con.setDoInput(true);
            // URL接続にデータを書き込む場合はtrue
            con.setDoOutput(true);

            // 接続
            con.connect(); // ①
            InputStream in = con.getInputStream();
            String readSt = readInputStream(in);

            // 配列を取得する場合
            JSONArray jsonArray = new JSONObject(readSt).getJSONArray("Position");
            for (int n = 0; n < jsonArray.length(); n++) {
                // User data
                JSONObject userObject = jsonArray.getJSONObject(n);
                int id = userObject.getInt("id");
                double latitude = userObject.getDouble("latitude");
                double longitude = userObject.getDouble("longitude");
                String created = userObject.getString("created");
                result += latitude + "," + longitude+",";
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String readInputStream(InputStream in) throws IOException, UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        String st = "";

        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        while((st = br.readLine()) != null)
        {
            sb.append(st);
        }
        try
        {
            in.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        String str[] = s.split(",", 0);
        double geo[] = new double[str.length];
        for(int i = 0; i< geo.length; i++) geo[i] = Double.parseDouble(str[i]);
        activity.mapLocation(geo);
    }
}
