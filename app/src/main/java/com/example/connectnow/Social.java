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

public class Social extends AppCompatActivity {

    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    Intent intent;
    String user;
    TextView tvName;
    TextView tvFacebook;
    TextView tvInstagram;
    TextView tvTwitter;

    Button editBtn;
    Button shareBtn;
    SocialCard socialCard;

    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

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
        tvFacebook = findViewById(R.id.tvFacebook);
        tvInstagram = findViewById(R.id.tvInstagram);
        tvTwitter = findViewById(R.id.tvTwitter);
        editBtn = findViewById(R.id.btnEdit);
        shareBtn = findViewById(R.id.btnShare);

        DocumentReference cardRef = db.document("accounts/" + user + "/cards/social");

        cardRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                assert documentSnapshot != null;

                if (documentSnapshot.exists()) { socialCard = documentSnapshot.toObject(SocialCard.class); }
                else { Log.d("Personal card", "No document exists"); }

                assert socialCard != null;
                tvName.setText(socialCard.getFirstName() + " " + socialCard.getLastName());
                tvFacebook.setText(socialCard.getFacebook());
                tvInstagram.setText(socialCard.getInstagram());
                tvTwitter.setText(socialCard.getTwitter());

            } else { Log.d("Task", "failed");}
        });


        editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditSocials.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });

        shareBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShareQR.class);
            intent.putExtra("code", "social");
            intent.putExtra("user", user);
            intent.putExtra("path", cardRef.getPath());
            startActivity(intent);
        });


    }

    }



