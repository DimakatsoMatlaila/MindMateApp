package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.databinding.ActivityListUsersBinding;
import com.example.login.databinding.ActivityReceiveMessagesBinding;
import com.example.login.databinding.ActivityRemoveAccountBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.Timer;
import java.util.TimerTask;

public class counsellorChat extends AppCompatActivity {
    LinearLayout embed;
    ActivityListUsersBinding binding;

    ActivityRemoveAccountBinding binding2;

    removeAccount removeAccountInst;

    TextView textView;
    ImageView imageView;


    TextView tv;
    ArrayList<String> arrNames=new ArrayList<String>();
    String searchUsername;
    receiverClass receiverclassInst;
    ReceiveMessages receiveMessagesInst;

    SharedPreferences sharedPreferences;
    String[] usernames,used;

    OkHttpClient client = new OkHttpClient();

    String url, url2,responseStr,UsernamesJson;

    Call meth;
    Call meth2;

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

    int track = 5;

    private TextView uname;
    ChatClient chatClient = new ChatClient();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListUsersBinding.inflate(getLayoutInflater());
        binding2 = ActivityRemoveAccountBinding.inflate(getLayoutInflater());
        textView = findViewById(R.id.dAcc);
        //imageView = findViewById(R.id.options);
        embed = new LinearLayout(this);
        sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        String logEmail = sharedPreferences.getString("email", "");
        embed.setOrientation(LinearLayout.VERTICAL);
        TextView head = new TextView(this);
        head.setBackgroundColor(getResources().getColor(R.color.backBlue));
        head.setTextSize(20);
        head.setPadding(16, 16, 16, 16);
        head.setTypeface(null, Typeface.BOLD);
        head.setText("Chats");
        head.setGravity(Gravity.CENTER);
        embed.addView(head);
        /*
        binding.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(binding.options);
            }
        });
        */
        binding.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(counsellorChat.this,"Logging out", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("email");
                editor.remove("UserType");
                editor.apply();
                startMain();
            }
        });
        binding.deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRemove();
            }
        });


        binding2.dAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMain();
                removeAccountInst.delete();
            }
        });

        head.setTextColor(getResources().getColor(R.color.purple_200));

        setContentView(R.layout.activity_counsellor_chat);
        String username = GlobalVariables.getInstance().getGlobalVariable1();
        System.out.println(username);
        TextView ret = new TextView(this);

        // Add query parameters to the URL
        HttpUrl.Builder urlB = HttpUrl.parse("https://lamp.ms.wits.ac.za/home/s2556165/getCounsellorname.php").newBuilder();
        urlB.addQueryParameter("email", logEmail);
        url = urlB.build().toString();

        // Perform the POST request
        meth = post(url, "", new Callback() {
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
                            searchUsername = responseStr;
                            GlobalVariables.getInstance().setLoggedUser(searchUsername);
                            ret.setText(searchUsername);
                            head.setText(searchUsername);
                            GlobalVariables.getInstance().setCounsellorname(searchUsername);
                            System.out.println("The counsellor is: "+GlobalVariables.getInstance().getCounsellorname());
                            ret.setTextSize(40);
                            ret.setTypeface(null, Typeface.BOLD);
                            ret.setGravity(Gravity.CENTER);
                            //ret.setBackgroundDrawable(getResources().getDrawable(R.drawable.background));
                            ret.setTextColor(getResources().getColor(R.color.counName));

                     //embed.addView(ret);

                            // Now that we have the searchUsername, we can make the second POST request
                            HttpUrl.Builder urlB2 = HttpUrl.parse("https://lamp.ms.wits.ac.za/home/s2556165/getUsers2.php").newBuilder();
                            urlB2.addQueryParameter("counsellor", searchUsername);
                            url2 = urlB2.build().toString();
                            meth2 = post(url2, "", new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    // Something went wrong
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (response.isSuccessful()) {
                                        UsernamesJson = response.toString();
                                        final String responseStr = response.body().string();

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                processJSON(responseStr);
                                               // GlobalVariables.getInstance().setCounsellorname(responseStr);
                                                used = createArr(responseStr);
                                                Toast.makeText(counsellorChat.this,"Welcome "+searchUsername + "!",Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    } else {
                                        // Request not successful
                                    }
                                }
                            });
                        }
                    });
                } else {
                    // Request not successful
                }
            }
        });

        setContentView(binding.getRoot());

    }

    public String[] createArr(String str) {
        try {
            // Parse the JSON string into a JSONArray
            JSONArray jsonArray = new JSONArray(str);

            // Create a string array with the same length as the JSONArray
            String[] usernames = new String[jsonArray.length()];

            // Populate the string array with values from the JSONArray
            for (int i = 0; i < jsonArray.length(); i++) {
                usernames[i] = jsonArray.getString(i);
            }

            // Now you can use the 'usernames' string array as needed

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return usernames;
    }

    public void processJSON(String json){
        try {
            JSONArray all = new JSONArray(json);
            String[] usernames = new String[all.length()];

            // Populate the string array with values from the JSONArray
            if (all.length() == 0) {
                TextView nullCase = new TextView(this);
                nullCase.setText("There are no users at the moment. \n Keep checking in to see if a user has been assigned to you!");
                nullCase.setTextColor(getResources().getColor(R.color.backBlue));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT

                );

                layoutParams.gravity = Gravity.CENTER_VERTICAL;

                nullCase.setLayoutParams(layoutParams);
                binding.listLL.addView(nullCase);
            } else {
                for (int i = 0; i < all.length(); i++) {
                    receiveMessagesInst = new ReceiveMessages();
                    if (all.getString(i).toLowerCase().equals("null")) {
                        TextView nullCase = new TextView(this);
                        nullCase.setText("There are no users at the moment. \n Keep checking to see if a user has been assigned to you!");
                        nullCase.setTextColor(getResources().getColor(R.color.backBlue));
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        layoutParams.gravity = Gravity.CENTER_VERTICAL;

                        nullCase.setLayoutParams(layoutParams);

                        binding.listLL.addView(nullCase);
                        continue;
                    }

                    usernames[i] = all.getString(i);
                    arrNames.add(usernames[i]);
                    String user = all.getString(i);
                    receiveMessagesInst.counsellor = searchUsername;
                   // receiveMessagesInst.user = user;
                    System.out.println(user);
                    int j =i;

                    int finalI = i;
                    int finalJ = j;
                    tv = new TextView(this);
                    tv.setText(all.getString(i));
                    tv.setTextColor(getResources().getColor(R.color.white));
                    tv.setTextSize(30);
                    tv.setBackgroundDrawable(getResources().getDrawable(R.color.purple_200));
                    binding.listLL.addView(tv);

                    int finalI1 = i;
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setUsername(finalI1);
                            openChat(finalJ);
                            // setContentView(R.layout.activity_userchatwindow);
                        }
                    });
                    j++;
                }

            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public void openChat(int i){
        GlobalVariables.getInstance().setUsername(arrNames.get(i));
        Intent intent = new Intent(this, ReceiveMessages.class);
        PageStack.push(thisIntent());
        startActivity(intent);
    }
    public void setUsername(int i){
        receiveMessagesInst.user=arrNames.get(i);
    }
    public Intent thisIntent(){
        Intent intent = new Intent(this, counsellorChat.class);
        startActivity(intent);
        return intent;
    }

    public void startRemove(){
        Intent intent = new Intent(this, removeAccount.class);
        startActivity(intent);
    }




    /*
    private void showPopup(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_remove_account, null);

        Dialog dialog = new Dialog(this);
        dialog.setContentView(popupView);
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        // Set the dialog's position
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = location[0] + view.getWidth() / 2;
        params.y = location[1] + view.getHeight();

        dialog.getWindow().setAttributes(params);


        dialog.setTitle("Popup Dialog");
        dialog.setCancelable(true);

        dialog.show();
    }

     */

    public void startMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void deleteAccount(View view){
        removeAccountInst = new removeAccount();
        removeAccountInst.delete();
    }



}
