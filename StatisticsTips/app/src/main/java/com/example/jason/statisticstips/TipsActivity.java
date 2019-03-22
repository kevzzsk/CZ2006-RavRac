package com.example.jason.statisticstips;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class TipsActivity extends AppCompatActivity {
    private DummyTip[] tips = new DummyTip[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        displayTips();
    }

    public void displayTips(){
        for(int i=0; i<5; i++){
            tips[i] = new DummyTip("Heading"+i);
            createImageView(i);
            createTextView(tips[i].heading, i);
        }
    }

    public void createImageView(int id){
        ImageView imageView = new ImageView(TipsActivity.this);
        imageView.setImageResource(R.drawable.picture1);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setClickable(true);
        imageView.setFocusable(true);
        imageView.setId(id);
//        imageView.setMaxHeight(50);
//        imageView.setMinimumHeight(50);
//        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imageView.getLayoutParams();
//        lp.setMargins(0, 50, 0, 0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 600);
        params.setMargins(5,5,5,5);
        imageView.setLayoutParams(params);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("debug", ""+v.getId());
                Intent intent = new Intent(TipsActivity.this, TipDetailsActivity.class);
                intent.putExtra("tip", tips[v.getId()]);
                startActivity(intent);
            }
        });

        LinearLayout linearLayout = findViewById(R.id.tipsLayout);
        linearLayout.addView(imageView);
    }

    public void createTextView(String heading, int id){
        TextView textView = new TextView(TipsActivity.this);
        textView.setText(heading);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setTextSize(COMPLEX_UNIT_SP, 50);
        textView.setId(id);

//        textView.setMaxHeight(100);
//        textView.setMinimumHeight(300);
//        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) textView.getLayoutParams();
//        lp.setMargins(0, 0, 0, 0);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(5,5,5,200);
        textView.setLayoutParams(params);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("debug", ""+v.getId());
                Intent intent = new Intent(TipsActivity.this, TipDetailsActivity.class);
                intent.putExtra("tip", tips[v.getId()]);
                startActivity(intent);
            }
        });

        LinearLayout linearLayout = findViewById(R.id.tipsLayout);
        linearLayout.addView(textView);
    }


}
