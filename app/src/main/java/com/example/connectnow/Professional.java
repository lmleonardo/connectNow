package com.example.connectnow;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Professional extends AppCompatActivity {

    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    Intent intent;
    String user;
    TextView tvName;
    TextView tvEmail;
    TextView tvPhone;
    TextView tvLinkedIn;
    Button editBtn;
    Button shareBtn;
    ProfessionalCard professionalCard;

    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional);

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
        tvLinkedIn = findViewById(R.id.tvLinkedIn);
        editBtn = findViewById(R.id.btnEdit);
        shareBtn = findViewById(R.id.btnShare);

        DocumentReference cardRef = db.document("accounts/" + user + "/cards/professional");

        cardRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                assert documentSnapshot != null;

                if (documentSnapshot.exists()) {
                    professionalCard = documentSnapshot.toObject(ProfessionalCard.class);
                    assert professionalCard != null;
                    tvName.setText(professionalCard.getFirstName() + " " + professionalCard.getLastName());
                    tvPhone.setText(professionalCard.getPhone());
                    tvEmail.setText(professionalCard.getEmail());
                    tvLinkedIn.setText(professionalCard.getLinkedIn());


                } else { Log.d("Professional card", "No document exists"); }

            } else { Log.d("Task", "failed");}
        });



        editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfessional.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });

        shareBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShareQR.class);
            intent.putExtra("code", "professional");
            intent.putExtra("user", user);
            intent.putExtra("path", cardRef.getPath());
            startActivity(intent);
        });


    }
    }
