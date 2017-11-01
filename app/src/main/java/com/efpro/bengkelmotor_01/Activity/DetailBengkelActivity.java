package com.efpro.bengkelmotor_01.Activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.efpro.bengkelmotor_01.Bengkel;
import com.efpro.bengkelmotor_01.R;

public class DetailBengkelActivity extends AppCompatActivity {

    FloatingActionButton fab_navigation;
    TextView txtDNama, txtDAlamat, txtDJamBuka, txtDTelepon;

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
    }
}
