package com.example.golan;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;


public class SectionHome extends Fragment {

    public SectionHome() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {


        super.onStart();
        MainActivity ma=(MainActivity)getActivity();
        ma.setActionTitle(MainActivity.section.getName());
        View update =(View)getView().findViewById(R.id.edit);
        View diseases =(View)getView().findViewById(R.id.diseases);
        View bugs =(View)getView().findViewById(R.id.bugs);
        View pest =(View)getView().findViewById(R.id.pest);
        update.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                Update fragment = new Update();
                FragmentTransaction t = fm.beginTransaction();
                t.replace(R.id.root_layout2, fragment);
                t.addToBackStack(null);
                t.commit();

            }
        });
        bugs.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                SectionItems fragment = new SectionItems();
                FragmentTransaction t = fm.beginTransaction();
                t.replace(R.id.root_layout2, fragment);
                t.addToBackStack(null);
                t.commit();

            }
        });



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_section_home, container, false);
    }
}