package com.ksm.cp.Objects;

import java.util.Date;

/**
 * Created by mparab on 4/28/2016.
 */
public class ChatMessage {
    public long MessageId;
    public long UserFromId;
    public long UserTo;
    public String Message;
    public Date SendDate;
    public boolean IsRead;
    public boolean IsFlagged;
    public long ItemId;
    public long OfferItemId;
    public String SendDateString;
    public String contactImageUrl;
    public double OfferAmount;
    public String ContactName;
}
