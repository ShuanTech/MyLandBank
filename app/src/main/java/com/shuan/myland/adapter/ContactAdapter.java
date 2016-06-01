package com.shuan.myland.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuan.myland.R;
import com.shuan.myland.list.ContactList;

import java.util.ArrayList;

/**
 * Created by team-leader on 4/16/2016.
 */
public class ContactAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ContactList> list;
    private LayoutInflater inflater;


    public ContactAdapter(Context mContext, ArrayList<ContactList> list) {
        this.mContext = mContext;
        this.list = list;
        inflater=LayoutInflater.from(mContext);
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

        ContactList currItem=list.get(position);
        convertView=inflater.inflate(R.layout.contact_us_item,null);

        ImageView img= (ImageView) convertView.findViewById(R.id.img);
        TextView head= (TextView) convertView.findViewById(R.id.head);
        TextView addr= (TextView) convertView.findViewById(R.id.addr);
        TextView ph= (TextView) convertView.findViewById(R.id.ph);
        TextView land= (TextView) convertView.findViewById(R.id.land);
        TextView fax= (TextView) convertView.findViewById(R.id.fax);

        LinearLayout col2= (LinearLayout) convertView.findViewById(R.id.col2);
        LinearLayout col3= (LinearLayout) convertView.findViewById(R.id.col3);
        LinearLayout col4= (LinearLayout) convertView.findViewById(R.id.col4);


        img.setImageResource(currItem.getImg());
        head.setText(currItem.getHead());
        addr.setText(currItem.getAddr());
        if(currItem.getPh().equalsIgnoreCase("")){col2.setVisibility(View.GONE);}else {ph.setText(currItem.getPh());}
        if(currItem.getLand().equalsIgnoreCase("")){col3.setVisibility(View.GONE);}else {land.setText(currItem.getLand());}
        if(currItem.getFax().equalsIgnoreCase("")){col4.setVisibility(View.GONE);}else {fax.setText(currItem.getPh());}

        return convertView;
    }
}
