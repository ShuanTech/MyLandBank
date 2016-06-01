package com.shuan.myland.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;

import com.shuan.myland.R;
import com.shuan.myland.adapter.Amenity;
import com.shuan.myland.list.Amities;

import java.util.ArrayList;

public class AmentiesActivity extends AppCompatActivity {

    private Amenity adapter;
    private String am;
    private ArrayList<Amities> amity;
    private GridView gridView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        am = getIntent().getStringExtra("am");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amenties);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Amenities");
        gridView = (GridView) findViewById(R.id.grid);
        amity = new ArrayList<Amities>();

        String[] str = am.split(",");
        for (int i = 0; i < str.length; i++) {
            amity.add(new Amities(str[i].toString()));
        }

        adapter = new Amenity(getApplicationContext(), amity);
        gridView.setAdapter(adapter);


    }
}
