package com.example.mychatapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder> {

    List<String> userList;
    String userName;
    Context mContext;

    FirebaseDatabase database;
    DatabaseReference reference;

    public UserAdapter(List<String> userList, String userName, Context mContext) {
        this.userList = userList;
        this.userName = userName;
        this.mContext = mContext;

        database= FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        reference.child("Users").child(userList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String otherName = snapshot.child("userName").getValue().toString();
                String imageURL = snapshot.child("image").getValue().toString();

                holder.textViewusers.setText(otherName);

                if (imageURL.equals("null"))
                {
                    holder.usersimage.setImageResource(R.drawable.baseline_account_circle_24);
                }
                else
                {
                    Picasso.get().load(imageURL).into(holder.usersimage);
                }
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext,MyChatActivity.class);
                        intent.putExtra("userName",userName);
                        intent.putExtra("otherName",otherName);
                        mContext.startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        private TextView textViewusers;
        private CircleImageView usersimage;
        private CardView cardView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            textViewusers = itemView.findViewById(R.id.textViewusers);
            usersimage = itemView.findViewById(R.id.imageviewcircleusers);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
}
