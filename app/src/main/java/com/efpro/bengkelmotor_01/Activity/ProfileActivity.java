package com.efpro.bengkelmotor_01.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.efpro.bengkelmotor_01.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    Button btnLogOut, btnRegBengkel;
    TextView txtNamaUser;
    ImageView imgProfileUser;
    String uid, name;
    Uri photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtNamaUser     = (TextView) findViewById(R.id.txtNamaUser);
        imgProfileUser  = (ImageView) findViewById(R.id.imgProfileUser);
        btnLogOut       = (Button) findViewById(R.id.btnLogOut);
        btnRegBengkel   = (Button) findViewById(R.id.btnRegBengkel);
        btnLogOut.setOnClickListener(this);
        btnRegBengkel.setOnClickListener(this);

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



}
