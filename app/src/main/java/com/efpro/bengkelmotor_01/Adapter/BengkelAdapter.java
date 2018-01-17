package com.efpro.bengkelmotor_01.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.efpro.bengkelmotor_01.Activity.AddBengkelActivity;
import com.efpro.bengkelmotor_01.Bengkel;
import com.efpro.bengkelmotor_01.Foto;
import com.efpro.bengkelmotor_01.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rzapalupi on 10/26/2017.
 */

public class BengkelAdapter extends ArrayAdapter<Bengkel> {


    ArrayList<Foto> mFotoBengkels;
    ImageButton btnMenuReview;

    public BengkelAdapter(@NonNull Context context, @NonNull List<Bengkel> objects, ArrayList<Foto> mFotoBengkels) {
        super(context, 0, objects);
        this.mFotoBengkels = mFotoBengkels;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

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
            final Bengkel bengkel = getItem(position);
            txtNama.setText(bengkel.getbNama());
            txtAlamat.setText(bengkel.getbAlamat());
            txtRating.setText(String.format("%.1f",bengkel.getbRate()));

            //Set Foto Bengkel
            for (Foto fotoBengkel : mFotoBengkels){
                if(fotoBengkel.getId().equals(bengkel.getbID())){
                    byte[] bytes = fotoBengkel.getFoto();
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imgList.setImageBitmap(bmp);
                }
            }

            //Set untuk MyBengkelFragment
            if(bengkel.getbJarak() == 0){
                txtJarak.setVisibility(View.GONE);
                ImageButton btnMenuBengkel       = (ImageButton) convertView.findViewById(R.id.btnMenuBengkel);
                btnMenuBengkel.setVisibility(View.VISIBLE);
                btnMenuBengkel.setFocusable(false);
                btnMenuBengkel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(getContext(), v);
                        MenuInflater inflater = popup.getMenuInflater();
                        inflater.inflate(R.menu.menu_side, popup.getMenu());
                        popup.show();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.menu_edit:
                                        Intent intent = new Intent(getContext(), AddBengkelActivity.class);
                                        intent.putExtra("EDIT",bengkel);
                                        getContext().startActivity(intent);
                                        Toast.makeText(getContext(), "edit", Toast.LENGTH_SHORT).show();
                                        return true;
                                    case R.id.menu_delete:
                                        final DatabaseReference mBengkelRef =FirebaseDatabase.getInstance().getReference("ListBengkel/" + bengkel.getbID());
                                        final DatabaseReference mReviewBengkelRef =FirebaseDatabase.getInstance().getReference("ReviewBengkel/" + bengkel.getbID());
                                        final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("FotoBengkel/" + bengkel.getbID() +"/");
                                        new AlertDialog.Builder(getContext())
                                        .setTitle("Delete Confirmation")
                                        .setMessage("Anda yakin ingin menghapus " + bengkel.getbNama() +" ?")
                                        // Add the buttons
                                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // User clicked OK button
                                                mStorageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // File deleted successfully
                                                        mBengkelRef.removeValue();
                                                        mReviewBengkelRef.removeValue();
                                                        mFotoBengkels.remove(position);
                                                        Toast.makeText(getContext(), "Bengkel Berhasil dihapus", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {
                                                        // Uh-oh, an error occurred!
                                                        Toast.makeText(getContext(), "Bengkel gagal dihapus", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                dialog.dismiss();
                                            }
                                        })
                                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // User cancelled the dialog
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                    }
                });
            } else{
                txtJarak.setText(String.format("%.2f",bengkel.getbJarak()) + " Km");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }

  

}
