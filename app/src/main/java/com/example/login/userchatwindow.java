package com.example.login;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class userchatwindow extends AppCompatActivity {

    private ArrayList<Message> messages;
    private MessageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userchatwindow);
        String sname = GlobalVariables.getInstance().getUsername();
        String rname = GlobalVariables.getInstance().getCounsellorname();
        String logger = "";
        boolean r = GlobalVariables.getInstance().getVld();
        if(r==true){
            logger =  GlobalVariables.getInstance().getCounsellorname();
        } else if (r==false) {
            logger=GlobalVariables.getInstance().getUsername();

        }

        messages = new ArrayList<>();
        adapter = new MessageAdapter(this, messages);

        ListView listView = findViewById(R.id.listViews);
        listView.setAdapter(adapter);

        Button sendButton = findViewById(R.id.sendButton);
        String finalLogger = logger;
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText messageEditText = findViewById(R.id.messageEditText);
                String message = messageEditText.getText().toString().trim();
                if (!message.isEmpty()) {
                    sendMessage(finalLogger, message);
                    ChatClient.sendMessage(sname,rname,message);
                    messageEditText.setText("");

                }
            }
        });



    }

    private void sendMessage(String sender, String message) {
        Message newMessage = new Message(sender, message);
        messages.add(newMessage);
        adapter.notifyDataSetChanged();
    }

    private void receiveMessage(String sender, String message) {
        Message newMessage = new Message(sender, message);
        messages.add(newMessage);
        adapter.notifyDataSetChanged();
    }
}
