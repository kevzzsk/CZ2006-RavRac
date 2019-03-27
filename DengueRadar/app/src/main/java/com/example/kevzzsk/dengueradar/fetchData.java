package com.example.kevzzsk.dengueradar;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.io.File;



public class fetchData extends AsyncTask<Void, Void, Void> {
    String data = "";
    String singleParsed = "";
    public JSONObject dataParsed ;
    String co = "";

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://geo.data.gov.sg/dengue-cluster/2019/03/15/geojson/dengue-cluster.geojson");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONObject obj1 = new JSONObject(data);
            JSONArray features = obj1.getJSONArray("features");
            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = features.getJSONObject(i);
                JSONObject properties = feature.getJSONObject("properties");
                //this get the description
                singleParsed = "Description" + properties.get("Description") + "\n";
                String substr = singleParsed.substring(singleParsed.indexOf("CASE_SIZE") + 19, singleParsed.indexOf("CASE_SIZE") + 100);
                String substr2 = substr.substring(0, substr.indexOf('<'));

                String substradd = singleParsed.substring(singleParsed.indexOf("LOCALITY") + 18, singleParsed.indexOf("LOCALITY") + 400);
                String substradd2 = substradd.substring(0, substradd.indexOf("</td>"));
                obj1.getJSONArray("features").getJSONObject(i).getJSONObject("properties").put("Name", "Cluster: " + Integer.toString(i + 1));
                obj1.getJSONArray("features").getJSONObject(i).getJSONObject("properties").put("Description", substr2);


                // the below part is for the removal of all < and / characters in our case size
                /*int length = substr.length();
                int a, b, count = 0;
                char []substr1 =substr.toCharArray();
                for( a =  b =0; a < length; a++){
                    if ((substr1[a] != '/' )&& (substr1[a] != '<')) {
                        substr1[b++] = substr1[a];
                    }  else {
                        count++;
                    }

                }
                while(count>0){
                    substr1[b++] ='\0';
                    count--;
                }*/
                //substr is the value for cases
                //substr = String.copyValueOf(substr1);


                // lets try to extract the value for location
               /* JSONObject geometry = feature.getJSONObject("geometry");
                JSONArray coordinates = geometry.getJSONArray("coordinates");
                JSONArray coords1 = coordinates.getJSONArray(0);
                JSONArray coords2 = coords1.getJSONArray(0);


                co = coords2.toString();*/



            }

            dataParsed = obj1;


            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public JSONObject getJson(){
        return dataParsed;
    }


}

