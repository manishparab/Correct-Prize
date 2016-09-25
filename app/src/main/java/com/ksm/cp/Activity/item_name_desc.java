package com.ksm.cp.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.ksm.cp.Adapter.AdapterImageSelector;
import com.ksm.cp.Adapter.AdapterListViewImage;
import com.ksm.cp.Adapter.CustomAdapterItemCategory;
import com.ksm.cp.Adapter.MyAdapter;
import com.ksm.cp.Helper.LinearLayoutManager;
import com.ksm.cp.Helper.MyLinearLayoutManager;
import com.ksm.cp.Helper.SessionHelper;
import com.ksm.cp.Helper.VolleySingle;
import com.ksm.cp.Interface.AdapterCallBack;
import com.ksm.cp.Objects.ItemCategory;
import com.ksm.cp.Objects.OfferItem;
import com.ksm.cp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.lucasr.twowayview.TwoWayView;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.CountDownLatch;

public class item_name_desc extends AppCompatActivity implements AdapterCallBack {

    private int RESULT_LOAD_IMAGE = 1;
    private final int REQUEST_CODE = 1;
    private final int REQUEST_CODE_CAMERA = 2;
    private Bitmap bitmap;
    public static ArrayList<Bitmap> arrayList;
    AdapterImageSelector adapterImageSelector;
    Button ButtonAddPhotos;
    AdapterCallBack adapterCallBack;
    TextView textViewCategory;

    EditText EditTextItemName;
    EditText EditTextItemDescription;
    ImageView tempImageView;
    
    ItemCategory[] itemCategories;
    ListView listView;
    OfferItem offerItem;
    ProgressDialog progress;
    String itemData;
    String itemImagePaths;
    String itemMode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //context = getApplicationContext();
        setContentView(R.layout.activity_item_name_desc);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        textViewCategory = (TextView) findViewById(R.id.TextViewCategory);
        EditTextItemName = (EditText) findViewById(R.id.EditTextItemName);
        EditTextItemDescription = (EditText) findViewById(R.id.EditTextItemDescription);

        RecyclerView recList = (RecyclerView) findViewById(R.id.recycler_view);
        this.arrayList = new ArrayList<Bitmap>();
        adapterImageSelector = new AdapterImageSelector(this.arrayList, this);
        recList.setAdapter(adapterImageSelector);
        //recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recList.setLayoutManager(llm);


