package com.example.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

public class MainActivity extends AppCompatActivity {

    Button Login, Registration;
    FirebaseFirestore db;
    TextView Username;
    EditText password;
    private volatile boolean stopThreadFlag = false;

    private Handler mainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Login = findViewById(R.id.Login_But);
        Registration = findViewById(R.id.RegisterPage);
        db = FirebaseFirestore.getInstance();
        Username = findViewById(R.id.Username);
        password = findViewById(R.id.Password);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usersearch = Username.getText().toString();

                if(!usersearch.isEmpty())
                {
                    SearchUser(usersearch);
                    startThread(2);

                }
            }
        });

        Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterPage.class));
            }
        });

    }
    public void startThread(int seconds)
    {
        for(int i=0; i< seconds; i++)
        {
            Log.d("Thread Activity", "Start Thread : " + i);
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        GraduationThread thread = new GraduationThread(10);
    }
    class GraduationThread extends Thread
    {
        int seconds;
        GraduationThread(int seconds)
        {
            this.seconds = seconds;
        }
        @Override
        public void run(){
            for(int i=0; i< seconds; i++)
            {
                Log.d("THREAD ACTIVITY", "Start Thread: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    public void SearchUser(String Username)
    {

        db.collection("UserAccount")
                .document(Username)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String queriedPassword = documentSnapshot.getString("password");
                        Log.d("Firestore", "Data read from Firestore: " + documentSnapshot.getData());

                        if(queriedPassword == null)
                        {
                            Toast.makeText(MainActivity.this,"User does not exist", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            password.setText(queriedPassword);
                            Toast.makeText(MainActivity.this,"User does exist", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this,HomePage.class));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", "Error reading data from Firestore: " + e.getMessage());
                    }
                });
    }
}