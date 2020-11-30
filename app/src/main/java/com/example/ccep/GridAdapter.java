package com.example.ccep;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;


public class GridAdapter extends BaseAdapter {
    private int sets=0;

    private String category;
    private InterstitialAd interstitialAd;
    public GridAdapter(int sets, String category, InterstitialAd interstitialAd) {
        this.sets = sets;
        this.category = category;
        this.interstitialAd =interstitialAd;

    }

    @Override
    public int getCount() {
        return sets;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View convertview, final ViewGroup parent) {

        View view1;


        if (convertview   ==   null){
            view1= LayoutInflater.from(parent.getContext()).inflate(R.layout.set_item,parent,false);

        }else{
            view1=convertview;
        }

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interstitialAd.setAdListener(new AdListener(){
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        interstitialAd.loadAd(new AdRequest.Builder().build());


                        Intent questionintent=new Intent(parent.getContext(),QuestionsActivity.class);
                        questionintent.putExtra("category",category);
                        questionintent.putExtra("setNo",i+1);
                        parent.getContext().startActivity(questionintent);
                    }
                });
                if (interstitialAd.isLoaded()){
                    interstitialAd.show();
                    return;
                }

                Intent questionintent=new Intent(parent.getContext(),QuestionsActivity.class);
                questionintent.putExtra("category",category);
                questionintent.putExtra("setNo",i+1);
                parent.getContext().startActivity(questionintent);
            }
        });

        ((TextView)view1.findViewById(R.id.textview)).setText(String.valueOf(i+1));

        return view1;
    }
}
