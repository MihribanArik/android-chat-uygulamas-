package com.proje.nediyoki;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder>{

    Context context;
    ArrayList<User> users;
    ArrayList<String> friends;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    String uid;
    DatabaseReference mDatabase;
    DatabaseReference friendsRef;

    public RequestAdapter(ArrayList<User> users, ArrayList<String> friends, Context context){
        this.users = users;
        this.context = context;
        this.friends = friends;

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        uid = currentUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        friendsRef = mDatabase.child("friends");

        storageReference = FirebaseStorage.getInstance().getReference().child("User");
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtUserName.setText(users.get(position).userName);
        holder.txtDescription.setText(users.get(position).description);
        holder.txtUid.setText(users.get(position).uid);
        holder.imgProfile.setImageResource(R.drawable.baseline_person);


        Boolean isFriend= false;
        String uidFriend = holder.txtUid.getText().toString();

        try {
            StorageReference strRef = storageReference.child(users.get(position).uid);
            strRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String imageUrl = uri.toString();

                    Glide.with(context)
                            .load(imageUrl)
                            .into(holder.imgProfile);
                }
            });
        }catch (Exception ex){

        }

        for(String i: friends){
            if (uidFriend.equals(i)){
                isFriend = true;
                holder.btnAction.setText("Arkadaşsınız");
                holder.btnAction.setEnabled(false);
                holder.btnAction.setTextColor(Color.BLACK);
            }
        }
        if (! isFriend) {
            holder.btnAction.setText("Arkadaş Ekle");
            holder.btnAction.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try{
                        holder.btnAction.setEnabled(false);
                        holder.btnAction.setText("Arkadaşsınız");
                        holder.btnAction.setTextColor(Color.BLACK);

                        Friend friend = new Friend(uidFriend.concat(uid), uid, uidFriend);
                        friendsRef.child(uidFriend.concat(uid)).setValue(friend).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Arkadaş Eklendi", Toast.LENGTH_SHORT).show();
                                } else {
                                    holder.btnAction.setEnabled(true);
                                    holder.btnAction.setText("Arkadaş Ekle");
                                    holder.btnAction.setTextColor(Color.WHITE);
                                    holder.btnAction.setTextColor(Color.WHITE);
                                    Toast.makeText(context, "Bir Hata Oluştu.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }catch (Exception ex){
                        Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtDescription, txtUid;
        Button btnAction;
        ImageView imgProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = (ImageView) itemView.findViewById(R.id.imgProfile);
            btnAction = (Button) itemView.findViewById(R.id.btnAction);
            txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
            txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
            txtUid = (TextView) itemView.findViewById(R.id.txtUid);
        }
    }
}
