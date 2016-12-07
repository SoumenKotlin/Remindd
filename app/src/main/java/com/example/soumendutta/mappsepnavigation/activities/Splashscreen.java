package com.example.soumendutta.mappsepnavigation.activities;

/**
 * Created by soumendutta on 21/10/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.soumendutta.mappsepnavigation.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class Splashscreen extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks
{


   // CoordinatorLayout appbargone;
   private GoogleApiClient googleApiClient;

    final static int REQUEST_LOCATION = 199;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        getActionBar().hide();


       // this.requestWindowFeature(Window.FEATURE_NO_TITLE);




     /*   appbargone =(CoordinatorLayout) findViewById(appbargone);
        appbargone.setVisibility(View.GONE);*/


        Thread myThread = new Thread()
        {
            @Override
            public void run()
            {

                try
                {
                    final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        //  buildAlertMessageNoGps();

                        enableLoc();

                    }
                    else {
                        Thread.sleep(3000);
                        Intent intent = new Intent(getApplicationContext(), HomeMapActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();

                }
            }
        };
        myThread.start();



    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.e("Location error ", "" + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(
                                        (Activity) Splashscreen.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }


    }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            switch (requestCode) {
                case REQUEST_LOCATION:
                    switch (resultCode) {
                        case Activity.RESULT_CANCELED:
                            // The user was asked to change settings, but chose not to
                            finish();
                            break;
                        case Activity.RESULT_OK:
                            try {
                                Thread.sleep(3000);
                                Intent intent = new Intent(getApplicationContext(), HomeMapActivity.class);
                                startActivity(intent);
                                finish();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        default: {
                            break;
                        }
                    }
                    break;
            }

        }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}