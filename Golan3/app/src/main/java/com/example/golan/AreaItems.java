package com.example.golan;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import android.app.Fragment;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class AreaItems extends Fragment {
    private ArrayList<Item> itemList;
    private RecyclerView recyclerView;
    private FireBase fb;
    private RecyclerAdapterItems adapter;
    public AreaItems() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onStart() {
        super.onStart();
        MainActivity ma=(MainActivity)getActivity();
        ma.setActionTitle("דיווחים");
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        //FireBase db=new FireBase(getActivity());
        //getting which category is requested
        //itemList=db.getOrders(MainActivity.user.getUsername());
        FloatingActionButton fab= getView().findViewById(R.id.fab2);
        FloatingActionButton export= getView().findViewById(R.id.fab);
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                Csv fragment1 = new Csv();
                FragmentTransaction t = fm.beginTransaction();
                t.replace(R.id.root_layout2, fragment1);
                t.addToBackStack(null);
                t.commit();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                AddItem fragment = new AddItem();
                FragmentTransaction t = fm.beginTransaction();
                t.replace(R.id.root_layout2, fragment);
                t.addToBackStack(null);
                t.commit();
            }
        });
        fb = new FireBase(getActivity());
        fb.getItems(MainActivity.area.getName(),this);

    }

    public void setupAdapter(ArrayList<Item> itemsList2) {
        if (!itemsList2.isEmpty()) {
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    //making every items clickable to view it's page
                    Bundle bundle = new Bundle();
                    String id = itemsList2.get(position).getDate();
                    bundle.putString("name", id);
                    MainActivity.item=itemsList2.get(position);
                    FragmentManager fm = getFragmentManager();
                    itemPage fragment = new itemPage();
                    fragment.setArguments(bundle);
                    FragmentTransaction t = fm.beginTransaction();
                    t.replace(R.id.root_layout2, fragment);
                    t.addToBackStack(null);
                    t.commit();

                }

                @Override
                public void onLongClick(View view, int position) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("האם אתה בטוח שברצונך למחוק?");
                    builder.setTitle("מחיקה");
                    builder.setCancelable(false);
                    builder.setPositiveButton("בטל", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton("מחק", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if(fb.deleteItem(itemsList2.get(position).getDate())) {
                                //remove and refresh
                                itemsList2.remove(position);
                                adapter.notifyItemRemoved(position);

                            }
                            else{
                                Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT);

                            }

                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }
            }));
            //setting up adapter
            adapter = new RecyclerAdapterItems(itemsList2, getFragmentManager());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    ((LinearLayoutManager) layoutManager).getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);
        } else Toast.makeText(getActivity(), "אין דיווחים", Toast.LENGTH_LONG).show();
    }
}
