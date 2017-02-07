package com.example.issa.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by philippe on 07/02/2017.
 */

public class AddProduct extends Activity implements IHttpRequestListener {
    String token;
    String name;
    String quantity;
    String shopping_list_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Récuperation du token en sharedpreferences file
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        token = sharedPreferences.getString("token","");
        shopping_list_id = sharedPreferences.getString("id_list_edit", "");

        final TextInputLayout name_wrapper = (TextInputLayout) findViewById(R.id.name_wrapper);
        final TextInputLayout quantity_wrapper = (TextInputLayout) findViewById(R.id.quantity_wrapper);

        final NumberPicker front_list_quantity = (NumberPicker)findViewById(R.id.quantity);
        front_list_quantity.setMaxValue(100);
        front_list_quantity.setMinValue(1);

        Button btn_add = (Button) findViewById(R.id.btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                name = name_wrapper.getEditText().getText().toString();
                quantity = Integer.toString(front_list_quantity.getValue());

                if (name.length() < 2) {
                    name_wrapper.setError("Veuillez saisir un mot contenant au moins 2 lettres");
                } else {
                    HttpRequest request = new HttpRequest();
                    request.delegate = AddProduct.this;
                    String encoded_name = "";
                    try {
                        encoded_name = URLEncoder.encode(name, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    request.execute("http://appspaces.fr/esgi/shopping_list/product/create.php?token="+token+"&shopping_list_id="+shopping_list_id+"&name="+encoded_name+"&quantity="+quantity);
                }
            }
        });
    }

    @Override
    public void onSuccess(JSONObject j) {
        Toast.makeText(getApplicationContext(), "Le produit "+ name + " a été ajouté !", Toast.LENGTH_LONG).show();
        Intent i = new Intent(AddProduct.this, ProductList.class);
        startActivity(i);
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed(){

        Intent i = new Intent(AddProduct.this, ProductList.class);
        startActivity(i);
    }

}