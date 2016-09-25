package com.ksm.cp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ksm.cp.Helper.SessionHelper;
import com.ksm.cp.Objects.ChatMessage;
import com.ksm.cp.R;

import java.util.ArrayList;

/**
 * Created by mparab on 2/28/2016.
 */
public class AdapterListViewMessageThread extends BaseAdapter {
    Context context;
    ArrayList<ChatMessage> chatMessages;
    Long currentUserId;

    public AdapterListViewMessageThread(Context context, ArrayList<ChatMessage> chatMessages)
    {
        this.currentUserId = SessionHelper.GetCurrentUser(context).UserId;
        this.chatMessages = chatMessages;
        this.context = context;
    }
    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View workingview;
        ChatMessage message = this.chatMessages.get(position);
        if (convertView ==  null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (message.UserFromId == this.currentUserId) {
                workingview = inflater.inflate(R.layout.list_item_message_right, parent, false);
            }
            else
            {
                workingview = inflater.inflate(R.layout.list_item_message_left, parent, false);
            }
        }
        else
        {
            workingview = convertView;
        }

        TextView txtMsg = (TextView) workingview.findViewById(R.id.txtMsg);
        TextView lblMsgFrom = (TextView) workingview.findViewById(R.id.lblMsgFrom);
        txtMsg.setText(message.Message);

        if (message.UserFromId == this.currentUserId)
        {
            lblMsgFrom.setText("you");
        }
        else
        {
            lblMsgFrom.setText(message.ContactName);
        }
        return workingview;
    }


}
