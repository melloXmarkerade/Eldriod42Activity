package com.example.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterPage extends AppCompatActivity {

    Button RecordBut, LoginPage;
    FirebaseFirestore db;
    TextView UsernameIn, PasswordIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        db = FirebaseFirestore.getInstance();
        RecordBut = findViewById(R.id.RecordBut);
        LoginPage = findViewById(R.id.LoginPage);
        UsernameIn = findViewById(R.id.Username);
        PasswordIn = findViewById(R.id.Password);


        LoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterPage.this,MainActivity.class));
            }
        });

        RecordBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username = UsernameIn.getText().toString();
                String Pass = PasswordIn.getText().toString();

                if (!Username.isEmpty() && !Pass.isEmpty()) {
                    AddUser(Username, Pass);
                } else {
                    Toast.makeText(RegisterPage.this, "Please Double Check the creditial", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void AddUser(String UsernameIn, String PasswordIn)
    {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put(" ", UsernameIn);
        user.put(" ", PasswordIn);

        // Add a new document with a generated ID
        db.collection("UserAccount").document(UsernameIn)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + UsernameIn);
                        Toast.makeText(RegisterPage.this,"Successfully Added " + UsernameIn, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterPage.this, "Error adding user " + e, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Error adding document", e);
                    }
                });
    }

}