package com.example.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ShowProducts extends AppCompatActivity {
    SQLiteDBHelper db;
    GridView grid;
    EditText searchtxt;
    ImageButton Stxt,Svoice,Scamera;
    TextView signout;
    Spinner spin;
    Button showcart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_products);
        db= new SQLiteDBHelper(this);
        grid= (GridView)findViewById(R.id.myGrid);
        searchtxt = (EditText) findViewById(R.id.searchtxtinShow);
        Stxt = (ImageButton) findViewById(R.id.searchbtntxtinShow);
        Svoice = (ImageButton) findViewById(R.id.searchbtnvoiceinShow);
        Scamera = (ImageButton) findViewById(R.id.searchbtncamerainShow);
        spin= (Spinner) findViewById(R.id.mycat);
        signout = (TextView) findViewById(R.id.signout);
        showcart = (Button) findViewById(R.id.showCart);
        String[] cats = new String[]{
                "Categories",
                "Health& Beauty",
                "Home& Kitchen",
                "TVs",
                "Phones",
                "Supermarket",
                "Toys"
        };

        final List<String> catsList = new ArrayList<>(Arrays.asList(cats));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item,catsList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the second item from Spinner
                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position==0) {
                    // Set the disable item text color
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spin.setAdapter(spinnerArrayAdapter);

        Intent intent = getIntent();
        String flag = intent.getStringExtra("bool");
        String name = intent.getStringExtra("Name");
        if(flag.equals("false")) {
            ArrayList<Product> products = db.getProductsByCatName(name);
            ProductsAdapter adapter = new ProductsAdapter(this, R.layout.custom_adapter, products);
            grid.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else if(flag.equals("true")){
            ArrayList<Product> products = db.getProductsByProName(name);
            ProductsAdapter adapter2 = new ProductsAdapter(this, R.layout.custom_adapter, products);
            grid.setAdapter(adapter2);
            adapter2.notifyDataSetChanged();
        }
        Scamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });
      Stxt.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              String pName = searchtxt.getText().toString();
              ArrayList<Product> products = db.getProductsByProName(pName);
              ProductsAdapter adapter3 = new ProductsAdapter(getApplicationContext(), R.layout.custom_adapter, products);
              grid.setAdapter(adapter3);
              adapter3.notifyDataSetChanged();
          }
      });
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0) {
                    String item = ((TextView) view).getText().toString();
                    ArrayList<Product> products = db.getProductsByCatName(item);
                    ProductsAdapter adapter = new ProductsAdapter(getApplicationContext(), R.layout.custom_adapter, products);
                    grid.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowProducts.this,SignIn.class));
                Toast.makeText(getApplicationContext(),"Signed Out Successfully",Toast.LENGTH_LONG).show();
               // Toast.makeText(getApplicationContext(),String.valueOf(ProductsAdapter.products.size()),Toast.LENGTH_LONG).show();
                //for(int i=0;i<ProductsAdapter.products.size();i++){
                  //  Toast.makeText(getApplicationContext(),String.valueOf(ProductsAdapter.products.get(i).getName()),Toast.LENGTH_LONG).show();
                //}
            }
        });
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s= ((TextView)view).getText().toString();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }
        });
        showcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowProducts.this, ShowCart.class);
                startActivity(intent);
            }
        });
        Svoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });
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
    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(captureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan Code");
        integrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result2 = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result2 != null) {
            if (result2.getContents() != null) {
                //searchtxt.setText(result2.getContents());
                String ProName = db.getProductNameByProBarCode(result2.getContents());
                ArrayList<Product> products = db.getProductsByProName(ProName);
                ProductsAdapter adapter3 = new ProductsAdapter(getApplicationContext(), R.layout.custom_adapter, products);
                grid.setAdapter(adapter3);
                adapter3.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "No result", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case 1: {
                    if (resultCode == RESULT_OK && null != data) {
                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        searchtxt.setText(result.get(0));
                        String name = result.get(0);
                        ArrayList<Product> products = db.getProductsByProName(name);
                        ProductsAdapter adapter3 = new ProductsAdapter(getApplicationContext(), R.layout.custom_adapter, products);
                        grid.setAdapter(adapter3);
                        adapter3.notifyDataSetChanged();
                    }
                    break;
                }
            }
        }
    }
}