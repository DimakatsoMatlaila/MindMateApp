package com.example.login;

import okhttp3.*;

import java.io.IOException;

public class ChatClient {

    private static final String BASE_URL = "https://lamp.ms.wits.ac.za/home/s2555500/messagebuilder.php";
    // Replace with your PHP script URL


    public static void sendMessage(String sname, String rname, String messagetext) {
        OkHttpClient client = new OkHttpClient();


        RequestBody requestBody = new FormBody.Builder()
                .add("sname", sname)
                .add("rname", rname)
                .add("messagetext", messagetext)
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
                    // Message sent successfully
                    String responseBody = response.body().string();
                    //public void run()
                    // Process the response if needed
                } else {
                    // Failed to send the message
                    // Handle response error
                }
            }
        });
    }

    // Method to receive messages
    public static void receiveMessages(String receiverID) {
        OkHttpClient client = new OkHttpClient();

        // Build the URL with the receiver ID as a query parameter
        HttpUrl url = HttpUrl.parse(BASE_URL).newBuilder()
                .addQueryParameter("receiverID", receiverID)
                .build();

        // Build the request
        Request request = new Request.Builder()
                .url(url)
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
                } else {
                    // Failed to receive messages
                    // Handle response error
                }
            }
        });
    }

    // Example usage
    public static void main(String[] args) {
        // Send a message
        sendMessage("sender123", "receiver456", "Hello, how are you?");

        // Receive messages
        receiveMessages("receiver456");
    }
}
