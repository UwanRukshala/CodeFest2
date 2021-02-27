package com.codefestfinal.codefest21;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class Ticket extends Fragment {

    ArrayList<String> types;
    public Ticket() {
        // Required empty public constructor
    }

FirebaseFirestore db=FirebaseFirestore.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        types=new ArrayList<>();
        types.add("Information Request");
        types.add("Complain");
        types.add("Compliment");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ticket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinner = view.findViewById(R.id.t_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        View b = view.findViewById(R.id.button3);
        EditText ttitle = view.findViewById(R.id.t_title);
        EditText tbody = view.findViewById(R.id.t_body);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = ttitle.getText().toString();
                String body = tbody.getText().toString();
                String type = spinner.getSelectedItem().toString();

                com.codefestfinal.codefest21.Model.Ticket ticket=new com.codefestfinal.codefest21.Model.Ticket();
                ticket.setTitle(title);
                ticket.setBody(body);
                ticket.setType(type);


                db.collection("Tickets").add(ticket).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(), "Ticket Sent", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}