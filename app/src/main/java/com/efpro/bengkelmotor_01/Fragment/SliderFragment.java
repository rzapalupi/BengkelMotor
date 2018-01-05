package com.efpro.bengkelmotor_01.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.efpro.bengkelmotor_01.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SliderFragment extends Fragment {

    private static final String ARG_PARAM1 = "params";

    private String imageUrls;

//    boolean ExpandedActionBar = true;

    public SliderFragment() {
        // Required empty public constructor
    }

    public static SliderFragment newInstance(String params) {
        SliderFragment fragment = new SliderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        imageUrls = getArguments().getString(ARG_PARAM1);
        View view = inflater.inflate(R.layout.fragment_slider, container, false);
        ImageView img                           = (ImageView) view.findViewById(R.id.imgFotoBengkel);
//        AppBarLayout Appbar                     = (AppBarLayout)view.findViewById(R.id.appbar);
//        CollapsingToolbarLayout CoolToolbar     = (CollapsingToolbarLayout)view.findViewById(R.id.ctolbar);
//        Toolbar toolbar                         = (Toolbar) view.findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        Glide.with(getActivity())
                .load(imageUrls)
                .apply(new RequestOptions()
                .placeholder(R.mipmap.ic_launcher))
                .into(img);;
//        String namabengkel = ((DetailBengkelActivity)getActivity()).getNamaBengkel();
//        CoolToolbar.setTitle(namabengkel);
//        CoolToolbar.setCollapsedTitleTextColor(Color.WHITE);
//        CoolToolbar.setExpandedTitleColor(Color.WHITE);
//
//        Appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//
//                if (Math.abs(verticalOffset) > 200){
//                    ExpandedActionBar = false;
////                    invalidateOptionsMenu();
//                } else {
//                    ExpandedActionBar = true;
////                    invalidateOptionsMenu();
//                }
//
//            }
//        });

        return view;
    }

}
