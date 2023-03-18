package com.example.mychatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Signup_Activity extends AppCompatActivity {

    private TextInputEditText  signupemail,signuppassword,name;
    private Button btsSignup;
    private CircleImageView circleprofile;

    FirebaseAuth auth ;
    FirebaseDatabase database;
    DatabaseReference reference;
    Boolean imagecontrol = false;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Uri imageuri;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupemail = findViewById(R.id.inputtextsignupemail);
        signuppassword = findViewById(R.id.inputtextsignuppassword);
        name = findViewById(R.id.inputtextnameupdate);
        circleprofile = findViewById(R.id.circleImageViewupdate);
        btsSignup = findViewById(R.id.buttonupdate);

        auth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();



        btsSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usersignupEmail = signupemail.getText().toString();
                String usersignuppasswordsignup =signuppassword.getText().toString();
                String userName =name.getText().toString();

                if(!usersignupEmail.equals("")&&!usersignuppasswordsignup.equals("")&&!userName.equals(""))
                {
                    signupwithfirebase(usersignupEmail,usersignuppasswordsignup,userName);
                }
                else {
                    Toast.makeText(Signup_Activity.this, "please enterted the details ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        circleprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();

            }
        });



    }

    public void imageChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==1 && resultCode==RESULT_OK && data!=null)
        {
            imageuri = data.getData();
            Picasso.get().load(imageuri).into(circleprofile);
            imagecontrol=true;
        }
        else {
            imagecontrol = false;
        }
    }
    public void signupwithfirebase(String usersignupEmail,String usersignuppasswordsignup,final String userName)
    {
        auth.createUserWithEmailAndPassword(usersignupEmail,usersignuppasswordsignup).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    reference.child("Users").child(auth.getUid()).child("userName").setValue(userName);

                    if (imagecontrol){

                        UUID randomID = UUID.randomUUID();
                        final String imagename = "images/"+randomID+".jpg";
                        storageReference.child(imagename).putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                StorageReference mystorageref = firebaseStorage.getReference(imagename);
                                mystorageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String filepath = uri.toString();
                                        reference.child("Users").child(auth.getUid()).child("image").setValue(filepath).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(Signup_Activity.this, "write to database is sucessfull", Toast.LENGTH_LONG).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Signup_Activity.this, "write to database is not sucessfull", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                            }
                        });

                    }
                    else {
                        reference.child("Users").child(auth.getUid()).child("image").setValue("null");

                    }
                    Intent intent = new Intent(Signup_Activity.this,MainActivity.class);

                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(Signup_Activity.this, "there is a problem", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}