package com.example.connectnow;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfessional extends AppCompatActivity {

    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    String user;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText phone;
    EditText linkedin;
    Button saveBtn;
    ProfessionalCard professionalCard;
    Intent intent;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_professional);

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
        email = findViewById(R.id.etEmail);
        phone = findViewById(R.id.etPhone);
        linkedin = findViewById(R.id.etLinkedIn);
        saveBtn = findViewById(R.id.btnSave);
        professionalCard = new ProfessionalCard();
        DocumentReference cardRef = db.document("accounts/" + user + "/cards/professional");

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

                    professionalCard = documentSnapshot.toObject(ProfessionalCard.class);
                    assert professionalCard != null;
                    firstName.setText(professionalCard.getFirstName());
                    lastName.setText(professionalCard.getLastName());
                    phone.setText(professionalCard.getPhone());
                    email.setText(professionalCard.getEmail());
                    linkedin.setText(professionalCard.getLinkedIn());

                } else { Log.d("Professional card", "No document exists"); }

            } else { Log.d("Task", "failed");}
        });


        saveBtn.setOnClickListener(v -> {

            if (firstName.getText().toString().isEmpty()) {
                firstName.setError("Please enter your first name");
                firstName.requestFocus();
            } else if (lastName.getText().toString().isEmpty()) {
                lastName.setError("Please enter your last name");
                lastName.requestFocus();
            } else if (email.getText().toString().isEmpty()) {
                email.setError("Please enter your email address");
                email.requestFocus();
            } else if (phone.getText().toString().isEmpty()) {
                phone.setError("Please enter your phone number");
                phone.requestFocus();
            } else if (linkedin.getText().toString().isEmpty()) {
                linkedin.setError("Please enter your phone number");
                linkedin.requestFocus();
            } else {
                professionalCard.setFirstName(firstName.getText().toString());
                professionalCard.setLastName(lastName.getText().toString());
                professionalCard.setEmail(email.getText().toString());
                professionalCard.setPhone(phone.getText().toString());
                professionalCard.setLinkedIn(linkedin.getText().toString());

                db.document("accounts/" + user + "/cards/professional").set(professionalCard);
                Toast.makeText(this, "Professional card successfully updated", Toast.LENGTH_SHORT).show();
                openProfessional(user);

            }
        });

        /*firstName.setOnClickListener(v -> { firstName.setText(""); });
        lastName.setOnClickListener(v -> { lastName.setText(""); });
        email.setOnClickListener(v -> { email.setText(""); });
        phone.setOnClickListener(v -> { phone.setText(""); });*/

    }

    private void openProfessional(String user) {
        Intent intent = new Intent(this, Personal.class);
        intent.putExtra("user", user);
        intent.putExtra("firstName", professionalCard.getFirstName());
        intent.putExtra("lastName", professionalCard.getLastName());
        intent.putExtra("email", professionalCard.getEmail());
        intent.putExtra("phone", professionalCard.getPhone());
        intent.putExtra("linkedin", professionalCard.getPhone());

        startActivity(intent);
    }

}
