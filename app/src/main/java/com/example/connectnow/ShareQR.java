// reference for qr code: https://www.geeksforgeeks.org/how-to-generate-qr-code-in-android/

package com.example.connectnow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.api.Context;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ShareQR extends AppCompatActivity {

    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    Intent intent;
    String user;
    String docPath;
    String code;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_q_r);

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
        ImageView imgQRCode = findViewById(R.id.imgQRCode);
        Button backBtn = findViewById(R.id.backBtn);
        user = intent.getStringExtra("user");
        code = intent.getStringExtra("code");
        docPath = intent.getStringExtra("path");

        qrgEncoder = new QRGEncoder(docPath, null, QRGContents.Type.TEXT, 100);

        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            imgQRCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.e("Tag", e.toString());
        }

        backBtn.setOnClickListener(v -> {

            openPrevious(code);


        });

    }

    private void openPrevious(String code) {

        Intent backIntent = null;

         switch (code) {
                case "personal": { backIntent = new Intent(this, Personal.class); break; }
                case "professional": { backIntent = new Intent(this, Professional.class); break; }
                case "social": { backIntent = new Intent(this, Social.class); break; }
         }

        //Intent backIntent = new Intent(this, Personal.class);
        assert backIntent != null;
        backIntent.putExtra("user", user);
        startActivity(backIntent);
    }


}
