package com.example.locationapp;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Locale;

public class NetworkLocation implements LocationListener {
    Context context;

    public NetworkLocation(Context context) {
        this.context = context;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> currentAddress = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String city = currentAddress.get(0).getLocality();
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
}
