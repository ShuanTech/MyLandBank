package com.shuan.myland.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


import com.shuan.myland.R;
import com.shuan.myland.fragment.BankFragment;
import com.shuan.myland.fragment.BusFragment;
import com.shuan.myland.fragment.HospitalFragment;
import com.shuan.myland.fragment.InstitutionFragment;
import com.shuan.myland.fragment.RaiwayFragment;
import com.shuan.myland.fragment.ShoppingFragment;
import com.shuan.myland.fragment.TheatreFragment;
import com.shuan.myland.tab.CacheFragmentStatePagerAdapter;
import com.shuan.myland.tab.SmartTab;


public class FacilitiesActivity extends AppCompatActivity {

    private FacilityAdapter adapter;
    private Toolbar toolbar;
    private ViewPager mPager;
    private String loc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        loc=getIntent().getStringExtra("loc");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilities);
        setTitle("");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setTitle("Facility");

        FacilityAdapter adapter=new FacilityAdapter(getSupportFragmentManager());
        mPager= (ViewPager) findViewById(R.id.viewpager);
        SmartTab tab= (SmartTab) findViewById(R.id.tab);
        mPager.setAdapter(adapter);
        tab.setViewPager(mPager);

    }


    public class FacilityAdapter extends CacheFragmentStatePagerAdapter {

        private final  String[] pro_view = getResources().getStringArray(R.array.facilities);

        public FacilityAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        protected Fragment createItem(int position) {
            Fragment f = null;
            Bundle bundle=new Bundle();
            final int pattern = position % 7;
            switch (pattern){
                case 0:
                    bundle.putString("loc",loc);
                    f=new InstitutionFragment();
                    f.setArguments(bundle);
                    break;
                case 1:
                    bundle.putString("loc",loc);
                    f=new RaiwayFragment();
                    f.setArguments(bundle);
                    break;
                case 2:
                    bundle.putString("loc",loc);
                    f=new BusFragment();
                    f.setArguments(bundle);
                    break;
                case 3:
                    bundle.putString("loc",loc);
                    f=new HospitalFragment();
                    f.setArguments(bundle);
                    break;
                case 4:
                    bundle.putString("loc",loc);
                    f=new BankFragment();
                    f.setArguments(bundle);
                    break;
                case 5:
                    bundle.putString("loc",loc);
                    f=new ShoppingFragment();
                    f.setArguments(bundle);
                    break;
                case 6:
                    bundle.putString("loc",loc);
                    f=new TheatreFragment();
                    f.setArguments(bundle);
                    break;

            }
            return f;
        }

        @Override
        public int getCount() {
            return pro_view.length;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return pro_view[position];
        }
    }

    private Fragment getCurrentFragment() {
        return adapter.getItemAt(mPager.getCurrentItem());
    }

}
