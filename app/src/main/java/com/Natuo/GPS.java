package com.Natuo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class GPS {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private AppCompatActivity MainAcitivity;
    private TextView Latitude, Longitude;
    private double latitudeValue, longitudeVale;


    public GPS() {
    }

    public GPS(AppCompatActivity MainActivity, TextView textView_Latitude, TextView  textView_Longitude) {
        this.MainAcitivity = MainActivity;
        Latitude = textView_Latitude;
        Longitude =  textView_Longitude;
    }

    public void getGPS() {
        LocationManager LM = (LocationManager) MainAcitivity.getSystemService(MainAcitivity.LOCATION_SERVICE);
        boolean OK = LM.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (OK) {

            getLocation();
        } else {
            Toast.makeText(MainAcitivity, "未檢測到GPS", Toast.LENGTH_SHORT).show();
        }
    }

    protected void getLocation() {
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) MainAcitivity.getSystemService(serviceName);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String provider = locationManager.getBestProvider(criteria, true);

        if (ContextCompat.checkSelfPermission(MainAcitivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainAcitivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, mLocationListener01);
    }

    private void updateLocation(Location location){
        if(location != null){

            longitudeVale = location.getLongitude();
            latitudeValue = location.getLatitude();
            Longitude.setText(""+longitudeVale);
            Latitude.setText(""+latitudeValue);
        }
    }

    public double getLatitudeValue(){
        return  latitudeValue;
    }

    public  double getLongitudeVale(){
        return longitudeVale;
    }


    public final LocationListener mLocationListener01 = new LocationListener(){

        @Override
        public void onLocationChanged(Location location) {
            updateLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {
            updateLocation(null);
        }
    };
}

