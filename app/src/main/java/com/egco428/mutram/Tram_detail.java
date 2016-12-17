package com.egco428.mutram;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.location.LocationListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Tram_detail extends AppCompatActivity implements android.location.LocationListener {
    ListView listView;
    private DatabaseReference mDatabase;

    protected LocationManager locationManager;
    protected android.location.LocationListener locationListener;

    String lat;
    String longitude_main;
    String name_loc;
    Location location;

    List<DataList> arrayData = new ArrayList<>();
    ArrayAdapter<DataList> customArrayAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tram_detail);
        listView = (ListView) findViewById(R.id.listView2);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location != null ) {
            // Do something with the recent location fix
            //  otherwise wait for the update below
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

        //get values for calculate
        Intent intent = getIntent();
        lat = intent.getStringExtra(MainActivity.Latitude);
        longitude_main = intent.getStringExtra(MainActivity.Longitude);
        name_loc = intent.getStringExtra(MainActivity.Station);

        mDatabase = FirebaseDatabase.getInstance().getReference("Location");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                retriveData((Map<String, Object>) dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        calculateDistance();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,  this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    private void retriveData(Map<String, Object> value) {

        for (Map.Entry<String, Object> entry : value.entrySet())
        {
            Map singleUser = (Map) entry.getValue();
            String nameUser = (String) singleUser.get("message");
            String password = (String) singleUser.get("picName");
            String detail = (String) singleUser.get("detail");
            String lat = (String) singleUser.get("lat");
            String longitude = (String) singleUser.get("longitude");
            String station = (String) singleUser.get("station");
            String red = (String) singleUser.get("redtime");
            String blue = (String) singleUser.get("bluetime");
            String green = (String) singleUser.get("greentime");

            if (name_loc.contains(nameUser)) {
             //   arrayData.add(new DataList(nameUser, password, detail, lat, longitude, station, true,red,blue,green));
            }
            if(arrayData.size()>0){
                customArrayAdapter = new CustomArrayAdapter(this, 0, arrayData);
//            customArrayAdapter.sort(new Comparator<DataList>() {
//                @Override
//                public int compare(DataList o1, DataList o2) {
//                    String cuto1 = o1.getDetail().replaceAll("\\D+","");
//                    String cuto2 = o2.getDetail().replaceAll("\\D+","");
//                    return Integer.parseInt(cuto1) - Integer.parseInt(cuto2);
//                }
//            });
                listView.setAdapter(customArrayAdapter);
            }
        }

    }

    private void calculateDistance(){

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}
