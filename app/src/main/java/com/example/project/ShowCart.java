package com.example.project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ShowCart extends AppCompatActivity {

ListView cart;
TextView back;
Button checkout;
Button AddItems;
CartAdapter adapter3;
public static List<Product> FinalProducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_cart);
        cart= (ListView) findViewById(R.id.cartData);
        back =(TextView)findViewById(R.id.backToPros);
        checkout=(Button) findViewById(R.id.Checkout);
        AddItems=(Button) findViewById(R.id.AddItems);
        ArrayList<Product> pros= ProductsAdapter.products;
        adapter3 = new CartAdapter(getApplicationContext(), R.layout.custom_cart_adapter,pros);
        cart.setAdapter(adapter3);
        adapter3.notifyDataSetChanged();

       /* cart.setOnItemClickListener(new CartAdapter(this,R.layout.custom_cart_adapter,pros).OnItemClickListener() {
            @Override
            public void onItemClick(CartAdapter<Product> adapter3, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),String.valueOf(i),Toast.LENGTH_SHORT).show();
               Product p = pros.get(i);
               pros.remove(p);
                adapter3 = new CartAdapter(getApplicationContext(), R.layout.custom_cart_adapter,pros);
                cart.setAdapter(adapter3);
            }
        });*/

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowCart.this, ShowProducts.class);
                intent.putExtra("Name", "Health& Beauty");
                intent.putExtra("bool", "false");
                startActivity(intent);
            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowCart.this, CheckOut.class);
             //   int s= ProductsAdapter.products.size();
               /* for(int i=0;i<ProductsAdapter.products.size();i++) {
                    Toast.makeText(getApplicationContext(), String.valueOf(ProductsAdapter.products.get(i).getQuantity()), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), String.valueOf(ProductsAdapter.products.get(i).getPrice()), Toast.LENGTH_SHORT).show();
                      //  ProductsAdapter.products.get(i).setQuantity(q);

                } */
               // Toast.makeText(getApplicationContext(), String.valueOf(CartAdapter.finalItems.size()), Toast.LENGTH_SHORT).show();
               // Toast.makeText(getApplicationContext(), String.valueOf(FinalProducts.size()), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        AddItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowCart.this, ShowProducts.class);
                intent.putExtra("Name", "Health& Beauty");
                intent.putExtra("bool", "false");
                startActivity(intent);
            }
        });

    }

}