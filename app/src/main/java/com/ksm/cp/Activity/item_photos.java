package com.ksm.cp.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ksm.cp.Helper.ExpandableHeightGridView;
import com.ksm.cp.Objects.OfferItem;
import com.ksm.cp.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class item_photos extends Activity {
    private ArrayList<String> imageUrls;
    //java.util.WeakHashMap<Integer,Bitmap> hashMap;
    //private ArrayList<Long> _imageUrls;
    ImageAdapter imageAdapter;
    SparseBooleanArray mSparseBooleanArray = new SparseBooleanArray();
    private DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_photos);
        PopulateImages();
        Button button = (Button) findViewById(R.id.Button_activity_SelectImage_Next);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OfferItem offerItem =  new OfferItem();
                ArrayList<String> lstImageUrl =  new ArrayList<String>();
                for (int i=0 ; i<= mSparseBooleanArray.size() ; i++ )
                {
                    lstImageUrl.add(imageUrls.get(mSparseBooleanArray.keyAt(i)));
                }
                //offerItem.imageUrl =lstImageUrl;
                String stringJson = new Gson().toJson(offerItem);
                Intent intent =  new Intent(v.getContext(),item_name_desc.class);
                intent.putExtra("item", stringJson);
                startActivity(intent);
            }
        });
    }


    private void PopulateImages() {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .preProcessor(new BitmapProcessor() {
                    public Bitmap process(Bitmap src) {
                        return ThumbnailUtils.extractThumbnail(src, 256, 256);
                    }
                })
                .build();
        //imageLoader = ImageLoaderSingle.GetImageLoaderInstance();
        final Cursor imagecursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);


        this.imageUrls = new ArrayList<String>();
        //this._imageUrls=  new ArrayList<Long>(1212);
        //this.hashMap = new java.util.WeakHashMap<>();

        //final int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Thumbnails._ID);

        //final BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inJustDecodeBounds = true;
        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
            imageUrls.add(imagecursor.getString(dataColumnIndex));
        }
        imagecursor.close();
        ExpandableHeightGridView imagegrid = (ExpandableHeightGridView) findViewById(R.id.grid_view_GridContactImages);
        imagegrid.setExpanded(true);
        imageAdapter = new ImageAdapter();
        imagegrid.setAdapter(imageAdapter);

        imagegrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                ImageView imageView = (ImageView) v.findViewById(R.id.Picture);
                if (!mSparseBooleanArray.get(position)) {
                    mSparseBooleanArray.put(position, true);

                    imageView.setBackgroundColor(Color.parseColor("#ff7e51c2"));
                } else {
                    mSparseBooleanArray.delete(position);
                    imageView.setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }
        });

    }

    public class ImageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return imageUrls.size();
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
            if (convertView ==  null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                workingview = inflater.inflate(R.layout.gridview_item_photo_icon, null);
            }
            else
            {
                workingview = convertView;
            }

            ImageView imageView = (ImageView) workingview.findViewById(R.id.Picture);

            if (mSparseBooleanArray.get(position))
            {
                imageView.setBackgroundColor(Color.parseColor("#ff7e51c2"));
            }
            else {
                imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }

            ImageAware imageAware = new ImageViewAware(imageView, false);

            /*CheckBox ChkImageSelector = (CheckBox) workingview.findViewById(R.id.ChkImageSelector);
            ChkImageSelector.setTag(position);
            ChkImageSelector.setChecked(mSparseBooleanArray.get(position));
            ChkImageSelector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mSparseBooleanArray.put((Integer) buttonView.getTag(),isChecked);
                }
            });*/
            //imageView.setImageResource(R.drawable.placeholder);
           // imageView.setImageBitmap(hashMap.get(position));
            imageLoader.displayImage("file://" + imageUrls.get(position), imageAware, options);
            return workingview;

        }

    }
}
