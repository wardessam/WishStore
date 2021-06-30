package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FinalActivity extends AppCompatActivity {
  TextView signout;
  Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_activity);
        signout = (TextView) findViewById(R.id.signout2);
        back = (Button) findViewById(R.id.backToHomePage);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(FinalActivity.this,SignIn.class));
                Toast.makeText(getApplicationContext(),"Signed Out Successfully",Toast.LENGTH_LONG).show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(FinalActivity.this,HomePage.class);
                i.putExtra("Name", SignIn.CustName);
                startActivity(i);

            }
        });
    }
}