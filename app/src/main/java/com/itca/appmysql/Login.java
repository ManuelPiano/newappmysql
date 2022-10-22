package com.itca.appmysql;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private Button btnLog;
    private EditText etuser, etclave;
    private dto_user usuario1 = new dto_user();
    private Switch mantener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLog = findViewById(R.id.btn_log);
        etuser = findViewById(R.id.etuser);
        etclave = findViewById(R.id.etclave);
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usu = etuser.getText().toString();
                String contra = etclave.getText().toString();

                if(usu.length() == 0){
                    etuser.setError("Campo obligatorio");
                }else if(contra.length() == 0){
                    etclave.setError("Campo obligatorio");
                }else {
                    if(usu.contains("@")){
                        usuario1.setCorreo(usu);
                    }else{
                        usuario1.setUsuario(usu);
                    }
                    usuario1.setClave(contra);

                    login(Login.this, usuario1, mantener);
                    dto_user usua = new dto_user();
                    SharedPreferences sp = getSharedPreferences("usuario", Context.MODE_PRIVATE);
                    String estado = sp.getString("estado", "");
                    if(estado.equals("logON")){
                        if(sp.contains("id")){
                            String id = sp.getString("id", "");
                            usua.setId(Integer.parseInt(id));
                            if(usua.getId() > 0){
                                Toast.makeText(Login.this, "Su id es: " + usua.getId(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        Intent nueva = new Intent(Login.this, MainActivity.class);
                        startActivity(nueva);
                    }

                }
            }
        });
    }


    public void OnClick(View view){
        Intent miIntent=null;
        switch (view.getId()){
            case R.id.btnRegistrarUsuario:
                miIntent=new Intent(Login.this,RegistrarUsuario.class);
                break;
            case R.id.btn_log:
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

    public void login(final Context context, dto_user usuario, Switch mantener) {
        String url = "https://salva10012002.000webhostapp.com/service/login.php";
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject requestJSON = new JSONObject(response.toString());
                    if(requestJSON.has("mensaje") == false){
                        String id = requestJSON.getString("id");
                        String usuario = requestJSON.getString("usuario");
                        String tipo = requestJSON.getString("tipo");

                        Intent nueva = new Intent(Login.this, MainActivity.class);
                        startActivity(nueva);


                        if(id.length() > 0){
                            Toast.makeText(context, "¡Bienvenido!", Toast.LENGTH_SHORT).show();
                            SharedPreferences spUsuario = context.getSharedPreferences("usuario", context.MODE_PRIVATE);
                            String estado = "logON";
                            SharedPreferences.Editor editor = spUsuario.edit();
                            editor.putString("estado", estado);
                            //Si se establecio la opcion de mantener iniciada sesion
                            if(mantener.isChecked()){

                                editor.putString("id", id);


                            }
                            editor.putString("nickName", usuario);
                            editor.putString("tipo", tipo);
                            editor.commit();


                        }else {
                            Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();

                        }
                    }else{
                        String mensaje = requestJSON.getString("mensaje");
                        Toast.makeText(context, "" + mensaje, Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "No se pudo iniciar session. \n" +"Intentelo más tarde." + volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //En este método se colocan o se setean los valores a recibir por el fichero *.php
                Map<String, String> map = new HashMap<>();
                if(usuario.getUsuario() != null){
                    map.put("Content-Type", "application/json; charset=utf-8");
                    map.put("Accept", "application/json");
                    map.put("usuario", usuario.getUsuario());
                    map.put("correo", "");
                    map.put("clave", usuario.getClave());
                }else if(usuario.getCorreo() != ""){
                    map.put("Content-Type", "application/json; charset=utf-8");
                    map.put("Accept", "application/json");
                    map.put("usuario", "");
                    map.put("correo", usuario.getCorreo());
                    map.put("clave", usuario.getClave());
                }

                return map;
            }
        };
        Log.e("URL", request.getUrl().toString());
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}