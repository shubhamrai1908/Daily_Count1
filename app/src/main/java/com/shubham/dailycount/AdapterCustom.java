package com.shubham.dailycount;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterCustom extends RecyclerView.Adapter<AdapterCustom.ViewHolder> {
    private ArrayList<DataSQL> arrayList;
    private Context context;
    DailyDatabase dailyDatabase;

    public AdapterCustom(ArrayList<DataSQL> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        dailyDatabase=new DailyDatabase(context);
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull AdapterCustom.ViewHolder holder, int position) {
        DataSQL dataSQL=arrayList.get(position);
        String date=dataSQL.date+"";
        holder.tv_date.setText(date.substring(0,10));
        holder.tv_milk.setText(dataSQL.milkQ);
        holder.tv_water.setText(dataSQL.waterQ);
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date1=dataSQL.date;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String strDate= formatter.format(date1);
                try {

                    dailyDatabase.delete(strDate);
                    Toast.makeText(context,"deleted data from "+strDate,Toast.LENGTH_LONG).show();


                }
                catch (Exception e)
                {
                    Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private TextView tv_milk,tv_water,tv_date;
        private ImageButton imageButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views
            tv_date= itemView.findViewById(R.id.tv_date);
            tv_milk = itemView.findViewById(R.id.tv_milk);
            tv_water = itemView.findViewById(R.id.tv_water);
            imageButton=itemView.findViewById(R.id.imagebtn);
        }
    }
}
