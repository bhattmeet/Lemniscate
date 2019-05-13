package com.example.asus.lemniscate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class Signup_select extends AppCompatActivity {

    private RadioGroup radioGroup;
    private EditText gener;
    private Button register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_select);

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        gener = (EditText)findViewById(R.id.et_gener);
        register = (Button) findViewById(R.id.btn_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.rb_author) {
                    startActivity(new Intent(Signup_select.this, User_Profile.class));
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rb_user) {
                    startActivity(new Intent(Signup_select.this, Home.class));
                } else {
                    Toast.makeText(Signup_select.this, "Please select any user type", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isValid() {
        String geners = gener.getText().toString().trim();

        if(TextUtils.isEmpty(geners)){
            Toast.makeText(this, "Enter Gener", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
