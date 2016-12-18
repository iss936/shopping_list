package com.example.issa.shoppinglist;

import org.json.JSONObject;

/**
 * Created by Issa on 02/12/2016.
 */
public interface IHttpRequestListener {
    void onSuccess(JSONObject j);
    void onFailure(String msg);
}
