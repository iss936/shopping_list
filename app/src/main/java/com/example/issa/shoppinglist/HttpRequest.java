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
 * Debug by Philippe on 07/02/2017.
 */
public class HttpRequest extends AsyncTask<String, Integer, String> {

    String TAG = getClass().getSimpleName();
    public IHttpRequestListener delegate = null;

    @Override
    protected String doInBackground(String... params) {
        String msg = "KO";
        try {
            URL url = new URL(params[0]);



            BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream()));

            String jsonString = new String();

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();

            jsonString = sb.toString();

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
        Log.d("debug print", code);

        if(code.equals("0")) {
            delegate.onSuccess(mainObject);
        }
        else if(code.equals("2")) {
            delegate.onFailure("Cette adresse mail est déjà utilisée");
        }
        else if(code.equals("3")) {
            delegate.onFailure("Email ou mot de passe incorrect");
        }
        else {
            delegate.onFailure("Une erreur est survenue");
        }
    }

}