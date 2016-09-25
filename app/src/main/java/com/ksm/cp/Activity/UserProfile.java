package com.ksm.cp.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ksm.cp.ActivityLogin;
import com.ksm.cp.Helper.LocationHelper;
import com.ksm.cp.Helper.SessionHelper;
import com.ksm.cp.Objects.CurrentUser;
import com.ksm.cp.R;
import com.squareup.picasso.Picasso;


public class UserProfile extends AppCompatActivity {

    ImageView ImageViewUserProfilePic;
    // LinearLayout LinerLayoutSettings;
    TextView TextViewJoinedDate;
    TextView TextViewUserName;
    TextView TextViewUserNameForEdit;
    TextView TextViewUserEmail;
    RelativeLayout RelativeLayoutDisplayName;
    RelativeLayout RelativeLayoutLocation;
    RelativeLayout RelativeLayoutEmail;
    RelativeLayout RelativelayoutLogOut;
    TextView TextViewUserLocation;

    de.hdodenhof.circleimageview.CircleImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Home");

        TextViewUserName = (TextView) findViewById(R.id.TextViewUserName);
        TextViewJoinedDate = (TextView) findViewById(R.id.TextViewJoinedDate);
        //LinerLayoutSettings = (LinearLayout) findViewById(R.id.LinerLayoutSettings);
        //ImageViewUserProfilePic = (ImageView) findViewById(R.id.ImageViewUserProfilePic);
        TextViewUserNameForEdit = (TextView) findViewById(R.id.TextViewUserNameForEdit);
        TextViewUserEmail = (TextView) findViewById(R.id.TextViewUserEmail);
        RelativeLayoutDisplayName = (RelativeLayout) findViewById(R.id.RelativeLayoutDisplayName);
        RelativeLayoutLocation = (RelativeLayout) findViewById(R.id.RelativeLayoutLocation);
        RelativeLayoutEmail = (RelativeLayout) findViewById(R.id.RelativeLayoutEmail);
        RelativelayoutLogOut = (RelativeLayout) findViewById(R.id.RelativelayoutLogOut);
        TextViewUserLocation = (TextView) findViewById(R.id.TextViewUserLocation);
        profile_image = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.profile_image);
        // RelativeLayoutDisplayName.setBackgroundResource(R.drawable.backgroundselector);

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.EditActivityProfileImage.class);
                startActivity(intent);
            }
        });


        RelativelayoutLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(v.getContext())
                        .setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SessionHelper.RemoveAllSharedPref(getApplicationContext());
                                Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
                                startActivity(intent);
                                UserProfile.this.finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        RelativeLayoutDisplayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.EditActivityDisplayName.class);
                startActivity(intent);
            }
        });


        RelativeLayoutLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.EditActivityLocation.class);
                startActivity(intent);
            }
        });

        RelativeLayoutEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.ksm.cp.Activity.EditActivityEmailAddress.class);
                startActivity(intent);
            }
        });


        PopulateUI();

    }

    private void PopulateUI()
    {
        CurrentUser currentUser = SessionHelper.GetCurrentUser(getApplicationContext());
        if (currentUser != null) {
            TextViewJoinedDate.setText(currentUser.CreatedDate);
            TextViewUserName.setText(currentUser.DisplayName);
            TextViewUserNameForEdit.setText(currentUser.DisplayName);
            TextViewUserEmail.setText(currentUser.Email);
            if (!currentUser.ProfileImagePath.isEmpty()) {
                Picasso.with(getApplicationContext())
                        .load("http://manishp.info/" + currentUser.ProfileImagePath)
                        .into(profile_image);
            }
        }

        TextViewUserLocation.setText(SessionHelper.GetCurrentUser(getApplicationContext()).location.LocationName);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

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

    @Override
    public void onRestart() {
        super.onRestart();
        PopulateUI();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
    }
}
