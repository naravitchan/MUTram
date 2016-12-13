package com.egco428.mutram;

/*
 * Created by Benz on 10/31/2016.
 */
public class DataList {
    private String picture;
    private String message;
    private String detail;
    private String lat;
    private String longitude;

    public DataList( String message, String pic_name , String detail, String lat, String longitude ){

        this.picture = pic_name;
        this.message = message;
        this.detail = detail;
        this.lat = lat;
        this.longitude= longitude;
    }
    public String getPicName(){ return picture ; }
    public String getMessage(){ return message; }
    public String getDetail(){ return detail; }
    public String getLat() {return lat;}
    public String getLongitude() {return longitude;}


}
