package com.example.golan;


import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class AddItem extends Fragment {
private AddItem ad;

    public AddItem() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_item, container, false);
    }
    public void onStart() {
        ad=this;
        MainActivity ma=(MainActivity)getActivity();
        ma.setActionTitle("הוספת דיווח");
        String name = MainActivity.area.getName();
        super.onStart();
        FireBase fb = new FireBase(getActivity());
        TextView bug1 =(TextView)(View)getView().findViewById(R.id.bug1);
        Spinner spinner = (Spinner) (View)getView().findViewById(R.id.dpinner);
        TextView bug2 =(TextView) (View)getView().findViewById(R.id.bug2);
        bug1.setText("0");
        bug2.setText("0");
        TextView notes =(TextView) (View)getView().findViewById(R.id.notes);
        MaterialButton addbtn = (MaterialButton) (View) getView().findViewById(R.id.export);
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
        categories.add("מוניר");
        categories.add("שחאדי");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String b1 =bug1.getText().toString().trim();
                String b2 =bug2.getText().toString().trim();
                String n =notes.getText().toString().trim();
                fb.addItem(name,b1,b2,n,ad,spinner.getSelectedItem().toString());


            }
        });



    }

    public void back() {
        getActivity().onBackPressed();
    }
}

