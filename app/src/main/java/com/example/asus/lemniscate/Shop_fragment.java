package com.example.asus.lemniscate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Shop_fragment extends Fragment {


    private RecyclerView mresultlist;
    private DatabaseReference mUserDatabase;

    public static Shop_fragment newInstance(){
        Shop_fragment fragment = new Shop_fragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shop_fragment,container,false);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("Posts");

        ImageView cart = view.findViewById(R.id.iv_cart);
        ImageView user = view.findViewById(R.id.iv_profile);
        mresultlist = view.findViewById(R.id.recyclerview);
        mresultlist.setHasFixedSize(true);
        mresultlist.setLayoutManager(new LinearLayoutManager(getActivity()));

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Cart.class);
                startActivity(intent);
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),User_Profile.class));
            }
        });


        String searchBook = null;
        firebaseshopitems(searchBook);

        return view;
    }

    private void firebaseshopitems(String searchBook){

        Query query = mUserDatabase.orderByChild("bookname").startAt(searchBook).endAt(searchBook + "\uf8ff");
        FirebaseRecyclerAdapter<Postmodel,PostsShopViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Postmodel, PostsShopViewHolder>(
                Postmodel.class,
                R.layout.shop_row_item,
                PostsShopViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(PostsShopViewHolder ViewHolder, final Postmodel postmodel, int i) {

                ViewHolder.setDetails(getContext(),postmodel.getUsername(),postmodel.getPostimage(),postmodel.getBookname());
                ViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),bkdetails.class);
                        intent.putExtra("pid",postmodel.getPostid());
                        startActivity(intent);
                    }
                });
            }
        };
        mresultlist.setAdapter(firebaseRecyclerAdapter);
    }
    public static class PostsShopViewHolder extends RecyclerView.ViewHolder{

        View mview;
        public PostsShopViewHolder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;
        }

        public void setDetails(Context ctx, String username, String postimage, String bookname){
            TextView book_name = mview.findViewById(R.id.bkname);
            TextView user_name = mview.findViewById(R.id.authorname);
            ImageView post_image = mview.findViewById(R.id.img_bk);

            user_name.setText(username);
            book_name.setText(bookname);
            Glide.with(ctx).load(postimage).error(R.mipmap.ic_launcher_round).into(post_image);
        }
    }
}
