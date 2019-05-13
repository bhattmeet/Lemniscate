package com.example.asus.lemniscate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class User_Profile extends AppCompatActivity {

    private final static String TAG="UserProfile";

    ImageView backbtn,setting;
    TextView name,gener,appreciations,appre,follow,followers,following,followings;
    Button edituser;
    ImageView userimage;

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private String UserID;
    private UserModel userModel;
    RecyclerView recyclerView;

    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        Log.d(TAG, "onCreate: in userProfile");
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
        UserID=firebaseUser.getUid();

        setContentView(R.layout.user_profile);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        userimage = (CircleImageView)findViewById(R.id.profile_img);
        backbtn = (ImageView) findViewById(R.id.back_btn);
        setting = (ImageView) findViewById(R.id.settings);
        name = (TextView) findViewById(R.id.tv_name);
        gener = (TextView) findViewById(R.id.tv_gener);
        appreciations = (TextView) findViewById(R.id.tv_appreciations);
        appre = (TextView) findViewById(R.id.tv_appre);
        follow = (TextView) findViewById(R.id.tv_follow);
        followers = (TextView) findViewById(R.id.tv_followers);
        following = (TextView) findViewById(R.id.tv_following);
        followings = (TextView) findViewById(R.id.tv_followings);
        edituser = (Button) findViewById(R.id.edit);
        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.navigationview);
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mUserDatabase = FirebaseDatabase.getInstance().getReference("Posts");

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(User_Profile.this, Signup.class));
                    finish();
                }
            }
        };
        getFollow();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawer(GravityCompat.END);
                switch (menuItem.getItemId()){
                    case R.id.account:{
                        startActivity(new Intent(User_Profile.this,Account.class));
                        finish();
                        break;
                    }
                    case R.id.notifications:{
                        startActivity(new Intent(User_Profile.this,Notifications.class));
                        finish();
                        break;
                    }
                    case R.id.invite:{
                        startActivity(new Intent(User_Profile.this,Invitefriends.class));
                        finish();
                        break;
                    }
                    case R.id.block:{
                        startActivity(new Intent(User_Profile.this,Blocked.class));
                        finish();
                        break;
                    }
                    case R.id.report:{
                        startActivity(new Intent(User_Profile.this, Report.class));
                        finish();
                        break;
                    }
                    case R.id.addaccnt:{
                        startActivity(new Intent(User_Profile.this,Signup.class));
                        break;
                    }
                    case R.id.signout:{

                        auth.signOut();

                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(User_Profile.this);
                        mBuilder.setMessage("Are U sure 2 Logout?");
                        mBuilder.setTitle("Lemniscate");
                        mBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        mBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                signOut();
                                final Intent intent = new Intent(User_Profile.this, Signup.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        mBuilder.show();
                        break;
                    }
                    case R.id.settings:{
                        startActivity(new Intent(User_Profile.this,Settings.class));
                        finish();
                        break;
                    }
                }


                return true;
            }
        });


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_Profile.this,Home.class));
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
        edituser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_Profile.this,EditProfile.class));
            }
        });
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));

        firebasepost();
        userInfo();
//        getFollowers();
        getPosts();
    }

    private void firebasepost(){
        FirebaseRecyclerAdapter<Postmodel,PostsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Postmodel, PostsViewHolder>(
                Postmodel.class,
                R.layout.post_item,
                PostsViewHolder.class,
                mUserDatabase
        ) {
            @Override
            protected void populateViewHolder(PostsViewHolder viewHolder, Postmodel model, int i) {

                viewHolder.setDetails(getApplicationContext(),model.getPostimage());

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder{

        View mview;
        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;
        }

        public void setDetails(Context ctx,String postimage){
            ImageView post_image = mview.findViewById(R.id.post_image);

            Glide.with(ctx).load(postimage).error(R.color.colorPrimaryDark).into(post_image);
        }

    }
    public void signOut(){
        auth.signOut();
    }

    public void userInfo(){
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mUserDatabase.child(UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                name.setText(dataSnapshot.child("fullName").getValue().toString());
                gener.setText(dataSnapshot.child("gener").getValue().toString());
                Glide.with(getApplicationContext()).load(dataSnapshot.child("userimage").getValue().toString()).error(R.mipmap.ic_launcher_round).into(userimage);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPosts(){
        FirebaseDatabase.getInstance()
                .getReference("Posts")
                .child("postid")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int count=3;
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            count++;
                            final Postmodel model = dataSnapshot1.getValue(Postmodel.class);
                        }
                        appre.setText(""+count);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    private void getFollow(){
        FirebaseDatabase.getInstance()
                .getReference("follow")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int count = 0;
                        for (DataSnapshot mData : dataSnapshot.getChildren()){
                            count++;
                            final FollowUnFollowModel model = mData.getValue(FollowUnFollowModel.class);
                        }
                        followers.setText(""+count);
//                        Toast.makeText(User_Profile.this, "Count"+count, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}

