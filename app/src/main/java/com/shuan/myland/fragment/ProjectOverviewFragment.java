package com.shuan.myland.fragment;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuan.myland.R;
import com.shuan.myland.activities.AmentiesActivity;
import com.shuan.myland.activities.FacilitiesActivity;
import com.shuan.myland.activities.GalleryActivity;
import com.shuan.myland.adapter.PropertyImageAdapter;
import com.shuan.myland.list.PropertyItem;
import com.shuan.myland.parser.HttpUrlConnectionParser;
import com.shuan.myland.parser.php;
import com.shuan.myland.property.ProjectView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ProjectOverviewFragment extends Fragment implements View.OnClickListener {
    private String id, pro_type;
    private TextView projectId, projectName, projectVal, projectArea, projectType, projectStatus, proType, proArea, proVal, projectDesc;
    private Button prodetail, more, seeMore;
    private ProgressBar progressBar;
    private HttpUrlConnectionParser parser = new HttpUrlConnectionParser();
    private php call = new php();
    private HashMap<String, String> projectData;
    private String pid, id_pro, pro_name, price_bet, parea, pa_type, pro_status, des, plan, amenities, pro_details, location;
    private RelativeLayout row5;
    private ViewPager viewPager, floorPlan;
    public ArrayList<PropertyItem> proImg;
    private PropertyImageAdapter adapter;
    public ImageButton prev, next;
    private NestedScrollView lay1;
    private TextView txt1, txt2, txt3;
    private ImageView img1, img2, img3, img4, img5, img6, img7, img8;
    private RelativeLayout am;


    public ProjectOverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        id = getArguments().getString("id");
        pro_type = getArguments().getString("pro_type");
        View view = inflater.inflate(R.layout.fragment_project_overview, container, false);

        projectId = (TextView) view.findViewById(R.id.project_id);
        projectName = (TextView) view.findViewById(R.id.project_name);
        projectVal = (TextView) view.findViewById(R.id.project_val);
        projectArea = (TextView) view.findViewById(R.id.project_area);
        projectType = (TextView) view.findViewById(R.id.project_type);
        projectStatus = (TextView) view.findViewById(R.id.project_status);
        proType = (TextView) view.findViewById(R.id.pro_type);
        proArea = (TextView) view.findViewById(R.id.pro_area);
        proVal = (TextView) view.findViewById(R.id.pro_val);
        projectDesc = (TextView) view.findViewById(R.id.project_desc);
        txt1 = (TextView) view.findViewById(R.id.txt1);
        txt2 = (TextView) view.findViewById(R.id.txt2);
        txt3 = (TextView) view.findViewById(R.id.txt3);
        row5 = (RelativeLayout) view.findViewById(R.id.row5);
        prev = (ImageButton) view.findViewById(R.id.prev);
        next = (ImageButton) view.findViewById(R.id.next);
        prodetail = (Button) view.findViewById(R.id.pro_detail);
        am = (RelativeLayout) view.findViewById(R.id.am);
        more = (Button) view.findViewById(R.id.more);
        seeMore = (Button) view.findViewById(R.id.amties);
        floorPlan = (ViewPager) view.findViewById(R.id.floor_plan);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        img1 = (ImageView) view.findViewById(R.id.img1);
        img2 = (ImageView) view.findViewById(R.id.img2);
        img3 = (ImageView) view.findViewById(R.id.img3);
        img4 = (ImageView) view.findViewById(R.id.img4);
        img5 = (ImageView) view.findViewById(R.id.img5);
        img6 = (ImageView) view.findViewById(R.id.img6);
        img7 = (ImageView) view.findViewById(R.id.img7);
        img8 = (ImageView) view.findViewById(R.id.img8);
        lay1 = (NestedScrollView) view.findViewById(R.id.lay1);
        proImg = new ArrayList<PropertyItem>();
        //Toast.makeText(getActivity(),id+pro_type,Toast.LENGTH_SHORT).show();
        prodetail.setOnClickListener(this);
        more.setOnClickListener(this);
        prev.setOnClickListener(this);
        next.setOnClickListener(this);
        img4.setOnClickListener(this);
        img5.setOnClickListener(this);
        img6.setOnClickListener(this);
        img7.setOnClickListener(this);
        img8.setOnClickListener(this);
        new ProjectResult().execute();
        new GetFloorImage().execute();
        progressBar.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent in = new Intent(getActivity(), FacilitiesActivity.class);
        switch (v.getId()) {
            case R.id.pro_detail:
                viewPager = (ViewPager) getActivity().findViewById(R.id.main_viewpager);
                viewPager.setCurrentItem(2, true);
                break;
            case R.id.more:
                showAlertDialog();
                break;
            case R.id.prev:
                goPrev();
                break;
            case R.id.next:
                goNext();
                break;
            case R.id.img4:
                in.putExtra("loc", location);
                startActivity(in);
                break;
            case R.id.img5:
                in.putExtra("loc", location);
                startActivity(in);
                break;
            case R.id.img6:
                in.putExtra("loc", location);
                startActivity(in);
                break;
            case R.id.img7:
                in.putExtra("loc", location);
                startActivity(in);
                break;
            case R.id.img8:
                in.putExtra("loc", location);
                startActivity(in);
                break;

        }

    }

    private void goNext() {


        int currentPage = floorPlan.getCurrentItem();
        int totalPages = floorPlan.getAdapter().getCount();

        int nextPage = currentPage + 1;
        if (nextPage >= totalPages) {
            // We can't go forward anymore.
            // Loop to the first page. If you don't want looping just
            // return here.
            //nextPage = 0;
            prev.setVisibility(View.VISIBLE);
            next.setVisibility(View.GONE);
        } else {
            next.setVisibility(View.VISIBLE);

        }

        floorPlan.setCurrentItem(nextPage, true);


    }

    private void goPrev() {
        int currentPage = floorPlan.getCurrentItem();
        int totalPages = floorPlan.getAdapter().getCount();

        int previousPage = currentPage - 1;
        if (previousPage < 0) {
            // We can't go back anymore.
            // Loop to the last page. If you don't want looping just
            // return here.
            prev.setVisibility(View.GONE);
            next.setVisibility(View.VISIBLE);
        } else {
            prev.setVisibility(View.VISIBLE);

        }

        floorPlan.setCurrentItem(previousPage, true);
    }


    private void showAlertDialog() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myScrollView = inflater.inflate(R.layout.read_more, null, false);
        TextView tv = (TextView) myScrollView
                .findViewById(R.id.read_more_txt);
        tv.setText(des);
        new AlertDialog.Builder(getActivity()).setView(myScrollView)
                .setTitle("Property Description")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }

                }).show();
    }


    public class GetFloorImage extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            projectData = new HashMap<String, String>();
            projectData.put("id", id);
            try {
                JSONObject json = parser.makeHttpUrlConnection(php.project_plan, projectData);
                int succ = json.getInt("success");

                if (succ == 0) {

                } else {
                    JSONArray jsonArray = json.getJSONArray("proImg");
                    JSONObject child = jsonArray.getJSONObject(0);

                    final String img = child.optString("plan");


                    for (String ans : img.split(",")) {
                        proImg.add(new PropertyItem(ans));
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new PropertyImageAdapter(getActivity(), proImg);
                            floorPlan.setAdapter(adapter);
                            adapter.setClickListener(new PropertyImageAdapter.ClickListener() {
                                @Override
                                public void itemClicked(View v, int pos) {
                                    Intent in = new Intent(getActivity(), GalleryActivity.class);
                                    in.putExtra("id", id);
                                    in.putExtra("pos", pos);
                                    in.putExtra("from", "floor");
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
    }


    public class ProjectResult extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            projectData = new HashMap<String, String>();
            projectData.put("id", id);
            String[] amArray = new String[0];
            try {
                JSONObject json = parser.makeHttpUrlConnection(call.project_result, projectData);
                int succ = json.getInt("success");

                if (succ == 0) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            am.setVisibility(View.VISIBLE);
                            lay1.setVisibility(View.GONE);
                        }
                    });
                } else {
                    final StringBuffer sb = new StringBuffer();
                    final StringBuffer sb1 = new StringBuffer();
                    final StringBuffer sb2 = new StringBuffer();
                    JSONArray jsonArray = json.getJSONArray("project");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject child = jsonArray.getJSONObject(i);
                        id_pro = child.optString("id_pro");
                        pro_name = child.optString("pro_name");
                        price_bet = child.optString("price_bet");
                        parea = child.optString("parea");
                        pa_type = child.optString("pa_type");
                        pro_status = child.optString("pro_status");
                        des = child.optString("des");
                        plan = child.optString("plan");
                        amenities = child.optString("amenities");
                        location = child.optString("loc");

                        amArray = amenities.split(",");

                        sb.append("\n" + child.optString("pro_deatils") + " " + child.optString("protype"));
                        sb1.append("\n" + child.optString("area") + " " + child.optString("a_type"));

                        if (i == jsonArray.length() - 1) {
                            sb2.append(child.optString("price"));
                        } else {
                            sb2.append("\n" + getResources().getString(R.string.Rs) + " " + child.optString("price"));

                        }


                    }

                    final String[] finalAmArray = amArray;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            lay1.setVisibility(View.VISIBLE);
                            am.setVisibility(View.GONE);
                            projectId.setText(id_pro);
                            projectName.setText(pro_name);

                            int len = price_bet.length();
                            String price = "";
                            int get = 0;

                            if (len <= 5) {
                                price = price_bet + " " + "K";

                            } else if (len >= 6 && len < 8) {
                                get = Integer.parseInt(price_bet);
                                price = Integer.toString((get / 100000)) + " " + "Lac";
                            } else {
                                get = Integer.parseInt(price_bet);
                                price = Integer.toString((get / 10000000)) + " " + "Cr";
                            }
                            projectVal.setText(getResources().getString(R.string.Rs) + " " + price);
                            projectArea.setText(parea + " " + pa_type);
                            projectType.setText(pro_type);
                            projectStatus.setText(pro_status);
                            proType.setText(sb);
                            proArea.setText(sb1);
                            proVal.setText(sb2);
                            projectDesc.setText(des);

                            ((ProjectView)getActivity()).getSupportActionBar().setTitle(pro_name);
                            int res1, res2, res3;

                            if (finalAmArray.length == 2) {
                                res1 = getActivity().getResources().getIdentifier(finalAmArray[0].toString().replace(" ", "_").toLowerCase(), "drawable", "com.shuan.myland");
                                res2 = getActivity().getResources().getIdentifier(finalAmArray[1].toString().replace(" ", "_").toLowerCase(), "drawable", "com.shuan.myland");
                                txt1.setText(finalAmArray[0].toString());
                                txt2.setText(finalAmArray[1].toString());
                                img1.setImageResource(res1);
                                img2.setImageResource(res2);
                                seeMore.setVisibility(View.GONE);

                            } else if (finalAmArray.length <= 3) {
                                res1 = getActivity().getResources().getIdentifier(finalAmArray[0].toString().replace(" ", "_").toLowerCase(), "drawable", "com.shuan.myland");
                                res2 = getActivity().getResources().getIdentifier(finalAmArray[1].toString().replace(" ", "_").toLowerCase(), "drawable", "com.shuan.myland");
                                res3 = getActivity().getResources().getIdentifier(finalAmArray[2].toString().replace(" ", "_").toLowerCase(), "drawable", "com.shuan.myland");
                                txt1.setText(finalAmArray[0].toString());
                                txt2.setText(finalAmArray[1].toString());
                                txt3.setText(finalAmArray[2].toString());
                                img1.setImageResource(res1);
                                img2.setImageResource(res2);
                                img3.setImageResource(res3);
                                seeMore.setVisibility(View.GONE);
                            } else {

                                res1 = getActivity().getResources().getIdentifier(finalAmArray[0].toString().replace(" ", "_").toLowerCase(), "drawable", "com.shuan.myland");
                                res2 = getActivity().getResources().getIdentifier(finalAmArray[1].toString().replace(" ", "_").toLowerCase(), "drawable", "com.shuan.myland");
                                res3 = getActivity().getResources().getIdentifier(finalAmArray[2].toString().replace(" ", "_").toLowerCase(), "drawable", "com.shuan.myland");
                                txt1.setText(finalAmArray[0].toString());
                                txt2.setText(finalAmArray[1].toString());
                                txt3.setText(finalAmArray[2].toString());
                                img1.setImageResource(res1);
                                img2.setImageResource(res2);
                                img3.setImageResource(res3);
                                seeMore.setVisibility(View.VISIBLE);
                                seeMore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent in = new Intent(getActivity(), AmentiesActivity.class);
                                        in.putExtra("am", amenities);
                                        startActivity(in);
                                    }
                                });

                            }


                        }
                    });


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

}
