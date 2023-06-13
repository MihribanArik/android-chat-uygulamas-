package com.proje.nediyoki;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin, btnRegister; // Hesaba giriş yap ve hesap oluştur buttonşları.

    public void init(){
        btnLogin = (Button) findViewById(R.id.btnLogin); // login buttonunun bağlanması.
        btnRegister = (Button) findViewById(R.id.btnRegister); // register buttonunun bağlanması.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init(); // 12. satırdaki init() çağırır.

        // login butonuna tıklandığında çalışacak.
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class); // login sayfasına yönlendirme yapacak intent oluşturma.
                startActivity(intentLogin);//oluşturduğumuz intenti çalıştırma.
                finish();
            }
        });

        // register buttonuna tıklandığında çalışacak.
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegister = new Intent(MainActivity.this, RegisterActivity.class); // register sayfasına yönlendirme yapacak intent oluşturma.
                startActivity(intentRegister);// Oluşturduğumuz intenti çalıştırma.
                finish();
            }
        });
    }
}