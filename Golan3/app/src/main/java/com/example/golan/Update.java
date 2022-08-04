package com.example.golan;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Update extends Fragment {
    private View vi;

    private HashMap<String,Button> mpBtn;
    private Map<String,DatePickerDialog> mpPick;
    private String[] strings;
    private TextView humidity;
    private TextView phoneNumber;
    private TextView titleA;
    private TextView titleS;
    private Spinner spinner;

    public Update() {
        mpBtn= new HashMap<>();
        mpPick= new HashMap<>();

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update, container, false);
    }

    public void onStart() {
        super.onStart();
        MainActivity ma = (MainActivity) getActivity();
        ma.setActionTitle("עדכון נתונים");
        spinner = (Spinner) (View) getView().findViewById(R.id.dpinner2);
        setupspinner();
        strings= new String[]{"start flowering", "30% flowering", "peak flowering", "biofix", "100 degrees"};
        Button dateButton;
        titleA = (TextView) (View) getView().findViewById(R.id.titleArea);
        titleS = (TextView) (View) getView().findViewById(R.id.titleSection);
        titleS.setText("פרטי חלקת ה"+MainActivity.section.getName());
        titleA.setText("פרטי אזור ה"+MainActivity.section.getArea().getName());
        humidity = (TextView) (View) getView().findViewById(R.id.leaf);
        phoneNumber = (TextView) (View) getView().findViewById(R.id.phone);
        dateButton = (MaterialButton) (View) getView().findViewById(R.id.datepicker2);
        mpBtn.put(strings[0],dateButton);
        dateButton = (MaterialButton) (View) getView().findViewById(R.id.datepicker3);
        mpBtn.put(strings[1],dateButton);
        dateButton = (MaterialButton) (View) getView().findViewById(R.id.datepicker4);
        mpBtn.put(strings[2],dateButton);
        dateButton = (MaterialButton) (View) getView().findViewById(R.id.datepicker5);
        mpBtn.put(strings[3],dateButton);
        dateButton = (MaterialButton) (View) getView().findViewById(R.id.datepicker6);
        mpBtn.put(strings[4],dateButton);
        for (String s : strings)
        {

            initDatePicker(s);
            mpBtn.get(s).setText("dd/mm/yyyy");
            mpBtn.get(s).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    mpPick.get(s).show();


                }
            });
            FireBase fb=new FireBase(getActivity());
            fb.getSectionById(MainActivity.section.getName(),this);
        }



        FireBase fb = new FireBase(getActivity());
        MaterialButton updateBtn = (MaterialButton) (View) getView().findViewById(R.id.update);
        updateBtn.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                updateFireBase();
            }
        });
    }
    public void setTexts(){
        Section section =MainActivity.section;
        if(section.getArea().getStartFlowering()!=null)
            mpBtn.get(strings[0]).setText(section.getArea().getStartFlowering());
        if(section.getArea().getThirtyFlowering()!=null)
            mpBtn.get(strings[1]).setText(section.getArea().getThirtyFlowering());
        if(section.getArea().getPeakFlowering()!=null)
            mpBtn.get(strings[2]).setText(section.getArea().getPeakFlowering());
        if(section.getArea().getBiofix()!=null)
            mpBtn.get(strings[3]).setText(section.getArea().getBiofix());
        if(section.getArea().getDegree100()!=null)
            mpBtn.get(strings[4]).setText(section.getArea().getDegree100());
        if(section.getArea().getLeafHumidity()!=-432)
            humidity.setText(section.getArea().getLeafHumidity()+"");
        if(section.getPhoneNumber()!=null)
            phoneNumber.setText(section.getPhoneNumber());
        if(section.getArea().getRimpro()!=null) {
            if(section.getArea().getRimpro().equals("חיובי"))
                spinner.setSelection(1);
            if(section.getArea().getRimpro().equals("שלילי"))
                spinner.setSelection(0);
        }

        /*checker=mpBtn.get(strings[0]).getText().toString();
        if(!checker.equals("dd/mm/yyyy"))
            MainActivity.area.setThirtyFlowering(checker);
        checker=mpBtn.get(strings[1]).getText().toString();
        if(!checker.equals("dd/mm/yyyy"))
            MainActivity.area.setPeakFlowering(checker);
        checker=mpBtn.get(strings[2]).getText().toString();
        if(!checker.equals("dd/mm/yyyy"))
            MainActivity.area.setBiofix(checker);
        checker=mpBtn.get(strings[3]).getText().toString();
        if(!checker.equals("dd/mm/yyyy"))
            MainActivity.area.setBiofix(checker);
        checker=mpBtn.get(strings[4]).getText().toString();
        if(!checker.equals("dd/mm/yyyy"))
            MainActivity.area.setDegree100(checker);
        checker=phoneNumber.getText().toString();
        if(checker.length()!=0){
            if(checker.length()==10){
                MainActivity.area.setPhoneNumber(checker);
            }else
                Toast.makeText(getActivity(),"מספר הטלפון צריך להיות 10 מספרים",Toast.LENGTH_SHORT);
        }
        MainActivity.area.setRimpro(spinner.getSelectedItem().toString());
        checker=humidity.getText().toString();
        if(checker.length()!=0)
            MainActivity.area.setLeafHumidity(Integer.parseInt(checker));
        FireBase fb=new FireBase(getActivity());
        fb.editSection(this,MainActivity.area);*/
    }
    private void updateFireBase(){
        String checker;
        checker=mpBtn.get(strings[0]).getText().toString();
        if(!checker.equals("dd/mm/yyyy"))
            MainActivity.section.getArea().setStartFlowering(checker);
        checker=mpBtn.get(strings[1]).getText().toString();
        if(!checker.equals("dd/mm/yyyy"))
            MainActivity.section.getArea().setThirtyFlowering(checker);
        checker=mpBtn.get(strings[2]).getText().toString();
        if(!checker.equals("dd/mm/yyyy"))
            MainActivity.section.getArea().setPeakFlowering(checker);
        checker=mpBtn.get(strings[3]).getText().toString();
        if(!checker.equals("dd/mm/yyyy"))
            MainActivity.section.getArea().setBiofix(checker);
        checker=mpBtn.get(strings[4]).getText().toString();
        if(!checker.equals("dd/mm/yyyy"))
            MainActivity.section.getArea().setDegree100(checker);
        checker=phoneNumber.getText().toString();
        if(checker.length()!=0){
            if(checker.length()==10){
                MainActivity.section.setPhoneNumber(checker);
            }else
                Toast.makeText(getActivity(),"מספר הטלפון צריך להיות 10 מספרים",Toast.LENGTH_SHORT);
        }
        MainActivity.section.getArea().setRimpro(spinner.getSelectedItem().toString());
        checker=humidity.getText().toString();
        if(checker.length()!=0)
            MainActivity.section.getArea().setLeafHumidity(Integer.parseInt(checker));
        FireBase fb=new FireBase(getActivity());
        fb.editSection(this,MainActivity.section);
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
        categories.add("לא ידוע");
        categories.add("חיובי");
        categories.add("שלילי");
        @SuppressLint("ResourceType") ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.drawable.spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }


    private void initDatePicker(String s) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                String date = makeDateString(year, month, day);
                mpBtn.get(s).setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), style, dateSetListener, year, month, day);
        mpPick.put(s,datePickerDialog);



    }


    private String makeDateString(int year, int month, int day) {
        return day + "/" + month + "/" + year;
    }

    public void updateUI() {
        getActivity().onBackPressed();
    }
}