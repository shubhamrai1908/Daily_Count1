package com.shubham.dailycount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity2 extends AppCompatActivity {
    CalendarView calendarView;
    EditText editText1;
    EditText editText2;
    TextView textView;
    Button button1,button2,calc,price,cancelA,read;
    DailyDatabase dailyDatabase;
    String date;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        createNotificationChannel();
        setAlarm();

        cancelA=findViewById(R.id.cancelAlarm);
        read=findViewById(R.id.read);


        calendarView=findViewById(R.id.calendarView);
        editText1=findViewById(R.id.editText1);
        editText2=findViewById(R.id.editText2);
        editText2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        //textView=findViewById(R.id.textView1);
        button1=findViewById(R.id.btn1);
        button2=findViewById(R.id.btn2);
        calc=findViewById(R.id.calc);
        price=findViewById(R.id.price);
        cancelA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });
        dailyDatabase=new DailyDatabase(getApplicationContext());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        date= sdf.format(new Date(calendarView.getDate()));
        try {
            start();
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }



        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                editText1.setText("");
                editText2.setText("");
                String dd,mm,yyyy;
                month++;
                dd=dayOfMonth+"";
                mm=month+"";
                yyyy=year+"";
                if(dayOfMonth/10==0)
                    dd="0"+dayOfMonth;
                if(month/10==0)
                    mm="0"+month;

                date=dd+"/"+mm+"/"+yyyy;
                start();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String milk=editText1.getText().toString().trim();
                 double milkQ=Double.parseDouble(milk);

                try {
                       dailyDatabase.fillDailyMilk(date,milkQ);
                    Toast.makeText(getApplicationContext(),date+"-DataMilk Saved Successfully",Toast.LENGTH_LONG).show();
                }
                catch(Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                }


            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String water=editText2.getText().toString().trim();
                double waterQ=Double.parseDouble(water);

                try {
                    dailyDatabase.fillDailyWater(date,waterQ);
                    Toast.makeText(getApplicationContext(),date+"-DataWater Saved Successfully",Toast.LENGTH_SHORT).show();
                }
                catch(Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                }


            }
        });
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity2.this,MainActivity3.class);
                startActivity(intent);
            }
        });
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity2.this,MainActivity4.class);
                startActivity(intent);
            }
        });
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity2.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            String name="dailycountChannel";
            String description = "Channel for Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel=new NotificationChannel("dailycount",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager= getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void start()
    {

        try {
            Cursor cursor1 = dailyDatabase.getAll("daily_milk");
            Cursor cursor2 = dailyDatabase.getAll("daily_water");
            boolean flag1=false;
            boolean flag2=false;
            if (cursor1.moveToFirst()) {
                do {
                    if(cursor1.getString(0).equals(date))
                    {
                        flag1=true;
                          editText1.setText(cursor1.getString(1));
                        break;
                    }
                } while (cursor1.moveToNext());

            }
            if (cursor2.moveToFirst()) {
                do {
                    if(cursor2.getString(0).equals(date))
                    {
                        flag2=true;
                        editText2.setText(cursor2.getString(1));
                        break;
                    }
                } while (cursor2.moveToNext());

            }


        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }


    }
    public void setAlarm()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(this,AlarmReceiver.class);
        pendingIntent=PendingIntent.getBroadcast(this,0,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_HOUR*2,pendingIntent);
        //Toast.makeText(getApplicationContext(),"Notification turned on",Toast.LENGTH_SHORT).show();

    }
    public void cancelAlarm()
    {
        Intent intent=new Intent(this,AlarmReceiver.class);
        pendingIntent=PendingIntent.getBroadcast(this,0,intent,0);
        if(alarmManager==null)
        {
            alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);
        Toast.makeText(getApplicationContext(),"Notification turned off",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setIcon(R.drawable.icon)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                }).create().show();
    }





}