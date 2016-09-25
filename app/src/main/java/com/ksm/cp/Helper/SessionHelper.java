package com.ksm.cp.Helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.ksm.cp.Objects.CurrentUser;
import com.ksm.cp.Objects.Filter;
import com.ksm.cp.Objects.Location;

/**
 * Created by mparab on 5/7/2016.
 */
public class SessionHelper {


    public static final String MY_PREFS_NAME = "MyPrefsFile";

    public static CurrentUser GetCurrentUser(Context context)
    {
        CurrentUser user =  new CurrentUser();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
        user.UserId = prefs.getLong("UserId", 0);
        user.Email = prefs.getString("Email", "");
        user.UserTypeId = prefs.getInt("UserTypeId", 0);
        user.CreatedDate = prefs.getString("CreatedDate", "");
        user.DisplayName = prefs.getString("DisplayName", "");
        user.ProfileImagePath =  prefs.getString("ProfileImagePath","");
        user.location = GetCurrentUserLocation(context);
        //String restoredText = prefs.getString("UserId", null);
        return user;
    }

    public static void RemoveAllSharedPref(Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

    public static void SetCurrentUser(Context context,CurrentUser user)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE).edit();
        editor.putLong("UserId", user.UserId);
        editor.putString("Email", user.Email);
        editor.putInt("name", user.UserTypeId);
        editor.putInt("UserTypeId", user.UserTypeId);

        if (user.ProfileImagePath != null) {
            editor.putString("ProfileImagePath", user.ProfileImagePath);
        }
        else
        {
            editor.putString("ProfileImagePath", "");
        }

        editor.putString("CreatedDate", user.CreatedDate);
        editor.putString("DisplayName", user.DisplayName);
        editor.commit();

        SetCurrentUserLocation(context, user.location);
    }

    public static void SetCurrentLocation(Context context,Location location)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE).edit();
        editor.putBoolean("isLocationEnabled", location.isLocationEnabled);
        editor.putFloat("LocationLng", (float)location.LocationLng);
        editor.putFloat("LocationLat", (float)location.LocationLat);
        if (location.Country != null) {
            editor.putString("Country", location.Country);
        }
        else
        {
            editor.putString("Country", "");
        }

        if (location.LocationName != null) {
            editor.putString("LocationName", location.LocationName);
        }
        else
        {
            editor.putString("LocationName", "");
        }

        if (location.PostalCode != null) {
            editor.putString("PostalCode", location.PostalCode);
        }
        else
        {
            editor.putString("PostalCode", "");
        }
        editor.commit();
    }

    public static void SetFilterOption(Context context, Filter filter)
    {

        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE).edit();
        editor.putInt("filterDistance", filter.Distance);
        SetCurrentLocation(context, filter.location);
        editor.commit();
    }

    public static void SetCurrentUserLocation(Context context,Location location)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE).edit();
        editor.putBoolean("UisLocationEnabled", location.isLocationEnabled);
        editor.putFloat("ULocationLng", (float)location.LocationLng);
        editor.putFloat("ULocationLat", (float)location.LocationLat);
        if (location.Country != null) {
            editor.putString("UCountry", location.Country);
        }
        else
        {
            editor.putString("UCountry", "");
        }

        if (location.LocationName != null) {
            editor.putString("ULocationName", location.LocationName);
        }
        else
        {
            editor.putString("ULocationName", "");
        }

        if (location.PostalCode != null) {
            editor.putString("UPostalCode", location.PostalCode);
        }
        else
        {
            editor.putString("UPostalCode", "");
        }
        editor.commit();
    }


    public static  Location GetCurrentLocation(Context context)
    {
        Location location =  new Location();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
        location.LocationLat = prefs.getFloat("LocationLat", 0);
        location.LocationLng = prefs.getFloat("LocationLng",0);
        location.LocationName = prefs.getString("LocationName", "");
        location.PostalCode = prefs.getString("PostalCode","");
        location.Country = prefs.getString("Country","");
        location.isLocationEnabled = prefs.getBoolean("isLocationEnabled",false);
        return  location;
    }

    public static Filter GetFilerOptions(Context context)
    {
        Filter filter =  new Filter();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
        filter.Distance = prefs.getInt("filterDistance", 0);
        if (filter.Distance == 0)
        {
            filter.Distance = 5;
        }
        filter.location =  GetCurrentLocation(context);
        return filter;
    }

    public static  Location GetCurrentUserLocation(Context context)
    {
        Location location =  new Location();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
        location.LocationLat = prefs.getFloat("ULocationLat", 0);
        location.LocationLng = prefs.getFloat("ULocationLng",0);
        location.LocationName = prefs.getString("ULocationName", "");
        location.PostalCode = prefs.getString("UPostalCode","");
        location.Country = prefs.getString("UCountry","");
        location.isLocationEnabled = prefs.getBoolean("UisLocationEnabled",false);
        return  location;
    }
}
