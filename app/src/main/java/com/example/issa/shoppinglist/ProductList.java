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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Steve on 06/02/2017.
 */

public class ProductList extends Activity implements IHttpRequestListener {

    ListView mListView;
    ArrayList<HashMap<String, String>> ListesdeProduits;
    String[] entetes = new String[]{"name", "quantity", "id", "price"};
    String token;
    String shopping_list_id;

    private class DeleteService implements IHttpRequestListener {

        void execute(String id) {
            String test = "http://appspaces.fr/esgi/shopping_list/product/remove.php?token=" + token + "&id=" + id;
            Log.d("requete", test);
            HttpRequest request = new HttpRequest();
            request.delegate = ProductList.this;
            request.execute(test);

        }

        public void onSuccess(JSONObject j) {
            Toast.makeText(getApplicationContext(), "Produit Supprimée", Toast.LENGTH_LONG).show();
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
        shopping_list_id = sharedPreferences.getString("id_list_edit","");


        ListesdeProduits = new ArrayList<>();
        mListView = (ListView) findViewById(R.id.list);


        String test = "http://appspaces.fr/esgi/shopping_list/product/list.php?token=" + token+"&shopping_list_id="+shopping_list_id;
        Log.d("requete", test);
        Log.d("token", "test" + token);
        HttpRequest request = new HttpRequest();
        request.delegate = ProductList.this;
        request.execute(test);


        Button btn_add = (Button) findViewById(R.id.btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(ProductList.this, AddProduct.class);
                startActivity(i);
            }
        });


    }


    private void afficherListoflist() {

        ListAdapter adapter;
        adapter = new SimpleAdapter(ProductList.this, (List<? extends Map<String, ?>>) ListesdeProduits,
                R.layout.list_item, entetes, new int[]{R.id.name,
                R.id.created_date, R.id.id, R.id.completed});

        mListView.setAdapter(adapter);
    }

    private void listenListoflist() {

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            HashMap<String, String> item;
            String id_list;

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long arg3) {

                item = (HashMap<String, String>) parent.getAdapter().getItem(position);
                id_list = (item.get("id"));

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        ProductList.this);

                // set title
                alertDialogBuilder.setTitle("Menu");

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
                                prefsEditor.putString("id_list_edit", id_list);
                                prefsEditor.commit();

                                Intent i = new Intent(ProductList.this, EditList.class);
                                startActivity(i);

                            }
                        })
                        .setNegativeButton("Supprimer", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id1) {

                                new ProductList.DeleteService().execute(id_list);
                                ListesdeProduits.remove(item);
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


                return false;
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
                String price = c.getString("price");

                HashMap<String, String> liste = new HashMap<>();
                liste.put("id", id);
                liste.put("name", name);
                liste.put("quantity", quantity);
                liste.put("price", price);

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


}