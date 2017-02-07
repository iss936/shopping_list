package com.example.issa.shoppinglist;

import android.app.Activity;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import org.json.JSONObject;

/**
 * Created by Rshown on 07/02/2017.
 */

public class MenuActivity  extends Activity implements IHttpRequestListener {
    String token;

    @Override
    public void onBackPressed() {
        // Récuperation du token en sharedpreferences file
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        if(token.equals("")) {
            Intent i = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        // On vérifie si l'utilisateur est connecté
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        if(token.equals("")) {
            Intent i = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(i);
        }

        // On récupère les boutons de l'interface
        final Button btn_logout = (Button) findViewById(R.id.btn_logout);
        final Button btn_add_list = (Button) findViewById(R.id.btn_add_list);
        final Button btn_lists = (Button) findViewById(R.id.btn_lists);

        // On supprime l'utilisateur et le redirige vers l'écran de connexion
        btn_logout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                SharedPreferences myPrefs = getSharedPreferences("preferences", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor;
                prefsEditor = myPrefs.edit();
                prefsEditor.remove("token");
                prefsEditor.apply();

                Intent i = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        btn_add_list.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, AddList.class);
                startActivity(i);
            }
        });

        btn_lists.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, ShoppingList.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onSuccess(JSONObject j) {

    }

    @Override
    public void onFailure(String msg) {

    }
}
