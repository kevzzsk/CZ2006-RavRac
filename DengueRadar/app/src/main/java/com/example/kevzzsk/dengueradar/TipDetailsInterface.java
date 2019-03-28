package com.example.kevzzsk.dengueradar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


public class TipDetailsInterface extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipdetail);
        Tip tip = (Tip)getIntent().getSerializableExtra("tip");
        displayHeading(tip.heading);
        String content = tip.content.replace("\\n", "\n")+"\n\n\n";
        content = content.replace("\n ", "\n");
        displayContent(content);

        //test
        Log.d("content", tip.content.replace('\\', '\\'));
        displayImage(tip.img);
    }

    public void displayHeading(String heading){
        TextView tv = findViewById(R.id.topHeading);
        //Log.d("debug", heading);
        tv.setText(heading);
    }

    public void displayContent(String content){
        TextView tv = findViewById(R.id.tipDetail);
        tv.setText(content);
    }

    public void displayImage(String url){
        ImageView iv = findViewById(R.id.imageView);
        new DownloadImageTask(iv).execute(url);
    }
}
