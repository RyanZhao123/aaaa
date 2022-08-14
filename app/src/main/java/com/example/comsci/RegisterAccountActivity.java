package com.example.comsci;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterAccountActivity extends AppCompatActivity {
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button createAccount;
    private Button cancel;
    private FirebaseAuth mAuth;
    //private FirebaseDatabase rootNode;
    //private DatabaseReference reference;
    private ProgressBar progressBar;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private boolean isMale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);
        name = findViewById(R.id.editTextName);
        email = findViewById(R.id.editTextEmail2);
        password = findViewById(R.id.editTextPassword2);
        confirmPassword = findViewById(R.id.editTextConfirmPassword);
        createAccount = findViewById(R.id.btnCreateAccount);
        cancel = findViewById(R.id.btnCancel);
        progressBar = findViewById(R.id.progressBar2);
        radioGroup = findViewById(R.id.radioGroup);
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword("justinlu@gmail.com", "1234");



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterAccountActivity.this, LoginActivity.class));
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        //rootNode = FirebaseDatabase.getInstance();
        //reference = rootNode.getReference("users");

        String name2 = name.getText().toString().trim();
        String email2 = email.getText().toString().trim();
        String password2 = password.getText().toString().trim();
        String confirmPassword2 = confirmPassword.getText().toString().trim();
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

        if(name2.isEmpty())
        {
            name.setError("Please Enter Name");
            name.requestFocus();
            return;
        }

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

        if(!confirmPassword2.equals(password2))
        {
            confirmPassword.setError("Passwords Do Not Match");
            confirmPassword.requestFocus();
            return;
        }

        if(confirmPassword2.isEmpty())
        {
            confirmPassword.setError("Please Enter Password");
            confirmPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        /*User user = new User(name2, email2, isMale);

        reference.child(name2).setValue(user);*/
        mAuth.createUserWithEmailAndPassword(email2, password2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    User user = new User(name2, email2);
                    //have to fix random string
                    FirebaseDatabase.getInstance("https://csia-53c8a-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users").child(FirebaseAuth.getInstance().
                            getCurrentUser().getUid()).setValue("Random String").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                Toast.makeText(RegisterAccountActivity.this, "User has been registered successfully. Check your email to verify your account.", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(RegisterAccountActivity.this, LoginActivity.class));
                            }
                            else{
                                Toast.makeText(RegisterAccountActivity.this, "1", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RegisterAccountActivity.this, "2", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}