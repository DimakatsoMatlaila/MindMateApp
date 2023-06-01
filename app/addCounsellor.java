import android.util.Log;
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

import java.io.IOException;

public class addCounsellor {
    OkHttpClient client = new OkHttpClient();
    String email = "o@gmail.com"; // Replace with the desired email address
    String username = "user400"; // Replace with the desired username
    String password = "abc123"; // Replace with the desired password
    String securityAnswer = "coke"; // Replace with the desired security answer
    String specialty = "depression";
    String url = "https://lamp.ms.wits.ac.za/home/s2555500/createcounsellor.php";
                Log.d(TAG, "URL: " + url); // Debug: Log the URL to verify it's correct

    // Create the request body with the form parameters
    RequestBody requestBody = new FormBody.Builder()
            .add("email", email)
            .add("username", username)
            .add("password", password)
            .add("security_answer", securityAnswer)
            .add("specialty",specialty)
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
                    }
                });
            } else {
                Log.e(TAG, "Response not successful. Code: " + response.code()); // Debug: Log the response code
            }
        }
    });
}
