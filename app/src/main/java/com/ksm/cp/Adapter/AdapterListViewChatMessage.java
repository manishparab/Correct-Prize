package com.ksm.cp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ksm.cp.Objects.ChatMessage;
import com.ksm.cp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mparab on 2/28/2016.
 */
public class AdapterListViewChatMessage extends BaseAdapter {
    Context context;
    ArrayList<ChatMessage> chatMessages;

    public AdapterListViewChatMessage(Context context, ArrayList<ChatMessage> chatMessages)
    {
        this.chatMessages = chatMessages;
        this.context = context;
    }
    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View workingview;
        if (convertView ==  null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            workingview = inflater.inflate(R.layout.chatmessage_sell, parent,false);
        }
        else
        {
            workingview = convertView;
        }

        ImageView imageView = (ImageView)workingview.findViewById(R.id.displayImageContactImage);
        TextView TextViewContactName = (TextView) workingview.findViewById(R.id.TextviewContactName);
        TextView TextViewChatMessage = (TextView) workingview.findViewById(R.id.ChatMessage);
        TextView TextViewContactOffer = (TextView) workingview.findViewById(R.id.TextviewContactOffer);
        TextView TextviewExtraData = (TextView) workingview.findViewById(R.id.TextviewExtraData);

        ChatMessage messages = chatMessages.get(position);
        TextViewChatMessage.setText(messages.Message);
        TextViewContactName.setText(messages.ContactName);
        Picasso.with(workingview.getContext())
                .load("http://manishp.info/" + messages.contactImageUrl)
                .into(imageView);

        if (messages.OfferAmount > 0)
        {
            TextViewContactOffer.setText("Offer Amount:" + messages.OfferAmount);
        }

        String ExtraData = messages.UserFromId + "æ" + messages.UserTo + "æ" + messages.ItemId;
        TextviewExtraData.setText(ExtraData);


        return workingview;
    }
}
