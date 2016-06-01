package com.shuan.myland.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shuan.myland.R;
import com.shuan.myland.adapter.NewsAdapter;
import com.shuan.myland.list.NewsList;
import com.shuan.myland.parser.HttpUrlConnectionParser;
import com.shuan.myland.parser.php;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NewsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<NewsList> list;
    private NewsAdapter adapter;
    private ProgressBar progressBar;
    private ListView newsList;
    private HashMap<String,String> newsData;
    private php call=new php();
    private HttpUrlConnectionParser parser=new HttpUrlConnectionParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        toolbar= (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Property News");
        list=new ArrayList<NewsList>();
        progressBar= (ProgressBar) findViewById(R.id.progress_bar);
        newsList= (ListView) findViewById(R.id.news_list);

        new GetNews().execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    public class GetNews extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            newsData=new HashMap<String,String>();
            newsData.put("id","id");
            try{
                JSONObject json=parser.makeHttpUrlConnection(call.pro_news,newsData);
                int succ=json.getInt("success");

                if(succ==0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"News Not Available",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    JSONArray jsonArray=json.getJSONArray("pro_news");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject child=jsonArray.getJSONObject(i);
                        String id=child.optString("id");
                        String head=child.optString("heading");
                        String img=child.optString("img");
                        String date=child.optString("date");

                        list.add(new NewsList(id,head,img,date));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter=new NewsAdapter(getApplicationContext(),list);
                            newsList.setAdapter(adapter);
                            newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    TextView txt= (TextView) view.findViewById(R.id.news_id);
                                    Intent in=new Intent(getApplicationContext(),NewsReadActivity.class);
                                    in.putExtra("news_id",txt.getText().toString());
                                    startActivity(in);
                                    //Toast.makeText(getApplicationContext(),txt.getText().toString(),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }catch (Exception e){}


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
