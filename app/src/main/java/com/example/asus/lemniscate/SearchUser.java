package com.example.asus.lemniscate;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUser extends Fragment {

    private EditText msearchfield;
    private Button msearchbtn;
    private RecyclerView mresultlist;
    private DatabaseReference mUserDatabase;

    public static Fragment newInstance() {
        SearchUser fragment = new SearchUser();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search, container, false);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("users");

        msearchbtn = view.findViewById(R.id.btn_search);
        msearchfield = view.findViewById(R.id.et_search);
        mresultlist = view.findViewById(R.id.recyclerview);
        mresultlist.setHasFixedSize(true);
        mresultlist.setLayoutManager(new LinearLayoutManager(getActivity()));

        msearchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchText = msearchfield.getText().toString();

                firebaseUserSearch(searchText);
            }
        });
        return view;
    }


    private void firebaseUserSearch(String searchText) {

        Query query = mUserDatabase.orderByChild("gener").startAt(searchText).endAt(searchText + "\uf8ff");
        FirebaseRecyclerAdapter<UserModel,UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserModel, UsersViewHolder>(
                UserModel.class,
                R.layout.user_item,
                UsersViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder,final  UserModel model, int i) {

                viewHolder.setDetails(getContext(),model.getFullName(),model.getGener(),model.getUserimage());

                final TextView tvFollow = viewHolder.mview.findViewById(R.id.tvFollowUnFollow);
                tvFollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final FollowUnFollowModel mFollowUnFollowModel= new FollowUnFollowModel();
                        mFollowUnFollowModel.setUserId(model.getId());
                        mFollowUnFollowModel.setUserName(model.getFullName());
                        FirebaseDatabase
                                .getInstance()
                                .getReference("follow")
                                .child(FirebaseAuth.getInstance().getUid())
                                .child(System.currentTimeMillis()+"")
                                .setValue(mFollowUnFollowModel)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                         if(task.isSuccessful())   {
                                             Toast.makeText(getActivity(), "Successfully followed", Toast.LENGTH_SHORT).show();
                                         }else{
                                             Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                         }
                                    }
                                });
                    }
                });
            }
        };
        mresultlist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{

        View mview;
        private TextView tvFollow;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;


        }

        public void setDetails(Context ctx,String fullName, String gener, String userimage){
            TextView user_name = mview.findViewById(R.id.username);
            TextView user_gener = mview.findViewById(R.id.gener);
            CircleImageView user_image = mview.findViewById(R.id.image_profile);

            user_name.setText(fullName);
            user_gener.setText(gener);
            Glide.with(ctx).load(userimage).error(R.mipmap.ic_launcher_round).into(user_image);
        }

    }
}
