package com.example.asus.lemniscate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class Login extends AppCompatActivity {
    private EditText met_email, met_pass;
    private ProgressBar mprogressBar;
    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener mauthlistener;
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "Login_Activity";
    private GoogleApiClient mGoogleApiClient;
    private static Pattern EmailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
    private static Pattern PasswordPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mauth = FirebaseAuth.getInstance();

        met_email = (EditText) findViewById(R.id.et_email);
        met_pass = (EditText) findViewById(R.id.et_pass);
        final Button mbtn_signup = (Button) findViewById(R.id.btn_signup);
        final Button mbtn_forgotpass = (Button) findViewById(R.id.btn_forgotpass);
        final Button mbtn_login = (Button) findViewById(R.id.btn_login);
        final Button mbtn_google = (Button) findViewById(R.id.btn_google);
        mprogressBar = (ProgressBar)findViewById(R.id.progressbar);

        mauthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null) {

                    startActivity(new Intent(Login.this, Home.class));
                }
            }
        };

        mbtn_login.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                SignIn();
            }
        });
        mbtn_forgotpass.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                startActivity(new Intent(Login.this,Forgotpassword.class));
                finish();
            }
        });
        mbtn_signup.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                startActivity(new Intent(Login.this,Signup.class));
                finish();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(Login.this, "Connection Problem", Toast.LENGTH_SHORT).show();
            }
        })
        .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        mbtn_google.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                signIn();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        mauth.addAuthStateListener(mauthlistener);
        mprogressBar.setVisibility(android.view.View.GONE);
    }

    private void SignIn() {
        final String email = met_email.getText().toString();
        final String password = met_pass.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {

            Toast.makeText(Login.this, "Fields Empty", Toast.LENGTH_SHORT).show();
        } else {
            mauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {

                        Toast.makeText(Login.this, "Login Problem", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG,"SignInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()){
                            Log.d(TAG,"signInWithCredential",task.getException());
                            Toast.makeText(Login.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }
}