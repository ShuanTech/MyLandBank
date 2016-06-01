package com.shuan.myland.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shuan.myland.R;
import com.shuan.myland.parser.HttpUrlConnectionParser;
import com.shuan.myland.parser.php;
import com.shuan.myland.scrollView.ObservableScrollView;
import com.shuan.myland.scrollView.ObservableScrollViewCallbacks;
import com.shuan.myland.scrollView.ScrollState;
import com.shuan.myland.scrollView.ScrollUtils;
import com.shuan.myland.utils.Common;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class NewsReadActivity extends AppCompatActivity implements ObservableScrollViewCallbacks{


    private php call = new php();
    TextView heading,content,date;
    String getHeading,getContent,getImg,getday;
    public ProgressBar progressBar;
    private Toolbar toolbar;
    private String head;
    private DisplayImageOptions options;
    private View mImageView;
    private View mToolbarView;
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;
    private HttpUrlConnectionParser parser = new HttpUrlConnectionParser();
    ImageView img;
    private String news_id;
    private Common mApp;
    private Context mContext;
    private HashMap<String,String> newsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = getApplicationContext();
        mApp = (Common) getApplicationContext();
        news_id=getIntent().getStringExtra("news_id");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_read);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        heading= (TextView) findViewById(R.id.news_head);
        content= (TextView) findViewById(R.id.news_desc);
        date= (TextView) findViewById(R.id.news_date);
        img= (ImageView) findViewById(R.id.news_img);
        mImageView = findViewById(R.id.news_img);
        mToolbarView = findViewById(R.id.app_bar);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.colorPrimary)));
        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        setSupportActionBar(toolbar);
        toolbar.setPadding(0, mApp.getStatusBarHeight(mContext), 0, 0);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        new LoadNewsDetails().execute();
        progressBar.setVisibility(View.VISIBLE);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.noimage)
                .showImageForEmptyUri(R.drawable.noimage)
                .showImageOnFail(R.drawable.noimage)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.colorPrimary);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        ViewHelper.setTranslationY(mImageView, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    public class LoadNewsDetails extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            newsData=new HashMap<String,String>();
            newsData.put("id",news_id);
            try{
                JSONObject json=parser.makeHttpUrlConnection(call.pro_news_read,newsData);
                int succ=json.getInt("success");
                if(succ==0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"No Data Available",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    JSONArray jsonArray=json.getJSONArray("pro_news");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject child=jsonArray.getJSONObject(i);
                        getHeading=child.optString("heading");
                        getContent=child.optString("desc");
                        getImg=child.optString("img");
                        getday=child.optString("date");

                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String outDate = null;
                            SimpleDateFormat getDate=new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat ansDate=new SimpleDateFormat("MMM dd, yy");
                            try {
                                Date date=getDate.parse(getday);
                                outDate=ansDate.format(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            date.setText(outDate);
                            heading.setText(getHeading);
                            content.setText(getContent);
                            ImageLoader.getInstance().displayImage(getImg,img,options,new SimpleImageLoadingListener(){
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {

                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                    String message = null;
                                    switch (failReason.getType()) {
                                        case IO_ERROR:
                                            message = "Input/Output error";
                                            break;
                                        case DECODING_ERROR:
                                            message = "Image can't be decoded";
                                            break;
                                        case NETWORK_DENIED:
                                            message = "Downloads are denied";
                                            break;
                                        case OUT_OF_MEMORY:
                                            message = "Out Of Memory error";
                                            break;
                                        case UNKNOWN:
                                            message = "Unknown error";
                                            break;
                                    }
                                    // Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

                                    //spinner.setVisibility(View.GONE);
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    //spinner.setVisibility(View.GONE);
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
