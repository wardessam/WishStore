package com.example.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Locale;

public class HomePage extends AppCompatActivity {
    private static final int REQUEST_CODE_SPEECH_INPUT =1000 ;
    SQLiteDBHelper DB;
    TextView User;
    EditText searchtext;
    ImageButton searchTbtn;
    ImageButton searchVbtn;
    ImageButton searchCbtn;
    String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        DB = new SQLiteDBHelper(this);
        DB.AddCategoriesToSystem();
        Intent intent = getIntent();
        String name = intent.getStringExtra("Name");
        User= (TextView) findViewById(R.id.NameofUser);
        searchtext = (EditText) findViewById(R.id.searchtxt);
        searchTbtn = (ImageButton)findViewById(R.id.searchbtntxt);
        searchVbtn= (ImageButton)findViewById(R.id.searchbtnvoice);
        searchCbtn = (ImageButton) findViewById(R.id.searchbtncamera);
        User.setText("Hello "+name);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_gallery_item);
        GridView ListOfCats = (GridView) findViewById(R.id.GridData);
        ListOfCats.setAdapter(adapter);
        Cursor c=DB.FetchCategories();
        while (!c.isAfterLast()){
            adapter.add(c.getString(0));
            c.moveToNext();
        }
        searchVbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });
        ListOfCats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              TextView selected = (TextView)view;
              String selectedItem=selected.getText().toString();
                Intent intent = new Intent(HomePage.this, ShowProducts.class);
                intent.putExtra("Name", selectedItem);
                intent.putExtra("bool", "false");
                startActivity(intent);
            }
        });
        searchCbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });
        searchTbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, ShowProducts.class);
                String productName = searchtext.getText().toString();
                intent.putExtra("Name", productName);
                intent.putExtra("bool", "true");
                startActivity(intent);
            }
        });
    }

    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(captureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan Code");
        integrator.initiateScan();

    }

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak Something");

        try{


            startActivityForResult(intent,1);





        }catch(ActivityNotFoundException e) {
            Intent your_browser_intent = new Intent(Intent.ACTION_VIEW,

                    Uri.parse("https://market.android.com/details?id=APP_PACKAGE_NAME"));
                    startActivity(your_browser_intent); }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result2 = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result2 != null) {
            if (result2.getContents() != null) {
              // searchtext.setText(result2.getContents());
                String ProName = DB.getProductNameByProBarCode(result2.getContents());
                Intent intent = new Intent(HomePage.this, ShowProducts.class);
                intent.putExtra("Name", ProName);
                intent.putExtra("bool", "true");
                startActivity(intent);

            } else {
                Toast.makeText(this, "No result", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case 1: {
                    if (resultCode == RESULT_OK && null != data) {
                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        searchtext.setText(result.get(0));
                        String n = result.get(0);
                        Intent intent = new Intent(HomePage.this, ShowProducts.class);
                        intent.putExtra("Name", n);
                        intent.putExtra("bool", "true");
                        startActivity(intent);
                    }
                    break;
                }
            }
        }
    }
}