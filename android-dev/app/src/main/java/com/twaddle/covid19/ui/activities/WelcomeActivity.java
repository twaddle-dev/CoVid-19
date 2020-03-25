package com.twaddle.covid19.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.twaddle.covid19.R;
import com.twaddle.covid19.utils.PrefManager;

public class WelcomeActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mSignInClient;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_welcome_user_login );
        Button btn_sign_in = findViewById( R.id.btn_google_sign );
        progressBar = findViewById( R.id.progress_circular_sign_in );
        progressBar.setVisibility( View.INVISIBLE );
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity( new Intent( this, MainActivity.class ) );
        } else {

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN )
                    .requestIdToken( getString( R.string.server_client_id_for_firebase_auth ) ).requestEmail().build();
            mSignInClient = GoogleSignIn.getClient( this, gso );

            btn_sign_in.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility( View.VISIBLE );
                    signInWithGoogle();
                }
            } );
        }
        changeStatusBarColor();
    }


    // get signinclient
    private void signInWithGoogle() {
        Intent signInIntent = mSignInClient.getSignInIntent();
        startActivityForResult( signInIntent, RC_SIGN_IN );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent( data );
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult( ApiException.class );
                firebaseAuthWithGoogle( account );
                PrefManager prefManager = new PrefManager( getApplicationContext() );
                prefManager.setFirstTimeLaunch( false );
                Toast.makeText( this, "Signed in", Toast.LENGTH_SHORT ).show();
                progressBar.setVisibility( View.INVISIBLE );
                startActivity( new Intent( this, UserInfoActivity.class ) );
            } catch (ApiException e) {
                progressBar.setVisibility( View.INVISIBLE );
                Toast.makeText( getApplicationContext(), "Sign in Failed", Toast.LENGTH_SHORT ).show();
            }
        }
    }

    // connect googlesignin to firebaseauth
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential( acct.getIdToken(), null );
        mAuth.signInWithCredential( credential ).addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText( getApplicationContext(), "Sign in Failed", Toast.LENGTH_SHORT ).show();
                }
            }
        } );
    }

    private void changeStatusBarColor() {
        Window window = getWindow();
        window.addFlags( WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS );
        window.setStatusBarColor( getColor( R.color.colorAccent ) );
    }
}
