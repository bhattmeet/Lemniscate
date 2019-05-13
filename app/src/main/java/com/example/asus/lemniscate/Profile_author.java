//package com.example.asus.lemniscate;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.google.android.material.navigation.NavigationView;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.view.GravityCompat;
//import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//
//public class Profile_author extends AppCompatActivity {
//
//    private ImageView settings,back;
//    private Button edituser,follow;
//    private NavigationView navigationView;
//    private RecyclerView recyclerView;
//    ProfileAdapter profileAdapter;
//    List<Postmodel> postmodels;
//    private FirebaseAuth.AuthStateListener authListener;
//    private FirebaseAuth auth;
//    private DatabaseReference mUserDatabase;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.author_profile);
//
//        mUserDatabase = FirebaseDatabase.getInstance().getReference("Posts");
//
//        settings = (ImageView)findViewById(R.id.iv_settings);
//        back = (ImageView)findViewById(R.id.iv_back);
//        edituser = (Button) findViewById(R.id.btn_edituser);
//        navigationView = (NavigationView) findViewById(R.id.navigationview);
//        follow = (Button) findViewById(R.id.btn_followuser);
//        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
//
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getApplicationContext(),3);
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        firebaseprofilepost();
//        postmodels = new ArrayList<>();
//        profileAdapter = new ProfileAdapter(getApplicationContext(),postmodels);
//        recyclerView.setAdapter(ProfileAdapter);
//
//        auth = FirebaseAuth.getInstance();
//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//
//        authListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user == null) {
//                    startActivity(new Intent(Profile_author.this, Login.class));
//                    finish();
//                }
//            }
//        };
//
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @SuppressLint("WrongConstant")
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                drawerLayout.closeDrawer(Gravity.END);
//                final Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.ConstraintLayout);
//                switch (menuItem.getItemId()){
//                    case R.id.account:{
//                        getSupportFragmentManager()
//                                .beginTransaction()
//                                .add(R.id.ConstraintLayout, new Account()
//                                        , Account.class.getSimpleName())
//                                .hide(currentFragment)
//                                .addToBackStack(Account.class.getSimpleName())
//                                .commit();
//                        break;
//                    }
//                    case R.id.notifications:{
//                        getSupportFragmentManager()
//                                .beginTransaction()
//                                .add(R.id.ConstraintLayout, new Notifications()
//                                        , Notifications.class.getSimpleName())
//                                .hide(currentFragment)
//                                .addToBackStack(Notifications.class.getSimpleName())
//                                .commit();
//                        break;
//                    }
//                    case R.id.invite:{
//                        getSupportFragmentManager()
//                                .beginTransaction()
//                                .add(R.id.ConstraintLayout, new Invitefriends()
//                                        , Invitefriends.class.getSimpleName())
//                                .hide(currentFragment)
//                                .addToBackStack(Invitefriends.class.getSimpleName())
//                                .commit();
//                        break;
//                    }
//                    case R.id.block:{
//                        getSupportFragmentManager()
//                                .beginTransaction()
//                                .add(R.id.ConstraintLayout, new Blocked()
//                                        , Blocked.class.getSimpleName())
//                                .hide(currentFragment)
//                                .addToBackStack(Blocked.class.getSimpleName())
//                                .commit();
//                        break;
//                    }
//                    case R.id.report:{
//                        getSupportFragmentManager()
//                                .beginTransaction()
//                                .add(R.id.ConstraintLayout, new Report()
//                                        , Report.class.getSimpleName())
//                                .hide(currentFragment)
//                                .addToBackStack(Report.class.getSimpleName())
//                                .commit();
//                        break;
//                    }
//                    case R.id.addaccnt:{
//                        startActivity(new Intent(Profile_author.this,Signup.class));
//                        break;
//                    }
//                    case R.id.signout:{
//
//                        auth.signOut();
//
//                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Profile_author.this);
//                        mBuilder.setMessage("Are you sure you want to logout?");
//                        mBuilder.setTitle("Lemniscate");
//                        mBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        });
//                        mBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                signOut();
//                                final Intent intent = new Intent(Profile_author.this, Signup.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                        });
//                        mBuilder.show();
//                        break;
//                    }
//                    case R.id.settings:{
//                        getSupportFragmentManager()
//                                .beginTransaction()
//                                .add(R.id.ConstraintLayout, new Settings()
//                                        , Settings.class.getSimpleName())
//                                .hide(currentFragment)
//                                .addToBackStack(Settings.class.getSimpleName())
//                                .commit();
//                        break;
//                    }
//                }
//                return true;
//            }
//        });
//        startActivity(new Intent(this,Profile_author.class));
//
//        settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.openDrawer(GravityCompat.END);
//            }
//        });
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Profile_author.this,Home.class));
//            }
//        });
//        edituser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Profile_author.this,EditProfile.class));
//            }
//        });
//
//    }
//    public void signOut(){
//        auth.signOut();
//    }
//
//    private void firebaseprofilepost(){
//        FirebaseRecyclerAdapter<Postmodel,ProfileViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Postmodel, ProfileViewHolder>(
//                Postmodel.class,
//                R.layout.post_item,
//                ProfileViewHolder.class,
//                mUserDatabase
//        ) {
//            @Override
//            protected void populateViewHolder(ProfileViewHolder profileViewHolder, Postmodel postmodel, int i) {
//                profileViewHolder.setDetails(getApplicationContext(),postmodel.getPostimage());
//            }
//        };
//        recyclerView.setAdapter(firebaseRecyclerAdapter);
//    }
//
//    public static class ProfileViewHolder extends RecyclerView.ViewHolder{
//
//        View mview;
//        public ProfileViewHolder(@NonNull View itemView) {
//            super(itemView);
//            mview = itemView;
//        }
//
//        public void setDetails(Context ctx,String post_image){
//            SquareImageView  postimage= mview.findViewById(R.id.post_image);
//
//            Glide.with(ctx).load(post_image).into(postimage);
//        }
//    }
//
//
//    public void Posts(){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                postmodels.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Postmodel postmodel = snapshot.getValue(Postmodel.class);
//                    if (postmodel.getUsername().equals());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//}
