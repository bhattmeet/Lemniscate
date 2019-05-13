package com.example.asus.lemniscate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class Cart extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference mdatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        mdatabase = FirebaseDatabase.getInstance().getReference("Cart");

        final Button order = (Button) findViewById(R.id.btn_order);
        final ImageView back = (ImageView) findViewById(R.id.iv_back);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cart.this, Payment.class));
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cart.this, Home.class));
                finish();
            }
        });
        String cart = null;
        firebasecartlist(cart);
    }

        private void firebasecartlist(String cart){

        Query query = mdatabase.orderByChild("bookname").startAt(cart).endAt(cart + "\uf8ff");
        FirebaseRecyclerAdapter<CartModel,CartViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CartModel, CartViewHolder>(
                CartModel.class,
                R.layout.row_cart_item,
                CartViewHolder.class,
                query
        ) {

            @Override
            protected void populateViewHolder(CartViewHolder ViewHolder, CartModel cartModel, int i) {
                ViewHolder.setDetails(getApplicationContext(),cartModel.getBookname(),cartModel.getBookimage(),cartModel.getGener(),cartModel.getQuantity(),cartModel.getPrice());
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder{

        View mview;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;
        }


        public void setDetails(Context ctx, String bookname, String bookimage, String gener, String quantity, String price){
            final TextView bkname = mview.findViewById(R.id.bk_name);
            final TextView bkgener = mview.findViewById(R.id.bk_gener);
            final TextView bkquantity = mview.findViewById(R.id.bk_price);
            final TextView bkprice = mview.findViewById(R.id.bk_quantity);
            final ImageView bkimage = mview.findViewById(R.id.bk_img);

            bkname.setText(bookname);
            bkgener.setText(gener);
            Glide.with(ctx).load(bookimage).error(R.mipmap.b1).into(bkimage);
            bkquantity.setText(quantity);
            bkprice.setText(price);
        }
    }
}
