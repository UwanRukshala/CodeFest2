package com.codefestfinal.codefest21.Model;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codefestfinal.codefest21.R;


public class Addholder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView desc;
    public TextView time;
    public TextView duration;
    public Button selectjob;
    public Add addid;
    public String adddocid;




    public Addholder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.job_title);
        desc = itemView.findViewById(R.id.job_desc);
        time = itemView.findViewById(R.id.time);
        duration = itemView.findViewById(R.id.duration);
        selectjob = itemView.findViewById(R.id.button2);
        selectjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(itemView.getContext(),"Job Accepted"+title.getText().toString(),Toast.LENGTH_SHORT).show();
//

            }
        });

    }
}
