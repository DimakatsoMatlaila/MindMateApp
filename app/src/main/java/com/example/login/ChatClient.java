package com.example.login;

import okhttp3.*;

import java.io.IOException;

public class ChatClient {

    private static final String BASE_URL = "https://lamp.ms.wits.ac.za/home/s2555500/get.php";
    // Replace with your PHP script URL


    public static void sendMessage(String sender, String receiver, String text) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("sender", sender)
                .add("receiver", receiver)
                .add("messagetxt", text)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle request success
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    // Process the response if needed
                } else {
                    // Failed to send the message
                    // Handle response error
                }
            }
        });
    }

    // Method to receive messages
    public static void receiveMessages(String sender, String receiver) {
        OkHttpClient client = new OkHttpClient();

        // Build the URL with the sender and receiver as query parameters
        HttpUrl url = HttpUrl.parse(BASE_URL).newBuilder()
                .addQueryParameter("sender", sender)
                .addQueryParameter("receiver", receiver)
                .build();

        // Build the request
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        // Send the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle request failure
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle request success
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    // Process the received messages
                    System.out.println(responseBody);
                } else {
                    // Failed to receive messages
                    // Handle response error
                    System.out.println("Failed to receive messages");
                }
            }
        });
    }

}
