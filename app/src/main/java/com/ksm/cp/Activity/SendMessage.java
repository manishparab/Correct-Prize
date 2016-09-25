package com.ksm.cp.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;
import com.ksm.cp.Helper.SessionHelper;
import com.ksm.cp.Helper.VolleySingle;
import com.ksm.cp.Objects.CurrentUser;
import com.ksm.cp.Objects.OfferItem;
import com.ksm.cp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class SendMessage extends AppCompatActivity {

    OfferItem offerItem;
    CurrentUser currentUser;
    TextView SendMessage_ToOwnerName;
    ImageView SendMessage_ToUserImage;
    EditText messageContent;
    ProgressDialog dialog;
    //String messageContentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Item");

        Button sendMessage = (Button) findViewById(R.id.Button_activity_SendMessage);
        messageContent = (EditText) findViewById(R.id.SendMessage_MessageContent);
        SendMessage_ToUserImage = (ImageView) findViewById(R.id.SendMessage_ToUserImage);
        SendMessage_ToOwnerName = (TextView) findViewById(R.id.SendMessage_ToOwnerName);
        currentUser = SessionHelper.GetCurrentUser(getApplicationContext());
        offerItem = new OfferItem();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            offerItem.ItemId = extras.getLong("ItemId");
            offerItem.UserId = extras.getLong("UserId");
            offerItem.seller = new CurrentUser();
            offerItem.seller.DisplayName = extras.getString("UserName");
            offerItem.seller.ProfileImagePath = extras.getString("UserProfilePic");
            if (!offerItem.seller.ProfileImagePath.isEmpty() && offerItem.seller.ProfileImagePath != null && !offerItem.seller.ProfileImagePath.equals("null")) {
                Picasso.with(getApplicationContext())
                        .load("http://manishp.info/" + offerItem.seller.ProfileImagePath)
                        .into(SendMessage_ToUserImage);
            }
            else
            {
                SendMessage_ToUserImage.setImageResource(R.drawable.person_placeholder);
            }
            SendMessage_ToOwnerName.setText("Send message to " + offerItem.seller.DisplayName);
        }

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingAnimation(true);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("UserTo", String.valueOf(offerItem.UserId));
                        params.put("UserFromId", String.valueOf(currentUser.UserId));
                        params.put("ItemId", String.valueOf(offerItem.ItemId));
                        params.put("message", messageContent.getText().toString());
                        String url = "http://manishp.info/CPDataService.svc/SendMessage";
                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                url, new JSONObject(params),
                                new Response.Listener<JSONObject>() {
                                    public void onResponse(JSONObject response) {
                                        LoadingAnimation(false);
                                        Toast.makeText(getApplicationContext(), "Message sent successfully", Toast.LENGTH_SHORT).show();
                                        onBackPressed();
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
                        VolleySingle.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjReq);
                    }
                });

            }
        });


    }

    private void LoadingAnimation(final Boolean showScreen) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (dialog == null)
                    dialog = new ProgressDialog(SendMessage.this);

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
