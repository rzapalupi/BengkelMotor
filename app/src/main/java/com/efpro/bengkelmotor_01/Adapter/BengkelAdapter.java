package com.efpro.bengkelmotor_01.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.efpro.bengkelmotor_01.Bengkel;
import com.efpro.bengkelmotor_01.Foto;
import com.efpro.bengkelmotor_01.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rzapalupi on 10/26/2017.
 */

public class BengkelAdapter extends ArrayAdapter<Bengkel> {


    ArrayList<Foto> mFotoBengkels;

    public BengkelAdapter(@NonNull Context context, @NonNull List<Bengkel> objects, ArrayList<Foto> mFotoBengkels) {
        super(context, 0, objects);
        this.mFotoBengkels = mFotoBengkels;
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
        TextView txtRating   = (TextView) convertView.findViewById(R.id.txtRating);


        try {
            Bengkel bengkel = getItem(position);
            txtNama.setText(bengkel.getbNama());
            txtAlamat.setText(bengkel.getbAlamat());
            txtRating.setText(String.format("%.1f",bengkel.getbRate()));

            for (Foto fotoBengkel : mFotoBengkels){
                if(fotoBengkel.getId().equals(bengkel.getbID())){
                    byte[] bytes = fotoBengkel.getFoto();
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imgList.setImageBitmap(bmp);
                }
            }

            if(bengkel.getbJarak() == 0){
                txtJarak.setVisibility(View.GONE);
            } else{
                txtJarak.setText(String.format("%.2f",bengkel.getbJarak()) + " Km");
            }
        } catch (Exception e){
            e.printStackTrace();
        }



        return convertView;
    }
}
