package com.efpro.bengkelmotor_01.Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.efpro.bengkelmotor_01.Bengkel;
import com.efpro.bengkelmotor_01.Fragment.SplashFragment;
import com.efpro.bengkelmotor_01.Haversine;
import com.efpro.bengkelmotor_01.PermissionUtils;
import com.efpro.bengkelmotor_01.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        LocationListener {


    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "";
    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;
    private Double radius = 0.1;

    private DatabaseReference mBengkelRef;
    private ArrayList<Bengkel> bengkels = new ArrayList<>();

    //FloatingActionButton fab;
    Double latitude, longitude;
    Location location;
    LocationManager locationManager;

    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    // flag for GPS status
    boolean canGetLocation = false;
    // flag for Fragment Status
    private boolean fragFlag;
    // flag for called enableLocation
    private boolean someFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(this);

        //Set firebase database
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mBengkelRef = FirebaseDatabase.getInstance().getReference("ListBengkel");
        mBengkelRef.keepSynced(true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // getting network status
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        enableMyLocation();

    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.fab:
//                if (fragFlag) {
//                    MapFragment mapFragment = new MapFragment();
//                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                    ft.replace(R.id.mainLayout, mapFragment).commit();
//                    fab.setImageResource(R.drawable.gb1);
//                    fragFlag = false;
//                } else {
//                    BengkelFragment bengkelFragment = new BengkelFragment();
//                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                    ft.replace(R.id.mainLayout, bengkelFragment).commit();
//                    fab.setImageResource(R.drawable.gb2);
//                    fragFlag = true;
//                }
//            break;
//        }
    }

    public void getLocation() {
        try {
            if (!isGPSEnabled && !isNetworkEnabled) {
                //network disable
            } else {
                this.canGetLocation = true;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, this);
                    Log.d("activity", "LOC Network Enabled");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            Log.d("activity", "LOC by Network");
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                else if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
                    Log.d("activity", "RLOC: GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            Log.d("activity", "RLOC: loc by GPS");
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }
            onLocationChanged(location);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.d("activity", "RLOC: Location USER "+latitude+" "+longitude);

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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

/**- - - - -  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ---- - - - -- - -  */

    public void enableMyLocation() {
        //mengecek status permission yang kita request
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //jika belum diberi permission, maka minta persmission
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else  {
            // Access to the location has been granted to the app.
            getLocation();
            getDatabase();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {

            if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Enable the my location layer if the permission has been granted.
                someFlag = true;
            } else {
                // Display the missing permission error dialog when the fragments resume.
                mPermissionDenied = true;
            }
        }else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    protected void onResume() {
        super.onResume();
        if(someFlag) {
            enableMyLocation();
        }
    }

    public ArrayList<Bengkel> getBengkelList() {
        return bengkels;
    }

    public void getDatabase(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bengkels.clear();

                /**--- with radius, for show bengkel location which in radius---**/
                for (DataSnapshot bengkelSnapshot: dataSnapshot.getChildren()) {
                    Bengkel bengkel = bengkelSnapshot.getValue(Bengkel.class);
                    if(bengkel.getbLongitude() > (longitude-radius) && bengkel.getbLongitude() < (longitude+radius) &&
                            bengkel.getbLatitude() > (latitude-radius) && bengkel.getbLatitude() < (latitude+radius) ) {
                        Log.e("Nama", bengkel.getbNama());
                        Log.e("Alamat", bengkel.getbAlamat());
                        double haversine = new Haversine().Formula(latitude,longitude,bengkel.getbLatitude(),bengkel.getbLongitude());
                        bengkel.setbJarak(haversine);
                        bengkels.add(bengkel);
                    }
                }
                //Sorting jarak terdekat
                Collections.sort(bengkels, new Comparator<Bengkel>() {
                    @Override
                    public int compare(Bengkel o1, Bengkel o2) {
                        return Double.compare(o1.getbJarak(), o2.getbJarak());
                    }
                });

//                for (DataSnapshot bengkelSnapshot: dataSnapshot.getChildren()) {
//                    Bengkel bengkel = bengkelSnapshot.getValue(Bengkel.class);
//                    Log.e("Nama", bengkel.getbNama());
//                    Log.e("Alamat", bengkel.getbAlamat());
//                    bengkels.add(bengkel);
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadNote:onCancelled", databaseError.toException());
            }
        };
        mBengkelRef.addValueEventListener(valueEventListener);

        SplashFragment splashFragment = new SplashFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainLayout, splashFragment).commit();
    }

}
