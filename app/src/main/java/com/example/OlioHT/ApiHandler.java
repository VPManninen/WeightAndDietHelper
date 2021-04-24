package com.example.OlioHT;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class ApiHandler {

    /* Brains of the API fragment and most methods required for API data fetching.
    * Fetching is done using JSON objects.
    * Singleton */

    private static ApiHandler handler = new ApiHandler();
    // diet has been hard coded to be "omnivore" as total calculator has minor difference in base values between the 3 options.
    // as vegans/vegetarians can leave meat/fish etc to be 0.
    private static final String API_URL = "https://ilmastodieetti.ymparisto.fi/ilmastodieetti/calculatorapi/v1/FoodCalculator?query.diet=omnivore";

    private ApiHandler() {
    }

    public static ApiHandler getInstance() {return handler; }

    public int getPercentage(boolean ent5, boolean ent20, boolean ent25, boolean ent50, boolean ent100) {
        /* this method returns the cumulative percentage from ticked boxes @ Api fragment
        * Adds the boxes value to "res" if checked and returns the sum*/
        int res = 0;
        if (ent5) {
            res += 5;
        }
        if (ent20) {
            res += 20;
        }
        if (ent25) {
            res += 25;
        }
        if (ent50) {
            res += 50;
        }
        if (ent100) {
            res += 100;
        }
        return res;
    }

    public void getTotalConsumption(int beefPer, int fishPer, int dairyPer, int saladPer, int ricePer, @NonNull ResponseCallback responseCallback, @NonNull RequestQueue requestQueue, @NonNull String tag) {
        /* method for total consumption fetching.
              inputs all required values for the calculator,
              ResponceCallback through interface-class and implemented as methods in "Api fragment"
              Volley RequestQueue to save multiple requests if required or happened through "spamming" etc.
              Tag for the JSONobject to use and have "why"

              Volley uses asynchronous HTTP requests.
         */

        // request url building for the API
        String requestUrl = String.format(API_URL + "&query.beefLevel=%d&query.fishLevel=%d&query.dairyLevel=%d&query.riceLevel=%d&query.winterSaladLevel=%d", beefPer, fishPer, dairyPer, ricePer, saladPer);
        // create a Volley json request with given information
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, responseCallback::onResponseFinished, responseCallback::onError);
        // add the tag
        jsonObjectRequest.setTag(tag);
        // add the request to the queue -> Volley JSON request is resolved and result handled @ api fragment
        requestQueue.add(jsonObjectRequest);
    }

}
