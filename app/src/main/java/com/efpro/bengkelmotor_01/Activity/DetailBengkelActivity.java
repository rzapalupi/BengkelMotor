package com.efpro.bengkelmotor_01.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.efpro.bengkelmotor_01.Adapter.ReviewAdapter;
import com.efpro.bengkelmotor_01.Bengkel;
import com.efpro.bengkelmotor_01.ExpandableHeightListView;
import com.efpro.bengkelmotor_01.R;
import com.efpro.bengkelmotor_01.ReviewBengkel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class DetailBengkelActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private static final String TAG = "DetailActivity";
    ExpandableHeightListView reviewListView;
    ReviewAdapter reviewAdapter;
    ArrayList<ReviewBengkel> mReviewBengkels = new ArrayList<>();
    FloatingActionButton fab_navigation;
    TextView    txtDNama, txtDAlamat, txtDJamBuka, txtDTelepon,
                txtMyUsername, txtMyComment, txtPostDate;
    EditText    edtReview;
    Button  btnSubmit;
    ImageButton btnMenuReview;
    RatingBar rtbMyRate;
    Intent mapIntent;
    Uri gmmIntentUri;
    String latlong, namaBengkel, bengkelID, reviewBengkelID, uid, username, date, comment;
    int rate, div ;
    double sumRate = 0;
    HashMap<String, String> hashMap;
    Map<Date, String> sortedMap = new TreeMap<Date, String>();
    DatabaseReference mReviewBengkelRef, mBengkelRef;
    boolean alreadyReview = false;
    int status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bengkel);

        fab_navigation  = (FloatingActionButton) findViewById(R.id.fab_navigation);
        txtDNama        = (TextView) findViewById(R.id.txtDNama);
        txtDAlamat      = (TextView) findViewById(R.id.txtDAlamat);
        txtDJamBuka     = (TextView) findViewById(R.id.txtDJamBuka);
        txtDTelepon     = (TextView) findViewById(R.id.txtDTelepon);
        txtMyUsername   = (TextView) findViewById(R.id.txtMyUsername);
        txtMyComment    = (TextView) findViewById(R.id.txtMyComment);
        txtPostDate     = (TextView) findViewById(R.id.txtPostDate);
        edtReview       = (EditText) findViewById(R.id.edtReview);
        btnSubmit       = (Button) findViewById(R.id.btnSubmit);
        btnMenuReview   = (ImageButton) findViewById(R.id.btnMenuReview);
        rtbMyRate       = (RatingBar) findViewById(R.id.rtbMyRate);
        reviewListView  = (ExpandableHeightListView) findViewById(R.id.reviewListView);
        reviewListView.setExpanded(true);
        btnSubmit.setOnClickListener(this);
        fab_navigation.setOnClickListener(this);

        getCurrentUserID();

        //set database review from firebase
        mReviewBengkelRef = FirebaseDatabase.getInstance().getReference("ReviewBengkel");
        mBengkelRef = FirebaseDatabase.getInstance().getReference("ListBengkel");
        mReviewBengkelRef.keepSynced(true);
        mBengkelRef.keepSynced(true);

        Bengkel detailBengkel = getIntent().getParcelableExtra("BENGKEL");
        hashMap = detailBengkel.getbJamBuka();
        txtDNama.setText(detailBengkel.getbNama());
        txtDAlamat.setText(detailBengkel.getbAlamat());
        latlong = detailBengkel.getbLatitude() + "," + detailBengkel.getbLongitude();
        bengkelID = detailBengkel.getbID();
        namaBengkel = detailBengkel.getbNama();
        reviewAdapter = new ReviewAdapter(this, mReviewBengkels, status);
