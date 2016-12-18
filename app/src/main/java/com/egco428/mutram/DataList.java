package com.egco428.mutram;

import android.widget.Button;

/*
 * Created by Benz on 10/31/2016.
 */
public class DataList {
    private String pic;
    private String message;
    private String detail;
    private String lat;
    private String longitude;
    private String station;
    private String red;
    private String blue;
    private String green;


    public DataList(String message , String pic, String detail, String lat, String longitude,
                    String station,String red,String blue, String green){
        this.pic =pic;
        this.message = message;
        this.detail = detail;
        this.lat = lat;
        this.longitude= longitude;
        this.station= station;
        this.red = red;
        this.blue = blue;
        this.green = green;
    }

    public String getMessage(){ return message; }
    public String getPicName(){ return pic; }
    public String getDetail(){ return detail; }
    public String getLat() {return lat;}
    public String getLongitude() {return longitude;}
    public String getStation() {return station;}
    public String getRedtime() {return red;}
    public String getBluetime() {return blue;}
    public String getGreentime() {return green;}




}
