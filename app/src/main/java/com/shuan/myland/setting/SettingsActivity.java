package com.shuan.myland.setting;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shuan.myland.R;
import com.shuan.myland.utils.Common;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity implements Preference.OnPreferenceClickListener{

    public SwitchPreference notify;
    public Preference about,contact,share,version;
    public Common mApp;
    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
   */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mApp= (Common) getApplicationContext();
        super.onCreate(savedInstanceState);
        setupActionBar();
        addPreferencesFromResource(R.xml.pref_setting);
        notify= (SwitchPreference) findPreference("notify");
        about=findPreference("about");
        contact=findPreference("contact");
        share=findPreference("share");
        version=findPreference("ver");
        notify.setOnPreferenceClickListener(this);
        about.setOnPreferenceClickListener(this);
        contact.setOnPreferenceClickListener(this);

        PackageManager manager = getApplicationContext().getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(
                    getApplicationContext().getPackageName(), 0);

            version.setTitle("My Land Bank Version"+" "+info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return false;

    }


    @Override
    public boolean onPreferenceClick(Preference preference) {

        if(preference.getKey().equalsIgnoreCase("notify")){
            if(mApp.getSharedPreferences().getBoolean(Common.NOTIFY,true)==true){
                mApp.getSharedPreferences().edit().putBoolean(Common.NOTIFY,false).commit();
                Toast.makeText(getApplicationContext(),"Notification turned ON Successfully",Toast.LENGTH_SHORT).show();
            }else {
                mApp.getSharedPreferences().edit().putBoolean(Common.NOTIFY,true).commit();
                Toast.makeText(getApplicationContext(),"Notification turned OFF Successfully",Toast.LENGTH_SHORT).show();
            }
        }else if(preference.getKey().equalsIgnoreCase("about")){
            showAlertDialog();
        }else if(preference.getKey().equalsIgnoreCase("contact")){
            startActivity(new Intent(getApplicationContext(),ContactUs.class));
        }else if(preference.getKey().equalsIgnoreCase("share")){
            shareApp();
        }

        return true;
    }

    private void shareApp() {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("android.intent.extra.SUBJECT", getResources().getString(R.string.app_name));
        intent.putExtra("android.intent.extra.TEXT", getString(R.string.app_url));
        startActivity(Intent.createChooser(intent, "Share the App"));
    }

    private void showAlertDialog() {

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myScrollView = inflater.inflate(R.layout.read_more, null, false);
            TextView tv = (TextView) myScrollView
                    .findViewById(R.id.read_more_txt);
            tv.setText(R.string.about_us);
            new AlertDialog.Builder(this).setView(myScrollView)
                    .setTitle("About Us")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @TargetApi(11)
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }

                    }).show();

    }
}
