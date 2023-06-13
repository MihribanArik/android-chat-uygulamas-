package com.proje.nediyoki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {

    Toolbar actionbar;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String uid;

    EditText tbxUserName, tbxEmail, tbxDescription;
    Button btnGonder;
    ImageView imageView;
    DatabaseReference mDatabase;
    DatabaseReference usersRef;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    public void init(){
        actionbar = (Toolbar) findViewById(R.id.actionbarProfile);
        setSupportActionBar(actionbar);
        getSupportActionBar().setTitle(R.string.nediyo_ki);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        uid = firebaseUser.getUid();

        tbxUserName = (EditText) findViewById(R.id.tbxUserName);
        tbxEmail = (EditText) findViewById(R.id.tbxEmail);
        tbxDescription = (EditText) findViewById(R.id.tbxDescription);
        btnGonder = (Button) findViewById(R.id.btnGonder);
        imageView = (ImageView) findViewById(R.id.imgProfile);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        usersRef = mDatabase.child("users");

        storageReference = FirebaseStorage.getInstance().getReference().child("User");
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = storageReference.child(uid);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageUrl = uri.toString();

                Glide.with(ProfileActivity.this)
                        .load(imageUrl)
                        .into(imageView);
            }
        });

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                for (DataSnapshot user: snapshot.getChildren()) {
                    if (user.child("uid").getValue().toString().equals(uid)){
                        tbxUserName.setText(user.child("userName").getValue().toString());
                        tbxEmail.setText(user.child("email").getValue().toString());
                        tbxEmail.setEnabled(false);
                        tbxDescription.setText(user.child("description").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Bir hata oluştu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();


            storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Toast.makeText(ProfileActivity.this, "Rersim Yüklendi.", Toast.LENGTH_SHORT).show();
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();

                            Glide.with(ProfileActivity.this)
                                    .load(imageUrl)
                                    .into(imageView);
                        }
                    });
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();

        imageView.setOnClickListener(view -> pickImage());

        btnGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(uid,tbxUserName.getText().toString(), tbxEmail.getText().toString(), tbxDescription.getText().toString());
                usersRef.child(uid).setValue(user);
                Toast.makeText(ProfileActivity.this, "Başarılı bir şekilde güncellendi.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}