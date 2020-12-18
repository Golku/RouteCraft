package com.example.routecraft.features.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.routecraft.R;
import com.example.routecraft.features.MainActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}