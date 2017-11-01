package com.efpro.bengkelmotor_01.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.efpro.bengkelmotor_01.Activity.DetailBengkelActivity;
import com.efpro.bengkelmotor_01.Activity.MainActivity;
import com.efpro.bengkelmotor_01.Bengkel;
import com.efpro.bengkelmotor_01.BengkelAdapter;
import com.efpro.bengkelmotor_01.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BengkelFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    ListView bengkelListView;
    FloatingActionButton fab_bengkel;
    BengkelAdapter bengkelAdapter;
    ArrayList<Bengkel> mBengkels;
    View mView;


    public BengkelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_bengkel, container, false);
        bengkelListView = (ListView) mView.findViewById(R.id.bengkelListView);
        fab_bengkel = (FloatingActionButton) mView.findViewById(R.id.fab_bengkel);
        fab_bengkel.setOnClickListener(this);

        mBengkels = ((MainActivity)getActivity()).getBengkelList();
        bengkelAdapter = new BengkelAdapter(getActivity(),mBengkels );
        bengkelListView.setAdapter(bengkelAdapter);
        bengkelAdapter.notifyDataSetChanged();

        bengkelListView.setOnItemClickListener(this);

        return mView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_bengkel:
                MapFragment mapFragment = new MapFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.mainLayout, mapFragment).commit();
            break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bengkel bengkel = mBengkels.get(position);
        Intent intent = new Intent(getActivity(), DetailBengkelActivity.class);
        intent.putExtra("BENGKEL",bengkel);
        startActivity(intent);
    }
}
