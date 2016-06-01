package com.shuan.myland.activities;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.shuan.myland.R;
import com.shuan.myland.adapter.GalleryAdapter;
import com.shuan.myland.list.PropertyItem;
import com.shuan.myland.parser.HttpUrlConnectionParser;
import com.shuan.myland.parser.php;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GalleryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager mPager;
    private HttpUrlConnectionParser parser=new HttpUrlConnectionParser();
    private ArrayList<PropertyItem> list;
    private GalleryAdapter adapter;
    public ArrayList<PropertyItem> proImg;
    private php call=new php();
    public HashMap<String,String> proImgData=new HashMap<String,String>();
    public String id,from;
    public ProgressBar progressBar;
    public int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        id=getIntent().getStringExtra("id");
        pos=getIntent().getIntExtra("pos", 0);
        from=getIntent().getStringExtra("from");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        mPager= (ViewPager) findViewById(R.id.gal_img);
        progressBar= (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        proImg=new ArrayList<PropertyItem>();
        new LoadPropertyImg().execute();

    }


    public class LoadPropertyImg extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            proImgData = new HashMap<String, String>();
            proImgData.put("id", id);
            String img = null;

            try {
                JSONObject json=null;
                if(from.equalsIgnoreCase("property")){
                    json = parser.makeHttpUrlConnection(php.pro_sub_img, proImgData);
                }else if(from.equalsIgnoreCase("project")){
                    json=parser.makeHttpUrlConnection(php.project_img,proImgData);
                }else{
                    json=parser.makeHttpUrlConnection(php.project_plan,proImgData);
                }

                int succ = json.getInt("success");

                if (succ == 0) {

                } else {
                    JSONArray jsonArray = json.getJSONArray("proImg");
                    JSONObject child = jsonArray.getJSONObject(0);
                    String man_img=child.optString("man_img");
                    if(from.equalsIgnoreCase("property")){
                        proImg.add(new PropertyItem(man_img));
                        img = child.optString("sub_img");
                    }else if(from.equalsIgnoreCase("project")){
                        proImg.add(new PropertyItem(man_img));
                        img=child.optString("gallery");
                    }else{
                        img=child.optString("plan");
                    }




                    for (String ans : img.split(",")) {
                        proImg.add(new PropertyItem(ans));
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new GalleryAdapter(getApplicationContext(), proImg);
                            mPager.setAdapter(adapter);
                            mPager.setCurrentItem(pos,true);

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
