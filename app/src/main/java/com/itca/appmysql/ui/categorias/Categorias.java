package com.itca.appmysql.ui.categorias;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.itca.appmysql.MySingleton;
import com.itca.appmysql.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Categorias extends Fragment {
    private EditText etid, etnombre;
    private Spinner sp1;
    private Button btncat;
    private TextView tvrespuesta;

    String datoSelect = "";

    public Categorias() {
    }


    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_categorias, container, false);

        etid = root.findViewById(R.id.etid);
        etnombre = root.findViewById(R.id.etnombre);
        sp1 = root.findViewById(R.id.sp1);
        btncat = root.findViewById(R.id.btncat);
        tvrespuesta = root.findViewById(R.id.tvrespuesta);


        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(sp1.getSelectedItemPosition()>0) {
                    datoSelect = sp1.getSelectedItem().toString();
                }else{
                    datoSelect = "";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btncat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "Clic en botón Guardar", Toast.LENGTH_SHORT).show();
                //recibirJson(getContext());
                String idcategoria = etid.getText().toString();
                String nombrecat = etnombre.getText().toString();
                String estado = datoSelect;

                String dato = "";

                if(idcategoria.length() == 0){
                    etid.setError("Campo obligatorio");
                }else if(nombrecat.length()==0){
                    etnombre.setError("Campo obligatorio");
                }else if(estado.length()==0){
                    dato = "Debe seleccionar una ópcion";
                    Toast.makeText(getContext(), ""+dato, Toast.LENGTH_SHORT).show();
                }else{
                    guardarcategoria(getContext(), Integer.parseInt(idcategoria), nombrecat, Integer.parseInt(estado));
                }

            }
        });


        return root;
    }


    private void guardar(){

    }

    private boolean estado(){

        return true;
    }

    public void recibirJson(final Context context){

        String url = "https://servicestechnology.com.sv/ws/json1.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject respuestaJSON = new JSONObject(response.toString());
                    String dato1 = respuestaJSON.getString("idcategoria");
                    String dato2 = respuestaJSON.getString("nombre");
                    Toast.makeText(context, "Datos recibidos: \n" +"Id: " + dato1 + ".\n" + "nombre:"+dato2, Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Respuesta: " + response.toString(), Toast.LENGTH_SHORT).show();

                    //tvid.setText("Id categoria: " + dato1);
                    //tvnombre.setText("Nombre categoria: " + dato2);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "ERROR. Verifque su conexión.", Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }


    private void guardarcategoria(final Context context, final int id_categoria, final String nom_categoria, final int estado_categoria) {
        String url = "https://noegarciasis11b.000webhostapp.com/service/guardar_categorias.php";
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
            map.put("id", String.valueOf(id_categoria));
            map.put("nombre", nom_categoria);
            map.put("estado", String.valueOf(estado_categoria));
            return map;
        }
    };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}