package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    private static final long LOADING_DELAY = 5000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // Delay the opening of the main activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openMainActivity();
            }
        }, LOADING_DELAY);

    }
    private void openMainActivity() {
        Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Finish the loading activity
    }
}
