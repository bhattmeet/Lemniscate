package com.example.asus.lemniscate;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfile extends AppCompatActivity {

    private ImageView cancel, save, userimage;
    private TextView username, gener, website, email, number, gender;
    FirebaseUser firebaseUser;
    private Uri mimageuri;
    private StorageTask storageTask;
    StorageReference storageRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        cancel = (ImageView) findViewById(R.id.cancel);
        save = (ImageView) findViewById(R.id.right);
        userimage = (ImageView) findViewById(R.id.user_img);
        username = (TextView) findViewById(R.id.et_user);
        gener = (TextView) findViewById(R.id.et_gener);
        website = (TextView) findViewById(R.id.et_website);
        email = (TextView) findViewById(R.id.et_email);
        number = (TextView) findViewById(R.id.et_number);
        gender = (TextView) findViewById(R.id.et_gender);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference("users");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                username.setText(userModel.getFullName());
                email.setText(userModel.getEmailid());
                gener.setText(userModel.getGener());
                gender.setText(userModel.getGender());
                website.setText(userModel.getWebsite());
                number.setText(userModel.getNumber());
                Glide.with(getApplicationContext()).load(userModel.getUserimage()).error(R.mipmap.ic_launcher_round).into(userimage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(EditProfile.this);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfile.this,User_Profile.class));
                Toast.makeText(EditProfile.this, "Update Cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(username.getText().toString(),gener.getText().toString(),email.getText().toString(),gender.getText().toString(),number.getText().toString(),website.getText().toString());
                startActivity(new Intent(EditProfile.this,User_Profile.class));
                Toast.makeText(EditProfile.this, "Update Successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void updateProfile(String username, String gener, String email,String gender,String number,String website) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("username", username);
        hashMap.put("gener", gener);
        hashMap.put("email", email);

        reference.updateChildren(hashMap);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading");
        progressDialog.show();

        if (mimageuri != null) {
            final StorageReference filereference = storageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mimageuri));
            storageTask = filereference.putFile(mimageuri);

            storageTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return filereference.getDownloadUrl();

                }

            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = (Uri) task.getResult();
                        String myUrl = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("userimage", ""+myUrl);

                        reference.updateChildren(hashMap);
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(EditProfile.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(EditProfile.this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mimageuri = result.getUri();
            uploadImage();
        }else {
            Toast.makeText(this, "Something goes Wrong!", Toast.LENGTH_SHORT).show();
        }
    }
}