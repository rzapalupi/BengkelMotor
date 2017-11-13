package com.efpro.bengkelmotor_01.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.efpro.bengkelmotor_01.Bengkel;
import com.efpro.bengkelmotor_01.R;

public class DetailBengkelActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab_navigation;
    TextView txtDNama, txtDAlamat, txtDJamBuka, txtDTelepon;
    Intent mapIntent;
    Uri gmmIntentUri;
    String latlong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bengkel);

        fab_navigation  = (FloatingActionButton) findViewById(R.id.fab_navigation);
        txtDNama        = (TextView) findViewById(R.id.txtDNama);
        txtDAlamat      = (TextView) findViewById(R.id.txtDAlamat);
        txtDJamBuka     = (TextView) findViewById(R.id.txtDJamBuka);
        txtDTelepon     = (TextView) findViewById(R.id.txtDTelepon);

        Bengkel detailBengkel = getIntent().getParcelableExtra("BENGKEL");

        txtDNama.setText(detailBengkel.getbNama());
        txtDAlamat.setText(detailBengkel.getbAlamat());
        //txtDJamBuka.setText(detailBengkel.getbJam());
        txtDTelepon.setText(detailBengkel.getbTelepon());

        latlong = detailBengkel.getbLatitude() + "," + detailBengkel.getbLongitude();

        fab_navigation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_navigation:
                // Buat Uri dari intent string. Gunakan hasilnya untuk membuat Intent.
                gmmIntentUri = Uri.parse("google.navigation:q=" + latlong);
                // Buat Uri dari intent gmIntentUri. Set action => ACTION_VIEW
                mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Set package Google Maps untuk tujuan aplikasi yang di Intent yaitu google maps
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(DetailBengkelActivity.this, "Google Maps Belum Terinstal. Install Terlebih dahulu.",
                            Toast.LENGTH_LONG).show();
                }
            break;
        }
    }
}
