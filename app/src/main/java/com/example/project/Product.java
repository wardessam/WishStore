package com.example.project;

import android.graphics.Bitmap;

public class Product {
    private String ProductID;
    private String ProductName;
    private String ProductPrice;
    private String Category;
    private String BarCode;
    private int Quantity;
    private Bitmap image;

    public Product(String productID, String productName, String productPrice, String category, int quantity,Bitmap img) {
        ProductID = productID;
        ProductName = productName;
        ProductPrice = productPrice;
        Category = category;
        Quantity = quantity;
        image=img;
    }

    public Product(String name, String price, Bitmap img){
        this.ProductName= name;
        this.ProductPrice=price;
        this.image=img;

    }
    public Product(String name,String price,String cat,int quantity,Bitmap img,String barCode){
        this.ProductName= name;
        this.ProductPrice=price;
        this.Category=cat;
        this.Quantity=quantity;
        this.image=img;
        BarCode = barCode;

    }

    public void setBarCode(String barCode) {
        BarCode = barCode;
    }

    public String getBarCode() {
        return BarCode;
    }

    public void setID(String n){
        this.ProductID=n;
    }
    public String getID() {
        return ProductID;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
    public Bitmap getImage() {
        return image;
    }
    public void setName(String n){
        this.ProductName=n;
    }
    public String getName() {
        return ProductName;
    }
    public void setPrice(String n){
        this.ProductPrice=n;
    }
    public String getPrice() {
        return ProductPrice;
    }
    public void setCat(String n){
        this.Category=n;
    }
    public String getCat() {
        return Category;
    }
    public void setQuantity(int n){
        this.Quantity=n;
    }
    public int getQuantity() {
        return Quantity;
    }
}
