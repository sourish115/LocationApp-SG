package com.example.locationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity{
    private String latitude, longitude;
    private LocationManager locationManager;
    private Location location;
    private Button locateButton;
    private TextView locationTv;
    private LocationAdapter networkLocation;
    private LocationAdapter gpsLocation;
    private ViewPager viewPager;
    private List<SlideModel> slideList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locateButton = findViewById(R.id.button);
        locationTv = findViewById(R.id.location_textView);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsLocation = new LocationAdapter(getApplicationContext(),locationManager);
        networkLocation = new LocationAdapter(getApplicationContext(),locationManager);
        viewPager = findViewById(R.id.page_slider);

        slideList = new ArrayList<>();
        slideList.add(new SlideModel(R.drawable.wall_one));
        slideList.add(new SlideModel(R.drawable.wall_two));
        slideList.add(new SlideModel(R.drawable.wall_three));

        SliderAdapter sliderAdapter = new SliderAdapter(getApplicationContext(),slideList);
        viewPager.setAdapter(sliderAdapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MainActivity.SliderTimer(),4000,6000);


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

    class SliderTimer extends TimerTask {
        @Override
        public void run() {

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem()<slideList.size()-1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                    }
                    else
                        viewPager.setCurrentItem(0);
                }
            });
        }
    }

}