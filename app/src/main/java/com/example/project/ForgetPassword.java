package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ForgetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
        SQLiteDBHelper DB = new SQLiteDBHelper(this);
        EditText q2 = (EditText) findViewById(R.id.Q2Check);
        Spinner q1 = (Spinner) findViewById(R.id.Q1Check);
        EditText email = (EditText) findViewById(R.id.EmailCheck);
        ImageButton back = (ImageButton) findViewById(R.id.backtoLogin);
        Button check = (Button) findViewById(R.id.checkData);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForgetPassword.this,SignIn.class);
                startActivity(i);
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Cursor c=  DB.ForgetPassword(email.getText().toString(),q1.getSelectedItem().toString(),q2.getText().toString());
               if(c!=null&&c.getCount()>0) {
                   Intent i2 = new Intent(ForgetPassword.this, SetNewPassword.class);
                   i2.putExtra("Email", email.getText().toString());
                   i2.putExtra("Name", c.getString(0));
                   startActivity(i2);
               }
               else{
                   Toast.makeText(getApplicationContext(), "Wrong Answers, Try Again", Toast.LENGTH_LONG).show();
               }
            }
        });
    }
}