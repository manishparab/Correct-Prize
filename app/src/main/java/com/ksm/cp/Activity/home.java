package com.ksm.cp.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.etsy.android.grid.StaggeredGridView;
import com.ksm.cp.ActivityLogin;
import com.ksm.cp.Adapter.DataAdapterEsty;
import com.ksm.cp.Helper.LocationHelper;
import com.ksm.cp.Helper.OfferItemHelper;
import com.ksm.cp.Helper.SessionHelper;
import com.ksm.cp.Helper.VolleySingle;
import com.ksm.cp.Objects.Filter;
import com.ksm.cp.Objects.Location;
import com.ksm.cp.Objects.OfferItem;
import com.ksm.cp.Objects.TabType;
import com.ksm.cp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class home extends AppCompatActivity {
    private StaggeredGridView mGridView;
    private DataAdapterEsty mAdapter;
    //private Context context;
    ArrayList<OfferItem> offerItems;
    int itemsToSkip = 0;
    EditText search_src_text;
    //ImageButton buttonclose;
    ImageButton addPosting;
    ImageButton buySellProfile;
    ImageButton userprofile;
    ImageButton searchbutton;
    ImageButton searchSettings;
    ProgressDialog dialog;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.action_bar_layout, null);
        actionBar.setCustomView(v);

        context =  getApplicationContext();

        searchbutton = (ImageButton) v.findViewById(R.id.search);
        search_src_text = (EditText) v.findViewById(R.id.search_src_text);

        search_src_text.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press

                    GetData();
                    return true;
                }
                return false;
            }
        });
        //buttonclose = (ImageButton) v.findViewById(R.id.buttonclose);
        userprofile = (ImageButton) v.findViewById(R.id.userprofile);
        addPosting = (ImageButton) v.findViewById(R.id.addPosting);
        buySellProfile = (ImageButton) v.findViewById(R.id.buySellProfile);
        searchSettings = (ImageButton) v.findViewById(R.id.searchSettings);


        searchSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.FilterActivity.class);
                startActivity(intent);
            }
        });


        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                ShowSearchBox(true);
            }
        });

        userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.UserProfile.class);
                startActivity(intent);
            }
        });

        addPosting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.item_name_desc.class);
                startActivity(intent);
            }
        });

        buySellProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.BuySellProfile.class);
                startActivity(intent);
            }
        });


       /* buttonclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowSearchBox(false);
            }
        });*/

        mGridView = (StaggeredGridView) findViewById(R.id.grid_view_home);
        GetData();


    }

    private void LoadingAnimation(final Boolean showScreen)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (dialog == null)
                    dialog = new ProgressDialog(home.this);

                dialog.setMessage("Loading");
                dialog.setCancelable(false);
                dialog.setInverseBackgroundForced(false);
                if (showScreen) {
                    dialog.show();
                } else {
                    dialog.hide();
                }
            }
        });
    }

    private void ShowSearchBox(boolean showbox) {
       if (showbox) {
           searchbutton.setVisibility(View.INVISIBLE);
           searchSettings.setVisibility(View.INVISIBLE);
           buySellProfile.setVisibility(View.INVISIBLE);
           addPosting.setVisibility(View.INVISIBLE);
           userprofile.setVisibility(View.INVISIBLE);
           //buttonclose.setVisibility(View.VISIBLE);
           search_src_text.setVisibility(View.VISIBLE);
       }
        else
       {
           searchbutton.setVisibility(View.VISIBLE);
           searchSettings.setVisibility(View.VISIBLE);
           buySellProfile.setVisibility(View.VISIBLE);
           addPosting.setVisibility(View.VISIBLE);
           userprofile.setVisibility(View.VISIBLE);
           //buttonclose.setVisibility(View.INVISIBLE);
           search_src_text.setVisibility(View.INVISIBLE);
       }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                ShowSearchBox(false);
                //mAdapter.clearData();
               // mAdapter.notifyDataSetChanged();
                //mAdapter = new DataAdapterEsty(getApplicationContext(), new ArrayList<OfferItem>(), TabType.UserProfileSelling);
                //mGridView.setAdapter(mAdapter);
                search_src_text.setText("");
                GetData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        GetData();
    }

    private void GetData() {
        LoadingAnimation(true);
        if (mAdapter != null) {
            mAdapter.clearData();
            mAdapter.notifyDataSetChanged();
        }

        offerItems = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<String, Object>();
        String filtertext = search_src_text.getText().toString();
        params.put("userId", SessionHelper.GetCurrentUser(getApplicationContext()).UserId);
        params.put("itemsToSkip", itemsToSkip);
        params.put("filtertext", filtertext);
        Filter filter = SessionHelper.GetFilerOptions(getApplicationContext());
        params.put("distance", filter.Distance);
        if (filter.location.LocationLat > 0 ) {
            params.put("lat", filter.location.LocationLat);
            params.put("lang", filter.location.LocationLng);
        }
        else
        {
            Location userLocation =  SessionHelper.GetCurrentUserLocation(getApplicationContext());
            if (userLocation.LocationLat > 0) {
                params.put("lat", userLocation.LocationLat);
                params.put("lang", userLocation.LocationLng);
            }
            else {
                Location currLocation = LocationHelper.GetLocation(getApplicationContext());
                params.put("lat", currLocation.LocationLat);
                params.put("lang", currLocation.LocationLng);
                filter.location = currLocation;
                SessionHelper.SetFilterOption(getApplicationContext(),filter);
            }
        }

        String url = "http://manishp.info/CPDataService.svc/GetAllItems";
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //String itemId = response.toString();
                        try {
                            if (response != null) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jresponse = response.getJSONObject(i);
                                    offerItems.add(OfferItemHelper.GetOfferItemFromJsonObject(jresponse));
                                }
                                mAdapter = new DataAdapterEsty(context, offerItems, TabType.UserProfileSelling);
                                mGridView.setAdapter(mAdapter);
                                LoadingAnimation(false);

                                if (offerItems.size() == 0)
                                {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                                    builder.setMessage("No Items found in your area,try changing  filter option")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //do things
                                                    Intent intent = new Intent(context, com.ksm.cp.Activity.FilterActivity.class);
                                                    startActivity(intent);
                                                    home.this.finish();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            }
                           /* */
                        } catch (JSONException e) {
                            //TVMessage.setText("Something went wrong, please try again");
                            //TVMessage.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                            LoadingAnimation(false);
                            AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                            builder.setMessage("Something went wrong, please try again")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            GetData();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                String test = error.getMessage();
                LoadingAnimation(false);
                AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                builder.setMessage("Something went wrong, please try again")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GetData();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                //pDialog.hide();
            }
        }, true);
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);
        VolleySingle.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjReq);
    }
}
