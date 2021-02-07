package com.example.pdfreaderapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.pdfreaderapplication.entity.HistoryMarkEntity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ironsource.mediationsdk.IronSource;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    static String TAG = "MainActivity";
    ListView lv_pdf;
    public static ArrayList<File> fileList = new ArrayList<>();
    MainActivityAdapter obj_adapter;
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    public File dir;
    Database mydb;
    DatabaseBookmark bookdb;
    SearchView searchView;
    private InterstitialAd mInterstitialAd;
    private int mSlectedFilePosition = 0 ;
   // int pos=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IronSource.setConsent(true);
        Toolbar toolbar = findViewById(R.id.MainActivitytoolbar);
        setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayHomeAsUpEnabled(false);
       getSupportActionBar().setDisplayShowHomeEnabled(false);
       initADS();
        searchView=findViewById(R.id.Mainsearch);
        mydb = new Database(this);
        bookdb=new DatabaseBookmark(this);
        lv_pdf=findViewById(R.id.lv_pdf);
        lv_pdf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSlectedFilePosition = position ;
                if(mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }else{
                    startPdfReader(mSlectedFilePosition);
                }
            }
        });
        searchView.setOnQueryTextListener(this);
        dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath());
       // Log.e(TAG,"Directory files  : " + dir);
       // Log.e(TAG,"Directory files data  : " + dir.listFiles());
        fn_permission();
        BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Home:
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Bookmark:
                        Intent intent=new Intent(MainActivity.this,BookmarkActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.History:
                        Intent intent1=new Intent(MainActivity.this,HistoryActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
    private  void fn_permission(){
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        } else {
            boolean_permission = true;
            deletePos();
            fileList.clear();
            getfile(dir);
            obj_adapter = new MainActivityAdapter(this, fileList);
            lv_pdf.setAdapter(obj_adapter);

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                boolean_permission = true;
                deletePos();
                fileList.clear();
                getfile(dir);
                obj_adapter=new MainActivityAdapter(this,fileList);
                lv_pdf.setAdapter(obj_adapter);

            } else {
                Toast.makeText(this, "Please Allow the Permission", Toast.LENGTH_SHORT).show();
            }
            if(grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Write Permission Allow", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "write permission not allow", Toast.LENGTH_SHORT).show();
            }
        }
    }
   public  static  void getfile(File dir){
        try{
            File listFile []  = dir.listFiles();
            //Log.e(TAG,"list file : " + listFile);
            for(File file:listFile){
               // Log.e(TAG, "Loop Name:"+ file.getName());
                if(file.isDirectory()){
                    getfile(file);
                }
                else {
                   // Log.e(TAG, "Else : Name:"+ file.getName());
                    if(file.getName().toLowerCase().endsWith(".pdf")){
                        fileList.add(file);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
       // obj_adapter.getFilter().filter(query);
        return false;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
       //obj_adapter.getFilter().filter(newText);
        String text=newText;
        obj_adapter.filterData(text);
        return false;
    }

    private void startPdfReader(int position){
        boolean isInseted=mydb.insertData(MainActivity.fileList.get(position).getName());
        if(isInseted==true){
            Toast.makeText(getApplicationContext(), "Pdf Data inserted Successfully", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Pdf Data not inserted Successfully", Toast.LENGTH_SHORT).show();
        }

        boolean isInsertedBookData= bookdb.insertBookData(MainActivity.fileList.get(position).getName());

        if(isInsertedBookData==true)
        {
            //Toast.makeText(getActivity(), "Add to Bookmark", Toast.LENGTH_SHORT).show();
        }
        else {
            // Toast.makeText(getActivity(), "Not Add to Bookmark", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(MainActivity.this,Dashboard.class);
        intent.putExtra("fileName", MainActivity.fileList.get(position).getName());
        startActivity(intent);
    }

    private void initADS(){
        mInterstitialAd=new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1016901275106207/6948424253");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                Log.e("Position", mSlectedFilePosition + "");
                startPdfReader(mSlectedFilePosition);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        IronSource.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }

    public void deletePos(){
       try{
          int pos=getIntent().getIntExtra("INTENTPOS",-1);
           fileList.get(pos).delete();
           obj_adapter.notifyDataSetChanged();
       }catch (Exception e){
           e.printStackTrace();
       }
    }
}