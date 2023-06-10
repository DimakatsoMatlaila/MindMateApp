package com.example.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.login.databinding.ActivityUserchatwindowBinding;

import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<Message> {

    public MessageAdapter(Context context, ArrayList<Message> messages) {
        super(context, 0, messages);
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        Message message = null;
        if (!userchatwindow.messages.isEmpty()) {
            message = getItem(position);
            view = convertView;
            TextView messageTextView;
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_container_sent_message, parent, false);
                messageTextView = view.findViewById(R.id.messageTextView);
                if (GlobalVariables.getInstance().getVld() == true) {
                    if (GlobalVariables.getInstance().getCounsellorname().equals(message.getSender())) {
                        view = LayoutInflater.from(getContext()).inflate(R.layout.item_container_sent_message, parent, false);
                        messageTextView = view.findViewById(R.id.textMessage);
                    } else {
                        view = LayoutInflater.from(getContext()).inflate(R.layout.item_container_received_message, parent, false);
                        messageTextView = view.findViewById(R.id.textMessageR);
                    }
                } else {
                    if (GlobalVariables.getInstance().getUsername().equals(message.getSender())) {
                        view = LayoutInflater.from(getContext()).inflate(R.layout.item_container_sent_message, parent, false);
                        messageTextView = view.findViewById(R.id.textMessage);
                    } else {
                        view = LayoutInflater.from(getContext()).inflate(R.layout.item_container_received_message, parent, false);
                        messageTextView = view.findViewById(R.id.textMessageR);
                    }
                }
            } else {
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_container_sent_message, parent, false);
                messageTextView = view.findViewById(R.id.messageTextView);
                if (GlobalVariables.getInstance().getVld() == true) {
                    if (GlobalVariables.getInstance().getCounsellorname().equals(message.getSender())) {
                        view = LayoutInflater.from(getContext()).inflate(R.layout.item_container_sent_message, parent, false);
                        messageTextView = view.findViewById(R.id.textMessage);
                    } else {
                        view = LayoutInflater.from(getContext()).inflate(R.layout.item_container_received_message, parent, false);
                        messageTextView = view.findViewById(R.id.textMessageR);
                    }
                } else {
                    if (GlobalVariables.getInstance().getUsername().equals(message.getSender())) {
                        view = LayoutInflater.from(getContext()).inflate(R.layout.item_container_sent_message, parent, false);
                        messageTextView = view.findViewById(R.id.textMessage);
                    } else {
                        view = LayoutInflater.from(getContext()).inflate(R.layout.item_container_received_message, parent, false);
                        messageTextView = view.findViewById(R.id.textMessageR);
                    }
                }
            }

            try {
                messageTextView.setText(message.getMessage());
            } catch (NullPointerException e) {
                throw new RuntimeException(e);
            }

            return view;
        } else {
            // Handle the case when userchatwindow.messages is empty or null
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
            // Customize the view or show an appropriate message to indicate no messages are available
        }

        return view;
    }
}
