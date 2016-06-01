package com.shuan.myland.search;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.shuan.myland.R;
import com.shuan.myland.adapter.LocationAdapter;
import com.shuan.myland.list.LocationList;
import com.shuan.myland.parser.HttpUrlConnectionParser;
import com.shuan.myland.parser.php;
import com.shuan.myland.rangebar.RangeBar;
import com.shuan.myland.utils.Common;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PropertySearchActivity extends AppCompatActivity implements View.OnClickListener, RangeBar.OnRangeBarChangeListener {
    private Toolbar toolbar;
    private AutoCompleteTextView location;
    private ArrayList<LocationList> locationList;
    private LocationAdapter adapter;
    private TextView flat, house, plot, farms, one_bhk, two_bhk, thr_bhk, fur_bhk, othr_bhk;
    private boolean fclick = true, hclick = true, pclick = true, faclick = true;
    private boolean obhk = true, tbhk = true, thbhk = true, fbhk = true, otbhk = true;
    private RelativeLayout row3, row4;
    private Button search;
    private TextView from, to, frmArea, toArea;
    private RangeBar rangeBar, areaBar;
    private String frmRate, frma;
    private String toRate, toa;
    public String one = "", two = "", three = "", four = "", other = "";
    public String sFlat = "", sHouse = "", sPlot = "", sFarms = "";
    public HttpUrlConnectionParser parser = new HttpUrlConnectionParser();
    public php call = new php();
    public HashMap<String, String> locData;
    public String level;
    public Common mApp;
    public Context mContext;


    public PropertySearchActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();
        mApp = (Common) mContext.getApplicationContext();
        setContentView(R.layout.activity_property_search);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Search Properties");
        toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        location = (AutoCompleteTextView) findViewById(R.id.location);
        flat = (TextView) findViewById(R.id.flat);
        house = (TextView) findViewById(R.id.house);
        plot = (TextView) findViewById(R.id.plot);
        farms = (TextView) findViewById(R.id.farms);
        one_bhk = (TextView) findViewById(R.id.o_bhk);
        two_bhk = (TextView) findViewById(R.id.t_bhk);
        thr_bhk = (TextView) findViewById(R.id.th_bhk);
        fur_bhk = (TextView) findViewById(R.id.f_bhk);
        othr_bhk = (TextView) findViewById(R.id.ot_bhk);
        rangeBar = (RangeBar) findViewById(R.id.budgetBar);
        areaBar = (RangeBar) findViewById(R.id.areabar);
        frmArea = (TextView) findViewById(R.id.fromarea);
        toArea = (TextView) findViewById(R.id.toarea);
        from = (TextView) findViewById(R.id.from);
        to = (TextView) findViewById(R.id.to);

        frma = "0";
        toa = "50000";
        frmRate = "0";
        toRate = "150000000";
        from.setText(getResources().getString(R.string.Rs) + " " + "0");
        to.setText(getResources().getString(R.string.Rs) + " " + "15+ cr");
        frmArea.setText("0 sqft");
        toArea.setText("50000+ sqft");


        row3 = (RelativeLayout) findViewById(R.id.row3);
        row4 = (RelativeLayout) findViewById(R.id.row4);

        search = (Button) findViewById(R.id.search);

        locationList = new ArrayList<LocationList>();


        flat.setOnClickListener(this);
        house.setOnClickListener(this);
        plot.setOnClickListener(this);
        farms.setOnClickListener(this);
        one_bhk.setOnClickListener(this);
        two_bhk.setOnClickListener(this);
        thr_bhk.setOnClickListener(this);
        fur_bhk.setOnClickListener(this);
        othr_bhk.setOnClickListener(this);
        search.setOnClickListener(this);

        rangeBar.setOnRangeBarChangeListener(this);
        areaBar.setOnRangeBarChangeListener(this);

        new GetLoc().execute();


    }


    public class GetLoc extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            locData = new HashMap<String, String>();
            locData.put("id", "id");

            try {

                JSONObject json = parser.makeHttpUrlConnection(php.pro_loc, locData);
                int succ = json.getInt("success");

                if (succ == 0) {
                } else {
                    JSONArray jsonArray = json.getJSONArray("location");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject child = jsonArray.getJSONObject(i);
                        String loc = child.optString("loc");
                        locationList.add(new LocationList(loc));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new LocationAdapter(getApplicationContext(), R.layout.search_item, R.id.location_name, locationList);
                            location.setAdapter(adapter);
                            location.setThreshold(1);

                            location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    TextView txt = (TextView) view.findViewById(R.id.location_name);
                                    location.setText(txt.getText().toString());
                                }
                            });
                        }
                    });

                }

            } catch (Exception e) {
            }
            return null;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flat:
               /* row4.setVisibility(View.INVISIBLE);
                row3.setVisibility(View.VISIBLE);*/
                if (fclick) {
                    flat.setBackgroundResource(R.drawable.selected_block);
                    sFlat = "flat";
                    fclick = false;
                } else {
                    flat.setBackgroundResource(R.drawable.text);
                    sFlat = "";
                    fclick = true;
                }
                break;
            case R.id.house:
                /*row4.setVisibility(View.INVISIBLE);
                row3.setVisibility(View.VISIBLE);*/
                if (hclick) {
                    house.setBackgroundResource(R.drawable.selected_block);
                    sHouse = "house/villa";
                    hclick = false;
                } else {
                    house.setBackgroundResource(R.drawable.text);
                    sHouse = "";
                    hclick = true;
                }
                break;
            case R.id.plot:
                if (pclick) {
                    plot.setBackgroundResource(R.drawable.selected_block);
                    sPlot = "plot";
                    pclick = false;
                   /* row4.setVisibility(View.VISIBLE);
                    row3.setVisibility(View.INVISIBLE);*/
                } else {
                    plot.setBackgroundResource(R.drawable.text);
                    sPlot = "";
                    pclick = true;
                   /* row4.setVisibility(View.INVISIBLE);
                    row3.setVisibility(View.VISIBLE);*/
                }
                break;
            case R.id.farms:
                if (faclick) {
                    farms.setBackgroundResource(R.drawable.selected_block);
                    sFarms = "farms";
                    faclick = false;
                    /*row4.setVisibility(View.VISIBLE);
                    row3.setVisibility(View.INVISIBLE);*/
                } else {
                    farms.setBackgroundResource(R.drawable.text);
                    sFarms = "";
                    faclick = true;
                    /*row4.setVisibility(View.INVISIBLE);
                    row3.setVisibility(View.VISIBLE);*/
                }
                break;
            case R.id.o_bhk:
                if (obhk) {
                    one_bhk.setBackgroundResource(R.drawable.selected_block);
                    one = "1BHK";
                    obhk = false;
                } else {
                    one_bhk.setBackgroundResource(R.drawable.text);
                    one = "";
                    obhk = true;
                }
                break;
            case R.id.t_bhk:
                if (tbhk) {
                    two_bhk.setBackgroundResource(R.drawable.selected_block);
                    two = "2BHK";
                    tbhk = false;
                } else {
                    two_bhk.setBackgroundResource(R.drawable.text);
                    two = "";
                    tbhk = true;
                }
                break;

            case R.id.th_bhk:
                if (thbhk) {
                    thr_bhk.setBackgroundResource(R.drawable.selected_block);
                    three = "3BHK";
                    thbhk = false;
                } else {
                    thr_bhk.setBackgroundResource(R.drawable.text);
                    three = "";
                    thbhk = true;
                }
                break;
            case R.id.f_bhk:
                if (fbhk) {
                    fur_bhk.setBackgroundResource(R.drawable.selected_block);
                    four = "4BHK";
                    fbhk = false;
                } else {
                    fur_bhk.setBackgroundResource(R.drawable.text);
                    four = "";
                    fbhk = true;
                }
                break;

            case R.id.ot_bhk:
                if (otbhk) {
                    othr_bhk.setBackgroundResource(R.drawable.selected_block);
                    other = "5BHK";
                    otbhk = false;
                } else {
                    othr_bhk.setBackgroundResource(R.drawable.text);
                    other = "";
                    otbhk = true;
                }
                break;

            case R.id.search:
                String type, br;

                if (location.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Select the Location to Search", Toast.LENGTH_SHORT).show();
                } else {

                    if (fclick && hclick && pclick && faclick/*oclick && sclick && cclick*/) {
                        type = "All";
                    } else {
                        type = sFlat + "," + sHouse + "," + sPlot + "," + sFarms/*sOffice + "," + sShop + "," + sCommercial*/;
                    }

                    if (obhk && tbhk && thbhk && fbhk && otbhk) {
                        br = "All";
                    } else {
                        br = one + "," + two + "," + three + "," + four + "," + other;
                    }

                    Intent in = new Intent(getApplicationContext(), PropertySearchResultActivity.class);
                    mApp.getSharedPreferences().edit().putString(Common.PRO_LOC, location.getText().toString()).commit();
                    in.putExtra("loc", location.getText().toString());
                    in.putExtra("type", type);
                    in.putExtra("from", frmRate);
                    in.putExtra("to", toRate);
                    if (row4.isShown()) {
                        in.putExtra("br", "area");
                        in.putExtra("frma", frma);
                        in.putExtra("toa", toa);
                    } else {
                        in.putExtra("br", br);
                    }


                    startActivity(in);

                }

                break;
        }
    }

    @Override
    public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {

        if (rangeBar.getId() == R.id.budgetBar) {

            switch (leftThumbIndex) {
                case 0:
                    from.setText(getResources().getString(R.string.Rs) + " " + 0);
                    frmRate = "0";
                    break;
                case 1:
                    from.setText(getResources().getString(R.string.Rs) + " " + 5 + " Lac");
                    frmRate = "500000";
                    break;
                case 2:
                    from.setText(getResources().getString(R.string.Rs) + " " + 10 + " Lac");
                    frmRate = "1000000";
                    break;
                case 3:
                    from.setText(getResources().getString(R.string.Rs) + " " + 20 + " Lac");
                    frmRate = "2000000";
                    break;
                case 4:
                    from.setText(getResources().getString(R.string.Rs) + " " + 30 + " Lac");
                    frmRate = "3000000";
                    break;
                case 5:
                    from.setText(getResources().getString(R.string.Rs) + " " + 40 + " Lac");
                    frmRate = "4000000";
                    break;
                case 6:
                    from.setText(getResources().getString(R.string.Rs) + " " + 50 + " Lac");
                    frmRate = "5000000";
                    break;
                case 7:
                    from.setText(getResources().getString(R.string.Rs) + " " + 60 + " Lac");
                    frmRate = "6000000";
                    break;
                case 8:
                    from.setText(getResources().getString(R.string.Rs) + " " + 70 + " Lac");
                    frmRate = "7000000";
                    break;
                case 9:
                    from.setText(getResources().getString(R.string.Rs) + " " + 80 + " Lac");
                    frmRate = "8000000";
                    break;
                case 10:
                    from.setText(getResources().getString(R.string.Rs) + " " + 90 + " Lac");
                    frmRate = "9000000";
                    break;
                case 11:
                    from.setText(getResources().getString(R.string.Rs) + " " + 1 + " cr");
                    frmRate = "10000000";
                    break;
                case 12:
                    from.setText(getResources().getString(R.string.Rs) + " " + 1.2 + " cr");
                    frmRate = "12000000";
                    break;
                case 13:
                    from.setText(getResources().getString(R.string.Rs) + " " + 1.4 + " cr");
                    frmRate = "14000000";
                    break;
                case 14:
                    from.setText(getResources().getString(R.string.Rs) + " " + 1.6 + " cr");
                    frmRate = "16000000";
                    break;
                case 15:
                    from.setText(getResources().getString(R.string.Rs) + " " + 1.8 + " cr");
                    frmRate = "18000000";
                    break;
                case 16:
                    from.setText(getResources().getString(R.string.Rs) + " " + 2 + " cr");
                    frmRate = "20000000";
                    break;
                case 17:
                    from.setText(getResources().getString(R.string.Rs) + " " + 2.3 + " cr");
                    frmRate = "23000000";
                    break;
                case 18:
                    from.setText(getResources().getString(R.string.Rs) + " " + 2.6 + " cr");
                    frmRate = "26000000";
                    break;
                case 19:
                    from.setText(getResources().getString(R.string.Rs) + " " + 3 + " cr");
                    frmRate = "30000000";
                    break;
                case 20:
                    from.setText(getResources().getString(R.string.Rs) + " " + 3.5 + " cr");
                    frmRate = "35000000";
                    break;
                case 21:
                    from.setText(getResources().getString(R.string.Rs) + " " + 4 + " cr");
                    frmRate = "40000000";
                    break;
                case 22:
                    from.setText(getResources().getString(R.string.Rs) + " " + 4.5 + " cr");
                    frmRate = "45000000";
                    break;
                case 23:
                    from.setText(getResources().getString(R.string.Rs) + " " + 5 + " cr");
                    frmRate = "50000000";
                    break;
                case 24:
                    from.setText(getResources().getString(R.string.Rs) + " " + 10 + " cr");
                    frmRate = "100000000";
                    break;
                case 25:
                    from.setText(getResources().getString(R.string.Rs) + " " + 15 + " cr");
                    frmRate = "150000000";
                    break;


            }


            switch (rightThumbIndex) {
                case 0:
                    to.setText(getResources().getString(R.string.Rs) + " " + 0);
                    toRate = "0";
                    break;
                case 1:
                    to.setText(getResources().getString(R.string.Rs) + " " + 5 + " Lac");
                    toRate = "500000";
                    break;
                case 2:
                    to.setText(getResources().getString(R.string.Rs) + " " + 10 + " Lac");
                    toRate = "1000000";
                    break;
                case 3:
                    to.setText(getResources().getString(R.string.Rs) + " " + 20 + " Lac");
                    toRate = "2000000";
                    break;
                case 4:
                    to.setText(getResources().getString(R.string.Rs) + " " + 30 + " Lac");
                    toRate = "3000000";
                    break;
                case 5:
                    to.setText(getResources().getString(R.string.Rs) + " " + 40 + " Lac");
                    toRate = "4000000";
                    break;
                case 6:
                    to.setText(getResources().getString(R.string.Rs) + " " + 50 + " Lac");
                    toRate = "5000000";
                    break;
                case 7:
                    to.setText(getResources().getString(R.string.Rs) + " " + 60 + " Lac");
                    toRate = "6000000";
                    break;
                case 8:
                    to.setText(getResources().getString(R.string.Rs) + " " + 70 + " Lac");
                    toRate = "7000000";
                    break;
                case 9:
                    to.setText(getResources().getString(R.string.Rs) + " " + 80 + " Lac");
                    toRate = "8000000";
                    break;
                case 10:
                    to.setText(getResources().getString(R.string.Rs) + " " + 90 + " Lac");
                    toRate = "9000000";
                    break;
                case 11:
                    to.setText(getResources().getString(R.string.Rs) + " " + 1 + " cr");
                    toRate = "10000000";
                    break;
                case 12:
                    to.setText(getResources().getString(R.string.Rs) + " " + 1.2 + " cr");
                    toRate = "12000000";
                    break;
                case 13:
                    to.setText(getResources().getString(R.string.Rs) + " " + 1.4 + " cr");
                    toRate = "14000000";
                    break;
                case 14:
                    to.setText(getResources().getString(R.string.Rs) + " " + 1.6 + " cr");
                    toRate = "16000000";
                    break;
                case 15:
                    to.setText(getResources().getString(R.string.Rs) + " " + 1.8 + " cr");
                    toRate = "18000000";
                    break;
                case 16:
                    to.setText(getResources().getString(R.string.Rs) + " " + 2 + " cr");
                    toRate = "20000000";
                    break;
                case 17:
                    to.setText(getResources().getString(R.string.Rs) + " " + 2.3 + " cr");
                    toRate = "23000000";
                    break;
                case 18:
                    to.setText(getResources().getString(R.string.Rs) + " " + 2.6 + " cr");
                    toRate = "26000000";
                    break;
                case 19:
                    to.setText(getResources().getString(R.string.Rs) + " " + 3 + " cr");
                    toRate = "30000000";
                    break;
                case 20:
                    to.setText(getResources().getString(R.string.Rs) + " " + 3.5 + " cr");
                    toRate = "35000000";
                    break;
                case 21:
                    to.setText(getResources().getString(R.string.Rs) + " " + 4 + " cr");
                    toRate = "40000000";
                    break;
                case 22:
                    to.setText(getResources().getString(R.string.Rs) + " " + 4.5 + " cr");
                    toRate = "45000000";
                    break;
                case 23:
                    to.setText(getResources().getString(R.string.Rs) + " " + 5 + " cr");
                    toRate = "50000000";
                    break;
                case 24:
                    to.setText(getResources().getString(R.string.Rs) + " " + 10 + " cr");
                    toRate = "100000000";
                    break;
                case 25:
                    to.setText(getResources().getString(R.string.Rs) + " " + 15 + "+" + " cr");
                    toRate = "150000000";
                    break;
            }
        } else if (rangeBar.getId() == R.id.areabar) {

            switch (leftThumbIndex) {
                case 0:
                    frmArea.setText("0 sqft");
                    frma = "0";
                    break;
                case 1:
                    frmArea.setText("100 sqft");
                    frma = "100";
                    break;
                case 2:
                    frmArea.setText("200 sqft");
                    frma = "200";
                    break;
                case 3:
                    frmArea.setText("300 sqft");
                    frma = "300";
                    break;
                case 4:
                    frmArea.setText("400 sqft");
                    frma = "400";
                    break;
                case 5:
                    frmArea.setText("1000 sqft");
                    frma = "1000";
                    break;
                case 6:
                    frmArea.setText("1500 sqft");
                    frma = "1500";
                    break;
                case 7:
                    frmArea.setText("2000 sqft");
                    frma = "2000";
                    break;
                case 8:
                    frmArea.setText("2500 sqft");
                    frma = "2500";
                    break;
                case 9:
                    frmArea.setText("4000 sqft");
                    frma = "4000";
                    break;
                case 10:
                    frmArea.setText("5000 sqft");
                    frma = "5000";
                    break;
                case 11:
                    frmArea.setText("10000 sqft");
                    frma = "10000";
                    break;
                case 12:
                    frmArea.setText("25000 sqft");
                    frma = "25000";
                    break;
                case 13:
                    frmArea.setText("50000 sqft");
                    frma = "50000";
                    break;

            }


            switch (rightThumbIndex) {
                case 0:
                    toArea.setText("0 sqft");
                    toa = "0";
                    break;
                case 1:
                    toArea.setText("100 sqft");
                    toa = "100";
                    break;
                case 2:
                    toArea.setText("200 sqft");
                    toa = "200";
                    break;
                case 3:
                    toArea.setText("300 sqft");
                    toa = "300";
                    break;
                case 4:
                    toArea.setText("400 sqft");
                    toa = "400";
                    break;
                case 5:
                    toArea.setText("1000 sqft");
                    toa = "1000";
                    break;
                case 6:
                    toArea.setText("1500 sqft");
                    toa = "1500";
                    break;
                case 7:
                    toArea.setText("2000 sqft");
                    toa = "2000";
                    break;
                case 8:
                    toArea.setText("2500 sqft");
                    toa = "2500";
                    break;
                case 9:
                    toArea.setText("4000 sqft");
                    toa = "4000";
                    break;
                case 10:
                    toArea.setText("5000 sqft");
                    toa = "5000";
                    break;
                case 11:
                    toArea.setText("10000 sqft");
                    toa = "10000";
                    break;
                case 12:
                    toArea.setText("25000 sqft");
                    toa = "25000";
                    break;
                case 13:
                    toArea.setText("50000+ sqft");
                    toa = "50000";
                    break;
            }
        }

    }
}
