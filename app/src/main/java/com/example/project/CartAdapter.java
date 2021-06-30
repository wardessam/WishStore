package com.example.project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CartAdapter extends ArrayAdapter<Product> {
    public Context mContext;
    int mResource;
    public static ArrayList<Product> finalItems= new ArrayList<>();
    public CartAdapter(@NonNull Context context, int resource, ArrayList<Product> objects) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
    }
    SQLiteDBHelper db = new SQLiteDBHelper(getContext());
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String price = getItem(position).getPrice();
        Bitmap img = getItem(position).getImage();
        int quanti = getItem(position).getQuantity();
        String pricePro = db.getProductPrice(name);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView= inflater.inflate(mResource,parent,false);
        TextView proName = (TextView) convertView.findViewById(R.id.proNameinCart);
        TextView proPrice = (TextView) convertView.findViewById(R.id.proPriceinCart);
        ImageView proImg = (ImageView) convertView.findViewById(R.id.proImgCart);
        ImageButton plus = (ImageButton) convertView.findViewById(R.id.AddonemoreinCart);
        ImageButton minus = (ImageButton) convertView.findViewById(R.id.minusonemoreinCart);
        EditText quan = (EditText) convertView.findViewById(R.id.quantityinCart);

        proName.setText(name);
        proPrice.setText(price);
        proImg.setImageBitmap(img);
        quan.setText(String.valueOf(quanti));

        String[] data = db.getCatIDAndProIDByproName(name);
        String Quan = quan.getText().toString();
        Product p = new Product(data[0], name, price, data[1], Integer.valueOf(Quan), img);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!quan.getText().toString().equals("0")) {
                    int q = Integer.valueOf(quan.getText().toString()) + 1;
                    quan.setText(String.valueOf(q));
                    String[] strings = proPrice.getText().toString().split(" ");
                    String[] strings2 = price.split(" ");
                   // Toast.makeText(mContext, strings[0], Toast.LENGTH_LONG).show();

                    int newP = Integer.parseInt(strings[0]) + Integer.parseInt(pricePro);
                    proPrice.setText(String.valueOf(newP) + " L.E");

                    for(int i=0;i<ProductsAdapter.products.size();i++) {
                        if (name.equals(ProductsAdapter.products.get(i).getName()) && data[0].equals(ProductsAdapter.products.get(i).getID())) {
                          //  ProductsAdapter.products.get(i).setPrice(String.valueOf(newP));
                            //ProductsAdapter.products.get(i).setQuantity(q);
                         //   Toast.makeText(getContext(),"error",Toast.LENGTH_SHORT).show();
                            ProductsAdapter.products.set(i,new Product(data[0], name, String.valueOf(newP), data[1], q, img));
                        }
                    }
                }

            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quan.getText().toString().equals("1")) {
                    for(int i=0;i<ProductsAdapter.products.size();i++) {
                        if (name.equals(ProductsAdapter.products.get(i).getName()) && quanti == ProductsAdapter.products.get(i).getQuantity()) {
                            ProductsAdapter.products.remove(i);
                           // finalItems.add(p);
                            remove(p);
                            notifyDataSetChanged();
                            Toast.makeText(getContext(), "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    int q = Integer.parseInt(quan.getText().toString()) - 1;
                    quan.setText(String.valueOf(q));
                    String[] strings = proPrice.getText().toString().split(" ");

                    int newP = Integer.parseInt(strings[0]) - Integer.parseInt(pricePro);
                    proPrice.setText(String.valueOf(newP) + " L.E");

                    for(int i=0;i<ProductsAdapter.products.size();i++) {
                        if (name.equals(ProductsAdapter.products.get(i).getName()) && data[0].equals(ProductsAdapter.products.get(i).getID())) {
                            //  ProductsAdapter.products.get(i).setPrice(String.valueOf(newP));
                            //ProductsAdapter.products.get(i).setQuantity(q);
                            //   Toast.makeText(getContext(),"error",Toast.LENGTH_SHORT).show();
                            ProductsAdapter.products.set(i,new Product(data[0], name, String.valueOf(newP), data[1], q, img));
                        }
                    }
                }
            }
        });

        return convertView;
    }

}
