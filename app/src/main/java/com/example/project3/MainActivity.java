package com.example.project3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.mbms.StreamingServiceInfo;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText editemail, editpassword;
    private MaterialButton signIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        editemail = (EditText) findViewById(R.id.email);
        editpassword = (EditText) findViewById(R.id.password);

        signIn = (MaterialButton) findViewById(R.id.loginbtn);
        signIn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;

            case R.id.loginbtn:
                 userLogin();
                 break;

        }
    }

    private void userLogin() {
        String email = editemail.getText().toString().trim();
        String password = editpassword.getText().toString().trim();

        if(email.isEmpty()){
            editemail.setError("Email is required!");
            editemail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editemail.setError("Please enter valid email!");
            editemail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editpassword.setError("Password is required!");
            editpassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            editpassword.setError("6 character min, please try again!");
            editpassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                     //   redirects to dashboard
                        startActivity(new Intent(MainActivity.this, Dashboard.class));
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check email to verify your account!", Toast.LENGTH_LONG).show();
                    }
                } else {
                Toast.makeText(MainActivity.this, "Failed! Please check credentials.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}