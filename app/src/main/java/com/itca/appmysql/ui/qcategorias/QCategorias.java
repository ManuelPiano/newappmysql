package com.itca.appmysql.ui.qcategorias;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.itca.appmysql.MySingleton;
import com.itca.appmysql.R;
import com.itca.appmysql.Setting_VAR;
import com.itca.appmysql.dto_categorias;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class QCategorias extends Fragment {
    private ListView listC;
    String idcategoria = "";
    String nombrecategoria = "";
    String estado = "";
    int conta = 0;
    ArrayList<String> lista = null;
    ArrayList<dto_categorias> listaCategorias;


    public QCategorias() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view = inflater.inflate(R.layout.fragment_q_categorias, container, false);
        listC(getContext());
        listC = view.findViewById(R.id.Viewcate);

                listC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (conta >= 1 && listC.getSelectedItemPosition() > 0) {
                            String item_spinner = listC.getSelectedItem().toString();
                            //Hago una busqueda en la cadena seleccionada en el spinner para separar el idcategoria y el nombre de la categoria
                            //Esto es necesario, debido a que lo que debe enviarse a guardar a la base de datos es únicamente el idcategoria.
                            String s[] = item_spinner.split("~");
                            //Dato ID CATEGORIA
                            idcategoria = s[0].trim();
                            //Con trim elimino espacios al inicio y final de la cadena para enviar limpio el ID CATEGORIA.
                            //Dato NOMBRE DE LA CATEGORIA
                            nombrecategoria = s[1];
                            estado = s[2];
                            Toast toast = Toast.makeText(getContext(),
                                    "Id cat: " + idcategoria + "\n" +
                                            "\"Nombre Categoria" + nombrecategoria + "\n" +
                                            "Estado" + estado, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            idcategoria = "";
                            nombrecategoria = "";
                        }
                        conta++;

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        return view;
    }

    public void listC(final Context context) {
        listaCategorias = new ArrayList<dto_categorias>();
        lista = new ArrayList<String>();

        String url = Setting_VAR.URL_consultaALLCategorias;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    int totalEncontrados = array.length();
                    //Toast.makeText(context, "Total:"+totalEncontrados, Toast.LENGTH_SHORT).show();
                    dto_categorias obj_categorias = null;
                    //dto_categorias obj_categorias = new dto_categorias();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject categoriasObject = array.getJSONObject(i);
                        int id_categoria = categoriasObject.getInt("id_categoria");
                        String nombre_categoria = categoriasObject.getString("nom_categoria");
                        int estado_categoria = Integer.parseInt(categoriasObject.getString("estado_categoria"));
                        //Encapsulo registro por registro encontrado dentro del objeto de manera temporal
                        obj_categorias = new dto_categorias(id_categoria, nombre_categoria, estado_categoria);

                        /*obj_categorias.setId_categoria(id_categoria);

                        obj_categorias.setNom_categoria(nombre_categoria);

                        obj_categorias.setEstado_categoria(estado_categoria);*/
                        //Agrego todos los registros en el arraylist
                        listaCategorias.add(obj_categorias);
                        //Saco la información del arraylist y personalizo la forma en que deseo se muestren los datos en el spinner y
                        //Selecciono que datos se van a mostrar del resultado.

                        lista.add(listaCategorias.get(i).getId_categoria() + " ~ " + listaCategorias.get(i).getNom_categoria());
                        //Creo un adaptador para cargar la lista preparada anteriormente.
                        // ArrayAdapter<String> adaptador = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, obtenerListaCategorias());
                        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, lista);
                        //Cargo los datos en el Spinner
                        listC.setAdapter(adaptador);
                        //Muestro datos en LogCat para verificar la respuesta obtenida desde el servidor.
                        Log.i("Id Categoria",
                                String.valueOf(obj_categorias.getId_categoria()));
                        Log.i("Nombre Categoria",
                                obj_categorias.getNom_categoria());
                        Log.i("Estado Categoria",
                                String.valueOf(obj_categorias.getEstado_categoria()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error. Compruebe su acceso a Internet.", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


}