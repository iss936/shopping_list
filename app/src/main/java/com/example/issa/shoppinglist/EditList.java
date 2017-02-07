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

public class EditList extends Activity implements IHttpRequestListener {
    String token;
    String name;
    String id_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        //recuperation du token en sharedpreferences file
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        token = sharedPreferences.getString("token","");
        id_list = sharedPreferences.getString("id_list_edit","");
        //recuperation du token en sharedpreferences file

        Log.d("token", "test"+token);
        final TextInputLayout name_wrapper = (TextInputLayout) findViewById(R.id.name_wrapper);

        Button btn_rename = (Button) findViewById(R.id.btn_rename);

        btn_rename.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                name = name_wrapper.getEditText().getText().toString();


                if (name.length() < 2) {
                    name_wrapper.setError("Veuillez saisir un prénom contenant au moins 2 lettres");
                } else {
                    HttpRequest request = new HttpRequest();
                    request.delegate = EditList.this;
                    request.execute("http://appspaces.fr/esgi/shopping_list/shopping_list/edit.php?token="+token+"&id="+id_list+"&name="+name);
                }
            }
        });
    }

    @Override
    public void onSuccess(JSONObject j) {
        SharedPreferences myPrefs = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor;
        prefsEditor = myPrefs.edit();
        prefsEditor.putString("id_list_edit", "");
        prefsEditor.commit();
        Toast.makeText(getApplicationContext(), "La liste "+ name + " est modifiée!", Toast.LENGTH_LONG).show();
        Intent i = new Intent(EditList.this, ShoppingList.class);
        startActivity(i);
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }


}