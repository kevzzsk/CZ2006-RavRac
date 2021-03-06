package com.example.kevzzsk.dengueradar;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPolygon;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MapsInterface extends Fragment implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback, GoogleMap.OnInfoWindowCloseListener {
    // use getActivity() to replace this as the context
    // This is due to MapsInterface being just a fragment, hence context
    // need to be taken from the "parent" (MenuInterface)

    GoogleMap mMap;
    MapsManager manager;

    private static final String TAG = "MapsInterface";

    private static final LatLng sg = new LatLng(1.3521, 103.8198);


    public MapsInterface() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_fragment, container, false);
        manager = new MapsManager(getActivity());
        manager.initializeUserLocation();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // INIT MAP
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        displaySearchBar();

    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {


        mMap = googleMap;
        // Add a marker in Singapore and move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sg));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnInfoWindowCloseListener(this);
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity()));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            mMap.setMyLocationEnabled(true);
        }
        // set recenter button to bottom right of the map
        alignRecenterButton();

        // apply a geojson layer onto the map
        // project dengue cluster onto the map
        createGeoJsonLayer();
    }


    private void alignRecenterButton(){

        View locationButton = ((View) getActivity().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 0, 250);
    }



    public void createGeoJsonLayer(){
        // ADD GeoJSON Layer
            GeoJsonLayer layer = manager.getLayer(mMap);
            GeoJsonPolygonStyle polygonStyle = layer.getDefaultPolygonStyle();
            polygonStyle.setFillColor(ContextCompat.getColor(getActivity(),R.color.colorAccentTransparent));
            polygonStyle.setStrokeColor(Color.parseColor("#80000000"));
            layer.addLayerToMap();

            layer.setOnFeatureClickListener(new GeoJsonLayer.GeoJsonOnFeatureClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onFeatureClick(Feature feature) {

                    List<LatLng> coord = getCoordinates(feature.getGeometry());
                    LatLng polygonCenter = getPolygonCenterPoint(coord);

                    Marker info = mMap.addMarker(new MarkerOptions()
                            .position(polygonCenter)
                            .visible(true)
                            .icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(42,42,
                                    Bitmap.Config.ARGB_8888,true)))
                            .title(feature.getProperty("Name"))
                            .snippet(feature.getProperty("Description")));
                    info.showInfoWindow();
                    //Zooms into the feature clicked
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(polygonCenter,14));
                }
            });


    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.hideInfoWindow();
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        if(marker!=null) {
            marker.remove();
        }
    }

    // get all coordinates of the vertex of a polygon
    protected static List<LatLng> getCoordinates(Geometry geometry) {

        List<LatLng> coordinates = new ArrayList<>();

        // GeoJSON geometry types:
        // http://geojson.org/geojson-spec.html#geometry-objects

        switch (geometry.getGeometryType()) {

            case "Polygon":
                List<? extends List<LatLng>> lists =
                        ((GeoJsonPolygon) geometry).getCoordinates();
                for (List<LatLng> list : lists) {
                    coordinates.addAll(list);
                }
                break;

        }

        return coordinates;
    }

    // get an approximation center point of a polygon
    protected static LatLng getPolygonCenterPoint(List<LatLng> polygonPointsList){
        LatLng centerLatLng = null;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i = 0 ; i < polygonPointsList.size() ; i++)
        {
            builder.include(polygonPointsList.get(i));
        }
        LatLngBounds bounds = builder.build();
        centerLatLng =  bounds.getCenter();

        return centerLatLng;
    }



    public void displaySearchBar(){
        // search_Bar
        if (!Places.isInitialized()) {
            Places.initialize(getActivity(), "AIzaSyC023WrXGzzcNAY3yoAXj1iT2cRnEypGr8");
        }

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.search_bar);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));

        autocompleteFragment.setCountry("sg"); //restrict search to SG

        autocompleteFragment.getView().setBackgroundColor(Color.parseColor("#45091F"));
        EditText search_input = autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input);
        search_input.setTextColor(Color.WHITE);



        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(),12));
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                Snackbar.make(getView(),"Error Occurred",Snackbar.LENGTH_SHORT);
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // check if service is bounded
        if (manager.isServicebounded()) {
            // unbind and stop service after
            getActivity().unbindService(manager.getServiceConnection());
            getActivity().stopService(manager.getmRequestLocationUpdatesIntent());
            Log.d(TAG, "onDestroy: ServiceConnection destroyed!");
        }
    }

    // convert SVG to Bitmap for Gmap icon
    // used to hide marker by applying empty icon
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.empty);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth() + 0, vectorDrawable.getIntrinsicHeight() + 0);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


}
