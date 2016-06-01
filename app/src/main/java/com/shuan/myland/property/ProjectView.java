package com.shuan.myland.property;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.shuan.myland.R;
import com.shuan.myland.activities.GalleryActivity;
import com.shuan.myland.adapter.PropertyImageAdapter;
import com.shuan.myland.fragment.DummyFragment;
import com.shuan.myland.fragment.ProjectOverviewFragment;
import com.shuan.myland.fragment.UnitFragment;
import com.shuan.myland.list.PropertyItem;
import com.shuan.myland.parser.HttpUrlConnectionParser;
import com.shuan.myland.parser.php;
import com.shuan.myland.tab.CacheFragmentStatePagerAdapter;
import com.shuan.myland.utils.Common;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectView extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ViewPager viewPager, pro_img;
    private TabLayout tabLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private HttpUrlConnectionParser parser = new HttpUrlConnectionParser();
    private php call = new php();
    public HashMap<String, String> proImgData = new HashMap<String, String>();
    public ArrayList<PropertyItem> proImg;
    private PropertyImageAdapter adapter;
    private DisplayImageOptions options;
    private Common mApp;
    private String id, protype;
    public ImageButton prev, next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mApp = (Common) getApplicationContext();
        id = getIntent().getStringExtra("id");
        protype = getIntent().getStringExtra("pro_type");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Parallax Tabs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collaps_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        pro_img = (ViewPager) findViewById(R.id.img);
        prev = (ImageButton) findViewById(R.id.prev);
        next = (ImageButton) findViewById(R.id.next);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                /*switch (tab.getPosition()) {
                    case 0:
                        showToast("One");
                        break;
                    case 1:
                        showToast("Two");

                        break;
                    case 2:
                        showToast("Three");

                        break;
                }*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        proImg = new ArrayList<PropertyItem>();
        new LoadPropertyImg().execute();
        prev.setOnClickListener(this);
        next.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prev:
                goPrev();
                break;
            case R.id.next:
                goNext();
                break;
        }
    }


    private void goNext() {


        int currentPage = pro_img.getCurrentItem();
        int totalPages = pro_img.getAdapter().getCount();

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

        pro_img.setCurrentItem(nextPage, true);


    }

    private void goPrev() {
        int currentPage = pro_img.getCurrentItem();
        int totalPages = pro_img.getAdapter().getCount();

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

        pro_img.setCurrentItem(previousPage, true);
    }

    public class LoadPropertyImg extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            proImgData = new HashMap<String, String>();
            proImgData.put("id", id);

            try {
                JSONObject json = parser.makeHttpUrlConnection(php.project_img, proImgData);
                int succ = json.getInt("success");

                if (succ == 0) {

                } else {
                    JSONArray jsonArray = json.getJSONArray("proImg");
                    JSONObject child = jsonArray.getJSONObject(0);
                    final String man_img = child.optString("man_img");
                    final String img = child.optString("gallery");

                    proImg.add(new PropertyItem(man_img));

                    for (String ans : img.split(",")) {
                        proImg.add(new PropertyItem(ans));
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new PropertyImageAdapter(getApplicationContext(), proImg);
                            pro_img.setAdapter(adapter);
                            adapter.setClickListener(new PropertyImageAdapter.ClickListener() {
                                @Override
                                public void itemClicked(View v, int pos) {
                                    Intent in = new Intent(getApplicationContext(), GalleryActivity.class);
                                    in.putExtra("id", id);
                                    in.putExtra("pos", pos);
                                    in.putExtra("from", "project");
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


    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

    }

    class ViewPagerAdapter extends CacheFragmentStatePagerAdapter {
        public final String[] tabs = {"Overview", "Unit"};

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        protected Fragment createItem(int position) {
            Fragment f = null;

            Bundle bundle = new Bundle();
            switch (position) {
                case 0:

                    bundle.putString("id", id);
                    bundle.putString("pro_type", protype);
                    f = new ProjectOverviewFragment();
                    f.setArguments(bundle);
                    break;
                case 1:
                    bundle.putString("id", id);
                    f = new UnitFragment();
                    f.setArguments(bundle);
                    break;
                case 2:
                    f = new DummyFragment();
                    break;
                case 3:
                    f = new DummyFragment();
                    //f = new ShopFragment();
                    break;
                case 4:
                    //f = new BusTiming();
                    break;
            }

            return f;
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }


    }


}
