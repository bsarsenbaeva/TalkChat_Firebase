package com.example.admin.talkchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextInputLayout mLogEmail;
    private TextInputLayout mLogPassword;
    private Button mLogBtn;

    private ProgressDialog mLogProgress;

    //Firebase Auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = findViewById(R.id.log_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("TalkChat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLogProgress = new ProgressDialog(this);

        mLogEmail = findViewById(R.id.log_email);
        mLogPassword = findViewById(R.id.log_password);
        mLogBtn = findViewById(R.id.login_btn);

        mLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mLogEmail.getEditText().getText().toString();
                String password = mLogPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(email)|| !TextUtils.isEmpty(password)){

                    mLogProgress.setTitle("Вход");
                    mLogProgress.setMessage("Пожалуйста подождите");
                   mLogProgress.setCanceledOnTouchOutside(false);
                   mLogProgress.show();

                    loginUser(email,password);
                }
            }
        });

    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    mLogProgress.dismiss();

                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }else{
                    mLogProgress.hide();

                    Toast.makeText(LoginActivity.this, "Cannot Sign in", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
