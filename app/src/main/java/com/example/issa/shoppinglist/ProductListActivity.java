package com.example.issa.shoppinglist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by philippe on 06/02/2017.
 */

public class ProductListActivity extends Activity implements IHttpRequestListener {

    ListView mListView;
    ArrayList<HashMap<String, String>> ListesdeProduits;
    String[] entetes = new String[]{"name", "quantity", "id", "price"};
    String token;
    String shopping_list_id;
    String shopping_list_name;

    private class DeleteService implements IHttpRequestListener {

        void execute(String id) {
            String test = "http://appspaces.fr/esgi/shopping_list/product/remove.php?token=" + token + "&id=" + id;
            Log.d("requete", test);
            HttpRequest request = new HttpRequest();
            request.delegate = ProductListActivity.this;
            request.execute(test);
        }

        public void onSuccess(JSONObject j) {
            Toast.makeText(getApplicationContext(), "Produit Supprimé", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(String msg) {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_product);

        //recuperation du token en sharedpreferences file
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        shopping_list_id = sharedPreferences.getString("id_list_edit", "");
        shopping_list_name = sharedPreferences.getString("name_list_edit", "");

        // On envoi le nom de la liste en front
        TextView front_list_name = (TextView)findViewById(R.id.list_name);
        front_list_name.setText(shopping_list_name);

        ListesdeProduits = new ArrayList<>();
        mListView = (ListView) findViewById(R.id.list);


        String test = "http://appspaces.fr/esgi/shopping_list/product/list.php?token=" + token+"&shopping_list_id="+shopping_list_id;
        HttpRequest request = new HttpRequest();
        request.delegate = ProductListActivity.this;
        request.execute(test);


        Button btn_add = (Button) findViewById(R.id.btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(ProductListActivity.this, AddProductActivity.class);
                startActivity(i);
            }
        });
    }


    private void afficherListoflist() {
        ListAdapter adapter;
        adapter = new SimpleAdapter(ProductListActivity.this, (List<? extends Map<String, ?>>) ListesdeProduits,
                R.layout.list_item, entetes, new int[]{R.id.name,
                R.id.created_date, R.id.id, R.id.completed});

        mListView.setAdapter(adapter);
    }

    private void listenListoflist() {

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            HashMap<String, String> item;
            String id_product_edit;
            String product_name;
            String quantity;
            String price;

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long arg3) {

                item = (HashMap<String, String>) parent.getAdapter().getItem(position);
                id_product_edit = (item.get("id"));
                product_name = (item.get("name"));
                quantity = (item.get("quantity"));
                price = (item.get("price"));

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        ProductListActivity.this);

                // set title
                alertDialogBuilder.setTitle("Options");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Voulez vous modifier ou supprimer l'article ?")
                        .setCancelable(false)
                        .setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // ajout de l'id en SharedPreferences pour les Traitements
                                SharedPreferences myPrefs = getSharedPreferences("preferences", MODE_PRIVATE);
                                SharedPreferences.Editor prefsEditor;
                                prefsEditor = myPrefs.edit();
                                prefsEditor.putString("id_product_edit", id_product_edit);
                                prefsEditor.putString("product_name", product_name);
                                prefsEditor.putString("quantity", quantity);
                                // prefsEditor.putString("price", price);
                                prefsEditor.commit();

                                Intent i = new Intent(ProductListActivity.this, EditProductActivity.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("Supprimer", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id1) {
                                new ProductListActivity.DeleteService().execute(id_product_edit);
                                ListesdeProduits.remove(item);
                                dialog.cancel();
                            }
                        })
                        .setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id1) {
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


                return true;
            }

        });
    }

    @Override
    public void onSuccess(JSONObject j) {

        try {
            JSONArray resultObject = j.getJSONArray("result");

            for (int i = 0; i < resultObject.length(); i++) {
                JSONObject c = resultObject.getJSONObject(i);

                String id = c.getString("id");
                String name = c.getString("name");
                String quantity = c.getString("quantity");
                // String price = c.getString("price");

                HashMap<String, String> liste = new HashMap<>();
                liste.put("id", id);
                liste.put("name", name);
                liste.put("quantity", quantity);
                // liste.put("price", price);

                ListesdeProduits.add(liste);
            }

        } catch (final JSONException e) {
            Log.e("PPG PARSER", "Json parsing error: " + e.getMessage());
        }
        afficherListoflist();
        listenListoflist();
    }


    @Override
    public void onFailure(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        SharedPreferences.Editor prefsEditor;
        //recuperation du token en sharedpreferences file
        SharedPreferences myPrefs = getSharedPreferences("preferences", MODE_PRIVATE);
        token = myPrefs.getString("token", "");

        if(token.equals("")) {
            Intent i = new Intent(ProductListActivity.this, LoginActivity.class);
            startActivity(i);
        }
        else {
            prefsEditor = myPrefs.edit();
            prefsEditor.remove("id_list_edit");
            prefsEditor.remove("name_list_edit");
            prefsEditor.apply();

            Intent i = new Intent(ProductListActivity.this, ShoppingListActivity.class);
            startActivity(i);
        }
    }
}