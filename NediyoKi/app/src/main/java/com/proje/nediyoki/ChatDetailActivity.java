package com.proje.nediyoki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewDebug;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.google.type.DateTime;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatDetailActivity extends AppCompatActivity {

    private Toolbar actionbarChatDetail;
    private String friendUid, uid;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentFirebaseUser;
    DatabaseReference mDatabase;
    DatabaseReference mesagesRef;
    private ArrayList<Messages> message;
    private Bundle bundle;
    private RecyclerView recyclerView;
    private Button btnGonder, btnAdd;
    private EditText tbxMessage;

    private StorageReference storageReference;
    private StorageReference storageRef;
    private FirebaseFirestore firebaseFirestore;
    public void init(){
        actionbarChatDetail = (Toolbar) findViewById(R.id.actionbarChatDetail);
        setSupportActionBar(actionbarChatDetail);
        getSupportActionBar().setTitle("Sohbet");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bundle = getIntent().getExtras();
        if (bundle != null){
            friendUid = bundle.getString("frienduid");
        }

        firebaseAuth = FirebaseAuth.getInstance();
        currentFirebaseUser = firebaseAuth.getCurrentUser();
        uid = currentFirebaseUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mesagesRef = mDatabase.child("messages");

        message = new ArrayList<Messages>();

        recyclerView = findViewById(R.id.rcwChatDetail);
        btnGonder = findViewById(R.id.btnGonder);
        btnAdd = findViewById(R.id.btnAdd);
        tbxMessage = findViewById(R.id.tbxMessage);

        storageReference = FirebaseStorage.getInstance().getReference().child("Message");
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        init();

        mesagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                message.clear();
                for (DataSnapshot item: snapshot.getChildren()) {
                    if (item.child("gonderuid").getValue().toString().equals(uid) && item.child("aluid").getValue().toString().equals(friendUid)){
                        Messages messages = new Messages();
                        messages.message=item.child("message").getValue().toString();
                        messages.gonderuid=item.child("gonderuid").getValue().toString();
                        messages.aluid=item.child("aluid").getValue().toString();
                        if (item.child("gonderuid").getValue().toString().equals(uid)){
                            messages.ben = true;
                        }else {
                            messages.ben = false;
                        }
                        message.add(messages);
                    }
                    if (item.child("gonderuid").getValue().toString().equals(friendUid) && item.child("aluid").getValue().toString().equals(uid)){
                        Messages messages = new Messages();
                        messages.message=item.child("message").getValue().toString();
                        messages.gonderuid=item.child("gonderuid").getValue().toString();
                        messages.aluid=item.child("aluid").getValue().toString();
                        if (item.child("aluid").getValue().toString().equals(uid)){
                            messages.ben = false;
                        }else {
                            messages.ben = true;
                        }
                        message.add(messages);
                    }
                }

                MessageAdapter messageAdapter = new MessageAdapter(message, getBaseContext());

                recyclerView.setAdapter(messageAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //message.add(new Messages("Oldu","xrLEyiqjqDX1SIUUry6LEscSdj63","K9MwBiWJbVWZbSgp1AESlfkOF9p2","demo"));
        MessageAdapter messageAdapter = new MessageAdapter(message, this);
        recyclerView.setAdapter(messageAdapter);
        System.out.println("deneme");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Messages messages = new Messages(tbxMessage.getText().toString(),uid,friendUid);

                LocalDateTime simdikiZaman = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    simdikiZaman = LocalDateTime.now();
                }

                int yil = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    yil = simdikiZaman.getYear();
                }
                int ay = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    ay = simdikiZaman.getMonthValue();
                }
                int gun = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    gun = simdikiZaman.getDayOfMonth();
                }
                int saat = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    saat = simdikiZaman.getHour();
                }
                int dakika = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    dakika = simdikiZaman.getMinute();
                }
                int saniye = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    saniye = simdikiZaman.getSecond();
                }
                int milisaniye = 0; // Nanosaniye cinsinden, milisaniye için dönüşüm
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    milisaniye = simdikiZaman.getNano() / 1_000_000;
                }

                String id = String.valueOf(yil)+String.valueOf(ay)+String.valueOf(gun)+
                        String.valueOf(saat)+String.valueOf(dakika)+String.valueOf(saniye)+String.valueOf(milisaniye)+uid.concat(friendUid);
                mesagesRef.child(id).setValue(messages);
                tbxMessage.setText("");
                Toast.makeText(ChatDetailActivity.this, "Mesaj gönderildi", Toast.LENGTH_SHORT).show();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(v);
            }
        });
    }

    // Resim seçmek için butona tıklandığında çağrılan yöntem
    public void selectImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    // Resim seçimi sonucunu işlemek için çağrılan yöntem
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            //Toast.makeText(this, selectedImageUri.toString(), Toast.LENGTH_LONG).show();

            LocalDateTime simdikiZaman = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                simdikiZaman = LocalDateTime.now();
            }

            int yil = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                yil = simdikiZaman.getYear();
            }
            int ay = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                ay = simdikiZaman.getMonthValue();
            }
            int gun = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                gun = simdikiZaman.getDayOfMonth();
            }
            int saat = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                saat = simdikiZaman.getHour();
            }
            int dakika = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                dakika = simdikiZaman.getMinute();
            }
            int saniye = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                saniye = simdikiZaman.getSecond();
            }
            int milisaniye = 0; // Nanosaniye cinsinden, milisaniye için dönüşüm
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                milisaniye = simdikiZaman.getNano();
            }

            String id = String.valueOf(yil)+String.valueOf(ay)+String.valueOf(gun)+
                    String.valueOf(saat)+String.valueOf(dakika)+String.valueOf(saniye)+String.valueOf(milisaniye)+uid.concat(friendUid);

            Messages messages = new Messages("Images://".concat(id),uid,friendUid);
            tbxMessage.setText("");

            storageRef = FirebaseStorage.getInstance().getReference().child("Message").child(id);
            storageRef.putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    mesagesRef.child(id).setValue(messages);
                    Toast.makeText(ChatDetailActivity.this, "Rersim Gönderildi.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}