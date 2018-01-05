package com.efpro.bengkelmotor_01.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.efpro.bengkelmotor_01.Activity.DetailBengkelActivity;
import com.efpro.bengkelmotor_01.Activity.ProfileActivity;
import com.efpro.bengkelmotor_01.Adapter.BengkelAdapter;
import com.efpro.bengkelmotor_01.Bengkel;
import com.efpro.bengkelmotor_01.Foto;
import com.efpro.bengkelmotor_01.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyBengkelFragment extends Fragment implements AdapterView.OnItemClickListener{

    ListView bengkelListView;
    BengkelAdapter bengkelAdapter;
    ArrayList<Bengkel> mMyBengkels;
    ArrayList<Foto> mMyFotoBengkels;
    View mView;

    public MyBengkelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_my_bengkel, container, false);
        bengkelListView = (ListView) mView.findViewById(R.id.bengkelListView);
        bengkelListView.setOnItemClickListener(this);

        mMyBengkels = ((ProfileActivity)getActivity()).getMyBengkels();
        mMyFotoBengkels = ((ProfileActivity)getActivity()).getMyfotobengkels();
        bengkelAdapter = new BengkelAdapter(getActivity(),mMyBengkels, mMyFotoBengkels);

        bengkelListView.setAdapter(bengkelAdapter);
        bengkelAdapter.notifyDataSetChanged();


        return mView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bengkel bengkel = mMyBengkels.get(position);
        Intent intent = new Intent(getActivity(), DetailBengkelActivity.class);
        intent.putExtra("BENGKEL",bengkel);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMyBengkels = ((ProfileActivity)getActivity()).getMyBengkels();
        bengkelAdapter = new BengkelAdapter(getActivity(),mMyBengkels, mMyFotoBengkels);
        bengkelListView.setAdapter(bengkelAdapter);
        bengkelAdapter.notifyDataSetChanged();
    }

}
