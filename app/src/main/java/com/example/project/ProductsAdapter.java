package com.example.project;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends ArrayAdapter<Product> {
    public Context mContext;
    int mResource;

    public static ArrayList<Product> products= new ArrayList<>();
    public ProductsAdapter(@NonNull Context context, int resource, ArrayList<Product> objects) {
        super(context, resource, objects);
        mResource=resource;
        mContext=context;
    }
    SQLiteDBHelper db = new SQLiteDBHelper(getContext());
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String price = getItem(position).getPrice();
        Bitmap img = getItem(position).getImage();
        Product p = new Product(name,price,img);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView= inflater.inflate(mResource,parent,false);
        TextView proName = (TextView) convertView.findViewById(R.id.proName);
        TextView proPrice = (TextView) convertView.findViewById(R.id.proPrice);
        ImageView proImg = (ImageView) convertView.findViewById(R.id.proImg);
        Button add = (Button) convertView.findViewById(R.id.AddToCart);
        ImageButton plus = (ImageButton) convertView.findViewById(R.id.Addonemore);
        ImageButton minus = (ImageButton) convertView.findViewById(R.id.minusonemore);
        EditText quan = (EditText) convertView.findViewById(R.id.quantity);
        proName.setText(name);
        proPrice.setText(price+" L.E");
        proImg.setImageBitmap(img);
        quan.setText("1");
        String[] data = db.getCatIDAndProIDByproName(name);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] data= db.getCatIDAndProIDByproName(name);
                products.add(new Product(data[0],name,proPrice.getText().toString(),data[1],Integer.valueOf(quan.getText().toString()),img));
               // Toast.makeText(getContext(),quan.getText().toString(),Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext,"Item added to Your Cart",Toast.LENGTH_SHORT).show();
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q = Integer.valueOf(quan.getText().toString())+1;
                quan.setText(String.valueOf(q));
                String[] strings = proPrice.getText().toString().split(" ");
                int newP = Integer.parseInt(strings[0]) + Integer.parseInt(price);
                proPrice.setText(String.valueOf(newP) + " L.E");

            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(quan.getText().toString())>1) {
                    int q = Integer.valueOf(quan.getText().toString())-1;
                    quan.setText(String.valueOf(q));
                    String[] strings = proPrice.getText().toString().split(" ");
                    int newP = Integer.parseInt(strings[0]) - Integer.parseInt(price);
                    proPrice.setText(String.valueOf(newP) + " L.E");

                }

            }
        });
        return convertView;
    }
}
