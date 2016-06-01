package com.shuan.myland.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuan.myland.R;
import com.shuan.myland.list.FacilityList;

import java.util.ArrayList;


public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.FacilityViewHolder> {


    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<FacilityList> list;

    public FacilityAdapter(Context mContext, ArrayList<FacilityList> list) {
        this.mContext = mContext;
        this.list=list;
        inflater=LayoutInflater.from(mContext);
    }

    @Override
    public FacilityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.facility_item,parent,false);
        FacilityViewHolder holder=new FacilityViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(FacilityViewHolder holder, int position) {

        FacilityList currItm=list.get(position);

        holder.name.setText(currItm.getName());
        holder.distance.setText(currItm.getDistance()+" "+"km");

    }

    class FacilityViewHolder extends RecyclerView.ViewHolder{
        TextView name,distance;
        public FacilityViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.name);
            distance= (TextView) itemView.findViewById(R.id.distance);
        }
    }
}
