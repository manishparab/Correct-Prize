package com.ksm.cp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ksm.cp.Helper.CurrentUserHelper;
import com.ksm.cp.Helper.LocationHelper;
import com.ksm.cp.Helper.SessionHelper;
import com.ksm.cp.Helper.VolleySingle;
import com.ksm.cp.Objects.CurrentUser;
import com.ksm.cp.Objects.Filter;
import com.ksm.cp.R;

import org.json.JSONObject;

public class EditActivityLocation extends AppCompatActivity {

    boolean isFromFilterActivity = false;
    TextView EditTextZipCode;
    Filter filter;
    Button Button_GetCurrentLocation;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Profile");
        setContentView(R.layout.activity_edit_activity_location);
        Button_GetCurrentLocation = (Button) findViewById(R.id.Button_GetCurrentLocation);
        Bundle extras = getIntent().getExtras();
        if (extras !=  null)
        {
            String isFilterActivity = extras.getString("FilterActivity");
            if (Integer.valueOf(isFilterActivity) == 1)
            {
                actionBar.setTitle("Filter Settings");
                Button_GetCurrentLocation.setText("Update Location");
                isFromFilterActivity = true;
            }

        }



        final CurrentUser currentUser = SessionHelper.GetCurrentUser(getApplicationContext());
        EditTextZipCode = (EditText) findViewById(R.id.EditTextZipCode);

        if (isFromFilterActivity) {
            filter = SessionHelper.GetFilerOptions(getApplicationContext());
            if (filter != null)
            {
                EditTextZipCode.setText(filter.location.PostalCode);
            }
        }
        else {
            if (currentUser.location.PostalCode != null && !currentUser.location.PostalCode.isEmpty()) {
                EditTextZipCode.setText(currentUser.location.PostalCode);
            } else {
                EditTextZipCode.setText(SessionHelper.GetCurrentLocation(getApplicationContext()).PostalCode);
            }
        }

        Button_GetCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingAnimation(true);
                String postalCode = EditTextZipCode.getText().toString();
                if (isFromFilterActivity)
                {
                    if (filter == null)
                    {
                        filter =  new Filter();
                    }
                    filter.location = LocationHelper.GetLocation(getApplicationContext(), postalCode);
                    SessionHelper.SetFilterOption(getApplicationContext(),filter);
                    Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.FilterActivity.class);
                    startActivity(intent);
                }
                else {
                    currentUser.location = LocationHelper.GetLocation(getApplicationContext(), postalCode);
                    String url = "http://manishp.info/CPDataService.svc/EditUser";
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            url, new JSONObject(CurrentUserHelper.CreateParameters(currentUser)),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(final JSONObject response) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            LoadingAnimation(false);
                                            //Intent intent;
                                            Toast.makeText(getApplicationContext(), "Location Updated", Toast.LENGTH_LONG).show();
                                            SessionHelper.SetCurrentUser(getApplicationContext(), CurrentUserHelper.GetUserFromJSON(response));
                                            //intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.UserProfile.class);
                                            //Intent intent =  new Intent(getApplicationContext(),com.ksm.cp.Activity.test.class);
                                            //startActivity(intent);
                                            onBackPressed();
                                            EditActivityLocation.this.finish();
                                        }
                                    });
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            LoadingAnimation(false);
                            String test = error.getMessage();
                        }
                    });
                    int socketTimeout = 30000;//30 seconds - change to what you want
                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    jsonObjReq.setRetryPolicy(policy);
                    VolleySingle.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjReq);
                }
            }
        });
    }

    private void LoadingAnimation(final Boolean showScreen)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (dialog == null)
                    dialog = new ProgressDialog(EditActivityLocation.this);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                onBackPressed();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
