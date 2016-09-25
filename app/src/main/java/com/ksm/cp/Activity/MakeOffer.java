package com.ksm.cp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ksm.cp.Helper.CurrentUserHelper;
import com.ksm.cp.Helper.SessionHelper;
import com.ksm.cp.Helper.VolleySingle;
import com.ksm.cp.Objects.CurrentUser;
import com.ksm.cp.Objects.OfferItem;
import com.ksm.cp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MakeOffer extends AppCompatActivity {

    OfferItem offerItem;
    EditText EditText_MakeOffer;
    Context context;
    ProgressDialog dialog;
    CurrentUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_offer);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Item");

        currentUser = SessionHelper.GetCurrentUser(getApplicationContext());

        ImageView imageView = (ImageView) findViewById(R.id.MakeOfferImageview);
        imageView.setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
        TextView itemName = (TextView) findViewById(R.id.MakeOffer_TextView_ItemName);
        Button button_MakeOffer = (Button) findViewById(R.id.Button_activity_MakeOffer);
        EditText_MakeOffer = (EditText) findViewById(R.id.MakeOffer_EditText_MakeOffer);
        context = getApplicationContext();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            offerItem = new OfferItem();
            offerItem.ItemId = extras.getLong("ItemId");
            offerItem.UserId = extras.getLong("UserId");
            offerItem.ImagePaths = extras.getStringArrayList("ItemImagePath");
            offerItem.Name = extras.getString("ItemName");

            itemName.setText("Whats your correct price for " + offerItem.Name);

            Picasso.with(getApplicationContext())
                    .load("http://manishp.info/" + offerItem.ImagePaths.get(0))
                    .into(imageView);
        }

        button_MakeOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingAnimation(true);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        params.put("Amount", Float.valueOf(EditText_MakeOffer.getText().toString()));
                        params.put("UserFromId", currentUser.UserId);
                        params.put("ItemId", offerItem.ItemId);
                        String url = "http://manishp.info/CPDataService.svc/MakeOffer";
                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                url, new JSONObject(params),
                                new Response.Listener<JSONObject>() {
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String messageId = response.getString("Id");
                                            SendMessage(messageId);
                                            LoadingAnimation(false);
                                            onBackPressed();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                String test = error.getMessage();
                                LoadingAnimation(false);
                                //progress.dismiss();
                                //VolleyLog.d(TAG, "Error: " + error.getMessage());
                                // hide the progress dialog
                                //pDialog.hide();
                            }
                        });

                        // Adding request to request queue
                        int socketTimeout = 30000;//30 seconds - change to what you want
                        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        jsonObjReq.setRetryPolicy(policy);
                        VolleySingle.getInstance(context).getRequestQueue().add(jsonObjReq);
                    }
                });
            }
        });


    }

    public void SendMessage(final String offerId) {

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("UserTo", offerItem.UserId);
        params.put("UserFromId", currentUser.UserId);
        params.put("ItemId", offerItem.ItemId);
        params.put("OfferItemId", Long.valueOf(offerId));
        params.put("message", "I will buy it for $" + EditText_MakeOffer.getText().toString());
        String url = "http://manishp.info/CPDataService.svc/SendMessage";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String test = error.getMessage();
                LoadingAnimation(false);
                //progress.dismiss();
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                //pDialog.hide();
            }
        });

        // Adding request to request queue
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);
        VolleySingle.getInstance(context).getRequestQueue().add(jsonObjReq);

    }

    private void LoadingAnimation(final Boolean showScreen) {
        if (dialog == null)
            dialog = new ProgressDialog(MakeOffer.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        if (showScreen) {
            dialog.show();
        } else {
            dialog.hide();
        }


    }

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
