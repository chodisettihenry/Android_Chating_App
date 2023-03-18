package com.example.mychatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {


    TextInputEditText updateName;
    CircleImageView updateprofile;
    Button update;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;

    Boolean imagecontrol = false;

    Uri imageuri;
    String image;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        update = findViewById(R.id.buttonupdate);
        updateName = findViewById(R.id.inputtextnameupdate);
        updateprofile = findViewById(R.id.circleImageViewupdate);

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        getUserInfo();

        updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();

            }
        });
    }

    public void updateProfile(){
        String userName = updateName.getText().toString();
        reference.child("Users").child(user.getUid()).child("userName").setValue(userName);
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
                                    Toast.makeText(Profile.this, "write to database is sucessfull", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Profile.this, "write to database is not sucessfull", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
            });

        }
        else {
            reference.child("Users").child(auth.getUid()).child("image").setValue(image);

        }
        Intent intent = new Intent(Profile.this,MainActivity.class);
        intent.putExtra("userName",userName);
        startActivity(intent);
        finish();
    }

    public void getUserInfo()
    {
        reference.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("userName").getValue().toString();
                image = snapshot.child("image").getValue().toString();
                updateName.setText(name);
                if (image.equals("null"))
                {
                    updateprofile.setImageResource(R.drawable.baseline_account_circle_24);
                }
                else
                {
                    Picasso.get().load(image).into(updateprofile);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

}