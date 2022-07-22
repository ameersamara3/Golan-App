package com.example.golan;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireBase {
    private Activity activity;
    private FirebaseFirestore db;


    public FireBase(Activity activity) {
        this.db = FirebaseFirestore.getInstance();
        this.activity = activity;
    }
    public void addArea(String name, double longitude, double latitude, Add_Area add_area) {
        Map<String, Object> area = new HashMap<>();
        area.put("longitide", longitude);
        area.put("latitude", latitude);
        db.collection("Areas").document(name).set(area)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(activity, "הצלחה", Toast.LENGTH_SHORT).show();
                        add_area.updateUI();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, "כישלון", Toast.LENGTH_SHORT).show();

                    }
                });

    }
    public void getAreas(double currlongitude, double currlatitude,AreasView av) {
        Task<QuerySnapshot> task = db.collection("Areas").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Area> areaList = new ArrayList<Area>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> mp = document.getData();
                                double longitude=Double.parseDouble(String.valueOf(mp.get("longitide")));
                                double latitude=Double.parseDouble(String.valueOf(mp.get("latitude")));
                                double distance=Area.distance(latitude , longitude ,currlatitude,currlongitude);

                                areaList.add(new Area(document.getId(),distance,longitude,latitude));
                            }
                            av.setupAdapter(areaList);
                        } else   Toast.makeText(activity, "כישלון", Toast.LENGTH_SHORT).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(activity, "הצלחה", Toast.LENGTH_SHORT).show();

                    }
                });
    }
    public void getAll(Csv csv,String dateB, String dateS) {
        Task<QuerySnapshot> task = db.collection("Areas").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Area> areaList = new ArrayList<Area>();
                        List<String[]> list= new ArrayList<>();
                        if (task.isSuccessful()) {
                            list.add(new String[]{"Area","Date","Apple Bug","Sea Bug","By","Notes"});
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                for(Item item: getItems2(document.getId())){
                                    if(between(item.getDate(),dateB,dateS))
                                    list.add(new String[]{document.getId() + "",item.getDate(),item.getBug1()+"",item.getBug2()+"",item.getBy(),item.getNotes()});
                                }
                            }
                            try {
                                csv.exportCSV(list);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else   Toast.makeText(activity, "כישלון", Toast.LENGTH_SHORT).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(activity, "הצלחה", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private boolean between(String date, String dateB, String dateS) {
        String[] b=dateB.split("/");
        String[] s=dateS.split("/");
        int year=Integer.parseInt(date.substring(0,4));
        int year1=Integer.parseInt(b[2]);
        int year2=Integer.parseInt(s[2]);
        int month=Integer.parseInt(date.substring(5,7));
        int month1=Integer.parseInt(b[1]);
        int month2=Integer.parseInt(s[1]);
        int day=Integer.parseInt(date.substring(8,10));
        int day1=Integer.parseInt(b[0]);
        int day2=Integer.parseInt(s[0]);
        int dat= (year)*10000+month*100+day;
        int dat1= (year1)*10000+month1*100+day1;
        int dat2= (year2)*10000+month2*100+day2;
        Log.e("t",dat+" "+dat1+" "+dat2);
        if(dat>=dat2 && dat<=dat1) return true;
        return false;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addItem(String area, String b1, String b2, String n, AddItem cl, String by) {
        Map<String, Object> item = new HashMap<>();
        item.put("bugApple", b1);
        item.put("bugSea", b2);
        item.put("notes", n);
        item.put("by", by);
        String date= LocalDateTime.now().toString();
        item.put("Date", date);
        db.collection("Areas").document(area).collection("Items").document(date).set(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(activity, "הצלחה", Toast.LENGTH_SHORT).show();
                        cl.back();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, "כישלון", Toast.LENGTH_SHORT).show();

                    }
                });
    }
    public void getItems(String area,AreaItems av) {
        Task<QuerySnapshot> task = db.collection("Areas").document(area).collection("Items").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Item> itemsList = new ArrayList<Item>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> mp = document.getData();
                                int bug1=Integer.parseInt(String.valueOf(mp.get("bugApple")));
                                int bug2=Integer.parseInt(String.valueOf(mp.get("bugSea")));
                                itemsList.add(new Item(document.getId(),bug1,bug2,String.valueOf(mp.get("notes")),String.valueOf(mp.get("by"))));
                            }
                            Collections.sort(itemsList);
                            av.setupAdapter(itemsList);
                        } else   Toast.makeText(activity, "כישלון", Toast.LENGTH_SHORT).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(activity, "הצלחה", Toast.LENGTH_SHORT).show();

                    }
                });
    }
    public ArrayList<Item> getItems2(String area) {
        Task<QuerySnapshot> task = db.collection("Areas").document(area).collection("Items").orderBy("Date").get();
        int c=0;
        while(!task.isSuccessful() && !task.isComplete() && !task.isCanceled()){
            try {
                Thread.sleep(100);
                c++;
                if(c>100){
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ArrayList<Item> itemsList = new ArrayList<Item>();
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Map<String, Object> mp = document.getData();
                int bug1 = Integer.parseInt(String.valueOf(mp.get("bugApple")));
                int bug2 = Integer.parseInt(String.valueOf(mp.get("bugSea")));
                itemsList.add(new Item(document.getId(), bug1, bug2, String.valueOf(mp.get("notes")), String.valueOf(mp.get("by"))));
            }
        }
        return itemsList;
    }
    public boolean deleteItem(String id) {

        Task task = db.collection("Areas").document(MainActivity.area.getName()).collection("Items").document(id).delete();
        int c=0;
        while(!task.isSuccessful() && !task.isComplete() && !task.isCanceled()){
            try {
                Thread.sleep(100);
                c++;
                if(c>100){
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return task.isSuccessful();
    }
    public boolean deleteArea(String id) {

        Task task = db.collection("Areas").document(id).delete();
        int c=0;
        while(!task.isSuccessful() && !task.isComplete() && !task.isCanceled()){
            try {
                Thread.sleep(100);
                c++;
                if(c>100){
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return task.isSuccessful();
    }
}
