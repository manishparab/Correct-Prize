package com.ksm.cp.Helper;

import com.ksm.cp.Objects.OfferItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mparab on 5/7/2016.
 */
public class OfferItemHelper {

    public static OfferItem GetOfferItemFromJsonObject(JSONObject jresponce) {
        OfferItem offerItem = new OfferItem();
        try {
            JSONObject itemUser = jresponce.getJSONObject("ItemUser");
            offerItem.ItemId = jresponce.getLong("ItemId");
            offerItem.UserId = jresponce.getLong("UserId");
            //offerItem.UserName = itemUser.getString("DisplayName");
            offerItem.Cost = jresponce.getDouble("Cost");
            offerItem.Name = jresponce.getString("Name");
            offerItem.Description = jresponce.getString("Description");
            offerItem.IsNegotiable = jresponce.getBoolean("IsNegotiable");
            offerItem.PostalCode = jresponce.getString("PostalCode");
            offerItem.DeliveryType = jresponce.getBoolean("DeliveryType");
            offerItem.DeliveryTypeDesc = jresponce.getString("DeliveryTypeDesc");
            offerItem.IsSold = jresponce.getBoolean("IsSold"); // need to add this
            offerItem.Category = jresponce.getInt("Category");
            offerItem.ConditionDesc = jresponce.getString("ConditionDesc"); //
            offerItem.Condition = jresponce.getInt("Category");
            offerItem.CategoryDesc = jresponce.getString("CategoryDesc");  // category is coming as null
            offerItem.PostedDateString = jresponce.getString("DatePosted"); //include this
            offerItem.GPSlat = jresponce.getDouble("LocationLat");
            offerItem.GPSLong = jresponce.getDouble("LocationLong");
            offerItem.GPSLocation = jresponce.getString("LocationName");
            offerItem.BestOfferAmount = jresponce.getDouble("BestOfferAmount");
            JSONArray imagepaths = jresponce.getJSONArray("ItemImagePaths");
            ArrayList<String> lstImagePaths = new ArrayList<>();
            for (int m = 0; m <= imagepaths.length() - 1; m++) {
                lstImagePaths.add(imagepaths.get(m).toString());
            }
            offerItem.ImagePaths = lstImagePaths;
            offerItem.seller = CurrentUserHelper.GetUserFromJSON(jresponce.getJSONObject("ItemUser"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return offerItem;
    }
}
