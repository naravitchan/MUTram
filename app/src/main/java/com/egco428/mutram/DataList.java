package com.egco428.mutram;

import android.widget.Button;

import java.io.Serializable;

/*
 * Created by Benz on 10/31/2016.
 */
public class DataList  {
    private String message;
    private String detail;
    private String lat;
    private String longitude;
    private String station;
    private String redtime;
    private String bluetime;
    private String greentime;

    public DataList(String message , String detail, String lat, String longitude,
                    String station,String redtime,String bluetime, String greentime){

        this.message = message;
        this.detail = detail;
        this.lat = lat;
        this.longitude= longitude;
        this.station= station;
        this.redtime = redtime;
        this.bluetime = bluetime;
        this.greentime = greentime;
    }

    public String getMessage(){ return message; }
    public String getDetail(){ return detail; }
    public String getLat() {return lat;}
    public String getLongitude() {return longitude;}
    public String getStation() {return station;}
    public String getRedtime() {return redtime;}
    public String getBluetime() {return bluetime;}
    public String getGreentime() {return greentime;}




}
