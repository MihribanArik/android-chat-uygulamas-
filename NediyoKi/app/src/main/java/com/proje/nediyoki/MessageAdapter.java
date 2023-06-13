package com.proje.nediyoki;


import com.bumptech.glide.Glide;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Messages> messages;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageRef;

    public MessageAdapter(ArrayList<Messages> messages, Context context){
        this.context = context;
        this.messages = messages;
        storageReference = FirebaseStorage.getInstance().getReference().child("Message");
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtMessage.setText(messages.get(position).message);
        try {
            if (messages.get(position).message.toString().substring(0,9).equals("Images://")){
                holder.imgMessage.setVisibility(View.VISIBLE);
                holder.txtMessage.setVisibility(View.GONE);
                storageRef = storageReference.child(messages.get(position).message.toString().substring(9,messages.get(position).message.toString().length()));
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // URI'yi burada kullanabilirsiniz
                        String imageUrl = uri.toString();
                        Glide.with(context)
                                .load(imageUrl)
                                .into(holder.imgMessage);
                    }
                });
            }
        }catch (Exception ex){
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
        }
        holder.ben = messages.get(position).ben;
        if (holder.ben){
            //holder.txtMessage.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.layout.setBackgroundResource(R.drawable.message_style2);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtMessage, txtUserName;
        Boolean ben;
        LinearLayout layout;
        ImageView imgMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMessage = itemView.findViewById(R.id.imgMessage);
            imgMessage.setVisibility(View.GONE);
            layout = itemView.findViewById(R.id.message_item);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtUserName = itemView.findViewById(R.id.txtGonderenIsim);
        }
    }
}
