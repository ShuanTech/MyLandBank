package com.shuan.myland.fragment;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuan.myland.R;
import com.shuan.myland.adapter.FacilityAdapter;
import com.shuan.myland.list.FacilityList;
import com.shuan.myland.parser.HttpUrlConnectionParser;
import com.shuan.myland.parser.php;
import com.shuan.myland.utils.Common;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private FacilityAdapter adapter;
    private ArrayList<FacilityList> list;
    public Context mContext;
    public Common mApp;
    public ProgressBar progressBar;
    public HttpUrlConnectionParser parser=new HttpUrlConnectionParser();
    public php call=new php();
    public HashMap<String,String> shopData;
    private RelativeLayout noResult;
    private String loc;


    public ShoppingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
        mApp= (Common) mContext.getApplicationContext();
        loc=getArguments().getString("loc");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.shopping);
        progressBar= (ProgressBar) view.findViewById(R.id.progress_bar);
        noResult= (RelativeLayout) view.findViewById(R.id.no_result);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        list=new ArrayList<FacilityList>();

        new GetShop().execute();
        progressBar.setVisibility(View.VISIBLE);

    }

    public class GetShop extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            shopData=new HashMap<String,String>();
            shopData.put("loc",mApp.getSharedPreferences().getString(Common.PRO_LOC,""));

            try{
                JSONObject json=parser.makeHttpUrlConnection(php.pro_facility_shop,shopData);
                int succ=json.getInt("success");
                if(succ==0){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            noResult.setVisibility(View.VISIBLE);
                            TextView txt= (TextView) noResult.findViewById(R.id.extras);
                            txt.setText("Couldn't find a Shopping Mall");
                        }
                    });
                }else {
                    JSONArray jsonArray=json.getJSONArray("shop");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject child=jsonArray.getJSONObject(i);
                        String name=child.optString("Name");
                        String dist=child.optString("Distance");
                        list.add(new FacilityList(name,dist));
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter = new FacilityAdapter(getActivity(),list);
                            mRecyclerView.setAdapter(mAdapter);

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
