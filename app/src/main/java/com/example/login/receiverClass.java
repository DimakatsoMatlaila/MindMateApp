package com.example.login;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.databinding.ActivityMainBinding;

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

public class receiverClass  extends AppCompatActivity {
    static String counsellor, user;
    String sender;
    String messageR;
    Call methodRun;
    userchatwindow userchatwindowInst;

    OkHttpClient client = new OkHttpClient();

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

    public void doWork(){
        HttpUrl.Builder urlB = HttpUrl.parse("https://lamp.ms.wits.ac.za/home/s2556165/getMessages.php").newBuilder();
        urlB.addQueryParameter("counsellor", counsellor);
        urlB.addQueryParameter("username", user);
        String url = urlB.build().toString();

        methodRun= post(url, "", new Callback() {
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
                            processSenderMessasge(responseStr);
                        }
                    });
                } else {
                    // Request not successful
                }
            }
        });
    }

    public void processSenderMessasge(String json){
        try {
            JSONArray all = new JSONArray(json);
            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                sender = item.getString("sender");
                messageR = item.getString("messagetext");
                userchatwindowInst.sendMessage(sender, messageR);
            }
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }




    public LayoutInflater getLayoutInflater() {
        return null;
    }

}

