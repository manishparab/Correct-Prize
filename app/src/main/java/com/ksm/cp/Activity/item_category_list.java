package com.ksm.cp.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ksm.cp.Helper.VolleySingle;
import com.ksm.cp.Objects.ItemCategory;
import com.ksm.cp.Objects.OfferItem;
import com.ksm.cp.R;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;

public class item_category_list extends Activity {
    ItemCategory[] itemCategories;
    ListView listView;
    OfferItem offerItem;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progress = ProgressDialog.show(this, "Loding Item Categories",
                "Loading data", true);
        Bundle extras = getIntent().getExtras();
        if (extras !=  null)
        {
           String itemJson = extras.getString("item");
            offerItem = new Gson().fromJson(itemJson, OfferItem.class);
        }
        setContentView(R.layout.activity_item_category_list);
        listView = (ListView)findViewById(R.id.ListViewCategories);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String item_category_name = ((TextView)view.findViewById(R.id.TextViewItemCategoryName)).getText().toString();
                String item_category_id = ((TextView)view.findViewById(R.id.TextViewItemCategoryId)).getText().toString();
                offerItem.Category = Integer.valueOf(item_category_id);
                //offerItem.itemCategoryName = item_category_name;
                Intent intent = new Intent(item_category_list.this, item_name_desc.class);
                String stringJson = new Gson().toJson(offerItem);
                intent.putExtra("item",stringJson);
                startActivity(intent);
            }
        });
        PopulateCategories();
    }

    public void onClick(View v) {
        Toast.makeText(v.getContext(),"manish", Toast.LENGTH_SHORT);
    }

    private void PopulateCategories()
    {
        String url = "http://manishp.info/CPDataService.svc/GetItemCategory";
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    public void onResponse(JSONArray response) {
                        itemCategories =  new Gson().fromJson(response.toString(),ItemCategory[].class);
                        listView.setAdapter(new ListViewCategoryAdapter());
                        progress.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String test = error.getMessage();
                progress.dismiss();
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                //pDialog.hide();
            }
        });

// Adding request to request queue
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);
        VolleySingle.getInstance(this).getRequestQueue().add(jsonObjReq);
    }



    public class ListViewCategoryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return itemCategories.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View workingview;
            if (convertView ==  null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                workingview = inflater.inflate(R.layout.item_category_list_item, parent,false);
            }
            else
            {
                workingview = convertView;
            }

            TextView textViewItemCategoryName = (TextView) workingview.findViewById(R.id.TextViewItemCategoryName);
            TextView textViewItemCategoryId = (TextView) workingview.findViewById(R.id.TextViewItemCategoryId);
            ItemCategory item =  itemCategories[position];
            textViewItemCategoryName.setText(item.Name);
            textViewItemCategoryId.setText(String.valueOf(item.Id));
            return workingview;

        }

    }

}
