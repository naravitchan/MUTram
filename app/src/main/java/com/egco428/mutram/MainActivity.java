package com.egco428.mutram;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter<DataList> customArrayAdapter ;
    MaterialSearchView searchView;
    protected List<DataList> values = new ArrayList<>();
    ListView listView;
    public static final String Latitude = "latitude";
    public static final String Longitude = "longitude";
    public static final String Station = "name";
    public static final String Station2 = "station";

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase;
    public static List<DataList> arrayData = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        listView= (ListView) findViewById(R.id.listView);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);          //set search toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Location Search");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        searchView = (MaterialSearchView)findViewById(R.id.search_view);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() { // function search
            @Override
            public void onSearchViewShown() {}

            @Override
            public void onSearchViewClosed() {                  //If closed Search View , lstView will return default
                listView.setAdapter(customArrayAdapter);
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {   //onsearchlistenner
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null && !newText.isEmpty()){                      //text not null ->search
                    List<DataList> arrayDataSearch = new ArrayList<>();
                    for(int i=0;i<arrayData.size();i++){
                        DataList object = (DataList)arrayData.get(i);
                        String newtext2=newText.toLowerCase();
                        String search = object.getMessage().toLowerCase();
                        String search2 = object.getDetail().toLowerCase();
                        if(search.contains(newtext2)||search2.contains(newtext2)){
                            arrayDataSearch.add(object);
                            Log.e("search",object.getMessage());
                        }

                    }
                    customArrayAdapter = new CustomArrayAdapter(MainActivity.this, 0, arrayDataSearch);        //search result
                    listView.setAdapter(customArrayAdapter);
                }
                else{
                                                        //if search text is null
                                                        //return default
                    customArrayAdapter = new CustomArrayAdapter(MainActivity.this, 0, arrayData);
                    listView.setAdapter(customArrayAdapter);
                }
                return true;
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("Location");            //set database

        ValueEventListener postListener = new ValueEventListener()  {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                retriveData((Map<String,Object>) snapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        mDatabase.addValueEventListener(postListener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {         //listview click
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                DataList object = (DataList) arrayData.get(position);

                Intent intent = new Intent(MainActivity.this,Tram_detail.class);
                intent.putExtra(Station, object.getMessage());
                intent.putExtra(Station2, object.getStation());
                startActivity(intent);                                              //go to tram_detail &&put Intent
            }
        });

    }

    private void retriveData(Map<String, Object> value) {                   //get data from firebase
        if(arrayData.size()<=0){
        for (Map.Entry<String, Object> entry : value.entrySet())
        {
            Map singleUser = (Map) entry.getValue();
            String nameUser = (String)singleUser.get("message");
            String detail = (String)singleUser.get("detail");
            String lat = (String)singleUser.get("lat");
            String longitude = (String)singleUser.get("longitude");
            String station = (String)singleUser.get("station");
            String red = (String) singleUser.get("redtime");
            String blue = (String) singleUser.get("bluetime");
            String green = (String) singleUser.get("greentime");


            arrayData.add(new DataList(nameUser,detail,lat,longitude,station,red,blue,green));      //add data object to arraylist
        }
        }
        if(arrayData.size()>0){
            customArrayAdapter = new CustomArrayAdapter(this, 0, arrayData);
            listView.setAdapter(customArrayAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

}

