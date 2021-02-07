package com.example.pdfreaderapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.pdfreaderapplication.entity.HistoryMarkEntity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity  {
    ListView history_list;
    ArrayList<HistoryMarkEntity> listItem;
    PdfHistoryAdapter pdfHistoryAdapter;
    HistoryDeleteAdapter historyDeleteAdapter;
    Database db;
    public static TextView selectall,Delete;
    boolean isSelectAll = true;
    private InterstitialAd hInter;
    ArrayList<String> list;
    boolean paused = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = findViewById(R.id.Historytoolbar);
        setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setDisplayShowHomeEnabled(true);
        hInter=new InterstitialAd(this);
        hInter.setAdUnitId("ca-app-pub-1016901275106207/4162494721");
        hInter.loadAd(new AdRequest.Builder().build());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(hInter.isLoaded()){
                   hInter.show();
               }
               else{
                   Intent intent=new Intent(HistoryActivity.this,MainActivity.class);
                   startActivity(intent);
               }
               hInter.setAdListener(new AdListener(){
                   @Override
                   public void onAdFailedToLoad(LoadAdError loadAdError) {
                       super.onAdFailedToLoad(loadAdError);
                       hInter.loadAd(new AdRequest.Builder().build());
                       hInter.show();
                   }
                   @Override
                   public void onAdLoaded() {
                       super.onAdLoaded();
                       hInter.loadAd(new AdRequest.Builder().build());
                       hInter.show();
                   }
                   @Override
                   public void onAdClosed() {
                       super.onAdClosed();
                       Intent intent=new Intent(HistoryActivity.this,MainActivity.class);
                       startActivity(intent);
                   }
               });
            }
        });
        history_list=findViewById(R.id.history_listview);
        db=new Database(this);
        history_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HistoryActivity.this, Dashboard.class);
                intent.putExtra("fileName", listItem.get(position).getHistoryMarkFileName());
                //intent.putExtra("position", position);
                startActivity(intent);
            }
        });
        selectall=findViewById(R.id.selecAll);
        Delete=findViewById(R.id.deleteAll);
        selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               for(int i=0;i<history_list.getChildCount();i++){
                   LinearLayout itemLayout=(LinearLayout)history_list.getChildAt(i);
                   CheckBox cb=(CheckBox)itemLayout.findViewById(R.id.h_historycheckbox);
                   cb.setChecked(true);
               }
            }
        });
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert=new AlertDialog.Builder(v.getContext())
                        .setTitle("Deletion Confirmation")
                        .setMessage("Are you sure you want to delete selected item..? ")
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                HistoryMarkEntity hs= new HistoryMarkEntity();
//                                hs.getListData();
                        for(int i=0;i<listItem.size();i++) {
                            if (hs.getListData().contains(listItem.get(i).getHistoryMarkFileName())) {
                                listItem.remove(i);
                                pdfHistoryAdapter.notifyDataSetChanged();
                            }
                        }
//                                for(int j=0; j < listItem.size(); j++){
//                                    for (int i=0;i<hs.getListData().size();i++){
//
//                                    }
//                                    listItem.removeAll();
//                                    pdfHistoryAdapter.notifyDataSetChanged();
//                            }

                       /*for(int i=0;i<history_list.getCount();i++){
                            LinearLayout itemLayout=(LinearLayout)history_list.getChildAt(i);
                           CheckBox cb=(CheckBox)itemLayout.findViewById(R.id.h_historycheckbox);
                            if(cb.isChecked()){
                                //listItem.remove(listItem.get(i));
                               // pdfHistoryAdapter.notifyDataSetChanged();
                            }
                        }*/
                       // final int size=history_list.getChildCount();
                        /*for(int i=0;i<history_list.getChildCount();i++){
                            LinearLayout itemLayout=(LinearLayout)history_list.getChildAt(i);
                            CheckBox cb=(CheckBox)itemLayout.findViewById(R.id.historycheckbox);
                            if(cb.isChecked()){
                                listItem.remove(pdfHistoryAdapter.all_pdf.remove(i));
                                pdfHistoryAdapter.notifyDataSetChanged();
                            }
                        }*/
                    }
                });
                AlertDialog a=alert.create();
                a.show();
                Button positive=a.getButton(DialogInterface.BUTTON_POSITIVE);
                positive.setTextColor(Color.BLACK);
                Button negative=a.getButton(DialogInterface.BUTTON_NEGATIVE);
                negative.setTextColor(Color.BLACK);
            }
        });
        db=new Database(this);
        listItem=new ArrayList<>();
        viewAllData();
        BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.History);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Home:
                        Intent intent=new Intent(HistoryActivity.this,MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Bookmark:
                        Intent intent1=new Intent(HistoryActivity.this,BookmarkActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.History:
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
    public  void viewAllData(){
       listItem.addAll(db.getAllHistorydata());
        if(listItem.isEmpty()){
            Toast.makeText(getApplicationContext(), "No Data Show", Toast.LENGTH_SHORT).show();
        }
        else {
           /* while (cursor.moveToNext()){
                listItem.add(cursor.getString(1));
            }*/
        }
        selectall.setVisibility(View.INVISIBLE);
        Delete.setVisibility(View.INVISIBLE);
        pdfHistoryAdapter =new PdfHistoryAdapter(this,listItem);
        history_list.setAdapter(pdfHistoryAdapter);
        pdfHistoryAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_delete_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.history_delete:
                if(paused){
                    paused=false;
                    item.setTitle("CANCEL");
                    selectall.setVisibility(View.VISIBLE);
                    Delete.setVisibility(View.VISIBLE);
                    historyDeleteAdapter = new HistoryDeleteAdapter(this, listItem);
                    history_list.setAdapter(historyDeleteAdapter);
                    historyDeleteAdapter.notifyDataSetChanged();
                }else {
                    paused=true;
                    item.setTitle("DELETE");
                    pdfHistoryAdapter =new PdfHistoryAdapter(this,listItem);
                    history_list.setAdapter(pdfHistoryAdapter);
                    pdfHistoryAdapter.notifyDataSetChanged();
                    selectall.setVisibility(View.INVISIBLE);
                    Delete.setVisibility(View.INVISIBLE);
                }

        }
        return super.onOptionsItemSelected(item);
    }
}