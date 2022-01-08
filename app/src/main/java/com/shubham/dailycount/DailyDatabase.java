package com.shubham.dailycount;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.util.ArrayList;

public class DailyDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "daily_database";
    private static final String TABLE_NAME1 = "daily_milk";
    private static final String TABLE_NAME2 = "daily_water";

    DailyDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable1 = "create table " + TABLE_NAME1 + "(date String PRIMARY KEY UNIQUE, quantity REAL)";
        String createTable2 = "create table " + TABLE_NAME2 + "(date String PRIMARY KEY UNIQUE, quantity REAL)";
        sqLiteDatabase.execSQL(createTable1);
        sqLiteDatabase.execSQL(createTable2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
    }

    public boolean fillDailyMilk(String date,double quant) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date",date);
        contentValues.put("quantity",quant);
        sqLiteDatabase.insertWithOnConflict(TABLE_NAME1,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        sqLiteDatabase.close();
        return true;
    }
    public boolean fillDailyWater(String date,double quant) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date",date);
        contentValues.put("quantity",quant);
        sqLiteDatabase.insertWithOnConflict(TABLE_NAME2,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        sqLiteDatabase.close();
        return true;
    }


    public Cursor getAll(String TABLE_NAME)
    {
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
       // db.close();
        return res;
    }
    public void delete(String date)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from "+TABLE_NAME1+" where date='"+date+"'");
        sqLiteDatabase.execSQL("delete from "+TABLE_NAME2+" where date='"+date+"'");

    }
    public ArrayList<DataSQL> readDataSQL() throws ParseException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor1 = db.rawQuery("SELECT * FROM " + TABLE_NAME1, null);
        Cursor cursor2 = db.rawQuery("SELECT * FROM " + TABLE_NAME2, null);
        String date="dd/mm/yyyy",milkQ="0",waterQ="0";
        ArrayList<DataSQL> dataSQLS=new ArrayList<>();
        if(cursor1.moveToFirst())
            do{
                date=cursor1.getString(0);
                if(cursor2.moveToFirst())
                {
                    do {
                        if (cursor2.getString(0).equals(date)) {
                            milkQ = cursor1.getString(1);
                            waterQ = cursor2.getString(1);
                        }
                    }while (cursor2.moveToNext());
                }
                DataSQL dataSQL=new DataSQL(date,milkQ,waterQ);
                dataSQLS.add(dataSQL);

            }while(cursor1.moveToNext());
            return dataSQLS;
    }

}
