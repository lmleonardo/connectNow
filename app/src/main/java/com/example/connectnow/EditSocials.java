package com.example.connectnow;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditSocials extends AppCompatActivity {

    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    String user;
    EditText firstName;
    EditText lastName;
    TextView facebook;
    TextView instagram;
    TextView twitter;
    Button saveBtn;
    SocialCard socialCard;
    Intent intent;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_socials);

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
        firstName = findViewById(R.id.etFirstName);
        lastName = findViewById(R.id.etLastName);
        facebook = findViewById(R.id.etFacebook);
        instagram = findViewById(R.id.etInstagram);
        twitter = findViewById(R.id.etTwitter);

        saveBtn = findViewById(R.id.btnSave);
        DocumentReference cardRef = db.document("accounts/" + user + "/cards/social");

      /*  Bundle extras = intent.getExtras();
        if (extras!=null) {
            if (extras.get("firstName")!= null) { firstName.setText(extras.get("firstName").toString());}
            if (extras.get("lastName")!= null) { lastName.setText(extras.get("lastName").toString());}
            if (extras.get("email")!= null) { email.setText(extras.get("email").toString());}
            if (extras.get("phone")!= null) { phone.setText(extras.get("phone").toString());}
        }*/

        cardRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                assert documentSnapshot != null;

                if (documentSnapshot.exists()) {
                    socialCard = documentSnapshot.toObject(SocialCard.class);
                    assert socialCard != null;
                    firstName.setText(socialCard.getFirstName());
                    lastName.setText(socialCard.getLastName());
                    facebook.setText(socialCard.getFacebook());
                    instagram.setText(socialCard.getInstagram());
                    twitter.setText(socialCard.getTwitter());

                } else { Log.d("Social card", "No document exists"); }


            } else { Log.d("Task", "failed");}
        });


        saveBtn.setOnClickListener(v -> {

            if (firstName.getText().toString().isEmpty()) {
                firstName.setError("Please enter your first name");
                firstName.requestFocus();
            } else if (lastName.getText().toString().isEmpty()) {
                lastName.setError("Please enter your last name");
                lastName.requestFocus();
            } else if (facebook.getText().toString().isEmpty()) {
                facebook.setError("Please enter Facebook URL");
                facebook.requestFocus();
            } else if (instagram.getText().toString().isEmpty()) {
                instagram.setError("Please enter your Instagram URL");
                instagram.requestFocus();
            } else if (twitter.getText().toString().isEmpty()) {
                twitter.setError("Please enter your Twitter URL");
                twitter.requestFocus();
            } else {

                socialCard = new SocialCard();
                socialCard.setFirstName(firstName.getText().toString());
                socialCard.setLastName(lastName.getText().toString());
                socialCard.setFacebook(facebook.getText().toString());
                socialCard.setInstagram(instagram.getText().toString());
                socialCard.setTwitter(twitter.getText().toString());


                db.document("accounts/" + user + "/cards/social").set(socialCard);
                Toast.makeText(this, "Social card successfully updated", Toast.LENGTH_SHORT).show();
                openSocial(user);

            }
        });


    }

    private void openSocial(String user) {
        Intent intent = new Intent(this, Social.class);
        intent.putExtra("user", user);
        intent.putExtra("firstName", socialCard.getFirstName());
        intent.putExtra("lastName", socialCard.getLastName());
        intent.putExtra("facebook", socialCard.getFacebook());
        intent.putExtra("instagram", socialCard.getInstagram());
        intent.putExtra("twitter", socialCard.getTwitter());
        startActivity(intent);
    }
}
