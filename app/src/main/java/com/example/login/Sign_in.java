package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Sign_in extends AppCompatActivity {
    private static final String TAG = "Login";
    private RequestCallback requestCallback;
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText txtPassword = findViewById(R.id.LogPassword);
        MaterialButton loginBtn = findViewById(R.id.loginbtn);
        EditText passEntered = findViewById(R.id.LogPassword);
        final String[] passed = {""};
        final String[] hashedPass = {""};
        final String[] uname = {""};
        RadioButton radU = findViewById(R.id.userradRes);
        RadioButton radC = findViewById(R.id.counradRes);

        requestCallback = new RequestCallback() {
            @Override
            public void onRequestComplete(String[] passReturned) {
                String passed1 = new String(passReturned[0].toString().trim().getBytes(), Charset.defaultCharset());
                System.out.println("The password from server 1: " + passReturned[0]);

                if (radU.isChecked()) {
                    if (passed[0].equals(passed1)) {
                        setContentView(R.layout.activity_user_chat);
                    } else {
                        Toast.makeText(Sign_in.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                    }
                } else if (radC.isChecked()) {
                    setContentView(R.layout.activity_counsellor_chat);
                } else {
                    Toast.makeText(Sign_in.this, "Please select one radio button!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String K = "";
                final String email = "abc@gmail.com";
                uname[0] = txtPassword.getText().toString();
                if (radU.isChecked()) {
                    K = "https://lamp.ms.wits.ac.za/home/s2556165/counlogin.php";
                } else if (radC.isChecked()) {
                    ;
                }
                final String url = K + uname[0];
                passed[0] = passEntered.getText().toString();
                Reques(url, requestCallback);
            }
        });


    }

    Call post(String url, String json, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(null, new byte[0]))
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }


    public final void Reques(String url, RequestCallback callback) {
        final String[] passReturned = {""};
        Log.d(TAG, "URL: " + url);
        post(url, "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            String Pass = "";

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.d(TAG, "Response: " + responseStr);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String x = responseStr;
                            String jsonObjectString = x.substring(1, x.length() - 1);
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(jsonObjectString);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                            String password = null;
                            try {
                                password = jsonObject.getString("Password");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            String[] splitArray = {"Password", password};
                            passReturned[0] = splitArray[1];
                            callback.onRequestComplete(passReturned);
                            System.out.println("The Password from the server is:" + passReturned[0]);
                            System.out.println("The Class for the Received from the server is : " + splitArray[1].getClass().getName());
                        }
                    });
                } else {
                    Log.e(TAG, "Response not successful. Code: " + response.code());
                }
            }
        });
    }

    public interface RequestCallback {
        void onRequestComplete(String[] passReturned);
    }

}
