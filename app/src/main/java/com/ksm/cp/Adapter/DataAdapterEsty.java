package com.ksm.cp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.ksm.cp.Helper.CurrentUserHelper;
import com.ksm.cp.Helper.SessionHelper;
import com.ksm.cp.Helper.VolleySingle;
import com.ksm.cp.Objects.OfferItem;
import com.ksm.cp.Objects.TabType;
import com.ksm.cp.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mparab on 4/16/2016.
 */
public class DataAdapterEsty extends BaseAdapter {
    ArrayList<OfferItem> offerItems;
    Context context;
    TabType tabType;
    Intent intent;

    public DataAdapterEsty(Context context, ArrayList<OfferItem> data, TabType tabType) {
        this.context = context;
        this.offerItems = data;
        this.tabType = tabType;
    }

    public void clearData() {
        // clear the data
        offerItems.clear();
    }

    @Override
    public int getCount() {
        return offerItems.size();
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
        View row = convertView;
        final DynamicHeightImageView imageView;
        TextView BestOfferAmount;
        TextView TextViewItemSold;
        final DealHolder holder;
        final OfferItem offerItem = offerItems.get(position);
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.imagesforsgrid, parent, false);
            imageView = (DynamicHeightImageView) row.findViewById(R.id.image);
            BestOfferAmount = (TextView) row.findViewById(R.id.BestOfferAmount);
            TextViewItemSold = (TextView) row.findViewById(R.id.TextViewItemSold);
            if (tabType == TabType.Selling )
            {
                if (!offerItem.IsSold) {
                    if (offerItem.BestOfferAmount > 0) {
                        BestOfferAmount.setVisibility(View.VISIBLE);
                        BestOfferAmount.setText("Best Offer: " + String.valueOf(offerItem.BestOfferAmount));
                    }
                }
                else
                {
                    TextViewItemSold.setVisibility(View.VISIBLE);
                }
            }
            if (tabType == TabType.UserProfileSelling || tabType == TabType.Buying)
            {
                if (offerItem.IsSold) {
                    TextViewItemSold.setVisibility(View.VISIBLE);
                }
            }
            holder = new DealHolder();
            holder.Image = imageView;
            String contactItemData = offerItem.UserId + "æ" +
                    offerItem.seller.DisplayName + "æ" +
                    offerItem.Cost + "æ" +
                    offerItem.Name + "æ" +
                    offerItem.Description + "æ" +
                    offerItem.IsNegotiable + "æ" +
                    offerItem.PostalCode + "æ" +
                    offerItem.DeliveryType + "æ" +
                    offerItem.DeliveryTypeDesc + "æ" +
                    offerItem.IsSold + "æ" +
                    offerItem.Category + "æ" +
                    offerItem.CategoryDesc + "æ" +
                    offerItem.Condition + "æ" +
                    offerItem.ConditionDesc + "æ" +
                    offerItem.GPSlat + "æ" +
                    offerItem.GPSLong + "æ" +
                    offerItem.PostedDateString + "æ" +
                    offerItem.ItemId  + "æ" +
                    offerItem.GPSLocation + "æ" +
                    offerItem.seller.ProfileImagePath
                    ;
            String contactItemImagePaths = "";
            for (int i = 0; i <= offerItem.ImagePaths.size() - 1; i++) {
                contactItemImagePaths = contactItemImagePaths + offerItem.ImagePaths.get(i) + "æ";
            }
            holder.ImagePaths = contactItemImagePaths;
            holder.ItemData = contactItemData;
            row.setTag(holder);
        } else {
            holder = (DealHolder) row.getTag();
        }
        String imagepath = "http://manishp.info/" + offerItem.ImagePaths.get(0);
        Picasso.with(this.context)
                .load(imagepath)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(holder.Image, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Drawable drawable = holder.Image.getDrawable();
                        int diw = drawable.getIntrinsicWidth();
                        int dih = drawable.getIntrinsicHeight();
                        float ratio = (float) dih / diw; //get image aspect ratio
                        if (ratio < 1) {
                            ratio = 1;
                        }
                        holder.Image.setHeightRatio(ratio);

                    }

                    @Override
                    public void onError() {
                        String error = "";
                    }
                });

        holder.Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence options[] = null;

                final boolean isSold = Boolean.valueOf(holder.ItemData.split("æ")[9]);

                if (tabType == TabType.Selling) {
                    if (isSold)
                    {
                        options = new CharSequence[]{"View Details", "View Messages"};
                    }
                    else {
                        options = new CharSequence[]{"Edit", "Mark it sold", "View Details", "View Messages"};
                    }
                } else if (tabType == TabType.Buying) {
                    options = new CharSequence[]{"View Details", "View Messages"};
                } else if (tabType == TabType.Watching) {
                    options = new CharSequence[]{"View Details", "View Messages"};
                }

                if (tabType == TabType.Selling || tabType == TabType.Buying || tabType == TabType.Watching) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("What do you want to do");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (tabType == TabType.Selling) {
                                if (isSold)
                                {
                                    if (which == 0) {
                                        intent = new Intent(context, com.ksm.cp.Activity.ItemDetails.class);
                                        intent.putExtra("itemData", holder.ItemData);
                                        intent.putExtra("itemImagePaths", holder.ImagePaths);
                                        context.startActivity(intent);
                                    }

                                    if (which == 1) {
                                        //intent = new Intent(context, com.ksm.cp.Activity.ChatMessageThread.class);
                                        //context.startActivity(intent);
                                        intent = new Intent(context, com.ksm.cp.Activity.messages_item_sell.class);
                                        intent.putExtra("toUserId", offerItem.UserId);
                                        intent.putExtra("fromUserId", SessionHelper.GetCurrentUser(context).UserId);
                                        intent.putExtra("itemId", offerItem.ItemId);
                                        context.startActivity(intent);
                                    }
                                }
                                else {
                                    if (which == 0) {
                                        intent = new Intent(context, com.ksm.cp.Activity.item_name_desc.class);
                                        intent.putExtra("Mode", "Edit");
                                        intent.putExtra("itemData", holder.ItemData);
                                        intent.putExtra("itemImagePaths", holder.ImagePaths);
                                        context.startActivity(intent);
                                    }
                                    if (which == 2) {
                                        intent = new Intent(context, com.ksm.cp.Activity.ItemDetails.class);
                                        intent.putExtra("itemData", holder.ItemData);
                                        intent.putExtra("itemImagePaths", holder.ImagePaths);
                                        context.startActivity(intent);
                                    }
                                    if (which == 1) {
                                        String[] itemDataSplit = holder.ItemData.split("æ");
                                        // mark it sold.
                                        MarkItemAsSold(Long.valueOf(itemDataSplit[17]), true);
                                    }
                                    if (which == 3) {
                                        //intent = new Intent(context, com.ksm.cp.Activity.ChatMessageThread.class);
                                        //context.startActivity(intent);
                                        intent = new Intent(context, com.ksm.cp.Activity.messages_item_sell.class);
                                        intent.putExtra("toUserId", offerItem.UserId);
                                        intent.putExtra("fromUserId", SessionHelper.GetCurrentUser(context).UserId);
                                        intent.putExtra("itemId", offerItem.ItemId);
                                        context.startActivity(intent);
                                    }
                                }

                            }
                            if (tabType == TabType.Buying) {
                                if (which == 0) {
                                    intent = new Intent(context, com.ksm.cp.Activity.ItemDetails.class);
                                    intent.putExtra("itemData", holder.ItemData);
                                    intent.putExtra("itemImagePaths", holder.ImagePaths);
                                    context.startActivity(intent);
                                }
                                if (which == 1) {
                                    intent = new Intent(context, com.ksm.cp.Activity.ChatMessageThread.class);
                                    intent.putExtra("toUserId", offerItem.UserId);
                                    intent.putExtra("fromUserId", SessionHelper.GetCurrentUser(context).UserId);
                                    intent.putExtra("itemId", offerItem.ItemId);
                                    context.startActivity(intent);
                                }
                            }
                        }
                    });
                    builder.show();
                }
                if (tabType == TabType.UserProfileSelling) {
                    intent = new Intent(context, com.ksm.cp.Activity.ItemDetails.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("itemData", holder.ItemData);
                    intent.putExtra("itemImagePaths", holder.ImagePaths);
                    context.startActivity(intent);
                }
            }
        });
        return row;

    }

    static class DealHolder {
        DynamicHeightImageView Image;
        String ItemData;
        String ImagePaths;
    }

    private void MarkItemAsSold(long itemId, boolean isSold)
    {
        final long itemIdThread = itemId;
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("itemId", itemId);
        params.put("isSold", isSold);
        String url = "http://manishp.info/CPDataService.svc/UpdateSellStatus";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        Toast.makeText(context,"Item marked as sold", Toast.LENGTH_SHORT).show();

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                SendSoldMesage(itemIdThread);
                            }
                        });

                        thread.start();

                        Thread threadStartActivity =  new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(context, com.ksm.cp.Activity.BuySellProfile.class);
                                //Intent intent =  new Intent(getApplicationContext(),com.ksm.cp.Activity.test.class);
                                context.startActivity(intent);
                            }
                        });

                        threadStartActivity.start();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String test = error.getMessage();
            }
        });
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);
        VolleySingle.getInstance(context).getRequestQueue().add(jsonObjReq);
    }

    private void SendSoldMesage(Long itemId){
        //Send message to all the items saying its sold
    }
}
