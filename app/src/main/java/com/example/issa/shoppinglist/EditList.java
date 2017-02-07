package com.example.issa.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

/**
 * Created by philippe on 05/02/2017.
 */

public class EditList extends Activity implements IHttpRequestListener {
    String token;
    String name;
    String complete;
    String complete_list;
    String id_list;
    String name_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        name_list = sharedPreferences.getString("name_list_edit", "");
        complete_list = sharedPreferences.getString("complete_list_edit", getString(R.string.uncomplete_list));

        final TextInputLayout name_wrapper = (TextInputLayout) findViewById(R.id.name_wrapper);
        final CheckBox cb_complete = (CheckBox) findViewById(R.id.cb_complete);

        // On envoi le nom de la liste en front
        TextView front_list_name = (TextView)findViewById(R.id.name);

        front_list_name.setText(name_list);

        if(complete_list == getString(R.string.complete_list)) {
            cb_complete.setChecked(true);
        }

        // Récuperation du token en sharedpreferences file
        sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        token = sharedPreferences.getString("token","");
        id_list = sharedPreferences.getString("id_list_edit", "");

        Button btn_save = (Button) findViewById(R.id.btn_rename);

        btn_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                name = name_wrapper.getEditText().getText().toString();

                if(cb_complete.isChecked()) {
                    complete = "1";
                }
                else {
                    complete = "0";
                }

                if (name.length() < 2) {
                    name_wrapper.setError("Veuillez saisir un prénom contenant au moins 2 lettres");
                } else {
                    HttpRequest request = new HttpRequest();
                    request.delegate = EditList.this;
                    try {
                        name = URLEncoder.encode(name, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    request.execute("http://appspaces.fr/esgi/shopping_list/shopping_list/edit.php?token="+token+"&id="+id_list+"&name="+name+"&completed="+complete);
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

        Toast.makeText(getApplicationContext(), "La liste "+ name + " a été modifiée!", Toast.LENGTH_LONG).show();

        Intent i = new Intent(EditList.this, ShoppingList.class);
        startActivity(i);
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed(){
        SharedPreferences.Editor prefsEditor;

        SharedPreferences myPrefs = getSharedPreferences("preferences", MODE_PRIVATE);
        token = myPrefs.getString("token", "");

        if(token.equals("")) {
            Intent i = new Intent(EditList.this, LoginActivity.class);
            startActivity(i);
        }
        else {
            prefsEditor = myPrefs.edit();
            prefsEditor.remove("id_list_edit");
            prefsEditor.remove("name_list_edit");
            prefsEditor.apply();

            Intent i = new Intent(EditList.this, ShoppingList.class);
            startActivity(i);
        }
    }
}