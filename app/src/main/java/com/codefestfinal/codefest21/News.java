package com.codefestfinal.codefest21;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codefestfinal.codefest21.Model.Add;
import com.codefestfinal.codefest21.Model.Addholder;
import com.codefestfinal.codefest21.directionsLib.FetchURL;
import com.codefestfinal.codefest21.directionsLib.mapDistanceObj;
import com.codefestfinal.codefest21.directionsLib.mapTimeObj;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;


public class News extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter firestoreRecyclerAdapter;
    private GoogleMap currengoogleMap;
    LatLng currentLoaction;
    LatLng dragtlocation;
    ProgressDialog progressDialog;


    public News() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setContentView(R.layout.fragment_news_map);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Query loadnews = db.collection("Add");

        FirestoreRecyclerOptions firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Add>().setQuery(loadnews,Add.class).build();
        //adpater set

        firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<Add, Addholder>(firestoreRecyclerOptions) {

            @NonNull
            @Override
            public Addholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.additem,parent,false);
                return new Addholder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull Addholder holder, int position, @NonNull Add model) {
                holder.title.setText(model.getTitle());
                holder.desc.setText(model.getBody());


//                holder.title.setText("Job From :"+model.getCustomername() +"" +model.getTpnumber());
//                holder.desc.setText("Desc :"+model.getEndloaction_lat());
//                holder.cost.setText(model.getEstimateprice()+"");
//                holder.duration.setText(model.getDuration());


                holder.adddocid = getSnapshots().getSnapshot(position).getId();
                holder.addid = model;
                //holder.addid.setRiderdocid(id);
            }
        };
        recyclerView.setAdapter(firestoreRecyclerAdapter);
    }


}