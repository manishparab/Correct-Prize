package com.ksm.cp.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ksm.cp.Helper.CurrentUserHelper;
import com.ksm.cp.Helper.SessionHelper;
import com.ksm.cp.Helper.VolleySingle;
import com.ksm.cp.Objects.CurrentUser;
import com.ksm.cp.R;
import com.squareup.picasso.Picasso;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class EditActivityProfileImage extends AppCompatActivity {

    Button Button_TakePhoto;
    CurrentUser currentUser;
    ImageView imageViewProfileImage;
    private Bitmap bitmap = null;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Profile");

        setContentView(R.layout.activity_edit_activity_profile_image);
        imageViewProfileImage = (ImageView) findViewById(R.id.ImageViewProfileImage);
        Button_TakePhoto =(Button) findViewById(R.id.Button_TakePhoto);
        Button_TakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap == null) {
                    selectImage();
                }
                else
                {
                    PostImages();
                }
            }
        });
        currentUser = SessionHelper.GetCurrentUser(getApplicationContext());

        if(currentUser != null) {
            if (!currentUser.ProfileImagePath.isEmpty()) {
                Picasso.with(getApplicationContext())
                        .load("http://manishp.info/" + currentUser.ProfileImagePath)
                        .into(imageViewProfileImage);
            }
        }
    }

    private void LoadingAnimation(final Boolean showScreen)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (dialog == null)
                    dialog = new ProgressDialog(EditActivityProfileImage.this);

                dialog.setMessage("Loading");
                dialog.setCancelable(false);
                dialog.setInverseBackgroundForced(false);
                if (showScreen) {
                    dialog.show();
                } else {
                    dialog.hide();
                }
            }
        });
    }

    @Override
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final Intent testIntent = data;

        if (requestCode == 1 && resultCode == Activity.RESULT_OK)

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
                        imageViewProfileImage.setImageBitmap(bitmap);
                        Button_TakePhoto.setText("Update Photo");


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });
            // We need to recyle unused bitmaps
        else {
            if (requestCode == 2 && resultCode == Activity.RESULT_OK)

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final int IMAGE_MAX_SIZE = 1000;
                        try {
                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inJustDecodeBounds = true;

                            Bitmap photo = (Bitmap) testIntent.getExtras().get("data");
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
                            imageViewProfileImage.setImageBitmap(bitmap);
                            Button_TakePhoto.setText("Update Photo");


                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                });
        }
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
                    startActivityForResult(i, 1);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



    private void PostImages() {
        LoadingAnimation(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String[] replies;
                    FTPClient ftpClient = new FTPClient();
                    //ftpClient.enterLocalPassiveMode();
                    //ftpClient.setConnectTimeout(5);
                    ftpClient.connect("ftp.manishp.info");
                    ftpClient.login("manishpi", "network1@3");
                           /* String[] replies = ftpClient.getReplyStrings();
                            if (replies != null && replies.length > 0) {
                                for (String aReply : replies) {
                                    System.out.println("SERVER: " + aReply);
                                }
                            }*/
                    //ftpClient.changeWorkingDirectory(serverRoad);
                    Boolean directoryExists = ftpClient.changeWorkingDirectory("images/" + currentUser.UserId + "/ProfileImage");
                    if (directoryExists) {

                       FTPFile[] ftpFiles = ftpClient.listFiles();

                        for (int i=0;i<= ftpFiles.length-1; i++)
                        {
                            FTPFile ftpfile =   ftpFiles[i];
                            if (ftpfile.isFile() && ftpfile.getSize() >0)
                            {
                                ftpClient.deleteFile("images/" + currentUser.UserId + "/ProfileImage/" + ftpfile.getName());
                            }
                        }

                        replies = ftpClient.getReplyStrings();
                        if (replies != null && replies.length > 0) {
                            for (String aReply : replies) {
                                System.out.println("SERVER: " + aReply);
                            }
                        }
                    }
                    if (!directoryExists) {

                        Boolean parentDirectoryExists = ftpClient.changeWorkingDirectory("images/" + currentUser.UserId);

                        replies = ftpClient.getReplyStrings();
                        if (replies != null && replies.length > 0) {
                            for (String aReply : replies) {
                                System.out.println("SERVER: " + aReply);
                            }
                        }

                        if (!parentDirectoryExists)
                        {
                            ftpClient.makeDirectory("images/" + currentUser.UserId);
                            ftpClient.changeWorkingDirectory("images/" + currentUser.UserId);

                            replies = ftpClient.getReplyStrings();
                            if (replies != null && replies.length > 0) {
                                for (String aReply : replies) {
                                    System.out.println("SERVER: " + aReply);
                                }
                            }
                        }
                        //ftpClient.makeDirectory("images/" + currentUser.UserId);
                        boolean childDirExist = ftpClient.changeWorkingDirectory("ProfileImage");
                        if (!childDirExist) {
                            ftpClient.makeDirectory("ProfileImage");
                            ftpClient.changeWorkingDirectory("ProfileImage");
                        }

                              replies = ftpClient.getReplyStrings();
                                if (replies != null && replies.length > 0) {
                                    for (String aReply : replies) {
                                        System.out.println("SERVER: " + aReply);
                                    }
                                }
                        //ftpClient.changeWorkingDirectory("images/" + currentUser.UserId + "/" + "ProfileImage");
                    }


                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    BufferedInputStream buffIn = null;
                    Bitmap bitmapnew = bitmap;
                    int randomnumber = (int)(Math.random()*10000);
                    String fileName = randomnumber + ".png";
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmapnew.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
                    buffIn = new BufferedInputStream(bs);
                    ftpClient.enterLocalPassiveMode();
                    ftpClient.storeFile(fileName, buffIn);
                    buffIn.close();
                    ftpClient.logout();
                    ftpClient.disconnect();
                    currentUser.ProfileImagePath = "images/" + currentUser.UserId + "/" + "ProfileImage/" + fileName;
                    String url = "http://manishp.info/CPDataService.svc/EditUser";
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            url, new JSONObject(CurrentUserHelper.CreateParameters(currentUser)),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    LoadingAnimation(false);
                                    SessionHelper.SetCurrentUser(getApplicationContext(), CurrentUserHelper.GetUserFromJSON(response));
                                    //Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                                    //startActivity(intent);
                                    onBackPressed();
                                    EditActivityProfileImage.this.finish();
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String test = error.getMessage();
                            LoadingAnimation(false);
                            //VolleyLog.d(TAG, "Error: " + error.getMessage());
                            // hide the progress dialog
                            //pDialog.hide();
                        }
                    });
                    VolleySingle.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjReq);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
