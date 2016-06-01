package com.shuan.myland.setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.shuan.myland.R;
import com.shuan.myland.adapter.ContactAdapter;
import com.shuan.myland.list.ContactList;

import java.util.ArrayList;

public class ContactUs extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<ContactList> list;
    private ContactAdapter adapter;
    private ListView contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        toolbar= (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        contactList= (ListView) findViewById(R.id.contact_list);
        int[] img={R.drawable.palakkad,R.drawable.mannarkkad,R.drawable.perinthalmanna};
        String[] head={"PALAKKAD","MANNARKKAD","PERINTHALMANNA"};
        String[] addr={getResources().getString(R.string.palakkadAddr),getResources().getString(R.string.mannarkkadAddr),getResources().getString(R.string.periAddr)};
        String[] ph={getResources().getString(R.string.palakkadph),getResources().getString(R.string.mannarkkadph),getResources().getString(R.string.periph)};
        String[] land={getResources().getString(R.string.palakkadland),getResources().getString(R.string.mannarkkadland),getResources().getString(R.string.periland)};
        String[] fax={getResources().getString(R.string.palakkadfax),getResources().getString(R.string.mannarkkadfax),getResources().getString(R.string.perifax)};

        list=new ArrayList<ContactList>();
        for(int i=0;i<img.length;i++){
            list.add(new ContactList(img[i],head[i],addr[i],ph[i],land[i],fax[i]));
        }
        adapter=new ContactAdapter(getApplicationContext(),list);
        contactList.setAdapter(adapter);
    }


}
