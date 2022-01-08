package com.shubham.dailycount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.text.CollationElementIterator;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity4 extends AppCompatActivity {
    ArrayList<DataSQL> dataSQLS;
    DailyDatabase dailyDatabase;
    AdapterCustom adapterCustom;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        recyclerView=findViewById(R.id.recycler);
        dataSQLS=new ArrayList<>();
        dailyDatabase=new DailyDatabase(getApplicationContext());
        try {
            dataSQLS=dailyDatabase.readDataSQL();
        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
        Collections.sort(dataSQLS,new Sort());
        adapterCustom=new AdapterCustom(dataSQLS,getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity4.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterCustom);
        adapterCustom.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(MainActivity4.this,MainActivity2.class);
        startActivity(intent);
    }
}
class Sort implements Comparator<DataSQL>
{
    public int compare(DataSQL d1,DataSQL d2)
    {
        return d1.date.compareTo(d2.date);
    }
}