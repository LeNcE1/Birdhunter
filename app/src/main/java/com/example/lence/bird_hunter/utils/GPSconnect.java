package com.example.lence.bird_hunter.utils;


import android.content.Context;
import android.location.LocationManager;
import android.widget.Toast;

public abstract class GPSconnect {

    public static Boolean execute(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(context, "Включите передачу геоданных", Toast.LENGTH_SHORT).show();
            return false;
        } else return true;

    }
}
