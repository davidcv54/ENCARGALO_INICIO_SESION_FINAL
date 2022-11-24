package mx.com.encargalo.tendero.Inicio_sesion.ui.Mis_ordenes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mx.com.encargalo.Adapters.mio_adaprvOrdenesOrden;
import mx.com.encargalo.Model.mio_mdlOrdenesOrden;
import mx.com.encargalo.R;
import mx.com.encargalo.Utils.Util;


public class mio_frgmisordenesprincipal extends Fragment  {

    RecyclerView mio_moprclvlistaproductos;
    RequestQueue requestQueue;
    ArrayList<mio_mdlOrdenesOrden> ArrayOrden;
    Spinner mio_mopspnordenarporestado, mio_mopspnordenarporfecha;
    mio_adaprvOrdenesOrden adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_mio_frgmisordenesprincipal, container, false);
        mio_moprclvlistaproductos = vista.findViewById(R.id.mio_moprclvlistaproductos);
        mio_mopspnordenarporestado = vista.findViewById(R.id.mio_mopspnordenarporestado);
        mio_mopspnordenarporfecha = vista.findViewById(R.id.mio_mopspnordenarporfecha);

        ArrayOrden = new ArrayList<>();

        mio_moprclvlistaproductos.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mio_moprclvlistaproductos.setHasFixedSize(true);
        requestQueue = Volley.newRequestQueue(getContext());

        String urlDefecto = Util.RUTA+"c_lista_ordenes_mis_ordenes.php?sp_idTienda=4&sp_idOrden=0&sp_odEstado=%&sp_odFechaPedido=DESC";



        mio_mopspnordenarporestado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String Estado = mio_mopspnordenarporestado.getItemAtPosition(i).toString();

                if(Estado.equals("ESTADO")) {

                    if(ArrayOrden == null || ArrayOrden.size() == 0)
                    {
                        webServicesOrdenes(urlDefecto);
                    }
                    else{
                        ArrayOrden.clear();
                        webServicesOrdenes(urlDefecto);
                        adapter.notifyDataSetChanged();
                    }


                }else{

                    String urlPorEstado = Util.RUTA+"c_lista_ordenes_mis_ordenes.php?sp_idTienda=4&sp_idOrden=0&sp_odEstado="+Estado+"&sp_odFechaPedido=DESC";

                    ArrayOrden.clear();
                    webServicesOrdenes(urlPorEstado);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return vista;
    }

    private void webServicesOrdenes(String URL) {


        URL.replace(" ", "%20");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mio_mdlOrdenesOrden orden = null;
                JSONArray json = response.optJSONArray("lista_ordenes");
                try {
                    for (int i = 0; i < json.length(); i++) {
                        orden = new mio_mdlOrdenesOrden();

                        JSONObject jsonObject = null;
                        jsonObject = json.getJSONObject(i);
                        orden.setIdOrden(jsonObject.optInt("idOrden"));
                        orden.setOdFechaPedido(jsonObject.optString("odFechaPedido"));
                        orden.setPerNombreCompleto(jsonObject.optString("perNombreCompleto"));
                        orden.setOdEstado(jsonObject.optString("odEstado"));
                        //sin en caso no existe repartidor
                        orden.setIdRepartidor(0);
                        ArrayOrden.add(orden);
                    }


                    adapter = new mio_adaprvOrdenesOrden(getContext(), ArrayOrden, new mio_adaprvOrdenesOrden.SendData() {
                        @Override
                        public void sendInfo(String [] datosOrden) {

                            Bundle bundle = new Bundle();
                            bundle.putString("idOrden", datosOrden[0]);
                            bundle.putString("perNombreCompleto", datosOrden[1]);
                            getParentFragmentManager().setFragmentResult("detallesPedido", bundle);

                        }
                    });


                    adapter.notifyDataSetChanged();

                    mio_moprclvlistaproductos.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ParseError) {
                    Toast.makeText(getContext(), "No existe registro", Toast.LENGTH_SHORT).show();
                }
            }
        });
        requestQueue.add(jsonObjectRequest);


    }




}