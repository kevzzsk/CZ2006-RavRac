package com.example.kevzzsk.dengueradar;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapsManager implements ActivityCompat.OnRequestPermissionsResultCallback, LocationListener {

    private static final String TAG = "MapsManager";
    Activity mContext;

    private static final int LOCATION_UPDATE_FREQUENCY = 5000; //5 s


    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    private boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private List<List> allDengueCluster = new ArrayList<>();
    PendingIntent mRequestLocationUpdatesPendingIntent;
    Intent mRequestLocationUpdatesIntent;
    public BackgroundService gpsService;

    private boolean notificationSent;
    NotificationInterface notificationInterface;
    private  boolean isServicebounded = false;

    public MapsManager(Activity context) {
        mContext = context;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void setNotificationSent(boolean b){
        notificationSent = b;
    }


    public FusedLocationProviderClient getmFusedLocationClient() {
        return mFusedLocationClient;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    //init map

                }
        }
    }

    public ServiceConnection getServiceConnection() {
        return serviceConnection;
    }

    public Intent getmRequestLocationUpdatesIntent() {
        return mRequestLocationUpdatesIntent;
    }

    public boolean isServicebounded() {
        return isServicebounded;
    }


    // SERVICE Connection
    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            String name = className.getClassName();
            if (name.endsWith("BackgroundService")) {
                gpsService = ((BackgroundService.LocationServiceBinder) service).getService();

            }
        }

        public void onServiceDisconnected(ComponentName className) {
            if (className.getClassName().equals("BackgroundService")) {
                gpsService = null;
                isServicebounded=false;
            }
        }
    };


    public void initializeUserLocation() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_UPDATE_FREQUENCY); // interval is in ms
        mLocationRequest.setFastestInterval(LOCATION_UPDATE_FREQUENCY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(mContext);
        settingsClient.checkLocationSettings(locationSettingsRequest);



        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                // create the INTENT to use BackgroundService to handle results
                mRequestLocationUpdatesIntent = new Intent(mContext, BackgroundService.class);
                mContext.startService(mRequestLocationUpdatesIntent);
                mContext.bindService(mRequestLocationUpdatesIntent,serviceConnection,Context.BIND_AUTO_CREATE);
                isServicebounded = true;

                // new Google API SDK v11 uses getFusedLocationProviderClient(this)
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                // do work here
                                onLocationChanged(locationResult.getLastLocation());
                            }
                        },
                        Looper.myLooper());

                //mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }

            }
        else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            //mMap.setMyLocationEnabled(true);
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(mContext)
                        .setTitle("Location Permission Needed")
                        .setMessage("For us to deliver a richer experience, we need your Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(mContext,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        LOCATION_PERMISSION_REQUEST_CODE );
                            }
                        })
                        .create()
                        .show();



            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(mContext,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE );
            }
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

            }
        }
    };


    public GeoJsonLayer getLayer(GoogleMap mMap){
        try {
            GeoJsonLayer layer = new GeoJsonLayer(mMap, R.raw.dengue_cluster,mContext);

            // get all the coordinates of dengue cluster(polygons)
            for (GeoJsonFeature feature : layer.getFeatures()) {
                //Log.d(TAG, "GeoJson: "+ getCoordinates(feature.getGeometry()));
                allDengueCluster.add(MapsInterface.getCoordinates(feature.getGeometry()));
            }
            return layer;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }



    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: Location changed!");
        Toast.makeText(mContext,"Location is now "+ String.valueOf(location.getLatitude())+" "+String.valueOf(location.getLongitude()),Toast.LENGTH_SHORT).show();
        /*circleOptions = new CircleOptions()
                .center(userLoc)
                .radius(1000)
                .fillColor(Color.BLUE);*/
        isDengueNearby(location);
    }

    public boolean isDengueNearby(Location newLocation){
        // check against all the coordinates of dengue cluster
        // calculate distance from user to all center of dengue cluster
        // radius of 1km

        LatLng userLoc = new LatLng(newLocation.getLatitude(),newLocation.getLongitude());

        for(List<LatLng> polygon: allDengueCluster){
            for(LatLng vertex: polygon){
                // returns distance in meter
                if(SphericalUtil.computeDistanceBetween(vertex,userLoc) <= 500){
                    // dengue cluster is within 1km
                    Log.d(TAG, "isDengueNearby: True");
                    Log.d(TAG, "isDengueNearby: " + SphericalUtil.computeDistanceBetween(vertex,userLoc));
                    Toast.makeText(mContext,String.valueOf(SphericalUtil.computeDistanceBetween(vertex,userLoc)),Toast.LENGTH_SHORT).show();
                    if(notificationSent == false) {
                        notificationInterface = new NotificationInterface(mContext);
                        notificationInterface.sendNotification();
                        notificationSent = true;
                    }
                    return true;
                }

            }
        }
        Log.d(TAG, "isDengueNearby: False");
        notificationSent = false;
        return false;
    }

    public void stopLocationUpdate(){
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    // will return resource ID
    public int getGeoJson(){
        // geojson will be polled from firebase and stored locally.
        // reduce internet usage

        return R.raw.dengue_cluster;
    }


}