        Button buttonNext = (Button) findViewById(R.id.Button_activity_name_description_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartNextActivity();
            }
        });

        offerItem = new OfferItem();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemMode = extras.getString("Mode");
            if (itemMode.equals("Edit")) {
                itemData = extras.getString("itemData");
                itemImagePaths = extras.getString("itemImagePaths");
                String[] itemImagePathsSplit = itemImagePaths.split("æ");
                final ArrayList<String> imagePaths = new ArrayList<>();
                for (int i = 0; i <= itemImagePathsSplit.length - 1; i++) {
                    imagePaths.add(itemImagePathsSplit[i]);
                }

                for (int j = 0; j <= imagePaths.size() - 1; j++) {

                    final String urltemp =  "http://manishp.info/" + imagePaths.get(j);
                 Thread t1= new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final URL url = new URL(urltemp);
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setDoInput(true);
                                connection.connect();
                                InputStream input = null;
                                input = connection.getInputStream();
                                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                                arrayList.add(myBitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    t1.start();
                    try {
                        t1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                offerItem.ImagePaths = imagePaths;
                String[] itemDataSplit = itemData.split("æ");
                offerItem.UserId = Long.valueOf(itemDataSplit[0]);
                //offerItem.UserName = itemDataSplit[1];
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
                textViewCategory.setText(offerItem.CategoryDesc);
                EditTextItemName.setText(offerItem.Name);
                EditTextItemDescription.setText(Html.fromHtml(offerItem.Description));


            } else {
                this.PopulateOfferItemFromIntent(getIntent());
            }
        }


        textViewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopulateCategories();
            }
        });

        ButtonAddPhotos = (Button) findViewById(R.id.ButtonAddPhotos);
        ButtonAddPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE); */

                selectImage();

            }
        });

    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 2);

                } else if (items[item].equals("Choose from Library")) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void StartNextActivity() {
        Intent intent = new Intent(getApplicationContext(), item_cost_delivery.class);
        offerItem.Name = ((EditText) findViewById(R.id.EditTextItemName)).getText().toString();
        offerItem.Description = ((EditText) findViewById(R.id.EditTextItemDescription)).getText().toString();
        //intent.putExtra("itemImage",this.arrayList);
        PopulateIntentWithData(intent);

        startActivity(intent);
    }


    public void PopulateOfferItemFromIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            offerItem.Name = extras.getString("Name");
            offerItem.Cost = extras.getDouble("Cost");
            offerItem.Description = extras.getString("Description");
            offerItem.IsNegotiable = extras.getBoolean("IsNegotiable");
            offerItem.PostalCode = extras.getString("PostalCode");
            offerItem.DeliveryType = extras.getBoolean("DeliveryType");
            offerItem.Category = extras.getInt("Category");
            offerItem.Condition = extras.getInt("Condition");
            offerItem.GPSlat = extras.getDouble("GPSlat");
            offerItem.GPSLong = extras.getDouble("GPSLong");
        }

    }

    public Intent PopulateIntentWithData(Intent intent) {

        if(offerItem.UserId == 0)
        {
            offerItem.UserId = SessionHelper.GetCurrentUser(getApplicationContext()).UserId;
        }
        intent.putExtra("UserId", offerItem.UserId);
        //intent.putExtra("UserName", offerItem.UserName);
        intent.putExtra("Name", offerItem.Name);
        intent.putExtra("Cost", offerItem.Cost);
        intent.putExtra("Description", offerItem.Description);
        intent.putExtra("IsNegotiable", offerItem.IsNegotiable);
        intent.putExtra("PostalCode", offerItem.PostalCode);
        intent.putExtra("DeliveryType", offerItem.DeliveryType);
        intent.putExtra("DeliveryTypeDesc", offerItem.DeliveryTypeDesc);
        intent.putExtra("IsSold", offerItem.IsSold);
        intent.putExtra("Category", offerItem.Category);
        intent.putExtra("CategoryDesc", offerItem.CategoryDesc);
        intent.putExtra("Condition", offerItem.Condition);
        intent.putExtra("ConditionDesc", offerItem.ConditionDesc);
        intent.putExtra("GPSlat", offerItem.GPSlat);
        intent.putExtra("GPSLong", offerItem.GPSLong);
        intent.putExtra("PostedDateString", offerItem.PostedDateString);
        intent.putExtra("ItemId", offerItem.ItemId);

        if (itemMode.equals("Edit")) {
            intent.putExtra("Mode", "Edit");
        } else {
            intent.putExtra("Mode", "");
        }
        return intent;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final Intent testIntent = data;

        if (requestCode == REQUEST_CODE  && resultCode == Activity.RESULT_OK)

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final int IMAGE_MAX_SIZE = 1000;
                    try {
                        BitmapFactory.Options o = new BitmapFactory.Options();
                        o.inJustDecodeBounds = true;

                        InputStream stream = getContentResolver().openInputStream(
                                testIntent.getData());

                        bitmap = BitmapFactory.decodeStream(stream, null, o);
                        stream.close();

                        int scale = 1;
                        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
                        }

                        //Decode with inSampleSize
                        stream = getContentResolver().openInputStream(
                                testIntent.getData());
                        BitmapFactory.Options o2 = new BitmapFactory.Options();
                        o2.inSampleSize = scale;
                        bitmap = BitmapFactory.decodeStream(stream, null, o2);
                        stream.close();
                        arrayList.add(bitmap);
                        if (arrayList.size() == 5) {
                            ButtonAddPhotos.setVisibility(View.GONE);
                        }

                        // adapterListViewImage.notifyDataSetChanged();
                        adapterImageSelector.addItem(arrayList.size() - 1);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });
        // We need to recyle unused bitmaps
        else
        {
            if (requestCode == REQUEST_CODE_CAMERA  && resultCode == Activity.RESULT_OK)

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final int IMAGE_MAX_SIZE = 1000;
                        try {
                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inJustDecodeBounds = true;

                            Bitmap photo = (Bitmap)testIntent.getExtras().get("data");
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            photo.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                            byte[] bitmapdata = bos.toByteArray();
                            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
                            ByteArrayInputStream bs1 = new ByteArrayInputStream(bitmapdata);

                            bitmap = BitmapFactory.decodeStream(bs, null, o);
                            bs.close();

                            int scale = 1;
                            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                                scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                                        (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
                            }

                            //Decode with inSampleSize

                            BitmapFactory.Options o2 = new BitmapFactory.Options();
                            o2.inSampleSize = scale;
                            bitmap = BitmapFactory.decodeStream(bs1, null, o2);
                            bs1.close();
                            arrayList.add(bitmap);
                            if (arrayList.size() == 5) {
                                ButtonAddPhotos.setVisibility(View.GONE);
                            }

                            // adapterListViewImage.notifyDataSetChanged();
                            adapterImageSelector.addItem(arrayList.size() - 1);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                });
        }


        super.onActivityResult(requestCode, resultCode, data);

    }


    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

    private void ShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Item Category");
        builder.setAdapter(new ListViewCategoryAdapter(), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO - Code when list item is clicked (int which - is param that gives you the index of clicked item)
            }
        });
        final AlertDialog alert = builder.create();
        final ListView listView = alert.getListView();
        listView.setDivider(new ColorDrawable(getResources().getColor(R.color.lightGray))); // set color
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                offerItem.Category = Integer.valueOf(((TextView) view.findViewById(R.id.TextViewItemCategoryId)).getText().toString());
                //offerItem.itemCategoryName = ((TextView) view.findViewById(R.id.TextViewItemCategoryName)).getText().toString();
                String categoryName = ((TextView) view.findViewById(R.id.TextViewItemCategoryName)).getText().toString();
                textViewCategory.setText(categoryName);
                alert.dismiss();
            }
        });

        listView.setDividerHeight(2); // set height
        alert.show();

        /*new AlertDialog.Builder(item_name_desc.this)
                .setAdapter(new ListViewCategoryAdapter(), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO - Code when list item is clicked (int which - is param that gives you the index of clicked item)
                    }
                })

                .setTitle("Choose Item Category")
                .show();*/
    }

    private void PopulateCategories() {
        String url = "http://manishp.info/CPDataService.svc/GetItemCategory";
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    public void onResponse(JSONArray response) {
                        itemCategories = new Gson().fromJson(response.toString(), ItemCategory[].class);
                        ShowDialog();
                        //listView.setAdapter(new ListViewCategoryAdapter());
                        //progress.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String test = error.getMessage();
                progress.dismiss();
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                //pDialog.hide();
            }
        });

// Adding request to request queue
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);
        VolleySingle.getInstance(this).getRequestQueue().add(jsonObjReq);
    }


    @Override
    public void DoSomeAction() {
        ButtonAddPhotos.setVisibility(View.VISIBLE);
    }


    public class ListViewCategoryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return itemCategories.length;
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
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                workingview = inflater.inflate(R.layout.item_category_list_item, null);
            } else {
                workingview = convertView;
            }

            TextView textViewItemCategoryName = (TextView) workingview.findViewById(R.id.TextViewItemCategoryName);
            TextView textViewItemCategoryId = (TextView) workingview.findViewById(R.id.TextViewItemCategoryId);
            ItemCategory item = itemCategories[position];
            textViewItemCategoryName.setText(item.Name);
            textViewItemCategoryId.setText(String.valueOf(item.Id));
            return workingview;

        }

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


