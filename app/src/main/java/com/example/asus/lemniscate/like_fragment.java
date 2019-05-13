//package com.example.asus.lemniscate;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.List;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class like_fragment extends Fragment {
//
//    private PostAdapter postAdapter;
//    private List<Postmodel> postLists;
//    private List<String> followingList;
//    private Button search;
//    private ImageView addpost;
//    private CircleImageView profile;
//    private RecyclerView recyclerView;
//    private DatabaseReference mUserDatabase;
//
//    public static like_fragment newInstance(){
//        return new like_fragment();
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.like_fragment, container, false);
//
//
//        mUserDatabase = FirebaseDatabase.getInstance().getReference("Posts");
//
//        profile = (CircleImageView)view.findViewById(R.id.iv_profile);
//        search = (Button) view.findViewById(R.id.search_go_btn);
//        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
//        addpost = (ImageView)view.findViewById(R.id.add_post);
//
//
//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(),SearchUser.class));
//            }
//        });
//
//        profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(),User_Profile.class));
//            }
//        });
//
//        addpost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(),Post.class));
//            }
//        });
//
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        firebasepost();
////        postLists = new ArrayList<>();
////        postAdapter = new PostAdapter(getContext(),postLists);
////        recyclerView.setAdapter(postAdapter);
//
////        checkfollowing();
//
//        return view;
//    }
//
//    private void firebasepost(){
//        FirebaseRecyclerAdapter<Postmodel,PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Postmodel, PostViewHolder>(
//                Postmodel.class,
//                R.layout.row_feed,
//                PostViewHolder.class,
//                mUserDatabase) {
//            @Override
//            protected void populateViewHolder(PostViewHolder postViewHolder, Postmodel postmodel, int i) {
//                postViewHolder.setDetails(getActivity(),postmodel.getProfileimage(),postmodel.getUsername(),
//                        postmodel.getPostimage(),postmodel.getLoves(),postmodel.getHashtags(),postmodel.getCaption(),
//                        postmodel.getTime());
//            }
//        };
//        recyclerView.setAdapter(firebaseRecyclerAdapter);
//    }
//
//    public static class PostViewHolder extends RecyclerView.ViewHolder{
//
//        View mview;
//        public PostViewHolder(@NonNull View itemView) {
//            super(itemView);
//            mview = itemView;
//        }
//
//        public void setDetails(Context ctx, String profile_photo, String tv_username, String post_image,
//                               String tv_loves,String tv_hashtags,String tv_caption,String tv_time){
//            TextView user_name = mview.findViewById(R.id.tv_username);
//            TextView loves = mview.findViewById(R.id.tv_loves);
//            TextView caption = mview.findViewById(R.id.tv_caption);
//            TextView hashtags = mview.findViewById(R.id.tv_hashtags);
//            TextView time = mview.findViewById(R.id.tv_time);
//            ImageView  postimage= mview.findViewById(R.id.post_image);
//            CircleImageView profilephoto = mview.findViewById(R.id.profile_photo);
//
//            user_name.setText(tv_username);
//            loves.setText(tv_loves);
//            caption.setText(tv_caption);
//            hashtags.setText(tv_hashtags);
//            time.setText(tv_time);
//            Glide.with(ctx).load(post_image).into(postimage);
//            Glide.with(ctx).load(profile_photo).into(profilephoto);
//        }
//    }
//
////    private void checkfollowing(){
////        followingList = new ArrayList<>();
////
////        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow")
////                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
////                .child("following");
////
////        reference.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                followingList.clear();
////               for (DataSnapshot snapshot : dataSnapshot.getChildren()){
////                   followingList.add(snapshot.getKey());
////               }
////                readpost();
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError) {
////
////            }
////        });
////    }
//
////    private void readpost(){
////        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts");
////
////        reference.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                postLists.clear();
////                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
////                    Postmodel postmodel = snapshot.getValue(Postmodel.class);
////                    for (String id : followingList){
////                        assert postmodel != null;
////                        if (postmodel.getPublisher().equals(id)){
////                            postLists.add(postmodel);
////                        }
////                    }
////                }
////                postAdapter.notifyDataSetChanged();
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError) {
////
////            }
////        });
////    }
//
//
//}
//
//
