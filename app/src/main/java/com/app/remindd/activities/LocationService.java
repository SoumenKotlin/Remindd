package com.app.remindd.activities;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.app.remindd.database.Config;
import com.app.remindd.database.Longtap;
import com.app.remindd.database.MyDatabase;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.Circle;

import java.util.ArrayList;
import java.util.Locale;


public class LocationService extends Service implements LocationListener, TextToSpeech.OnInitListener {

    LocationManager locationManager;
    Location location;

    double destination_lat; // latitude
    double destination_lng; // longitude

    double current_lat;
    double current_lng;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    Location mCurrentLocation;

    double radius;
    double distance_out = 0;
    MyDatabase database;
    ArrayList<Longtap> arrayList;

    private TextToSpeech tts;

    private Circle mCircle;

   // int value = 0;
    final static String MY_ACTION = "MY_ACTION";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        database = new MyDatabase(getApplicationContext());
        tts = new TextToSpeech(this, this);


        arrayList = database.getLoginData();


        Log.e("Service", "Service started");
       /* for (Longtap values : arrayList) {
            String db_startDate = values.getFdate();
            String db_endDate = values.getTdate();

            if(Utils.getDateTime().compareTo(db_startDate) >= 0) {
                Toast.makeText(this, "ABC",  Toast.LENGTH_LONG).show();
            }
            else if(Utils.getDateTime().compareTo(db_endDate) <= 0){
                Toast.makeText(this, "XYZ",  Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "expired",  Toast.LENGTH_LONG).show();
            }
        }*/

        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria locationCritera = new Criteria();
        locationCritera.setAccuracy(Criteria.ACCURACY_FINE);
        locationCritera.setAltitudeRequired(false);
        locationCritera.setBearingRequired(false);
        locationCritera.setCostAllowed(true);
        locationCritera.setPowerRequirement(Criteria.NO_REQUIREMENT);

        String provider = locationManager.getBestProvider(locationCritera, true);

        Location gpsLoc = locationManager.getLastKnownLocation("gps");
        Location networkLoc = locationManager.getLastKnownLocation("network");

        if (gpsLoc != null) {
            Log.e("Gps", "Gps is null");
            locationManager.requestLocationUpdates("gps", 0, 1, this);

        }
        else if (networkLoc != null) {
                Log.e("Network", "Network is null");
                locationManager.requestLocationUpdates("network", 0, 1, this);
            }


        if (gpsLoc == null && networkLoc == null) {
            Toast.makeText(LocationService.this, "Please make sure you have active internet connection or gps", Toast.LENGTH_SHORT).show();
        }

        return Service.START_STICKY;
    }


    @Override
    public void onLocationChanged(Location location) {

        current_lat = location.getLatitude();
        current_lng = location.getLongitude();

        arrayList = database.getLoginData();

        for (Longtap values : arrayList) {
            destination_lat = Double.parseDouble(values.getLat());
            destination_lng = Double.parseDouble(values.getLog());

            String units = values.getUnits();
            if (units.contains("Miles")) {
                radius = Double.parseDouble(values.getRedious());
                radius = Utils.milesToMeter(radius);

            } else if (units.contains("Kilometer")) {
                radius = Double.parseDouble(values.getRedious());
                radius = Utils.kmToMeter(radius);

            } else {
                radius = Double.parseDouble(values.getRedious());

            }

            String playPauseStatus = values.getPlayPauseStatus();
            Location startPoint = new Location("Source");
            startPoint.setLatitude(current_lat);
            startPoint.setLongitude(current_lng);

            Location endPoint = new Location("Destination");
            endPoint.setLatitude(destination_lat);
            endPoint.setLongitude(destination_lng);
            String db_startDate = values.getFdate();
            String db_endDate = values.getTdate();

            String toggle_status = values.getTogglebutton();

            //double distance2 =startPoint.distanceTo(endPoint);
            double distance = 1000 * CalculationByDistance(current_lat, current_lng, destination_lat, destination_lng);
            Log.e("Distance: ", "" + distance);



            if (distance <= radius && playPauseStatus.equals("play") && Utils.getDateTime().compareTo(db_startDate) >= 0 &&
                    Utils.getDateTime().compareTo(db_endDate) <= 0) {

                Log.e("radius: ", "" + radius);

                distance_out = distance;
                if (toggle_status.equals("Coming In")) {

                    if (Config.value == 1) {
                        tts.stop();
                    } else {
                        Config.value = 1;
                        tts.speak("You are near to " + values.getName() + values.getDec(), TextToSpeech.QUEUE_FLUSH, null);

                        if (MyApplication.isActivityVisible()) {
                            Intent intent = new Intent();
                            intent.setAction(MY_ACTION);
                            intent.putExtra("name1", values.getName());
                            intent.putExtra("dec1", values.getDec());
                            intent.putExtra("lat1", values.getLat());
                            sendBroadcast(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), HomeMapActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("name", values.getName());
                            intent.putExtra("dec", values.getDec());
                            intent.putExtra("lat", values.getLat());
                            getApplicationContext().startActivity(intent);

                        }


                       // Toast.makeText(getApplicationContext(), "Within the coverage: " + distance, Toast.LENGTH_LONG).show();
                    }

                }
            }


            if (distance_out != 0 && distance >= radius && playPauseStatus.equals("play") && Utils.getDateTime().compareTo(db_startDate) >= 0 &&
                    Utils.getDateTime().compareTo(db_endDate) <= 0) {


                if (toggle_status.equals("Going Out")) {
                    if (Config.value == 1) {
                        tts.stop();
                    } else {

                        Config.value = 1;
                        tts.speak("You are near to " + values.getName() + values.getDec(), TextToSpeech.QUEUE_FLUSH, null);

                        if (MyApplication.isActivityVisible()) {
                            Intent intent = new Intent();
                            intent.setAction(MY_ACTION);
                            intent.putExtra("name1", values.getName());
                            intent.putExtra("dec1", values.getDec());
                            intent.putExtra("lat1", values.getLat());
                            sendBroadcast(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), HomeMapActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("name", values.getName());
                            intent.putExtra("dec", values.getDec());
                            intent.putExtra("lat", values.getLat());
                            getApplicationContext().startActivity(intent);

                         /*   Intent intent1 = new Intent();
                            intent1.setAction(MY_ACTION);

                            sendBroadcast(intent1);*/

                        }


                       // Toast.makeText(getApplicationContext(), "Going out from the coverage: " + distance, Toast.LENGTH_LONG).show();
                    }

                }
            }


        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public double CalculationByDistance(double initialLat, double initialLong,
                                        double finalLat, double finalLong) {
        int R = 6371; // km (Earth radius)
        double dLat = toRadians(finalLat - initialLat);
        double dLon = toRadians(finalLong - initialLong);
        initialLat = toRadians(initialLat);
        finalLat = toRadians(finalLat);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(initialLat) * Math.cos(finalLat);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public double toRadians(double deg) {
        return deg * (Math.PI / 180);
    }


    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }

        } else {
            Log.e("TTS", "Initialization Failed!");
        }

    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }


}