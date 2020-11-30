package com.example.ccep;



import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class SetsActivity extends AppCompatActivity {

    private GridView gridView;
    private InterstitialAd mInterstitialAd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);
        Toolbar toolbars=findViewById(R.id.toolbars);

        loadAds();
        gridView=findViewById(R.id.gridview);

        GridAdapter adapter=new GridAdapter(getIntent().getIntExtra("sets",0),getIntent().getStringExtra("title"),mInterstitialAd);

        gridView.setAdapter(adapter);


        setSupportActionBar(toolbars);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));




    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    private void loadAds(){
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitialAd_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


    }
}
