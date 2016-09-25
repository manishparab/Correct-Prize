package com.ksm.cp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.ksm.cp.Adapter.AdapterListViewChatMessage;
import com.ksm.cp.Adapter.DataAdapterEsty;
import com.ksm.cp.Helper.ChatMessageHelper;
import com.ksm.cp.Helper.CurrentUserHelper;
import com.ksm.cp.Helper.OfferItemHelper;
import com.ksm.cp.Helper.SessionHelper;
import com.ksm.cp.Helper.VolleySingle;
import com.ksm.cp.Objects.ChatMessage;
import com.ksm.cp.Objects.CurrentUser;
import com.ksm.cp.Objects.TabType;
import com.ksm.cp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class messages_item_sell extends AppCompatActivity {

    CurrentUser currentUser;
    long ItemId;
    ArrayList<ChatMessage> chatMessages;
    ListView ListViewChatMessage ;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_item_sell);
        currentUser = SessionHelper.GetCurrentUser(getApplicationContext());
        ListViewChatMessage = (ListView)findViewById(R.id.ListViewChatMessage);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        LoadingAnimation(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ItemId = extras.getLong("itemId");
        }
        PopulateData();

        ListViewChatMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                TextView extraData =  (TextView)view.findViewById(R.id.TextviewExtraData);
                String[] itemDataSplit = extraData.getText().toString().split("æ");
                Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.ChatMessageThread.class);
                intent.putExtra("toUserId", Long.parseLong(itemDataSplit[0]) );
                intent.putExtra("fromUserId", Long.parseLong(itemDataSplit[1]) );
                intent.putExtra("itemId" , Long.parseLong(itemDataSplit[2]));
                startActivity(intent);
            }
        });

    }

    private void PopulateData() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("toUserId", String.valueOf(currentUser.UserId));
        params.put("itemId", ItemId);
        String url = "http://manishp.info/CPDataService.svc/GetMessagesForItemSelling";
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        chatMessages =  new ArrayList<>();
                        try {
                            if (response != null) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jresponse = response.getJSONObject(i);
                                    chatMessages.add(ChatMessageHelper.GetChatMessageFromJSONObject(jresponse));
                                }
                                AdapterListViewChatMessage adapter =
                                        new AdapterListViewChatMessage(getApplicationContext(),chatMessages);
                                ListViewChatMessage.setAdapter(adapter);
                                LoadingAnimation(false);
                            }
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
        }, true);
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);
        VolleySingle.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjReq);
    }

    private void LoadingAnimation(final Boolean showScreen) {

        if (dialog == null)
            dialog = new ProgressDialog(messages_item_sell.this);

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

