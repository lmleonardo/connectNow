package com.example.connectnow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditPersonal extends AppCompatActivity {


    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    String user;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText phone;
    Button saveBtn;
    PersonalCard personalCard;
    Intent intent;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal);

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
        saveBtn = findViewById(R.id.btnSave);
        personalCard = new PersonalCard();
        DocumentReference cardRef = db.document("accounts/" + user + "/cards/personal");

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

                if (documentSnapshot.exists()) { personalCard = documentSnapshot.toObject(PersonalCard.class); }
                else { Log.d("Personal card", "No document exists"); }

                assert personalCard != null;
                firstName.setText(personalCard.getFirstName());
                lastName.setText(personalCard.getLastName());
                phone.setText(personalCard.getPhone());
                email.setText(personalCard.getEmail());

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
            } else {
                personalCard.setFirstName(firstName.getText().toString());
                personalCard.setLastName(lastName.getText().toString());
                personalCard.setEmail(email.getText().toString());
                personalCard.setPhone(phone.getText().toString());

                db.document("accounts/" + user + "/cards/personal").set(personalCard);
                Toast.makeText(this, "Personal card successfully updated", Toast.LENGTH_SHORT).show();
                openPersonal(user);

            }
        });

        /*firstName.setOnClickListener(v -> { firstName.setText(""); });
        lastName.setOnClickListener(v -> { lastName.setText(""); });
        email.setOnClickListener(v -> { email.setText(""); });
        phone.setOnClickListener(v -> { phone.setText(""); });*/

    }

    private void openPersonal(String user) {
        Intent intent = new Intent(this, Personal.class);
        intent.putExtra("user", user);
        intent.putExtra("firstName", personalCard.getFirstName());
        intent.putExtra("lastName", personalCard.getLastName());
        intent.putExtra("email", personalCard.getEmail());
        intent.putExtra("phone", personalCard.getPhone());
        startActivity(intent);
    }
}