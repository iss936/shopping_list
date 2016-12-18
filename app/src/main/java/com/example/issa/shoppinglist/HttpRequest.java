package com.example.issa.shoppinglist;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Issa on 02/12/2016.
 */
public class HttpRequest extends AsyncTask<String, Integer, String> {

    String TAG = getClass().getSimpleName();
    public IHttpRequestListener delegate = null;

    @Override
    protected String doInBackground(String... params) {
        String msg = "KO";
        try {
            URL url = new URL(params[0]);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream()));

            String jsonString = new String();

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();

            jsonString = sb.toString();

            int response = conn.getResponseCode();
            Log.d("debugReponse=", "The response is: " + response);
            InputStream is = conn.getInputStream();
            is.close();
            conn.disconnect();
            msg = jsonString;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return msg;
    }

    protected void onPreExecute (){
        Log.d(TAG + " PreExceute","On pre Exceute......");
    }

    protected void onProgressUpdate(Integer...a){
        Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
    }

    @Override
    protected void onPostExecute(String result) {

        JSONObject mainObject = null;
        try {
            mainObject = new JSONObject(result);
           // String code = mainObject.getJsonString("code");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String code = mainObject.optString("code");

        if (code.equals("0")) {
            delegate.onSuccess(mainObject);
        }else{
            delegate.onFailure("Une erreur est survenue");
        }
    }

}
