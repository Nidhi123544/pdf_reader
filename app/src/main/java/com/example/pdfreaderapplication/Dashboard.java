package com.example.pdfreaderapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.List;

public class Dashboard extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener  {
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;
    String TAG = "PdfActivity";
    int position = -1;
    int p = 1;
    EditText editText;
    TextView cancel, ok;
    boolean flag = true;
    boolean paused = true;
    private InterstitialAd interstitialAd;
    Toolbar toolbar;
    int PageCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = findViewById(R.id.Dashboardtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        interstitialAd=new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-1016901275106207/7865101864");
        interstitialAd.loadAd(new AdRequest.Builder().build());
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation1);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.about:
                        Toast.makeText(Dashboard.this, "Add To Bookmark", Toast.LENGTH_SHORT).show();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.dashboard:
                        if(paused){
                            paused=false;
                            item.setIcon(R.mipmap.hori);
                            pdfView.fromFile(MainActivity.fileList.get(position))
                                    .defaultPage(pageNumber)
                                    .swipeHorizontal(true)
                                    .spacing(20)
                                    .load();
                        }else{
                            paused=true;
                            item.setIcon(R.mipmap.vari);
                            pdfView.fromFile(MainActivity.fileList.get(position))
                                    .defaultPage(pageNumber)
                                    .swipeHorizontal(false)
                                    .spacing(20)
                                    .load();
                        }
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.homedash:
                        Dialog dialog = new Dialog(Dashboard.this);
                        dialog.setContentView(R.layout.jumpdialog);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        editText = (EditText) dialog.findViewById(R.id.dialogEdittext);
                        cancel = (TextView) dialog.findViewById(R.id.textviewCancel);
                        ok = (TextView) dialog.findViewById(R.id.textviewOk);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String input = String.valueOf(editText.getText());
                                if (input != null && !input.equals("")) {
                                    p = Integer.valueOf(String.valueOf(editText.getText()));
                                    pageNumber = p - 1;
                                    editText.setText("");
                                    dialog.dismiss();
                                    if(p>PageCount)
                                    {
                                        Toast.makeText(Dashboard.this, "This file contain only " + PageCount + "Page", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        pdfView.fromFile(MainActivity.fileList.get(position))
                                                .defaultPage(pageNumber)
                                                .spacing(20)
                                                .load();
                                       //Toast.makeText(Dashboard.this, "Current Page Number and PageCount = " + p +PageCount , Toast.LENGTH_SHORT).show();
                                        Toast.makeText(Dashboard.this, p+"/"+PageCount, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                        dialog.show();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.newMode:
                        if(paused){
                            paused=false;
                            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
                            item.setIcon(R.drawable.ic_baseline_flare_24);
                            pdfView.fromFile(MainActivity.fileList.get(position))
                                    .defaultPage(pageNumber)
                                    .spacing(20)
                                    .nightMode(true)
                                    .load();
                        }else{
                            paused=true;
                            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
                            item.setIcon(R.drawable.ic_baseline_bedtime_24);
                            pdfView.fromFile(MainActivity.fileList.get(position))
                                    .defaultPage(pageNumber)
                                    .spacing(20)
                                    .nightMode(false)
                                    .load();
                        }
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        init();
    }
    private void init() {
        pdfView = findViewById(R.id.pdfview);
       // position = getIntent().getIntExtra("position", -1);
        position = getFilePathFromName(getIntent().getStringExtra("fileName" ));
        displayFromSdcard();
    }
    private void displayFromSdcard() {

        if (position>=0) {
            pdfFileName = MainActivity.fileList.get(position).getName();
            pdfView.fromFile(MainActivity.fileList.get(position))
                    .defaultPage(pageNumber)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .onPageChange(this)
                    .enableAnnotationRendering(true)
                    .onLoad(this)
                    .spacing(20)
                    .load();
        } else {
            Toast.makeText(Dashboard.this,"Try again",Toast.LENGTH_SHORT).show();
        }


    }

    private int getFilePathFromName(String fileName){
        int result = -1;
        for (int x = 0 ; x <  MainActivity.fileList.size(); x++)
        {
            if(MainActivity.fileList.get(x).getName().equalsIgnoreCase(fileName)){
                result = x;
                break;
            }
        }
        return result;
    }
    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        PageCount=pageCount;
        setTitle(String.format("%s", pdfFileName));
        Toast.makeText(this, page + 1 + "/" + PageCount, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }
    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {
            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));
            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.Share:

                if(interstitialAd.isLoaded()){
                    interstitialAd.show();
                }
                else{
                    Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+MainActivity.fileList.get(position)));
                    startActivity(Intent.createChooser(shareIntent, "Share the file"));
                }
                interstitialAd.setAdListener(new AdListener(){
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        interstitialAd.loadAd(new AdRequest.Builder().build());
                        interstitialAd.show();
                    }
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        interstitialAd.loadAd(new AdRequest.Builder().build());
                        interstitialAd.show();
                    }
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+MainActivity.fileList.get(position)));
                        startActivity(Intent.createChooser(shareIntent, "Share the file"));
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}