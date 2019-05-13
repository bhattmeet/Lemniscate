package com.example.asus.lemniscate;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class bkdetails extends AppCompatActivity {

    private ImageView bkimage;
    private TextView bkname,bkdesc,costprice;
    private NumberPicker numberPicker;
    private ImageButton cart;
    private String bookid = " ";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_details);

        bookid = getIntent().getStringExtra("pid");


        cart = findViewById(R.id.btn_add2cart);
        bkimage = findViewById(R.id.iv_bkimg);
        bkname = findViewById(R.id.tv_bkname);
        bkdesc = findViewById(R.id.tv_bkdesc);
        numberPicker = findViewById(R.id.et_numberpicker);
        costprice = findViewById(R.id.tv_costprice);
//        rentprice = findViewById(R.id.tv_rentprice);


        cart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(bkdetails.this,Cart.class));
                add2cartlist();

            }
        });

        bookDetails(bookid);
    }

    private void add2cartlist(){

        String curntTime,curntDate;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("EEE, d MMM yyyy");
        curntDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm aaa");
        curntTime = currentTime.format(calendar.getTime());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Cart");

        final HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("pid",bookid);
        hashMap.put("bookname",bkname.getText().toString());
        hashMap.put("time",curntTime);
        hashMap.put("date",curntDate);
        hashMap.put("quantity",numberPicker.getValue());
        hashMap.put("price",costprice.getText().toString());

        reference.child("Cart").child(bookid)
                .updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(bkdetails.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Cart.class));
                        }
                    }
                });

//        final CartModel model = new CartModel();
//        model.setPid(bookid);
//        model.setBookname(bkname.getText().toString());
//        model.setDate(curntDate);
//        model.setTime(curntTime);
//        model.setQuantity(numberPicker.getValue()+"");
//        model.setPrice(costprice.getText().toString());
//        model.setBookimage(String.valueOf(bkimage.getAccessibilityClassName()));
//        FirebaseDatabase.getInstance()
//                .getReference()
//                .child("Cart")
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .setValue(model)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(bkdetails.this, "Added to Cart", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(),Cart.class));
//                            finish();
//                        }
//                    }
//                });
//
    }

    private void bookDetails(String bookid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts");

        reference.child(bookid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    Postmodel model = dataSnapshot.getValue(Postmodel.class);

                    bkname.setText(dataSnapshot.child("bookname").getValue().toString());
                    bkdesc.setText(dataSnapshot.child("description").getValue().toString());
                    Glide.with(getApplicationContext()).load(dataSnapshot.child("postimage").getValue().toString()).into(bkimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
