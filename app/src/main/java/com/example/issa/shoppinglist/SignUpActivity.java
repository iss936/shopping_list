package com.example.issa.shoppinglist;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements IHttpRequestListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button btnSignUp = (Button) findViewById(R.id.btnInscription);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                HttpRequest request = new HttpRequest();
                EditText email = (EditText) findViewById(R.id.editTextEmail);
                EditText name = (EditText) findViewById(R.id.editTextName);
                EditText firstName = (EditText) findViewById(R.id.editTextFirstname);
                EditText password = (EditText) findViewById(R.id.editTextPassword);
                EditText rePassword = (EditText) findViewById(R.id.editTextRePassword);

                Boolean ok = true;

                if (isValidEmail(email.getText().toString()) == false) {
                    ok =false;
                    email.setError("Veuillez saisir une adresse email valid");
                }
                if (name.getText().toString().length() < 2) {
                    ok =false;
                    name.setError("Veuillez saisir un nom contenant au moins 2 lettres");
                }
                if (firstName.getText().toString().length() < 2) {
                    ok =false;
                    firstName.setError("Veuillez saisir un prénom contenant au moins 2 lettres");
                }
                if (password.getText().toString().length() < 2) {
                    ok =false;
                    password.setError("Veuillez saisir un mot de passe contenant au moins 2 lettres");
                }
                if (!password.getText().toString().equals(rePassword.getText().toString())) {
                    ok =false;
                    password.setError("Veuillez saisir 2 mot de passe identiques");
                    rePassword.setError("Veuillez saisir 2 mot de passe identiques");
                }
                Toast.makeText(getApplicationContext(),name.getText().toString(), Toast.LENGTH_LONG).show();

                if (ok == true) {
                    request.delegate = SignUpActivity.this;
                    request.execute("http://appspaces.fr/esgi/shopping_list/account/subscribe.php?email="+email.getText().toString()+"&password="+password.getText().toString()+"&lastname="+name.getText().toString()+"&firstname="+firstName.getText().toString());
                }

                /* try {
                    URL url = new URL("http://appspaces.fr/esgi/shopping_list/account/subscribe.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000); // milliseconds
                    conn.setConnectTimeout(15000); // milliseconds
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("email", "soumar_i14_myges_fr")
                            .appendQueryParameter("password", "esgi")
                            .appendQueryParameter("name", "issa")
                            .appendQueryParameter("firstname", "issa");
                    String query = builder.build().getEncodedQuery();

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();

                    conn.connect();
                    int response = conn.getResponseCode();
                    Log.d("debug=", "The response is: " + response);
                    InputStream is = conn.getInputStream();
                    is.close();
                    conn.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                } */

            }
        });

    }

    @Override
    public void onSuccess(JSONObject j) {
        Toast.makeText(getApplicationContext(),"Inscription enregistrée veuillez vous connecter", Toast.LENGTH_LONG).show();
        Intent i = new Intent(SignUpActivity.this,MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}