//        reviewAdapter = new ReviewAdapter(this, mReviewBengkels);
        getDataReview();

        SortDay();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                case R.id.btnSubmit:
                //do something
                comment = String.valueOf(edtReview.getText());
                rate = (int) rtbMyRate.getRating();
                SimpleDateFormat postDateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("in", "ID", "ID"));
                Calendar c = Calendar.getInstance();
                date = postDateFormat.format(c.getTime());
                if (comment.isEmpty() || rate == 0){
                    Toast.makeText(this, "Silahkan isi nilai atau ulasan anda", Toast.LENGTH_SHORT).show();
                } else {
                    addReview(username, comment, rate,  date);
                    getDataReview();
                }
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                alreadyReview = false;
                updateUI();
                return true;
            case R.id.menu_delete:
                alreadyReview = false;
                mReviewBengkelRef.child(bengkelID).child(uid).removeValue();
                getDataReview();
                edtReview.setText("");
                rtbMyRate.setRating(0);
                updateUI();
                return true;
            default:
                return false;
        }
    }

    public void getDataReview() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mReviewBengkels.clear();

                //check bengkelID
                for (DataSnapshot bengkelSnapshot : dataSnapshot.getChildren()) {
                    reviewBengkelID = bengkelSnapshot.getKey();
                    //jika sama, maka akan diambil data review bengkel tersebut
                    if(reviewBengkelID.equals(bengkelID)){
                        div = 0;
                        sumRate = 0;
                        for (DataSnapshot reviewSnapshot : bengkelSnapshot.getChildren()) {
                            ReviewBengkel reviewBengkel = reviewSnapshot.getValue(ReviewBengkel.class);
                            if (uid.equals(reviewSnapshot.getKey())) {
                                txtMyUsername.setText(reviewBengkel.getUsername());
                                txtPostDate.setText(reviewBengkel.getDate());
                                rtbMyRate.setRating(reviewBengkel.getRate());
                                txtMyComment.setText(reviewBengkel.getComment());
                                edtReview.setText(reviewBengkel.getComment());
                                alreadyReview = true;
                                updateUI();

                            } else {
                                mReviewBengkels.add(reviewBengkel);
                                //set adapter to create listview
                            }
                            sumRate = sumRate + reviewBengkel.getRate();
                            div++;
                        }
                        sumRate = sumRate/div; //menghitung total rating
                    }
                }
                mBengkelRef.child(bengkelID).child("bRate").setValue(sumRate);
                reviewListView.setAdapter(reviewAdapter);
                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled (DatabaseError databaseError){
                Log.w(TAG, "loadNote:onCancelled", databaseError.toException());
            }
        };
        mReviewBengkelRef.addValueEventListener(valueEventListener);

    }

    public void getCurrentUserID(){
        //get profile current user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            //dont show cdvMyReview
            CardView cdvMyReview = (CardView) findViewById(R.id.cdvMyReview);
            cdvMyReview.setVisibility(View.GONE);
        } else {
            FirebaseUser user = mAuth.getCurrentUser();
            uid = user.getUid();
            username = user.getDisplayName();
        }

    }

    public void SortDay(){
        //Sorting day
        String today;
        //format hari untuk agar bisa dijumlahkan (current day)
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE, MMM dd yyyy", new Locale("in", "ID", "ID"));
        //format hari yang akan disimpan pada hash
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
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                String hari = entry.getKey();
                String jam = entry.getValue();
                if (hari.equals(today)) {
                    sortedMap.put(c.getTime(), jam);
                    Log.e(TAG, "Hari sorted : " + c.getTime());
                }
            }
        }

        for (Map.Entry<Date, String> entry : sortedMap.entrySet()) {
            Date date = entry.getKey();
            String jambuka = entry.getValue();
            String jadwal = dft.format(date);
            String tmpJamBuka = (jadwal + "\t\t\t\t\t\t\t" + ": " + jambuka);
            if (txtDJamBuka.getText().length() > 0) {
                txtDJamBuka.setText(txtDJamBuka.getText() + "\n" + tmpJamBuka);
            } else {
                txtDJamBuka.setText(tmpJamBuka);
            }
        }
    }

    public void addReview(String username, String comment, int rate, String date){
        ReviewBengkel rBengkel = new ReviewBengkel(username,comment,rate,date);
        //mReviewBengkelRef.child(bengkelID).child("NamaBengkel").setValue(namaBengkel);
        mReviewBengkelRef.child(bengkelID).child(uid).setValue(rBengkel);
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_review, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(this);
    }

    public void updateUI(){
        if (alreadyReview){
            txtMyUsername.setVisibility(View.VISIBLE);
            txtMyComment.setVisibility(View.VISIBLE);
            rtbMyRate.setIsIndicator(true);
            edtReview.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.GONE);
            btnMenuReview.setVisibility(View.VISIBLE);
        } else {
            txtMyUsername.setVisibility(View.GONE);
            txtMyComment.setVisibility(View.GONE);
            rtbMyRate.setIsIndicator(false);
            edtReview.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.VISIBLE);
            btnMenuReview.setVisibility(View.GONE);
            txtPostDate.setText("Nilai bengkel ini");
        }
    }


}
