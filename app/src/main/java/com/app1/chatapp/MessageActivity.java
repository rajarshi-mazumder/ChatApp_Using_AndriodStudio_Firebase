package com.app1.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText edtMessageInput;
    private TextView txtChattingWith;
    private ProgressBar progressBar;
    private ImageView imgToolbar, sendMessage;

    private ArrayList<Message> messages;

    private MessageAdapter messageAdapter;
    String username_of_roommate, email_of_roommate, chatRoomId;

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

        username_of_roommate= getIntent().getStringExtra("name_of_roommate");
        email_of_roommate= getIntent().getStringExtra("email_of_roommate");

        messages= new ArrayList<>();

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("messages/" +chatRoomId).push().setValue(new Message(
                        FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                        email_of_roommate,
                        edtMessageInput.getText().toString()));

                edtMessageInput.setText("");
            }
        });
        messageAdapter= new MessageAdapter(messages, getIntent().getStringExtra("my_img"),
                            getIntent().getStringExtra("img_of_roommate"),
                            MessageActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        Glide.with(MessageActivity.this).load(getIntent().getStringExtra("img_of_roommate"))
                        .placeholder(R.drawable.account_img).error(R.drawable.account_img)
                        .into(imgToolbar);
        SetUpChatroom();

    }

    private void SetUpChatroom()
    {
        FirebaseDatabase.getInstance().getReference("user/"+ FirebaseAuth.getInstance().getUid()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String myUsename= snapshot.getValue(User.class).getUsername();
                        if(username_of_roommate.compareTo(myUsename)>0)
                        {
                            chatRoomId=myUsename+username_of_roommate;
                        }
                        else if(username_of_roommate.compareTo(myUsename)==0)
                        {
                            chatRoomId= myUsename+ username_of_roommate;
                        }
                        else chatRoomId= username_of_roommate+myUsename;

                        attachMessageListener(chatRoomId);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void attachMessageListener(String chatRoomId)
    {
        Log.d("Custom_log", "attachMessageListener called");
        FirebaseDatabase.getInstance().getReference("/messages/"+ chatRoomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("Custom_log", snapshot+"onDataChange called");
                for(DataSnapshot snapshot1: snapshot.getChildren())
                {
                    messages.add(snapshot1.getValue(Message.class));
                    Log.d("Custom_log", snapshot1.getValue(Message.class).getContent().toString());
                }

                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size()-1);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}