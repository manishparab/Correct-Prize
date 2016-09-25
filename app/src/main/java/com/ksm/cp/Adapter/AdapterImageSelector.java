
package com.ksm.cp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ksm.cp.Interface.AdapterCallBack;
import com.ksm.cp.R;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by mparab on 2/19/2016.
 */
public class AdapterImageSelector extends RecyclerView.Adapter<AdapterImageSelector.ViewHolder> {

    // Create a class which holds both bitmap and the url and play with it.
    ArrayList<Bitmap> images;
    AdapterCallBack adapterCallBack;

    public AdapterImageSelector( ArrayList<Bitmap> images, AdapterCallBack callBack)
    {
        this.images = images;
        this.adapterCallBack = callBack;
    }

    @Override
    public AdapterImageSelector.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imageselector, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterImageSelector.ViewHolder holder, final int position) {
        final Bitmap bitmap = this.images.get(position);
        holder.imageViewImageSelector.setImageBitmap(bitmap);
        holder.imageViewImageSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("RecyclerView", "onClick：" + getPosition());
                //Toast.makeText(v.getContext(),getPosition(), Toast.LENGTH_LONG).show();
                //Toast.makeText(v.getContext(), "Position: " + getLayoutPosition(), Toast.LENGTH_LONG).show();
                CharSequence colors[] = new CharSequence[]{"Yes", "No"};

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Delete Image");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        if (which == 0) {
                            removeItem(bitmap);
                            //removeItem(tempPosition);
                        }
                    }
                });
                builder.show();
            }
        });
    }

    public void addItem(Bitmap image) {
        //ImageUrls.add(data);
        this.images.add(image);
        notifyItemInserted(this.images.size() - 1);
    }

    public void addItem(int position)
    {
        notifyItemInserted(position);
    }

    public void removeItem(Bitmap bitmap) {
        int position = images.indexOf(bitmap);
        images.remove(position);
        notifyItemRemoved(position);
        if (this.images.size() < 5) {
            adapterCallBack.DoSomeAction();
        }

    }

    @Override
    public int getItemCount() {
       return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageViewImageSelector;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageViewImageSelector = (ImageView)itemView.findViewById(R.id.ImageViewImageSelector);
        }
    }


}
