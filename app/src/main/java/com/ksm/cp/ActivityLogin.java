package com.ksm.cp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ksm.cp.Helper.CurrentUserHelper;
import com.ksm.cp.Helper.LocationHelper;
import com.ksm.cp.Helper.SessionHelper;
import com.ksm.cp.Helper.VolleySingle;
import com.ksm.cp.Objects.CurrentUser;
import com.ksm.cp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityLogin extends Activity {

    Context context;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        context = getApplicationContext();
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.ActivitySignup.class);
                startActivity(intent);
            }
        });

        Button Login = (Button) findViewById(R.id.email_sign_in_button);
        Login.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         LoadingAnimation(true);
                                         runOnUiThread(new Runnable() {
                                                           @Override
                                                           public void run() {
                                                               String authtoken = ((TextView) findViewById(R.id.emailAddress)).getText().toString();
                                                               String password = ((TextView) findViewById(R.id.password)).getText().toString();
                                                               String url = "http://manishp.info/CPDataService.svc/AuthenticateUser?authToken=" + authtoken + "&Password=" + password;

                                                               JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                                                                       url, null,
                                                                       new Response.Listener<JSONObject>() {
                                                                           @Override
                                                                           public void onResponse(final JSONObject response) {

                                                                               CurrentUser currentUser = CurrentUserHelper.GetUserFromJSON(response);
                                                                               SessionHelper.SetCurrentUser(context, currentUser);
                                                                               if (currentUser.location.PostalCode != null && !currentUser.location.PostalCode.isEmpty()) {
                                                                                   SessionHelper.SetCurrentUserLocation(context, LocationHelper.GetLocation(context));
                                                                                   //SessionHelper.SetCurrentLocation(getApplicationContext(), LocationHelper.GetLocation(getApplicationContext()));
                                                                               }
                                                                               LoadingAnimation(false);
                                                                               Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show();
                                                                               Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.home.class);
                                                                               startActivity(intent);
                                                                               ActivityLogin.this.finish();

                                                                           }
                                                                           //currentUser.ItemId = response.getLong("ItemId");
                                                                       }
                                                                       , new Response.ErrorListener() {
                                                                   @Override
                                                                   public void onErrorResponse(VolleyError error) {
                                                                       String test = error.getMessage();
                                                                       LoadingAnimation(false);
                                                                       Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show();
                                                                       //VolleyLog.d(TAG, "Error: " + error.getMessage());
                                                                       // hide the progress dialog
                                                                       //pDialog.hide();
                                                                   }
                                                               }

                                                               );
                                                               VolleySingle.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjReq);
                                                           }
                                                       }
                                         );

                                     }
                                 }
        );

        Button btnStartActivity = (Button) findViewById(R.id.Button_activity_login_startPhotoUpload);
        btnStartActivity.setOnClickListener(new View.OnClickListener()

                                            {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.item_name_desc.class);
                                                    //Intent intent =  new Intent(getApplicationContext(),com.ksm.cp.Activity.test.class);
                                                    startActivity(intent);
                                                }
                                            }

        );

        Button buySellProfile = (Button) findViewById(R.id.Button_activity_login_BuySellProfile);
        buySellProfile.setOnClickListener(new View.OnClickListener()

                                          {
                                              @Override
                                              public void onClick(View v) {
                                                  Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.BuySellProfile.class);
                                                  //Intent intent =  new Intent(getApplicationContext(),com.ksm.cp.Activity.test.class);
                                                  startActivity(intent);
                                              }
                                          }

        );

        Button messageThread = (Button) findViewById(R.id.Button_activity_login_MessageThread);
        messageThread.setOnClickListener(new View.OnClickListener()

                                         {
                                             @Override
                                             public void onClick(View v) {
                                                 Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.ChatMessageThread.class);
                                                 //Intent intent =  new Intent(getApplicationContext(),com.ksm.cp.Activity.test.class);
                                                 startActivity(intent);
                                             }
                                         }

        );

        Button viewProfile = (Button) findViewById(R.id.Button_activity_login_ViewProfile);
        viewProfile.setOnClickListener(new View.OnClickListener()

                                       {
                                           @Override
                                           public void onClick(View v) {
                                               Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.UserProfile.class);
                                               //Intent intent =  new Intent(getApplicationContext(),com.ksm.cp.Activity.test.class);
                                               startActivity(intent);
                                           }
                                       }

        );

        Button Button_activity_home = (Button) findViewById(R.id.Button_activity_home);
        Button_activity_home.setOnClickListener(new View.OnClickListener()

                                                {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.home.class);
                                                        //Intent intent =  new Intent(getApplicationContext(),com.ksm.cp.Activity.test.class);
                                                        startActivity(intent);
                                                    }
                                                }

        );


        Button Button_activity_filter = (Button) findViewById(R.id.Button_activity_filter);
        Button_activity_filter.setOnClickListener(new View.OnClickListener()

                                                  {
                                                      @Override
                                                      public void onClick(View v) {
                                                          Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.FilterActivity.class);
                                                          //Intent intent =  new Intent(getApplicationContext(),com.ksm.cp.Activity.test.class);
                                                          startActivity(intent);
                                                      }
                                                  }

        );

        CurrentUser currentUser = SessionHelper.GetCurrentUser(getApplicationContext());
        if (currentUser.UserId != 0)
        {
            Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.home.class);
            startActivity(intent);
            ActivityLogin.this.finish();
        }

    }

    private void LoadingAnimation(final Boolean showScreen) {

        if (dialog == null)
            dialog = new ProgressDialog(ActivityLogin.this);

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
