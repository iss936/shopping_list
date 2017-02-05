package com.example.issa.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
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
 * Created by Philippe on 04/02/2017.
 */

public class ShoppingList extends Activity implements IHttpRequestListener {

    ListView mListView;
    ArrayList<HashMap<String, String>> ListesdeCourses;
    String[] entetes = new String[]{"name","created_date","id","completed"};
    String token;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //recuperation du token en sharedpreferences file
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        token = sharedPreferences.getString("token","");


        ListesdeCourses = new ArrayList<>();
        mListView = (ListView) findViewById(R.id.list);


        String test = "http://appspaces.fr/esgi/shopping_list/shopping_list/list.php?token="+token;
        Log.d("requete",test);
        Log.d("token", "test"+token);
        HttpRequest request = new HttpRequest();
        request.delegate = ShoppingList.this;
        request.execute(test);



        Button btn_add = (Button) findViewById(R.id.btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(ShoppingList.this, AddList.class);
                startActivity(i);

            }

        });


    }



    private void afficherListoflist(){

        ListAdapter adapter;
        adapter = new SimpleAdapter(ShoppingList.this, (List<? extends Map<String, ?>>)ListesdeCourses,
                        R.layout.list_item,entetes, new int[]{R.id.name,
                        R.id.created_date,R.id.id, R.id.completed});

        //ProductAdapter adapter = new ProductAdapter(ShoppingList.this, products);
        mListView.setAdapter(adapter);
    }

    private void listenListoflist(){

        // Item Click Listener for the listview
        OnItemClickListener itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
                // Getting the Container Layout of the ListView
                LinearLayout linearLayoutParent = (LinearLayout) container;

                // Getting the inner Linear Layout
                LinearLayout linearLayoutChild = (LinearLayout ) linearLayoutParent.getChildAt(1);

                // Getting the Country TextView
                TextView tvCountry = (TextView) linearLayoutChild.getChildAt(0);

                Toast.makeText(getBaseContext(), tvCountry.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        };
    }
    @Override
    public void onSuccess(JSONObject j) {
        Toast.makeText(getApplicationContext(), "chargement effectué", Toast.LENGTH_LONG).show();

        try {
            JSONArray resultObject = j.getJSONArray("result");

            for (int i = 0; i < resultObject.length(); i++) {
                JSONObject c = resultObject.getJSONObject(i);

                String id = c.getString("id");
                String name = c.getString("name");
                String created_date = c.getString("created_date");
                String completed = c.getString("completed");

                HashMap<String, String> liste = new HashMap<>();
                liste.put("id", id);
                liste.put("name", name);
                liste.put("created_date", created_date);
                liste.put("completed", completed );

                ListesdeCourses.add(liste);

                }

        }catch(final JSONException e) {
            Log.e("PPG PARSER" , "Json parsing error: " + e.getMessage());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG)
                            .show();
                }
            });

        }
        afficherListoflist();

    }


    @Override
    public void onFailure(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }




}