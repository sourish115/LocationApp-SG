package com.example.locationapp;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Locale;

public class LocationAdapter implements LocationListener {
    private Context context;
    private LocationManager locationManager;
    private String loc;

    public String getLoc() {
        return loc;
    }

    public LocationAdapter(Context context, LocationManager locationManager) {
        this.context = context;
        this.locationManager = locationManager;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            String city = getCity(location);
            loc = city;
            //locationManager.removeUpdates(this);
            Log.e("CITY", city);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    public String getCity(Location location){
        Log.e("LOCATION", String.valueOf(location));
        try{
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> currentAddress = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String city = currentAddress.get(0).getLocality();
            return city;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
