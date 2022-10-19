package com.itca.appmysql;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegistrarUsuario extends AppCompatActivity {
    private EditText id, nombre, apellidos, email, user, clave, respuesta;
     private Spinner tipo, estado, pregunta;
     private TextView fecha_registro;
     private Button btnRegistrar;

    String datoSelectTipo = "";
    String datoSelectEstado = "";
    String datoSelectPregunta = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);

        id = (EditText) findViewById(R.id.edt_IdUser);
        nombre = (EditText) findViewById(R.id.edt_NomUser);
        apellidos = (EditText) findViewById(R.id.edt_ApellUser);
        email = (EditText) findViewById(R.id.edt_correoUser);
        user = (EditText) findViewById(R.id.edt_usuario);
        clave = (EditText) findViewById(R.id.edt_claveUser);
        pregunta = (Spinner) findViewById(R.id.sp_PreguntaUser);
        respuesta = (EditText) findViewById(R.id.edt_Respuesta);
        tipo = (Spinner) findViewById(R.id.sp_TipoUser);
        estado = (Spinner) findViewById(R.id.sp_EstadoUser);
        fecha_registro = (TextView) findViewById(R.id.tv_fechaRegistro);
        fecha_registro.setText(timedate());
        btnRegistrar = (Button) findViewById(R.id.btn_registrar);


        tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(tipo.getSelectedItemPosition()>0) {
                    datoSelectTipo = tipo.getSelectedItem().toString();
                }else{
                    datoSelectTipo = "";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(estado.getSelectedItemPosition()>0) {
                    datoSelectEstado = estado.getSelectedItem().toString();
                }else{
                    datoSelectEstado = "";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pregunta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(pregunta.getSelectedItemPosition()>0) {
                    datoSelectPregunta = pregunta.getSelectedItem().toString();
                }else{
                    datoSelectPregunta = "";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "Clic en botón Guardar", Toast.LENGTH_SHORT).show();
                //recibirJson(getContext());
                String iduser = id.getText().toString();
                String nombreUser = nombre.getText().toString();
                String apellUser = apellidos.getText().toString();
                String emailUser = email.getText().toString();
                String usuario = user.getText().toString();
                String claveU = clave.getText().toString();
                String tipo = datoSelectTipo;
                String estado = datoSelectEstado;
                String pregunta = datoSelectPregunta;
                String respuestaU = respuesta.getText().toString();

                String dato = "";

                if(iduser.length() == 0){
                    id.setError("Campo obligatorio");
                }else if(nombreUser.length()==0) {
                    nombre.setError("Campo obligatorio");
                }else if(apellUser.length()==0){
                    apellidos.setError("Campo obligatorio");
                }else if(emailUser.length()==0){
                    email.setError("Campo obligatorio");
                }else if(usuario.length()==0){
                    user.setError("Campo obligatorio");
                }else if(claveU.length()==0){
                    clave.setError("Campo obligatorio");
                }else if(tipo.length()==0){
                    dato = "Debe seleccionar una ópcion";
                    Toast.makeText(RegistrarUsuario.this, ""+dato, Toast.LENGTH_SHORT).show();
                }else{
                    guardarUser(RegistrarUsuario.this, Integer.parseInt(iduser), nombreUser, apellUser, emailUser, usuario, claveU, pregunta, respuestaU, Integer.parseInt(tipo), Integer.parseInt(estado));
                }
            }
        });

    }

    private String timedate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
        String fecha = sdf.format(cal.getTime());
        return fecha;
    }

    private void guardarUser(final Context context, final int id_user, final String nombreUser,
                             final String apellUser, final String emailUser, final String usuario,
                             final String claveU, final String pregunta, final String respuestaU, final int tipo, final int estado) {
        String url = "https://salva10012002.000webhostapp.com/service/guardar_usuario.php";
        //String url = "http://localhost/service/guardar_categorias.php";
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject requestJSON = null;
                try {
                    requestJSON = new JSONObject(response.toString());
                    String estado = requestJSON.getString("estado");
                    String mensaje = requestJSON.getString("mensaje");
                    if(estado.equals("1")){
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context, "Registro almacenado en MySQL.", Toast.LENGTH_SHORT).show();
                    }else if(estado.equals("2")){
                        Toast.makeText(context, ""+mensaje, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "No se puedo guardar. \n" +"Intentelo más tarde.", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //En este método se colocan o se setean los valores a recibir por el fichero *.php
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("id_usuario", String.valueOf(id_user));
                map.put("nom_usuario", nombreUser);
                map.put("apellido_usuarios", apellUser);
                map.put("correo", emailUser);
                map.put("usuario", usuario);
                map.put("clave", claveU);
                map.put("tipo", String.valueOf(tipo));
                map.put("estado", String.valueOf(estado));
                map.put("pregunta", String.valueOf(pregunta));
                map.put("respuesta", respuestaU);
                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

}