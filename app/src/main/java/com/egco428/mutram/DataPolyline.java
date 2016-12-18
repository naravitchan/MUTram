package com.egco428.mutram;

/**
 * Created by Benz on 12/18/2016.
 */
public class DataPolyline {
    private String lat;
    private String lon;

    public DataPolyline(String lat , String lon){
        this.lat=lat;
        this.lon=lon;
    }

    public String getLat(){return lat;}
    public String getLon(){return lon;}


}
