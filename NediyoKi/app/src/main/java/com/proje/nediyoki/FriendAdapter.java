package com.proje.nediyoki;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    ArrayList<User> friends;
    Context context;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    public FriendAdapter(ArrayList<User> friends, Context context){
        this.friends = friends;
        this.context = context;

        storageReference = FirebaseStorage.getInstance().getReference().child("User");
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtUserName.setText(friends.get(position).userName);
        holder.txtDescription.setText(friends.get(position).description);
        holder.txtUid.setText(friends.get(position).uid);
        holder.imgProfile.setImageResource(R.drawable.baseline_person);

        try {
            StorageReference strRef = storageReference.child(friends.get(position).uid);
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
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtUserName, txtDescription, txtUid;
        Button btnAction;
        ImageView imgProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtUid = itemView.findViewById(R.id.txtUid);
            btnAction = itemView.findViewById(R.id.btnAction);
            imgProfile = itemView.findViewById(R.id.imgProfile);

            btnAction.setVisibility(View.GONE);
        }
    }
}
