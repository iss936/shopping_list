package com.example.issa.shoppinglist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

        String token = sharedPreferences.getString("token", "");
        Log.d("print token", token);

//        if(token.matches("")) {
//            Intent i = new Intent(IndexActivity.this, LoginActivity.class);
//            startActivity(i);
//        }

        Button btnDisconnect = (Button) findViewById(R.id.btnDisconnect);

        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Vous avez été déconnecté!", Toast.LENGTH_LONG).show();

                SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("token").commit();

                Intent i = new Intent(IndexActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
