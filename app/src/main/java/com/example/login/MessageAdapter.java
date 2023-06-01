package com.example.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<Message> {

    public MessageAdapter(Context context, ArrayList<Message> messages) {
        super(context, 0, messages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
        }

        Message message = getItem(position);

        TextView senderTextView = view.findViewById(R.id.senderTextView);
        TextView messageTextView = view.findViewById(R.id.messageTextView);

        senderTextView.setText(message.getSender());
        messageTextView.setText(message.getMessage());

        return view;
    }
}

