package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class CounsSignup extends AppCompatActivity {
    private static final String TAG = "Login";

    OkHttpClient client = new OkHttpClient();
    private EditText txtEmail;
    private RadioButton ADS;
    private RadioButton AlcoholAngerOther;
    private RadioButton SE;
    private RadioButton grief;
    private EditText txtUsername;
    private EditText txtPassword;
    private EditText txtCPassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couns_signup);
        txtEmail = findViewById(R.id.CEmail);
        txtUsername=findViewById(R.id.Cuname);
        txtPassword=findViewById(R.id.LogCPassword);
        AlcoholAngerOther = findViewById(R.id.prob2);
        SE = findViewById(R.id.prob6);
        grief=findViewById(R.id.prob7);
        ADS = findViewById(R.id.prob4);
        MaterialButton signupbtn = findViewById(R.id.signupbtn);
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String specialty ="";
                String password="";
                String email = ((EditText) findViewById(R.id.CEmail)).getText().toString();
                String username = ((EditText) findViewById(R.id.Cuname)).getText().toString();
                String pass1 = ((EditText) findViewById(R.id.LogCPassword)).getText().toString();
                String pass2= ((EditText) findViewById(R.id.crtpasswordC)).getText().toString();
                if(pass1.equals(pass2)){
                    password=pass1.trim();

                }
                String Fpass=HashSalt.hashString(password).trim();

                if(SE.isChecked()){
                    specialty = "Self-Esteem";
                } else if (grief.isChecked()) {
                    specialty ="Grief/Loss";

                } else if (ADS.isChecked()) {
                    specialty="Anxiety/Depresion/Stress";

                } else if (AlcoholAngerOther.isChecked()) {
                    specialty="Alcohol/Anger/Drugs/other";

                }

                else {
                    Toast.makeText(CounsSignup.this, "Select one Specialty!", Toast.LENGTH_SHORT).show();
                }
                specialty.trim();
                int c=0;
                int p =validateinput(c);
                if(p==0) {
                    String url = "https://lamp.ms.wits.ac.za/home/scyber/csignup.php";
                    Log.d(TAG, "URL: " + url); // Debug: Log the URL to verify it's correct

                    // Create the request body with the form parameters
                    RequestBody requestBody = new FormBody.Builder()
                            .add("email", email)
                            .add("username", username)
                            .add("specialty", specialty)
                            .add("password", Fpass)
                            .build();

                    // Create the POST request
                    Request request = new Request.Builder()
                            .url(url)
                            .post(requestBody)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
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
                                        // Update the UI if needed
                                        Toast.makeText(CounsSignup.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                                        openMain();

                                    }
                                });
                            } else {
                                Log.e(TAG, "Response not successful. Code: " + response.code());
                                // Debug: Log the response code
                            }
                        }
                    });

                }else{
                    Toast.makeText(CounsSignup.this, "Sigup failed!", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
    public void openMain(){
        Intent intent= new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    public int validateinput(int p){
        p = 0;
        String email = ((EditText) findViewById(R.id.CEmail)).getText().toString();
        String username = ((EditText) findViewById(R.id.Cuname)).getText().toString();
        String pass1 = ((EditText) findViewById(R.id.LogCPassword)).getText().toString();
        String pass2= ((EditText) findViewById(R.id.crtpasswordC)).getText().toString();
        if(email.isEmpty()){
            p+=1;
            Toast.makeText(this, "Email address is required!", Toast.LENGTH_SHORT).show();
        } else if (username.isEmpty()) {
            p+=1;
            Toast.makeText(this, "Username address is required!", Toast.LENGTH_SHORT).show();

        } else if (pass1.isEmpty()) {
            p+=1;
            Toast.makeText(this, "Please set password!", Toast.LENGTH_SHORT).show();
        } else if (pass2.isEmpty()) {
            p+=1;
            Toast.makeText(this, "Please confirm password!", Toast.LENGTH_SHORT).show();

        }
        if(pass1.equals(pass2)){
            p+=0;
        }
        else{
            p++;
            Toast.makeText(this, "Password does not Match!", Toast.LENGTH_SHORT).show();
        }

        AlcoholAngerOther = findViewById(R.id.prob2);
        SE = findViewById(R.id.prob6);
        grief=findViewById(R.id.prob7);
        ADS = findViewById(R.id.prob4);
        if(ADS.isChecked()|| SE.isChecked()||AlcoholAngerOther.isChecked()|| grief.isChecked()){
            p+=0;

        }
        else{
            p+=1;
        }



        return p;
    }
}