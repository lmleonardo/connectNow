package com.example.connectnow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    public Button personalCardBtn;
    public Button professionalCardBtn;
    public Button socialsCardBtn;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    String user = "user";
    PersonalCard personalCard;
    ProfessionalCard professionalCard;
    SocialCard socialCard;
    String firstName;
    String lastName;
    String email;
    String phone;
    String linkedin;
    String facebook;
    String instagram;
    String twitter;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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




        personalCardBtn = findViewById(R.id.btnPersonal);
        professionalCardBtn = findViewById(R.id.btnProfessional);
        socialsCardBtn = findViewById(R.id.btnSocial);


        personalCardBtn.setOnClickListener(v -> {

                    DocumentReference docRef = db.document("accounts/" + user + "/cards/personal");
                    docRef.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            assert document != null;
                            if (document.exists()) {
                                personalCard = document.toObject(PersonalCard.class);
                                assert personalCard != null;
                                firstName = (personalCard.getFirstName());
                                lastName = (personalCard.getLastName());
                                phone = (personalCard.getPhone());
                                email = (personalCard.getEmail());
                                openNext(1);
                                Log.d("Personal Card", "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d("Personal Card", "No such document");
                            }
                        } else {
                            Log.d("Personal Card", "get failed with ", task.getException());
                        }
                    });

        });

        professionalCardBtn.setOnClickListener(v -> db.document("accounts/user/cards/professional").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                assert documentSnapshot != null;

                if (documentSnapshot.exists()) {
                    professionalCard = documentSnapshot.toObject(ProfessionalCard.class);
                    assert professionalCard != null;
                    firstName = (professionalCard.getFirstName());
                    lastName = (professionalCard.getLastName());
                    phone = (professionalCard.getPhone());
                    email = (professionalCard.getEmail());
                    linkedin = (professionalCard.getLinkedIn());

                    openNext(3);

                } else {
                    Log.d("Professional card", "No document exists");
                    openNext(4);
                }

            } else { Log.d("Task", "failed");}
        }));


        socialsCardBtn.setOnClickListener(v -> {
            db.document("accounts/user/cards/social").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    assert documentSnapshot != null;

                    if (documentSnapshot.exists()) {
                        socialCard = documentSnapshot.toObject(SocialCard.class);
                        assert socialCard != null;
                        firstName = (socialCard.getFirstName());
                        lastName = (socialCard.getLastName());
                        facebook = (socialCard.getFacebook());
                        instagram = (socialCard.getInstagram());
                        twitter = (socialCard.getTwitter());

                        openNext(5);

                    } else {
                        Log.d("Social card", "No document exists");
                        openNext(6);
                    }

                } else { Log.d("Task", "failed");}
            });
        });

    }

    private void openNext(int num) {

        Intent intent = null;
        if (num == 1) { intent = new Intent(this, Personal.class); }
        else if (num == 2) { intent = new Intent(this, EditPersonal.class); }
        else if (num == 3) { intent = new Intent(this, Professional.class); }
        else if (num == 4) { intent = new Intent(this, EditProfessional.class); }
        else if (num == 5) { intent = new Intent(this, Social.class); }
        else if (num == 6) { intent = new Intent(this, EditSocials.class); }

        assert intent != null;
        intent.putExtra("user", user);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        intent.putExtra("email", email);
        intent.putExtra("phone", phone);
        intent.putExtra("linkedin", linkedin);
        intent.putExtra("facebook", facebook);
        intent.putExtra("instagram", instagram);
        intent.putExtra("twitter", twitter);

        startActivity(intent);
    }
}
