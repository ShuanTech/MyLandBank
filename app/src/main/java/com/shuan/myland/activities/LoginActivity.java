package com.shuan.myland.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.shuan.myland.Main;
import com.shuan.myland.R;
import com.shuan.myland.parser.HttpUrlConnectionParser;
import com.shuan.myland.parser.php;
import com.shuan.myland.utils.Common;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import mehdi.sakout.fancybuttons.FancyButton;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private EditText usr, pass;
    private FancyButton login;
    private String lUsr,lPass,usrId,usrEmpId,usrFullName,usrName,usrPass,usrAddr,usrPhNo,usrEmail,usrImg,usrLevel;
    private php call = new php();
    private HashMap<String, String> loginData;
    private Common mApp;
    private ProgressDialog pDialog;
    private HttpUrlConnectionParser parser = new HttpUrlConnectionParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mApp = (Common) getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setTitle("");
        toolbar.setTitle("My Land Bank");

        usr = (EditText) findViewById(R.id.usr);
        pass = (EditText) findViewById(R.id.pass);
        login = (FancyButton) findViewById(R.id.login);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login:
                if (usr.getText().length() == 0) {
                    usr.setError("Enter User Name");
                } else if (pass.getText().length() == 0) {
                    pass.setError("Enter Password");
                } else {
                    lPass = pass.getText().toString();
                    lUsr = usr.getText().toString();
                    new Login().execute();
                }
                break;
        }

    }

    public class Login extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setMessage("Login...");
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            loginData = new HashMap<String, String>();
            loginData.put("usr", lUsr);
            loginData.put("pass", lPass);

            try{
                JSONObject json=parser.makeHttpUrlConnection(call.login,loginData);
                int succ=json.getInt("success");
                if(succ==0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Error");
                            builder.setMessage("Login Failed...Try Again!");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    usr.setText("");
                                    pass.setText("");
                                }
                            }).show();
                        }
                    });
                }else {
                    JSONArray jsonArray=json.getJSONArray("login");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject child=jsonArray.getJSONObject(0);
                        usrId=child.optString("id");
                        usrEmpId=child.optString("emp_id");
                        usrFullName=child.optString("name");
                        usrName=child.optString("username");
                        usrPass=child.optString("password");
                        usrAddr=child.optString("address");
                        usrPhNo=child.optString("ph_no");
                        usrEmail=child.optString("email_id");
                        usrImg=child.optString("pro_pic");
                        usrLevel=child.optString("level");
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Success");
                            builder.setMessage("Successfully Login!...");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mApp.getSharedPreferences().edit().putString(Common.USR_ID,usrId).commit();
                                    mApp.getSharedPreferences().edit().putString(Common.USR_EMPID,usrEmpId).commit();
                                    mApp.getSharedPreferences().edit().putString(Common.USR_FULLNAME,usrFullName).commit();
                                    mApp.getSharedPreferences().edit().putString(Common.USR_NAME,usrName).commit();
                                    mApp.getSharedPreferences().edit().putString(Common.USR_PASS,usrPass).commit();
                                    mApp.getSharedPreferences().edit().putString(Common.USR_ADDR,usrAddr).commit();
                                    mApp.getSharedPreferences().edit().putString(Common.USR_PHNO,usrPhNo).commit();
                                    mApp.getSharedPreferences().edit().putString(Common.USR_EMAIL,usrEmail).commit();
                                    mApp.getSharedPreferences().edit().putString(Common.USR_IMG, usrImg).commit();
                                    mApp.getSharedPreferences().edit().putBoolean(Common.Login, true).commit();
                                    startActivity(new Intent(LoginActivity.this, Main.class));
                                    finish();
                                }
                            }).show();
                        }
                    });
                }
            }catch (Exception e){}


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.cancel();
        }
    }
}
