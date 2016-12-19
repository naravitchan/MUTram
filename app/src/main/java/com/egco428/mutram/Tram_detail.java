package com.egco428.mutram;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import com.google.firebase.database.DatabaseReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Tram_detail extends AppCompatActivity implements android.location.LocationListener,SensorEventListener {
    ListView listView;
    private View view;
    int count=0;
    boolean distanceshow=true;
    private DatabaseReference mDatabase;
    List<DataList> arrayData = new ArrayList<>();
    List<DataList> arrayCal = MainActivity.arrayData;
    ArrayAdapter<DataList> customArrayAdapter;
    private SensorManager sensorManager;
    private long lastUpdate;

    String Station = MainActivity.Station;          //intent name
    String Station2 = MainActivity.Station2;
    String Station3 = "stationsrc";

    String stationdesc;
    protected LocationManager locationManager;

    Double redtimed=9.9;                            //var for check station line
    Double bluetimed=9.9;
    Double greentimed=9.9;

    Double Latitude = LoadScreen.latitudeknow;      //first get LatLon from Loadscreen then use from onchange
    Double Longitude =  LoadScreen.longitudeknow;

    String name="";                                 //var for get time or distance
    String green,red,blue;

    String stationred="";                           //var for find nearly station
    String stationblue="";
    String stationgreen="";

    double minred=2000,minblue=2000,mingreen=2000;         //var for distance calculate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tram_detail);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);          //back icon bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);        //for accelerometer
        lastUpdate = System.currentTimeMillis();

        name = intent.getStringExtra("name");                                   //intent
        stationdesc = intent.getStringExtra("station");

        listView = (ListView) findViewById(R.id.listView2);
        calculateDistance();                                                    //cal distance for find nearly station cause onchange is slow
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        Location location;


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("turn off gps ","or internet");           //permit error
            return;
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {             //onclick listview
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                DataList object = (DataList) arrayData.get(position);

                Intent intent = new Intent(Tram_detail.this,TramMapsActivity.class);
                intent.putExtra(Station, name);
                intent.putExtra(Station2, stationdesc);
                intent.putExtra(Station3, object.getStation()); //src station
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


    private void calculateDistance() {                  //function find then show 3 nearly station
        arrayData.clear();


        for(int i=0;i<arrayCal.size();i++){
            DataList object = (DataList)arrayCal.get(i);
            String station = object.getStation();
            Double longitu=Double.parseDouble(object.getLongitude());
            Double latitude=Double.parseDouble(object.getLat());


            if(object.getStation().equals(stationdesc)){            //get time from destination
                if(!object.getRedtime().equals(""))
                redtimed=Double.parseDouble(object.getRedtime());
                if(!object.getBluetime().equals(""))
                bluetimed=Double.parseDouble(object.getBluetime());
                if(!object.getGreentime().equals(""))
                greentimed=Double.parseDouble(object.getGreentime());
            }

            Double distance = distance(Latitude,Longitude,latitude,longitu);    //use distance function
            if((!object.getGreentime().equals(""))&&(distance<mingreen)){       //find shortest distance
                mingreen=distance;
                stationgreen=station;
            }
            if((!object.getRedtime().equals(""))&&(distance<minred)){
                minred=distance;
                stationred=station;
            }
            if((!object.getBluetime().equals(""))&&(distance<minblue)){
                minblue=distance;
                stationblue=station;
            }

        }


        for(int i=0;i<arrayCal.size();i++){                                     //after know shortest distance
            DataList object = (DataList)arrayCal.get(i);
            String station = object.getStation();
            if((stationgreen.equals(station))||(stationblue.equals(station))||(stationred.equals(station))) {
                String title = object.getMessage();
                String lat = object.getLat();
                String longi = object.getLongitude();
                String greentime = object.getGreentime();
                String redtime = object.getRedtime();
                String bluetime = object.getBluetime();
                if (stationgreen.equals(station)&&greentimed!=9.9) {                //check (station=nearest) and (destination must have this color line)
                    Log.v("add best green station ", station + " end " + station);
                    Double greentimesrc = Double.parseDouble(object.getGreentime());
                    Double greenusetime=greentimed-greentimesrc;
                    if(greenusetime<0){
                        greenusetime=(1260-greentimesrc)+greentimed;                //if   against the lane direction must use this cal
                    }
                    green = tomin(greenusetime);
                    arrayData.add(new DataList(title+"\n\n"+green, "Green Tram", lat, longi, station, redtime, bluetime, greentime)); //get data and time
                }
                if (stationblue.equals(station)&&bluetimed!=9.9) {                  //for blue
                    Log.v("add best blue station ", station + " end " + station);
                    Double bluetimesrc = Double.parseDouble(object.getBluetime());
                    Double blueusetime=bluetimed-bluetimesrc;
                    if(blueusetime<0){
                        blueusetime=(1260-bluetimesrc)+bluetimed;
                    }
                    blue = tomin(blueusetime);

                    arrayData.add(new DataList(title+"\n\n"+blue, "Blue Tram", lat, longi, station, redtime, bluetime, greentime));
                }
                if (stationred.equals(station)&&redtimed!=9.9) {                  //for red
                    Log.v("add best red station ", station + " end " + station);
                    Double redtimesrc = Double.parseDouble(object.getRedtime());
                    Double redusetime=redtimed-redtimesrc;
                    if(redusetime<0){
                        redusetime=(1260-redtimesrc)+redtimed;
                    }
                    red = tomin(redusetime);
                    arrayData.add(new DataList(title+"\n\n"+red, "Red Tram", lat, longi, station, redtime, bluetime, greentime));
                }
            }

        }
        if (arrayData.size() > 0) {                                             //put to list view
            customArrayAdapter = new CustomArrayAdapterTram(this, 0, arrayData);
            listView.setAdapter(customArrayAdapter);
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {           //distance function use 2 LatLng
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515*1609.344;
        return (dist);
    }
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public static String tomin (Double biggy)                           //function change sec to min
    {
        Double min =biggy/60;

        Double second=biggy%60;
        DecimalFormat df = new DecimalFormat("####00");
        String tomin= "Time "+df.format(min)+":"+df.format(second)+" min";
        return tomin;
    }

    @Override
    public void onLocationChanged(Location location) {              //location change
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
            calculateDistance();                            //use function calcu again
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            getAccelerometer(event);
        }
    }

    private void getAccelerometer(SensorEvent event){           //accelerometer
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH); //distance between 2 point
        long actualTime = System.currentTimeMillis();
        if (accelationSquareRoot >= 2) //
        {

            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;
            if(count>1){                                                //shacke 2 time to do
                Log.e("accelerometer","shake");
                arrayData.clear();
                for(int i=0;i<arrayCal.size();i++){                         //case 1 :show distance case 2 :show time
                    DataList object = (DataList)arrayCal.get(i);
                    String station = object.getStation();
                    if((stationgreen.equals(station))||(stationblue.equals(station))||(stationred.equals(station))) {
                        String title = object.getMessage();
                        String lat = object.getLat();
                        String longi = object.getLongitude();
                        String greentime = object.getGreentime();
                        String redtime = object.getRedtime();
                        String bluetime = object.getBluetime();
                        if(distanceshow){
                            if (stationgreen.equals(station)&&greentimed!=9.9) {
                                int dis = (int) mingreen;
                                arrayData.add(new DataList(title+"\n\n"+"distance: "+dis+" m", "Green Tram", lat, longi, station, redtime, bluetime, greentime));
                            }
                            if (stationblue.equals(station)&&bluetimed!=9.9) {
                                int dis = (int) minblue;
                                arrayData.add(new DataList(title+"\n\n"+"distance: "+dis+" m", "Blue Tram", lat, longi, station, redtime, bluetime, greentime));
                            }
                            if (stationred.equals(station)&&redtimed!=9.9) {
                                int dis = (int) minred;
                                arrayData.add(new DataList(title+"\n\n"+"distance: "+dis+" m", "Red Tram", lat, longi, station, redtime, bluetime, greentime));
                            }
                        }
                        else{
                            if (stationgreen.equals(station)&&greentimed!=9.9) {
                                Log.v("add best green station ", station + " end " + station);
                                Double greentimesrc = Double.parseDouble(object.getGreentime());
                                Double greenusetime=greentimed-greentimesrc;
                                if(greenusetime<0){
                                    greenusetime=(1260-greentimesrc)+greentimed;
                                }
                                green = tomin(greenusetime);
                                arrayData.add(new DataList(title+"\n\n"+green, "Green Tram", lat, longi, station, redtime, bluetime, greentime));
                            }
                            if (stationblue.equals(station)&&bluetimed!=9.9) {
                                Log.v("add best blue station ", station + " end " + station);
                                Double bluetimesrc = Double.parseDouble(object.getBluetime());
                                Double blueusetime=bluetimed-bluetimesrc;
                                if(blueusetime<0){
                                    blueusetime=(1260-bluetimesrc)+bluetimed;
                                }
                                blue = tomin(blueusetime);

                                arrayData.add(new DataList(title+"\n\n"+blue, "Blue Tram", lat, longi, station, redtime, bluetime, greentime));
                            }
                            if (stationred.equals(station)&&redtimed!=9.9) {
                                Log.v("add best red station ", station + " end " + station);
                                Double redtimesrc = Double.parseDouble(object.getRedtime());
                                Double redusetime=redtimed-redtimesrc;
                                if(redusetime<0){
                                    redusetime=(1260-redtimesrc)+redtimed;
                                }
                                red = tomin(redusetime);
                                arrayData.add(new DataList(title+"\n\n"+red, "Red Tram", lat, longi, station, redtime, bluetime, greentime));
                            }
                        }
                    }

                }
                if (arrayData.size() > 0) {
                    customArrayAdapter = new CustomArrayAdapterTram(this, 0, arrayData);
                    listView.setAdapter(customArrayAdapter);
                }
                count=0;
                if(distanceshow){
                    distanceshow=false;
                }
                else distanceshow=true;
            }
            else {count++;}

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor,int accuracy){

    }
    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
