package com.example.issa.shoppinglist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity implements IHttpRequestListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextInputLayout email_wrapper = (TextInputLayout) findViewById(R.id.email_wrapper);
        final TextInputLayout password_wrapper = (TextInputLayout) findViewById(R.id.password_wrapper);
        final Button btn_signin = (Button) findViewById(R.id.btn_signin);
        final Button btn_signup = (Button) findViewById(R.id.btn_signup);

        email_wrapper.setHint("Adresse mail");
        password_wrapper.setHint("Mot de passe");


        btn_signin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String email = email_wrapper.getEditText().getText().toString();
                String password = password_wrapper.getEditText().getText().toString();

                if(email.length() < 1) {
                    email_wrapper.setError("Veuillez renseigner une adresse mail");
                }
                else if(password.length() < 1) {
                    email_wrapper.setError("Veuillez renseigner un mot de passe");
                }
                else {
                    email_wrapper.setErrorEnabled(false);
                    password_wrapper.setErrorEnabled(false);

                    HttpRequest request = new HttpRequest();
                    request.delegate = LoginActivity.this;
                    request.execute("http://appspaces.fr/esgi/shopping_list/account/login.php?email=" + email + "&password=" + password);
                }
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onSuccess(JSONObject j) {
        Toast.makeText(getApplicationContext(), "Vous êtes connecté!", Toast.LENGTH_LONG).show();
        String token = "";

        try {
            JSONObject resultObject = j.getJSONObject("result");
            token = resultObject.getString("token");

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token).commit();

        Intent i = new Intent(LoginActivity.this, IndexActivity.class);
        startActivity(i);
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}