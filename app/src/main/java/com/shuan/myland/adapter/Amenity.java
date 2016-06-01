package com.shuan.myland.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuan.myland.R;
import com.shuan.myland.list.Amities;

import java.util.ArrayList;


public class Amenity extends BaseAdapter {

    private Context mContext;
    private ArrayList<Amities> list;
    private LayoutInflater inflater;

    public Amenity(Context mContext, ArrayList<Amities> list) {
        this.mContext = mContext;
        this.list = list;
        inflater = LayoutInflater.from(mContext);
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

        Amities curr = list.get(position);

        convertView = inflater.inflate(R.layout.amenties, null);

        ImageView img1 = (ImageView) convertView.findViewById(R.id.img1);
        TextView txt1 = (TextView) convertView.findViewById(R.id.txt1);

        txt1.setText(curr.getAmity());
        img1.setImageResource(mContext.getResources().getIdentifier(curr.getAmity().toString().replace(" ", "_").toLowerCase(), "drawable", "com.shuan.myland"));

        return convertView;
    }
}
