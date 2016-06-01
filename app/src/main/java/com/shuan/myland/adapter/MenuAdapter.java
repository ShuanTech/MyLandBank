package com.shuan.myland.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuan.myland.R;
import com.shuan.myland.list.MenuList;

import java.util.ArrayList;

/**
 * Created by team-leader on 3/17/2016.
 */
public class MenuAdapter extends BaseAdapter{

    public Context mContext;
    public ArrayList<MenuList> menulist;
    public LayoutInflater inflater;

    public MenuAdapter(Context mContext, ArrayList<MenuList> menulist) {
        this.mContext = mContext;
        this.menulist = menulist;
        inflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return menulist.size();
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
        convertView=inflater.inflate(R.layout.menu_item,null);

        ImageView mnuImg= (ImageView) convertView.findViewById(R.id.mnu_Img);

        TextView mnuName= (TextView) convertView.findViewById(R.id.mnu_name);

        MenuList currMenu=menulist.get(position);

        mnuImg.setImageResource(currMenu.getId());
        mnuName.setText(currMenu.getMnuName());

        return convertView;
    }
}
