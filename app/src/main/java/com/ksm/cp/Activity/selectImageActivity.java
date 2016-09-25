package com.ksm.cp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class selectImageActivity extends AppCompatActivity {
    private int RESULT_LOAD_IMAGE = 1;
    private final int REQUEST_CODE = 1;
    private Bitmap resultBitmap = null;
    ImageView profile_image;
    Button UpdateProfileImage;
    CurrentUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        UpdateProfileImage = (Button) findViewById(R.id.UpdateProfileImage);
        UpdateProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostImages();
            }
        });

        currentUser = SessionHelper.GetCurrentUser(getApplicationContext());
    }


    private void PostImages() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {


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
                        ftpClient.removeDirectory("images/" + currentUser.UserId + "/" + "ProfileImage");
                        directoryExists = false;
                    }
                    if (!directoryExists) {

                        ftpClient.makeDirectory("images/" + currentUser.UserId + "/" + "ProfileImage");
                               /* replies = ftpClient.getReplyStrings();
                                if (replies != null && replies.length > 0) {
                                    for (String aReply : replies) {
                                        System.out.println("SERVER: " + aReply);
                                    }
                                }*/
                        ftpClient.changeWorkingDirectory("images/" + currentUser.UserId + "/" + "ProfileImage");
                    }


                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    BufferedInputStream buffIn = null;
                    Bitmap bitmap = resultBitmap;
                    String fileName = currentUser.UserId + ".png";
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
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
                                    SessionHelper.SetCurrentUser(getApplicationContext(), CurrentUserHelper.GetUserFromJSON(response));
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String test = error.getMessage();
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final Intent testIntent = data;

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final int IMAGE_MAX_SIZE = 1000;
                    try {
                        BitmapFactory.Options o = new BitmapFactory.Options();
                        o.inJustDecodeBounds = true;

                        InputStream stream = getContentResolver().openInputStream(
                                testIntent.getData());

                        resultBitmap = BitmapFactory.decodeStream(stream, null, o);
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
                        resultBitmap = BitmapFactory.decodeStream(stream, null, o2);
                        stream.close();
                        profile_image.setImageBitmap(resultBitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });
        // We need to recyle unused bitmaps


        super.onActivityResult(requestCode, resultCode, data);

    }

}
