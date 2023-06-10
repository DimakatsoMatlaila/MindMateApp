package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReceiveMessages extends AppCompatActivity {

    static String counsellor, user;
    String sender;
    String messageR;
    Call methodRun;
    userchatwindow userchatwindowInst;
    RelativeLayout relativeLayout;



    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //relativeLayout=findViewById(R.id.rlayout);
        //doWork();
        refresh();
        openChat();
        System.out.println(user);
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
            int allMess = all.length();
            GlobalVariables.getInstance().setMessageArrSize(allMess);
            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                if(item.getString("messagetext")!=null || item.getString("sender")!=null) {
                    if(item!=null) {
                        sender = item.getString("sender");
                        messageR = item.getString("messagetext");
                        if (messageR == null) {
                            return;
                        }
                        else{
                            String DecryptText = Encryption.decrypt(messageR);
                            if(DecryptText!=null) {
                                userchatwindowInst.sendMessage(sender, DecryptText);
                            }
                        }
                    }else{
                        return;
                    }
                }else{
                    return;
                }
            }
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return;
    }

    public void openChat(){
        Intent intent = new Intent(this,userchatwindow.class);
        PageStack.push(thisIntent());
        startActivity(intent);
    }
    public Intent thisIntent(){
        Intent intent = new Intent(this, userchatwindow.class);
        startActivity(intent);
        return intent;
    }


    public void refresh(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                if (userchatwindowInst.messages != null) {
                    userchatwindowInst.messages.clear();
                }
                doWork();

            }
        };


        timer.scheduleAtFixedRate(task, 0, 3000);
    }
}