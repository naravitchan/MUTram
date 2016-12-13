package com.egco428.mutram;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    ArrayAdapter<DataList> customArrayAdapter ;
    protected List<DataList> values = new ArrayList<>();
    ListView listView;
    public static final String Latitude = "latitude";
    public static final String Longitude = "longitude";
    public static final String name = "name";

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase;
    List<DataList> arrayData = new ArrayList<>();

    private String time="31-Oct-2016 5.30 p.m.";
    public static final int DETAIL_REQ_CODE = 1001;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        listView= (ListView) findViewById(R.id.listView);

        DatabaseReference ref = database.getReference("User");
        DatabaseReference postsRef = ref.getParent();
        DatabaseReference newPostRef = postsRef.push();
        DataList dataList = new DataList("Mahidol Learning Center","pic0","Top Supermarket, One Stop Service, Harmony","13.792069", "100.322207");
        newPostRef.setValue(dataList);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        ValueEventListener postListener = new ValueEventListener()  {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                retriveData((Map<String,Object>) snapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                DataList object = (DataList) arrayData.get(position);

                Intent intent = new Intent(MainActivity.this,Calculate.class);
                intent.putExtra(name, object.getMessage());
                intent.putExtra(Latitude, object.getLat());
                intent.putExtra(Longitude, object.getLongitude());
                startActivity(intent);
            }
        });

//  
    }

    private void retriveData(Map<String, Object> value) {

        for (Map.Entry<String, Object> entry : value.entrySet()){
            Map singleUser = (Map) entry.getValue();
            String nameUser = (String)singleUser.get("message");
            String password = (String)singleUser.get("picName");
            String detail = (String)singleUser.get("detail");
            String lat = (String)singleUser.get("lat");
            String longitude = (String)singleUser.get("longitude");

            arrayData.add(new DataList(nameUser,password,detail,lat,longitude));
        }
        if(arrayData.size()>0){
            customArrayAdapter = new CustomArrayAdapter(this, 0, arrayData);
            listView.setAdapter(customArrayAdapter);
        }

    }
}

