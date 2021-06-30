package com.example.project;

import java.util.List;

public class Order {
       private String OrderDate;
       private String CustID;
       private String Address;
       private List<Product> products;

       public Order(String orderDate, String custID, String address, List<Product> products) {
              OrderDate = orderDate;
              CustID = custID;
              Address = address;
              this.products = products;
       }


       public void setOrderDate(String orderDate) {
              OrderDate = orderDate;
       }

       public void setCustID(String custID) {
              CustID = custID;
       }

       public void setAddress(String address) {
              Address = address;
       }

       public void setProducts(List<Product> products) {
              this.products = products;
       }


       public String getOrderDate() {
              return OrderDate;
       }

       public String getCustID() {
              return CustID;
       }

       public String getAddress() {
              return Address;
       }

       public List<Product> getProducts() {
              return products;
       }


}
