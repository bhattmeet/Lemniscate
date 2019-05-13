package com.example.asus.lemniscate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Crtbk_format extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crtbk_format);

        final ImageView standard = (ImageView) findViewById(R.id.img_sp);
        final ImageView premium = (ImageView) findViewById(R.id.img_pb);
        final ImageView professional = (ImageView) findViewById(R.id.img_ph);
        final ImageView pocket = (ImageView) findViewById(R.id.img_pp);

        standard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Crtbk_format.this,Bk_infofill.class));
            }
        });
        premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Crtbk_format.this,Bk_infofill.class));
            }
        });
        professional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Crtbk_format.this,Bk_infofill.class));
            }
        });
        pocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Crtbk_format.this,Bk_infofill.class));
            }
        });

    }
}
