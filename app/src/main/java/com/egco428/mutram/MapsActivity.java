package com.egco428.mutram;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private GoogleMap mMap;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    TextView lblLat;
    TextView lblLon;
    EditText name;
    EditText detail;
    EditText station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        lblLat = (TextView) findViewById(R.id.lblLat);
        lblLon = (TextView) findViewById(R.id.lblLon);
        name = (EditText)findViewById(R.id.nameT);
        detail = (EditText)findViewById(R.id.detailT);
        station = (EditText)findViewById(R.id.stationT);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onLocationChanged(Location location) {

        lblLat.setText("" + location.getLatitude()); //get current Latitude :
        lblLon.setText("" + location.getLongitude()); //get current Longitude :
        Log.e("LatLon","Latitude : " + location.getLatitude()+"Longitude : " + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public void AddLaLo(View view) {
        String lat= lblLat.getText().toString();
        String longi = lblLon.getText().toString();
        String nameLoc = name.getText().toString();
        String detailLoc = detail.getText().toString();
        String stationLoc = station.getText().toString();

        DatabaseReference ref = database.getReference("Location");
        ref.push().setValue(new DataList(nameLoc,detailLoc,lat,longi,stationLoc,"","",""));
        ref.child("redline").push().setValue(new DataPolyline("11","12"));
        ref.child("blueline").push().setValue(new DataPolyline("11","12"));
        ref.child("greenline").push().setValue(new DataPolyline("11","12"));
        Toast.makeText(this,"added to firebase already",Toast.LENGTH_SHORT).show();
    }
}
