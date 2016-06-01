package com.shuan.myland.search;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ProjectSearchActivity extends AppCompatActivity implements View.OnClickListener, RangeBar.OnRangeBarChangeListener {

    private Toolbar toolbar;
    private AutoCompleteTextView location;
    private ArrayList<LocationList> locationList;
    private LocationAdapter adapter;
    private TextView flat, house, plot, farms,readyToMove,underConstruction;
    private boolean fclick = true, hclick = true, pclick = true, faclick = true,rm=true,uc=true;
    private Button search;
    private TextView from, to;
    private RangeBar rangeBar;
    private String frmRate, toRate;
    public String sFlat = "", sHouse = "", sPlot = "", sFarms = "",srdy="",sundr="";
    public HttpUrlConnectionParser parser = new HttpUrlConnectionParser();
    public php call = new php();
    public HashMap<String, String> locData;
    public Common mApp;
    public Context mContext;
    private RelativeLayout row3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        mApp = (Common) mContext.getApplicationContext();

        setContentView(R.layout.activity_project_search);
        toolbar= (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Search Projects");

        //toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        location = (AutoCompleteTextView) findViewById(R.id.location);
        flat = (TextView) findViewById(R.id.flat);
        house = (TextView) findViewById(R.id.house);
        plot = (TextView) findViewById(R.id.plot);
        farms = (TextView) findViewById(R.id.farms);
        rangeBar = (RangeBar) findViewById(R.id.budgetBar);
        from = (TextView) findViewById(R.id.from);
        to = (TextView) findViewById(R.id.to);
        readyToMove= (TextView) findViewById(R.id.rdy);
        underConstruction= (TextView) findViewById(R.id.undr);

        frmRate = "0";
        toRate = "150000000";
        from.setText(getResources().getString(R.string.Rs) + " " + "0");
        to.setText(getResources().getString(R.string.Rs) + " " + "15+ cr");

        row3 = (RelativeLayout) findViewById(R.id.row3);

        search = (Button) findViewById(R.id.search);

        locationList = new ArrayList<LocationList>();

        flat.setOnClickListener(this);
        house.setOnClickListener(this);
        plot.setOnClickListener(this);
        farms.setOnClickListener(this);
        readyToMove.setOnClickListener(this);
        underConstruction.setOnClickListener(this);

        search.setOnClickListener(this);

        rangeBar.setOnRangeBarChangeListener(this);


        new GetLoc().execute();

    }

    public class GetLoc extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            locData = new HashMap<String, String>();
            locData.put("id", "id");

            try {

                JSONObject json = parser.makeHttpUrlConnection(php.pro_name, locData);
                int succ = json.getInt("success");

                if (succ == 0) {
                } else {
                    JSONArray jsonArray = json.getJSONArray("pro_name");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject child = jsonArray.getJSONObject(i);
                        String loc = child.optString("pro_name");
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

                } else {
                    plot.setBackgroundResource(R.drawable.text);
                    sPlot = "";
                    pclick = true;

                }
                break;
            case R.id.farms:
                if (faclick) {
                    farms.setBackgroundResource(R.drawable.selected_block);
                    sFarms = "farms";
                    faclick = false;

                } else {
                    farms.setBackgroundResource(R.drawable.text);
                    sFarms = "";
                    faclick = true;

                }
                break;
            case R.id.rdy:
                if(rm){
                    readyToMove.setBackgroundResource(R.drawable.selected_block);
                    srdy="ready to move";
                    rm=false;
                }else {
                    readyToMove.setBackgroundResource(R.drawable.text);
                    srdy="";
                    rm=true;
                }
                break;
            case R.id.undr:
                if(uc){
                    underConstruction.setBackgroundResource(R.drawable.selected_block);
                    sundr="under construction";
                    uc=false;
                }else {
                    underConstruction.setBackgroundResource(R.drawable.text);
                    sundr="";
                    uc=true;
                }
                break;

            case R.id.search:
                String type,level;

                if (location.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Select the Location to Search", Toast.LENGTH_SHORT).show();
                } else {

                    if (fclick && hclick && pclick && faclick) {
                        type = "All";
                    } else {
                        type = sFlat + "," + sHouse + "," + sPlot + "," + sFarms;
                    }

                    if(rm && uc){
                        level="All";
                    }else{
                        level=srdy+","+sundr;
                    }

                    Intent in = new Intent(getApplicationContext(), ProjectSearchResultActivity.class);
                    mApp.getSharedPreferences().edit().putString(Common.PRO_LOC, location.getText().toString()).commit();
                    in.putExtra("loc", location.getText().toString());
                    in.putExtra("type", type);
                    in.putExtra("level",level);
                    in.putExtra("from", frmRate);
                    in.putExtra("to", toRate);

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
        }

    }
}
