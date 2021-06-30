package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignIn extends AppCompatActivity {
    SharedPreferences mPrefs;
    public static String CustID;
    public static String CustEmail;
    public static String CustName;
    static final String PrefsName = "PrefsFile";
    public EditText email;
    public EditText pass;
    CheckBox RememberMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        SQLiteDBHelper DB = new SQLiteDBHelper(this);
        RememberMe = (CheckBox) findViewById(R.id.remeberMe);
        mPrefs = getSharedPreferences(PrefsName,MODE_PRIVATE);
        email = (EditText) findViewById(R.id.emailLogin);
        pass = (EditText) findViewById(R.id.PasswordLogin);
        TextView signup = (TextView) findViewById(R.id.signupPage);
        TextView forgetpass = (TextView) findViewById(R.id.forgetPass);
        Button login = (Button) findViewById(R.id.Login);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignIn.this,SignUp.class);
                startActivity(i);
            }
        });
        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignIn.this,ForgetPassword.class);
                startActivity(i);
            }
        });
        getPreferencesData();
       login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
             if(isEmailValid(email.getText().toString())&&isValidPassword(pass.getText().toString())){
                 if(RememberMe.isChecked()){
                     Boolean isChecked = RememberMe.isChecked();
                     SharedPreferences.Editor editor = mPrefs.edit();
                     editor.putString("PrefName",email.getText().toString());
                     editor.putString("PrefPassword",pass.getText().toString());
                     editor.putBoolean("PrefCheck",isChecked);
                     editor.apply();
                     Toast.makeText(getApplicationContext(),"Account has been saved",Toast.LENGTH_SHORT).show();

                 }
                 else{
                     mPrefs.edit().clear().apply();
                 }
                 if(email.getText().toString().equals("admin@gmail.com")&&pass.getText().toString().equals("A1@34")){
                     Toast.makeText(getApplicationContext(), "Logged In Successfully", Toast.LENGTH_SHORT).show();
                     Intent i = new Intent(SignIn.this, AdminAddProducts.class);
                     startActivity(i);
                 }
                 else {
                     Cursor c = DB.LoginCustomer(email.getText().toString(), pass.getText().toString());
                     if (c != null && c.getCount() > 0) {
                         Toast.makeText(getApplicationContext(), "Logged In Successfully", Toast.LENGTH_LONG).show();
                         CustEmail = email.getText().toString();
                         Intent i = new Intent(SignIn.this, HomePage.class);
                         CustID = c.getString(0);
                         CustName = c.getString(1);
                         i.putExtra("Name", c.getString(1));
                         startActivity(i);
                     } else {
                         Toast.makeText(getApplicationContext(), "Wrong Email Address or Password", Toast.LENGTH_LONG).show();
                         Toast.makeText(getApplicationContext(), "If you forgot your password, You could reset it", Toast.LENGTH_LONG).show();
                     }
                 }
             }
             else if (!isEmailValid(email.getText().toString())){
                 Toast.makeText(getApplicationContext(),"Please Enter a Valid Email Address",Toast.LENGTH_LONG).show();
                 }
             else{
                 Toast.makeText(getApplicationContext(),"Please Enter a Valid Password, Password should have at least 1 number , 1 capital char, 1 special char",Toast.LENGTH_LONG).show();

             }

           }
       });
    }
    public void getPreferencesData(){
        SharedPreferences p = getSharedPreferences(PrefsName,MODE_PRIVATE);
        if(p.contains("PrefName")){
            String name =p.getString("PrefName","NOT FOUND");
            email.setText(name.toString());
        }
        if(p.contains("PrefPassword")){
            String password =p.getString("PrefPassword","NOT FOUND");
            pass.setText(password.toString());
        }
        if(p.contains("PrefCheck")){
            boolean checked =p.getBoolean("PrefCheck",false);
            RememberMe.setChecked(checked);
        }
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
}