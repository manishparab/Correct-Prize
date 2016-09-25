package com.ksm.cp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivitySignup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button signUp_button = (Button)findViewById(R.id.signUp_button);
        signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });
    }

    private void SignUp()
    {
        String email = ((EditText)findViewById(R.id.Email)).getText().toString();
        String displayName = ((EditText)findViewById(R.id.displayName)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();
        //String jsonString =  GetJSON(email, displayName, password);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("DisplayName",displayName);
        params.put("Email",email);
        params.put("Password",password);
        String url = "http://manishp.info/CPDataService.svc/RegisterUser";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url,new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CurrentUser currentUser= CurrentUserHelper.GetUserFromJSON(response);
                        if (!currentUser.IsEmailAlreadyRegistered) {
                            Toast.makeText(getApplicationContext(),"Registration successful", Toast.LENGTH_SHORT).show();
                            SessionHelper.SetCurrentUser(getApplicationContext(), currentUser);
                            if (currentUser.location.PostalCode != null && !currentUser.location.PostalCode.isEmpty()) {
                                SessionHelper.SetCurrentLocation(getApplicationContext(), LocationHelper.GetLocation(getApplicationContext()));
                                SessionHelper.SetCurrentUserLocation(getApplicationContext(), LocationHelper.GetLocation(getApplicationContext()));
                            }
                            Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.home.class);
                            //Intent intent =  new Intent(getApplicationContext(),com.ksm.cp.Activity.test.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Email Address is already used",Toast.LENGTH_SHORT).show();
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
        });
        VolleySingle.getInstance(this).getRequestQueue().add(jsonObjReq);

    }

    private String GetJSON(String email, String displayName, String password)
    {
        JSONObject jsonObject =  new JSONObject();
        try {
            jsonObject.put("Email",email);
            jsonObject.put("Displayname",displayName);
            jsonObject.put("Password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsonObject.toString();
    }
}
