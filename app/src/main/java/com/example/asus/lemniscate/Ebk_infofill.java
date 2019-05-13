package com.example.asus.lemniscate;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class Ebk_infofill extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    FirebaseAuth auth;
    private EditText username,email,author,title;
    private Button uploadfile,uploadcover,submit;
    private TextView tvuploadfile,tvuploadcover;
    Uri epubUri;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ebk_infofill);

        username = (EditText) findViewById(R.id.et_user);
        email = (EditText) findViewById(R.id.et_email);
        author = (EditText) findViewById(R.id.et_author);
        title = (EditText) findViewById(R.id.et_title);
        uploadfile = (Button)findViewById(R.id.upload_file);
        tvuploadfile = (TextView)findViewById(R.id.tv_uploadfile);
        uploadcover = (Button)findViewById(R.id.upload_cover);
        tvuploadcover = (TextView)findViewById(R.id.tv_uploadcover);
        submit = (Button)findViewById(R.id.submit);

        firebaseDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl("https://lemniscate-b5619.firebaseio.com/");

        uploadfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Ebk_infofill.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){

                }else {
                    ActivityCompat.requestPermissions(Ebk_infofill.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
                }
            }
        });

        uploadcover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Ebk_infofill.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){

                }else {
                    ActivityCompat.requestPermissions(Ebk_infofill.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    final Ebkinfo_model ebkinfo_model = new Ebkinfo_model();
                    ebkinfo_model.setUsername(username.getText().toString().trim());
                    ebkinfo_model.setAuthor(author.getText().toString().trim());
                    ebkinfo_model.setEmail(email.getText().toString().trim());
                    ebkinfo_model.setTitle(title.getText().toString().trim());
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("E-Book_Information_Form")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(ebkinfo_model)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Ebk_infofill.this, "Successful", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(Ebk_infofill.this, "Fail", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                if (epubUri!=null){
                    uploadfiles(epubUri);
                    uploadcover(epubUri);
                }else {
                    Toast.makeText(Ebk_infofill.this, "Select a File", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Upload file
    private void uploadfiles(Uri pdfUri) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File...");
        progressDialog.setProgressStyle(0);
        progressDialog.show();

        final String filename = System.currentTimeMillis()+"";
        StorageReference storageReference = storage.getReference();

        storageReference.child("Book_Information_Form").child(filename).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        String url = taskSnapshot.getUploadSessionUri().toString();
                        DatabaseReference reference = firebaseDatabase.getReference();

                        reference.child("Book_Information_Form").child(filename).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(Ebk_infofill.this, "File Successfully Updated", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(Ebk_infofill.this, "File not successfully Updated", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Ebk_infofill.this, "File not successfully Updated", Toast.LENGTH_SHORT);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgress = (int)(100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            selectEPUB();
        }else {
            Toast.makeText(this, "Please provide permission..", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectEPUB() {
        Intent intent = new Intent();
        intent.setType("application/EPUB");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 86 && resultCode == RESULT_OK && data!=null){
            epubUri = data.getData();
            tvuploadfile.setText("File Selected");
        }else {
            Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == 86 && resultCode == RESULT_OK && data!=null){
            epubUri = data.getData();
            tvuploadcover.setText("File Selected");
            Toast.makeText(this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }
    ///////
    //Upload Cover Page
    private void uploadcover(Uri pdfUri) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File...");
        progressDialog.setProgressStyle(0);
        progressDialog.show();

        final String filename = System.currentTimeMillis()+"";
        StorageReference storageReference = storage.getReference();

        storageReference.child("Book_Information_Form").child(filename).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        String url = taskSnapshot.getUploadSessionUri().toString();
                        DatabaseReference reference = firebaseDatabase.getReference();

                        reference.child("Book_Information_Form").child(filename).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(Ebk_infofill.this, "File Successfully Updated", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(Ebk_infofill.this, "File not successfully Updated", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Ebk_infofill.this, "File not successfully Updated", Toast.LENGTH_SHORT);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgress = (int)(100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });

    }


    public boolean isValid(){
        String mtitle = title.getText().toString().trim();
        String mauthor = author.getText().toString().trim();
        String memail = email.getText().toString().trim();
        String musername = username.getText().toString().trim();

        if(TextUtils.isEmpty(mtitle)){
            Toast.makeText(this, "Enter Book Title", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(mauthor)){
            Toast.makeText(this, "Enter Author", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(memail)) {
            Toast.makeText(this, "Enter Email-id", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(musername)){
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
