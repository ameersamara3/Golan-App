package com.example.golan;



import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

public class Add_Area extends Fragment {
    private LocationRequest locationRequest;
    private String nam;
    private ProgressDialog dialog;


    public Add_Area() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_area, container, false);
    }
    public void onStart() {
        super.onStart();
        MainActivity ma=(MainActivity)getActivity();
        ma.setActionTitle("הוספת אזור");
        TextView name = (TextView) (View) getView().findViewById(R.id.name); 
        MaterialButton LocationButton = getView().findViewById(R.id.addbtn);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        LocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog= ProgressDialog.show(getActivity(),"loading..","please wait",true);
                nam=name.getText().toString().trim();
                if(nam.equals("")){
                        Toast.makeText(getActivity(), "Don't leave empty", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                }
                else {
                    if (MainActivity.longitude != -1 && MainActivity.latitude != -1) {
                        getCurrentLocation();
                    }
                }
            }
        });
    }

    private void addArea(String nam, double longitude, double latitude) {
        FireBase fb=new FireBase(getActivity());
        fb.addArea(nam,longitude,latitude,this);

    }
    public void updateUI() {
        getActivity().onBackPressed();
        dialog.dismiss();
    }

    @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            if (requestCode == 1){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    if (isGPSEnabled()) {

                        getCurrentLocation();

                    }else {

                        turnOnGPS();
                        dialog.dismiss();

                    }
                }
            }


        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 2) {
                if (resultCode == Activity.RESULT_OK) {

                    getCurrentLocation();
                }
            }
        }

        private void getCurrentLocation() {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    if (isGPSEnabled()) {

                        LocationServices.getFusedLocationProviderClient(getActivity())
                                .requestLocationUpdates(locationRequest, new LocationCallback() {
                                    @Override
                                    public void onLocationResult(@NonNull LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        LocationServices.getFusedLocationProviderClient(getActivity())
                                                .removeLocationUpdates(this);

                                        if (locationResult != null && locationResult.getLocations().size() >0){

                                            int index = locationResult.getLocations().size() - 1;
                                            MainActivity.latitude = locationResult.getLocations().get(index).getLatitude();
                                            MainActivity.longitude = locationResult.getLocations().get(index).getLongitude();
                                            addArea(nam, MainActivity.longitude, MainActivity.latitude);



                                        }
                                    }
                                }, Looper.getMainLooper());

                    } else {
                        turnOnGPS();
                        dialog.dismiss();
                    }

                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    dialog.dismiss();
                }
            }
        }

        private void turnOnGPS() {



            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);

            Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getActivity())
                    .checkLocationSettings(builder.build());

            result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                @Override
                public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);
                        Toast.makeText(getActivity(), "GPS is already tured on", Toast.LENGTH_SHORT).show();

                    } catch (ApiException e) {

                        switch (e.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                try {
                                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                    resolvableApiException.startResolutionForResult(getActivity(), 2);
                                } catch (IntentSender.SendIntentException ex) {
                                    ex.printStackTrace();
                                }
                                break;

                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                //Device does not have location
                                break;
                        }
                    }
                }
            });

        }

        private boolean isGPSEnabled() {
            LocationManager locationManager = null;
            boolean isEnabled = false;

            if (locationManager == null) {
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            }

            isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            return isEnabled;

        }

    }
    

