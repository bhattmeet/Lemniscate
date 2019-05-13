package com.example.asus.lemniscate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class Signup extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mauthlistener;
    private ProgressBar progressBar;
    private EditText email,password,user;
    private Button signup,login,google;
    private Spinner spineer;
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "Login_Activity";
    private GoogleApiClient mGoogleApiClient;
    private static Pattern EmailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
    private static Pattern PasswordPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        auth = FirebaseAuth.getInstance();

        login = (Button) findViewById(R.id.bt_login);
        email = (EditText)findViewById(R.id.et_email);
        password = (EditText)findViewById(R.id.et_pass);
        user = (EditText)findViewById(R.id.et_user);
        signup = (Button) findViewById(R.id.btn_signup);
        google = (Button) findViewById(R.id.btn_google);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this,Login.class));
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    final String emailid = email.getText().toString().trim();
                    final String pass = password.getText().toString().trim();
                    final String username = user.getText().toString().trim();
                    auth.createUserWithEmailAndPassword(emailid, pass)
                            .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(Signup.this,Signup_select.class));
                                        final UserModel userModel = new UserModel();
                                        userModel.setEmailid(emailid);
                                        userModel.setFullName(username);
                                        FirebaseDatabase
                                                .getInstance()
                                                .getReference()
                                                .child("users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(userModel)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(Signup.this, "Successful", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        } else {
                                                            Toast.makeText(Signup.this, "Fail", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(Signup.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(Signup.this, "Connection Problem", Toast.LENGTH_SHORT).show();
            }
        })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        google.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                signIn();
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    public boolean isValid(){
        String fname = user.getText().toString().trim();
        String emailid = email.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if(TextUtils.isEmpty(fname)){
            Toast.makeText(this, "Enter User Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (fname.length()>25){
            Toast.makeText(this, "Enter less then 25 Char", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (fname.length()==0|| emailid.length()==0||pass.length()==0){
            Toast.makeText(Signup.this, "Fiil Empty Fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(emailid)) {
            Toast.makeText(Signup.this, "Enter EmailId", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!EmailPattern.matcher(emailid).matches()) {
            Toast.makeText(Signup.this, "Invalid Email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(Signup.this, "Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!PasswordPattern.matcher(pass).matches()) {
            Toast.makeText(Signup.this, "Invalid Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }else {

            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account){

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG,"SignInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()){
                            Log.d(TAG,"signInWithCredential",task.getException());
                            Toast.makeText(Signup.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }
}
