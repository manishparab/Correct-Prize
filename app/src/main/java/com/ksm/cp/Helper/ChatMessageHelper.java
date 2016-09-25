package com.ksm.cp.Helper;

import com.ksm.cp.Objects.ChatMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mparab on 6/21/2016.
 */
public class ChatMessageHelper {

    public static ChatMessage GetChatMessageFromJSONObject(JSONObject jresponse) {

        ChatMessage message = new ChatMessage();
        try {
            message.UserFromId = jresponse.getLong("UserFromId");
            message.UserTo = jresponse.getLong("UserTo");
            message.Message = jresponse.getString("message");
            message.MessageId = jresponse.getLong("MessageId");
            String dateString = jresponse.getString("SendDate");
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            Date convertedDate = new Date();
            try {
                convertedDate = dateFormat.parse(dateString);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            message.IsRead = jresponse.getBoolean("IsRead");
            message.IsFlagged = jresponse.getBoolean("IsFlagged");
            message.ItemId = jresponse.getLong("ItemId");
            message.OfferAmount = jresponse.getDouble("OfferAmount");
            message.contactImageUrl = jresponse.getString("ContactImageUrl");
           if (!jresponse.isNull("OfferItemId")) {
               message.OfferItemId = jresponse.getLong("OfferItemId");
           }
            message.ContactName = jresponse.getString("ContactName");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return message;
    }
}
