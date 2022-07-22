package com.example.golan;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;


public class itemPage extends Fragment {
private Item item;

    public itemPage() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {


        super.onStart();
        String id = this.getArguments().getString("name");
        item=MainActivity.item;
        MainActivity ma=(MainActivity)getActivity();
        ma.setActionTitle("דיווח");
        String date = item.getDate();
        date=date.replace("T"," ");
        date=date.substring(0,date.length()-7);
        TextView area =(TextView)(View)getView().findViewById(R.id.area);
        TextView bug1 =(TextView)(View)getView().findViewById(R.id.bug1);
        TextView bug2 =(TextView)(View)getView().findViewById(R.id.bug2);
        TextView notes =(TextView)(View)getView().findViewById(R.id.notes);
        TextView datee =(TextView)(View)getView().findViewById(R.id.date);
        area.setText(MainActivity.area.getName());
        bug1.setText(item.getBug1()+"");
        bug2.setText(item.getBug2()+"");
        datee.setText(date);
        if(!item.getNotes().equals(""))
        notes.setText(item.getNotes());
        else notes.setText("אין הערות");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_page, container, false);
    }
}