package com.efpro.bengkelmotor_01.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.efpro.bengkelmotor_01.Adapter.ReviewAdapter;
import com.efpro.bengkelmotor_01.Adapter.ViewPagerAdapter;
import com.efpro.bengkelmotor_01.Bengkel;
import com.efpro.bengkelmotor_01.ExpandableHeightListView;
import com.efpro.bengkelmotor_01.Foto;
import com.efpro.bengkelmotor_01.R;
import com.efpro.bengkelmotor_01.ReviewBengkel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimerTask;
import java.util.TreeMap;

public class DetailBengkelActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private static final String TAG = "DetailActivity";
    ExpandableHeightListView reviewListView;
    ReviewAdapter reviewAdapter;
    ArrayList<ReviewBengkel> mReviewBengkels = new ArrayList<>();
    List<Foto> fotoDetailBengkels = new ArrayList<>();
    FloatingActionButton fab_navigation;
    TextView    txtDAlamat, txtDHari, txtDJam, txtDToday, txtDHour, txtDTelepon,
                txtMyUsername, txtMyComment, txtPostDate;
    EditText    edtReview;
    ImageView imgMyProfile;
    Button  btnSubmit;
    ImageButton btnMenuReview;
    RatingBar rtbMyRate;
    AppBarLayout Appbar;
    CollapsingToolbarLayout CoolToolbar;
    Toolbar toolbar;
    Intent mapIntent;
    Uri gmmIntentUri, photoUrl;
    String latlong, bengkelID, reviewBengkelID, uid, username, date, comment, sPhotoUrl;
    int rate, div ;
    double sumRate = 0;
    HashMap<String, String> hashMap;
    Map<Date, String> sortedMap = new TreeMap<Date, String>();
    DatabaseReference mReviewBengkelRef, mBengkelRef;
    StorageReference mStorageRef;
    FirebaseAuth mAuth;
    boolean alreadyReview = false;
    boolean ExpandedActionBar = true;
    int status = 0;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    int dotscount = 3;
    ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bengkel);

        fab_navigation      = (FloatingActionButton) findViewById(R.id.fab_navigation);
        txtDAlamat          = (TextView) findViewById(R.id.txtDAlamat);
        txtDToday           = (TextView) findViewById(R.id.txtDToday);
        txtDHour            = (TextView) findViewById(R.id.txtDHour);
        txtDHari            = (TextView) findViewById(R.id.txtDHari);
        txtDJam             = (TextView) findViewById(R.id.txtDJam);
        txtDTelepon         = (TextView) findViewById(R.id.txtDTelepon);
        txtMyUsername       = (TextView) findViewById(R.id.txtMyUsername);
        txtMyComment        = (TextView) findViewById(R.id.txtMyComment);
        txtPostDate         = (TextView) findViewById(R.id.txtPostDate);
        edtReview           = (EditText) findViewById(R.id.edtReview);
        btnSubmit           = (Button) findViewById(R.id.btnSubmit);
        imgMyProfile        = (ImageView) findViewById(R.id.imgMyProfile);
        btnMenuReview       = (ImageButton) findViewById(R.id.btnMenuReview);
        rtbMyRate           = (RatingBar) findViewById(R.id.rtbMyRate);
        reviewListView      = (ExpandableHeightListView) findViewById(R.id.reviewListView);
        Appbar              = (AppBarLayout)findViewById(R.id.appbar);
        CoolToolbar         = (CollapsingToolbarLayout)findViewById(R.id.ctolbar);
        toolbar             = (Toolbar) findViewById(R.id.toolbar);
        viewPager           = (ViewPager) findViewById(R.id.viewPager);
        sliderDotspanel     = (LinearLayout) findViewById(R.id.SliderDots);


        setSupportActionBar(toolbar);
        reviewListView.setExpanded(true);
        btnSubmit.setOnClickListener(this);
        fab_navigation.setOnClickListener(this);

        getCurrentUserID();

        //set database review from firebase
        mReviewBengkelRef   = FirebaseDatabase.getInstance().getReference("ReviewBengkel");
        mBengkelRef         = FirebaseDatabase.getInstance().getReference("ListBengkel");
        mStorageRef         = FirebaseStorage.getInstance().getReference("FotoBengkel");
        mReviewBengkelRef.keepSynced(true);
        mBengkelRef.keepSynced(true);

        Bengkel detailBengkel = getIntent().getParcelableExtra("BENGKEL");
        hashMap = detailBengkel.getbJamBuka();
        txtDAlamat.setText(detailBengkel.getbAlamat());
        txtDTelepon.setText(": " + detailBengkel.getbTelepon());
        latlong = detailBengkel.getbLatitude() + "," + detailBengkel.getbLongitude();
        bengkelID = detailBengkel.getbID();
        CoolToolbar.setTitle(detailBengkel.getbNama());
        CoolToolbar.setCollapsedTitleTextColor(Color.WHITE);
        CoolToolbar.setExpandedTitleColor(Color.WHITE);

        for(int index=0; index<3; index++){
            String numb = String.valueOf(index);
            getDetailFotoBengkel(bengkelID, numb);
        }

        dots = new ImageView[dotscount];
        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        reviewAdapter = new ReviewAdapter(this, mReviewBengkels, status);
        getDataReview();
        SortDay();
        Appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) > 200){
                    ExpandedActionBar = false;
                    invalidateOptionsMenu();
                } else {
                    ExpandedActionBar = true;
                    invalidateOptionsMenu();
                }
            }
        });

