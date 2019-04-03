package com.nikhildagrawal.worktrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nikhildagrawal.worktrack.dialogs.PasswordResetDialog;
import com.nikhildagrawal.worktrack.dialogs.ResendVerificationDialog;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText mEmail;
    private TextInputEditText mPassword;
    private MaterialButton mBtnLogin;
    private MaterialButton mBtnRegister;
    private MaterialButton mBtnForgetPass;
    private MaterialButton mBtnResendEmail;
    private ProgressBar mProgressBar;
    private View mView;



    private static final String TAG = LoginActivity.class.getSimpleName();

    private FirebaseAuth.AuthStateListener mAuthStateListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initilaizeUIComponents();
        setUpFirebaseAuth();

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isEmpty(mEmail.getText().toString()) && !isEmpty(mPassword.getText().toString())){

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(mEmail.getText().toString(),mPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });


                }else{
                    Toast.makeText(LoginActivity.this, "Please enter both the fields!",Toast.LENGTH_SHORT).show();
                }


            }
        });

        mBtnResendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResendVerificationDialog dialog = new ResendVerificationDialog();
                dialog.show(getSupportFragmentManager(), "dialog_resend_email_verification");
            }
        });

        mBtnForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordResetDialog dialog = new PasswordResetDialog();
                dialog.show(getSupportFragmentManager(), "dialog_password_reset");

            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Initialize UI Components
     */
    private void initilaizeUIComponents(){

        mView = findViewById(R.id.constraint_layout);
        mEmail  = findViewById(R.id.et_login_email);
        mPassword  = findViewById(R.id.et_login_password);
        mBtnLogin = findViewById(R.id.btn_login_login);
        mBtnRegister = findViewById(R.id.btn_login_register);
        mBtnForgetPass = findViewById(R.id.btn_login_forget_pass);
        mBtnResendEmail = findViewById(R.id.btn_login_resend_email);

    }

    // set up firebase auth

    private void setUpFirebaseAuth(){
        mAuthStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user!= null){

                    if(user.isEmailVerified()){
                        Log.d(TAG,"User Logged in: "+ user.getUid());
                        Intent intent = new Intent(LoginActivity.this,TabActivity.class);
                        startActivity(intent);
                        finish();
                        Snackbar.make(mView,"Signed in as "+user.getEmail(),Snackbar.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(LoginActivity.this,"Check your inbox for verification link!!",Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().signOut();
                    }

                }else{
                    Log.d(TAG,"Authentication failed");
                }
            }
        };
    }


    @Override
    protected void onStop() {
        super.onStop();

        if(mAuthStateListner!=null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListner);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListner);

    }


    /**
     * Return true if @param 's1' matches @param 's2'
     * @param s1
     * @param s2
     * @return
     */
    private boolean doStringsMatch(String s1, String s2){
        return s1.equals(s2);
    }

    /**
     * Return true if the @param is null
     * @param string
     * @return
     */
    private boolean isEmpty(String string){
        return string.equals("");
    }


    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
