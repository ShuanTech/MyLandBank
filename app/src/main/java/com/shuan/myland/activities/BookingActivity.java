package com.shuan.myland.activities;


import android.app.FragmentTransaction;
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
import com.shuan.myland.fragment.DateDialog;
import com.shuan.myland.parser.HttpUrlConnectionParser;
import com.shuan.myland.parser.php;
import com.shuan.myland.utils.Common;

import org.json.JSONObject;

import java.util.HashMap;

import mehdi.sakout.fancybuttons.FancyButton;

public class BookingActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private EditText clntName, clntPhno, advance, txtDate;
    private FancyButton book;
    private String cName, cPhno, cAmnt, cdate, id, pro_id;
    private php call = new php();
    private HashMap<String, String> bookingData;
    private Common mApp;
    private ProgressDialog pDialog;
    private HttpUrlConnectionParser parser = new HttpUrlConnectionParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mApp = (Common) getApplicationContext();
        pro_id = getIntent().getStringExtra("pro_id");
        id = getIntent().getStringExtra("id");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        setTitle("");
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setTitle("Property Booking");

        clntName = (EditText) findViewById(R.id.client_name);
        clntPhno = (EditText) findViewById(R.id.ph);
        advance = (EditText) findViewById(R.id.advance);
        txtDate = (EditText) findViewById(R.id.confrm_date);
        book = (FancyButton) findViewById(R.id.book);
        txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DateDialog dialog = new DateDialog(v);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                    txtDate.setFocusable(false);

                }
            }
        });

        book.setOnClickListener(this);
        txtDate.setOnClickListener(this);
        //Toast.makeText(getApplicationContext(),id+pro_id,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confrm_date:
                DateDialog dialog = new DateDialog(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, "DatePicker");
                break;
            case R.id.book:
                validate();
                break;
        }
    }

    private void validate() {
        if (clntName.getText().length() == 0) {
            clntName.setError("Enter Client Name");
        } else if (clntPhno.getText().length() == 0) {
            clntPhno.setError("Enter Client Ph.No");
        } else if (txtDate.getText().length() == 0) {
            txtDate.setError("Select Conform Date");
        } else {
            cName = clntName.getText().toString();
            cPhno = clntPhno.getText().toString();
            if (advance.getText().length() != 0) {
                cAmnt = advance.getText().toString();
            } else {
                cAmnt = "0";
            }
            cdate = txtDate.getText().toString();
            new Booking().execute();
        }
    }

    public class Booking extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BookingActivity.this);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setMessage("Booking...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            bookingData = new HashMap<String, String>();
            bookingData.put("name", cName);
            bookingData.put("phno", cPhno);
            bookingData.put("advance_amnt", cAmnt);
            bookingData.put("confrm_date", cdate);
            bookingData.put("agent_id", mApp.getSharedPreferences().getString(Common.USR_ID, ""));
            bookingData.put("pro_id", pro_id);
            bookingData.put("id", id);

            try {

                JSONObject json = parser.makeHttpUrlConnection(call.booking, bookingData);
                int succ = json.getInt("success");

                if (succ == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this);
                            builder.setTitle("Error");
                            builder.setMessage("Booking Failed...Try Again!");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    clntName.setText("");
                                    clntPhno.setText("");
                                    advance.setText("");
                                    txtDate.setText("");
                                }
                            }).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this);
                            builder.setTitle("Success");
                            builder.setMessage("Successfully Booked");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(BookingActivity.this, Main.class));
                                    finish();
                                }
                            }).show();
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
            pDialog.cancel();
        }
    }
}
