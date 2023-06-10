package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class removeAccount extends AppCompatActivity {

    static OkHttpClient client = new OkHttpClient();
    String res,logger,rec;
    userchatwindow userchatwindowInst;

    SharedPreferences sharedPreferences;

    TextView textView;
    static Call methodRun;

    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_account);
        sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        String storedEmail = sharedPreferences.getString("email", "");

        setUser();
        logger= GlobalVariables.getInstance().getLoggedUser();
        user = logger;
        textView = findViewById(R.id.dAcc);
        /*
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMain();
                delete();
            }
        });

         */


    }

    public void setUser(){
        if (sharedPreferences.getString("email","").equals("Counsellor")) {
            logger = GlobalVariables.getInstance().getCounsellorname();
            rec = GlobalVariables.getInstance().getUsername();
        } else{
            logger = GlobalVariables.getInstance().getUsername();
            rec = GlobalVariables.getInstance().getCounsellorname();
        }
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    static Call post(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public void delete(){
        String url;
        if(GlobalVariables.getInstance().getVld()==true) {
            HttpUrl.Builder urlB = HttpUrl.parse("https://lamp.ms.wits.ac.za/home/s2556165/deleteCounsellors.php").newBuilder();
            urlB.addQueryParameter("username", user);
            url = urlB.build().toString();
        }else{
            HttpUrl.Builder urlB = HttpUrl.parse("https://lamp.ms.wits.ac.za/home/s2556165/deleteUsers.php").newBuilder();
            urlB.addQueryParameter("username", GlobalVariables.getInstance().getLoggedUser());
            urlB.addQueryParameter("counsellor", rec);
            url = urlB.build().toString();
        }

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
                            res = responseStr;
                        }
                    });
                } else {
                    // Request not successful
                }
            }
        });
    }


    public void logOut(View view){
        openMain();
    }

    public void deleteAccount(View view){
        System.out.println(GlobalVariables.getInstance().getLoggedUser());
        Toast.makeText(removeAccount.this,"Removing account",Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("email");
        editor.remove("UserType");
        editor.apply();
        delete();
        openMain();

    }

    public void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}