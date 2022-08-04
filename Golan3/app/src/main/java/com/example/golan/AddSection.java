package com.example.golan;



import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.List;

public class AddSection extends Fragment {
    private LocationRequest locationRequest;
    private String nam;
    private ProgressDialog dialog;
    private Spinner spinner;


    public AddSection() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_section, container, false);
    }
    public void onStart() {
        super.onStart();
        spinner= (Spinner) getView().findViewById(R.id.dpinner3);
        setupspinner();
        MainActivity ma=(MainActivity)getActivity();
        ma.setActionTitle("הוספת חלקה");
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
    private void setupspinner() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });
        List<String> categories = new ArrayList<String>();//for spinner
        categories.add("מגאריק");
        categories.add("ברכת רם");
        categories.add("נמרה");
        categories.add("מרג");
        categories.add("קאטע");
        categories.add("רויסה");
        @SuppressLint("ResourceType") ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.drawable.spinner_item2, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }
    private void addSection(String nam, double longitude, double latitude) {
        FireBase fb=new FireBase(getActivity());
        fb.addSection(nam,longitude,latitude,spinner.getSelectedItem().toString(),this);

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
                                            addSection(nam, MainActivity.longitude, MainActivity.latitude);



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
    

