package com.example.issa.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.TextInputLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class SignUpActivity extends Activity implements IHttpRequestListener {
    String token;

    @Override
    public void onBackPressed() {
        //recuperation du token en sharedpreferences file
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        if(!token.equals("")) {
            Intent i = new Intent(SignUpActivity.this, ShoppingList.class);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final TextInputLayout email_wrapper = (TextInputLayout) findViewById(R.id.email_wrapper);
        final TextInputLayout first_name_wrapper = (TextInputLayout) findViewById(R.id.first_name_wrapper);
        final TextInputLayout last_name_wrapper = (TextInputLayout) findViewById(R.id.last_name_wrapper);
        final TextInputLayout password_wrapper = (TextInputLayout) findViewById(R.id.password_wrapper);
        final TextInputLayout password_confirm_wrapper = (TextInputLayout) findViewById(R.id.password_confirm_wrapper);

        Button btn_signup = (Button) findViewById(R.id.btn_signup);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String email = email_wrapper.getEditText().getText().toString();
                String first_name = first_name_wrapper.getEditText().getText().toString();
                String last_name = last_name_wrapper.getEditText().getText().toString();
                String password = password_wrapper.getEditText().getText().toString();
                String password_confirm = password_confirm_wrapper.getEditText().getText().toString();

                if (isValidEmail(email) == false) {
                    email_wrapper.setError("Veuillez saisir une adresse email valide");
                } else if (last_name.length() < 2) {
                    last_name_wrapper.setError("Veuillez saisir un nom contenant au moins 2 lettres");
                } else if (first_name.length() < 2) {
                    first_name_wrapper.setError("Veuillez saisir un prénom contenant au moins 2 lettres");
                } else if (password.length() < 2) {
                    password_wrapper.setError("Veuillez saisir un mot de passe contenant au moins 2 lettres");
                } else if (!password.equals(password_confirm)) {
                    password_wrapper.setError("Veuillez saisir 2 mot de passe identiques");
                    password_confirm_wrapper.setError("Veuillez saisir 2 mot de passe identiques");
                } else {
                    HttpRequest request = new HttpRequest();
                    request.delegate = SignUpActivity.this;
                    request.execute("http://appspaces.fr/esgi/shopping_list/account/subscribe.php?email=" + email + "&password=" + password + "&lastname=" + last_name + "&firstname=" + first_name);
                }
            }
        });
    }

    @Override
    public void onSuccess(JSONObject j) {
        Toast.makeText(getApplicationContext(), "Inscription validée!", Toast.LENGTH_LONG).show();
        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(i);
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}