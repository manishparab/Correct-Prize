package com.ksm.cp.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ksm.cp.Helper.SessionHelper;
import com.ksm.cp.Helper.VolleySingle;
import com.ksm.cp.Objects.CurrentUser;
import com.ksm.cp.Objects.OfferItem;
import com.ksm.cp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemDetails extends AppCompatActivity {

    OfferItem offerItem;
    SupportMapFragment mySupportMapFragment;
    LinearLayout LinearLayoutUserProfileInfo;
    LinearLayout LinearLayoutMessageSeller;
    ImageView ImageViewItem_Detail;
    TextView Item_detail_Item_Name;
    TextView Item_detail_Item_PostDateTime;
    TextView Item_detail_Item_Category;
    TextView Item_detail_Item_OwerName;
    TextView ImageViewItem_ItemCondition;
    TextView ImageViewItem_Description;
    TextView ImageViewItem_ItemLocation;
    TextView TextViewCost;
    Context context;
    String itemData;
    String itemImagePaths;
    ImageView imageviewIsSold;
    ImageView imageViewItem_OwnerIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        this.context = getApplicationContext();
        mySupportMapFragment = ((SupportMapFragment) this
                .getSupportFragmentManager().findFragmentById(R.id.mapPickUpLocation));
        ImageViewItem_Detail = (DynamicHeightImageView) findViewById(R.id.ImageViewItem_Detail);
        Item_detail_Item_Name = (TextView) findViewById(R.id.Item_detail_Item_Name);
        Item_detail_Item_Category = (TextView) findViewById(R.id.Item_detail_Item_Category);
        Item_detail_Item_PostDateTime = (TextView) findViewById(R.id.Item_detail_Item_PostDateTime);
        Item_detail_Item_OwerName = (TextView) findViewById(R.id.Item_detail_Item_OwerName);
        ImageViewItem_ItemCondition = (TextView) findViewById(R.id.ImageViewItem_ItemCondition);
        ImageViewItem_Description = (TextView) findViewById(R.id.ImageViewItem_Description);
        LinearLayoutUserProfileInfo = (LinearLayout) findViewById(R.id.LinearLayoutUserProfileInfo);
        LinearLayoutMessageSeller = (LinearLayout) findViewById(R.id.LinearLayoutMessageSeller);
        ImageViewItem_ItemLocation = (TextView) findViewById(R.id.ImageViewItem_ItemLocation);
        TextViewCost = (TextView) findViewById(R.id.TextViewCost);
        imageviewIsSold = (ImageView) findViewById(R.id.imageviewIsSold);
        imageViewItem_OwnerIcon = (ImageView) findViewById(R.id.ImageViewItem_OwnerIcon);
        //PopulateData();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemData = extras.getString("itemData");
            itemImagePaths = extras.getString("itemImagePaths");
        }

        String[] itemDataSplit = itemData.split("æ");
        String[] itemImagePathsSplit = itemImagePaths.split("æ");

        offerItem = new OfferItem();
        offerItem.UserId = Long.valueOf(itemDataSplit[0]);
        offerItem.seller = new CurrentUser();
        offerItem.seller.DisplayName = itemDataSplit[1];
        offerItem.Cost = Double.valueOf(itemDataSplit[2]);
        offerItem.Name = itemDataSplit[3];
        offerItem.Description = itemDataSplit[4];
        offerItem.IsNegotiable = Boolean.valueOf(itemDataSplit[5]);
        offerItem.PostalCode = itemDataSplit[6];
        offerItem.DeliveryType = Boolean.valueOf(itemDataSplit[7]);
        offerItem.DeliveryTypeDesc = itemDataSplit[8];
        offerItem.IsSold = Boolean.valueOf(itemDataSplit[9]);
        offerItem.Category = Integer.valueOf(itemDataSplit[10]);
        offerItem.CategoryDesc = itemDataSplit[11];
        offerItem.Condition = Integer.valueOf(itemDataSplit[12]);
        offerItem.ConditionDesc = itemDataSplit[13];
        offerItem.GPSlat = Double.valueOf(itemDataSplit[14]);
        offerItem.GPSLong = Double.valueOf(itemDataSplit[15]);
        offerItem.PostedDateString = itemDataSplit[16];
        offerItem.ItemId = Long.valueOf(itemDataSplit[17]);
        offerItem.GPSLocation = itemDataSplit[18];
        offerItem.seller.ProfileImagePath = itemDataSplit[19];

        offerItem.ImagePaths = new ArrayList<>();
        for (int i = 0; i <= itemImagePathsSplit.length - 1; i++) {
            offerItem.ImagePaths.add(itemImagePathsSplit[i]);
        }

        Picasso.with(context)
                .load("http://manishp.info/" + offerItem.ImagePaths.get(0))
                .into(ImageViewItem_Detail);

        if (offerItem.seller != null) {
            if (!offerItem.seller.ProfileImagePath.isEmpty() && offerItem.seller.ProfileImagePath != null && !offerItem.seller.ProfileImagePath.equals("null") ) {
                Picasso.with(getApplicationContext())
                        .load("http://manishp.info/" + offerItem.seller.ProfileImagePath)
                        .into(imageViewItem_OwnerIcon);
            } else {
                imageViewItem_OwnerIcon.setImageResource(R.drawable.person_placeholder);
            }
        }

        Item_detail_Item_Name.setText(offerItem.Name);
        Item_detail_Item_PostDateTime.setText(offerItem.PostedDateString);
        Item_detail_Item_Category.setText(offerItem.CategoryDesc);
        Item_detail_Item_OwerName.setText(offerItem.seller.DisplayName);
        ImageViewItem_ItemCondition.setText(offerItem.ConditionDesc);
        ImageViewItem_Description.setText(Html.fromHtml(offerItem.Description));
        ImageViewItem_ItemLocation.setText(offerItem.GPSLocation);
        TextViewCost.setText("US$ " + String.valueOf(offerItem.Cost));
        if (offerItem.IsSold) {
            imageviewIsSold.setVisibility(View.VISIBLE);
        }
        // Add a marker at San Francisco.
        GoogleMap googleMap = mySupportMapFragment.getMap();

        //LatLng location = new LatLng(-31.90, 115.86);
        LatLng location = new LatLng(offerItem.GPSlat, offerItem.GPSLong);
        Marker perth = googleMap.addMarker(new MarkerOptions()
                .title("PickUp location")
                .position(location)
                .draggable(true));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(offerItem.GPSlat, offerItem.GPSLong)).zoom(12).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        Button details_ask = (Button) findViewById(R.id.Button_activity_Item_details_ask);
        Button makeOffer = (Button) findViewById(R.id.Button_activity_Item_details_makeOffer);

        details_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.SendMessage.class);
                intent.putExtra("ItemId", offerItem.ItemId);
                intent.putExtra("UserId", offerItem.UserId);
                intent.putExtra("UserName", offerItem.seller.DisplayName);
                intent.putExtra("UserProfilePic", offerItem.seller.ProfileImagePath);
                startActivity(intent);
            }
        });

        LinearLayoutMessageSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.SendMessage.class);
                intent.putExtra("ItemId", offerItem.ItemId);
                intent.putExtra("UserId", offerItem.UserId);
                intent.putExtra("UserName", offerItem.seller.DisplayName);
                intent.putExtra("UserProfilePic", offerItem.seller.ProfileImagePath);
                startActivity(intent);
            }
        });

        if (offerItem.IsSold) {
            details_ask.setVisibility(View.INVISIBLE);
            makeOffer.setVisibility(View.INVISIBLE);
            LinearLayoutMessageSeller.setVisibility(View.INVISIBLE);
        }

        makeOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.MakeOffer.class);
                intent.putExtra("ItemId", offerItem.ItemId);
                intent.putExtra("UserId", offerItem.UserId);
                intent.putExtra("ItemName", offerItem.Name);
                intent.putExtra("ItemImagePath", offerItem.ImagePaths);
                startActivity(intent);
            }
        });

        LinearLayoutUserProfileInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.UserSellingProfile.class);
                intent.putExtra("UserId", offerItem.UserId);
                intent.putExtra("UserName", offerItem.seller.DisplayName);
                intent.putExtra("UserProfilePic", offerItem.seller.ProfileImagePath);
                startActivity(intent);
            }
        });

        CurrentUser currentUser = SessionHelper.GetCurrentUser(getApplicationContext());
       /* if (currentUser != null)
        {
            if (!currentUser.ProfileImagePath.isEmpty())
            {
                Picasso.with(getApplicationContext())
                        .load("http://manishp.info/" + currentUser.ProfileImagePath)
                        .into(imageViewItem_OwnerIcon);
            }
        }*/

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

