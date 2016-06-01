package com.shuan.myland.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.shuan.myland.R;
import com.shuan.myland.adapter.PropertyAdapter;
import com.shuan.myland.list.PropertyList;
import com.shuan.myland.parser.HttpUrlConnectionParser;
import com.shuan.myland.parser.php;
import com.shuan.myland.property.PropertyView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnitFragment extends Fragment {

    private PropertyAdapter adapter;
    private String id;
    private RelativeLayout lay;
    private ListView list;
    private HashMap<String, String> pData;
    private ArrayList<PropertyList> proList;
    private ProgressBar progressBar;


    public UnitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        id = getArguments().getString("id");
        View view = inflater.inflate(R.layout.fragment_unit, container, false);

        list = (ListView) view.findViewById(R.id.search_list);

        lay = (RelativeLayout) view.findViewById(R.id.lay);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        proList = new ArrayList<PropertyList>();
        new getPropertyDetails().execute();

        return view;
    }

    public class getPropertyDetails extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            pData = new HashMap<String, String>();
            pData.put("id", id);
            try {
                final JSONObject json = HttpUrlConnectionParser.makeHttpUrlConnection(php.project_property, pData);
                int succ = json.getInt("success");
                if (succ == 0) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lay.setVisibility(View.VISIBLE);
                            list.setVisibility(View.GONE);
                        }
                    });
                } else {
                    JSONArray jsonArray = json.getJSONArray("project");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject child = jsonArray.getJSONObject(i);
                        //id, pro_type, plot_id, pro_deatils, price, area, a_type, man_img
                        String id = child.optString("id");
                        String pro_type = child.optString("pro_type");
                        String plot_id = child.optString("plot_id");
                        String pro_deatils = child.optString("pro_deatils");
                        String price = child.optString("price");
                        String area = child.optString("area");
                        String a_type = child.optString("a_type");
                        String man_img = child.optString("man_img");

                        proList.add(new PropertyList(id, pro_type, plot_id, pro_deatils, price, area, a_type, man_img));
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            list.setVisibility(View.VISIBLE);
                            lay.setVisibility(View.GONE);
                            adapter = new PropertyAdapter(getActivity(), proList);
                            list.setAdapter(adapter);
                            adapter.setClickListener(new PropertyAdapter.ClickListener() {
                                @Override
                                public void itemClicked(View v, String id) {
                                    Intent in = new Intent(getActivity(), PropertyView.class);
                                    in.putExtra("id", id);
                                    startActivity(in);
                                }
                            });
                        }
                    });
                }

            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
        }
    }

}
