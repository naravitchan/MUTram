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
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Tram_detail extends AppCompatActivity implements android.location.LocationListener {
    ListView listView;
    private DatabaseReference mDatabase;

    protected LocationManager locationManager;
    protected android.location.LocationListener locationListener;

    Double Latitude;
    Double Longitude;
    String lat;
    String longitude_main;
    String name_loc;
    Location location;

    List<DataList> arrayData = new ArrayList<>();
    List<DataList> arrayCal = new ArrayList<>();
    ArrayAdapter<DataList> customArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tram_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.listView2);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            // Do something with the recent location fix
            //  otherwise wait for the update below
        } else {
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

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }


    private void retriveData(Map<String, Object> value) {

        for (Map.Entry<String, Object> entry : value.entrySet()) {
            Map singleUser = (Map) entry.getValue();
            String title = (String) singleUser.get("message");
            String picname = (String) singleUser.get("picName");
            String detail = (String) singleUser.get("detail");
            String lat = (String) singleUser.get("lat");
            String longitude = (String) singleUser.get("longitude");
            String station = (String) singleUser.get("station");
            String red = (String) singleUser.get("redtime");
            String blue = (String) singleUser.get("bluetime");
            String green = (String) singleUser.get("greentime");

            arrayCal.add(new DataList(title, picname, detail, lat, longitude, station, true,red,blue,green));
        }

    }

    private void calculateDistance() {
        double minred=2000;
        double minblue=2000;
        double mingreen=2000;
        String stationred="";
        String stationblue="";
        String stationgreen="";

        for(int i=0;i<arrayCal.size();i++){
            DataList object = (DataList)arrayCal.get(i);
            String lat = object.getLat();
            String longi = object.getLongitude();
            String station = object.getStation();
            String greentime = object.getGreen();
            String redtime = object.getRed();
            String bluetime = object.getBlue();
            Double longitu=Double.parseDouble(longi);
            Double latitude=Double.parseDouble(lat);
            Double distance = distance(Latitude,Longitude,latitude,longitu);
            if((!greentime.equals(""))&&(distance<mingreen)){
                Log.v("min green disc ", distance + " and station " + station);
                mingreen=distance;
                stationgreen=station;
            }
            if((!redtime.equals(""))&&(distance<minred)){
                Log.v("min red disc ", distance + " and station " + station);
                minred=distance;
                stationred=station;
            }
            if((!bluetime.equals(""))&&(distance<minblue)){
                Log.v("min blue disc ", distance + " and station " + station);
                minblue=distance;
                stationblue=station;
            }


        }
//        Log.v("Total","---");
//        minblue=minblue*1609.34;
//        mingreen=mingreen*1609.34;
//        minred=minred*1609.34;
//        Log.e("min green disc ", stationgreen + " distance " + mingreen);
//        Log.e("min red disc ", stationred + " distance " + minred);
//        Log.e("min blue disc ", stationblue + " distance " + minblue);
        for(int i=0;i<arrayCal.size();i++){
            DataList object = (DataList)arrayCal.get(i);
            String title=object.getMessage();
            String picname=object.getPicName();
            String lat = object.getLat();
            String longi = object.getLongitude();
            String station = object.getStation();
            String greentime = object.getGreen();
            String redtime = object.getRed();
            String bluetime = object.getBlue();
            if(stationgreen.equals(station)){
                Log.v("add best green station ", station + " end " + station);
                arrayData.add(new DataList(title, picname, "Green Tram", lat, longi, station, true,redtime,bluetime,greentime));
            }
            if(stationblue.equals(station)){
                Log.v("add best blue station ", station + " end " + station);
                arrayData.add(new DataList(title, picname, "Blue Tram", lat, longi, station, true,redtime,bluetime,greentime));
            }
            if(stationred.equals(station)){
                Log.v("add best red station ", station + " end " + station);
                arrayData.add(new DataList(title, picname, "Red Tram", lat, longi, station, true,redtime,bluetime,greentime));
            }


        }
        if (arrayData.size() > 0) {
            customArrayAdapter = new CustomArrayAdapterTram(this, 0, arrayData);
            listView.setAdapter(customArrayAdapter);
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {

            Latitude=location.getLatitude();
            Longitude=location.getLongitude();
            Log.v("Location Changed la ", Latitude + " and lo" + Longitude);
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
            locationManager.removeUpdates(this);
            calculateDistance();

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}
