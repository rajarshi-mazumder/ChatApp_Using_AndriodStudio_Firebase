package com.app1.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText edtUsername, edtEmail, edtPassword;
    private Button btnSubmit;
    private TextView txtLoginInfo;

    private boolean isSigningUp= true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail= findViewById(R.id.edtEmail);
        edtUsername= findViewById(R.id.edtusername);
        edtPassword= findViewById(R.id.edtPassword);

        btnSubmit= findViewById(R.id.btnSubmit);
        txtLoginInfo= findViewById(R.id.txtLoginInfo);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            startActivity(new Intent(MainActivity.this, FriendsActivity.class));
            finish();
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtEmail.getText().toString().isEmpty()|| edtPassword.getText().toString().isEmpty())
                {
                    if(edtUsername.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Some fields are empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(isSigningUp)
                {
                    handleSignUp();
                }
                else {
                    handleLogIn();
                }
            }
        });

        txtLoginInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(isSigningUp)
                {
                    isSigningUp=false;
                    edtUsername.setVisibility(View.GONE);
                    btnSubmit.setText("Log in");
                    txtLoginInfo.setText("Don't have an account? SIgn up!");
                }
                else {
                    isSigningUp= true;
                    btnSubmit.setText("Sign Up");
                    edtUsername.setVisibility(View.VISIBLE);
                    txtLoginInfo.setText("Already have an account? Log in!");
                }
            }
        });
    }

    private void handleSignUp()
    {
//        FirebaseAuth.getInstance().createUserWithEmailAndPassword(edtEmail.getText().toString(),
//                edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful())
//                    Toast.makeText(MainActivity.this, "Created user successfully", Toast.LENGTH_SHORT).show();
//                else Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        edtEmail.getText().toString(),
                        edtPassword.getText().toString())
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        FirebaseDatabase.getInstance("https://chatapp-28c7c-default-rtdb.firebaseio.com/").getReference("user/"+ FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(new User(edtUsername.getText().toString(),
                                edtEmail.getText().toString(), ""));
                        startActivity(new Intent(MainActivity.this, FriendsActivity.class));
                        Toast.makeText(MainActivity.this, "Created user successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void handleLogIn()
    {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(edtEmail.getText().toString(),
                edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>()  {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(MainActivity.this, FriendsActivity.class));
                    Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}