package com.codefestfinal.codefest21;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;
import android.view.View;


import com.codefestfinal.codefest21.Model.ProductModal;
import com.codefestfinal.codefest21.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        hideToolbar();
        mainBinding.fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager=getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack("");
                News news=new News();
                fragmentTransaction.replace(R.id.mainLayout,news,"News");
                fragmentTransaction.commit();
            }
        });
        mainBinding.tickt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager=getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack("");
                Ticket news=new Ticket();
                fragmentTransaction.replace(R.id.mainLayout,news,"Ticket");
                fragmentTransaction.commit();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.addToBackStack("");
        LogIn logIn = new LogIn();
        fragmentTransaction.replace(R.id.mainLayout, logIn, "LogIn");
        fragmentTransaction.commit();

        //loadProducts();

    }

    private void loadProducts() {
        ArrayList<ProductModal> l = new ArrayList<>();
        ProductModal product1 = new ProductModal("Burger", "Rs.600.00", "https://firebasestorage.googleapis.com/v0/b/codefestfinal.appspot.com/o/Products%2FBurger.png?alt=media&token=ddc18e39-1baa-4c9c-8992-5ced902985ce");
        ProductModal product2 = new ProductModal("Rice", "Rs.500.00", "https://firebasestorage.googleapis.com/v0/b/codefestfinal.appspot.com/o/Products%2FRice.png?alt=media&token=1542d8f5-c725-4129-aba7-055b8a62b483");
        ProductModal product3 = new ProductModal("Sandwich", "330.00", "https://firebasestorage.googleapis.com/v0/b/codefestfinal.appspot.com/o/Products%2FSandwich.png?alt=media&token=bdfe1020-0d16-48bd-9642-faee52710fff");
        ProductModal product4 = new ProductModal("Submarine", "Rs.550.00", "https://firebasestorage.googleapis.com/v0/b/codefestfinal.appspot.com/o/Products%2FSubmarine.png?alt=media&token=132807f9-ef3f-4a14-90eb-ee3496e40670");
        l.add(product1);
        l.add(product2);
        l.add(product3);
        l.add(product4);

        for (ProductModal productModal:l){
            db.collection("Products").add(productModal).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }

    void hideToolbar(){
        mainBinding.tickt.setVisibility(View.GONE);
        mainBinding.fb.setVisibility(View.GONE);

    }

    void showToolbar(){
        mainBinding.tickt.setVisibility(View.VISIBLE);


    }



}