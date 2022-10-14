package com.itca.appmysql;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void OnClick(View view){
        Intent miIntent=null;
        switch (view.getId()){
            case R.id.btnRegistrarUsuario:
                miIntent=new Intent(Login.this,RegistrarUsuario.class);
                break;
            case R.id.btn_login:
                miIntent=new Intent(Login.this,MainActivity.class);
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Por favor espera...");
                progressDialog.show();
                break;
        }
        if (miIntent!=null){
            startActivity(miIntent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            new android.app.AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_close)
                    .setTitle("Warning")
                    .setMessage("¿Realmente desea salir?")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                        }
                    })
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}