package com.example.pdfreaderapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.Hometoolbar);
        setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_navigation1);
        bottomNavigationView.setSelectedItemId(R.id.homedash);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.about:
                        Intent intent1=new Intent(Home.this,About.class);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.dashboard:
                        Intent intent=new Intent(Home.this,Dashboard.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.homedash:
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.newMode:
                        Intent intent2=new Intent(Home.this,Mode.class);
                        startActivity(intent2);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}