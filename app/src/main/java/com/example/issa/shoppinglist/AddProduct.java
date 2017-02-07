package com.example.issa.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by Steve on 07/02/2017.
 */

public class AddProduct extends Activity implements IHttpRequestListener {
    String token;
    String name;
    String shopping_list_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        //recuperation du token en sharedpreferences file
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        token = sharedPreferences.getString("token","");
        shopping_list_id = sharedPreferences.getString("id_list_edit","");

        final TextInputLayout name_wrapper = (TextInputLayout) findViewById(R.id.name_wrapper);

        Button btn_add = (Button) findViewById(R.id.btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                name = name_wrapper.getEditText().getText().toString();


                if (name.length() < 2) {
                    name_wrapper.setError("Veuillez saisir un mot contenant au moins 2 lettres");
                } else {
                    HttpRequest request = new HttpRequest();
                    request.delegate = AddProduct.this;
                    request.execute("http://appspaces.fr/esgi/shopping_list/product/create.php?token="+token+"&shopping_list_id="+shopping_list_id+"&name="+name);
                }
            }
        });
    }

    @Override
    public void onSuccess(JSONObject j) {
        Toast.makeText(getApplicationContext(), "Le produit "+ name + "est créée!", Toast.LENGTH_LONG).show();
        Intent i = new Intent(AddProduct.this, ProductList.class);
        startActivity(i);
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }


}