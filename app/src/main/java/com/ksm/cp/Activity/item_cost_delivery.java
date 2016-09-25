package com.ksm.cp.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ksm.cp.Helper.GPSTracker;
import com.ksm.cp.Helper.LocationHelper;
import com.ksm.cp.Helper.SessionHelper;
import com.ksm.cp.Helper.VolleySingle;
import com.ksm.cp.Objects.CurrentUser;
import com.ksm.cp.Objects.Location;
import com.ksm.cp.Objects.OfferItem;
import com.ksm.cp.R;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class item_cost_delivery extends AppCompatActivity {
    OfferItem offerItem;
    EditText EditTextPrice;
    EditText EditTextZipCode;
    RadioGroup togglePrice;
    RadioButton radioYesPrice;
    RadioGroup toggleDeliveryOption;
    RadioButton radioYes;
    DiscreteSeekBar seekBar;
    double latitude;
    double longitude;
    String itemMode;
    ArrayList<String> imagepaths;
    ProgressDialog dialog;
    CurrentUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_cost_delivery);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        seekBar = (DiscreteSeekBar) findViewById(R.id.SeekBarItemCondition);
        final TextView TextViewItemCondition = (TextView) findViewById(R.id.TextViewItemCondition);

        EditTextZipCode = (EditText) findViewById(R.id.EditTextZipCode);
        EditTextPrice = (EditText) findViewById(R.id.EditTextPrice);
        togglePrice = (RadioGroup) findViewById(R.id.togglePrice);
        toggleDeliveryOption = (RadioGroup) findViewById(R.id.toggleDeliveryOption);
        radioYesPrice = (RadioButton) findViewById(R.id.radioYesPrice);
        radioYes = (RadioButton) findViewById(R.id.radioYes);

        offerItem = new OfferItem();
        this.PopulateOfferItemFromIntent(getIntent());
        if (offerItem.Cost > 0) {
            EditTextPrice.setText(String.valueOf(offerItem.Cost));
        }
        EditTextZipCode.setText(offerItem.PostalCode);

        if (offerItem.IsNegotiable) {
            radioYesPrice.setChecked(true);
        }

        if (offerItem.DeliveryType) {
            radioYes.setChecked(true);
        }

        seekBar.setProgress(offerItem.Condition);


        if (offerItem.GPSlat <= 0) {
            Location currentLocationFromSession = SessionHelper.GetCurrentUserLocation(getApplicationContext());
            if (currentLocationFromSession.PostalCode != null && !currentLocationFromSession.PostalCode.isEmpty()) {
                EditTextZipCode.setText(currentLocationFromSession.PostalCode);
                latitude = currentLocationFromSession.LocationLat;
                longitude = currentLocationFromSession.LocationLng;
            } else {
                runOnUiThread(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EditTextZipCode.setText(SetPinCode());
                        //EditTextZipCode.setText("manish");
                    }
                }));
            }
        } else {
            //find zip code using lat and lang
            //EditTextZipCode.setText
        }

        ImageView ImageViewRefreshLocation = (ImageView) findViewById(R.id.ImageViewRefreshLocation);
        final EditText EditTextZipCode = (EditText) findViewById(R.id.EditTextZipCode);
        ImageViewRefreshLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EditTextZipCode.setText(SetPinCode());
                        //EditTextZipCode.setText("manish");
                    }
                }));

            }
        });

        Button button = (Button) findViewById(R.id.Button_activity_cost_delivery_next);
        if (itemMode.equals("Edit")) {
            button.setText("Update Offer");
        }

        //runOnUiThread(new Thread(new Runnable() {
        // @Override
        //public void run() {
        //EditText EditTextZipCode = (EditText) findViewById(R.id.EditTextZipCode);
        //EditTextZipCode.setText(SetPinCode());
        //}
        //}));

        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                final int progressvalue = value;
                runOnUiThread(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressvalue == 1) {
                            TextViewItemCondition.setText("Used");
                        }
                        if (progressvalue == 2) {
                            TextViewItemCondition.setText("Open Box");
                        }
                        if (progressvalue == 3) {
                            TextViewItemCondition.setText("Certified/Reconditioned");
                        }
                        if (progressvalue == 4) {
                            TextViewItemCondition.setText("New");
                        }
                    }
                }));
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoadingAnimation(true);
                //final String userId = "6";
                PopulateItem();
               /* EditText EditTextPrice = (EditText    QA) findViewById(R.id.EditTextPrice);
                Switch switchPricefirm = (Switch)findViewById(R.id.SwitchPricefirm);
                Switch switchDeliveryOption = (Switch)findViewById(R.id.SwitchDeliveryOption);

                offerItem.canDeliver = switchDeliveryOption.isChecked();
                offerItem.firmOnCost = switchPricefirm.isChecked();*/

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("UserId", String.valueOf(offerItem.UserId));
                params.put("Name", offerItem.Name);
                params.put("Description", offerItem.Description);
                params.put("Cost", String.valueOf(offerItem.Cost));
                params.put("IsNegotiable", String.valueOf(offerItem.IsNegotiable));
                params.put("LocationLat", String.valueOf(latitude)); // check for null lattitute
                params.put("LocationLong", String.valueOf(longitude));
                params.put("LocationName", offerItem.GPSLocation);
                params.put("DeliveryType", String.valueOf(offerItem.DeliveryType));
                params.put("PostalCode", offerItem.PostalCode);
                params.put("Category", String.valueOf(offerItem.Category));
                params.put("Condition", String.valueOf(offerItem.Condition));

                String url = "http://manishp.info/CPDataService.svc/SaveItem";
                if (itemMode.equals("Edit")) {
                    params.put("ItemId", String.valueOf(offerItem.ItemId));
                    url = "http://manishp.info/CPDataService.svc/EditItem";
                }

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String itemId = null;
                                try {
                                    itemId = response.getString("Id");
                                    PostImages(itemId);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
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
                });
                VolleySingle.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjReq);
                // Save images using Ftp
            }
        });

        currentUser = SessionHelper.GetCurrentUser(getApplicationContext());

    }

    private void LoadingAnimation(Boolean showScreen) {
        if (dialog == null)
            dialog = new ProgressDialog(item_cost_delivery.this);

        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        if (showScreen) {
            dialog.show();
        } else {
            dialog.hide();
        }
    }

    private void PostImages(final String itemId) {
        final String userId = String.valueOf(offerItem.UserId);
        final ArrayList<Bitmap> images = item_name_desc.arrayList;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    imagepaths = new ArrayList<>();

                    FTPClient ftpClient = new FTPClient();
                    //ftpClient.enterLocalPassiveMode();
                    //ftpClient.setConnectTimeout(5);
                    ftpClient.connect("ftp.manishp.info");
                    ftpClient.login("manishpi", "network1@3");
                           /* String[] replies = ftpClient.getReplyStrings();
                            if (replies != null && replies.length > 0) {
                                for (String aReply : replies) {
                                    System.out.println("SERVER: " + aReply);
                                }
                            }*/
                    //ftpClient.changeWorkingDirectory(serverRoad);

                    Boolean directoryExists = ftpClient.changeWorkingDirectory("images/" + userId + "/" + itemId);
                    if (directoryExists) {
                        if (itemMode.equals("Edit")) {
                            ftpClient.removeDirectory("images/" + userId + "/" + itemId);
                            directoryExists = false;
                        }
                    }
                    if (!directoryExists) {
                        Boolean parentDirectoryExists = ftpClient.changeWorkingDirectory("images/" + userId);
                        if (!parentDirectoryExists) {
                            ftpClient.makeDirectory("images/" + userId);
                            ftpClient.changeWorkingDirectory("images/" + userId);
                        }
                        ftpClient.makeDirectory(itemId);
                              /*String[] replies = ftpClient.getReplyStrings();
                                if (replies != null && replies.length > 0) {
                                    for (String aReply : replies) {
                                        System.out.println("SERVER: " + aReply);
                                    }
                                }*/
                        ftpClient.changeWorkingDirectory(itemId);
                    }


                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    BufferedInputStream buffIn = null;
                    for (int i = 0; i < images.size(); i++) {
                        Bitmap bitmap = images.get(i);
                        String fileName = String.valueOf(i) + ".png";
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                        byte[] bitmapdata = bos.toByteArray();
                        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
                        buffIn = new BufferedInputStream(bs);
                        ftpClient.enterLocalPassiveMode();
                        ftpClient.storeFile(fileName, buffIn);
                        buffIn.close();
                        imagepaths.add("images/" + userId + "/" + itemId + "/" + fileName);
                    }
                    ftpClient.logout();
                    ftpClient.disconnect();


                    if (itemMode.equals("Edit")) {
                        HashMap<String, Long> paramsForDelete = new HashMap<String, Long>();
                        paramsForDelete.put("itemId", offerItem.ItemId);

                        String url = "http://manishp.info/CPDataService.svc/DeleteItemImages";
                        JsonObjectRequest jsonObjReqDelete = new JsonObjectRequest(Request.Method.POST,
                                url, new JSONObject(paramsForDelete),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        // On successful delete on image entries for item insert new entries
                                        HashMap<String, String> params = new HashMap<String, String>();
                                        params.put("UserId", userId);
                                        params.put("ItemId", itemId);
                                        for (int i = 1; i <= imagepaths.size(); i++) {
                                            params.put("ItemImage" + i, imagepaths.get(i - 1));
                                        }
                                        String url = "http://manishp.info/CPDataService.svc/SaveItemImages";
                                        JsonObjectRequest jsonObjReqSaveItemImages = new JsonObjectRequest(Request.Method.POST,
                                                url, new JSONObject(params),
                                                new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        Toast.makeText(getApplicationContext(), "Offer Updated successfully", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.home.class);
                                                        startActivity(intent);
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
                                        VolleySingle.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjReqSaveItemImages);
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
                        VolleySingle.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjReqDelete);
                    }

                    if (!itemMode.equals("Edit")) {
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("UserId", userId);
                        params.put("ItemId", itemId);
                        for (int i = 1; i <= imagepaths.size(); i++) {
                            params.put("ItemImage" + i, imagepaths.get(i - 1));
                        }
                        String url = "http://manishp.info/CPDataService.svc/SaveItemImages";
                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                url, new JSONObject(params),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        LoadingAnimation(false);
                                        Toast.makeText(getApplicationContext(), "Offer posted successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.home.class);
                                        startActivity(intent);
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
                        });
                        VolleySingle.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjReq);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void PopulateOfferItemFromIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            offerItem.UserId = extras.getLong("UserId");
            //offerItem.UserName = extras.getString("UserName");
            offerItem.Name = extras.getString("Name");
            offerItem.Cost = extras.getDouble("Cost");
            offerItem.Description = extras.getString("Description");
            offerItem.IsNegotiable = extras.getBoolean("IsNegotiable");
            offerItem.PostalCode = extras.getString("PostalCode");
            offerItem.DeliveryType = extras.getBoolean("DeliveryType");
            offerItem.DeliveryTypeDesc = extras.getString("DeliveryTypeDesc");
            offerItem.IsSold = Boolean.valueOf("IsSold");
            offerItem.Category = extras.getInt("Category");
            offerItem.CategoryDesc = extras.getString("CategoryDesc");
            offerItem.Condition = extras.getInt("Condition");
            offerItem.ConditionDesc = extras.getString("ConditionDesc");
            offerItem.GPSlat = extras.getDouble("GPSlat");
            offerItem.GPSLong = extras.getDouble("GPSLong");
            latitude = offerItem.GPSlat;
            longitude = offerItem.GPSLong;
            offerItem.PostedDateString = extras.getString("PostedDateString");
            offerItem.GPSLocation = extras.getString("LocationName");
            offerItem.ItemId = extras.getLong("ItemId");
            //Set itemMode
            itemMode = extras.getString("Mode");
        }
    }

    public Intent PopulateIntentWithData(Intent intent) {
        intent.putExtra("Name", offerItem.Name);
        intent.putExtra("Cost", offerItem.Cost);
        intent.putExtra("Description", offerItem.Description);
        intent.putExtra("IsNegotiable", offerItem.IsNegotiable);
        intent.putExtra("PostalCode", offerItem.PostalCode);
        intent.putExtra("DeliveryType", offerItem.DeliveryType);
        intent.putExtra("Category", offerItem.Category);
        intent.putExtra("Condition", offerItem.Condition);
        intent.putExtra("GPSlat", offerItem.GPSlat);
        intent.putExtra("GPSLong", offerItem.GPSLong);

        return intent;
    }

    private void PopulateItem() {
        offerItem.Cost = Double.valueOf(EditTextPrice.getText().toString());
        offerItem.PostalCode = EditTextZipCode.getText().toString();
        int togglePriceid = togglePrice.getCheckedRadioButtonId();
        int toggleCanDeliverid = toggleDeliveryOption.getCheckedRadioButtonId();
        if (togglePriceid == R.id.radioYesPrice) {
            offerItem.IsNegotiable = true;
        } else {
            offerItem.IsNegotiable = false;
        }

        if (toggleCanDeliverid == R.id.radioYes) {
            offerItem.DeliveryType = true;
        } else {
            offerItem.DeliveryType = false;
        }

        offerItem.Condition = seekBar.getProgress();
        offerItem.PostalCode = EditTextZipCode.getText().toString();
    }

    private String SetPinCode() {
        String pinCode = "";
        Location location = SessionHelper.GetCurrentLocation(getApplicationContext());
        pinCode = location.PostalCode;
        latitude = location.LocationLat;
        longitude = location.LocationLng;
        offerItem.GPSLocation = location.LocationName;
        //try {


       /* GPSTracker gps = new GPSTracker(item_cost_delivery.this);
        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
    
            Geocoder geocoder;
            List<Address> addresses;
            List<Address> addresses1;
            geocoder = new Geocoder(item_cost_delivery.this, Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            //addresses1 = geocoder.getFromLocationName("94404 USA",1);
            if (addresses.size()>0) {
                pinCode = addresses.get(0).getPostalCode();
            }
            // \n is for new line
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            //gps.showSettingsAlert();
        //}*/

        return pinCode;
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
