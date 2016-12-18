package com.egco428.mutram;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benz on 12/18/2016.
 */
public class Singleton {
    private static Singleton mInstance = null;
    private List<DataList> arraydata;

    public static Singleton getInstance(){
        if(mInstance == null)
        {
            mInstance = new Singleton();
        }
        return mInstance;
    }
    public List<DataList> getList(){
        return this.arraydata;
    }

    public void setList(List<DataList> value){
        this.arraydata.clear();
        this.arraydata.addAll(value);
    }

}
