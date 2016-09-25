package com.ksm.cp.Objects;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mparab on 1/25/2016.
 */
public class OfferItem {
    public long ItemId;
    public long UserId;
    public double Cost;
    public String Name;
    public String Description;
    public boolean IsNegotiable;
    public String PostalCode;
    public boolean DeliveryType;
    public String DeliveryTypeDesc;
    public boolean IsSold;
    public int Category;
    public String CategoryDesc;
    public int Condition;
    public String ConditionDesc;
    public double GPSlat;
    public double GPSLong;
    public double BestOfferAmount;
    public String GPSLocation;
    public String PostedDateString;
    public ArrayList<String> ImagePaths;
    public CurrentUser seller;

}
