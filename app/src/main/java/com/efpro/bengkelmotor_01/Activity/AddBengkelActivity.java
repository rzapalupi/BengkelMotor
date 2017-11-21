package com.efpro.bengkelmotor_01.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.efpro.bengkelmotor_01.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

public class AddBengkelActivity extends AppCompatActivity implements View.OnClickListener {


    int PLACE_PICKER_REQUEST = 1;
    Button btnSetLokasi;
    TextView txtLat, txtLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bengkel);

        txtLat   = (TextView) findViewById(R.id.txtLat);
        txtLng   = (TextView) findViewById(R.id.txtLng);
        btnSetLokasi = (Button) findViewById(R.id.btnSetLokasi);
        btnSetLokasi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSetLokasi:
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = builder.build(this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                LatLng latLng = place.getLatLng();
                double lat = latLng.latitude;
                double lng = latLng.longitude;
                //Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                txtLat.setText("Latitude: " + lat);
                txtLng.setText("Longitude: " + lng);
            }
        }
    }
}
