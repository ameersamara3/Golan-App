package com.example.golan;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

public class RecyclerAdapterAreas extends RecyclerView.Adapter<RecyclerAdapterAreas.MyViewHolder> {


    private ArrayList<Area> areaList;
    private Context context;
    private FragmentManager fm;


    public RecyclerAdapterAreas(ArrayList<Area> areaList, FragmentManager fm,Context context) {
        Collections.sort(areaList);
        this.areaList = areaList;
        this.fm=fm;
        this.context=context;

    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView distance;
        private TextView nameTxt;
        private ImageView waze;
        private View layout;

        private Area area;

        private MaterialButton seeitems;
        public  MyViewHolder(final View view){
            super(view);
            distance=view.findViewById(R.id.distance);
            nameTxt=view.findViewById(R.id.name);
            waze=view.findViewById(R.id.waze);
            layout=view.findViewById(R.id.bg);
            /*seeitems.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    //open requested edit page
                    Bundle bundle = new Bundle();
                    String id = area.getId();
                    bundle.putString(context.getResources().getString(R.string.id), id);
                    Boolean flag;
                    if(ChronoUnit.MINUTES.between(order.getDt(), LocalDateTime.now())>=5) flag=true;
                    else flag=false;
                    bundle.putBoolean(context.getResources().getString(R.string.flag), flag);
                    OrderItems fragment = new OrderItems();
                    fragment.setArguments(bundle);
                    FragmentTransaction t = fm.beginTransaction();
                    t.replace(R.id.root_layout2, fragment);
                    t.addToBackStack(null);
                    t.commit();

                }
            });*/
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_format,parent,false);
        return new MyViewHolder(itemView);
    }


    @Override
    //setup all buttons and image
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String name = areaList.get(position).getName();
       // Log.println(Log.DEBUG,context.getResources().getString(R.string.debug), id);
        holder.nameTxt.setText(name);
        holder.distance.setText(String.format("%.1f", areaList.get(position).getDistance())+" ק״מ");
        holder.waze.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "https://waze.com/ul?ll=%f,%f&z=10,", areaList.get(position).getLatitide(), areaList.get(position).getLongitude());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(intent);


            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String id = areaList.get(position).getName();
                MainActivity.area=areaList.get(position);
                bundle.putString("name", id);
                AreaItems fragment = new AreaItems();
                fragment.setArguments(bundle);
                FragmentTransaction t = fm.beginTransaction();
                t.replace(R.id.root_layout2, fragment);
                t.addToBackStack(null);
                t.commit();


            }
        });
        /*int price = areaList.get(position).getTotalprice();
        holder.priceTxt.setText(price+context.getResources().getString(R.string.shekel));
        LocalDateTime date = areaList.get(position).getDt();
        holder.dateTxt.setText(date.toString());
        holder.order=areaList.get(position);*/

    }

    @Override
    public int getItemCount() {
        return areaList.size();
    }
}
