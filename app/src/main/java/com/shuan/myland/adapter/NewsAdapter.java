package com.shuan.myland.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shuan.myland.R;
import com.shuan.myland.list.NewsList;
import com.shuan.myland.utils.Common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<NewsList> list;
    private LayoutInflater inflater;
    private Common mApp;
    private DisplayImageOptions options;

    public NewsAdapter(Context mContext, ArrayList<NewsList> list) {
        this.mContext = mContext;
        this.list = list;
        this.inflater=LayoutInflater.from(mContext);
        mApp = (Common) mContext.getApplicationContext();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.noimage)
                .showImageForEmptyUri(R.drawable.noimage)
                .showImageOnFail(R.drawable.noimage)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        NewsList currItem=list.get(position);
        convertView=inflater.inflate(R.layout.news_item,null);
        TextView newsId= (TextView) convertView.findViewById(R.id.news_id);
        TextView newsHead= (TextView) convertView.findViewById(R.id.news_head);
        TextView newsDate= (TextView) convertView.findViewById(R.id.news_date);
        ImageView img= (ImageView) convertView.findViewById(R.id.news_img);
        String outDate = null;
        SimpleDateFormat getDate=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat ansDate=new SimpleDateFormat("MMM dd, yy");
        try {
            Date date=getDate.parse(currItem.getDate());
            outDate=ansDate.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        newsId.setText(currItem.getId());
        newsHead.setText(currItem.getHead());
        newsDate.setText(outDate);
        ImageLoader.getInstance().displayImage(currItem.getImg(), img, options, new SimpleImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                super.onLoadingStarted(imageUri, view);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                super.onLoadingCancelled(imageUri, view);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String s, View view, int i, int i1) {

            }
        });
        return convertView;
    }
}
