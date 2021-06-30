package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.jar.Attributes;

public class SQLiteDBHelper  extends SQLiteOpenHelper {
    public static String databaseName = "WishStoreDB";
    public static SQLiteDatabase WishStoreDB;
    private ByteArrayOutputStream myOutputStream;
    private byte[] imageInByte;

    Context context;
    public SQLiteDBHelper(Context context){
        super(context,databaseName,null,2);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Customer (CustID integer primary key autoincrement, Name text not null," +
                "Email text not null, Password text not null," +
                "BirthDate text not null , Gender text not null," +
                "Question1 text not null , Question2 text not null," +
                "Job text not null)");

        db.execSQL("create table Category (CatID integer primary key autoincrement, CatName text not null)");

        db.execSQL("create table Orders (OrderID integer primary key autoincrement, OrderDate text not null," +
                "Address text not null , Cust_ID integer not null," +
                "FOREIGN KEY (Cust_ID) REFERENCES Customer (CustID))");

        db.execSQL("create table Products (ProductID integer primary key autoincrement, ProductName text not null," +
                "Price text not null ,Quantity integer not null, Cat_ID integer not null, ProductImg BLOB not null,BarCode text ," +
                "FOREIGN KEY (Cat_ID) REFERENCES Category (CatID))");

        db.execSQL("create table OrderDetails (OrdID integer not null, ProID integer not null," +
                "Quantity integer not null , PRIMARY KEY(OrdID,ProID)," +
                "FOREIGN KEY (OrdID) REFERENCES Orders (OrderID),"+
                "FOREIGN KEY (ProID) REFERENCES Products (ProductID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists Customer");
        db.execSQL("drop table if exists Category");
        db.execSQL("drop table if exists Orders");
        db.execSQL("drop table if exists Products");
        db.execSQL("drop table if exists OrderDetails");
        onCreate(db);
    }
    public void AddNewCustomer(Customer c){
        ContentValues row = new ContentValues();
        row.put("Name",c.Name);
        row.put("Email",c.Email);
        row.put("Password",c.Password);
        row.put("BirthDate",c.birthDate);
        row.put("Gender",c.Gender);
        row.put("Question1",c.Ques1);
        row.put("Question2",c.Ques2);
        row.put("Job",c.Job);
        WishStoreDB = getWritableDatabase();
        WishStoreDB.insert("Customer",null,row);
        WishStoreDB.close();
    }

    public Cursor LoginCustomer(String email,String password){
        WishStoreDB = getReadableDatabase();
        String[] data={email,password};
        Cursor cursor = WishStoreDB.rawQuery("select CustID,Name from Customer where Email Like ? and Password Like ?",data);
        cursor.moveToFirst();
        WishStoreDB.close();
        return cursor;
    }
    public Cursor CustIDandName(String email){
        WishStoreDB = getReadableDatabase();
        String[] data={email};
        Cursor cursor = WishStoreDB.rawQuery("select CustID,Name from Customer where Email Like ?",data);
        cursor.moveToFirst();
        WishStoreDB.close();
        return cursor;
    }
    public Cursor ForgetPassword(String mail,String q1,String q2){
        WishStoreDB = getReadableDatabase();
        String[] data={mail,q1,q2};
        Cursor cursor = WishStoreDB.rawQuery("select Name from Customer where Email Like ? and Question1 Like ? and Question2 Like ?",data);
        cursor.moveToFirst();
        WishStoreDB.close();
        return cursor;
    }
    public void ChangePassword(String mail,String pass){
        WishStoreDB = getReadableDatabase();
        String[] data={pass,mail};
        Cursor cursor = WishStoreDB.rawQuery("update Customer set Password=? where Email Like ? ",data);
        cursor.moveToFirst();
        WishStoreDB.close();

    }

