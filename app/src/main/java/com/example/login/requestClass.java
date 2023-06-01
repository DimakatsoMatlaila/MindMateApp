package com.example.login;


import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class requestClass {
    String req;

    OkHttpClient client = new OkHttpClient();

    HttpUrl.Builder urlB = HttpUrl.parse("https://lamp.ms.wits.ac.za/home/s2556165/cars.php").newBuilder();
    //urlB.addQueryParameter("username", "pravesh");
    String url = urlB.build().toString();



    private void runOnUiThread(Runnable runnable) {
    }

    public void processJSON(String json){
        try {
            JSONArray all = new JSONArray(json);
            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                String licensePlate = item.getString("CAR_ID");
                if(licensePlate.equals("1")){
                    req = licensePlate;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    Call post(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    Call post = post(url, "", new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            // Something went wrong
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                final String responseStr = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processJSON(responseStr);
                        req = "the response method runs";
                    }
                });
            } else {
                // Request not successful
            }
        }
    });


    public String printString(){
        return req;
    }

}
