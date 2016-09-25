package com.ksm.cp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ksm.cp.Adapter.AdapterListViewMessageThread;
import com.ksm.cp.Adapter.DataAdapterEsty;
import com.ksm.cp.Helper.ChatMessageHelper;
import com.ksm.cp.Helper.VolleySingle;
import com.ksm.cp.Objects.ChatMessage;
import com.ksm.cp.Objects.OfferItem;
import com.ksm.cp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatMessageThread extends AppCompatActivity {

    Context context;
    ArrayList<ChatMessage> messages;
    ListView listView;
    String messageId;
    AdapterListViewMessageThread adapter;
    Long toUserId, fromUserId, itemId;
    ProgressDialog dialog;
    Button ButtonSendMessage;
    EditText EditChatMessageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat_message_thread);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        LoadingAnimation(true);

        listView = (ListView) findViewById(R.id.list_view_messages);
        context = getApplicationContext();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            toUserId = extras.getLong("toUserId");
            fromUserId = extras.getLong("fromUserId");
            itemId = extras.getLong("itemId");
        }
        PopulateData();

        ButtonSendMessage = (Button)findViewById(R.id.ButtonSendMessage);
        EditChatMessageText = (EditText)findViewById(R.id.EditChatMessageText);

        ButtonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingAnimation(true);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("UserTo", String.valueOf(toUserId));
                        params.put("UserFromId", String.valueOf(fromUserId));
                        params.put("ItemId", String.valueOf(itemId));
                        params.put("message", EditChatMessageText.getText().toString());
                        String url = "http://manishp.info/CPDataService.svc/SendMessage";
                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                url, new JSONObject(params),
                                new Response.Listener<JSONObject>() {
                                    public void onResponse(JSONObject response) {
                                        LoadingAnimation(false);
                                        Toast.makeText(getApplicationContext(), "Message sent successfully", Toast.LENGTH_SHORT).show();
                                        EditChatMessageText.setText("");
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

    private void PopulateData() {
        messages = new ArrayList<>();
        HashMap<String, Long> params = new HashMap();
        params.put("toUserId", toUserId);
        params.put("fromUserId", fromUserId);
        params.put("itemId",itemId);
        String url = "http://manishp.info/CPDataService.svc/GetMessagthread";
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
                                    messages.add(ChatMessageHelper.GetChatMessageFromJSONObject(jresponse));
                                }
                                adapter = new AdapterListViewMessageThread(context, messages);
                                listView.setAdapter(adapter);
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
        }, true);
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);
        VolleySingle.getInstance(this.context).getRequestQueue().add(jsonObjReq);
    }

    private void LoadingAnimation(final Boolean showScreen) {

        if (dialog == null)
            dialog = new ProgressDialog(ChatMessageThread.this);

        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        if (showScreen) {
            dialog.show();
        } else {
            dialog.hide();
        }

    }
}
