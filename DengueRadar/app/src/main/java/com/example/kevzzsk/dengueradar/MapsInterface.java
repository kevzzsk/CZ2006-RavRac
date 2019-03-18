package com.example.kevzzsk.dengueradar;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsInterface extends AppCompatActivity implements OnMapReadyCallback,NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MapsInterface";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    private GoogleMap mMap;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;



    public boolean isServicesOK(){
        Log.d(TAG,"isServicesOK: Checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapsInterface.this);

        if (available == ConnectionResult.SUCCESS){
            // everything is fine
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            // an error occur but can be resolved
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");

            Dialog dialog =GoogleApiAvailability.getInstance().getErrorDialog(MapsInterface.this,available,ERROR_DIALOG_REQUEST);
            ((Dialog) dialog).show();

        }else{
            Toast.makeText(this, "you cannot make map request",Toast.LENGTH_SHORT).show();
        }
        return false;

    }


    private void getLocationPermission(){
        String[] permission = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            }else {
                ActivityCompat.requestPermissions(this,permission,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else {
            ActivityCompat.requestPermissions(this,permission,LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0){
                    for (int i=0 ; i<grantResults.length; i++){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    //init map

                }
        }
    }

    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsInterface.this);
    }

    private DrawerLayout drawer;

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    // make menuItem clicks become active
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_map:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                     new MapsInterface2() ).commit();
                break;
            case R.id.nav_statistics:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new StatisticInterface() ).commit();
                break;
            case R.id.nav_tips:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TipsInterface() ).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setContentView(R.layout.activity_maps_interface);

        drawer = findViewById(R.id.drawerLayout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this, drawer,
                toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        Log.d(TAG, "onCreate: drawer message!"+ String.valueOf(drawer == null) );
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //set MapInterface to be default start up page
        // savedInstanceState ensures that rotating device will not switch back to default page
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MapsInterface2()).commit();
            navigationView.setCheckedItem(R.id.nav_map);
        }



        getLocationPermission();
        Log.d(TAG, "onCreate: Permission granted");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsInterface.this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sg = new LatLng(1.3521, 103.8198);
        mMap.addMarker(new MarkerOptions().position(sg).title("Marker in Singapore"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sg));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }
    // api key AIzaSyC023WrXGzzcNAY3yoAXj1iT2cRnEypGr8
}
