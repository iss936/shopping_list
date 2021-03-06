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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by philippe on 04/02/2017.
 */

public class ShoppingListActivity extends Activity implements IHttpRequestListener {

    ListView mListView;
    ArrayList<HashMap<String, String>> shoppingListArray;
    String[] head = new String[]{"name", "created_date", "id", "completed"};
    String token;

    private class DeleteService implements IHttpRequestListener {

        void execute(String id) {
            String test = "http://appspaces.fr/esgi/shopping_list/shopping_list/remove.php?token=" + token + "&id=" + id;
            Log.d("requete", test);
            HttpRequest request = new HttpRequest();
            request.delegate = ShoppingListActivity.this;
            request.execute(test);
        }

        public void onSuccess(JSONObject j) {
            Toast.makeText(getApplicationContext(), "liste Supprimée", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(String msg) {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //recuperation du token en sharedpreferences file
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        if(token.equals("")) {
            Intent i = new Intent(ShoppingListActivity.this, LoginActivity.class);
            startActivity(i);
        }

        shoppingListArray = new ArrayList<>();
        mListView = (ListView) findViewById(R.id.list);


        String test = "http://appspaces.fr/esgi/shopping_list/shopping_list/list.php?token=" + token;
        HttpRequest request = new HttpRequest();
        request.delegate = ShoppingListActivity.this;
        request.execute(test);

        Button btn_add = (Button) findViewById(R.id.btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            Intent i = new Intent(ShoppingListActivity.this, AddListActivity.class);
            startActivity(i);
            }

        });
    }


    private void displayListoflist() {

        ListAdapter adapter;
        adapter = new SimpleAdapter(ShoppingListActivity.this, (List<? extends Map<String, ?>>) shoppingListArray,
                R.layout.list_item, head, new int[]{R.id.name,
                R.id.created_date, R.id.id, R.id.completed});

        mListView.setAdapter(adapter);
    }

    private void listenListoflist() {

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            HashMap<String, String> item;
            String id_list;
            String name_list_edit;
            String complete_list_edit;

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long arg3) {

                item = (HashMap<String, String>) parent.getAdapter().getItem(position);
                id_list = (item.get("id"));
                name_list_edit = (item.get("name"));
                complete_list_edit = (item.get("completed"));

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        ShoppingListActivity.this);

                // set title
                alertDialogBuilder.setTitle("Options");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Voulez vous modifier ou supprimer la liste ?")
                        .setCancelable(false)
                        .setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                // ajout de l'id en SharedPreferences pour les Traitements
                                SharedPreferences myPrefs = getSharedPreferences("preferences", MODE_PRIVATE);
                                SharedPreferences.Editor prefsEditor;
                                prefsEditor = myPrefs.edit();
                                prefsEditor.putString("id_list_edit", id_list);
                                prefsEditor.putString("name_list_edit", name_list_edit);
                                prefsEditor.putString("complete_list_edit", complete_list_edit);

                                prefsEditor.commit();

                                Intent i = new Intent(ShoppingListActivity.this, EditListActivity.class);
                                startActivity(i);

                            }
                        })
                        .setNegativeButton("Supprimer", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id1) {

                                new DeleteService().execute(id_list);
                                shoppingListArray.remove(item);
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

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            HashMap<String, String> item;
            String id_list;
            String name_list;
            String complete_list_edit;

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                item = (HashMap<String, String>) parent.getAdapter().getItem(position);
                id_list = (item.get("id"));
                name_list = (item.get("name"));
                complete_list_edit = (item.get("completed"));

                // ajout de l'id en SharedPreferences pour les Traitements
                SharedPreferences myPrefs = getSharedPreferences("preferences", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor;
                prefsEditor = myPrefs.edit();
                prefsEditor.putString("id_list_edit", id_list);
                prefsEditor.putString("name_list_edit", name_list);
                prefsEditor.putString("complete_list_edit", complete_list_edit);
                prefsEditor.commit();

                Intent i = new Intent(ShoppingListActivity.this, ProductListActivity.class);
                startActivity(i);
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
                String created_date = c.getString("created_date");
                String completed = c.getString("completed");

                if(completed == "1") {
                    completed = getString(R.string.complete_list);
                }
                else {
                    completed = getString(R.string.uncomplete_list);
                }

                // On modifie le format de la date
                String inputPattern = "yyyy-MM-dd HH:mm:ss";
                String outputPattern = "dd/MM/yyyy";
                SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                Date date = inputFormat.parse(created_date);
                String str_date = outputFormat.format(date);

                HashMap<String, String> liste = new HashMap<>();
                liste.put("id", id);
                liste.put("name", name);
                liste.put("created_date", str_date);
                liste.put("completed", completed);

                shoppingListArray.add(liste);
            }

        } catch (final JSONException e) {
            Log.e("PPG PARSER", "Json parsing error: " + e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        displayListoflist();
        listenListoflist();
    }


    @Override
    public void onFailure(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
        //recuperation du token en sharedpreferences file
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        if(token.equals("")) {
            Intent i = new Intent(ShoppingListActivity.this, LoginActivity.class);
            startActivity(i);
        }
        else {
            Intent i = new Intent(ShoppingListActivity.this, MenuActivity.class);
            startActivity(i);
        }
    }
}