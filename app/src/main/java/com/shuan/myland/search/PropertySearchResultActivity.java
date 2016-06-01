package com.shuan.myland.search;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuan.myland.R;
import com.shuan.myland.adapter.PropertyItemAdapter;
import com.shuan.myland.list.PropertyItem;
import com.shuan.myland.parser.HttpUrlConnectionParser;
import com.shuan.myland.parser.php;
import com.shuan.myland.property.PropertyView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PropertySearchResultActivity extends AppCompatActivity {

    private ListView propertyListView;
    private ArrayList<PropertyItem> propertyItems;
    private PropertyItemAdapter adapter;
    private Toolbar toolbar;
    private HttpUrlConnectionParser parser = new HttpUrlConnectionParser();
    private php call = new php();
    private ProgressBar progressBar;
    private HashMap<String, String> searchData;
    private String level,loc, type, br, from, to,frma,toa,area;
    private RelativeLayout noResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        loc = getIntent().getStringExtra("loc");
        type = getIntent().getStringExtra("type");
        br = getIntent().getStringExtra("br");
        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");
        area=getIntent().getStringExtra("area");
        frma=getIntent().getStringExtra("frma");
        toa=getIntent().getStringExtra("toa");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_search_result);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        propertyListView = (ListView) findViewById(R.id.search_list);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        noResult= (RelativeLayout) findViewById(R.id.no_result);

        propertyItems = new ArrayList<PropertyItem>();


        new LoadSearchData().execute();

        progressBar.setVisibility(View.VISIBLE);


    }

    public class LoadSearchData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            searchData = new HashMap<String, String>();
            try {
                JSONObject json = null;
                if(br.equalsIgnoreCase("area")){
                    searchData.put("loc", loc);
                    searchData.put("frma",frma);
                    searchData.put("toa",toa);
                    searchData.put("from", from);
                    searchData.put("to", to);
                    searchData.put("type", type);
                    json=parser.makeHttpUrlConnection(php.pro_search_area,searchData);
                }else if (type.equalsIgnoreCase("all") && br.equalsIgnoreCase("all")) {
                    searchData.put("loc", loc);
                    searchData.put("from", from);
                    searchData.put("to", to);
                    json = parser.makeHttpUrlConnection(php.pro_search_all, searchData);
                } else if (!type.equalsIgnoreCase("all") && br.equalsIgnoreCase("all")) {
                    searchData.put("loc", loc);
                    searchData.put("type", type);
                    searchData.put("from", from);
                    searchData.put("to", to);
                    json = parser.makeHttpUrlConnection(php.pro_search_type, searchData);
                } else if(type.equalsIgnoreCase("all") && !br.equalsIgnoreCase("all")){
                    searchData.put("loc", loc);
                    searchData.put("br", br);
                    searchData.put("from", from);
                    searchData.put("to", to);
                    json=parser.makeHttpUrlConnection(php.pro_search_detail,searchData);
                }  else{
                    searchData.put("loc", loc);
                    searchData.put("type", type);
                    searchData.put("br", br);
                    searchData.put("from", from);
                    searchData.put("to", to);
                    json = parser.makeHttpUrlConnection(php.pro_search, searchData);
                }


                int succ = json.getInt("success");
                if (succ == 0) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toolbar.setTitle("Property Search 0 Result");
                                noResult.setVisibility(View.VISIBLE);
                            }
                        });
                } else {
                    final JSONArray jsonArray = json.getJSONArray("property");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject child = jsonArray.getJSONObject(i);

                        String id = child.optString("id");
                        String pro_id=child.optString("pro_id");
                        String img = child.optString("man_img");
                        String price = child.optString("price");
                        String pdate = child.optString("posted_date");
                        String pdetail = child.optString("pro_deatils");
                        String ptype = child.optString("pro_type");
                        String ploc = child.optString("location");
                        String parea = child.optString("area");
                        String pareaType = child.optString("a_type");

                        propertyItems.add(new PropertyItem(id,pro_id, img, price,pdate, pdetail, ptype, ploc, parea, pareaType));
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toolbar.setTitle("Property Search "+ jsonArray.length()+" Results");
                            adapter = new PropertyItemAdapter(getApplicationContext(), propertyItems);
                            propertyListView.setAdapter(adapter);

                            propertyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    TextView txt = (TextView) view.findViewById(R.id.id);
                                    Intent in = new Intent(getApplicationContext(), PropertyView.class);
                                    in.putExtra("id", txt.getText().toString());
                                    startActivity(in);
                                }
                            });
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
}
