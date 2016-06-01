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
import com.shuan.myland.adapter.ProjectItemAdapter;
import com.shuan.myland.list.PropertyItem;
import com.shuan.myland.parser.HttpUrlConnectionParser;
import com.shuan.myland.parser.php;
import com.shuan.myland.property.ProjectView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ProjectSearchResultActivity extends AppCompatActivity {

    private ListView propertyListView;
    private ArrayList<PropertyItem> propertyItems;
    private ProjectItemAdapter adapter;
    private Toolbar toolbar;
    private HttpUrlConnectionParser parser = new HttpUrlConnectionParser();
    private php call = new php();
    private ProgressBar progressBar;
    private HashMap<String, String> searchData;
    private String level,loc, type,from, to;
    private RelativeLayout noResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

       /* level=getIntent().getStringExtra("level");
        loc=getIntent().getStringExtra("loc");
        type=getIntent().getStringExtra("type");
        from=getIntent().getStringExtra("from");
        to=getIntent().getStringExtra("to");*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_search_result);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Project Search");
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
            searchData.put("project","project");
            try {

                JSONObject json = parser.makeHttpUrlConnection(php.project_search,searchData);
                int succ = json.getInt("success");
                if (succ == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toolbar.setTitle("Project Search 0 Result");
                            noResult.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    final JSONArray jsonArray = json.getJSONArray("project");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject child = jsonArray.getJSONObject(i);

                        String id = child.optString("id");
                        String pro_id=child.optString("id_pro");
                        String pro_name=child.optString("pro_name");
                        String price_bet=child.optString("price_bet");
                        String img = child.optString("man_img");
                        String pro_detail=child.optString("pro_deatils");


                        propertyItems.add(new PropertyItem(id,pro_id,pro_name,price_bet,img,pro_detail));
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toolbar.setTitle("Project Search "+jsonArray.length()+" Results");
                            adapter = new ProjectItemAdapter(getApplicationContext(), propertyItems);
                            propertyListView.setAdapter(adapter);

                            propertyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    TextView txt = (TextView) view.findViewById(R.id.id);
                                    TextView txt1= (TextView) view.findViewById(R.id.pro_loc);
                                    Intent in = new Intent(getApplicationContext(), ProjectView.class);
                                    in.putExtra("id", txt.getText().toString());
                                    in.putExtra("pro_type",txt1.getText().toString());
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
