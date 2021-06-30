package com.example.project;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class Customer {
    String ID;
    String Name,Email,Password,birthDate, Ques1,Ques2,Gender,Job;

    public Customer(String name,String email,String pass,String date,String gender,String q1,String q2,String job){
        this.Name=name;
        this.Email = email;
        this.Password=pass;
        this.birthDate=date;
        this.Gender=gender;
        this.Ques1 = q1;
        this.Ques2=q2;
        this.Job=job;
    }


}
