package com.example.connectnow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Personal extends AppCompatActivity {

    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    Intent intent;
    String user;
    TextView tvName;
    TextView tvEmail;
    TextView tvPhone;
    Button editBtn;
    Button shareBtn;
    PersonalCard personalCard;

    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        // Bottom Navigation
        BottomNavigationView bottomNavigation = findViewById(R.id.navigationView);
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.btnHome:
                    Intent homeIntent = new Intent(this, MainActivity.class);
                    homeIntent.putExtra("user", user);
                    startActivity(homeIntent);
                    break;
                case R.id.btnScan:
                    Intent scanIntent = new Intent(this, Scanner.class);
                    scanIntent.putExtra("user", user);
                    startActivity(scanIntent);
                    break;
                case R.id.btnContacts:
                    Intent contactIntent = new Intent(this, Contacts.class);
                    contactIntent.putExtra("user", user);
                    startActivity(contactIntent);
                    break;
            }
            return true;
        });

        intent = getIntent();
        user = intent.getStringExtra("user");
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        editBtn = findViewById(R.id.btnEdit);
        shareBtn = findViewById(R.id.btnShare);

        DocumentReference cardRef = db.document("accounts/" + user + "/cards/personal");

        cardRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                assert documentSnapshot != null;

                if (documentSnapshot.exists()) {
                    personalCard = documentSnapshot.toObject(PersonalCard.class);
                    assert personalCard != null;
                    tvName.setText(personalCard.getFirstName() + " " + personalCard.getLastName());
                    tvPhone.setText(personalCard.getPhone());
                    tvEmail.setText(personalCard.getEmail());

                } else { Log.d("Personal card", "No document exists"); }

            } else { Log.d("Task", "failed");}
        });



        editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditPersonal.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });

        shareBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShareQR.class);
            intent.putExtra("code", "personal");
            intent.putExtra("user", user);
            intent.putExtra("path", cardRef.getPath());
            startActivity(intent);
        });


    }

 }
