package com.twaddle.covid19.services;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.twaddle.covid19.model.PeriodicLocations;
import com.twaddle.covid19.network.RetrofitApiInterface;
import com.twaddle.covid19.ui.App;
import com.twaddle.covid19.utils.PrefManager;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LocationTrack extends Service {

    @Inject
    Retrofit retrofit;

    private static final String TAG = "LocationTrack";
    private static double latitude ;
    private static double longitude ;
    private boolean isPresentInHome = true;
    private double latitudeNow , longitudeNow;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequestForCurrent , mLocationRequestForHomeCheck;
    private LocationCallback mLocationCallbackForCurrent , mLocationCallbackForHomeCheck;
    private PrefManager prefManager;
    private FirebaseMessagingService firebaseMessagingService;
    private FirebaseAuth firebaseAuth;
    public LocationTrack() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        App.getApp().getDataComponent().inject( this );
        Log.i( TAG, "onCreate" );
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient( this );
        createLocationCallback();
        createLocationRequest();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i( TAG, "onStartCommand" );
        firebaseAuth = FirebaseAuth.getInstance();
        App.getApp().getDataComponent().inject( this );
        prefManager = new PrefManager( getApplicationContext() );
        firebaseMessagingService = new FirebaseMessagingService();
        alarmIt();
        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            stopSelf();
            return START_NOT_STICKY;
        }
        startPeriodicLocationUpdate();
        startPeriodicLocationUpdateForHomeCheck();
        checkIfPresentInHome();
        if(intent != null) {
            if (Objects.equals( intent.getAction(), "currentLocation" )) {
                startPeriodicLocationUpdate();
            }
            if (Objects.equals( intent.getAction(), "perodicHomeCheckStart" )) {
                startPeriodicLocationUpdateForHomeCheck();
            }
            if(Objects.equals( intent.getAction() , "checkIfPresentHome" )){
                checkIfPresentInHome();
            }
        }
        getLastKnownLocation();

        return START_STICKY;
    }

    private void checkIfPresentInHome() {
        if((latitudeNow <= prefManager.getHomeLatitude() + 1.00 || latitudeNow >= prefManager.getHomeLatitude() - 1.00) &&
                (longitudeNow <= prefManager.getHomeLongitude() + 1.00 || longitudeNow >= prefManager.getHomeLongitude() - 1.00)){
            Log.i( TAG, "checkIfPresentInHome: present in home" );
            prefManager.setPresentInHome( true );
            if(!isPresentInHome){
                firebaseMessagingService.sendNotification( "Please Wash Your Hands" );
            }
            isPresentInHome = true;
        }
        else {
            firebaseMessagingService.sendNotification( "Please be aware, outside" );
            prefManager.setPresentInHome( false );
            isPresentInHome = false;
        }
    }

    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener( new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location lastLocation = task.getResult();
                            Log.i( TAG, "getLastKnownLocation: " + lastLocation );
                        }
                    }
                } );
    }

    private void createLocationRequest() {
        mLocationRequestForCurrent = new LocationRequest()
                .setInterval( TimeUnit.SECONDS.toMillis( 10 ) )
                .setFastestInterval( TimeUnit.SECONDS.toMillis( 10 ) )
                .setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY );

        mLocationRequestForHomeCheck = new LocationRequest()
                .setInterval( TimeUnit.SECONDS.toMinutes( 10 ) )
                .setFastestInterval( TimeUnit.SECONDS.toMinutes( 10 ) )
                .setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY );
    }


    private void createLocationCallback() {
        mLocationCallbackForCurrent = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult( locationResult );
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    Log.i( TAG, "onLocationResult: lastknownlocation=" + location );
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
                Log.i( TAG, "onLocationResult: locationresult count=" + locationResult.getLocations().size() );
                for (Location location1 : locationResult.getLocations()) {
                    Log.i( TAG, "locationResult location " + location1.getLatitude() + " " + location1.getLongitude() + " " + location1.getAccuracy() + " " + location1.getProvider() );
                    latitude = location1.getLatitude();
                    longitude = location1.getLongitude();
                }
                Log.i( TAG, "locationResult stop" );
            }
        };

        mLocationCallbackForHomeCheck = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult( locationResult );
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    Log.i( TAG, "onLocationResult: lastknownlocation=" + location );
                    latitudeNow = location.getLatitude();
                    longitudeNow = location.getLongitude();
                }

                Log.i( TAG, "onLocationResult: locationresult count=" + locationResult.getLocations().size() );
                for (Location location1 : locationResult.getLocations()) {
                    Log.i( TAG, "locationResult location " + location1.getLatitude() + " " + location1.getLongitude() + " " + location1.getAccuracy() + " " + location1.getProvider() );
                    latitudeNow = location1.getLatitude();
                    longitudeNow = location1.getLongitude();
                }
                Log.i( TAG, "locationResult stop" );
                sendPeriodicCurrentLocation();
            }
        };

    }

    private void sendPeriodicCurrentLocation() {
        ArrayList<PeriodicLocations> ongoingJourneyCoordinatesArrayList = new ArrayList<>();
        ongoingJourneyCoordinatesArrayList.add( new PeriodicLocations( System.currentTimeMillis(), latitudeNow, longitudeNow ) );

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String JSONObject = gson.toJsonTree( ongoingJourneyCoordinatesArrayList ).toString();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty( "current_location", JSONObject );
        jsonObject1.addProperty( "uuid", firebaseAuth.getCurrentUser().getUid() );

        RetrofitApiInterface apiInterface = retrofit.create( RetrofitApiInterface.class );
        Call<Void> call = apiInterface.sendPeriodicCoordinates( jsonObject1 );
        call.enqueue( new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i( TAG, "onFailure: " + t );
            }
        } );
    }

    private void startPeriodicLocationUpdateForHomeCheck() {
        mFusedLocationClient.requestLocationUpdates( mLocationRequestForHomeCheck, mLocationCallbackForCurrent, Looper.myLooper() );
    }

    private void startPeriodicLocationUpdate() {
        Log.i( TAG, "periodic update" );
        mFusedLocationClient.requestLocationUpdates( mLocationRequestForCurrent, mLocationCallbackForHomeCheck, Looper.myLooper() );
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    private void alarmIt() {
        Intent intent = new Intent(this, LocationTrack.class);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);

        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);

        AlarmManager manager = (AlarmManager) getSystemService( Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60, pendingIntent);
    }

}

