package com.example.pdfreaderapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Mode extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        Toolbar toolbar = findViewById(R.id.Modetoolbar);
        setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_navigation1);
        bottomNavigationView.setSelectedItemId(R.id.newMode);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.about:
                        Intent intent1=new Intent(Mode.this,About.class);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.dashboard:
                        Intent intent3=new Intent(Mode.this,Dashboard.class);
                        startActivity(intent3);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.homedash:
                        Intent intent=new Intent(Mode.this,Home.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.newMode:
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}