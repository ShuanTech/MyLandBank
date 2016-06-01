package com.shuan.myland;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shuan.myland.activities.NewsActivity;
import com.shuan.myland.activities.ProfileActivity;
import com.shuan.myland.property.NewProjectActivity;
import com.shuan.myland.search.ProjectSearchResultActivity;
import com.shuan.myland.search.PropertySearchActivity;
import com.shuan.myland.setting.SettingsActivity;
import com.shuan.myland.utils.Common;

import de.hdodenhof.circleimageview.CircleImageView;
import mehdi.sakout.fancybuttons.FancyButton;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Common mApp;
    private Toolbar toolbar;
    private boolean exit = false;
    private DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FrameLayout mContentFrame;
    private FancyButton projects, newProject, upProject;

    private static final String PREFERENCES_FILE = "mlb_settings";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private int mCurrentSelectedPosition;
    private TextView usrName;
    private CircleImageView usrImg;
    private DisplayImageOptions options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mApp = (Common) getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.root);

        mUserLearnedDrawer = Boolean.valueOf(readSharedSetting(this, PREF_USER_LEARNED_DRAWER, "false"));

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });


        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mContentFrame = (FrameLayout) findViewById(R.id.nav_contentframe);
        ImageView img = (ImageView) findViewById(R.id.back);

        projects = (FancyButton) findViewById(R.id.projects);
        newProject = (FancyButton) findViewById(R.id.new_project);
        upProject = (FancyButton) findViewById(R.id.upcoming_project);

        View header = mNavigationView.inflateHeaderView(R.layout.drawer_header);

        usrName = (TextView) header.findViewById(R.id.usr_name);
        usrImg = (CircleImageView) header.findViewById(R.id.user_img);

        mNavigationView.setNavigationItemSelectedListener(this);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.back, options);

        img.setImageBitmap(bm);

        projects.setOnClickListener(this);
        newProject.setOnClickListener(this);
        upProject.setOnClickListener(this);


        usrName.setText(mApp.getSharedPreferences().getString(Common.USR_FULLNAME, ""));
        setImage(mApp.getSharedPreferences().getString(Common.USR_IMG, ""), usrImg);


       /* NavigationDrawer drawer = (NavigationDrawer) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        drawer.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.root), toolbar);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search:
                mApp.getSharedPreferences().edit().putString(Common.PRO_LEVEL, "search").commit();
                Intent in = new Intent(getApplicationContext(), PropertySearchActivity.class);
                startActivity(in);
                break;
            case R.id.profile:
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.home:
                mCurrentSelectedPosition = 0;
                mDrawerLayout.closeDrawers();
                break;
            case R.id.project:
                startActivity(new Intent(getApplicationContext(), ProjectSearchResultActivity.class));
                mDrawerLayout.closeDrawers();
                break;
            case R.id.new_project:
                startActivity(new Intent(getApplicationContext(), NewProjectActivity.class));
                mDrawerLayout.closeDrawers();
                break;
            case R.id.upcoming_project:
                startActivity(new Intent(getApplicationContext(), NewProjectActivity.class));
                mDrawerLayout.closeDrawers();
                break;
           /* case R.id.localities:
                mDrawerLayout.closeDrawers();
                break;*/
            case R.id.project_news:
                startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                mDrawerLayout.closeDrawers();
                break;
            case R.id.setting:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                mDrawerLayout.closeDrawers();
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.projects:
                startActivity(new Intent(Main.this, ProjectSearchResultActivity.class));
                break;
            case R.id.new_project:
                startActivity(new Intent(getApplicationContext(), NewProjectActivity.class));
                break;
            case R.id.upcoming_project:
                startActivity(new Intent(getApplicationContext(), NewProjectActivity.class));
                break;
        }
    }

    public void setImage(String path, CircleImageView img) {

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.drawable.user)
                .showImageForEmptyUri(R.drawable.user)
                .showImageOnFail(R.drawable.user)
                .build();

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
}
