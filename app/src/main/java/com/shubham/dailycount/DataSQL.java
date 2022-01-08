package com.shubham.dailycount;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataSQL {
    public Date date;
    public String milkQ;
    public String waterQ;

    public DataSQL(String date, String milkQ, String waterQ) throws ParseException {

        this.date = convertDate(date);
        this.milkQ = milkQ+" Litre";
        this.waterQ = waterQ+" Jar";
    }
    public Date convertDate(String s) throws ParseException {
        Date date=new SimpleDateFormat("dd/MM/yyyy").parse(s);
        return date;
    }
}
