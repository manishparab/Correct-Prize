package com.ksm.cp.Helper;

import android.content.Context;

import com.ksm.cp.Objects.CurrentUser;
import com.ksm.cp.Objects.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by mparab on 5/13/2016.
 */
public class CurrentUserHelper {

    public static HashMap<String, Object> CreateParameters(CurrentUser currentUser)
    {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("UserId", currentUser.UserId);
        params.put("DisplayName", currentUser.DisplayName);
        params.put("UserTypeId", currentUser.UserTypeId);
        params.put("LocationLng", currentUser.location.LocationLng);
        params.put("LocationLat", currentUser.location.LocationLat);
        params.put("PostalCode", currentUser.location.PostalCode);
        params.put("LocationName", currentUser.location.LocationName);
        params.put("Country", currentUser.location.Country);
        params.put("ProfileImagePath", currentUser.ProfileImagePath);
        params.put("Email", currentUser.Email);
        return params;
    }

    public static CurrentUser GetUserFromJSON(JSONObject response) {
        CurrentUser currentUser = new CurrentUser();
        Location location =  new Location();
        try {

            if (!response.isNull("UserId"))
                currentUser.UserId = response.getLong("UserId");
            if (!response.isNull("UserTypeId"))
                currentUser.UserTypeId = response.getInt("UserTypeId");
            if (!response.isNull("DisplayName"))
                currentUser.DisplayName = response.getString("DisplayName");
            if (!response.isNull("IsEmailAlreadyRegistered"))
                currentUser.IsEmailAlreadyRegistered = response.getBoolean("IsEmailAlreadyRegistered");
            if (!response.isNull("Email"))
                currentUser.Email = response.getString("Email");
            if (!response.isNull("ProfileImagePath"))
                currentUser.ProfileImagePath = response.getString("ProfileImagePath");
           if (!response.isNull("LocationLat"))
               location.LocationLat = response.getDouble("LocationLat");
            if (!response.isNull("LocationLng"))
                location.LocationLng = response.getDouble("LocationLng");
            if (!response.isNull("CreatedDate"))
                currentUser.CreatedDate = response.getString("CreatedDate");
            if (!response.isNull("PostalCode"))
                location.PostalCode = response.getString("PostalCode");
            if (!response.isNull("Country"))
                location.Country = response.getString("Country");
            if (!response.isNull("LocationName"))
                location.LocationName = response.getString("LocationName");
            currentUser.location = location;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return currentUser;
    }

}
