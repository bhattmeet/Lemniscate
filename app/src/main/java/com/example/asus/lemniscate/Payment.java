package com.example.asus.lemniscate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class Payment extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        final Button confirm = (Button)findViewById(R.id.btn_confirm);
        final TextView cancel = (TextView)findViewById(R.id.cancel_order);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Payment.this,Payment_done.class));
                Toast.makeText(Payment.this, "Payment Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Payment.this,Cart.class));
                Toast.makeText(Payment.this, "Payment Cancel", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
