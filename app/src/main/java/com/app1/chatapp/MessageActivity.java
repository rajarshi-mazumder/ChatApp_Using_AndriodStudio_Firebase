package com.app1.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText edtMessageInput;
    private TextView txtChattingWith;
    private ProgressBar progressBar;
    private ImageView imgToolbar, sendMessage;

    private ArrayList<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        recyclerView= findViewById(R.id.recyclerMessages);
        imgToolbar= findViewById(R.id.img_toolbar);
        sendMessage= findViewById(R.id.imgSendMessage);
        edtMessageInput= findViewById(R.id.edtText);
        txtChattingWith= findViewById(R.id.txtChattingWith);
        progressBar= findViewById(R.id.progressMessages);
    }
}