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
import com.shuan.myland.list.PropertyItem;
import com.shuan.myland.utils.Common;

import java.util.ArrayList;

/**
 * Created by team-leader on 5/5/2016.
 */
public class ProjectItemAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<PropertyItem> propertyItems;
    private LayoutInflater inflater;
    private Common mApp;
    private DisplayImageOptions options;

    public ProjectItemAdapter(Context mContext, ArrayList<PropertyItem> propertyItems) {
        this.mContext = mContext;
        this.propertyItems = propertyItems;
        inflater=LayoutInflater.from(mContext);
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
        return propertyItems.size();
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
        convertView=inflater.inflate(R.layout.search_project_item,null);

        PropertyItem currItem=propertyItems.get(position);

        ImageView proImg= (ImageView) convertView.findViewById(R.id.pro_img);
        TextView id= (TextView) convertView.findViewById(R.id.id);
        TextView proId= (TextView) convertView.findViewById(R.id.pro_id);
        TextView proPrice= (TextView) convertView.findViewById(R.id.pro_price);
        TextView proName= (TextView) convertView.findViewById(R.id.pro_det);
        TextView proDetail= (TextView) convertView.findViewById(R.id.pro_loc);

        id.setText(currItem.getId());
        proId.setText(currItem.getPro_id());

        int len = currItem.getPro_price().length();
        String price = "";
        int get = 0;

        if (len <= 5) {
            price = currItem.getPro_price() + " " + "K";

        } else if (len >= 6 && len < 8) {
            get = Integer.parseInt(currItem.getPro_price());
            price = Integer.toString((get / 100000)) + " " + "Lac";
        } else {
            get = Integer.parseInt(currItem.getPro_price());
            price = Integer.toString((get / 10000000)) + " " + "Cr";
        }


        proPrice.setText(mContext.getResources().getString(R.string.Rs)+" "+price + "  - " +" Onwards");
        proName.setText(currItem.getPro_name());



        proDetail.setText(currItem.getPro_loc());



        ImageLoader.getInstance().displayImage(currItem.getPro_img(), proImg, options, new SimpleImageLoadingListener() {

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

