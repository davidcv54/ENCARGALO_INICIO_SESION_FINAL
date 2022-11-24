package mx.com.encargalo.tendero.Inicio_sesion.ui.Mis_ordenes;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import mx.com.encargalo.R;
import mx.com.encargalo.Utils.Util;

public class mio_frgdetallepedido extends Fragment {
    Button dpbtndetalle,dpbtnchat;
    TextView mio_txtnumpedido,mio_txtLdetalllecliente,  mio_dpedtdireccionentrega;
    RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_mio_frgdetallepedido, container, false);
        dpbtndetalle= vista.findViewById(R.id.mio_dpbtndetalle);
        dpbtnchat=vista.findViewById(R.id.mio_dpbtnchat);

        mio_txtLdetalllecliente = vista.findViewById(R.id.mio_txtLdetalllecliente);
        mio_txtnumpedido = vista.findViewById(R.id.mio_txtnumpedido);
        mio_dpedtdireccionentrega = vista.findViewById(R.id.mio_dpedtdireccionentrega);
        requestQueue = Volley.newRequestQueue(getContext());


        //método para obtener los detalles del pedido que fueron enviado por el fragment anterior
        getParentFragmentManager().setFragmentResultListener("detallesPedido", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull @NotNull String requestKey, @NonNull @NotNull Bundle result) {
                String idOrden = result.getString("idOrden");
                String perNombreCompleto = result.getString("perNombreCompleto");
                mio_txtnumpedido.setText(idOrden);
                mio_txtLdetalllecliente.setText(perNombreCompleto);

                llamarWsUbicación(idOrden);

                //Navegación hacia lista de productos al hacer click en Botón productos
                dpbtndetalle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Enviamos la variable idOrden al fragment de detalles donde se muestran los productos
                        Bundle bundle = new Bundle();
                        bundle.putString("idOrden", idOrden);
                        getParentFragmentManager().setFragmentResult("detallesProducto", bundle);
                        Navigation.findNavController(view).navigate(R.id.nav_misordenesdetalleproducto);
                    }
                });
            }
        });
        dpbtndetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_misordenesdetalleproducto);
            }
        });

        dpbtnchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_misordenesconversacion);
            }
        });

        return vista;
    }

    private void llamarWsUbicación(String urlIdOrden) {


        String URL = Util.RUTA +"c_coordenadas_de_orden_entregada.php?idOrden="+urlIdOrden;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String latitud = response.getString("Latitud");
                            String longitud = response.getString("Longitud");

                            mio_dpedtdireccionentrega.setText(coordenadasDireccion(latitud, longitud));
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    //Función para transformar coordenadas a dirección
    private String coordenadasDireccion(String latitud, String longitud) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        String NombreDireccion = "";
        try{
            List<Address> listAddress = geocoder.getFromLocation(Double.parseDouble(latitud), Double.parseDouble(longitud), 1);
            NombreDireccion =  listAddress.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return NombreDireccion;
    }
}