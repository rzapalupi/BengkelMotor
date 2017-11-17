package com.efpro.bengkelmotor_01.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.efpro.bengkelmotor_01.Bengkel;
import com.efpro.bengkelmotor_01.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class DetailBengkelActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DetailActivity";

    FloatingActionButton fab_navigation;
    TextView txtDNama, txtDAlamat, txtDJamBuka, txtDTelepon;
    Intent mapIntent;
    Uri gmmIntentUri;
    String latlong;
    HashMap<String, Long> hashMap;
    Map<Date, Long> sortedMap = new TreeMap<Date, Long>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bengkel);

        fab_navigation = (FloatingActionButton) findViewById(R.id.fab_navigation);
        txtDNama = (TextView) findViewById(R.id.txtDNama);
        txtDAlamat = (TextView) findViewById(R.id.txtDAlamat);
        txtDJamBuka = (TextView) findViewById(R.id.txtDJamBuka);
        txtDTelepon = (TextView) findViewById(R.id.txtDTelepon);


        Bengkel detailBengkel = getIntent().getParcelableExtra("BENGKEL");
        hashMap = detailBengkel.getbJamBuka();
        txtDNama.setText(detailBengkel.getbNama());
        txtDAlamat.setText(detailBengkel.getbAlamat());


        String today;
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE, MMM dd yyyy", new Locale("in", "ID", "ID"));
        SimpleDateFormat dft = new SimpleDateFormat("EEEE", new Locale("in", "ID", "ID"));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        today = dayFormat.format(c.getTime());
        Log.e(TAG, "Hari ini : " + today);
        try {
            c.setTime(dayFormat.parse(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i <= 6; i++) {
            c.add(Calendar.DATE, 1);  // number of days to add
            today = dft.format(c.getTime());  // dt is now the new date
            Log.e(TAG, "Hari besok : " + today);
            for (Map.Entry<String, Long> entry : hashMap.entrySet()) {
                String hari = entry.getKey();
                Long jam = entry.getValue();
                if (hari.equals(today)) {
                    sortedMap.put(c.getTime(), jam);
                    Log.e(TAG, "Hari sorted : " + c.getTime());
                }
            }
        }

        for (Map.Entry<Date, Long> entry : sortedMap.entrySet()) {
            Date date = entry.getKey();
            Long jambuka = entry.getValue();
            String jadwal = dft.format(date);
            String tmpJamBuka = (jadwal + "\t\t\t\t\t\t\t" + ": " + jambuka);
            if (txtDJamBuka.getText().length() > 0) {
                txtDJamBuka.setText(txtDJamBuka.getText() + "\n" + tmpJamBuka);
            } else {
                txtDJamBuka.setText(tmpJamBuka);
            }
        }
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
