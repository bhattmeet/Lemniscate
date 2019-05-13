package com.example.asus.lemniscate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class Crtbk_fragment extends Fragment {
    public static Crtbk_fragment newInstance(){
        Crtbk_fragment fragment = new Crtbk_fragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.crtbk_fragment,container,false);

        ImageView crtbk = view.findViewById(R.id.crt_bk);
        ImageView crtebk = view.findViewById(R.id.crt_ebk);
        ImageView cart = view.findViewById(R.id.iv_cart);
        ImageView user = view.findViewById(R.id.iv_profileimg);

        crtbk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Crtbk_format.class);
                startActivity(intent);
            }
        });
        crtebk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Ebk_infofill.class);
                startActivity(intent);
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),Cart.class));
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),User_Profile.class));
            }
        });

        return view;
    }
}