    public void AddCategoriesToSystem(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("drop table if exists Category");
        db.execSQL("create table Category (CatID integer primary key autoincrement, CatName text not null)");

        ContentValues row = new ContentValues();
        row.put("CatName","Health& Beauty");
        WishStoreDB = getWritableDatabase();
        WishStoreDB.insert("Category",null,row);

        ContentValues row2 = new ContentValues();
        row2.put("CatName","Home& Kitchen");
        WishStoreDB = getWritableDatabase();
        WishStoreDB.insert("Category",null,row2);

        ContentValues row3 = new ContentValues();
        row3.put("CatName","TVs");
        WishStoreDB = getWritableDatabase();
        WishStoreDB.insert("Category",null,row3);

        ContentValues row4 = new ContentValues();
        row4.put("CatName","Phones");
        WishStoreDB = getWritableDatabase();
        WishStoreDB.insert("Category",null,row4);

        ContentValues row5 = new ContentValues();
        row5.put("CatName","Supermarket");
        WishStoreDB = getWritableDatabase();
        WishStoreDB.insert("Category",null,row5);

        ContentValues row6 = new ContentValues();
        row6.put("CatName","Toys");
        WishStoreDB = getWritableDatabase();
        WishStoreDB.insert("Category",null,row6);
        WishStoreDB.close();
    }
    public Cursor FetchCategories(){
        WishStoreDB = getReadableDatabase();
        Cursor cursor = WishStoreDB.rawQuery("select CatName from Category",null);
        cursor.moveToFirst();
        WishStoreDB.close();
        return cursor;
    }
    public String getCategoryID(String CatName){
        WishStoreDB = getReadableDatabase();
        String[] data={CatName};
        Cursor cursor = WishStoreDB.rawQuery("select CatID from Category where CatName Like ? ",data);
        cursor.moveToFirst();
        WishStoreDB.close();
        return cursor.getString(0);
    }
    public String getProductPrice(String Name){
        WishStoreDB = getReadableDatabase();
        String[] data={Name};
        Cursor cursor = WishStoreDB.rawQuery("select Price from Products where ProductName like ?",data);
        cursor.moveToFirst();
        WishStoreDB.close();
        return cursor.getString(0);
    }
    public String[] getCatIDAndProIDByproName(String proName){
        WishStoreDB = getReadableDatabase();
        String[] data={proName};
        Cursor cursor = WishStoreDB.rawQuery("select ProductID,Cat_ID from Products where ProductName Like ? ",data);
        cursor.moveToFirst();
        WishStoreDB.close();
        return new String[]{cursor.getString(0),cursor.getString(1)};
    }
    public void storeImg(Product p){
        try{
            SQLiteDatabase sql = this.getWritableDatabase();
            Bitmap imgTostore = p.getImage();
            myOutputStream = new ByteArrayOutputStream();
            imgTostore.compress(Bitmap.CompressFormat.JPEG,100,myOutputStream);
            imageInByte=myOutputStream.toByteArray();
            ContentValues content = new ContentValues();
            content.put("ProductName",p.getName());
            content.put("Price",p.getPrice());
            content.put("Quantity",p.getQuantity());
            content.put("Cat_ID",p.getCat());
            content.put("ProductImg",imageInByte);
            content.put("BarCode",p.getBarCode());
            long checkQuery=sql.insert("Products",null,content);
            if(checkQuery!=-1){
                Toast.makeText(context,"Image SUCCESSFULLY INSERTED",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context,"Error: Can't Save Image",Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<Product> getProductsByCatName(String category){
        String CatID = getCategoryID(category);
        SQLiteDatabase sql = this.getReadableDatabase();
        ArrayList<Product> p_List = new ArrayList<>();
        String []arg ={CatID};
        Cursor c = sql.rawQuery("select ProductName, Price,ProductImg from Products where Cat_ID like ?",arg);
        if(c!=null && c.getCount()!=0){
            while (c.moveToNext()){
                String Name = c.getString(0);
                String Price = c.getString(1);
                byte [] img = c.getBlob(2);
                Bitmap objBitmap = BitmapFactory.decodeByteArray(img,0,img.length);
                p_List.add(new Product(Name,Price,objBitmap));
            }
        }
        else{
            Toast.makeText(context,"There is No Values In DataBase",Toast.LENGTH_SHORT).show();
            return null;
        }
        return p_List;

    }

    public ArrayList<Product> getProductsByProName(String name){
        SQLiteDatabase sql = this.getReadableDatabase();
        ArrayList<Product> p_List = new ArrayList<>();
        String []arg ={"%"+ name+ "%"};
        Cursor c = sql.rawQuery("select ProductName, Price,ProductImg from Products where ProductName like ?",arg);
        if(c!=null && c.getCount()!=0){
            while (c.moveToNext()){
                String Name = c.getString(0);
                String Price = c.getString(1);
                byte [] img = c.getBlob(2);
                Bitmap objBitmap = BitmapFactory.decodeByteArray(img,0,img.length);
                p_List.add(new Product(Name,Price,objBitmap));
            }
        }
        else{
            Toast.makeText(context,"There is No Values In DataBase",Toast.LENGTH_SHORT).show();
            return null;
        }
        return p_List;

    }

    public String getProductNameByProBarCode(String barcode){
        SQLiteDatabase sql = this.getReadableDatabase();
        String name;
        String []arg ={barcode};
        Cursor c = sql.rawQuery("select ProductName from Products where BarCode like ?",arg);
        c.moveToFirst();
        if(c!=null && c.getCount()!=0){
            name = c.getString(0);
            return name;
        }
        else{
            Toast.makeText(context,"There is No Values In DataBase",Toast.LENGTH_SHORT).show();
            return null;
        }


    }

    public void MakeOrder(Order o){
        ContentValues row = new ContentValues();
        row.put("OrderDate",o.getOrderDate());
        row.put("Address",o.getAddress());
        row.put("Cust_ID",o.getCustID());
        WishStoreDB = getWritableDatabase();
        WishStoreDB.insert("Orders",null,row);
    }
    public String getOrdID(Order o){
        WishStoreDB = getReadableDatabase();
        String[] data={o.getOrderDate(),o.getAddress(),o.getCustID()};
        Cursor cursor = WishStoreDB.rawQuery("select OrderID from Orders where OrderDate Like ? and Address Like ? and Cust_ID Like ? ",data);
        cursor.moveToFirst();
        WishStoreDB.close();
        return cursor.getString(0);
    }
    public void OrderDetails(String ordID,String proID, int quantity){
        ContentValues row = new ContentValues();
        row.put("OrdID",ordID);
        row.put("ProID",proID);
        row.put("Quantity",quantity);
        WishStoreDB = getWritableDatabase();
        WishStoreDB.insert("OrderDetails",null,row);

    }
    public void SetNewQuantitiesAfterEveryOrder(Product p){
        WishStoreDB = getReadableDatabase();
        String[] data={p.getName()};
        Cursor cursor = WishStoreDB.rawQuery("select Quantity from Products where ProductName Like ? ",data);
        cursor.moveToFirst();
        int q =Integer.parseInt(cursor.getString(0));
        int Newq = q - p.getQuantity();
        String[] data2={String.valueOf(Newq),p.getName()};
        Cursor cursor2 = WishStoreDB.rawQuery("update Products set Quantity=? where ProductName Like ? ",data2);
        cursor2.moveToFirst();
        WishStoreDB.close();
    }

}
