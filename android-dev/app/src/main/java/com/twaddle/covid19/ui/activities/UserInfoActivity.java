package com.twaddle.covid19.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.twaddle.covid19.R;
import com.twaddle.covid19.databinding.ActivityUserInfoBinding;
import com.twaddle.covid19.model.UserDetails;
import com.twaddle.covid19.network.RetrofitApiInterface;
import com.twaddle.covid19.services.LocationAddress;
import com.twaddle.covid19.services.LocationTrack;
import com.twaddle.covid19.ui.App;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;

public class UserInfoActivity extends AppCompatActivity {
    @Inject
    Retrofit retrofit;

    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final String TAG = "UserInfoActivity";
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private LocationTrack locationTrack;
    private FirebaseAuth firebaseAuth;
    private UserDetails userDetails;
    private double latitude,longitude;
    EditText et_address;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_user_info );
        firebaseAuth = FirebaseAuth.getInstance();
        App.getApp().getDataComponent().inject( this );
        if (!checkPermission()) {
            requestPermission();
        }
        final EditText et_name = findViewById( R.id.et_name );
        final EditText et_age = findViewById( R.id.et_age );
        et_age.setText( String.valueOf( 7 ) );
        et_address = findViewById( R.id.et_address );
        Button btn_next = findViewById( R.id.btn_user_info_next );
        btn_next.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showEnableLocationSetting( UserInfoActivity.this );
//
                userDetails = new UserDetails( firebaseAuth.getCurrentUser().getUid() ,
                        firebaseAuth.getCurrentUser().getEmail(),
                        et_name.getText().toString(),
                        Integer.parseInt( et_age.getText().toString()),
                        latitude,
                        longitude
                );
                createUserDetails();
                Intent intent = new Intent( UserInfoActivity.this, MainActivity.class );
                startActivity( intent );
            }
        } );

    }

    //turn on location gps
    private void showEnableLocationSetting(final Context context){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY );
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest( locationRequest );
        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient( context ).checkLocationSettings( builder.build() );
        task.addOnSuccessListener( new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                LocationSettingsStates states = locationSettingsResponse.getLocationSettingsStates();
                if(states.isLocationPresent()){
                    Log.i( TAG, "onSuccess: " );
                    startService( new Intent( UserInfoActivity.this, LocationTrack.class ) );
                    try {
                        Thread.sleep( 1000 );
                    } catch (InterruptedException e) {
                    }
                    getGPSLocation();
                }
            }
        } );

        task.addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof ResolvableApiException){
                    try{
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        resolvableApiException.startResolutionForResult( UserInfoActivity.this , REQUEST_CHECK_SETTINGS );
                    }catch (IntentSender.SendIntentException exception){}
                }
            }
        } );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            Log.i( TAG, "onActivityResult: " );
            startService( new Intent( UserInfoActivity.this, LocationTrack.class ) );
            try {
                Thread.sleep( 1000 );
            } catch (InterruptedException e) {
            }
            getGPSLocation();
        }
    }

    private void getGPSLocation() {
        locationTrack = new LocationTrack();
        if (locationTrack.getLatitude() != 0 && locationTrack.getLongitude() != 0) {
            Log.i( TAG, "run: non 0 location" );
        }
        else{
            Log.i( TAG, "getGPSLocation:  0 location" );
        }
//        final Handler handler = new Handler();
//        final Runnable r = new Runnable() {
//            public void run() {
//                Log.i( TAG, "run: " );
//                if (locationTrack.getLatitude() != 0 && locationTrack.getLongitude() != 0) {
//                    Log.i( TAG, "run: non 0 location" );
//                    LocationAddress.getAddressFromLocation( locationTrack.getLatitude() , locationTrack.getLongitude(),
//                            UserInfoActivity.this , new GeocoderHandler());
//                } else {
//                    locationTrack = new LocationTrack();
//                }
//                handler.postDelayed( this, 10000 );
//            }
//
//        };
//        handler.postDelayed( r, 10000 );

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission( getApplicationContext(), ACCESS_FINE_LOCATION );
        int result1 = ContextCompat.checkSelfPermission( getApplicationContext(), READ_CONTACTS );
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions( this, new String[]{ACCESS_FINE_LOCATION, READ_CONTACTS}, PERMISSION_REQUEST_CODE );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean contactAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            if (!locationAccepted && contactAccepted) {
                Toast.makeText( this, " Please grant permissions", Toast.LENGTH_LONG ).show();
                requestPermission();
            }
        }
    }

    private void createUserDetails(){
        Log.i( this.getClass().getSimpleName(), "getFollowers: " );
        RetrofitApiInterface apiInterface = retrofit.create( RetrofitApiInterface.class);
        Call<UserDetails> call = apiInterface.createUser(userDetails);
        call.enqueue( new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if(response.isSuccessful()){
                    Log.i( this.getClass().getSimpleName(), "onResponse: " + response );
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Log.i( TAG, "onFailure: " + t );
            }
        } );
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            Log.i( TAG, "handleMessage: location=" + locationAddress );
            et_address.setText( locationAddress );
        }
    }
}

