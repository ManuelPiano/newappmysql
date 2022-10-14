package com.itca.appmysql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegistrarUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
    }

    public void OnClick(View view){
        Intent miIntent=null;
        switch (view.getId()){
            case R.id.btn_Login:
                miIntent=new Intent(RegistrarUsuario.this,Login.class);
                break;
            case R.id.btn_registrar:
                miIntent=new Intent(RegistrarUsuario.this,MainActivity.class);
                break;
        }
        if (miIntent!=null){
            startActivity(miIntent);
        }
    }
}