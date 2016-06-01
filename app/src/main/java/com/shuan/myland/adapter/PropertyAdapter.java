package com.shuan.myland.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shuan.myland.R;
import com.shuan.myland.list.PropertyList;
import com.shuan.myland.utils.Common;

import java.util.ArrayList;

/**
 * Created by Android on 31-05-2016.
 */
public class PropertyAdapter extends BaseAdapter {

    private ClickListener clickListener;
    private LayoutInflater inflater;
    private Common mApp;
    private Context mContext;
    private DisplayImageOptions options;
    private ArrayList<PropertyList> prolist;

    public PropertyAdapter(Context mContext, ArrayList<PropertyList> prolist) {
        this.mContext = mContext;
        this.prolist = prolist;
        inflater = LayoutInflater.from(mContext);
        mApp = (Common) mContext.getApplicationContext();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.noimage)
                .showImageForEmptyUri(R.drawable.noimage)
                .showImageOnFail(R.drawable.noimage)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getCount() {
        return prolist.size();
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
        final PropertyList curr = prolist.get(position);

        convertView = inflater.inflate(R.layout.property_item, null);

      /*  TextView pId = (TextView) convertView.findViewById(R.id.p_id);*/
        TextView pNo = (TextView) convertView.findViewById(R.id.p_no);
        TextView pDetail = (TextView) convertView.findViewById(R.id.p_detail);
        TextView pPrice = (TextView) convertView.findViewById(R.id.p_price);
        TextView pArea = (TextView) convertView.findViewById(R.id.p_area);
        ImageView pImg = (ImageView) convertView.findViewById(R.id.p_img);
        Button bt = (Button) convertView.findViewById(R.id.see);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.itemClicked(v, curr.getId());
                }
            }
        });

        pNo.setText(curr.getPlot_id());
        pDetail.setText(curr.getPro_deatils() + " " + curr.getPro_type());

        int len = curr.getPrice().length();
        String price = "";
        int get = 0;

        if (len <= 5) {
            price = curr.getPrice() + " " + "K";

        } else if (len >= 6 && len < 8) {
            get = Integer.parseInt(curr.getPrice());
            price = Integer.toString((get / 100000)) + " " + "Lac";
        } else {
            get = Integer.parseInt(curr.getPrice());
            price = Integer.toString((get / 10000000)) + " " + "Cr";
        }
        pPrice.setText(mContext.getResources().getString(R.string.Rs) + " " + price);
        pArea.setText(curr.getArea() + " " + curr.getA_type());

        ImageLoader.getInstance().displayImage(curr.getMan_img(), pImg, options, new SimpleImageLoadingListener() {

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

    public interface ClickListener {
        void itemClicked(View v, String id);
    }
}
