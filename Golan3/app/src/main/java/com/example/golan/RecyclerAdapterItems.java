package com.example.golan;

import android.app.FragmentManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class RecyclerAdapterItems extends RecyclerView.Adapter<RecyclerAdapterItems.MyViewHolder> {


    private ArrayList<Item> itemList;
    private Context context;
    private FragmentManager fm;


    public RecyclerAdapterItems(ArrayList<Item> itemList, FragmentManager fm) {
        this.itemList = itemList;
        this.fm=fm;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView distance;
        private TextView nameTxt;
        private Section section;

        private MaterialButton seeitems;
        public  MyViewHolder(final View view){
            super(view);
            distance=view.findViewById(R.id.distance);
            nameTxt=view.findViewById(R.id.name);
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
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_format,parent,false);
        return new MyViewHolder(itemView);
    }


    @Override
    //setup all buttons and image
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name = itemList.get(position).getDate();
        name=name.replace("T"," ");
        name=name.substring(0,name.length()-7);
        String by = itemList.get(position).getBy();
        // Log.println(Log.DEBUG,context.getResources().getString(R.string.debug), id);
        holder.nameTxt.setText(name);
        holder.distance.setText(by);
        /*int price = itemList.get(position).getTotalprice();
        holder.priceTxt.setText(price+context.getResources().getString(R.string.shekel));
        LocalDateTime date = itemList.get(position).getDt();
        holder.dateTxt.setText(date.toString());
        holder.order=itemList.get(position);*/

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
