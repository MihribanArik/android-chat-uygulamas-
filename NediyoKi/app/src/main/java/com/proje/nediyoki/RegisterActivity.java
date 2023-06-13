package com.proje.nediyoki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Toolbar actionbarRegister;
    private EditText tbxUserName, tbxEmail, tbxPassword, tbxPasswordReplay;
    private Button btnRegister;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    DatabaseReference mDatabase;
    DatabaseReference usersRef;


    private void init(){
        actionbarRegister = (Toolbar) findViewById(R.id.actionbarRegister);
        setSupportActionBar(actionbarRegister);
        getSupportActionBar().setTitle("Kayıt Ol");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        tbxUserName = (EditText) findViewById(R.id.tbxUserName);
        tbxEmail = (EditText) findViewById(R.id.tbxEmail);
        tbxPassword = (EditText) findViewById(R.id.tbxPassword);
        tbxPasswordReplay = (EditText) findViewById(R.id.tbxPasswordConfirm);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        usersRef = mDatabase.child("users");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createAccount();
            }
        });
    }

    private void createAccount() {
        String userName = tbxUserName.getText().toString();
        String email = tbxEmail.getText().toString();
        String password = tbxPassword.getText().toString();
        String passwordReplay = tbxPasswordReplay.getText().toString();

        if (TextUtils.isEmpty(userName)){
            Toast.makeText(this, "Kullanıcı ismi Boş Geçilemez.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email Alanı Boş Geçilemez.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password) || password.length() < 8){
            Toast.makeText(this, "Sifre Boş Veya 8 Karakterden Kısa Geçilemez.", Toast.LENGTH_SHORT).show();
        }
        else if (! password.equals(passwordReplay)){
            Toast.makeText(this, "Lütfen şifrenizi Doğrulayın.", Toast.LENGTH_SHORT).show();
        }else{
            btnRegister.setEnabled(false);

            firebaseAuth.createUserWithEmailAndPassword(email, passwordReplay).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        currentUser = firebaseAuth.getCurrentUser();
                        String uid = currentUser.getUid();
                        User user = new User(uid, userName, email, "Merhaba Ben NediyoKi App Kullanıyorum.");
                        usersRef.child(uid).setValue(user);
                        Toast.makeText(RegisterActivity.this, "Hesabınız Başarılı Bir Şekilde Oluşturuldu.", Toast.LENGTH_SHORT).show();
                        Intent intentLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intentLogin);
                        finish();
                    }else {
                        String s = task.getException().toString();
                        Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        tbxUserName.setText(s);
                        btnRegister.setEnabled(true);
                    }
                }

            });
        }
    }
}