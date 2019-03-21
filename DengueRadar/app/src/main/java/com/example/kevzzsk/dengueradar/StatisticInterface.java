package com.example.kevzzsk.dengueradar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;


import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StatisticInterface extends Fragment {

    private LineGraphSeries<DataPoint> series;
    private int[] monthlyData = createDummyData(STATE.MONTHLY);
    private int[] weeklyData = createDummyData(STATE.WEEKLY);
    private enum STATE{
        WEEKLY, MONTHLY
    }
    private static STATE state;



    public void switchSelected(boolean isChecked){
        if(!isChecked){
            setStateToWeekly();
            drawGraph(weeklyData);
        }
        else{
            setStateToMonthly();
            drawGraph(monthlyData);
        }
    }

    private void setStateToMonthly(){
        state = STATE.MONTHLY;
        TextView textViewWeekly = getView().findViewById(R.id.textViewWeekly);
        textViewWeekly.setTextColor(getResources().getColor(R.color.textDefault));
        TextView textViewMonthly = getView().findViewById(R.id.textViewMonthly);
        textViewMonthly.setTextColor(getResources().getColor(R.color.textSelected));
    }

    private void setStateToWeekly(){
        state = STATE.WEEKLY;
        TextView textViewWeekly = getView().findViewById(R.id.textViewWeekly);
        textViewWeekly.setTextColor(getResources().getColor(R.color.textSelected));
        TextView textViewMonthly = getView().findViewById(R.id.textViewMonthly);
        textViewMonthly.setTextColor(getResources().getColor(R.color.textDefault));
    }

    public void displayText(int noOfCases, int x, STATE state){
        displayNoOfCases(noOfCases);
        displayDate(x, state);
    }

    private void displayDate(int x, STATE state){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
        TextView textView = getView().findViewById(R.id.textViewDate);
        String text = "";
        if(state == STATE.WEEKLY){
            Date currentDate = new Date();
            // convert date to calendar
            Calendar c = Calendar.getInstance();
            c.setTime(currentDate);
            c.add(Calendar.DATE, -7 * (52-x+1));
            Date startDate = c.getTime();
            c.add(Calendar.DATE, 6);
            Date endDate = c.getTime();
            text = dateFormat.format(startDate) + " to " + dateFormat.format(endDate);
        }
        else{
            Date currentDate = new Date();
            // convert date to calendar
            Calendar c = Calendar.getInstance();
            c.setTime(currentDate);
            c.add(Calendar.MONTH, -(12-x));
            Date date = c.getTime();
            //debug
            Log.d("debug", ""+date.getMonth());
            switch(date.getMonth()){
                case 0:
                    text = "Jan";
                    break;
                case 1:
                    text = "Feb";
                    break;
                case 2:
                    text = "Mar";
                    break;
                case 3:
                    text = "Apr";
                    break;
                case 4:
                    text = "May";
                    break;
                case 5:
                    text = "Jun";
                    break;
                case 6:
                    text = "Jul";
                    break;
                case 7:
                    text = "Aug";
                    break;
                case 8:
                    text = "Sep";
                    break;
                case 9:
                    text = "Oct";
                    break;
                case 10:
                    text = "Nov";
                    break;
                case 11:
                    text = "Dec";
                    break;
            }
        }
        textView.setText(text);
        if(state == STATE.MONTHLY){
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        }
        else{
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        }
    }

    private void displayNoOfCases(int noOfCases){
        TextView textView = getView().findViewById(R.id.textViewNum);
        String text = "";
        if(noOfCases < 10){
            text+=" ";
        }
        text += noOfCases;
        textView.setText(text);
    }

    private int[] createDummyData(STATE state){
        int size;
        if(state == STATE.MONTHLY){
            size = 12;
        }
        else{
            size = 52;
        }
        //create dummy data
        int[] dummyData = new int[size];
        for(int i=0; i<size; i++){
            dummyData[i] = (int)(Math.random() * 16);
        }
        return dummyData;
    }

    public int getMax(int[] array){
        int max = array[0];
        for(int i=1; i<array.length; i++){
            if(array[i] > max){
                max = array[i];
            }
        }
        return max;
    }

    public void initializeGraph(){
        //create graph object
        GraphView graph = getView().findViewById(R.id.graph);

        //set padding
        graph.getGridLabelRenderer().setPadding(30);

        //enable scaling and scrolling
        //graph.getViewport().setScalable(true);

        //set on switch listener
        Switch mySwitch = getView().findViewById(R.id.switch1);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                switchSelected(isChecked);
            }

        });
    }

    public void drawGraph(int[] data){
        int maxY, maxX, minY, minX;

        //clear old graph if any
        GraphView graph = getView().findViewById(R.id.graph);
        graph.removeAllSeries();

        minY = 0;
        maxY = getMax(data) + 1;
        minX = 0;
        maxX = data.length + 1;

        //control view port
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(minX);
        graph.getViewport().setMaxX(maxX);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(minY);
        graph.getViewport().setMaxY(maxY);

        if(state == STATE.WEEKLY){
            graph.getGridLabelRenderer().setNumHorizontalLabels(maxX/5);
        }
        else{
            graph.getGridLabelRenderer().setNumHorizontalLabels(maxX);
        }

        graph.getGridLabelRenderer().setNumVerticalLabels(maxY);

        //create data series
        series = new LineGraphSeries<>();

        //set on tap listener
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
//            //debug
//            public void onTap(Series series, DataPointInterface dataPoint) {
//                Log.d("debug", "onTap: " + (int)dataPoint.getX() + " " + (int)dataPoint.getY());
//            }
            public void onTap(Series series, DataPointInterface dataPoint){
                displayText((int)dataPoint.getY(), (int)dataPoint.getX(), StatisticInterface.state); //MainActivity.state
            }
        });

        //set draw data points
        series.setDrawDataPoints(true);

        //put data into graph point series
        for(int i=0; i<data.length; i++){
            series.appendData(new DataPoint(i+1, data[i]), true, data.length);
        }

        graph.addSeries(series);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.statistic_page,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //initialize switch
        Switch mySwitch = getView().findViewById(R.id.switch1);
        mySwitch.setChecked(true);

        setStateToMonthly();
        initializeGraph();
        drawGraph(monthlyData);
        displayText(monthlyData[monthlyData.length-1], monthlyData.length, state);
    }
}
