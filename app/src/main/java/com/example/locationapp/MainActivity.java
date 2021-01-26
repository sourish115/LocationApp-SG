package com.example.locationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{
    private String latitude, longitude;
    private LocationManager locationManager;
    private Button locateButton;
    private TextView locationTv;
    NetworkLocation networkLocation;
    GPSLocation gpsLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locateButton = findViewById(R.id.button);
        locationTv = findViewById(R.id.location_textView);
        networkLocation = new NetworkLocation(getApplicationContext());
        gpsLocation = new GPSLocation(getApplicationContext());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }

        //Log.e("Network provider status", String.valueOf(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)));

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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000, 0, gpsLocation);
    }

    @SuppressLint("MissingPermission")
    private void getNetworkLocation(){
        Log.e("NETWORK", "Network location");
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000,10000,networkLocation);
    }

}