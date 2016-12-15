package com.egco428.mutram;

/*
 * Created by Benz on 10/31/2016.
 */
public class DataList {
    private String pic;
    private String message;
    private String detail;
    private String lat;
    private String longitude;

    public DataList( String message ,String pic, String detail, String lat, String longitude ){
        this.pic =pic;
        this.message = message;
        this.detail = detail;
        this.lat = lat;
        this.longitude= longitude;
    }

    public String getMessage(){ return message; }
    public String getPicName(){ return pic; }
    public String getDetail(){ return detail; }
    public String getLat() {return lat;}
    public String getLongitude() {return longitude;}


}
