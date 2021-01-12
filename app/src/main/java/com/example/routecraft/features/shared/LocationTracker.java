package com.example.routecraft.features.shared;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Looper;

import com.example.routecraft.data.pojos.LatLng;
import com.example.routecraft.features.main.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static androidx.core.content.ContextCompat.checkSelfPermission;

public class LocationTracker {

    private final String DEBUG_TAG = "DEBUG_TAG";

    private Context context;
    private Listener listener;

    public interface Listener {
        void onUserLocation(LatLng userLocation);
    }

    public LocationTracker(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    public boolean hasLocationPermission(){
        return checkSelfPermission(context, ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    public void getUserLocation(){
        if (checkSelfPermission(context, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationManager locationManager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        FusedLocationProviderClient client =
                LocationServices.getFusedLocationProviderClient(context);

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

            client.getLastLocation().addOnSuccessListener((MainActivity) context, location -> {
                if (location != null) {
                    listener.onUserLocation(new LatLng(location.getLatitude(), location.getLongitude()));
                } else {
                    LocationRequest locationRequest = new LocationRequest()
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                            .setInterval(10000)
                            .setFastestInterval(1000)
                            .setNumUpdates(1);
                    LocationCallback locationCallback = new LocationCallback(){
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            if(locationResult != null){
                                listener.onUserLocation(new LatLng(locationResult.getLastLocation()
                                        .getLatitude(), locationResult.getLastLocation().getLongitude()));
                            }
                        }
                    };
                    client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                }
            });
        }
    }

//    private void getUserAddress(LatLng userLocation){
//        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocation(userLocation.latitude, userLocation.longitude, 1);
//            callback.updateUserLocation(addresses.get(0).getAddressLine(0), userLocation);
//        }catch (IndexOutOfBoundsException | IOException e){
//            Log.d(debugTag, "LocationManager line 58 exception: " + e.getMessage());
//        }
//    }
}
