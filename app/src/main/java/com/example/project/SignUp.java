package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {


    final Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        SQLiteDBHelper DB = new SQLiteDBHelper(this);
        Spinner spinner = (Spinner) findViewById(R.id.myspinner);
        Spinner Gspinner = (Spinner) findViewById(R.id.genderspinner);
        EditText email = (EditText) findViewById(R.id.EmailSignUp);
        EditText name = (EditText) findViewById(R.id.NameSignUp);
        EditText pass = (EditText) findViewById(R.id.PasswordSignUp);
        EditText confirmPass = (EditText) findViewById(R.id.PasswordSignUpConfirm);
        EditText q2 = (EditText) findViewById(R.id.Q2);
        Button signUp = (Button) findViewById(R.id.signup);
        EditText job = (EditText) findViewById(R.id.job);

        TextView openCal = (TextView) findViewById(R.id.opencalendar);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                openCal.setText(sdf.format(myCalendar.getTime()));
            }

        };


        openCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SignUp.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

       signUp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Customer cust= new Customer(name.getText().toString(),email.getText().toString(),pass.getText().toString(),openCal.getText().toString(),Gspinner.getSelectedItem().toString(),spinner.getSelectedItem().toString(),q2.getText().toString(),job.getText().toString());
               if(!cust.Name.equals("")&&isEmailValid(cust.Email)&&isValidPassword(cust.Password)&&MatchedPasswords(cust.Password,confirmPass.getText().toString())&&!cust.birthDate.equals("")&&!cust.Ques1.equals("")&&!cust.Ques2.equals("")&&!cust.Job.equals("")&&!cust.Gender.equals("")){
                   DB.AddNewCustomer(cust);
                   Toast.makeText(getApplicationContext(),"Signed Up Successfully",Toast.LENGTH_LONG).show();
                   Intent i = new Intent(SignUp.this,HomePage.class);
                   Cursor c = DB.CustIDandName(cust.Email);
                   SignIn.CustID = c.getString(0);
                   SignIn.CustName = cust.Name;
                   SignIn.CustEmail = cust.Email;
                   i.putExtra("Name",cust.Name);
                   i.putExtra("Email",cust.Email);
                   startActivity(i);
             }
               else if(cust.Name.equals("")){
                   Toast.makeText(getApplicationContext(),"Please Enter Your Name",Toast.LENGTH_LONG).show();
               }
             else if(!isEmailValid(cust.Email)){
                 Toast.makeText(getApplicationContext(),"Please Enter a Valid Email Address",Toast.LENGTH_LONG).show();
             }
             else if(!isValidPassword(cust.Password)){
                 Toast.makeText(getApplicationContext(),"Please Enter a Valid Password, Password should have at least 1 number , 1 capital char, 1 special char",Toast.LENGTH_LONG).show();
             }
             else if(!MatchedPasswords(cust.Password,confirmPass.getText().toString())){
                 Toast.makeText(getApplicationContext(),"Passwords didnt match",Toast.LENGTH_LONG).show();
             }
             else if(cust.birthDate.equals("")){
                 Toast.makeText(getApplicationContext(),"Please Enter a birth date",Toast.LENGTH_LONG).show();
             }
             else if(cust.Ques1.equals("")||cust.Ques2.equals("")){
                 Toast.makeText(getApplicationContext(),"Please Answer the 2 questions",Toast.LENGTH_LONG).show();
             }
               else if(cust.Job.equals("")){
                   Toast.makeText(getApplicationContext(),"Please Write Your Job",Toast.LENGTH_LONG).show();
               }
           }
       });
    }
    public boolean isEmailValid(String email)
    {
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        final Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
    public boolean MatchedPasswords(String pass,String ConfirmPass){
        if(pass.equals(ConfirmPass)){
            return true;
        }
        else
        {
            return false;
        }
    }
}