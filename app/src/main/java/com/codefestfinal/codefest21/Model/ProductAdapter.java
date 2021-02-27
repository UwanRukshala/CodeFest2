package com.codefestfinal.codefest21.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.codefestfinal.codefest21.R;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<ProductModal> {


        // constructor for our list view adapter.
        public ProductAdapter(@NonNull Context context, ArrayList<ProductModal> productModalArrayList) {
            super(context, 0, productModalArrayList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


            View listitemView = convertView;
            if (listitemView == null) {
                listitemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
            }


            ProductModal item = getItem(position);

            // initializing our UI components of list view item.
            TextView nameTV = listitemView.findViewById(R.id.p_name);
            TextView priceTV = listitemView.findViewById(R.id.p_price);
            ImageView pImage = listitemView.findViewById(R.id.p_image);
            Button b=listitemView.findViewById(R.id.buy_button);

            nameTV.setText(item.getProductName());
            priceTV.setText(item.getPrice());

            Glide.with(pImage.getContext()).load(item.getImageUrl()).into(pImage);


            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(getContext(), "Item Bought : " + item.getProductName(), Toast.LENGTH_SHORT).show();
                }
            });
            return listitemView;
        }
    }


