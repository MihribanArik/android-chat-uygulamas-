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

public class LoginActivity extends AppCompatActivity {

    private Toolbar actionbarLogin;
    private EditText tbxEmail, tbxPassword;
    private Button btnLogin;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    public void init(){
        actionbarLogin = (Toolbar) findViewById(R.id.actionbarLogin);
        setSupportActionBar(actionbarLogin);
        getSupportActionBar().setTitle("Giriş Yap");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        tbxEmail = (EditText) findViewById(R.id.tbxEmail);
        tbxPassword = (EditText) findViewById(R.id.tbxPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginAccount();

            }
        });
    }

    private void loginAccount() {
        String email = tbxEmail.getText().toString();
        String password = tbxPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email Alanı Boş Geçilemez.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Şifre Alanı Boş Geçilemez.", Toast.LENGTH_SHORT).show();
        }else {
            btnLogin.setEnabled(false);

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent intentHome = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intentHome);
                        finish();
                    }else {
                        btnLogin.setEnabled(true);
                        Toast.makeText(LoginActivity.this, "Hesabınız Açılırken Bir hata Oluştu.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}