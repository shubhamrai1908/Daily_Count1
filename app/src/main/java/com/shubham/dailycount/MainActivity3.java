package com.shubham.dailycount;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity3 extends AppCompatActivity {
    EditText ToDate, FromDate;
    Button SendButton;
    String FilterToDate, FilterFromDate;
    DailyDatabase dailyDatabase;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    public int milkPrice;
    public int waterPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        milkPrice = sharedpreferences.getInt("milk", 0);
        waterPrice = sharedpreferences.getInt("water", 0);
        ToDate = (EditText) findViewById(R.id.ToDate);
        FromDate = (EditText) findViewById(R.id.FromDate);
        SendButton = (Button) findViewById(R.id.SendDateBtn);
        dailyDatabase=new DailyDatabase(getApplicationContext());
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                monthOfYear++;
                FilterFromDate=dayOfMonth+"/"+monthOfYear+"/"+year;
                FromDate.setText(sdf.format(myCalendar.getTime()));

            }

        };
        FromDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity3.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                monthOfYear++;
                FilterToDate=dayOfMonth+"/"+monthOfYear+"/"+year;
                ToDate.setText(sdf.format(myCalendar.getTime()));
            }

        };
        ToDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity3.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        SendButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),FilterFromDate+"-"+FilterToDate,Toast.LENGTH_LONG).show();

                try {
                    Date d1=convertDate(FilterFromDate);
                    Date d2=convertDate(FilterToDate);
                   // quant.setText(d1.toString()+"-"+d2.toString());
                    getData(d1,d2);

                } catch (ParseException e) {
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                }


            }
        });




    }
    @SuppressLint("SetTextI18n")
    public void getData(Date d1,Date d2) throws ParseException {
            Cursor cursor1 = dailyDatabase.getAll("daily_milk");
            Cursor cursor2 = dailyDatabase.getAll("daily_water");

        List<DataMilk> list1= new ArrayList<>();
        List<DataWater> list2= new ArrayList<>();
        if (cursor1.moveToFirst()) {
            do {
                Date d=convertDate(cursor1.getString(0));
                double milkQ=Double.parseDouble(cursor1.getString(1));
                DataMilk dataM=new DataMilk(d,milkQ);
                if(dataM.date.compareTo(d1)>=0 && dataM.date.compareTo(d2)<=0)
                list1.add(dataM);
            } while (cursor1.moveToNext());

        }
          if (cursor2.moveToFirst()) {
            do {
                Date d=convertDate(cursor2.getString(0));
                double waterQ=Double.parseDouble(cursor2.getString(1));
                DataWater dataW=new DataWater(d,waterQ);
                if(dataW.date.compareTo(d1)>=0 && dataW.date.compareTo(d2)<=0)
                list2.add(dataW);

            } while (cursor2.moveToNext());

        }
          String quanM="Milk:";
          String quanW="Water:";
        if(list1.size()>0)
        {
            Collections.sort(list1,new Sort1());
            DataMilk[] data=list1.toArray(new DataMilk[list1.size()]);
            double milkQ=0;
            for(int i=0;i<data.length;i++)
            {
                milkQ+=data[i].milkQ;
            }
            milkQ*=milkPrice;
            quanM+=milkQ;


        }
        if(list2.size()>0)
        {
            Collections.sort(list2,new Sort2());
            DataWater[] data=list2.toArray(new DataWater[list2.size()]);
            double waterQ=0;
            for(int i=0;i<data.length;i++)
            {
                waterQ+=data[i].waterQ;
            }
            waterQ*=waterPrice;
            quanW+=waterQ;
        }
        showDialog(quanM,quanW);


    }
    public Date convertDate(String s) throws ParseException {
        Date date=new SimpleDateFormat("dd/MM/yyyy").parse(s);
        return date;
    }
    public void showDialog(String s1,String s2)
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity3.this);
        View mView = getLayoutInflater().inflate(R.layout.activity_bill,null);
        final TextView text1 = (TextView)mView.findViewById(R.id.text1);
        final TextView text2 = (TextView)mView.findViewById(R.id.text2);
        text1.setText(s1);
        text2.setText(s2);
        Button btn_close = (Button)mView.findViewById(R.id.button);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

}
class Sort1 implements Comparator<DataMilk>
{
    public int compare(DataMilk d1,DataMilk d2)
    {
        return d1.date.compareTo(d2.date);
    }
}
class Sort2 implements Comparator<DataWater>
{
    public int compare(DataWater d1,DataWater d2)
    {
        return d1.date.compareTo(d2.date);
    }
}