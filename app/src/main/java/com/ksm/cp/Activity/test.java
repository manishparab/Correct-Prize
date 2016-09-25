package com.ksm.cp.Activity;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.etsy.android.grid.StaggeredGridView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ksm.cp.Adapter.AdapterForSgrid;
import com.ksm.cp.Adapter.DataAdapterEsty;
import com.ksm.cp.Adapter.MyAdapter;
import com.ksm.cp.R;

import java.util.ArrayList;
import java.util.List;

public class test extends FragmentActivity {

    GoogleMap mMap;
    SupportMapFragment mapFrag;

    private StaggeredGridView mGridView;
    private DataAdapterEsty mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<String> myDataset=  new ArrayList<String>();
        myDataset.add("manish");
        myDataset.add("manish 1");
        myDataset.add("manish 3");
        myDataset.add("manish 4");
        myDataset.add("manish 5");
        myDataset.add("manish 6");
        myDataset.add("manish 7");
        myDataset.add("manish 8");
        myDataset.add("manish 9");
        myDataset.add("manish 10");

        ArrayList<String> data =  new ArrayList<>();
        data.add("http://manishp.info/images/6/0.png");
        data.add("http://manishp.info/images/6/1.png");
        data.add("http://manishp.info/images/6/2.png");
        data.add("http://manishp.info/images/6/3.png");
        data.add("http://manishp.info/images/6/4.png");
        data.add("http://manishp.info/images/6/5.png");


        setContentView(R.layout.activity_test);

        SupportMapFragment mySupportMapFragment = ((SupportMapFragment) this
                .getSupportFragmentManager().findFragmentById(R.id.map));
        // Add a marker at San Francisco.
        GoogleMap googleMap = mySupportMapFragment.getMap();

        LatLng PERTH = new LatLng(-31.90, 115.86);
        Marker perth = googleMap.addMarker(new MarkerOptions()
                .title("PickUp location")
                .position(PERTH)
                .draggable(true));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(-31.90,115.86)).zoom(12).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



        //mGridView = (StaggeredGridView) findViewById(R.id.grid_view);
        //mAdapter =  new DataAdapterEsty(getApplicationContext(),data);
        //mGridView.setAdapter(mAdapter);


        /*RecyclerView mRecyclerView =  (RecyclerView) findViewById(R.id.my_recycler_view);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        MyAdapter mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);*/

        /*RecyclerView recList = (RecyclerView) findViewById(R.id.my_recycler_view);
        AdapterForSgrid adapterForSgrid =  new AdapterForSgrid("6",2);
        recList.setAdapter(adapterForSgrid);
        //recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(com.ksm.cp.Helper.LinearLayoutManager.HORIZONTAL);
        recList.setLayoutManager(llm);*/
    }
}
