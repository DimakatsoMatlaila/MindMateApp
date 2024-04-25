package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ForgotPasswordActivity";

    private RadioButton radUser;
    private RadioButton radCounsellor;
    private RequestCallback requestCallback;
    GlobalVariables globalVars = GlobalVariables.getInstance();
    String EMAIL = "";
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        radCounsellor = findViewById(R.id.counradRes);
        radUser = findViewById(R.id.userradRes);
        EditText UNAME = findViewById(R.id.ResUsername);
        EditText EMAIL = findViewById(R.id.FPemail);
        EditText newPass = findViewById(R.id.newPasswordEditText);
        EditText cnewPass = findViewById(R.id.confirmPasswordEditText);

        requestCallback = new RequestCallback() {
            @Override
            public void onRequestComplete(String passReturned) {
                String passed1 = passReturned.trim();

                Log.d(TAG, "The password from server BB: " + passed1);

                if (passed1.equals("true")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ForgotPasswordActivity.this, "Password reset successful!", Toast.LENGTH_SHORT).show();
                            openMain();
                        }
                    });
                } else if (passed1.equals("false")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ForgotPasswordActivity.this, "Reset failed! User does not exist!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        };

        Button resetBtn = findViewById(R.id.resetPasswordButton);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = UNAME.getText().toString();
                String email = EMAIL.getText().toString();
                String pass1 = newPass.getText().toString();
                String pass2 = cnewPass.getText().toString();
                if (!username.isEmpty() && !email.isEmpty() && !pass1.isEmpty() && !pass2.isEmpty()) {
                    if (pass1.equals(pass2)) {
                        String Fpass = HashSalt.hashString(pass1);
                        if (radUser.isChecked()) {
                            Toast.makeText(ForgotPasswordActivity.this, "Rad user selected!", Toast.LENGTH_SHORT).show();
                            String url = "https://lamp.ms.wits.ac.za/home/scyber/resetuser.php?username=" + username + "&&email=" + email + "&&password=" + Fpass;
                            Reques(url, requestCallback);
                        } else if (radCounsellor.isChecked()) {
                            String url = "https://lamp.ms.wits.ac.za/home/scyber/resetcoun.php?username=" + username + "&&email=" + email + "&&password=" + Fpass;
                            Reques(url, requestCallback);
                            // openCounsellorPage(); // Replace with the correct PHP file for counsellors
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "Please select a radio button!", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Password does not match!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
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
        Log.d(TAG, "URL: " + url); // Debug: Log the URL to verify it's correct
        post(url, "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Something went wrong
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.d(TAG, "Response: " + responseStr); // Debug: Log the response
                    String [] arr = responseStr.split(":");
                    String ans = arr[1].substring(0,4);
                    Log.d(TAG,"pass: "+ ans);
                    if(new String(ans.trim().getBytes(),Charset.defaultCharset()).equals("true")){

                        openMain();

                    }else {
                        //Toast.makeText(ForgotPasswordActivity.this, "Reset Unsuccessful,Username does not exist!", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        System.out.println("......"+ ans);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        String status = jsonObject.getString("status");
                        String status1= new String(status.trim().getBytes(),Charset.defaultCharset());

                        if (status1.equals("true")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //openMain();
                                    //Toast.makeText(ForgotPasswordActivity.this, "Password reset successful!", Toast.LENGTH_SHORT).show();

                                }
                            });
                        } else if (status1.equals("false")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(ForgotPasswordActivity.this, "Reset failed! User does not exist!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "Response not successful. Code: " + response.code()); // Debug: Log the response code
                    // Toast.makeText(MainActivity.this, "Username already exists, Try a different name", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public void openMain() {
      //  Toast.makeText(ForgotPasswordActivity.this, "Reset succesful,sign in!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public interface RequestCallback {
        void onRequestComplete(String passReturned);
    }
}
