package com.shuan.myland.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shuan.myland.R;
import com.shuan.myland.parser.HttpUrlConnectionParser;
import com.shuan.myland.parser.php;
import com.shuan.myland.utils.Common;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Common mApp;
    private ImageButton edit;
    private CircleImageView usrImg;
    private TextView usrName, level;
    private RelativeLayout row;
    private DisplayImageOptions options;
    private php call = new php();
    private HashMap<String, String> profileData;
    private String usrFullName,usrAddr, usrPhNo, usrEmail, usrLevel, usrimg;
    private ProgressBar progressBar;
    private HttpUrlConnectionParser parser = new HttpUrlConnectionParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mApp = (Common) getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        usrName = (TextView) findViewById(R.id.usr_name);
        level = (TextView) findViewById(R.id.level);
        usrImg = (CircleImageView) findViewById(R.id.user_img);
        edit = (ImageButton) findViewById(R.id.edit);
        row = (RelativeLayout) findViewById(R.id.row);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        new getProfiledetails().execute();
        progressBar.setVisibility(View.VISIBLE);

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.drawable.user)
                .showImageForEmptyUri(R.drawable.user)
                .showImageOnFail(R.drawable.user)
                .build();


        edit.setOnClickListener(this);


    }



    public class getProfiledetails extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            profileData = new HashMap<String, String>();
            profileData.put("id", mApp.getSharedPreferences().getString(Common.USR_ID, ""));

            try {
                JSONObject json = parser.makeHttpUrlConnection(call.getProfile, profileData);

                int succ = json.getInt("success");
                if (succ == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mApp.getSharedPreferences().getString(Common.USR_LEVEL, "").equalsIgnoreCase("silver")) {
                                row.setBackgroundColor(getResources().getColor(R.color.silver));
                            } else if (mApp.getSharedPreferences().getString(Common.USR_LEVEL, "").equalsIgnoreCase("gold")) {
                                row.setBackgroundColor(getResources().getColor(R.color.gold));
                            } else {
                                row.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            }
                            usrName.setText(mApp.getSharedPreferences().getString(Common.USR_FULLNAME, ""));
                            level.setText(mApp.getSharedPreferences().getString(Common.USR_LEVEL, ""));
                            setImage(mApp.getSharedPreferences().getString(Common.USR_IMG, ""),usrImg);

                        }
                    });
                }else{
                    JSONArray jsonArray=json.getJSONArray("profile");
                    JSONObject child=jsonArray.getJSONObject(0);
                    usrFullName=child.optString("name");
                    usrAddr=child.optString("address");
                    usrPhNo=child.optString("ph_no");
                    usrEmail=child.optString("email_id");
                    usrimg=child.optString("pro_pic");
                    usrLevel=child.optString("level");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (usrLevel.equalsIgnoreCase("silver")) {
                                row.setBackgroundColor(getResources().getColor(R.color.silver));
                            } else if (usrLevel.equalsIgnoreCase("gold")) {
                                row.setBackgroundColor(getResources().getColor(R.color.gold));
                            } else {
                                row.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            }
                            usrName.setText(usrFullName);
                            level.setText(usrLevel);
                            setImage(usrimg,usrImg);
                        }
                    });

                }
            } catch (Exception e) {
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }


    public void setImage(String path, CircleImageView img) {
        ImageLoader.getInstance().displayImage(path, img, options, new SimpleImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                super.onLoadingStarted(imageUri, view);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                super.onLoadingCancelled(imageUri, view);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String s, View view, int i, int i1) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit:
                Intent in=new Intent(getApplicationContext(), EditProfileActivity.class);
                in.putExtra("name",usrFullName);
                in.putExtra("email",usrEmail);
                in.putExtra("phno",usrPhNo);
                in.putExtra("addr",usrAddr);
                in.putExtra("level",usrLevel);
                in.putExtra("img",usrimg);
                startActivity(in);
                break;
        }
    }
}
