package com.itca.appmysql.ui.qproductos;

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.itca.appmysql.MySingleton;
import com.itca.appmysql.R;
import com.itca.appmysql.Setting_VAR;
import com.itca.appmysql.dto_categorias;
import com.itca.appmysql.dto_productos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Qproductos extends Fragment {
    private ListView listP;
    String idProduc = "";
    String nom_produc = "";
    String des_produc = "";
    String stock = "";
    String precio ="";
    String unidad_medida="";
    String estado_pro="";
    String cat ="";

    int conta = 0;
    ArrayList<String> listaPro = null;
    ArrayList<dto_productos> listaProducto;


    public Qproductos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qproductos, container, false);
        listP(getContext());
        listP = view.findViewById(R.id.ViewProduc);

        listP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (conta >= 1 && listP.getSelectedItemPosition() > 0) {
                    String item_spinner = listP.getSelectedItem().toString();
                    //Hago una busqueda en la cadena seleccionada en el spinner para separar el idcategoria y el nombre de la categoria
                    //Esto es necesario, debido a que lo que debe enviarse a guardar a la base de datos es únicamente el idcategoria.
                    String s[] = item_spinner.split("~");
                    //Dato ID CATEGORIA
                    idProduc = s[0].trim();
                    //Con trim elimino espacios al inicio y final de la cadena para enviar limpio el ID CATEGORIA.
                    //Dato NOMBRE DE LA CATEGORIA
                    nom_produc = s[1];
                    des_produc=s[2];
                    cat=s[7];

                    Toast toastP = Toast.makeText(getContext(),
                            "Id producto: " + idProduc + "\n" +
                                    "Nombre Producto: " + nom_produc + "\n" +
                            "Categoria: " + cat, Toast.LENGTH_SHORT);
                    toastP.setGravity(Gravity.CENTER, 0, 0);
                    toastP.show();
                } else {
                    idProduc = "";
                    nom_produc = "";
                    cat="";
                }
                conta++;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }


    public void listP(final Context context) {
        listaProducto = new ArrayList<dto_productos>();
        listaPro = new ArrayList<String>();

        String url = Setting_VAR.URL_consultaALLProducto;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    int totalEncontrados = array.length();
                    //Toast.makeText(context, "Total:"+totalEncontrados, Toast.LENGTH_SHORT).show();
                    dto_productos obj_productos = null;
                    //dto_categorias obj_categorias = new dto_categorias();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject productosObject = array.getJSONObject(i);
                        int id_producto = productosObject.getInt("id_producto");
                        String nom_producto = productosObject.getString("nom_producto");
                        String des_producto = productosObject.getString("des_producto");
                        double stock = productosObject.getDouble("stock");
                        double precio = productosObject.getDouble("precio");
                        String unidad_medida = productosObject.getString("unidad_medida");
                        int estado_producto = productosObject.getInt("estado_producto");
                        int categoria = productosObject.getInt("categoria");


                        //Encapsulo registro por registro encontrado dentro del objeto de manera temporal
                        obj_productos = new dto_productos(id_producto, nom_producto, des_producto, stock, precio, unidad_medida, estado_producto, categoria);

                        //obj_productos.setId_producto(id_producto);

                       // obj_categorias.setNom_categoria(nombre_categoria);

                        //obj_categorias.setEstado_categoria(estado_categoria);
                        //Agrego todos los registros en el arraylist
                        listaProducto.add(obj_productos);
                        //Saco la información del arraylist y personalizo la forma en que deseo se muestren los datos en el spinner y
                        //Selecciono que datos se van a mostrar del resultado.

                        listaPro.add(listaProducto.get(i).getId_producto() + " ~ " + listaProducto.get(i).getNom_producto()+ " ~ " +
                                listaProducto.get(i).getCategoria());
                        //Creo un adaptador para cargar la lista preparada anteriormente.
                        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, listaPro);

                        listP.setAdapter(adaptador);
                        //Muestro datos en LogCat para verificar la respuesta obtenida desde el servidor.
                        Log.i("id_producto", String.valueOf(obj_productos.getId_producto()));
                        Log.i("nom_producto", obj_productos.getNom_producto());
                        Log.i("des_producto", obj_productos.getDes_producto());
                        Log.i("stock", String.valueOf(obj_productos.getStock()));
                        Log.i("precio", String.valueOf(obj_productos.getPrecio()));
                        Log.i("unidad_medida", obj_productos.getUnidad_medida());
                        Log.i("estado_producto", String.valueOf(obj_productos.getEstado_producto()));
                        Log.i("categoria", String.valueOf(obj_productos.getCategoria()));
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