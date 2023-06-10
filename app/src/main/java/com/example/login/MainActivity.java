package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Login";

    private EditText txtPassword;

    String userType;
    private RadioButton radUser;
    private RadioButton radCounsellor;

    EditText email;

    private SharedPreferences sharedPreferences;

    private RequestCallback requestCallback;
    GlobalVariables globalVars = GlobalVariables.getInstance();
    String EMAIL= "";

    OkHttpClient client = new OkHttpClient();
    private static final long LOADING_DELAY = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        String storedEmail = sharedPreferences.getString("email", "");
        String storedUserType = sharedPreferences.getString("userType", "");

        if (!storedEmail.isEmpty() && !storedUserType.isEmpty()) {
            navigateToMainActivity();
            return;
        }








        setContentView(R.layout.activity_main);
        txtPassword = findViewById(R.id.LogPassword);
        radUser = findViewById(R.id.userrad);
        radCounsellor = findViewById(R.id.counrad);
        email = findViewById(R.id.FPusername);
        userType = "";
        MaterialButton loginBtn = findViewById(R.id.loginbtn);
        TextView forgotPassTextView = findViewById(R.id.forgotpass);
        TextView createAccountText = findViewById(R.id.createAccountText);

        requestCallback = new RequestCallback() {
            @Override
            public void onRequestComplete(String[] passReturned) {
                PageStack.push(thisIntent());
                sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
                String passed1 = new String(passReturned[1].toString().trim().getBytes(), Charset.defaultCharset());
                Log.d(TAG, "The password from server BB: " + passReturned[1]);


                if (radUser.isChecked()) {
                    if (passed1.equals("true")) {
                       boolean R= GlobalVariables.getInstance().isCounsillor(radUser,radCounsellor);
                       GlobalVariables.getInstance().setVld(R);
                        globalVars.setGlobalVariable1(EMAIL);
                        startUserChat();
                        userType = "User";
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", email.getText().toString().trim());
                        editor.putString("userType", userType);
                        editor.apply();
                    } else if(passed1.equals("false")) {
                        Toast.makeText(MainActivity.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                    }
                    else if(passed1.equals("none")){
                        Toast.makeText(MainActivity.this,"User Account does Not exist!",Toast.LENGTH_SHORT).show();;
                    }
                } else if (radCounsellor.isChecked()) {
                    if (passed1.equals("true")) {
                        boolean R= GlobalVariables.getInstance().isCounsillor(radUser,radCounsellor);
                        GlobalVariables.getInstance().setVld(R);
                        globalVars.setGlobalVariable1(EMAIL);
                        //startUserSignupChat();
                        startCounsChat();
                        userType = "Counsellor";
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", email.getText().toString().trim());
                        editor.putString("userType", userType);
                        editor.apply();
                    } else if(passed1.equals("false")) {
                        Toast.makeText(MainActivity.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                    }
                    else if(passed1.equals("none")){
                        Toast.makeText(MainActivity.this,"Counsellor Account does Not exist!",Toast.LENGTH_SHORT).show();;
                    }
                    //openCounsellorPage();
                } else {
                    Toast.makeText(MainActivity.this, "Please select a radio button!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String email = "abc@gmail.com";
                //String pass = "ttt123";
                String email = ((EditText) findViewById(R.id.FPusername)).getText().toString();
                String pass = ((EditText) findViewById(R.id.LogPassword)).getText().toString();
                EMAIL=email;
                if(!email.isEmpty() && !pass.isEmpty()) {
                    String Fpass=HashSalt.hashString(pass);
                    if (radUser.isChecked()) {
                        String url = "https://lamp.ms.wits.ac.za/home/s2555500/ulogin.php?email=" + email + "&&password=" + Fpass;
                        Reques(url, requestCallback);

                    } else if (radCounsellor.isChecked()) {
                        String url = "https://lamp.ms.wits.ac.za/home/s2555500/counlogin.php?email=" + email + "&&password=" + Fpass;
                        Reques(url, requestCallback);
                        // openCounsellorPage(); // Replace with the correct PHP file for counsellors
                    } else {
                        Toast.makeText(MainActivity.this, "Please select a radio button!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this,"Please fill in all the fields!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        forgotPassTextView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {

                openForgotPasswordPage();
            }
        });

        createAccountText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                radUser = findViewById(R.id.userrad);
                radCounsellor = findViewById(R.id.counrad);
                if(radCounsellor.isChecked()) {
                    startCounsSignupActivity();
                }
                else if (radUser.isChecked()){
                    //openUserPage();
                    startUserSignupActivity();
                }
                else{
                    Toast.makeText(MainActivity.this, "Please select Account type!", Toast.LENGTH_SHORT).show();
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

    private void openForgotPasswordPage() {
        Intent intent=new Intent(this,ForgotPasswordActivity.class);
        Toast.makeText(MainActivity.this, "Forgot password clicked", Toast.LENGTH_SHORT).show();
        PageStack.push(thisIntent());
        startActivity(intent);
        // Implement the logic to open the Forgot Password page
        // You can start a new activity or perform any desired action
    }

    private void openUserPage() {
        Toast.makeText(MainActivity.this, "User page clicked", Toast.LENGTH_SHORT).show();

        // Implement the logic to open the User page
        // You can start a new activity or perform any desired action
    }

    private void openCounsellorPage() {
        Toast.makeText(MainActivity.this, "Counsellor page clicked", Toast.LENGTH_SHORT).show();
        // Implement the logic to open the Counsellor page
        // You can start a new activity or perform any desired action
    }

    private void startCounsSignupActivity() {
        Intent intent = new Intent(this, CounsSignup.class);
        PageStack.push(thisIntent());
        startActivity(intent);

    }
    private void startUserSignupActivity(){
        Intent intent = new Intent(this,userSignUpActivity.class);
        PageStack.push(thisIntent());
        startActivity(intent);

    }
    private void startUserChat() {
        Intent intent = new Intent(this, userChat.class);
        //PageStack.push(thisIntent());
        startActivity(intent);

    }
    private void startCounsChat() {
        Intent intent = new Intent(this, counsellorChat.class);
        //PageStack.push(thisIntent());
        startActivity(intent);

    }
    public Intent thisIntent(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return intent;
    }

    public void navigateToMainActivity(){
        if(sharedPreferences.getString("userType", "").equals("User")){
            startUserChat();
        }else{
            startCounsChat();
        }
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject jsonObject;
                            String status = "";
                            try {
                                jsonObject = new JSONObject(responseStr);
                                status = jsonObject.getString("status");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            String[] splitArray = { "status", status };
                            callback.onRequestComplete(splitArray);
                            Log.d(TAG, "The PassstartCounsCword from the server is: " + splitArray[1]);
                            Log.d(TAG, "The Class received from the server is: " + splitArray[1].getClass().getName());
                        }
                    });
                } else {
                    Log.e(TAG, "Response not successful. Code: " + response.code()); // Debug: Log the response code
                    // Toast.makeText(MainActivity.this, "Username already exists, Try a different name", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public interface RequestCallback {
        void onRequestComplete(String[] passReturned);
    }
}