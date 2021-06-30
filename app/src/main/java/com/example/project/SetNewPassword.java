package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetNewPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_new_password);
        SQLiteDBHelper DB = new SQLiteDBHelper(this);
        EditText pass = (EditText) findViewById(R.id.PasswordChange);
        EditText confirmpass = (EditText) findViewById(R.id.ConfirmPasswordChange);
        Intent intent = getIntent();
        String mail = intent.getStringExtra("Email");
        String name = intent.getStringExtra("Name");
        Button signin = (Button) findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidPassword(pass.getText().toString())&&MatchedPasswords(pass.getText().toString(),confirmpass.getText().toString())) {
                    //Toast.makeText(getApplicationContext(), mail, Toast.LENGTH_LONG).show();
                    DB.ChangePassword(mail, pass.getText().toString());

                    Cursor c = DB.CustIDandName(mail);
                    if (c != null && c.getCount() > 0) {
                        SignIn.CustID = c.getString(0);
                        Toast.makeText(getApplicationContext(), "Password Changed", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(SetNewPassword.this, HomePage.class);
                        i.putExtra("Name", c.getString(1));
                        startActivity(i);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Unmatched Passwords or Invalid Password, Password should have at least 1 number , 1 capital char, 1 special char",Toast.LENGTH_LONG).show();
                }

            }
        });
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