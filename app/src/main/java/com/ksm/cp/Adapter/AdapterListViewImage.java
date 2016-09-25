package com.ksm.cp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ksm.cp.Objects.ItemCategory;
import com.ksm.cp.R;

import java.util.ArrayList;

/**
 * Created by mparab on 2/28/2016.
 */
public class AdapterListViewImage extends BaseAdapter {
    Context context;
    ArrayList<Bitmap> bitmapArrayList;

    public AdapterListViewImage( Context context, ArrayList<Bitmap> bitmapArrayList)
    {
        this.bitmapArrayList = bitmapArrayList;
        this.context = context;
    }
    @Override
    public int getCount() {
        return bitmapArrayList.size();
    }

    public void AddItem(Bitmap image)
    {
        this.bitmapArrayList.add(image);
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return bitmapArrayList.get(position);
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
            workingview = inflater.inflate(R.layout.gridview_item_photo_icon, parent,false);
        }
        else
        {
            workingview = convertView;
        }

        ImageView imageView = (ImageView)workingview.findViewById(R.id.Picture);
        Bitmap item =  this.bitmapArrayList.get(position);
        imageView.setImageBitmap(item);
        return workingview;
    }
}