//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);
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
                    addReview(username, comment, rate, date, sPhotoUrl);
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

    public void getDetailFotoBengkel(final String bengkelID, final String numb){
        StorageReference fotoRef = mStorageRef.child(bengkelID).child(bengkelID+"_"+numb);
        final long ONE_MEGABYTE = 1024 * 1024;
        fotoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Foto fotoBengkel = new Foto(bengkelID, bytes);
                fotoDetailBengkels.add(fotoBengkel);
                setFotoBengkel(fotoDetailBengkels);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public void getCurrentUserID(){
        //get profile current user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            //dont show cdvMyReview
            CardView cdvMyReview = (CardView) findViewById(R.id.cdvMyReview);
            cdvMyReview.setVisibility(View.GONE);
            uid = "-1";
        } else {
            FirebaseUser user = mAuth.getCurrentUser();
            uid = user.getUid();
            username = user.getDisplayName();
            photoUrl = user.getPhotoUrl();
            Log.e(TAG, "getCurrentUserID: " + photoUrl );
            Glide.with(this).asBitmap().load(photoUrl)
                    .into(new BitmapImageViewTarget(imgMyProfile) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable rounded =
                                    RoundedBitmapDrawableFactory.create(DetailBengkelActivity.this.getResources(), resource);
                            rounded.setCircular(true);
                            imgMyProfile.setImageDrawable(rounded);
                        }
                    });
            sPhotoUrl = String.valueOf(photoUrl);
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
            String hari = dft.format(date);
            String jam = entry.getValue();

            if (txtDToday.getText().length() > 0){
                if (txtDHari.getText().length() > 0) {
                    txtDHari.setText(txtDHari.getText() + "\n" + hari);
                    txtDJam.setText(txtDJam.getText() + "\n:  " + jam);
                } else {
                    txtDHari.setText(hari);
                    txtDJam.setText(":  " + jam);
                }
            } else {
                txtDToday.setText(hari);
                txtDHour.setText(":  " + jam);
            }

        }
    }

    public void addReview(String username, String comment, int rate, String date, String sPhotoUrl){
        ReviewBengkel rBengkel = new ReviewBengkel(username,comment,rate,date,sPhotoUrl);
        mReviewBengkelRef.child(bengkelID).child(uid).setValue(rBengkel);
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_side, popup.getMenu());
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

    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            DetailBengkelActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(viewPager.getCurrentItem() == 0){
                        viewPager.setCurrentItem(1);
                    } else if(viewPager.getCurrentItem() == 1){
                        viewPager.setCurrentItem(2);
                    } else {
                        viewPager.setCurrentItem(0);
                    }

                }
            });

        }
    }

    public void setFotoBengkel( List<Foto> fotoDetailBengkels){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, fotoDetailBengkels);
        viewPager.setAdapter(viewPagerAdapter);
    }

}
