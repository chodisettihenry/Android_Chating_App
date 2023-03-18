package com.example.mychatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private TextInputEditText Emailidreset;
    private Button btsPassword;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Emailidreset = findViewById(R.id.textinputReset);
        btsPassword = findViewById(R.id.buttoncontinue);

        btsPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmailidpassword = Emailidreset.getText().toString();
                if (!Emailidreset.equals(""))
                {
                    resetPassword(userEmailidpassword);
                }
            }
        });

        auth = FirebaseAuth.getInstance();
    }

    public void resetPassword(String userEmailidpassword){

        auth.sendPasswordResetEmail(userEmailidpassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(ResetPassword.this, "Please check your email for reset password", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(ResetPassword.this, "please check your emailid ", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}