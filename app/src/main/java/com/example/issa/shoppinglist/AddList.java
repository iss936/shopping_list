package com.example.issa.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by philippe on 05/02/2017.
 */

public class AddList extends Activity implements IHttpRequestListener {
    String token;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        //recuperation du token en sharedpreferences file
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        Button btn_add = (Button) findViewById(R.id.btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            final TextInputLayout name_wrapper = (TextInputLayout) findViewById(R.id.name_wrapper);
            name = name_wrapper.getEditText().getText().toString();

            if(name.length() < 2) {
                name_wrapper.setError("Veuillez saisir un prénom contenant au moins 2 lettres");
            }
            else {
                HttpRequest request = new HttpRequest();
                request.delegate = AddList.this;
                request.execute("http://appspaces.fr/esgi/shopping_list/shopping_list/create.php?token="+token+"&name="+name);
            }
            }
        });
    }

    @Override
    public void onSuccess(JSONObject j) {
        Toast.makeText(getApplicationContext(), "La liste '" + name + "' est créée!", Toast.LENGTH_LONG).show();
        Intent i = new Intent(AddList.this, ShoppingList.class);
        startActivity(i);
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(AddList.this, ShoppingList.class);
        startActivity(i);
    }
}