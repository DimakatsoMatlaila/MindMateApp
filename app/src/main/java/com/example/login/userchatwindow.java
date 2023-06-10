package com.example.login;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.login.databinding.ActivityUserchatwindowBinding;

import java.util.ArrayList;

public class userchatwindow extends AppCompatActivity {

    static public ArrayList<Message> messages;

    SharedPreferences sharedPreferences;
    ImageView imageView;
    TextView rnameR;
    public String logger;
    public static MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userchatwindow);
        sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        imageView = findViewById(R.id.imgBack);

        rnameR = findViewById(R.id.rNamee);

        String rec = "";
        boolean r = GlobalVariables.getInstance().getVld();
        GlobalVariables.getInstance().setVld(r);

        if (sharedPreferences.getString("userType","").equals("Counsellor")) {
            logger = GlobalVariables.getInstance().getCounsellorname();
            rec = GlobalVariables.getInstance().getUsername();
            rnameR.setText(rec);
        } else{
            logger = GlobalVariables.getInstance().getUsername();
            rec = GlobalVariables.getInstance().getCounsellorname();
            rnameR.setText(rec);
        }
        if(messages==null) {
            messages = new ArrayList<>();
        }
            adapter = new MessageAdapter(this, messages);

            ListView listView = findViewById(R.id.listViews);
            listView.setAdapter(adapter);



        Button sendButton = findViewById(R.id.sendButton);
        String finalLogger = logger;
        String finalLogger1 = logger;
        String finalRec = rec;
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText messageEditText = findViewById(R.id.messageEditText);
                String message = messageEditText.getText().toString().trim();
                if (!message.isEmpty()) {
                    if(message!=null) {
                        sendMessage(finalLogger, message);
                        String EncryptText = Encryption.encrypt(message);
                        ChatClient.sendMessage(finalLogger1, finalRec, EncryptText);
                        //ChatClient.receiveMessages(finalRec,finalLogger1);
                        messageEditText.setText("");
                    }

                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToList();
            }
        });
    }

    static void sendMessage(String sender, String message) {
        if(sender==null || message==null || sender.isEmpty() || message.isEmpty()){
            return;
        }else{
            Message newMessage = new Message(sender, message);
            if(newMessage!=null){
                if(messages == null){
                    messages = new ArrayList<>();
                }
                messages.add(newMessage);
                if(!messages.isEmpty()){
                    if(adapter!=null){
                        if(!(messages.size()==0)){
                            adapter.notifyDataSetChanged();
                        }else{
                            return;
                        }
                    }else{
                        return;
                    }
                }else{
                    return;
                }

            }else{
                return;
            }
        }
    }

    private void receiveMessage(String sender, String message) {
        Message newMessage = new Message(sender, message);
        messages.add(newMessage);
        adapter.notifyDataSetChanged();
    }

    public static void clearChats() {
        adapter.clear();
        adapter.notifyDataSetChanged();
    }

    public void returnToList(){
        Intent intent;
        if(GlobalVariables.getInstance().getVld()==true){
            intent = new Intent(this,counsellorChat.class);
            startActivity(intent);
        }else{
            intent = new Intent(this, userChat.class);
            startActivity(intent);
        }
    }

    public void onBackPressed() {
        returnToList();
        super.onBackPressed();
    }


}
