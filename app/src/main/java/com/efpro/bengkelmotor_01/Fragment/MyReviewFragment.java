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
import com.efpro.bengkelmotor_01.Adapter.ReviewAdapter;
import com.efpro.bengkelmotor_01.Bengkel;
import com.efpro.bengkelmotor_01.R;
import com.efpro.bengkelmotor_01.ReviewBengkel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyReviewFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView reviewListView;
    ReviewAdapter reviewAdapter;
    ArrayList<ReviewBengkel> mMyReviews;
    ArrayList<Bengkel> mBengkels;
    View mView;
    int status = 1;

    public MyReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_my_review, container, false);
        reviewListView = (ListView) mView.findViewById(R.id.reviewListView);

        mMyReviews      = ((ProfileActivity)getActivity()).getMyReviews();
        mBengkels       = ((ProfileActivity)getActivity()).getTmpBengkels();
        //reviewAdapter   = new ReviewAdapter(getActivity(),mMyReviews, status);
        reviewAdapter   = new ReviewAdapter(getActivity(),mMyReviews, mBengkels, status);
        reviewListView.setAdapter(reviewAdapter);
        reviewAdapter.notifyDataSetChanged();
        reviewListView.setOnItemClickListener(this);

        return mView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bengkel bengkel = mBengkels.get(position);
        Intent intent = new Intent(getActivity(), DetailBengkelActivity.class);
        intent.putExtra("BENGKEL",bengkel);
        startActivity(intent);
    }
}
