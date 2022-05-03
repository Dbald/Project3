package com.example.project3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private TextView editheader, registerUser;
    private EditText editfullName, editbirthdate, editemail, editusername, editpassword;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        editheader = (TextView) findViewById(R.id.header);
        editheader.setOnClickListener(this);

        registerUser = (MaterialButton) findViewById(R.id.registeruser);
        registerUser.setOnClickListener(this);

        editfullName = (EditText) findViewById(R.id.fullname);
        editbirthdate = (EditText) findViewById(R.id.birthdate);
        editemail = (EditText)  findViewById(R.id.email);
        editusername = (EditText) findViewById(R.id.username);
        editpassword = (EditText) findViewById(R.id.password);

    }

    @Override
    public void onClick(View view) {
    switch (view.getId()){
        case R.id.header:
            startActivity(new Intent(this, MainActivity.class));
            break;
        case R.id.registeruser:
            registerUser();
            break;
        }
    }

    private void registerUser() {

        String email = editemail.getText().toString().trim();
        String username = editusername.getText().toString().trim();
        String password = editpassword.getText().toString().trim();
        String fullName = editfullName.getText().toString().trim();
        String dob = editbirthdate.getText().toString().trim();

        if (fullName.isEmpty()){
            editfullName.setError("Full name is required!");
            editfullName.requestFocus();
            return;
        }

        if (dob.isEmpty()){
            editbirthdate.setError("Date of Birth is required!");
            editbirthdate.requestFocus();
            return;
        }

        if (email.isEmpty()){
            editemail.setError("Email is required!");
            editemail.requestFocus();
            return;
        }

        // Checks for the validity of the email address
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editemail.setError("Please provide valid email!");
            editemail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            editpassword.setError("Password is required");
            editpassword.requestFocus();
            return;
        }

        if (password.length() < 6){
            editpassword.setError("6 character minimum, please try again.");
            editpassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        User user = new User(fullName, dob, username, email);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                               if(task.isSuccessful()){
                                   Toast.makeText(RegisterUser.this, "Registration Successful!", Toast.LENGTH_LONG).show();
//                                   redirect to login screen
                               } else {
                                   Toast.makeText(RegisterUser.this, "Registration Failed.", Toast.LENGTH_LONG).show();
                               }
                            }
                        });

                    } else {
                        Toast.makeText(RegisterUser.this, "Registration Failed. Please try again!", Toast.LENGTH_LONG).show();
                    }
                }
            });

    }
}
