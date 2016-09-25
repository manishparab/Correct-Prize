package com.ksm.cp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ksm.cp.Helper.SessionHelper;
import com.ksm.cp.Objects.CurrentUser;
import com.ksm.cp.Objects.Filter;
import com.ksm.cp.Objects.Location;
import com.ksm.cp.R;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;


public class FilterActivity extends AppCompatActivity {
    TextView TextViewSelectedDistance;
    RelativeLayout RelativeLayoutLocation;
    TextView TextViewUserLocation;
    Filter filter;
    DiscreteSeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Home");

        seekBar = (DiscreteSeekBar) findViewById(R.id.SeekBarFilterDistance);
        TextViewSelectedDistance = (TextView) findViewById(R.id.TextViewSelectedDistance);
        RelativeLayoutLocation = (RelativeLayout) findViewById(R.id.RelativeLayoutLocation);
        TextViewUserLocation = (TextView) findViewById(R.id.TextViewUserLocation);
        filter = SessionHelper.GetFilerOptions(getApplicationContext());
        if (filter !=  null)
        {
            TextViewUserLocation.setText(filter.location.LocationName);
        }
        if (filter.Distance > 0) {
            SetSeekBarProgress(filter.Distance);
        }

        RelativeLayoutLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.EditActivityLocation.class);
                intent.putExtra("FilterActivity","1");
                startActivity(intent);
            }
        });
        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                final int progressvalue = value;
                runOnUiThread(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressvalue == 1) {
                            TextViewSelectedDistance.setText("5");
                            SetFilterDistance(5);
                        }
                        if (progressvalue == 2) {
                            TextViewSelectedDistance.setText("10");
                            SetFilterDistance(10);
                        }
                        if (progressvalue == 3) {
                            TextViewSelectedDistance.setText("20");
                            SetFilterDistance(20);
                        }
                        if (progressvalue == 4) {
                            TextViewSelectedDistance.setText("30");
                            SetFilterDistance(30);
                        }
                        if (progressvalue == 5) {
                            TextViewSelectedDistance.setText("Max");
                            SetFilterDistance(Integer.MAX_VALUE);
                        }
                    }
                }));
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });
    }

    private void SetFilterDistance(int distance)
    {
        if (filter != null)
        {
            filter.Distance = distance;
            SessionHelper.SetFilterOption(getApplicationContext(),filter);
        }
    }

    private void SetSeekBarProgress(int value)
    {
        TextViewSelectedDistance.setText(String.valueOf(value));
        if (value == 5)
        {
            seekBar.setProgress(1);
        }
        else if (value ==10)
        {
            seekBar.setProgress(2);

        }
        else if (value == 20)
        {
            seekBar.setProgress(3);
        }
        else if (value == 30)
        {
            seekBar.setProgress(4);
        }
        else if (value == Integer.MAX_VALUE)
        {
            seekBar.setProgress(5);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filtermenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                onBackPressed();
                this.finish();
                return true;
            case R.id.done:
                Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.home.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
