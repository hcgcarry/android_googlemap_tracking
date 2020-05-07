package com.example.googlemap;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Location{

    private static final String TAG = "LocationClass";
    public double latitude;
    public double longitude;

    protected void get(){
        //httpget();
        //網路的連接不能放在主thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    httpgetLocation();
                    printLocation(TAG);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    public void printLocation(String TAG){
        Log.d(TAG, "-----------Location--------:");
        Log.d(TAG, "latitude:");
        Log.d(TAG, Double.toString(this.latitude));
        Log.d(TAG, "longitude:");
        Log.d(TAG, Double.toString(this.longitude));
        Log.d(TAG, "-----------end Location--------:");

    }

    /**
     * Converts the contents of an InputStream to a String.
     */

    private void httpgetLocation(){
        //String urlString = "http://192.168.127.103/phoneLocation/insert_location.php?latitude=23&longitude=222";
        String urlString = "http://192.168.127.103/phoneLocation/get_location.php";
        String respondMessage;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            Log.d(TAG, "fuc111k");

            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");//設定訪問方式為“GET”
            connection.setConnectTimeout(8000);//設定連線伺服器超時時間為8秒
            connection.setReadTimeout(8000);//設定讀取伺服器資料超時時間為8秒
            Log.d(TAG, "fuc112k");
            Log.d(TAG, "fuc113k");
            respondMessage= connection.getResponseMessage();
            Log.d(TAG, "fuc114k");
            Log.d(TAG, respondMessage);
            Log.d(TAG, Integer.toString(connection.getResponseCode()));

            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                Log.d(TAG, "connect success");
//從伺服器獲取響應並把響應資料轉為字串列印
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;
                while (null != (line = reader.readLine())) {
                    response.append(line);
                }
                String responseString= response.toString();
                Log.d(TAG, response.toString());

                String col1="latitude",col2="longitude";
                String splitArray[] =responseString.split(",");
                this.longitude= Double.parseDouble(splitArray[0]);
                this.latitude= Double.parseDouble(splitArray[1]);
                //int[] result=new int[2];
            }
        } catch (Exception e) {
            Log.e(TAG,Log.getStackTraceString(e));
        } finally {
            if (null!= connection) {
                connection.disconnect();
            }
        }
    }

}
