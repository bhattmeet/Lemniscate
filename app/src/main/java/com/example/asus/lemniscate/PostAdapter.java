package com.example.asus.lemniscate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    public ImageView profile_photo,post_image,image_heart,comment_bubble,save;
    public TextView time,comment,publisher,username,loves,caption;
    public Context context;
    public List<Postmodel> postmodels;

    private FirebaseUser firebaseUser;

    public PostAdapter(Context context,List<Postmodel> postmodels){
        this.context = context;
        this.postmodels = postmodels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_feed,viewGroup,false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Postmodel postmodel = postmodels.get(i);

        Glide.with(context).load(postmodel.getPostimage()).into(viewHolder.image);

        if (postmodel.getCaption().equals("")){
            viewHolder.caption.setVisibility(View.GONE);
        }else {
            viewHolder.caption.setVisibility(View.VISIBLE);
            viewHolder.caption.setText(postmodel.getCaption());
        }
        userinfo(viewHolder.userimage,viewHolder.username,viewHolder.publisher,postmodel.getUsername());
    }

    @Override
    public int getItemCount() {
        return postmodels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView userimage,image,img_love,img_comment,img_save;
        public TextView username,loves,caption,hashtags,time,comments,publisher;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userimage = itemView.findViewById(R.id.profile_photo);
            username = itemView.findViewById(R.id.tv_username);
            image = itemView.findViewById(R.id.post_image);
            img_love = itemView.findViewById(R.id.image_heart);
            img_comment = itemView.findViewById(R.id.comment_bubble);
            img_save = itemView.findViewById(R.id.save);
            loves = itemView.findViewById(R.id.tv_loves);
            caption = itemView.findViewById(R.id.tv_caption);
            hashtags = itemView.findViewById(R.id.tv_hashtags);
            comments = itemView.findViewById(R.id.tv_comments_link);
            time = itemView.findViewById(R.id.tv_time);
        }
    }

    private void userinfo(final ImageView userimage,final TextView username,final TextView publisher,final String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                Glide.with(context).load(userModel.getUserimage()).into(userimage);
                username.setText(userModel.getFullName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
