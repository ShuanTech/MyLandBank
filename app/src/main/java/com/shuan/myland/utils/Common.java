package com.shuan.myland.utils;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.multidex.MultiDex;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.shuan.myland.parser.HttpUrlConnectionParser;
import com.shuan.myland.parser.php;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;

public class Common extends Application {


    private static SharedPreferences mSharedPreferences;
    public static final String Login = "login";
    public static final String USR_ID = "usr_id";
    public static final String USR_EMPID = "usr_empid";
    public static final String USR_FULLNAME = "usr_fullname";
    public static final String USR_NAME = "usr_name";
    public static final String USR_PASS = "usr_pass";
    public static final String USR_ADDR = "usr_addr";
    public static final String USR_PHNO = "usr_phno";
    public static final String USR_EMAIL = "usr_email";
    public static final String USR_IMG = "usr_img";
    public static final String USR_LEVEL = "usr_level";
    public static final String PRO_LEVEL = "pro_level";
    public static final String PRO_LOC = "pro_loc";
    public static final String NOTIFY = "notify";
    private Context mContext;
    private DisplayImageOptions mDisplayImageOptions;
    private ImageLoader mImageLoader;
    private ImageLoaderConfiguration mImageLoaderConfiguration;
    private Picasso mPicasso;
    private HashMap<String, String> saveData;
    private HttpUrlConnectionParser parser = new HttpUrlConnectionParser();
    private String result;
    PackageInfo info = null;
    private String update = null;
    public static final String Version = "version";


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mSharedPreferences = this.getSharedPreferences("com.shuan.myland", MODE_PRIVATE);
        //Picasso.
        mPicasso = new Picasso.Builder(mContext).build();
        //ImageLoader.
        mImageLoader = ImageLoader.getInstance();
        mImageLoaderConfiguration = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSizePercentage(13)
                .imageDownloader(new ByteArrayUniversalImageLoader(mContext))
                .build();
        mImageLoader.init(mImageLoaderConfiguration);


        PackageManager manager = mContext.getPackageManager();
        try {
            info = manager.getPackageInfo(
                    mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        new chkVersion().execute();
    }


    public class chkVersion extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> verData = new HashMap<String, String>();
            verData.put("ver", info.versionName);


            try {
                JSONObject json = parser.makeHttpUrlConnection(php.chkVer, verData);

                int succ = json.getInt("success");

                if (succ == 0) {
                    update = "false";
                } else {
                    update = "true";
                }

            } catch (Exception e) {
            }

            return update;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                getSharedPreferences().edit().putString(Common.Version, update).commit();

            }
        }
    }

    /*
     *Getter
     */

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public Picasso getPicasso() {
        return mPicasso;
    }


    /*
    * Returns the status bar height for the current layout configuration.
    */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    /*
     * Returns the navigation bar height for the current layout configuration.
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }

        return 0;
    }
}
