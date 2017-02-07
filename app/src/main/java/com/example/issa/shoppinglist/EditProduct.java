package com.example.issa.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Rshown on 07/02/2017.
 */

public class EditProduct extends Activity implements IHttpRequestListener {
    String token;
    String name;
    String shopping_list_id;
    String id_product_edit;
    String price;
    String quantity;
    String product_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        //recuperation du token en sharedpreferences file
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        token = sharedPreferences.getString("token","");
        shopping_list_id = sharedPreferences.getString("id_list_edit","");
        id_product_edit = sharedPreferences.getString("id_product_edit", "");
        product_name = sharedPreferences.getString("product_name", "");
        quantity = sharedPreferences.getString("quantity", "");
        price = sharedPreferences.getString("price", "");

        // On envoi le nom de la liste en front
        TextView front_list_name = (TextView)findViewById(R.id.name);
        front_list_name.setText(product_name);

        final NumberPicker front_list_quantity = (NumberPicker)findViewById(R.id.quantity);
        front_list_quantity.setMaxValue(100);
        front_list_quantity.setMinValue(1);
        front_list_quantity.setValue(Integer.parseInt(quantity));
        /*
        front_list_name = (TextView)findViewById(R.id.price);
        front_list_name.setText(price);
*/
        final TextInputLayout name_wrapper = (TextInputLayout) findViewById(R.id.name_wrapper);
       // final TextInputLayout price_wrapper = (TextInputLayout) findViewById(R.id.price);

        Button btn_add = (Button) findViewById(R.id.btn_save);

        btn_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                name = name_wrapper.getEditText().getText().toString();
                quantity = Integer.toString(front_list_quantity.getValue());
              //  price = price_wrapper.getEditText().getText().toString();

                // recuperer ici quantity et price
                if (name.length() < 2) {
                    name_wrapper.setError("Veuillez saisir un mot contenant au moins 2 lettres");
                } else {
                    HttpRequest request = new HttpRequest();
                    request.delegate = EditProduct.this;
                    String encoded_name = "";
                    try {
                        encoded_name = URLEncoder.encode(name, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    request.execute("http://appspaces.fr/esgi/shopping_list/product/edit.php?token="+token+"&name="+encoded_name+"&quantity="+quantity+"&price="+price+"&id="+id_product_edit);
                }
            }
        });
    }

    @Override
    public void onSuccess(JSONObject j) {
        Toast.makeText(getApplicationContext(), "Le produit "+ name + " a été modifié !", Toast.LENGTH_LONG).show();
        SharedPreferences.Editor prefsEditor;
        SharedPreferences myPrefs = getSharedPreferences("preferences", MODE_PRIVATE);
        prefsEditor = myPrefs.edit();
        prefsEditor.remove("id_product_edit");
        prefsEditor.remove("product_name");
        prefsEditor.apply();

        Intent i = new Intent(EditProduct.this, ProductList.class);
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

        prefsEditor = myPrefs.edit();
        prefsEditor.remove("id_product_edit");
        prefsEditor.remove("product_name");
        prefsEditor.remove("quantity");
        prefsEditor.remove("price");
        prefsEditor.apply();

        Intent i = new Intent(EditProduct.this, ProductList.class);
        startActivity(i);
    }

}