package com.efpro.bengkelmotor_01.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.efpro.bengkelmotor_01.Adapter.TabsPagerAdapter;
import com.efpro.bengkelmotor_01.Bengkel;
import com.efpro.bengkelmotor_01.Fragment.MyBengkelFragment;
import com.efpro.bengkelmotor_01.Fragment.MyReviewFragment;
import com.efpro.bengkelmotor_01.R;
import com.efpro.bengkelmotor_01.ReviewBengkel;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ProfileActivity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    Button btnLogOut, btnRegBengkel;
    TextView txtNamaUser;
    ImageView imgProfileUser;
    String uid, name, bengkelID, namaBengkel;
    Uri photoUrl;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    //static boolean calledAlready_fbProfileAct = false; // flag for Fragment Status
    private DatabaseReference mMyBengkelRef, mReviewBengkelRef;
    private ArrayList<Bengkel> myBengkels = new ArrayList<>();
    private ArrayList<ReviewBengkel> myReviews = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initilization
        txtNamaUser     = (TextView) findViewById(R.id.txtNamaUser);
        imgProfileUser  = (ImageView) findViewById(R.id.imgProfileUser);
        btnLogOut       = (Button) findViewById(R.id.btnLogOut);
        btnRegBengkel   = (Button) findViewById(R.id.btnRegBengkel);
        viewPager       = (ViewPager) findViewById(R.id.viewpager);
        tabLayout       = (TabLayout) findViewById(R.id.tabs);
        btnRegBengkel.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);


        mMyBengkelRef = FirebaseDatabase.getInstance().getReference("ListBengkel");
        mReviewBengkelRef = FirebaseDatabase.getInstance().getReference("ReviewBengkel");
        mMyBengkelRef.keepSynced(true);
        mReviewBengkelRef.keepSynced(true);

        getCurrentUser();
        getDataMyBengkel();
        getDataMyReview();

    }

    private void setupViewPager(ViewPager viewPager) {
        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MyBengkelFragment(), "My Bengkel");
        adapter.addFragment(new MyReviewFragment(), "My Review");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogOut:
                signOut();
            break;
            case R.id.btnRegBengkel:
                Intent intentReg = new Intent(this, AddBengkelActivity.class);
                startActivity(intentReg);
            break;
        }
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                        finish();
                    }
                });

        mAuth.signOut();
        LoginManager.getInstance().logOut();
    }

    public ArrayList<Bengkel> getMyBengkels() {
        return myBengkels;
    }

    public ArrayList<ReviewBengkel> getMyReviews() {
        return myReviews;
    }

    public void getDataMyBengkel(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myBengkels.clear();

                for (DataSnapshot bengkelSnapshot: dataSnapshot.getChildren()) {
                    Bengkel bengkel = bengkelSnapshot.getValue(Bengkel.class);

                    if(bengkel.getbUid().equals(uid)) {
                        myBengkels.add(bengkel);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadNote:onCancelled", databaseError.toException());
            }
        };
        mMyBengkelRef.addValueEventListener(valueEventListener);
    }

    public void getDataMyReview(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myReviews.clear();
                for (DataSnapshot bengkelSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot reviewSnapshot : bengkelSnapshot.getChildren()) {
                        ReviewBengkel reviewBengkel = reviewSnapshot.getValue(ReviewBengkel.class);
                        if (uid.equals(reviewSnapshot.getKey())) {
                            myReviews.add(reviewBengkel);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadNote:onCancelled", databaseError.toException());
            }
        };
        mReviewBengkelRef.addValueEventListener(valueEventListener);
    }

    public void getCurrentUser(){
        //get profile current user
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(ProfileActivity.this, SignInActivity.class));
            finish();
        } else {
            FirebaseUser user = mAuth.getCurrentUser();
            uid = user.getUid();
            name = user.getDisplayName();
            photoUrl = user.getPhotoUrl();

            txtNamaUser.setText(name);
            Glide.with(this).asBitmap().load(photoUrl)
                    .into(new BitmapImageViewTarget(imgProfileUser) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable rounded =
                                    RoundedBitmapDrawableFactory.create(ProfileActivity.this.getResources(), resource);
                            rounded.setCircular(true);
                            imgProfileUser.setImageDrawable(rounded);
                        }
                    });
        }

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

}
