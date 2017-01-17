package com.example.issa.shoppinglist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements IHttpRequestListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

        String token = sharedPreferences.getString("token", "existe");

        if (token.equals("existe"))
        {
            Intent i = new Intent(MainActivity.this,IndexActivity.class);
            startActivity(i);
        }

        Button btnLinkSignUp = (Button) findViewById(R.id.btnLinkSignUp);

        btnLinkSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(i);
            }
        });

        // pour la connexion
        Button btnLinkSignIn = (Button)  findViewById(R.id.btnLinkSignIn);

        btnLinkSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText email = (EditText) findViewById(R.id.editTextLoginEmail);
                EditText password = (EditText) findViewById(R.id.editTextLoginPassword);
                boolean ok = true;

                if (email.getText().toString().length() < 1) {
                    ok =false;
                    email.setError("Veuillez saisir une adresse email");
                }
                if (password.getText().toString().length() < 1) {
                    ok =false;
                    password.setError("Veuillez saisir un mot de passe");
                }
                HttpRequest request = new HttpRequest();

                if (ok == true) {
                    request.delegate = MainActivity.this;
                    request.execute("http://appspaces.fr/esgi/shopping_list/account/login.php?email="+email.getText().toString()+"&password="+password.getText().toString());
                }
            }
        });

    }

    @Override
    public void onSuccess(JSONObject j) {
        Toast.makeText(getApplicationContext(),"Vous êtes connecté", Toast.LENGTH_LONG).show();
        String token = "";
        try {
            JSONObject resultObject = j.getJSONObject("result");
            token = resultObject.getString("token");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.commit();

        Intent i = new Intent(MainActivity.this,IndexActivity.class);
        startActivity(i);
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }
}
