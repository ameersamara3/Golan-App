package com.example.golan;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.button.MaterialButton;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
public class Csv extends Fragment {
    private Csv csv;
    private View vi;
    private Button dateButton;
    private DatePickerDialog datePickerDialog;
    private Button dateButton2;
    private DatePickerDialog datePickerDialog2;

    public Csv() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_csv, container, false);
    }
    public void onStart() {
        super.onStart();
        csv=this;
        MainActivity ma=(MainActivity)getActivity();
        ma.setActionTitle("הורדת קובץ איקסל");
        initDatePicker();
        initDatePicker2();
        dateButton = (MaterialButton)(View) getView().findViewById(R.id.datepicker10);
        dateButton.setText(getTodaysDate(true));
        dateButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                datePickerDialog.show();


            }
        });
        dateButton2 = (MaterialButton)(View) getView().findViewById(R.id.datepicker11);
        dateButton2.setText(getTodaysDate(false));
        dateButton2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                datePickerDialog2.show();


            }
        });
        FireBase fb = new FireBase(getActivity());
        ActivityCompat.requestPermissions(getActivity(), new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        MaterialButton addbtn = (MaterialButton) (View) getView().findViewById(R.id.export);
        addbtn.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                fb.getAll(csv,dateButton.getText().toString(),dateButton2.getText().toString());
                vi=v;

            }
        });
    }

    private String getTodaysDate(boolean flag) {
        Calendar cal = Calendar.getInstance();

        int year=cal.get(Calendar.YEAR);
        if(!flag) year--;
        int month=cal.get(Calendar.MONTH);
        month++;
        int day=cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(year,month,day);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                String date=makeDateString(year,month,day);
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);
        int style= AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog= new DatePickerDialog(getActivity(),style,dateSetListener,year,month,day);


    }
    private void initDatePicker2() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                String date=makeDateString(year,month,day);
                dateButton2.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        year--;
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);
        int style= AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog2= new DatePickerDialog(getActivity(),style,dateSetListener,year,month,day);


    }
    private String makeDateString(int year, int month, int day) {
        return day+"/"+month+"/"+year;
    }
    public void exportCSV(List<String[]>list) throws IOException {
        File root = android.os.Environment.getExternalStorageDirectory();
        final String filename =root.getAbsolutePath() + "/download"+ "/" + "myData.csv";
        FileWriter fileWriter=new FileWriter(filename);
        CSVWriter write= new CSVWriter(fileWriter);
        write.writeAll(list);
        write.flush();
        //buttonShareFile(vi);
        Share(new File(filename));
    }
    private void Share(File savepath) {

        if (savepath != null) {

            Uri uri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", savepath);

            Intent i = new Intent(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"info.daliluk@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Excel File");
            //Log.d("URI@!@#!#!@##!", Uri.fromFile(pic).toString() + "   " + pic.exists());
            i.putExtra(Intent.EXTRA_STREAM, uri);
            i.setType("text/comma_separated_values/csv");
            getActivity().startActivity(Intent.createChooser(i, "Share the file ..."));

        }
    }
    public void buttonShareFile(View view){
        File root = android.os.Environment.getExternalStorageDirectory();
        final String filename =root.getAbsolutePath() + "/download"+ "/" + "myData.csv";
        File file = new File(filename);
        if (!file.exists()){
            Toast.makeText(getActivity(), "File doesn't exists", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/comma_separated_values/csv");
        intentShare.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file));
        startActivity(Intent.createChooser(intentShare, "Share the file ..."));
    }

    public void buttonShareText(View view){
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_SUBJECT,"My Subject Here ... ");
        intentShare.putExtra(Intent.EXTRA_TEXT,"My Text of the message goes here ... write anything what you want");

        startActivity(Intent.createChooser(intentShare, "Shared the text ..."));
    }
}
