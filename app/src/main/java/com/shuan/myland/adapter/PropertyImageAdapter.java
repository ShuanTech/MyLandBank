package com.shuan.myland.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shuan.myland.R;
import com.shuan.myland.list.PropertyItem;
import com.shuan.myland.utils.Common;

import java.util.ArrayList;


public class PropertyImageAdapter extends PagerAdapter {


    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<PropertyItem> propertyItems;
    private Common mApp;
    private DisplayImageOptions options;
    private ClickListener clickListener;


    public PropertyImageAdapter(Context mContext, ArrayList<PropertyItem> propertyItems) {
        this.mContext = mContext;
        this.propertyItems = propertyItems;
        mLayoutInflater = LayoutInflater.from(mContext);
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

        /*      .showImageOnLoading(R.drawable.load1)
                .showImageForEmptyUri(R.drawable.no_image)
                .showImageOnFail(R.drawable.no_image)
               ;*/
    }

    @Override
    public int getCount() {
        return propertyItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        final PropertyItem currItm = propertyItems.get(position);

        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

        ImageLoader.getInstance().displayImage(currItm.getPro_sub_img(), imageView, options, new SimpleImageLoadingListener() {

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


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListener!=null){
                    clickListener.itemClicked(v,position);
                }
            }
        });
        /*itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(new Intent(mContext,GalleryActivity.class));
                in.putExtra("pos",position);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(in);

            }
        });
*/
        container.addView(itemView);

        return itemView;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }




    public interface ClickListener{
        public void itemClicked(View v,int pos);
    }
}
