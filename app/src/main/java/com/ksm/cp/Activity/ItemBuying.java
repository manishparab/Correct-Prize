package com.ksm.cp.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.etsy.android.grid.StaggeredGridView;
import com.ksm.cp.Adapter.DataAdapterEsty;
import com.ksm.cp.Helper.OfferItemHelper;
import com.ksm.cp.Helper.SessionHelper;
import com.ksm.cp.Helper.VolleySingle;
import com.ksm.cp.Objects.CurrentUser;
import com.ksm.cp.Objects.OfferItem;
import com.ksm.cp.Objects.TabType;
import com.ksm.cp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemBuying extends android.support.v4.app.Fragment {

    private StaggeredGridView mGridView;
    private DataAdapterEsty mAdapter;
    private Context context;
    ArrayList<OfferItem> offerItems;
    CurrentUser currentUser;

    public ItemBuying()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_item_buying, container, false);
        mGridView = (StaggeredGridView) v.findViewById(R.id.grid_view_item_buying);
        offerItems =  new ArrayList<>();
        context = getContext();
        currentUser = SessionHelper.GetCurrentUser(context);
        PopulateData();
        return v;
    }

    private void PopulateData()
    {
        if (offerItems.size() ==0) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("userId", String.valueOf(currentUser.UserId));
            String url = "http://manishp.info/CPDataService.svc/GetItemsBuying";
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
                                    mAdapter = new DataAdapterEsty(context, offerItems, TabType.Buying);
                                    mGridView.setAdapter(mAdapter);
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
                    //VolleyLog.d(TAG, "Error: " + error.getMessage());
                    // hide the progress dialog
                    //pDialog.hide();
                }
            }, true);
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjReq.setRetryPolicy(policy);
            VolleySingle.getInstance(this.context).getRequestQueue().add(jsonObjReq);
        }
        else
        {
            mAdapter = new DataAdapterEsty(context, offerItems, TabType.Buying);
            mGridView.setAdapter(mAdapter);
        }
    }

}
