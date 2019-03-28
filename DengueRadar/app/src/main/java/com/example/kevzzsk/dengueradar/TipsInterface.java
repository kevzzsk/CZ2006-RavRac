package com.example.kevzzsk.dengueradar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class TipsInterface extends Fragment {
    private ArrayList<Tip> tips = new ArrayList<Tip>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tips_page,container,false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startLoadingTips();
        displayTips(tips);
    }

    public void displayTips(ArrayList<Tip> tips){
        for(int i=0; i<tips.size(); i++){
            //test
            Log.d("image", tips.get(i).img);
            createImageView(tips.get(i).img, i);
            createTextView(tips.get(i).heading, i);
        }
    }

    public void startLoadingTips(){
        new DatabaseViewer(0, this);
    }


    public void finishLoadingTips(ArrayList<Tip> tips){
        this.tips = tips;
        displayTips(tips);
    }

    public void createImageView(String url, int id){
        ImageView imageView = new ImageView(getActivity());
        //set image from url
        new DownloadImageTask(imageView).execute(url);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setClickable(true);
        imageView.setFocusable(true);
        imageView.setId(id);
//        imageView.setId(id);
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
                Intent intent = new Intent(getActivity(), TipDetailsInterface.class);
                intent.putExtra("tip", tips.get(v.getId()));
                startActivity(intent);
            }
        });

        LinearLayout linearLayout = getView().findViewById(R.id.tipsLayout);
        linearLayout.addView(imageView);
    }

    public void createTextView(String heading, int id){
        TextView textView = new TextView(getActivity());
        textView.setText(heading);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setTextSize(COMPLEX_UNIT_SP, 30);
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
                Intent intent = new Intent(getActivity(), TipDetailsInterface.class);
                intent.putExtra("tip", tips.get(v.getId()));
                startActivity(intent);
            }
        });

        LinearLayout linearLayout = getView().findViewById(R.id.tipsLayout);
        linearLayout.addView(textView);
    }
}
