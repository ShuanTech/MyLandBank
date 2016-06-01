package com.shuan.myland.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuan.myland.R;
import com.shuan.myland.adapter.SimpleRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DummyFragment extends Fragment {

    int color;
    SimpleRecyclerAdapter adapter;

    public DummyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dummy, container, false);


        /*final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
        frameLayout.setBackgroundColor(color);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < VersionModel.data.length; i++) {
            list.add(VersionModel.data[i]);
        }

        Amenity = new SimpleRecyclerAdapter(list);
        recyclerView.setAdapter(Amenity);*/

        return view;
    }

}
