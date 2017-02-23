package com.example.wohhi.weatherforcast;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;

/**
 * Created by wohhi on 2/23/2017.
 */
public class GPSTracker extends Service implements LocationListener {

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATEDS = 10;
    private static final long MIN_TIME_BW_UPDATEDS = 1000 * 60 * 1;
    private final Context context;
    protected LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location;
    double lantitude;
    double longtitude;

    public GPSTracker(Context context) {
        this.context = context;
        getLocation();
    }

    private Location getLocation() {

        try{
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (!isGPSEnabled && !isNetworkEnabled){
                this.canGetLocation = false;

            }else{
                this.canGetLocation = true;
                if(isNetworkEnabled){

                    if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
                        return null;
                    }

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATEDS, MIN_DISTANCE_CHANGE_FOR_UPDATEDS, this);
                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if(location != null){
                            lantitude = location.getLatitude();
                            longtitude = location.getLongitude();
                        }
                    }
                }


                if(isGPSEnabled){
                    if (location == null){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATEDS, MIN_DISTANCE_CHANGE_FOR_UPDATEDS, this);

                        if(location != null ){
                            lantitude = location.getLatitude();
                            longtitude = location.getLongitude();
                        }
                    }
                }

            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return location;
    }

    public void stopUsingGPS(){
        if(locationManager != null){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED){
                return;
            }

            locationManager.removeUpdates(GPSTracker.this);
        }
    }


    public double getLantitude() {
        if(location != null){
            lantitude = location.getLatitude();
        }
        return lantitude;
    }

    public double getLongtitude(){
        if(location != null){
            longtitude = location.getLongitude();
        }

        return longtitude;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
