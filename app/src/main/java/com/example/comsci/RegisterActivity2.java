package com.example.comsci;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class RegisterActivity2 extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button createAccount;
    private Button cancel;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        name = findViewById(R.id.editTextNameUpdated);
        email = findViewById(R.id.editTextEmailUpdated);
        password = findViewById(R.id.editTextPasswordUpdated);
        confirmPassword = findViewById(R.id.editTextConfirmPasswordUpdated);
        cancel = findViewById(R.id.btnCancelUpdated);
        createAccount = findViewById(R.id.btnConfirm);
        mAuth = FirebaseAuth.getInstance();

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity2.this, LoginActivity.class));
            }
        });
    }

    private void createAccount() {
        String emailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString();
        String confirmPasswordStr = confirmPassword.getText().toString();
        String nameStr = name.getText().toString();

        if(nameStr.isEmpty())
        {
            name.setError("Please Enter Name");
            name.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()){
            email.setError("Please provide valid email!");
            email.requestFocus();
            return;
        }

        if(emailStr.isEmpty()){
            email.setError("Please Enter Email");
            email.requestFocus();
            return;
        }


        if(passwordStr.isEmpty())
        {
            password.setError("Please Enter Password");
            password.requestFocus();
            return;
        }

        if(!confirmPasswordStr.equals(passwordStr))
        {
            confirmPassword.setError("Passwords Do Not Match");
            confirmPassword.requestFocus();
            return;
        }

        if(confirmPasswordStr.isEmpty())
        {
            confirmPassword.setError("Please Enter Password");
            confirmPassword.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseDatabase.getInstance("https://csia-53c8a-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users").child(FirebaseAuth.getInstance().
                        getCurrentUser().getUid()).setValue("Random String");
            }
        });
    }
}