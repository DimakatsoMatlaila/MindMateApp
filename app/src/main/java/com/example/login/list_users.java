package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.login.databinding.ActivityListUsersBinding;
import com.example.login.databinding.ActivityRemoveAccountBinding;

public class list_users extends AppCompatActivity {
    ImageView imageView;
    ActivityListUsersBinding binding;

    ActivityRemoveAccountBinding binding2;

    removeAccount removeAccountInst;

    View secondLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);
        binding = ActivityListUsersBinding.inflate(getLayoutInflater());
        binding2 = ActivityRemoveAccountBinding.inflate(getLayoutInflater());
        //imageView = findViewById(R.id.options);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Options();
            }
        });

        binding2.dAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMain();
                Toast.makeText(list_users.this, "removing account", Toast.LENGTH_SHORT).show();
                removeAccountInst.delete();
            }
        });

        binding.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("email");
                editor.remove("UserType");
                editor.apply();
                startMain();
            }
        });
    }

    public void deleteAccount(View view){
        Toast.makeText(list_users.this,"Removing account",Toast.LENGTH_SHORT).show();
        startMain();
        removeAccountInst.delete();
    }

    private void showPopup() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_remove_account, null);

        Dialog dialog = new Dialog(this);
        dialog.setContentView(popupView);

        dialog.setTitle("Popup Dialog");
        dialog.setCancelable(true);

        dialog.show();
    }

    public void Options() {
        Toast.makeText(list_users.this, "imageViewClicked", Toast.LENGTH_SHORT).show();
        showPopup();
    }

    public void startMain(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        System.exit(0);
    }
}
