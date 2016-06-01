package com.shuan.myland.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.shuan.myland.R;
import com.shuan.myland.list.LocationList;

import java.util.ArrayList;

/**
 * Created by team-leader on 3/16/2016.
 */
public class LocationAdapter extends ArrayAdapter<LocationList> implements Filterable {

    private Context context;
    private int resource,textViewResourceId;
    private ArrayList<LocationList> busCodeList,temp,sugg;
    private LayoutInflater inflater;
    private ArrayFilter mFilter;

    public LocationAdapter(Context context, int resource, int textViewResourceId, ArrayList<LocationList> busCodeList) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.busCodeList = busCodeList;
        this.temp=new ArrayList<LocationList>(busCodeList);
        this.sugg=new ArrayList<LocationList>();
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return busCodeList.size();
    }

    @Override
    public LocationList getItem(int position) {
        return busCodeList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=inflater.inflate(resource,null);
        LocationList curr=busCodeList.get(position);

        TextView id= (TextView) convertView.findViewById(R.id.location_name);



        id.setText(curr.getLocName());
        return  convertView;
    }

    @Override
    public Filter getFilter() {
        //return nameFilter;
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {
        private  LocationList lock;

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (temp == null) {
                synchronized (lock) {
                    temp = new ArrayList<LocationList>(busCodeList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    ArrayList<LocationList> list = new ArrayList<LocationList>(
                            temp);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                final String prefixString = prefix.toString().toLowerCase();

                ArrayList<LocationList> values = temp;
                int count = values.size();

                ArrayList<LocationList> newValues = new ArrayList<LocationList>(count);

                for (int i = 0; i < count; i++) {
                    LocationList item = values.get(i);
                    if (item.getLocName().toLowerCase().contains(prefix.toString().toLowerCase())) {
                        newValues.add(item);
                    }

                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
            if (results.values != null) {
                busCodeList = (ArrayList<LocationList>) results.values;
            } else {
                busCodeList = new ArrayList<LocationList>();
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
