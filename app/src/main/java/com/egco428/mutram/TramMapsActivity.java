package com.egco428.mutram;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class TramMapsActivity extends AppCompatActivity implements OnMapReadyCallback,android.location.LocationListener {
    protected LocationManager locationManager;
    protected android.location.LocationListener locationListener;
    private GoogleMap mMap;
    LatLng now;
    Marker usermarker;
    Double longitu=LoadScreen.longitudeknow; //user
    Double latitude=LoadScreen.latitudeknow; //user
    Double longitu2; //destination
    Double latitude2; //destination
    Double longitu3; //src
    Double latitude3; //src

    TextView dis;
    TextView time;
    TextView des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tram_maps);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name"); //destination name
        String stationdsc = intent.getStringExtra("station"); //station number
        String stationsrc = intent.getStringExtra("stationsrc");
        List<DataList> arrayData = MainActivity.arrayData;
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dis = (TextView)findViewById(R.id.textView);
        time = (TextView)findViewById(R.id.textView2);
        des = (TextView)findViewById(R.id.textView3);

        for(int i=0;i<arrayData.size();i++){
            DataList object = (DataList)arrayData.get(i);
            String station = object.getStation();
            if(station.equals(stationdsc)){
                longitu2=Double.parseDouble(object.getLongitude()); // find location destination station
                latitude2=Double.parseDouble(object.getLat());
            }
            if(station.equals(stationsrc)){
                longitu3=Double.parseDouble(object.getLongitude()); // find location src station
                latitude3=Double.parseDouble(object.getLat());
            }
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        now = new LatLng(latitude, longitu);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        mMap.setMyLocationEnabled(true);
//
//        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
//        usermarker = mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(latitude, longitu))
//                .icon(icon)
//                .title("User")
//                .snippet("content"));
        Marker markerdes = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude2, longitu2))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.endpin))
                .title("Destination Station")
                .snippet("content"));
        Marker markersrc = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude3, longitu3))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.startpin))
                .title("Start Station")
                .snippet("content"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(now,18));

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            if(usermarker!=null){
            usermarker.remove();}
            latitude=location.getLatitude();
            longitu=location.getLongitude();
            Log.v("Trammap--LocaChanged ", latitude + " and lo" + longitu);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                return;
            }
            usermarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitu))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                    .title("User")
                    .snippet("content"));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }
    public void maprefesh(){}
}
