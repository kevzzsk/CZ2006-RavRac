package com.example.kevzzsk.dengueradar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mcontext;

    public CustomInfoWindowAdapter(Context context) {
        mcontext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window,null);
    }

    private void renderWindowText (Marker marker,View view){

        String title = marker.getTitle();
        TextView cusTitle = (TextView) view.findViewById(R.id.title);
        if(!title.equals("")){
            cusTitle.setText(title);
        }

        String snippet = marker.getSnippet();
        TextView cusSnippet = (TextView) view.findViewById(R.id.nCases);
        if(!snippet.equals("")){
            cusSnippet.setText(snippet);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker,mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker,mWindow);
        return mWindow;
    }
}
