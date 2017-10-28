package com.efpro.bengkelmotor_01;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rzapalupi on 10/26/2017.
 */

public class BengkelAdapter extends ArrayAdapter<Bengkel> {


    public BengkelAdapter(@NonNull Context context, @NonNull List<Bengkel> objects) {
        super(context, 0, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.bengkel_list, parent, false);
        }

        ImageView imgList   = (ImageView) convertView.findViewById(R.id.imgList);
        TextView txtNama    = (TextView) convertView.findViewById(R.id.txtNama);
        TextView txtAlamat  = (TextView) convertView.findViewById(R.id.txtAlamat);
        TextView txtJarak   = (TextView) convertView.findViewById(R.id.txtJarak);

        Bengkel bengkel = getItem(position);
        txtNama.setText(bengkel.getbNama());
        txtAlamat.setText(bengkel.getbAlamat());
//        double x = bengkel.getbJarak();
        txtJarak.setText(String.format("%.2f",bengkel.getbJarak()) + "Km");

        return convertView;
    }
}
