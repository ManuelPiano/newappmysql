package com.itca.appmysql.ui.categorias;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.ScrollingTabContainerView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Categorias extends Fragment {
    private EditText etid, etnombre;
    private Spinner sp1;
    private Button btncat,btnNewc, btnmod, btneli;
    private TextView tvrespuesta;
    private ListView listC;

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
        View root = inflater.inflate(R.layout.fragment_categorias, container, false);
        View list = inflater.inflate(R.layout.fragment_q_categorias, container, false);

        etid = root.findViewById(R.id.etid);
        etnombre = root.findViewById(R.id.etnombre);
        sp1 = root.findViewById(R.id.sp1);
        btncat = root.findViewById(R.id.btncat);
        btnmod = root.findViewById(R.id.btnedit);
        btneli = root.findViewById(R.id.btneli);
        btnNewc = root.findViewById(R.id.btnNew);
        tvrespuesta = root.findViewById(R.id.tvrespuesta);
        listC = list.findViewById(R.id.Viewcate);


        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp1.getSelectedItemPosition() > 0) {
                    datoSelect = sp1.getSelectedItem().toString();
                } else {
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
        btnNewc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_categoria();
            }
        });




        btnmod.setOnClickListener(new View.OnClickListener() {
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
                    modificarCategoria(getContext(), Integer.parseInt(idcategoria), nombrecat, Integer.parseInt(estado));
                }
            }
        });
        btneli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "Clic en botón Guardar", Toast.LENGTH_SHORT).show();
                //recibirJson(getContext());
                String idcategoria = etid.getText().toString();


                if(idcategoria.length() == 0){
                    etid.setError("Campo obligatorio");

                }else{
                    eliminarCategoria(getContext(), Integer.parseInt(idcategoria));
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
        String url = "https://salva10012002.000webhostapp.com/service/guardar_categorias.php";
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
                Toast.makeText(context, "No se puede guardar. \n" +"Intentelo más tarde.", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //En este método se colocan o se setean los valores a recibir por el fichero *.php
            Map<String, String> map = new HashMap<>();
            map.put("Content-Type", "application/json; charset=utf-8");
            map.put("Accept", "application/json");
            map.put("id_categoria", String.valueOf(id_categoria));
            map.put("nom_categoria", nom_categoria);
            map.put("estado_categoria", String.valueOf(estado_categoria));
            return map;
        }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
}
    private void new_categoria() {
        etid.setText(null);
        etnombre.setText(null);
        sp1.setSelection(0);

    }
    private void eliminarCategoria(final Context context, final int id_categoria) {
        String url = "https://salva10012002.000webhostapp.com/service/eliminar_categoria.php";
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
                Toast.makeText(context, "No se puede eliminar. \n" +"Intentelo más tarde.", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //En este método se colocan o se setean los valores a recibir por el fichero *.php
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("id_categoria", String.valueOf(id_categoria));

                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);

}
    private void modificarCategoria(final Context context, final int id_categoria, final String nom_categoria, final int estado_categoria) {
        String url = "https://salva10012002.000webhostapp.com/service/actualizar_categorias.php";
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
                Toast.makeText(context, "No se pudo modificar. \n" +"Intentelo más tarde.", Toast.LENGTH_SHORT).show();
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
