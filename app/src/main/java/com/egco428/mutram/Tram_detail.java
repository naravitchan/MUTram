package com.egco428.mutram;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Tram_detail extends AppCompatActivity implements android.location.LocationListener {
    ListView listView;
    private DatabaseReference mDatabase;
    String Station = MainActivity.Station;
    String Station2 = MainActivity.Station2;
    String Station3 = "stationsrc";
    String stationdesc;
    protected LocationManager locationManager;
    Double redtimed=9.9;
    Double bluetimed=9.9;
    Double greentimed=9.9;
    int i=1,r,g,b;
    Double Latitude = LoadScreen.latitudeknow;
    Double Longitude =  LoadScreen.longitudeknow;
    String name="";
    String green,red,blue;
    String station="";
    List<DataList> arrayData = new ArrayList<>();
    List<DataList> arrayCal = MainActivity.arrayData;
    ArrayAdapter<DataList> customArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tram_detail);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        stationdesc = intent.getStringExtra("station"); //desc
        listView = (ListView) findViewById(R.id.listView2);
        calculateDistance();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        Location location;

//        if (location != null) {
//
//            Latitude= location.getLatitude();
//            Longitude= location.getLongitude();
////            calculateDistance();
//            Log.e("find Last know ","------------");
//
//        } else {
////            calculateDistance();
//
//            Log.d("dont find Last know ","no get last know");
//
//        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("turn off gps ","or internet");
            return;
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                DataList object = (DataList) arrayData.get(position);

                Intent intent = new Intent(Tram_detail.this,TramMapsActivity.class);
                intent.putExtra(Station, name);
                intent.putExtra(Station2, stationdesc);
                intent.putExtra(Station3, object.getStation()); //src station
                String timesent;
                if(position==r);
                timesent = red;
                if(position==g);
                timesent = green;
                if(position==b);
                timesent = blue;
                intent.putExtra("time", timesent);
                intent.putExtra("namesrc", object.getMessage());
                Log.e("name dsc"+name,"station dsc"+stationdesc+"src"+object.getStation());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }


    private void calculateDistance() {
        arrayData.clear();
        double minred=2000;
        double minblue=2000;
        double mingreen=2000;
        String stationred="";
        String stationblue="";
        String stationgreen="";

        for(int i=0;i<arrayCal.size();i++){
            DataList object = (DataList)arrayCal.get(i);
            String station = object.getStation();
            Double longitu=Double.parseDouble(object.getLongitude());
            Double latitude=Double.parseDouble(object.getLat());
            Double distance = distance(Latitude,Longitude,latitude,longitu);
            if(object.getStation().equals(stationdesc)){
                if(!object.getRedtime().equals(""))
                redtimed=Double.parseDouble(object.getRedtime());
                if(!object.getBluetime().equals(""))
                bluetimed=Double.parseDouble(object.getBluetime());
                if(!object.getGreentime().equals(""))
                greentimed=Double.parseDouble(object.getGreentime());

            }
            if((!object.getGreentime().equals(""))&&(distance<mingreen)){
//                Log.v("min green disc ", distance + " and station " + station);
                mingreen=distance;
                stationgreen=station;
            }
            if((!object.getRedtime().equals(""))&&(distance<minred)){
//                Log.v("min red disc ", distance + " and station " + station);
                minred=distance;
                stationred=station;
            }
            if((!object.getBluetime().equals(""))&&(distance<minblue)){
//                Log.v("min blue disc ", distance + " and station " + station);
                minblue=distance;
                stationblue=station;
            }

        }


        for(int i=0;i<arrayCal.size();i++){
            DataList object = (DataList)arrayCal.get(i);
            String station = object.getStation();
            if((stationgreen.equals(station))||(stationblue.equals(station))||(stationred.equals(station))) {
                String title = object.getMessage();
                String lat = object.getLat();
                String longi = object.getLongitude();
                String greentime = object.getGreentime();
                String redtime = object.getRedtime();
                String bluetime = object.getBluetime();
                if (stationgreen.equals(station)&&greentimed!=9.9) {
                    Log.v("add best green station ", station + " end " + station);
                    Double greentimesrc = Double.parseDouble(object.getGreentime());
                    Double greenusetime=greentimed-greentimesrc;
                    if(greenusetime<0){
                        greenusetime=(1260-greentimesrc)+greentimed;
                    }
                    green = tomin(greenusetime);
                    arrayData.add(new DataList(title+"\n"+green, "Green Tram", lat, longi, station, redtime, bluetime, greentime));
                    g=i;
                    i++;
                }
                if (stationblue.equals(station)&&bluetimed!=9.9) {
                    Log.v("add best blue station ", station + " end " + station);
                    Double bluetimesrc = Double.parseDouble(object.getBluetime());
                    Double blueusetime=bluetimed-bluetimesrc;
                    if(blueusetime<0){
                        blueusetime=(1260-bluetimesrc)+bluetimed;
                    }
                    blue = tomin(blueusetime);

                    arrayData.add(new DataList(title+"\n"+blue, "Blue Tram", lat, longi, station, redtime, bluetime, greentime));
                    b=i;
                    i++;
                }
                if (stationred.equals(station)&&redtimed!=9.9) {
                    Log.v("add best red station ", station + " end " + station);
                    Double redtimesrc = Double.parseDouble(object.getRedtime());
                    Double redusetime=redtimed-redtimesrc;
                    if(redusetime<0){
                        redusetime=(1260-redtimesrc)+redtimed;
                    }
                    red = tomin(redusetime);
                    arrayData.add(new DataList(title+"\n"+red, "Red Tram", lat, longi, station, redtime, bluetime, greentime));
                    r=i;
                    i++;
                }
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

    public static String tomin (Double biggy)
    {
        Double min =biggy/60;

        Double second=biggy%60;
        DecimalFormat df = new DecimalFormat("####00");
//        System.out.println("Value: " + df.format(value));
        String tomin= "Time "+df.format(min)+":"+df.format(second)+" min";
        return tomin;
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
