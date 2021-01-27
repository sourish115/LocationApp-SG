package com.example.locationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{
    private String latitude, longitude;
    private LocationManager locationManager;
    private Location location;
    private Button locateButton;
    private TextView locationTv;
    LocationAdapter networkLocation;
    LocationAdapter gpsLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locateButton = findViewById(R.id.button);
        locationTv = findViewById(R.id.location_textView);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsLocation = new LocationAdapter(getApplicationContext(),locationManager);
        networkLocation = new LocationAdapter(getApplicationContext(),locationManager);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }

        locateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && isNetworkAvailable()){
                    getNetworkLocation();
                }
                else{
                    getGpsLocation();
                }
            }
        });
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] nwInfos = connectivityManager.getAllNetworkInfo();
        boolean status=false;
        for (NetworkInfo nwInfo : nwInfos) {
            status = status || nwInfo.isConnected();
        }
        return status;
    }

    @SuppressLint("MissingPermission")
    private void getGpsLocation(){
        Log.e("GPS", "GPS location");
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location!=null){
            locationTv.setText(gpsLocation.getCity(location));
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0, 0, gpsLocation);
            locationTv.setText(gpsLocation.getLoc());
        }

    }

    @SuppressLint("MissingPermission")
    private void getNetworkLocation(){
        Log.e("NETWORK", "Network location");
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location!=null){
            locationTv.setText(networkLocation.getCity(location));
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000,10000,networkLocation);
            locationTv.setText(networkLocation.getLoc());
        }
    }

}