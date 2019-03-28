package com.example.kevzzsk.dengueradar;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class TipsInterface extends Fragment {
    private DummyTip[] tips = new DummyTip[5];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tips_page,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        ImageView imageView = new ImageView(getActivity());
        //TODO control the img
        imageView.setImageResource(R.drawable.empty);
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
                Intent intent = new Intent(getActivity(), TipDetailsInterface.class);
                intent.putExtra("tip", tips[v.getId()]);
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
                Intent intent = new Intent(getActivity(), TipDetailsInterface.class);
                intent.putExtra("tip", tips[v.getId()]);
                startActivity(intent);
            }
        });

        LinearLayout linearLayout = getView().findViewById(R.id.tipsLayout);
        linearLayout.addView(textView);
    }
}
