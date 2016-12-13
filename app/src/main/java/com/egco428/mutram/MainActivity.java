package com.egco428.mutram;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    ArrayAdapter<DataList> customArrayAdapter ;
    protected List<DataList> values = new ArrayList<>();
    ListView listView;

    private String time="31-Oct-2016 5.30 p.m.";
    public static final int DETAIL_REQ_CODE = 1001;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        listView= (ListView) findViewById(R.id.listView);
//        dataSource = new DataSource(this);
//        dataSource.open();
//        values = dataSource.getAllFortune();
        DataList dataList;
        customArrayAdapter = new CustomArrayAdapter(this, 0, values);
        listView.setAdapter(customArrayAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
//
//                final DataList object = (DataList) listView.getItemAtPosition(position);
//                dataSource.deleteFortune(object);
//
//                view.animate().setDuration(1000).alpha(0).withEndAction(new Runnable() {
//                    @Override
//                    public void run() {
//                        customArrayAdapter.remove(object);
//                        customArrayAdapter.notifyDataSetChanged();
//                        view.setAlpha(1);
//                    }
//                });
//            }
//        });
    }
}
