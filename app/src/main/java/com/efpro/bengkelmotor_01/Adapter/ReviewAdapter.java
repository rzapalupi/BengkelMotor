package com.efpro.bengkelmotor_01.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.efpro.bengkelmotor_01.R;
import com.efpro.bengkelmotor_01.ReviewBengkel;

import java.util.List;

/**
 * Created by rzapalupi on 12/13/2017.
 */

public class ReviewAdapter extends ArrayAdapter<ReviewBengkel> {

//    public ReviewAdapter(@NonNull Context context,  @NonNull List<ReviewBengkel> objects) {
//        super(context, 0, objects);
//    }
    int status;
    public ReviewAdapter(@NonNull Context context,  @NonNull List<ReviewBengkel> objects, int status) {
        super(context, 0, objects);
        this.status = status;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.review_list, parent, false);

        }

        ImageView imgProfile    = (ImageView) convertView.findViewById(R.id.imgProfile);
        TextView txtUsername    = (TextView) convertView.findViewById(R.id.txtUsername);
        TextView txtDate        = (TextView) convertView.findViewById(R.id.txtDate);
        TextView txtComment     = (TextView) convertView.findViewById(R.id.txtComment);
        RatingBar rtbUserRate   = (RatingBar) convertView.findViewById(R.id.rtbUserRate);

        ReviewBengkel reviewBengkel = getItem(position);
        txtUsername.setText(reviewBengkel.getUsername());
        txtDate.setText(reviewBengkel.getDate());
        txtComment.setText(reviewBengkel.getComment());
        rtbUserRate.setRating(reviewBengkel.getRate());

        if(status == 1){
            imgProfile.setVisibility(View.GONE);
            txtUsername.setVisibility(View.GONE);
        }


        return convertView;
    }
}
