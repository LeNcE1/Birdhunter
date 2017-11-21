package com.example.lence.bird_hunter.ui;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.lence.bird_hunter.R;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class Map implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    Context mContext;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    double gpsmyX=0.0;
    double gpsmyY=0.0;
    private GoogleApiClient client;
    private boolean mFirst = true;
    SupportMapFragment mapFragment;
    MapInterface mMapInterface;

    public Map(Context context, SupportMapFragment mapFragment,MapInterface mMapInterface) {
        mContext = context;
        this.mapFragment = mapFragment;
        this.mMapInterface = mMapInterface;
        mapFragment.getMapAsync(this);
        client = new GoogleApiClient.Builder(mContext).addApi(AppIndex.API).build();
    }


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        //Log.e(TAG, mLastLocation.getLatitude() + " " + mLastLocation.getLongitude());


        gpsmyX=mLastLocation.getLatitude();
        gpsmyY=mLastLocation.getLongitude();
        //loc.setText(String.format("%.4f",gpsmyX)+"\n"+String.format("%.4f",gpsmyY));
           Log.e("GPS message ", gpsmyX + " " + gpsmyY);

        mMapInterface.onLocationChanged(gpsmyX,gpsmyY);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setBuildingsEnabled(false);
        if (ActivityCompat.checkSelfPermission(mContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mGoogleApiClient == null) {
            //Log.e(TAG, "googleapi");
            createLocationRequest();
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }

        mMap.setMyLocationEnabled(true);
    }
    private void createLocationRequest() {
        //Log.e(TAG, "createLocationRequest");

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        setIntervalTime(100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }
    private void setIntervalTime(int time) {
        mLocationRequest.setFastestInterval(time);
    }
    private void startLocationUpdates() {
        //Log.e(TAG, "startLocationUpdates");

        if (ActivityCompat.checkSelfPermission(mContext,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);
    }
    private void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            //   stopLocationUpdates();
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
