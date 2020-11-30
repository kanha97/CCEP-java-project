package com.example.ccep;



import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;

import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuestionsActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private TextView question,noIndicator;
    private ImageView bookmarkbtn;
    private LinearLayout optionsContainer;

    private Button share,next;
    private int position=0;
    private int score=0;

    private String category;
    private int setNo;
private Dialog loadingDialog;
    private List<QuestionModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Toolbar qtoolbar=findViewById(R.id.toolbar);

        loadAds();
        setActionBar(qtoolbar);
        question=findViewById(R.id.question);
        noIndicator=findViewById(R.id.no_indicator);
        optionsContainer=findViewById(R.id.question_container);
        share=findViewById(R.id.share);
        next=findViewById(R.id.next);


        category=getIntent().getStringExtra("category");
        setNo=getIntent().getIntExtra("setNo",1);

        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        list=new ArrayList<>();

        loadingDialog.show();
        myRef.child("SETS").child(category).child("questions").orderByChild("setNo").equalTo(setNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    list.add(dataSnapshot.getValue(QuestionModel.class));
                }

                if (list.size() > 0){


                    for (int i=0;i<4;i++){
                        optionsContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                checkAnswer((Button) v);

                            }
                        });
                    }
                    playAnim(question,0,list.get(position).getQuestion());

                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            next.setEnabled(false);
                            next.setAlpha(0.7f);
                            enableOption(true);

                            position++;
                            if (position == list.size()){

                                Intent scoreIntent = new Intent(QuestionsActivity.this,ScoreActivity.class);
                                scoreIntent.putExtra("score",score);
                                scoreIntent.putExtra("total",list.size());
                                startActivity(scoreIntent);
                                finish();
                                return;
                            }


                            count=0;
                            playAnim(question,0,list.get(position).getQuestion());
                        }
                    });
                    share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String body =list.get(position).getQuestion() + "\n\n" +
                                    list.get(position).getOptionA() + "\n\n" +
                                    list.get(position).getOptionB() + "\n\n" +
                                    list.get(position).getOptionC() + "\n\n" +
                                    list.get(position).getOptionD();
                            Intent shareIntent= new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT,"CCEP Quesion");
                            shareIntent.putExtra(Intent.EXTRA_TEXT,body);
                            startActivity(Intent.createChooser(shareIntent,"Share Via"));
                        }
                    });
                }else{
                    finish();
                    Toast.makeText(QuestionsActivity.this, "No Questions", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(QuestionsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                finish();
            }
        });


    }

    private int count=0;

    private void playAnim(final View view, final int value, final String data){

        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if(value==0 && count<4 ){

                    String option="";
                    if (count==0){

                        option=list.get(position).getOptionA();
                    }else if (count==1){
                        option=list.get(position).getOptionB();
                    }else if (count==2){
                        option=list.get(position).getOptionC();
                    }else if (count==3){
                        option=list.get(position).getOptionD();

                    }

                    playAnim(optionsContainer.getChildAt(count),0,option);
                    count++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {

                if (value == 0){
                    try{
                        ((TextView)view).setText(data);

                        noIndicator.setText(position+1+"/"+list.size());
                    }catch (ClassCastException ex){

                        ((Button)view).setText(data);
                    }

                    view.setTag(data);

                    playAnim(view,1,data);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
    private void checkAnswer(Button selectedOption){

        enableOption(false);
        next.setEnabled(true);
        next.setAlpha(1);
        if (selectedOption.getText().toString().equals(list.get(position).getCorrectans())){
            ////correct
            score++;
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff9e29")));
        }else {
            ///incorrect
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));

            Button correctOption= (Button)optionsContainer.findViewWithTag(list.get(position).getCorrectans());
            correctOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff9e29")));
        }
    }
    private void enableOption(boolean enable){
        for (int i=0;i<4;i++){
            optionsContainer.getChildAt(i).setEnabled(enable);

            if (enable){
                optionsContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));

            }
        }
    }

    private void loadAds(){
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
