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
        view.setBackgroundColor(Color.WHITE);

        TextView txt = (TextView)view.findViewById(R.id.msgText);
        txt.setText(dataList.getMessage());

        TextView time = (TextView)view.findViewById(R.id.details);
        time.setText(dataList.getTimestamp());

        ImageView image = (ImageView)view.findViewById(R.id.imageView);
        int res = context.getResources().getIdentifier(dataList.getPicName(),"drawable",context.getPackageName());
        image.setImageResource(res);
        return view;

    }
}
