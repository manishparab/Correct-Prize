
package com.ksm.cp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



import com.ksm.cp.Interface.AdapterCallBack;
import com.ksm.cp.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
//import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mparab on 2/19/2016.
 */
public class AdapterForSgrid extends RecyclerView.Adapter<AdapterForSgrid.ViewHolder> {

    // Create a class which holds both bitmap and the url and play with it.

    String userId;
    int imageCount;
    String serverName = "manishp.info";
    String imageFolderName = "images";
    String imagePath;
    private DisplayImageOptions options;
    protected ImageLoader imageLoader ;

    public AdapterForSgrid(String userId , int imageCount)
    {
        this.userId = userId;
        this.imageCount = imageCount;

        this.imageLoader = ImageLoader.getInstance();
        this.imagePath = serverName + "/" + imageFolderName + "/" + userId;

        options = new DisplayImageOptions.Builder().build();
        /*options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();*/
    }

    @Override
    public AdapterForSgrid.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imagesforsgrid, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterForSgrid.ViewHolder holder, int position) {


       // final Bitmap bitmap = this.images.get(position);
        String imagePath =  "http://" + this.imagePath + "/" + (position) + ".png";
        
        //ImageAware imageAware = new ImageViewAware(holder.ImageViewForGrid, false);
        imageLoader.displayImage(imagePath, holder.ImageViewForGrid, options);
        //holder.ImageViewForGrid.setImageBitmap(bitmap);
       /* holder.imageViewImageSelector.setOnClickListener(new View.OnClickListener() {
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
        }); */
    }



    @Override
    public int getItemCount() {
       return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView ImageViewForGrid;
        public Context context;
        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            //this.ImageViewForGrid = (ImageView)itemView.findViewById(R.id.ImageViewForGrid);
        }
    }


}
