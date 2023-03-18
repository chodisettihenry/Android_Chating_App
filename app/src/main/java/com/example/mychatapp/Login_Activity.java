package com.example.mychatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {

    private TextInputEditText emailid,password;
    private Button signin,signup;
    private TextView forgotpassword;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null)
        {
            Intent intent = new Intent(Login_Activity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailid = findViewById(R.id.inputtextemail);
        password = findViewById(R.id.inputtextpassword);
        signin = findViewById(R.id.buttonsignin);
        signup = findViewById(R.id.buttonsignup);
        forgotpassword = findViewById(R.id.textViewforgot);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userEmail = emailid.getText().toString();
                String userpasswordsignup =password.getText().toString();

                if(!userEmail.equals("")&&!userpasswordsignup.equals(""))
                {
                    signInLoginInWithFirebase(userEmail,userpasswordsignup);
                }
                else {
                    Toast.makeText(Login_Activity.this, "please enter email and password", Toast.LENGTH_LONG).show();
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Login_Activity.this,Signup_Activity.class);
                startActivity(intent);

            }
        });
    }

    public void signInLoginInWithFirebase(String userEmail,String userpasswordsignup)
    {
         auth.signInWithEmailAndPassword(userEmail,userpasswordsignup).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(Login_Activity.this, "sucessfully register", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Login_Activity.this,MainActivity.class);
                    startActivity(i);

                }
                else {
                    Toast.makeText(Login_Activity.this, "sorry is not register sucessfully", Toast.LENGTH_LONG).show();
                }


            }
        });


    }


}