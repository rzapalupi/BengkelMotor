package com.efpro.bengkelmotor_01;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BengkelFragment extends Fragment {

    ListView bengkelListView;
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

        mBengkels = ((MainActivity)getActivity()).getBengkelList();
        bengkelAdapter = new BengkelAdapter(getActivity(),mBengkels );
        bengkelListView.setAdapter(bengkelAdapter);
        bengkelAdapter.notifyDataSetChanged();

        return mView;
    }
}
