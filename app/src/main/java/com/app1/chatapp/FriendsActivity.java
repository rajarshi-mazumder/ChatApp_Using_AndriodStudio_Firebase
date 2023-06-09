 package com.app1.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

 public class FriendsActivity extends AppCompatActivity {

     private RecyclerView recyclerView;
     private ArrayList<User> users;
     private ProgressBar progressBar;
     private UsersAdapter usersAdapter;
     UsersAdapter.OnUserClickListener onUserClickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        progressBar= findViewById(R.id.progressBar);
        users= new ArrayList<>();
        recyclerView= findViewById(R.id.recycler);

        onUserClickListener= new UsersAdapter.OnUserClickListener() {
            @Override
            public void onUserClicked(int position) {
                Toast.makeText(FriendsActivity.this, "Tapped on user "+ users.get(position).getUsername(), Toast.LENGTH_SHORT).show();
            }
        };

        getUsers();
    }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
         return true;
     }

     @Override
     public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== R.id.menu_item_profile)
            startActivity(new Intent(FriendsActivity.this, Profile.class));
        return super.onOptionsItemSelected(item);
     }

     private void getUsers(){
         FirebaseDatabase.getInstance().getReference("user").addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 for(DataSnapshot snapshot1: snapshot.getChildren())
                 {
                     users.add(snapshot1.getValue(User.class));
                 }
                 usersAdapter= new UsersAdapter(users, FriendsActivity.this, onUserClickListener);
                 recyclerView.setLayoutManager(new LinearLayoutManager(FriendsActivity.this));
                 recyclerView.setAdapter(usersAdapter);
                 progressBar.setVisibility(View.GONE);
                 recyclerView.setVisibility(View.VISIBLE);

             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
     }
 }