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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    public void addSection(String name, double longitude, double latitude,String area, AddSection add_section) {
        Map<String, Object> section = new HashMap<>();
        section.put("Area", area);
        section.put("longitide", longitude);
        section.put("latitude", latitude);
        db.collection("Sections").document(name).set(section)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(activity, "הצלחה", Toast.LENGTH_SHORT).show();
                        add_section.updateUI();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, "כישלון", Toast.LENGTH_SHORT).show();

                    }
                });

    }


    public void getSections(double currlongitude, double currlatitude, SectionsList av) {
        Task<QuerySnapshot> task = db.collection("Sections").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Section> sectionList = new ArrayList<Section>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> mp = document.getData();
                                double longitude=Double.parseDouble(String.valueOf(mp.get("longitide")));
                                double latitude=Double.parseDouble(String.valueOf(mp.get("latitude")));
                                double distance= Section.distance(latitude , longitude ,currlatitude,currlongitude);

                                sectionList.add(new Section(document.getId(),distance,longitude,latitude,(String)mp.get("Area")));
                            }
                            av.setupAdapter(sectionList);
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
        Task<QuerySnapshot> task = db.collection("Sections").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Section> sectionList = new ArrayList<Section>();
                        List<String[]> list= new ArrayList<>();
                        if (task.isSuccessful()) {
                            list.add(new String[]{"Section","Date","Apple Bug","Sea Bug","By","Notes"});
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
    public void addItem(String section, String b1, String b2, String n, AddItem cl, String by) {
        Map<String, Object> item = new HashMap<>();
        item.put("bugApple", b1);
        item.put("bugSea", b2);
        item.put("notes", n);
        item.put("by", by);
        String date= LocalDateTime.now().toString();
        item.put("Date", date);
        db.collection("Sections").document(section).collection("Items").document(date).set(item)
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
    public void getItems(String section, SectionItems av) {
        Task<QuerySnapshot> task = db.collection("Sections").document(section).collection("Items").get()
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
    public ArrayList<Item> getItems2(String section) {
        Task<QuerySnapshot> task = db.collection("Sections").document(section).collection("Items").orderBy("Date").get();
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

        Task task = db.collection("Sections").document(MainActivity.section.getName()).collection("Items").document(id).delete();
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
    public boolean deleteSection(String id) {

        Task task = db.collection("Sections").document(id).delete();
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
    public boolean editSection(Update up, Section section){
        DocumentReference ref = db.collection("Sections").document(section.getName());
        Map<String,Object> updates = new HashMap<>();
        updates.put("Phone Number", section.getPhoneNumber());
        Task task=ref.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        editArea(up,section);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, "כישלון", Toast.LENGTH_SHORT).show();

                    }
                });
        return task.isSuccessful();
    }
    public boolean editArea(Update up, Section section){
        DocumentReference ref = db.collection("Areas").document(section.getArea().getName());
        Map<String,Object> updates = new HashMap<>();
        if(section.getArea().getStartFlowering()!=null)
            updates.put("Start Flowering", section.getArea().getStartFlowering());
        if(section.getArea().getThirtyFlowering()!=null)
            updates.put("30% Flowering", section.getArea().getThirtyFlowering());
        if(section.getArea().getPeakFlowering()!=null)
            updates.put("Peak Flowering", section.getArea().getPeakFlowering());
        if(section.getArea().getBiofix()!=null)
            updates.put("Biofix", section.getArea().getBiofix());
        if(section.getArea().getDegree100()!=null)
            updates.put("100 degrees", section.getArea().getDegree100());
        if(section.getArea().getLeafHumidity()!=-432)//null
            updates.put("Leaf Humidity", section.getArea().getLeafHumidity());
        if(section.getArea().getRimpro()!=null)
            updates.put("Rimpro", section.getArea().getRimpro());
        Task task=ref.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(activity, "הצלחה", Toast.LENGTH_SHORT).show();
                        up.updateUI();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, "כישלון", Toast.LENGTH_SHORT).show();

                    }
                });
        return task.isSuccessful();
    }

    public void getSectionById(String name,Update up) {
        Task<DocumentSnapshot> task = db.collection("Sections").document(name).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Map<String, Object> mp = task.getResult().getData();
                        if(mp.get("Phone Number")!=null)
                            MainActivity.section.setPhoneNumber((String) mp.get("Phone Number"));
                        getAreaById((String) mp.get("Area"),up);
                    }}).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(activity, "כישלון", Toast.LENGTH_SHORT).show();
                    }
                });


    }
    public void getAreaById(String name,Update up) {
        Task<DocumentSnapshot> task = db.collection("Areas").document(name).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Map<String, Object> mp = task.getResult().getData();
                        if(mp.get("Start Flowering")!=null)
                            MainActivity.section.getArea().setStartFlowering((String) mp.get("Start Flowering"));
                        if(mp.get("30% Flowering")!=null)
                            MainActivity.section.getArea().setThirtyFlowering((String) mp.get("30% Flowering"));
                        if(mp.get("Peak Flowering")!=null)
                            MainActivity.section.getArea().setPeakFlowering((String) mp.get("Peak Flowering"));
                        if(mp.get("Biofix")!=null)
                            MainActivity.section.getArea().setBiofix((String) mp.get("Biofix"));
                        if(mp.get("100 degrees")!=null)
                            MainActivity.section.getArea().setDegree100((String) mp.get("100 degrees"));
                        if(mp.get("Leaf Humidity")!=null) {
                            Number n = (Number) mp.get("Leaf Humidity");
                            MainActivity.section.getArea().setLeafHumidity(n.intValue());
                        }
                        else
                            MainActivity.section.getArea().setLeafHumidity(-432);
                        if(mp.get("Rimpro")!=null)
                            MainActivity.section.getArea().setRimpro((String) mp.get("Rimpro"));
                        up.setTexts();
                    }}).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(activity, "כישלון", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}
