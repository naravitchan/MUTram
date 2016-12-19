package com.egco428.mutram;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by seagame on 13/12/2559.
 */
public class CustomArrayAdapterTram extends ArrayAdapter<DataList>{         //for page tram_detail
    Context context;
    List<DataList> objects;

    public CustomArrayAdapterTram(Context context, int resource, List <DataList> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    public void clearData(){
        objects.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DataList dataList = objects.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.locationtram, null);
        view.setBackgroundColor(Color.WHITE);
        TextView time = (TextView)view.findViewById(R.id.detailtram);
        TextView txt = (TextView)view.findViewById(R.id.tramline);
        txt.setText(dataList.getDetail());
        time.setText(dataList.getMessage());

        if(txt.getText().equals("Red Tram")){                               //set color for each line
            view.setBackgroundResource(R.color.red);
        }
        else if(txt.getText().equals("Blue Tram")){
            view.setBackgroundResource(R.color.blue);
        }
        else if(txt.getText().equals("Green Tram")){
            view.setBackgroundResource(R.color.green);
        }


        ImageView image = (ImageView)view.findViewById(R.id.imageView);
       int res = context.getResources().getIdentifier("pic"+dataList.getStation(),"drawable",context.getPackageName());
        image.setImageResource(res);
        return view;

    }

}

