package com.ksm.cp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.ksm.cp.Adapter.DataAdapterEsty;
import com.ksm.cp.Helper.CurrentUserHelper;
import com.ksm.cp.Helper.OfferItemHelper;
import com.ksm.cp.Helper.SessionHelper;
import com.ksm.cp.Helper.VolleySingle;
import com.ksm.cp.Objects.CurrentUser;
import com.ksm.cp.Objects.TabType;
import com.ksm.cp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EditActivityDisplayName extends AppCompatActivity {
    EditText editTextDisplayName;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Profile");
        setContentView(R.layout.activity_edit_activity_display_name);
        editTextDisplayName = (EditText) findViewById(R.id.EditTextDisplayName);
        final CurrentUser currentUser = SessionHelper.GetCurrentUser(getApplicationContext());
        editTextDisplayName.setText(currentUser.DisplayName);
        Button buttonEditDisplayName = (Button) findViewById(R.id.ButtonEditDisplayName);
        buttonEditDisplayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingAnimation(true);
                currentUser.DisplayName = editTextDisplayName.getText().toString();
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
                                       Toast.makeText(getApplicationContext(),"Display Name Updated",Toast.LENGTH_LONG).show();
                                       SessionHelper.SetCurrentUser(getApplicationContext(), CurrentUserHelper.GetUserFromJSON(response));
                                       //Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.UserProfile.class);
                                       //Intent intent =  new Intent(getApplicationContext(),com.ksm.cp.Activity.test.class);
                                       //startActivity(intent);
                                       onBackPressed();
                                       EditActivityDisplayName.this.finish();
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
        });
    }

    private void LoadingAnimation(final Boolean showScreen)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (dialog == null)
                    dialog = new ProgressDialog(EditActivityDisplayName.this);

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
