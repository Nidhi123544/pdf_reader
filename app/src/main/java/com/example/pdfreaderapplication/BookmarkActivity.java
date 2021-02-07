package com.example.pdfreaderapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.pdfreaderapplication.entity.BookMarkEntity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity  {
    ListView bookmark_list;
    ArrayList<BookMarkEntity> listItem;
    PdfBookmarkAdapter pdfBookmarkAdapter;
    BookmarkDeleteAdapter bookmarkDeleteAdapter;
    DatabaseBookmark db;
    private InterstitialAd mInter;
    boolean paused = true;
    //int pos=0;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        Toolbar toolbar = findViewById(R.id.Bookmarktoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mInter=new InterstitialAd(this);
        mInter.setAdUnitId("ca-app-pub-1016901275106207/4162494721");
        mInter.loadAd(new AdRequest.Builder().build());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mInter.isLoaded()){
                    mInter.show();
                }
                else{
                    Intent intent=new Intent(BookmarkActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                mInter.setAdListener(new AdListener(){
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        mInter.loadAd(new AdRequest.Builder().build());
                        mInter.show();
                    }
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        mInter.loadAd(new AdRequest.Builder().build());
                        mInter.show();
                    }
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        Intent intent=new Intent(BookmarkActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        bookmark_list = (ListView)findViewById(R.id.lv_bookmark_pdf);
        bookmark_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //pos=position;
                Intent intent = new Intent(BookmarkActivity.this, Dashboard.class);
               // intent.putExtra("position", position);
                intent.putExtra("fileName", listItem.get(position).getBookMarkFileName());
                startActivity(intent);
            }
        });
        db = new DatabaseBookmark(this);
        listItem = new ArrayList<>();
        viewAllBookMarkData();
        BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Bookmark);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Home:
                        Intent intent=new Intent(BookmarkActivity.this,MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Bookmark:
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.History:
                        Intent intent1=new Intent(BookmarkActivity.this,HistoryActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
    public void viewAllBookMarkData() {
        listItem .addAll(db.getAllBookdata());
        if (listItem.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No Data Show", Toast.LENGTH_SHORT).show();
        } else {
           /* while (cursor.moveToNext()) {
                listItem.add(cursor.getString(1));
            }*/
            pdfBookmarkAdapter = new PdfBookmarkAdapter(this, listItem);
            bookmark_list.setAdapter(pdfBookmarkAdapter);
            pdfBookmarkAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bookmark_delete_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.bookmark_delete:
                if(paused) {
                    paused = false;
                    item.setTitle("CANCEL");
                    bookmarkDeleteAdapter = new BookmarkDeleteAdapter(this, listItem);
                    bookmark_list.setAdapter(bookmarkDeleteAdapter);
                    bookmarkDeleteAdapter.notifyDataSetChanged();
                }
                else {
                    paused=true;
                    item.setTitle("DELETE");
                    pdfBookmarkAdapter = new PdfBookmarkAdapter(this, listItem);
                    bookmark_list.setAdapter(pdfBookmarkAdapter);
                    pdfBookmarkAdapter.notifyDataSetChanged();
                }
        }
        return super.onOptionsItemSelected(item);
    }
}