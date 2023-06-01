package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class userChat extends AppCompatActivity {
    LinearLayout embed;
    TextView tv;
    String searchUsername;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        embed = new LinearLayout(this);
        String logEmail = GlobalVariables.getInstance().getGlobalVariable1();
        embed.setOrientation(LinearLayout.VERTICAL);
        TextView head = new TextView(this);
        head.setBackgroundColor(getResources().getColor(R.color.backBlue));
        head.setTextSize(35);
        head.setPadding(16, 16, 16, 16);
        head.setTypeface(null, Typeface.BOLD);
        head.setText("Chats");
        head.setGravity(Gravity.CENTER);
        embed.addView(head);

        head.setTextColor(getResources().getColor(R.color.purple_200));

        setContentView(R.layout.activity_counsellor_chat);
        String username = GlobalVariables.getInstance().getGlobalVariable1();
        System.out.println(username);
        TextView ret = new TextView(this);

        // Add query parameters to the URL
        HttpUrl.Builder urlB = HttpUrl.parse("https://lamp.ms.wits.ac.za/home/s2556165/getUserName.php").newBuilder();
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
                            ret.setText(searchUsername);
                            head.setText(searchUsername);
                            GlobalVariables.getInstance().setUsername(searchUsername);
                            System.out.println("The user name is : " + GlobalVariables.getInstance().getUsername());
                            ret.setTextSize(40);
                            ret.setTypeface(null, Typeface.BOLD);
                            ret.setGravity(Gravity.CENTER);
                            //ret.setBackgroundDrawable(getResources().getDrawable(R.drawable.background));
                            ret.setTextColor(getResources().getColor(R.color.counName));

                            //embed.addView(ret);

                            // Now that we have the searchUsername, we can make the second POST request
                            HttpUrl.Builder urlB2 = HttpUrl.parse("https://lamp.ms.wits.ac.za/home/s2556165/getCounsellorName.php").newBuilder();
                            urlB2.addQueryParameter("email", logEmail);
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
                                                used = createArr(responseStr);
                                                Toast.makeText(userChat.this,"Welcome "+searchUsername + "!",Toast.LENGTH_SHORT).show();

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









        setContentView(embed);
        //embed.addView(ret);
        //var.addView(embed);

        //embed.addView(tv);



        /*setContentView(embed);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_userchatwindow);
            }
        });

         */

        // TextView uname= (TextView) findViewById(R.id.reftext);
        //uname = new TextView(this);
        //uname.setText(username);
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
            for (int i = 0; i < all.length(); i++) {
                usernames[i] = all.getString(i);
                GlobalVariables.getInstance().setCounsellorname(usernames[i]);
                System.out.println("The counsellor name is : " + GlobalVariables.getInstance().getCounsellorname());
                tv = new TextView(this);
                if(all.getString(i).toLowerCase().equals("null")){
                    continue;
                }
                tv.setText(all.getString(i));
                tv.setTextColor(getResources().getColor(R.color.white));
                tv.setTextSize(30);
                tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.background));
                embed.addView(tv);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setContentView(R.layout.activity_userchatwindow);
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void openChat(String uname){
        //GlobalVariables.getInstance().setUsername(uname);
       // System.out.println("Username is "+GlobalVariables.getInstance().getUsername());
        Intent intent = new Intent(this,userchatwindow.class);
        startActivity(intent);
    }
}