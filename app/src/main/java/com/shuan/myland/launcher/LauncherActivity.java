package com.shuan.myland.launcher;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.shuan.myland.Main;
import com.shuan.myland.R;
import com.shuan.myland.activities.LoginActivity;
import com.shuan.myland.parser.HttpUrlConnectionParser;
import com.shuan.myland.parser.php;
import com.shuan.myland.utils.Common;
import com.shuan.myland.utils.NetworkUtil;

import org.json.JSONObject;

import java.util.HashMap;

public class LauncherActivity extends AppCompatActivity {

    private Common mApp;
    private Context context;
    private static int SPLASH_TIME_OUT = 2000;
    private AlertDialog.Builder builder;
    private HashMap<String,String> checkData;
    private HttpUrlConnectionParser parser=new HttpUrlConnectionParser();
    private php call=new php();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        context=getApplicationContext();
        mApp= (Common) context.getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        if(NetworkUtil.getConnectivityStatus(getApplicationContext())==0){

            showAlert();

        }else{
            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity

                    //Toast.makeText(getApplicationContext(),Boolean.toString(mApp.getSharedPreferences().getBoolean(Common.Login,false)),Toast.LENGTH_SHORT).show();

                    if(mApp.getSharedPreferences().getBoolean(Common.Login, false)==false){

                        //Toast.makeText(getApplicationContext(),"Not Login",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LauncherActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }else {

                        new checkUserPass().execute();
                    }
                }
            }, SPLASH_TIME_OUT);
        }

    }


    public class checkUserPass extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            checkData=new HashMap<String,String>();
            checkData.put("usr",mApp.getSharedPreferences().getString(Common.USR_NAME,""));
            checkData.put("pass",mApp.getSharedPreferences().getString(Common.USR_PASS,""));
            try{

                JSONObject json=parser.makeHttpUrlConnection(call.login,checkData);
                int succ=json.getInt("success");

                if(succ==0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"UserName or Password is incorrect.Login Again",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        }
                    });
                }else{
                    Intent i = new Intent(LauncherActivity.this, Main.class);
                    startActivity(i);
                    finish();
                }

            }catch (Exception e){}
            return null;
        }
    }







    private void showAlert() {
        builder=new AlertDialog.Builder(LauncherActivity.this)
                .setTitle("No Internet")
                .setCancelable(false)
                .setMessage("No Internet Connection Available.\n Do you want to try again?");

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.cancel();
            }
        }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                checkConnection();
            }
        }).show();

    }

    private void checkConnection() {

        if(NetworkUtil.getConnectivityStatus(getApplicationContext())==0){

            showAlert();

        }else{
            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity

                    //Toast.makeText(getApplicationContext(),Boolean.toString(mApp.getSharedPreferences().getBoolean(Common.Login,false)),Toast.LENGTH_SHORT).show();

                    if(mApp.getSharedPreferences().getBoolean(Common.Login, false)==false){

                        //Toast.makeText(getApplicationContext(),"Not Login",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LauncherActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }else {
                        new checkUserPass().execute();

                    }
                }
            }, SPLASH_TIME_OUT);
        }

    }
}
