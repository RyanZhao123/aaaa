package com.example.comsci;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity2 extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button createAccount;
    private Button cancel;
    private FirebaseAuth mAuth;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private boolean isMale;

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
        radioGroup = findViewById(R.id.radioGroupGender);
        mAuth = FirebaseAuth.getInstance();

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAcc();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity2.this, LoginActivity.class));
            }
        });
    }

    public void createAcc(){
        String nameStr = name.getText().toString();
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();
        String confirmPasswordStr = confirmPassword.getText().toString();
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

        switch(radioButton.getText().toString()){
            case "Male":
                isMale = true;
                break;
            case "Female":
                isMale = false;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + radioButton.getText());
        }
        if(TextUtils.isEmpty(nameStr)){
            name.setError("Name cannot be empty");
            name.requestFocus();
        }
        if(TextUtils.isEmpty(emailStr))
        {
            email.setError("Email cannot be empty");
            email.requestFocus();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()){
            email.setError("Please provide valid email!");
            email.requestFocus();
        }
        else if (TextUtils.isEmpty(passwordStr))
        {
            password.setError("Password cannot be empty");
            password.requestFocus();
        } else if(TextUtils.isEmpty(confirmPasswordStr)){
            confirmPassword.setError("Please confirm password");
            confirmPassword.requestFocus();
        } else if(!confirmPasswordStr.equals(passwordStr)){
            confirmPassword.setError("Passwords do not match");
            confirmPassword.requestFocus();
        }
        else{
            mAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        User user = new User(nameStr, emailStr);
                        System.out.println(user);
                        FirebaseDatabase.getInstance("https://csia-53c8a-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users").child(FirebaseAuth.getInstance()
                                .getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegisterActivity2.this, "Verification email sent", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(RegisterActivity2.this, LoginActivity.class));
                                        }
                                        else{
                                            Toast.makeText(RegisterActivity2.this, "Verification email failed to send", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                    else{
                        Toast.makeText(RegisterActivity2.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }



    /*private void createAccount() {
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
    }*/
}