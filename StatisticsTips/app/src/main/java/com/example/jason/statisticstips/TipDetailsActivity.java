package com.example.jason.statisticstips;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class TipDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipdetail);
        DummyTip tip = (DummyTip)getIntent().getSerializableExtra("tip");
        displayHeading(tip.heading);
        displayContent(tip.content);
        displayImage();
    }

    public void displayHeading(String heading){
        TextView tv = findViewById(R.id.topHeading);
        Log.d("debug", heading);
        tv.setText(heading);
    }

    public void displayContent(String content){
        TextView tv = findViewById(R.id.tipDetail);
        tv.setText(content);
    }

    public void displayImage(){
        ImageView iv = findViewById(R.id.imageView);
        iv.setImageResource(R.drawable.picture1);
    }
}
