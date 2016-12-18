package com.egco428.mutram;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ArrayAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoadScreen extends Activity {
    Handler handler;
    Runnable runnable;
    long delay_time;
    long time = 3000L;

//    private DatabaseReference mDatabase;
//    public static List<DataList> arrayData = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_load_screen);
        handler = new Handler();
        //List<DataList> arrayData = new ArrayList<>();

        runnable = new Runnable() {
            public void run() {
                Intent intent = new Intent(LoadScreen.this, MainActivity.class);
//                mDatabase = FirebaseDatabase.getInstance().getReference("Location");
//                mDatabase.addListenerForSingleValueEvent(new ValueEventListener(){
//                    @Override
//                    public void onDataChange(DataSnapshot snapshot) {
//                        retriveData((Map<String,Object>) snapshot.getValue());
//  //                      Singleton.getInstance().setList(arrayData);
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        // Getting Post failed, log a message
//
//                        // ...
//                    }
//                });

                startActivity(intent);
                finish();
            }
        };
    }
//    private void retriveData(Map<String, Object> value) {
//
//        for (Map.Entry<String, Object> entry : value.entrySet())
//        {
//            Map singleUser = (Map) entry.getValue();
//            String nameUser = (String)singleUser.get("message");
//            String password = (String)singleUser.get("picName");
//            String detail = (String)singleUser.get("detail");
//            String lat = (String)singleUser.get("lat");
//            String longitude = (String)singleUser.get("longitude");
//            String station = (String)singleUser.get("station");
//            String red = (String) singleUser.get("redtime");
//            String blue = (String) singleUser.get("bluetime");
//            String green = (String) singleUser.get("greentime");
//
//            arrayData.add(new DataList(nameUser,password,detail,lat,longitude,station,false,red,blue,green));
//        }}
//

    public void onResume() {
        super.onResume();
        delay_time = time;
        handler.postDelayed(runnable, delay_time);
        time = System.currentTimeMillis();
    }

    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        time = delay_time - (System.currentTimeMillis() - time);
    }
}