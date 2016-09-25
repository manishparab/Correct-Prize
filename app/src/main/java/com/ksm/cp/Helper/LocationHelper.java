package com.ksm.cp.Helper;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.JsonArray;
import com.ksm.cp.Objects.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by mparab on 5/13/2016.
 */
public class LocationHelper {
    public static String responseJson;

    public static Location GetLocation(Context context) {
        Location location;
        //location = SessionHelper.GetCurrentLocation(context);

        location = new Location();
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            location.isLocationEnabled = false;
        } else {
            try {
                GPSTracker gps = new GPSTracker(context);
                // check if GPS enabled
                if (gps.canGetLocation()) {

                    location.LocationLat = (float) gps.getLatitude();
                    location.LocationLng = (float) gps.getLongitude();

                    Geocoder geocoder;
                    List<Address> addresses;
                    List<Address> addresses1;
                    geocoder = new Geocoder(context, Locale.getDefault());
                    addresses = geocoder.getFromLocation(location.LocationLat, location.LocationLng, 1);

                    //addresses1 = geocoder.getFromLocationName("94404 USA",1);
                    if (addresses.size() > 0) {
                        location.PostalCode = addresses.get(0).getPostalCode();
                        location.LocationName = addresses.get(0).getLocality();
                        location.Country = addresses.get(0).getCountryName();
                    }

                    //SessionHelper.SetCurrentUserLocation(context,location);
                    // \n is for new line
                    //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        return location;
    }

    public static Location GetLocation(Context context, String ZipCode) {
        Location location = new Location();
        try {

            Geocoder geocoder;
            List<Address> addresses;
            List<Address> addresses1;
            geocoder = new Geocoder(context, Locale.getDefault());

            addresses = geocoder.getFromLocationName(ZipCode + " USA", 1);

            if (addresses.size() > 0) {
                location.PostalCode = addresses.get(0).getPostalCode();
                location.LocationName = addresses.get(0).getLocality();
                location.Country = addresses.get(0).getCountryName();
                location.LocationLat = (float) addresses.get(0).getLatitude();
                location.LocationLng = (float) addresses.get(0).getLongitude();
            }

        } catch (IOException e) {


            String query = URLEncoder.encode(ZipCode + " USA");
            String url = "http://maps.google.com/maps/api/geocode/json?address=" + query + "&sensor=true";
                try {
                    String responsejson = getLatLongFromAddress(url);
                    JSONObject response = new JSONObject(responsejson);
                    JSONObject jsonObject = response;
                    if (jsonObject.getString("status").equals("OK")) {
                        JSONObject results = jsonObject.getJSONArray("results")
                                .getJSONObject(0);
                        JSONObject geometry = results.getJSONObject("geometry");
                        JSONObject locationresults = geometry.getJSONObject("location");
                        String lat = locationresults.getString("lat");
                        String lng = locationresults.getString("lng");
                        String address = results.getString("formatted_address");
                        location.LocationLat = Float.valueOf(lat);
                        location.LocationLng = Float.valueOf(lng);
                        location.PostalCode = ZipCode;
                        location.LocationName = address;
                        // find the country name
                        JSONArray addressComponent = results.getJSONArray("address_components");
                        for (int j = 0; j < addressComponent.length(); j++) {

                            if (addressComponent.getJSONObject(j).getJSONArray("types").get(0).toString().equals("country") ) {
                                location.Country = addressComponent.getJSONObject(j).getString("short_name");
                            }
                        }
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
        }

        return location;
    }

    private static String getLatLongFromAddress(final String address) {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder result = new StringBuilder();
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(address);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    responseJson = result.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return responseJson;
    }
}
