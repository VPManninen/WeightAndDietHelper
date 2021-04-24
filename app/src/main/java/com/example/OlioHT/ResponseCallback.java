package com.example.OlioHT;

import androidx.annotation.NonNull;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface ResponseCallback {
    // Volley + JSON object response interface for the ApiHandler class to use when returning results
    // below functions are located in "ApiFragment", which implements this interface.
    void onResponseFinished(@NonNull JSONObject response);
    void onError(@NonNull VolleyError error);
}
