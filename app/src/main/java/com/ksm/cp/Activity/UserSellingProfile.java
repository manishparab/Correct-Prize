package com.ksm.cp.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.etsy.android.grid.StaggeredGridView;
import com.ksm.cp.Adapter.DataAdapterEsty;
import com.ksm.cp.Helper.OfferItemHelper;
import com.ksm.cp.Helper.VolleySingle;
import com.ksm.cp.Objects.CurrentUser;
import com.ksm.cp.Objects.OfferItem;
import com.ksm.cp.Objects.TabType;
import com.ksm.cp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UserSellingProfile extends AppCompatActivity {
    TextView UserName;
    OfferItem offerItem;
    private StaggeredGridView mGridView;
    private DataAdapterEsty mAdapter;
    //private Context context;
    ArrayList<OfferItem> offerItems;
    ImageView userProfilePic;
    TextView TextViewUserName;
    ProgressDialog dialog;
    TextView TextViewUserNameOfferFrom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selling_profile);
        mGridView = (StaggeredGridView) findViewById(R.id.grid_view_item_selling);
        UserName = (TextView) findViewById(R.id.TextViewUserName);
        userProfilePic = (ImageView) findViewById(R.id.userProfilePic);
        TextViewUserName = (TextView) findViewById(R.id.TextViewUserName);
        TextViewUserNameOfferFrom = (TextView) findViewById(R.id.TextViewUserNameOfferFrom);
        LoadingAnimation(true);
        offerItem =  new OfferItem();
        Bundle intenetData = getIntent().getExtras();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Item Details");

        if (intenetData !=  null)
        {
            offerItem.seller = new CurrentUser();
            offerItem.seller.DisplayName = intenetData.getString("UserName");
            offerItem.seller.ProfileImagePath =  intenetData.getString("UserProfilePic");
            offerItem.UserId = intenetData.getLong("UserId");
            GetData(offerItem.UserId);
            TextViewUserName.setText(offerItem.seller.DisplayName);
            if (!offerItem.seller.ProfileImagePath.isEmpty() && offerItem.seller.ProfileImagePath != null && !offerItem.seller.ProfileImagePath.equals("null")) {
                Picasso.with(getApplicationContext())
                        .load("http://manishp.info/" + offerItem.seller.ProfileImagePath)
                        .into(userProfilePic);
            }
            else
            {
                userProfilePic.setImageResource(R.drawable.person_placeholder);
            }

            TextViewUserNameOfferFrom.setText("Offers from  : " + offerItem.seller.DisplayName);
        }
    }

    private void GetData(long userId)
    {
        offerItems =  new ArrayList<>();
        HashMap<String, Long> params = new HashMap<String, Long>();
        params.put("userId", userId);
        String url = "http://manishp.info/CPDataService.svc/GetItemsForSell";
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                url,new JSONObject(params),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //String itemId = response.toString();
                        try {
                            if (response !=  null)
                            {
                                for(int i = 0; i < response.length(); i++) {
                                    JSONObject jresponse = response.getJSONObject(i);
                                    offerItems.add(OfferItemHelper.GetOfferItemFromJsonObject(jresponse));
                                }
                                mAdapter =  new DataAdapterEsty(getApplicationContext(),offerItems, TabType.UserProfileSelling);
                                mGridView.setAdapter(mAdapter);
                                LoadingAnimation(false);
                            }

                           /* */

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String test = error.getMessage();
                LoadingAnimation(false);
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                //pDialog.hide();
            }
        },true);
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);
        VolleySingle.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjReq);
    }

    private void LoadingAnimation(final Boolean showScreen) {

        if (dialog == null)
            dialog = new ProgressDialog(UserSellingProfile.this);

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
