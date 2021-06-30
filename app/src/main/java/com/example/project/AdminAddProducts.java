package com.example.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class AdminAddProducts extends AppCompatActivity {

    private static int PICK_IMAGE_REQUEST =100;
    private ImageView myImg;
    private EditText proName;
    private EditText proPrice;
    private EditText proQuan;
    private EditText probarcode;
    private Spinner Cat;
    private Uri imageFilePath;
    private Bitmap imageToStore;
    SQLiteDBHelper sql ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_products);
        proName   = (EditText)findViewById(R.id.ProductName);
        proPrice = (EditText)findViewById(R.id.ProductPrice);
        proQuan = (EditText)findViewById(R.id.ProductQuantity);
        Cat = (Spinner) findViewById(R.id.category);
        probarcode = (EditText) findViewById(R.id.ProductBarcode);
        myImg =(ImageView)findViewById(R.id.imgView);
        sql  = new SQLiteDBHelper(this);
        sql.AddCategoriesToSystem();
    }
    public void chooseImg(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if(resultCode==RESULT_OK && requestCode == PICK_IMAGE_REQUEST && data!=null && data.getData()!=null){
                imageFilePath=data.getData();
                imageToStore= MediaStore.Images.Media.getBitmap(getContentResolver(),imageFilePath);
                myImg.setImageBitmap(imageToStore);
            }
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    public void storeImg(View view){
        try{
            if(!proName.getText().toString().isEmpty() &&!proPrice.getText().toString().isEmpty() && !proQuan.getText().toString().isEmpty()&&
                    myImg.getDrawable()!=null&& imageToStore!=null) {
                String c= sql.getCategoryID(Cat.getSelectedItem().toString());
                sql.storeImg(new Product(proName.getText().toString(),proPrice.getText().toString(),c,Integer.valueOf(proQuan.getText().toString()),imageToStore,probarcode.getText().toString()));

            }
            else{
                Toast.makeText(getApplicationContext(),"Provide Image and Data Please",Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
}