package com.egco428.mutram;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by seagame on 13/12/2559.
 */
public class CustomArrayAdapter extends ArrayAdapter<DataList>{
    Context context;
    List<DataList> objects;

    public CustomArrayAdapter(Context context,int resource,List <DataList> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DataList dataList = objects.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.locationrow, null);
//        if(dataList.getBoolean()==false){
//            View view = inflater.inflate(R.layout.locationtram, null);
//        }
//        else {
//        View view = inflater.inflate(R.layout.locationrow, null);}
        view.setBackgroundColor(Color.WHITE);
        TextView time = (TextView)view.findViewById(R.id.details);
        TextView txt = (TextView)view.findViewById(R.id.msgText);
        txt.setText(dataList.getMessage());
            time.setText(dataList.getDetail());



        if (position % 2 == 1) {
            view.setBackgroundResource(R.color.fis);
        } else {
            view.setBackgroundResource(R.color.tw);
        }


        ImageView image = (ImageView)view.findViewById(R.id.imageView);
       int res = context.getResources().getIdentifier("pic"+dataList.getStation(),"drawable",context.getPackageName());
        image.setImageResource(res);
        return view;

    }

}

