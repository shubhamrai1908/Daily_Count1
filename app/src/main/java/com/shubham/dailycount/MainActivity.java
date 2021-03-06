package com.shubham.dailycount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText et1,et2;
    Button btn;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        et1=findViewById(R.id.editText1);
        et2=findViewById(R.id.editText2);
        btn=findViewById(R.id.save);
        int milk = sharedpreferences.getInt("milk", 0);
        int water = sharedpreferences.getInt("water", 0);
        if(milk!=0&&water!=0)
        {

            try {
                et1.setText(milk+"");
                et2.setText(water+"");
                update();
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
            }
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    SharedPreferences.Editor myEdit = sharedpreferences.edit();
                    myEdit.putInt("milk", Integer.parseInt(et1.getText().toString()));
                    myEdit.putInt("water", Integer.parseInt(et2.getText().toString()));
                    myEdit.commit();
                    Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                    startActivity(intent);


            }
        });


    }
    public void update()
    {
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(MainActivity.this);
        builder.setMessage("Do you want to update it?");
        builder.setTitle("Pricing are already filled");
        builder.setCancelable(false);
        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                dialog.cancel();
                            }
                        });
        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                                startActivity(intent);
                            }
                        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();



    }

}