package com.example.comsci;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private Button registerAccount;
    private ProgressBar progressBarMain;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        login = findViewById(R.id.btnLogin);
        registerAccount = findViewById(R.id.btnRegisterAccount);
        progressBarMain = findViewById(R.id.progressBarMainActivity);
        mAuth = FirebaseAuth.getInstance();

        registerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity2.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBarMain.setVisibility(View.VISIBLE);
                userLogin();
            }
        });


    }

    private void userLogin() {
        String email2 = email.getText().toString().trim();
        String password2 = password.getText().toString().trim();

        if(!Patterns.EMAIL_ADDRESS.matcher(email2).matches()){
            email.setError("Please provide valid email!");
            email.requestFocus();
            return;
        }

        if(email2.isEmpty()){
            email.setError("Please Enter Email");
            email.requestFocus();
            return;
        }


        if(password2.isEmpty())
        {
            password.setError("Please Enter Password");
            password.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email2, password2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified())
                    {
                        Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                        i.putExtra("uid", user.getUid());
                        startActivity(i);
                    }
                    else
                    {
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Please Verify Email", Toast.LENGTH_LONG).show();
                        progressBarMain.setVisibility(View.GONE);
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                    progressBarMain.setVisibility(View.GONE);
                }
            }
        });
    }

}